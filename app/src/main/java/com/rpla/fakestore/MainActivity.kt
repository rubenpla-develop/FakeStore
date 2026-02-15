package com.rpla.fakestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rpla.fakestore.core.ui.theme.FakeStoreTheme
import com.rpla.fakestore.feature.products.ui.view.ProductsRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FakeStoreTheme {
                FakeStoreApp()
            }
        }
    }
}

@Composable
private fun FakeStoreApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                AppDestinations.entries.forEach { destination ->
                    val selected =
                        currentDestination
                            ?.hierarchy
                            ?.any { it.route == destination.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestinations.PRODUCTS.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AppDestinations.PRODUCTS.route) {
                _root_ide_package_.com.rpla.fakestore.feature.products.ui.view.ProductsRoute(
                    paddingValues = innerPadding,
                    onFavoriteClicked = { /* TODO */ },
                )
            }
            composable(AppDestinations.FAVORITES.route) {
                Text("Favorites") // TODO
            }
            composable(AppDestinations.PROFILE.route) {
                Text("Profile") // TODO
            }
        }
    }
}

private enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val route: String,
) {
    PRODUCTS("Products", Icons.Default.Home, "products"),
    FAVORITES("Favorites", Icons.Default.Favorite, "favorites"),
    PROFILE("Profile", Icons.Default.AccountBox, "profile"),
}

@Preview(showBackground = true)
@Composable
private fun Preview_FakeStoreApp() {
    FakeStoreTheme {
        FakeStoreApp()
    }
}
