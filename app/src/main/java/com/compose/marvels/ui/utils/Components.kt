package com.compose.marvels.ui.utils

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.compose.marvels.R
import com.compose.marvels.ui.theme.Red500
import com.compose.marvels.ui.theme.Red700
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.animateTransformation
import com.skydoves.orbital.rememberContentWithOrbitalScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title: String = "",
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Red700,
            titleContentColor = Color.White,
        ),
        title = {
            Row {
                Image(
                    modifier = Modifier.size(75.dp),
                    painter = painterResource(id = R.drawable.logo_transparent),
                    contentDescription = ""
                )
                Text(text = title)
            }
        },
        navigationIcon = { navigationIcon() },
        actions = { actions() }
    )
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filter(
    filterText: String,
    color: Color,
    onValueChange: (String) -> Unit,
    onIconClick: () -> Unit,
    onCleanClick: () -> Unit = {}
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = filterText,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = "Buscador") },
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .clickable { if (filterText.isNotEmpty()) onIconClick() },
                imageVector = Icons.Outlined.Search,
                contentDescription = ""
            )
        },
        trailingIcon = {
            if (filterText.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable { onCleanClick() },
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = ""
                )
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Red500,
            focusedBorderColor = Red700,
            textColor = color,
            unfocusedTrailingIconColor = Red500,
            focusedTrailingIconColor = Red700,
            unfocusedLeadingIconColor = Red500,
            focusedLeadingIconColor = Red700,
            placeholderColor = color
        )
    )
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