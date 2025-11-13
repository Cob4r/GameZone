package com.example.gamezone.navigation

import androidx.navigation.NavController
import com.example.gamezone.R

object NavGraph {
    fun navigateToLogin(navController: NavController) {
        navController.navigate(R.id.action_start_to_login)
    }

    fun navigateToRegister(navController: NavController) {
        navController.navigate(R.id.action_login_to_register)
    }

    fun navigateToHome(navController: NavController) {
        navController.navigate(R.id.action_login_to_home)
    }
}


