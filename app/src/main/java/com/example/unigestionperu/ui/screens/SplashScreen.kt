package com.example.unigestionperu.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unigestionperu.ui.components.AppLogo
import com.example.unigestionperu.ui.theme.UC_Azul
import com.example.unigestionperu.ui.theme.UC_Purpura
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var progress by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(key1 = true) {
        // Incrementa progresivamente el porcentaje de carga durante 3 segundos
        while (progress < 1f) {
            delay(30)
            progress += 0.01f
        }
        delay(200)
        onSplashFinished()
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 100),
        label = "progress"
    )

    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryTextColor = MaterialTheme.colorScheme.primary
    val secondaryTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contenedor del Logo con Círculo de Carga alrededor
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                // Círculo indicador de carga en porcentaje
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Círculo base de fondo
                    drawCircle(
                        color = UC_Azul.copy(alpha = 0.15f),
                        style = Stroke(width = 6.dp.toPx())
                    )
                    // Círculo de carga con progreso animado
                    drawArc(
                        color = UC_Azul,
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = 6.dp.toPx())
                    )
                }

                // Isotipo Vectorial de la Marca
                AppLogo(modifier = Modifier.size(130.dp))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "UNIGESTIÓN PERÚ",
                color = primaryTextColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Muestra el porcentaje de carga en tiempo real
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                color = UC_Purpura,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Gestión Universitaria",
                color = secondaryTextColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
