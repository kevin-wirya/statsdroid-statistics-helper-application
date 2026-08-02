package com.kevin.statsdroid.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kevin.statsdroid.ui.navigation.Screen

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector
)

@Composable
fun AppBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem(Screen.Lookup, Icons.Default.Search),
        BottomNavItem(Screen.Hypothesis, Icons.Default.List),
        BottomNavItem(Screen.Clt, Icons.Default.Refresh),
        BottomNavItem(Screen.Reference, Icons.Default.Info),
        BottomNavItem(Screen.About, Icons.Default.AccountCircle)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.screen.title) },
                label = { Text(item.screen.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    if (currentRoute != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}