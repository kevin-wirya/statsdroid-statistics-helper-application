package com.kevin.statsdroid.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        BottomNavItem(Screen.Lookup, Icons.Default.TableChart),
        BottomNavItem(Screen.Hypothesis, Icons.Default.Calculate),
        BottomNavItem(Screen.Clt, Icons.Default.Equalizer),
        BottomNavItem(Screen.Reference, Icons.Default.Book),
        BottomNavItem(Screen.About, Icons.Default.AccountCircle)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val formattedTitle = item.screen.title.replace(" ", "\n")
            val isSelected = currentRoute == item.screen.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.screen.title
                    )
                },
                label = {
                    Text(
                        text = formattedTitle,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        lineHeight = 13.sp
                    )
                },
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                ),
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