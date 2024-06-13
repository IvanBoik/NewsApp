package com.boiko.newsapp.presentation.navgraph

sealed class Route(
    val route: String
) {
    data object OnBoardingScreen: Route(route = "onBoardingScreen")
    data object HomeScreen: Route(route = "homeScreen")
    data object SearchScreen: Route(route = "searchScreen")
    data object BookmarkScreen: Route(route = "bookmarkScreen")
    data object DetailsScreen: Route(route = "detailsScreen")
    data object AppStartNavigation: Route(route = "appStartNavigation")
    data object NewsNavigation: Route(route = "newsNavigation")
    data object NewsNavigatorScreen: Route(route = "newsNavigator")
    data object PersonalArea: Route(route = "personalArea")
    data object CameraScreen: Route(route = "CameraScreen")
    data object MusicScreen: Route(route = "MusicScreen")
}