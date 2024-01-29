package com.example.olisapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MainMenuNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = OrderScreen.route
    ) {
        navigation()
    }
}