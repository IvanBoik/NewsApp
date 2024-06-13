package com.boiko.newsapp.presentation.news_navigator

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.boiko.newsapp.R
import com.boiko.newsapp.domain.model.Article
import com.boiko.newsapp.presentation.bookmark.BookmarkScreen
import com.boiko.newsapp.presentation.bookmark.BookmarkViewModel
import com.boiko.newsapp.presentation.details.DetailsEvent
import com.boiko.newsapp.presentation.details.DetailsScreen
import com.boiko.newsapp.presentation.details.DetailsViewModel
import com.boiko.newsapp.presentation.home.HomeScreen
import com.boiko.newsapp.presentation.home.HomeViewModel
import com.boiko.newsapp.presentation.music_screen.MusicScreen
import com.boiko.newsapp.presentation.music_screen.MusicViewModel
import com.boiko.newsapp.presentation.navgraph.Route
import com.boiko.newsapp.presentation.news_navigator.components.BottomNavigationItem
import com.boiko.newsapp.presentation.news_navigator.components.NewsBottomNavigation
import com.boiko.newsapp.presentation.personal_area.PersonalAreaScreen
import com.boiko.newsapp.presentation.personal_area.PersonalAreaViewModel
import com.boiko.newsapp.presentation.personal_area.components.CameraScreen
import com.boiko.newsapp.presentation.search.SearchScreen
import com.boiko.newsapp.presentation.search.SearchViewModel

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("RememberReturnType")
@Composable
fun NewsNavigator() {
    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(icon = R.drawable.ic_home, text = "Home"),
            BottomNavigationItem(icon = R.drawable.ic_search, text = "Search"),
            BottomNavigationItem(icon = R.drawable.ic_bookmark, text = "Bookmark"),
            BottomNavigationItem(icon = R.drawable.ic_music, text = "Music")
        )
    }

    val navController = rememberNavController()
    val backstackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    selectedItem = remember(key1 = backstackState) {
        when(backstackState?.destination?.route) {
            Route.HomeScreen.route -> 0
            Route.SearchScreen.route -> 1
            Route.BookmarkScreen.route -> 2
            Route.MusicScreen.route -> 3
            else -> 0
        }
    }

    val isBottomBarVisible = remember(key1 = backstackState) {
        val route = backstackState?.destination?.route
        return@remember route == Route.HomeScreen.route
                || route == Route.SearchScreen.route
                || route == Route.BookmarkScreen.route
                || route == Route.MusicScreen.route
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                NewsBottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem
                ) { index ->
                    when (index) {
                        0 -> navigateToTab(
                            navController = navController,
                            route = Route.HomeScreen.route
                        )

                        1 -> navigateToTab(
                            navController = navController,
                            route = Route.SearchScreen.route
                        )

                        2 -> navigateToTab(
                            navController = navController,
                            route = Route.BookmarkScreen.route
                        )

                        3 -> navigateToTab(
                            navController = navController,
                            route = Route.MusicScreen.route
                        )
                    }
                }
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            composable(route = Route.HomeScreen.route) {
                val viewModel = hiltViewModel<HomeViewModel>()
                val articles = viewModel.news.collectAsLazyPagingItems()
                HomeScreen(
                    viewModel = viewModel,
                    articles = articles,
                    navigateToPersonalArea = {
                        navController.navigate(Route.PersonalArea.route)
                    },
                    navigateToSearch = {
                        navigateToTab(
                            navController = navController,
                            route = Route.SearchScreen.route
                        )
                    },
                    navigateToDetails = { article ->
                        navigateToDetails(
                            navController = navController,
                            article = article
                        )
                    }
                )
            }

            composable(route = Route.SearchScreen.route) {
                val viewModel = hiltViewModel<SearchViewModel>()
                val state = viewModel.state.value
                SearchScreen(state = state, event = viewModel::onEvent) { article ->
                    navigateToDetails(
                        navController = navController,
                        article = article
                    )
                }
            }

            composable(route = Route.DetailsScreen.route) {
                val viewModel = hiltViewModel<DetailsViewModel>()
                if (viewModel.sideEffect != null) {
                    Toast.makeText(LocalContext.current, viewModel.sideEffect, Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(DetailsEvent.RemoveSideEffect)
                }
                navController.previousBackStackEntry?.savedStateHandle?.get<Article?>("article")
                    ?.let { article ->
                        DetailsScreen(article = article, event = viewModel::onEvent) {
                            navController.navigateUp()
                        }
                }
            }

            composable(route = Route.BookmarkScreen.route) {
                val viewModel = hiltViewModel<BookmarkViewModel>()
                val state = viewModel.state.value
                BookmarkScreen(state = state) { article ->
                    navigateToDetails(
                        navController = navController,
                        article = article
                    )
                }
            }

            composable(route = Route.PersonalArea.route) {
                val viewModel = hiltViewModel<PersonalAreaViewModel>()
                PersonalAreaScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(route = Route.CameraScreen.route) {
                val viewModel = hiltViewModel<PersonalAreaViewModel>()
                CameraScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(route = Route.MusicScreen.route) {
                val viewModel = hiltViewModel<MusicViewModel>()
                MusicScreen(viewModel = viewModel)
            }
        }
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}

private fun navigateToDetails(navController: NavController, article: Article) {
    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
    navController.navigate(
        route = Route.DetailsScreen.route
    )
}