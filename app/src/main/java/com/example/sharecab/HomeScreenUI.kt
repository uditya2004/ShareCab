package com.example.sharecab



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatReclineNormal
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

//==============================
// Home Screen
//==============================
@Composable
fun HomeScreenUI(modifier: Modifier = Modifier) {
    CarpoolContent(modifier = modifier)
}

//==============================
data class RideDetailItems(
    val date: String,
    val from: String,
    val to: String,
    val seats: String,
    val price: String,
    val vehicleType: String,
    val bookingStatus: String = "Not Booked",
)
//==============================


//==============================
// Main composable that shows the content
//==============================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarpoolContent(modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState()    //sheetState handles the smooth animations of the bottom sheet appearing and disappearing.
    var showBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedDropdownValue by remember { mutableStateOf("Select sorting") }

    val defaultDate: Long? = null
    val defaultDropdownValue = "Select sorting"

    Box(modifier = modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .background(Color.White)
                .fillMaxSize()
        ) {

            //Column element:- Button to Search which open Alert Dialog
            SearchFieldsBox()


            Spacer(modifier = Modifier.height(16.dp))

            //Column element:- Filter and DropDown Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //DropDown Button
                DropdownMenuButton(
                    defaultValue = "All Vehicle",
                    options = listOf("All Vehicle", "Eco Van", "Ertiga", "Bolero"),
                    onValueChanged = {newValue ->
                        // Update the state with the new dropdown selection
                        selectedDropdownValue = newValue
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Filter Button  -----------
                Button(
                    onClick = {
                        showBottomSheet = true  /* IMPLEMENT MORE FILTER BUTTON*/
                    },
                    shape = RoundedCornerShape(30),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Tune Icon",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("More Filters", color = Color.Black)
                    }
                }

                if (showBottomSheet) {
                    var tempSelectedDate by remember { mutableStateOf(selectedDate) }
                    var tempSelectedDropdownValue by remember { mutableStateOf(selectedDropdownValue) }

                    ModalBottomSheet(
                        containerColor = Color.White,
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState
                    ) {
                        BottomSheetContent(
                            // Parameter - 1
                            initialSelectedDate = tempSelectedDate,

                            // Parameter - 2
                            initialSelectedDropdownValue = tempSelectedDropdownValue,

                            // Parameter - 3
                            onSaveClick = { newSelectedDate, newSelectedDropdownValue ->
                                selectedDate = newSelectedDate
                                selectedDropdownValue = newSelectedDropdownValue

                                // Hiding the bottom sheet after saving the changes
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (it !is CancellationException) {  // this code ensures that the bottom sheet is hidden only if the coroutine completes successfully and not if it was cancelled.
                                        showBottomSheet = false
                                    }
                                }
                            },

                            // Parameter - 4
                            onCancelClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (it !is CancellationException) {
                                        showBottomSheet = false
                                    }
                                }
                            },

                            // Parameter - 5
                            onResetClick = {
                                tempSelectedDate = defaultDate
                                tempSelectedDropdownValue = defaultDropdownValue
                            },

                            // Parameter - 6
                            onDateSelected = { tempSelectedDate = it },

                            // Parameter - 7
                            onDropdownItemSelected = { tempSelectedDropdownValue = it }
                        )
                    }
                }

                // Filter Button ENDS  -----------
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Some fake data to show ride cards
            val rideItems = listOf(
                RideDetailItems(
                    date = "10/01/2025" + ",  "+ "21:45",
                    from = "VIT",
                    to = "RKMP",
                    seats = "4/4",
                    price = "₹400/person",
                    vehicleType = "Ertiga"
                ),
                RideDetailItems(
                    date = "21/01/2025" + ",  " + "08:30",
                    from = "VIT",
                    to = "BPL",
                    seats = "2/2",
                    price = "₹800/person",
                    vehicleType = "Eco van"
                )
            )

            // Render ride cards
            rideItems.forEach { item ->
                RideCard(
                    date = item.date,
                    from = item.from,
                    to = item.to,
                    seats = item.seats,
                    price = item.price,
                    vehicleType = item.vehicleType
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


//=======================
// More Filter Bottom Sheet Content
//=======================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    initialSelectedDate: Long?,
    initialSelectedDropdownValue: String,
    onSaveClick: (Long?, String) -> Unit,
    onCancelClick: () -> Unit,
    onResetClick: () -> Unit,
    onDateSelected: (Long) -> Unit,
    onDropdownItemSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var showDatePickerDialog by remember { mutableStateOf(false) }   // state that determines whether the DatePickerDialog should be shown or not
        val datePickerState = rememberDatePickerState(initialSelectedDate)  //the datePickerState variable stores the state of the currently selected date inside the date picker dialog.

        var selectedDateText by remember { mutableStateOf(initialSelectedDate?.let { formatDate(it) } ?: "Select Date") }
        var selectedDropdown by remember { mutableStateOf(initialSelectedDropdownValue) }

        // Keep the local state in sync with the initial value for reset
        LaunchedEffect(initialSelectedDropdownValue) {
            selectedDropdown = initialSelectedDropdownValue
        }

        Text(text = "Date of Travel", fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        // Select Date button
        Button(
            onClick = { showDatePickerDialog = true },
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(30)
        ) {
            Text(text = selectedDateText, color = Color.Black)
        }

        if (showDatePickerDialog) {
            DatePickerDialog(
                colors = DatePickerDefaults.colors(containerColor = Color.White),
                onDismissRequest = { showDatePickerDialog = false },
                confirmButton = {
                    Button(
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A82F7)),
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                onDateSelected(it)
                                selectedDateText = formatDate(it)
                            }
                            showDatePickerDialog = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A82F7)),
                        onClick = { showDatePickerDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState, colors = DatePickerDefaults.colors(Color.White))
            }
        }

        Spacer(modifier = Modifier.height(7.dp))

        Text(text = "Sort By", fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)

        // Wrap DropdownMenuButton inside key(...)
        key(selectedDropdown) {
            DropdownMenuButton(
                options = listOf("Date", "Cost", "Available Seats"),
                defaultValue = selectedDropdown
            ) { selectedValue ->
                selectedDropdown = selectedValue
                onDropdownItemSelected(selectedValue)
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis = null
                    selectedDateText = "Select Date"
                    selectedDropdown = initialSelectedDropdownValue // Reset local state
                    onResetClick()
                },
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Reset", color = Color.White)
            }
            Row {
                Button(
                    onClick = onCancelClick,
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A82F7))
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        onSaveClick(datePickerState.selectedDateMillis, selectedDropdown)
                    },
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A82F7))
                ) {
                    Text("Save")
                }
            }
        }
    }
}


fun formatDate(millis: Long): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(Date(millis))
}

//=======================
// Search Field Box for entering location and destination
//=======================
@Composable
fun SearchFieldsBox() {
    var locationText by remember { mutableStateOf("") }
    var destinationText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Column {
            OutlinedTextField(
                value = locationText,
                onValueChange = { locationText = it },
                placeholder = { Text("Your location") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(30),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.RadioButtonChecked,
                        contentDescription = "Search Location Icon"
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            //Horizontal divider
            HorizontalDivider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = destinationText,
                onValueChange = { destinationText = it },
                placeholder = { Text("Enter destination") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Search Destination Icon"
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}


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


//=======================
// Ride Card
//=======================
@Composable
fun RideCard(
    date: String,
    from: String,
    to: String,
    seats: String,
    price: String,
    vehicleType: String,
    bookingStatus: String = "Not Booked"
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8EB), RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            //Column Element - 1 -> Date text
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {

                //Left end:- Icon and Date details
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //Row Element - 1 -> Icon
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Calendar Today Icon",
                        tint = Color.Black // Optional: Change the tint color here
                    )

                    //Row Element - 2 -> Space
                    Spacer(modifier = Modifier.width(8.dp))

                    //Row Element - 3 -> Date Text
                    Text(text = date, fontWeight = FontWeight.Medium)
                }

                //Row Element - 5 -> Booking Status
                Text(text = bookingStatus, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Column Element 2 -> Location Details
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "Location Icon",
                    tint = Color.Black // Optional: You can change the tint color here
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$from To $to", color = Color.Gray)

            }

            Spacer(modifier = Modifier.height(12.dp))

            // Column Element - 3 -> Seats and Vehicle type
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Left End: People Icon and Seats Details
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // People Icon
                    Icon(
                        imageVector = Icons.Filled.AirlineSeatReclineNormal,
                        contentDescription = "People Alt Icon",
                        tint = Color.Black // Optional: You can change the tint color here
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Seats details
                    Text(text = "$seats seats vacant", color = Color.Gray)
                }

                // Right End: Car Icon and Vehicle Type
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Car Icon
                    Icon(
                        painter = painterResource(id = R.drawable.lucide_car),
                        contentDescription = "Car Icon"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Vehicle type details
                    Text(text = vehicleType, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Column element :- Price Details and Join Ride Button
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = price, fontSize = 17.sp, color = Color(0xFF3A82F7), fontWeight = FontWeight.Bold)

//                Spacer(modifier = Modifier.width(20.dp))

                Button(
                    onClick = { /* Join Ride */ },
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A82F7))
                ) {
                    Text(text = "Join Ride", color = Color.White)
                }
            }
        }
    }
}
