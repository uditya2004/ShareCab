package com.example.sharecab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatReclineNormal
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sharecab.components.CustomTimePickerDialog
import com.example.sharecab.components.DropdownMenuButton

//===========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCarpoolRoomScreen() {
    var date by remember { mutableStateOf("") }
    var fromLocation by remember { mutableStateOf("") }
    var toLocation by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf("") }
    var totalCost by remember { mutableStateOf("") }
    var totalSeats by remember { mutableStateOf("") }

    var showTimePickerDialog by remember { mutableStateOf(false) }
    var selectedTimeText by remember { mutableStateOf("Select Time") }

    Scaffold(
        topBar = {
            TopAppBar(
                colors =  TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 56.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Create Room")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle close */ }) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Close")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
//                            .padding(10.dp)   // Floating bar
                    .height(66.dp)
                    .clip(RoundedCornerShape(20.dp)) // Rounded corners
                    .border(BorderStroke(1.dp, Color(0xFFE2E8F0))),
                containerColor = MaterialTheme.colorScheme.background,
                content = {

                    Row(
                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { /* Handle Cancel */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                        ) {
                            Text("Cancel", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { /* Handle Create Room */ },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Create")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            //Space
            item{
                Spacer(modifier = Modifier.height( 8.dp))
            }

            // Date and Time Picker
            item{

                Row(verticalAlignment = Alignment.CenterVertically){
                    Column{
                        //Date Picker
                        Text(text = "Date", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(bottom = 3.dp))
                        DatePickerUI(
                            initialSelectedDate = null,
                            onDateSelected = { date = formatDate(it) }
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column{
                        //Element - 2:- Time Picker
                        Text(text = "Time", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(bottom = 3.dp))

                        Button(
                            onClick = { showTimePickerDialog = true },
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(30)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = selectedTimeText, color = Color.Black)
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = "Time",
                                    tint = Color.Gray
                                )
                            }

                        }
                    }


                }
            }

            // From Search Field
            item{

                Text(text = "From", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(bottom = 3.dp))
                OutlinedTextField(
                    value = fromLocation,
                    onValueChange = { fromLocation = it },
                    label = { Text("Enter pickup location") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.RadioButtonChecked,
                            contentDescription = "Search Location Icon"
                        )
                    },
                )
            }

            // To Search Field
            item{

                Text(text = "To", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(bottom = 3.dp))
                OutlinedTextField(
                    value = toLocation,
                    onValueChange = { toLocation = it },
                    label = { Text("Enter destination") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Search Destination Icon"
                        )
                    }
                )
            }

            // Vehicle Type DropDown
            item{

                Text(text = "Vehicle Type", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(bottom = 3.dp))
                DropdownMenuButton(
                    options = listOf("All Vehicle", "Eco-Van", "Ertiga", "Bolero"),
                    defaultValue = "Select Vehicle",
                    onValueChanged = { vehicleType = it }
                )
            }

            //Total Cost
            item{

                Text(text = "Total Cost (₹)", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(bottom = 3.dp))
                OutlinedTextField(
                    value = totalCost,
                    onValueChange = { totalCost = it },
                    singleLine = true,
                    label = { Text("Total Cost") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            painter  = painterResource(id = R.drawable.currency_rupee_circle),
                            contentDescription = "Currency Icon"
                        )
                    }
                )
            }

            //Total Seats
            item{

                Text(text = "Total Seats", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(bottom = 3.dp))
                OutlinedTextField(
                    value = totalSeats,
                    onValueChange = { totalSeats = it },
                    label = { Text("Total Seats") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.AirlineSeatReclineNormal,
                            contentDescription = "People Alt Icon",
                        )
                    }
                )
            }

            //Spacer
            item{
                Spacer(modifier = Modifier.height(3.dp))
            }


        }
    }

    // Conditionally show the Dialog
    if (showTimePickerDialog) {
        CustomTimePickerDialog(
            onDismissRequest = { showTimePickerDialog = false },
            onTimeConfirm = { newTime ->
                selectedTimeText = newTime
                showTimePickerDialog = false
            }
        )
    }
}


//========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerUI(
    initialSelectedDate: Long?,
    onDateSelected: (Long) -> Unit,
) {

    var showDatePickerDialog by remember { mutableStateOf(false) }   // state that determines whether the DatePickerDialog should be shown or not
    val datePickerState = rememberDatePickerState(initialSelectedDate)  //the datePickerState variable stores the state of the currently selected date inside the date picker dialog.



    var selectedDateText by remember { mutableStateOf(initialSelectedDate?.let {
        formatDate(
            it
        )
    } ?: "Select Date") }
    // Element - 1:-  Select Date button
    Button(
        onClick = { showDatePickerDialog = true },
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(30)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = selectedDateText, color = Color.Black, fontSize = 15.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = "Calendar",
                tint = Color.Gray
            )
        }

    }

    if (showDatePickerDialog) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button(
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
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
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    onClick = { showDatePickerDialog = false }
                ) {
                    Text("Cancel",color = Color.White)
                }
            }
        ) {
            DatePicker(state = datePickerState, colors = DatePickerDefaults.colors(MaterialTheme.colorScheme.background))
        }
    }

}

