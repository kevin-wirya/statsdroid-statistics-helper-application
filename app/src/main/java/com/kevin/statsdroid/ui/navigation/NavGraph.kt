package com.kevin.statsdroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kevin.statsdroid.ui.screens.AboutScreen
import com.kevin.statsdroid.ui.screens.CltScreen
import com.kevin.statsdroid.ui.screens.HypothesisScreen
import com.kevin.statsdroid.ui.screens.LookupScreen
import com.kevin.statsdroid.ui.screens.ReferenceScreen

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
            LookupScreen()
        }
        composable(Screen.Hypothesis.route) {
            HypothesisScreen()
        }
        composable(Screen.Clt.route) {
            CltScreen()
        }
        composable(Screen.Reference.route) {
            ReferenceScreen()
        }
        composable(Screen.About.route) {
            AboutScreen()
        }
    }
}
