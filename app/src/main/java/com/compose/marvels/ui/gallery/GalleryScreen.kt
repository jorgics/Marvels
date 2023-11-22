package com.compose.marvels.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.compose.marvels.ui.MainViewModel
import com.compose.marvels.ui.models.Routes
import com.compose.marvels.ui.theme.BodyText
import com.compose.marvels.ui.theme.RedGradiant
import com.compose.marvels.ui.theme.TitleText
import com.compose.marvels.ui.utils.Filter
import com.compose.marvels.ui.utils.ImageDefault
import com.compose.marvels.ui.utils.LoadImage
import com.compose.marvels.ui.utils.LoadingProgress
import com.compose.marvels.ui.utils.MessageError
import com.compose.marvels.ui.utils.MyLogo
import com.compose.marvels.ui.utils.MyTitle
import com.compose.marvels.ui.utils.MyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val mode by mainViewModel.mode.collectAsState()
    val animate by mainViewModel.animate.collectAsState()
    val characters by mainViewModel.charactersList.collectAsState()
    val isLoading by mainViewModel.isLoading.collectAsState()
    val filterText by mainViewModel.filterText.collectAsState()
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        if (mainViewModel.getPage() == -1) mainViewModel.getCharacters()
        if (animate) mainViewModel.onAnimateChange()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(
                title = { MyTitle(title = stringResource(id = R.string.gallery_screen)) },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable { navController.popBackStack() },
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White
                    )
                },
                actions = { MyLogo() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(if (mode) Color.Black else Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (isLoading && !animate) {
                LoadingProgress()
            } else {
                Filter(
                    filterText = filterText,
                    color = if (mode) Color.White else Color.Black,
                    onValueChange = { mainViewModel.onValueChange(it) },
                    onIconClick = { mainViewModel.onIconClick() },
                    onCleanClick = { mainViewModel.onCleanClick() }
                )
                if (characters.isEmpty()) {
                    MessageError(
                        message = stringResource(id = R.string.characters_error),
                        mode = mode
                    )
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp),
                        columns = GridCells.Adaptive(250.dp),
                        state = lazyGridState
                    ) {
                        items(characters) {
                            Item(it) { character ->
                                mainViewModel.onItemClick(character)
                                navController.navigate(Routes.Detail.route)
                            }
                        }
                    }

                    if (lazyGridState.isScrollInProgress) {
                        if (mainViewModel.isReachedEnd(lazyGridState)) {
                            if (filterText.isEmpty()) mainViewModel.nextCharacters(mainViewModel.getPage())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Item(characterModel: CharacterModel, onClick: (CharacterModel) -> Unit) {
    OutlinedCard(
        Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(4.dp)
            .clickable { onClick(characterModel) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RedGradiant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .background(Color.White)
            ) {
                if (characterModel.image!!.path != null) {
                    val url = "${characterModel.image.path}.${characterModel.image.extension}"
                    LoadImage(modifier = Modifier.fillMaxSize(), url = url)
                } else {
                    ImageDefault()
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = characterModel.name!!,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = TitleText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Comics: ${characterModel.comics?.items?.size.toString()}",
                textAlign = TextAlign.Center,
                color = Color.White,
                style = BodyText
            )
        }

    }
}
