package com.compose.marvels.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.compose.marvels.ui.theme.Red500

@Composable
fun LoadImage(modifier: Modifier, url: String) {
    SubcomposeAsyncImage(
        modifier = modifier.fillMaxSize(),
        model = url,
        contentDescription = "",
        contentScale = ContentScale.Crop
    ) {
        when (painter.state) {
            AsyncImagePainter.State.Empty -> ImageDefault()
            is AsyncImagePainter.State.Error -> MessageError("Error al cargar la imagen")
            is AsyncImagePainter.State.Loading -> LoadingProgress()
            is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
        }
    }
}

@Composable
fun LoadingProgress() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Red500)
    }
}

@Composable
fun MessageError(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = message
        )
    }
}

@Composable
fun ImageDefault() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            imageVector = Icons.Filled.Image,
            contentDescription = "Image default"
        )
    }
}