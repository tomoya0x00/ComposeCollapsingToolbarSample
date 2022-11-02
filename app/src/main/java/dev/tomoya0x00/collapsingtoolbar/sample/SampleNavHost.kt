package dev.tomoya0x00.collapsingtoolbar.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.tomoya0x00.collapsingtoolbar.sample.ui.InitialScreen
import dev.tomoya0x00.collapsingtoolbar.sample.ui.ScrollByOffsetScreen
import dev.tomoya0x00.collapsingtoolbar.sample.ui.TopContentPaddingScreen

@Composable
fun SampleNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Destination = Destination.INITIAL,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.name,
        modifier = modifier,
    ) {
        composable(Destination.INITIAL.name) {
            InitialScreen(
                onNavigateToTopContentPadding = {
                    navController.navigate(Destination.TOP_CONTENT_PADDING.name)
                },
                onNavigateToScrollByOffset = {
                    navController.navigate(Destination.SCROLL_BY_OFFSET.name)
                },
            )
        }
        composable(Destination.TOP_CONTENT_PADDING.name) {
            TopContentPaddingScreen(
                onBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(Destination.SCROLL_BY_OFFSET.name) {
            ScrollByOffsetScreen(
                onBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
