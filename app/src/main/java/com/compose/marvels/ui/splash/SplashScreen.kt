package com.compose.marvels.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.compose.marvels.R
import com.compose.marvels.ui.MainViewModel
import com.compose.marvels.ui.models.Routes
import com.compose.marvels.ui.theme.BlackGradiant
import com.compose.marvels.ui.theme.WhiteGradiant
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val mode by mainViewModel.mode.collectAsState()

    LaunchedEffect(Unit) {
        delay(2000)
        delay(1000)
        navController.navigate(Routes.Home.route)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (mode) BlackGradiant else WhiteGradiant),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_transparent),
            contentDescription = ""
        )
    }
}