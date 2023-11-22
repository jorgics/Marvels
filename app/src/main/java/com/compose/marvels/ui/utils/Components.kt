package com.compose.marvels.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Cancel
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.compose.marvels.R
import com.compose.marvels.ui.theme.BodyText
import com.compose.marvels.ui.theme.Red500
import com.compose.marvels.ui.theme.Red700
import com.compose.marvels.ui.theme.TitleTextSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Red700,
            titleContentColor = Color.White,
        ),
        title = { title() },
        navigationIcon = { navigationIcon() },
        actions = { actions() }
    )
}

@Composable
fun MyTitle(
    title: String = ""
) {
    Text(
        text = title,
        style = BodyText,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun MyLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(85.dp)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.logo_transparent),
            contentDescription = ""
        )
    }
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
            is AsyncImagePainter.State.Error -> MessageError(stringResource(id = R.string.image_error))
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
            text = message,
            style = TitleTextSmall
        )
    }
}

@Composable
fun MessageError(
    message: String,
    mode: Boolean
) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = message,
        color = if (mode) Color.White else Color.Black,
        textAlign = TextAlign.Center,
        style = TitleTextSmall
    )
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
        placeholder = { Text(text = stringResource(id = R.string.search_button)) },
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
