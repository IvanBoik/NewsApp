package com.boiko.newsapp.presentation.personal_area

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.boiko.newsapp.presentation.navgraph.Route
import com.boiko.newsapp.presentation.onboarding.components.DateInput
import com.boiko.newsapp.presentation.onboarding.components.TextInput
import com.boiko.newsapp.util.Constants
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PersonalAreaScreen(
    viewModel: PersonalAreaViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var userData = viewModel.getUserData()
    val avatar by remember { mutableStateOf(viewModel.getAvatar()) }

    val email = remember { mutableStateOf(userData.email) }
    val nickname = remember { mutableStateOf(userData.nickname) }
    val birthday = remember { mutableStateOf(userData.birthday) }
    val password = remember { mutableStateOf("**********") }
    val newPassword = remember { mutableStateOf("") }
    val formattedBirthday by remember {
        derivedStateOf {
            val date = birthday.value
            if (date == LocalDate.now()) {
                ""
            }
            else {
                "${date.dayOfMonth} ${Constants.MONTHS[date.monthValue-1]} ${date.year}"
            }

        }
    }

    var userDataReadOnly by remember { mutableStateOf(true) }
    var passwordReadOnly by remember { mutableStateOf(true) }

    val isEmailValid = remember { mutableStateOf(true) }
    val isNicknameValid = remember { mutableStateOf(true) }
    val isBirthdayValid = remember { mutableStateOf(true) }
    val isPasswordValid = remember { mutableStateOf(false) }
    val isNewPasswordValid = remember { mutableStateOf(false) }

    var showEmailError by remember { mutableStateOf(false) }
    var showNicknameError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var showNewPasswordError by remember { mutableStateOf(false) }

    val birthdayDialogState = rememberMaterialDialogState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .clickable {
                            navController.navigate(Route.HomeScreen.route)
                        }
                )
                AsyncImage(
                    model = ImageRequest.Builder(context).data(avatar).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.Transparent,
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {
                    onClickEditAvatar(context)
                    navController.navigate(Route.CameraScreen.route)
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = "Edit",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 20.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(1.dp))
            Spacer(modifier = Modifier.height(5.dp))
            TextInput(
                value = email,
                placeholder = "Email",
                isValid = isEmailValid,
                readOnly = userDataReadOnly
            ) {
                EMAIL_ADDRESS.matcher(it).matches()
            }
            if (showEmailError) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Email must be valid",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                )
            }
            TextInput(
                value = nickname,
                placeholder = "Nickname",
                isValid = isNicknameValid,
                readOnly = userDataReadOnly
            ) {
                it.isNotBlank()
            }
            if (showNicknameError) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Nickname must be not empty",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                )
            }
            DateInput(
                value = birthday,
                readOnly = userDataReadOnly,
                formattedValue = formattedBirthday,
                placeholder = "Birthday",
                dialogState = birthdayDialogState,
                isValid = isBirthdayValid
            )
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {
                    if (userDataReadOnly) {
                        password.value = ""
                        userDataReadOnly = false
                        if (password.value != "**********") {
                            password.value = "**********"
                        }
                        showEmailError = false
                        showNicknameError = false
                        passwordReadOnly = true
                        isPasswordValid.value = true
                        isNewPasswordValid.value = true
                    }
                    else {
                        showEmailError = !isEmailValid.value
                        showNicknameError = !isNicknameValid.value

                        if (isEmailValid.value && isNicknameValid.value) {

                            viewModel.onEvent(
                                PersonalAreaEvent.UpdateDataEvent(
                                    email = email.value,
                                    nickname = nickname.value,
                                    birthday = birthday.value
                                )
                            )
                            userDataReadOnly = true
                        }
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = if (userDataReadOnly) "Edit" else "Save",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 20.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(1.dp))
            Spacer(modifier = Modifier.height(5.dp))
            TextInput(
                value = password,
                readOnly = passwordReadOnly,
                placeholder = "Password",
                visualTransformation = PasswordVisualTransformation(),
                isValid = isPasswordValid
            ) {
                it.length >= 8
            }
            if (showPasswordError) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Password must contain at least 8 characters",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                )
            }
            if (!passwordReadOnly) {
                TextInput(
                    value = newPassword,
                    readOnly = false,
                    placeholder = "New Password",
                    visualTransformation = PasswordVisualTransformation(),
                    isValid = isNewPasswordValid
                ) {
                    it.length >= 8
                }
            }
            if (showPasswordError) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "New password must contain at least 8 characters",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {
                    if (passwordReadOnly) {
                        password.value = ""
                        passwordReadOnly = false

                        userData = viewModel.getUserData()
                        email.value = userData.email
                        nickname.value = userData.nickname
                        birthday.value = userData.birthday
                        isEmailValid.value = true
                        isNicknameValid.value = true
                        showEmailError = false
                        showNicknameError = false
                        userDataReadOnly = true
                    }
                    else {
                        showPasswordError = !isPasswordValid.value
                        showNewPasswordError = !isNewPasswordValid.value

                        if (isPasswordValid.value && isNewPasswordValid.value) {
                            try {
                                viewModel.onEvent(
                                    PersonalAreaEvent.UpdatePasswordEvent(
                                        oldPassword = password.value,
                                        newPassword = newPassword.value
                                    )
                                )
                                passwordReadOnly = true
                                password.value = newPassword.value
                            }
                            catch (e: Throwable) {
                                Toast.makeText(context, "Invalid password", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = if (passwordReadOnly) "Edit" else "Save",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 20.sp
                    )
                )
            }
        }
        Button(
            onClick = {
                viewModel.onEvent(PersonalAreaEvent.LogOutEvent)
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Text(
                text = "Log out",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )
            )
        }

    }
}

private fun onClickEditAvatar(context: Context) {
    if (!hasRequiredPermissions(context)) {
        ActivityCompat.requestPermissions(
            context as Activity, Permissions.CAMERAX_PERMISSIONS, 0
        )
    }
}

private fun hasRequiredPermissions(context: Context): Boolean {
    return Permissions.CAMERAX_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

object Permissions {
    val CAMERAX_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA
    )
}