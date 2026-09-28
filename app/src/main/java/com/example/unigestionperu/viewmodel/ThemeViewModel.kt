package com.example.unigestionperu.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeState {
    var isDarkTheme by mutableStateOf(false) // Modo claro por defecto
}
