package com.boiko.newsapp.presentation.onboarding.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boiko.newsapp.R
import com.boiko.newsapp.util.Constants.MONTHS
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpBottomSheet(
    sheetState: SheetState,
    isClickOnSignIn: MutableState<Boolean>
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val nickname = remember { mutableStateOf("") }
    val birthday = remember { mutableStateOf(LocalDate.now()) }
    val formattedBirthday by remember {
        derivedStateOf {
            val date = birthday.value
            if (date == LocalDate.now()) {
                ""
            }
            else {
                "${date.dayOfMonth} ${MONTHS[date.monthValue-1]} ${date.year}"
            }

        }
    }
    val birthdayDialogState = rememberMaterialDialogState()

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxHeight(0.85f)) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
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
                            scope.launch {
                                focusManager.clearFocus()
                                sheetState.hide()
                            }
                        }
                )
                Text(
                    text = "Sign up",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.Transparent,
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Sign up with",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ThirdServiceButton(icon = R.drawable.ic_google) {

                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            TextInput(value = email, placeholder = "Email") {
                //TODO put email pattern
                it.isNotEmpty()
            }
            TextInput(
                value = password,
                placeholder = "Password",
                visualTransformation = PasswordVisualTransformation()
            ) {
                //TODO put password pattern (min 8 characters, with numbers and letters)
                it.isNotEmpty()
            }
            TextInput(value = nickname, placeholder = "Nickname") {
                it.isNotEmpty()
            }
            DateInput(
                value = birthday,
                formattedValue = formattedBirthday,
                placeholder = "Birthday",
                dialogState = birthdayDialogState
            )
            Spacer(modifier = Modifier.height(44.dp))
            Button(
                onClick = {
//                    viewModel.onEvent(AuthUiEvent.SignUp(
//                        email = email.value,
//                        password = password.value,
//                        nickname = nickname.value,
//                        birthday = birthday.value
//                    ))
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(46.dp)
            ) {
                Text(
                    text = "Sign up",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = "Log in",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.clickable {
                        isClickOnSignIn.value = true
                    }
                )
            }
        }
    }
}