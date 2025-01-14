package com.example.sharecab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Below Code contain the Navigation Drawer and Top App Bar UI Logic.
data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerUI(
    homeScreen: @Composable (Modifier) -> Unit   // Accepts the screen content as a parameter){}
){
    //List of Items to show in navigation drawer
    val items = listOf(
        NavigationItem(
            title = "All",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        NavigationItem(
            title = "Urgent",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            badgeCount = 45
        ),
        NavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        //Main Code starts here
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)   // Storing the state of drawer here , initialized to closed
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }        // This variable keeps track of the currently selected item's index.

        ModalNavigationDrawer(
            drawerContent = {
                // Items what are shown in the navigation drawer

                // The ModalDrawerSheet is used to display a list of navigation items within the drawer.
                ModalDrawerSheet(
                    drawerContainerColor = Color.White,
                    drawerContentColor = Color.White,
                    modifier = Modifier.fillMaxWidth(0.75f)   // Used to set the width of the drawer content.
                ){
                    Spacer(modifier = Modifier.height(20.dp))

                    //forEachIndexed is used to iterate through iterable, it returns index as well as item
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = {
                                Text(text = item.title)
                            },
                            // selected parameter is used to visually highlight the selected item in the drawer.
                            selected = index == selectedItemIndex,   // It returns true when the index of item in this particular iteration, matches the selected item index. If the condition is true , item will be highlighted as selected.

                            onClick = {
//                                navController.navigate(item.route)   // Used to Navigate to the screen associated with that navigation item.
                                selectedItemIndex = index    // updates the selectedItemIndex variable with the index of the currently clicked item in the navigation drawer.
                                scope.launch {               //when clicked on an item, we close the drawer
                                    drawerState.close()
                                }
                            },

                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            //text associated with the item in the drawer.
                            badge = {
                                item.badgeCount?.let {   // if badge count is not null then show the badge text.
                                    Text(text = item.badgeCount.toString())
                                }
                            },
                            modifier = Modifier    // It is the inner padding of each item in the navigation Drawer
                                .padding(NavigationDrawerItemDefaults.ItemPadding)   // Used the default padding given by NavigationDrawerItemDefaults
                        )
                    }
                }
            },

            drawerState = drawerState
        ) {

            // Content of actual Home/main Screen - Content behind the opened drawer
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                        modifier = Modifier.border(BorderStroke(1.dp, Color(0xFFE2E8F0))),
                        title = {
                            Text(
                                text = "Campus Ride",
                                color = Color(0xFF3A82F7),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu Icon",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        actions = {
                            Button(
                                onClick = { /* Handle Create Room Click */ },
                                shape = MaterialTheme.shapes.small,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A82F7))
                            ) {
                                Text(text = "Create Room", color = Color.White)
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White) // Ensures consistent background color
                        .padding(paddingValues)
                ) {
                    homeScreen(Modifier.fillMaxSize())
                }
            }
        }
    }
}