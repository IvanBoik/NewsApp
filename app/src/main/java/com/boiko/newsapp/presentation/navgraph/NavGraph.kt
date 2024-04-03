package com.boiko.newsapp.presentation.navgraph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.boiko.newsapp.presentation.home.HomeScreen
import com.boiko.newsapp.presentation.home.HomeViewModel
import com.boiko.newsapp.presentation.onboarding.OnBoardingScreen
import com.boiko.newsapp.presentation.onboarding.OnBoardingViewModel
import com.boiko.newsapp.presentation.search.SearchScreen
import com.boiko.newsapp.presentation.search.SearchViewModel

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                val viewModel = hiltViewModel<OnBoardingViewModel>()
                OnBoardingScreen(
                    event = viewModel::onEvent
                )
            }
        }

        navigation(
            route = Route.NewsNavigation.route,
            startDestination = Route.NewsNavigatorScreen.route
        ) {
            composable(route = Route.NewsNavigatorScreen.route) {
                val viewModel = hiltViewModel<SearchViewModel>()
                SearchScreen(state = viewModel.state.value, event = viewModel::onEvent) {
                    
                }
            }
        }
    }
}