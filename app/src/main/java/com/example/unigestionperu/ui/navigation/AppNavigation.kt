package com.example.unigestionperu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.unigestionperu.ui.screens.DetailScreen
import com.example.unigestionperu.ui.screens.FormScreen
import com.example.unigestionperu.ui.screens.HomeScreen
import com.example.unigestionperu.ui.screens.LoginScreen
import com.example.unigestionperu.ui.screens.RegistroScreen
import com.example.unigestionperu.ui.screens.SplashScreen
import com.example.unigestionperu.viewmodel.AuthViewModel
import com.example.unigestionperu.viewmodel.CursoViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Registro : Screen("registro")
    object Home : Screen("home")
    object Form : Screen("form")
    object Detail : Screen("detail/{cursoId}") {
        fun createRoute(cursoId: Int) = "detail/$cursoId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val cursoViewModel: CursoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Registro.route)
                },
            )
        }
        composable(Screen.Registro.route) {
            RegistroScreen(
                viewModel = authViewModel,
                onBackToLogin = {
                    navController.popBackStack()
                },
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                usuarioLogueado = authViewModel.uiState.usuarioLogueado,
                cursoViewModel = cursoViewModel,
                onCourseClick = { cursoId ->
                    navController.navigate(Screen.Detail.createRoute(cursoId))
                },
                onNavigateToForm = {
                    navController.navigate(Screen.Form.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Screen.Form.route) {
            FormScreen(
                viewModel = cursoViewModel,
                onSearchComplete = {
                    navController.popBackStack()
                },
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("cursoId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val cursoId = backStackEntry.arguments?.getInt("cursoId") ?: 0
            val usuarioId = authViewModel.uiState.usuarioLogueado?.id ?: 1

            DetailScreen(
                cursoId = cursoId,
                usuarioId = usuarioId,
                viewModel = cursoViewModel,
                onBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
