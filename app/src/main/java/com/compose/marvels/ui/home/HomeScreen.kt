package com.compose.marvels.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.compose.marvels.ui.MainViewModel
import com.compose.marvels.ui.models.Routes
import com.compose.marvels.ui.theme.BlackGradiant
import com.compose.marvels.ui.theme.Red700
import com.compose.marvels.ui.theme.TitleText
import com.compose.marvels.ui.theme.WhiteGradiant
import com.compose.marvels.ui.utils.MyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val mode by mainViewModel.mode.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { mainViewModel.onModeChange() },
                        imageVector = if (mode) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(if (mode) BlackGradiant else WhiteGradiant)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { navController.navigate(Routes.Gallery.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red700
                    )
                ) {
                    Text(text = "Personajes", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}