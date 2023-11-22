package com.compose.marvels.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.marvels.R
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.ui.MainViewModel
import com.compose.marvels.ui.theme.BodyText
import com.compose.marvels.ui.theme.BodyTextSmall
import com.compose.marvels.ui.theme.Red700
import com.compose.marvels.ui.theme.RedGradiant
import com.compose.marvels.ui.theme.TitleText
import com.compose.marvels.ui.theme.TitleTextSmall
import com.compose.marvels.ui.utils.ImageDefault
import com.compose.marvels.ui.utils.LoadImage
import com.compose.marvels.ui.utils.LoadingProgress
import com.compose.marvels.ui.utils.MessageError
import com.compose.marvels.ui.utils.MyLogo
import com.compose.marvels.ui.utils.MyTitle
import com.compose.marvels.ui.utils.MyTopBar
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val mode by mainViewModel.mode.collectAsState()
    val character by mainViewModel.character.collectAsState()
    val isLoading by mainViewModel.isLoading.collectAsState()
    val comics by mainViewModel.comics.collectAsState()
    val expanded by mainViewModel.expanded.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(
                title = { MyTitle(title = stringResource(id = R.string.detail_screen)) },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            mainViewModel.onAnimateChange()
                            navController.popBackStack()
                        },
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White
                    )
                },
                actions = { MyLogo(navController = navController) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(if (mode) Color.Black else Color.White)
        ) {
            if (isLoading) {
                LoadingProgress()
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (character != null) {
                        CharacterItem(character!!, expanded) { mainViewModel.onExpanded() }
                        Comics(comics, mode)
                    } else {
                        MessageError(message = stringResource(id = R.string.characters_error), mode = mode)
                    }
                }
                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 20.dp, end = 20.dp)
                        .clickable {
                            mainViewModel.onAnimateChange()
                            navController.popBackStack()
                        },
                    imageVector = Icons.Filled.Close,
                    contentDescription = "",
                    tint = Red700
                )
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: CharacterModel,
    expanded: Boolean,
    onExpanded: () -> Unit = {}
) {
    OutlinedCard(
        modifier = Modifier
            .clickable { onExpanded() }
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(RedGradiant)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
            ) {
                if (character.image!!.path != null) {
                    val url = "${character.image.path}.${character.image.extension}"
                    LoadImage(modifier = Modifier.fillMaxSize(), url = url)
                } else {
                    ImageDefault()
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                text = character.name!!,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = TitleText
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = character.description!!,
                textAlign = TextAlign.Justify,
                color = Color.White,
                style = BodyText,
                maxLines = if (expanded) Int.MAX_VALUE else 1
            )
        }
    }
}

@Composable
fun Comics(comics: List<ComicModel>, mode: Boolean) {
    if (comics.isEmpty()) {
        MessageError(message = stringResource(id = R.string.comics_error), mode = mode)
    } else {
        LazyHorizontalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
            rows = GridCells.Adaptive(200.dp)
        ) {
            items(comics) {
                ComicItem(it)
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun ComicItem(comicModel: ComicModel) {
    val format = SimpleDateFormat("dd/MM/yyyy")
    var title = comicModel.title ?: ""
    if (title.isNotEmpty() && title.length >= 50)
        title = title.replaceRange(49, title.length, "...")

    OutlinedCard(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RedGradiant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                if (comicModel.image!!.path != null) {
                    val url = "${comicModel.image.path}.${comicModel.image.extension}"
                    LoadImage(modifier = Modifier.fillMaxSize(), url = url)
                } else {
                    ImageDefault()
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                text = title,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = TitleTextSmall
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = "Modificado: ${format.format(comicModel.date ?: Date())}",
                textAlign = TextAlign.Start,
                color = Color.White,
                style = BodyTextSmall
            )
        }
    }
}