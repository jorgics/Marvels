package com.compose.marvels.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.domain.models.ComicModel
import com.compose.marvels.ui.MainViewModel
import com.compose.marvels.ui.models.Routes
import com.compose.marvels.ui.theme.BlackGradiant
import com.compose.marvels.ui.theme.BodyText
import com.compose.marvels.ui.theme.BodyTextSmall
import com.compose.marvels.ui.theme.Red500
import com.compose.marvels.ui.theme.RedGradiant
import com.compose.marvels.ui.theme.TitleText
import com.compose.marvels.ui.theme.TitleTextSmall
import com.compose.marvels.ui.utils.ImageDefault
import com.compose.marvels.ui.utils.LoadImage
import com.compose.marvels.ui.utils.LoadingProgress
import java.text.SimpleDateFormat

@Composable
fun DetailScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val character by mainViewModel.character.collectAsState()
    val isLoading by mainViewModel.isLoading.collectAsState()
    val comics by mainViewModel.comics.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize().background(BlackGradiant)) {
            if (isLoading) {
                LoadingProgress()
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CharacterItem(character)
                    Comics(comics)
                }
                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 20.dp, end = 20.dp)
                        .clickable { navController.navigate(Routes.Gallery.route) },
                    imageVector = Icons.Filled.Close,
                    contentDescription = "",
                    tint = Red500
                )
            }
        }
    }
}

@Composable
fun CharacterItem(character: CharacterModel?) {
    OutlinedCard(
        modifier = Modifier
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
                if (character?.image!!.path != null) {
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
                text = character?.name!!,
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
                style = BodyText
            )
        }
    }
}

@Composable
fun Comics(comics: List<ComicModel>) {
    LazyHorizontalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp),
        rows = GridCells.Adaptive(250.dp)
    ) {
        items(comics) {
            ComicItem(it)
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun ComicItem(comicModel: ComicModel) {
    val format = SimpleDateFormat("dd/MM/yyyy")

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
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                text = comicModel.title!!,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = TitleTextSmall
            )
            Text(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                text = "Modificado: ${format.format(comicModel.date!!)}",
                textAlign = TextAlign.Start,
                color = Color.White,
                style = BodyTextSmall
            )
        }
    }
}