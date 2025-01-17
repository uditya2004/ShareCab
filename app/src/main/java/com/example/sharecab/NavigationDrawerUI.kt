package com.example.sharecab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerUI(
    homeScreen: @Composable (Modifier) -> Unit   // Accepts the screen content as a parameter){}
){
    //List of Items to show in navigation drawer
    val navigationItems = listOf(
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
    val bottomBarItems = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Outlined.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Chat",
            selectedIcon = Icons.Outlined.ChatBubbleOutline,
            unselectedIcon = Icons.Outlined.ChatBubbleOutline,
            hasNews = false,
            badgeCount = 45
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Outlined.Person,
            unselectedIcon = Icons.Outlined.Person,
            hasNews = true,
        ),
    )
    var selectedBottomBarItemIndex by rememberSaveable { mutableIntStateOf(0) }

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
                    drawerContainerColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth(0.75f)   // Used to set the width of the drawer content.
                ){
                    Spacer(modifier = Modifier.height(20.dp))

                    //forEachIndexed is used to iterate through iterable, it returns index as well as item
                    navigationItems.forEachIndexed { index, item ->
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
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier.border(BorderStroke(1.dp, Color(0xFFE2E8F0))),
                        title = {
                            Text(
                                text = "Campus Ride",
                                color = MaterialTheme.colorScheme.primary,
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
                                    tint = Color.Black
                                )
                            }
                        },
//                        actions = {
//                            Button(
//                                onClick = { /* Handle Create Room Click */ },
//                                shape = MaterialTheme.shapes.small,
//                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
//                            ) {
//                                Text(text = "Create Room", color = Color.White)
//                            }
//                        }
                    )
                },
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .padding(10.dp)   // Floating bar
                            .height(56.dp)
                            .clip(RoundedCornerShape(20.dp)) // Rounded corners
                            .border(BorderStroke(1.dp, Color(0xFFE2E8F0))),
                        containerColor = MaterialTheme.colorScheme.tertiary, // Background color Color(0xFF1c272b)
                        tonalElevation = 8.dp // Floating effect
                    ) {
                        bottomBarItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent, // No indicator
                                    selectedIconColor = Color.Black, // Selected icon color
                                    unselectedIconColor = Color(0xFF8e959c) // Unselected icon color
                                ),
                                selected = selectedBottomBarItemIndex == index,
                                onClick = {
                                    selectedBottomBarItemIndex = index
                                },
                                icon = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        BadgedBox(
                                            badge = {
                                                if (item.badgeCount != null) {
                                                    Badge(
                                                        modifier = Modifier.offset(x = 8.dp, y = (-7).dp)
                                                    ){
                                                        Text(
                                                            text = item.badgeCount.toString(),
                                                            color = Color.White
                                                        )
                                                    }
                                                } else if (item.hasNews) {
                                                    Badge()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (index == selectedBottomBarItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title,

                                                tint = if (index == selectedBottomBarItemIndex){
                                                    MaterialTheme.colorScheme.primary
                                                }else{
                                                    Color.Black
                                                }
                                            )
                                        }
                                    }
                                }

                            )
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background) // Ensures consistent background color
//                        .padding(paddingValues)
                ) {
                    homeScreen(Modifier.fillMaxSize())
                }
            }
        }
    }
}