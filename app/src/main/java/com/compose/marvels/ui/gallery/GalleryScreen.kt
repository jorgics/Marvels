package com.compose.marvels.ui.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.compose.marvels.domain.models.CharacterModel
import com.compose.marvels.ui.MainViewModel
import com.compose.marvels.ui.models.Routes
import com.compose.marvels.ui.theme.BlackGradiant
import com.compose.marvels.ui.theme.BodyText
import com.compose.marvels.ui.theme.Red500
import com.compose.marvels.ui.theme.RedGradiant
import com.compose.marvels.ui.theme.TitleText
import com.compose.marvels.ui.utils.ImageDefault
import com.compose.marvels.ui.utils.LoadImage
import com.compose.marvels.ui.utils.LoadingProgress

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        mainViewModel.getCharacters()
    }
    val characters by mainViewModel.filterList.collectAsState()
    val isLoading by mainViewModel.isLoading.collectAsState()
    val total by mainViewModel.total.collectAsState()
    val filterText by mainViewModel.filterText.collectAsState()
    val lazyGridState = rememberLazyGridState()
    val lazyPaging = mainViewModel.lazyPagingItems.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(pageCount = { total / 20 })

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        //HorizontalPager(state = pagerState) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlackGradiant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (isLoading) {
                    LoadingProgress()
                } else {
                    Filter(filterText){ mainViewModel.onValueChange(it) }
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

                        }
                    }
                }
            }
      //  }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filter(filterText: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = filterText,
        onValueChange = { onValueChange(it) },
        trailingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "", tint = Red500)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Red500,
            focusedBorderColor = Red500,
            textColor = Color.White
        )
    )
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
