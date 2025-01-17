package com.example.sharecab.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

//=======================
// DropdownMenu
//=======================
@Composable
fun DropdownMenuButton(
    options: List<String>,
    defaultValue: String,
    onValueChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(defaultValue) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        Button(
            onClick = {
                focusManager.clearFocus()   // Clear focus to hide the keyboard, then toggle menu.
                expanded = !expanded
            },
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(30)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = selectedOption, color = Color.Black)
                Spacer(Modifier.width(3.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        onValueChanged(option)
                        expanded = false
                    },
                    text = {
                        Text(text = option)
                    }
                )
            }
        }
    }
}