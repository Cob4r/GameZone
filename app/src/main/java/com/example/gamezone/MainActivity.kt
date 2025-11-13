package com.example.gamezone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.gamezone.ui.auth.LoginScreen
import com.example.gamezone.ui.auth.RegisterScreen
import com.example.gamezone.ui.home.HomeScreen
import com.example.gamezone.ui.start.StartScreen
import com.example.gamezone.ui.theme.GameZoneTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GameZoneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameZoneApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GameZoneApp() {
    val navController = rememberNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "start",
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        // 🟦 Pantalla de inicio (logo, bienvenida)
        composable("start") {
            StartScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        // 🟦 Pantalla de login
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") },
                onCreateAccount = { navController.navigate("register") }
            )
        }

        // 🟦 Pantalla de registro
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("login") }
            )
        }

        // 🟦 Pantalla principal
        composable("home") {
            HomeScreen()
        }
    }
}
