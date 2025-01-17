package com.example.sharecab.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch


@Composable
fun CustomTimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeConfirm: (String) -> Unit,
    initialHour: Int = 6,
    initialMinute: Int = 0,
    initialAmPmIndex: Int = 0
) {
    // Local state for the selected time
    var hour by remember { mutableIntStateOf(initialHour) }
    var minute by remember { mutableIntStateOf(initialMinute) }
    var amPmIndex by remember { mutableIntStateOf(initialAmPmIndex) }

    Dialog(onDismissRequest = onDismissRequest) {
        // You can style the dialog background however you like:
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White) // example dark background
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Title
                Text(
                    text = "Set Time",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))

                // Our time wheel picker
                TimeWheelPicker(
                    initialHourIndex = hour - 1,            // 1..12 => index = hour - 1
                    initialMinuteIndex = minute,            // 0..59 => index = minute
                    initialAmPmIndex = amPmIndex,           // 0 = AM, 1 = PM
                    onTimeSelected = { h, m, amPm ->
                        hour = h
                        minute = m
                        amPmIndex = if (amPm == "AM") 0 else 1
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val amPmText = if (amPmIndex == 0) "AM" else "PM"
                            val timeString = "${hour}:${minute.toString().padStart(2, '0')} $amPmText"
                            onTimeConfirm(timeString)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelPicker(
    rawItems: List<String>,
    modifier: Modifier = Modifier,
    visibleCount: Int = 5,
    // You can keep this if you want to know the final “selected” index after scrolling:
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    circular: Boolean = false
) {
    // -- Repeated “circular” list logic --
    val repeatCount = 100
    val items = if (circular) {
        List(repeatCount) { rawItems }.flatten()
    } else {
        rawItems
    }
    val baseSize = rawItems.size

    // -- Place user near the middle of the repeated list --
    val initialIndex = if (circular) {
        (repeatCount / 2) * baseSize + selectedIndex
    } else {
        selectedIndex
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(listState)
    val scope = rememberCoroutineScope()

    // 1. Real-time computed center item
    //    We track which item is physically in the center of the viewport.
    val centerItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) return@derivedStateOf null

            // The “center” of the viewport in pixels
            val centerPx = layoutInfo.viewportStartOffset +
                    (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2f

            // Find which visible item is closest to that centerPx
            visibleItems.minByOrNull { item ->
                val itemCenter = item.offset + (item.size / 2f)
                kotlin.math.abs(itemCenter - centerPx)
            }?.index  // This is the *big list* index
        }
    }

    // 2. After scrolling stops, figure out the final “real” item index and notify parent
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerItem = listState.firstVisibleItemIndex
            val realIndex = centerItem % baseSize
            onSelectedIndexChange(realIndex)

            // If circular, re-center so user doesn’t drift infinitely
            if (circular) {
                val middleCenter = (repeatCount / 2) * baseSize + realIndex
                if (centerItem != middleCenter) {
                    listState.scrollToItem(middleCenter)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = snapFlingBehavior,
            contentPadding = PaddingValues(vertical = 20.dp * (visibleCount / 2)),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            itemsIndexed(items) { index, item ->

                // If this item’s big-list index == the centerItemIndex,
                // we consider it “bolded” in real time.
                val isCenter = (index == centerItemIndex)

                Text(
                    text = item,
                    fontSize = if (isCenter) 24.sp else 18.sp,
                    color = if (isCenter) Color.Black else Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable {
                            scope.launch {
                                listState.scrollToItem(index)
                            }
                        },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}





//===========================================
@Composable
fun TimeWheelPicker(
    modifier: Modifier = Modifier,
    initialHourIndex: Int = 0,
    initialMinuteIndex: Int = 0,
    initialAmPmIndex: Int = 0,
    onTimeSelected: (hour: Int, minute: Int, amPm: String) -> Unit
) {
    // 1..12 for hours, 0..59 for minutes, “AM/PM” list
    val hours = (1..12).map { it.toString() }
    val minutes = (0..59).map { it.toString().padStart(2, '0') }
    val amPmOptions = listOf("AM", "PM")

    var selectedHourIndex by remember { mutableIntStateOf(initialHourIndex) }
    var selectedMinuteIndex by remember { mutableIntStateOf(initialMinuteIndex) }
    var selectedAmPmIndex by remember { mutableIntStateOf(initialAmPmIndex) }

    // Whenever any wheel changes, call back with the new time
    LaunchedEffect(selectedHourIndex, selectedMinuteIndex, selectedAmPmIndex) {
        val hourValue = hours[selectedHourIndex].toInt()
        val minuteValue = minutes[selectedMinuteIndex].toInt()
        val amPmValue = amPmOptions[selectedAmPmIndex]
        onTimeSelected(hourValue, minuteValue, amPmValue)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hours Picker (circular if you want 1→12→1 wrap)
        WheelPicker(
            rawItems = hours,
            selectedIndex = selectedHourIndex,
            onSelectedIndexChange = { newIndex -> selectedHourIndex = newIndex },
            modifier = Modifier.weight(1f),
            circular = true  // <--- If you want hours to wrap 1→12→1
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Minutes Picker (circular so 00→59→00)
        WheelPicker(
            rawItems = minutes,
            selectedIndex = selectedMinuteIndex,
            onSelectedIndexChange = { newIndex -> selectedMinuteIndex = newIndex },
            modifier = Modifier.weight(1f),
            circular = true  // <--- Important for minutes wrapping
        )

        Spacer(modifier = Modifier.width(8.dp))

        // AM/PM Picker (usually just 2 items - not typically scrolled, but you can)
        WheelPicker(
            rawItems = amPmOptions,
            selectedIndex = selectedAmPmIndex,
            onSelectedIndexChange = { newIndex -> selectedAmPmIndex = newIndex },
            modifier = Modifier.weight(1f),
            circular = false  // <--- Probably not needed for AM/PM
        )
    }
}

