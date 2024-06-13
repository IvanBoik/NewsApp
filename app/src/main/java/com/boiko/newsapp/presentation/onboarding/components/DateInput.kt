package com.boiko.newsapp.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate

@Composable
fun DateInput(
    value: MutableState<LocalDate>,
    readOnly: Boolean = false,
    formattedValue: String,
    placeholder: String,
    dialogState: MaterialDialogState,
    isValid: MutableState<Boolean>
) {
    val interactionSource = remember { MutableInteractionSource() }

    Spacer(modifier = Modifier.height(16.dp))
    //TODO show error message when value is not valid
    BasicTextField(
        value = formattedValue,
        onValueChange = {},
        enabled = false,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(46.dp)
            .background(Color.Transparent, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if (!readOnly) {
                    dialogState.show()
                }
              },
        decorationBox = {innerTextField ->
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent, RoundedCornerShape(16.dp))
            ){
                Row {
                    Spacer(modifier = Modifier.width(24.dp))
                    if (formattedValue.isEmpty()) {
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
                if (formattedValue.isNotEmpty()) {
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
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            negativeButton(
                text = "Cancel",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        datepicker(
            initialDate = value.value,
            title = "Pick a date",
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.background,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.onBackground,
                dateActiveTextColor = MaterialTheme.colorScheme.background,
                dateInactiveTextColor = MaterialTheme.colorScheme.onBackground,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onBackground,
                headerTextColor = MaterialTheme.colorScheme.onBackground
            ),
            yearRange = IntRange(start = 1900, endInclusive = 2023)
        ) {
            value.value = it
        }
    }
}