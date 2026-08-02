package com.kevin.statsdroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Lookup.route,
        modifier = modifier
    ) {
        composable(Screen.Lookup.route) {
        }
        composable(Screen.Hypothesis.route) {
        }
        composable(Screen.Clt.route) {
        }
        composable(Screen.Reference.route) {
        }
        composable(Screen.About.route) {
        }
    }
}
