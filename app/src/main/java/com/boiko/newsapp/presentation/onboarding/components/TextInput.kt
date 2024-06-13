package com.boiko.newsapp.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextInput(
    value: MutableState<String>,
    readOnly: Boolean = false,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isValid: MutableState<Boolean>,
    validate: (String) -> Boolean
) {
    Spacer(modifier = Modifier.height(16.dp))
    //TODO show error message when value is not valid

    BasicTextField(
        value = value.value,
        onValueChange = { value.value = it },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(50.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color.Transparent),
        decorationBox = {innerTextField ->
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row {
                    Spacer(modifier = Modifier.width(24.dp))
                    if (value.value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        )
                    }
                    else {
                        innerTextField()
                    }
                }

                if (validate(value.value)) {
                    isValid.value = true
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(end = 24.dp)
                    )
                }
                else {
                    isValid.value = false
                }
            }
        }
    )
}