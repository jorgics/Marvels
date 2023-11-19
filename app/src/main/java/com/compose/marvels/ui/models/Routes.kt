package com.compose.marvels.ui.models

sealed class Routes(val route: String) {
    object Splash: Routes("splash_screen")
    object Gallery: Routes("gallery_screen")
    object Detail: Routes("detail_screen")
}
