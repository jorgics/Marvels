package com.compose.marvels.ui.splash

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.compose.marvels.R
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.animateTransformation
import com.skydoves.orbital.rememberContentWithOrbitalScope
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    var isTransformed by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(2000)
        isTransformed = true
        delay(1000)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Example(isTransformed)
    }
}

@Composable
fun Example(isTransformed: Boolean) {
    val transformationSpec = SpringSpec<IntSize>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = 200f
    )

    val poster = rememberContentWithOrbitalScope {
        Image(
            modifier = if (isTransformed) {
                Modifier.size(150.dp, 310.dp)
            } else {
                Modifier.size(75.dp, 155.dp)
            }.animateTransformation(this, transformationSpec),
            painter = painterResource(id = R.drawable.logo_transparent),
            contentDescription = ""
        )
    }

    Orbital {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            poster()
        }
    }
}