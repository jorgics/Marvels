package com.compose.marvels.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compose.marvels.ui.detail.DetailScreen
import com.compose.marvels.ui.gallery.GalleryScreen
import com.compose.marvels.ui.home.HomeScreen
import com.compose.marvels.ui.models.Routes
import com.compose.marvels.ui.splash.SplashScreen
import com.compose.marvels.ui.theme.MarvelsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    mainViewModel.createPreference(context = applicationContext)
                    val navController = rememberNavController()
                    val isError by mainViewModel.isError.collectAsState()

                    if (isError) Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG)
                        .show()

                    NavHost(navController = navController, startDestination = Routes.Splash.route) {
                        composable(
                            Routes.Splash.route,
                            exitTransition = {
                                fadeOut(animationSpec = tween(2000)) +
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Left,
                                            tween(2000)
                                        )
                            },
                            enterTransition = {
                                fadeIn(animationSpec = tween(2000)) +
                                        slideIntoContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Left,
                                            tween(2000)
                                        )
                            },
                            popExitTransition = {
                                fadeOut(animationSpec = tween(2000)) +
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            tween(2000)
                                        )
                            },
                            popEnterTransition = {
                                fadeIn(animationSpec = tween(2000)) +
                                        slideIntoContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            tween(2000)
                                        )
                            }
                        ) {
                            SplashScreen(navController, mainViewModel)
                        }
                        composable(
                            Routes.Home.route,
                            exitTransition = {
                                fadeOut(animationSpec = tween(2000)) +
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Left,
                                            tween(2000)
                                        )
                            },
                            enterTransition = {
                                fadeIn(animationSpec = tween(2000)) +
                                        slideIntoContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Left,
                                            tween(2000)
                                        )
                            },
                            popExitTransition = {
                                fadeOut(animationSpec = tween(2000)) +
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            tween(2000)
                                        )
                            },
                            popEnterTransition = {
                                fadeIn(animationSpec = tween(2000)) +
                                        slideIntoContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            tween(2000)
                                        )
                            }
                        ) {
                            HomeScreen(navController, mainViewModel)
                        }
                        composable(
                            Routes.Gallery.route,
                            exitTransition = {
                                fadeOut(animationSpec = tween(2000)) +
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Left,
                                            tween(2000)
                                        )
                            },
                            enterTransition = {
                                fadeIn(animationSpec = tween(2000)) +
                                        slideIntoContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Left,
                                            tween(2000)
                                        )
                            },
                            popExitTransition = {
                                fadeOut(animationSpec = tween(2000)) +
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            tween(2000)
                                        )
                            },
                            popEnterTransition = {
                                fadeIn(animationSpec = tween(2000)) +
                                        slideIntoContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            tween(2000)
                                        )
                            }
                        ) {
                            GalleryScreen(navController, mainViewModel)
                        }
                        composable(
                            Routes.Detail.route,
                            exitTransition = {
                                fadeOut(animationSpec = tween(2000)) +
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Left,
                                            tween(2000)
                                        )
                            },
                            enterTransition = {
                                fadeIn(animationSpec = tween(2000)) +
                                        slideIntoContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Left,
                                            tween(2000)
                                        )
                            },
                            popExitTransition = {
                                fadeOut(animationSpec = tween(2000)) +
                                        slideOutOfContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            tween(2000)
                                        )
                            },
                            popEnterTransition = {
                                fadeIn(animationSpec = tween(2000)) +
                                        slideIntoContainer(
                                            AnimatedContentTransitionScope.SlideDirection.Right,
                                            tween(2000)
                                        )
                            }
                        ) {
                            DetailScreen(navController, mainViewModel)
                        }
                    }
                }
            }
        }
    }
}

