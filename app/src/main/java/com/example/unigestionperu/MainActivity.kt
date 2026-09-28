package com.example.unigestionperu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.unigestionperu.ui.navigation.AppNavigation
import com.example.unigestionperu.ui.theme.UniGestionPeruTheme
import com.example.unigestionperu.viewmodel.ThemeState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniGestionPeruTheme(darkTheme = ThemeState.isDarkTheme) {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}
