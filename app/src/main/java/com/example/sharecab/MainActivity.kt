package com.example.sharecab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sharecab.ui.theme.ShareCabTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShareCabTheme(dynamicColor = false, darkTheme = false){
                NavigationDrawerUI { modifier ->
                    HomeScreenUI(modifier)
                }
            }
        }
    }
}

