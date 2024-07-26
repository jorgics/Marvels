package com.compose.marvels.ui.home

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.compose.marvels.R
import com.compose.marvels.ui.MainViewModel
import com.compose.marvels.ui.models.Routes
import com.compose.marvels.ui.theme.Orange800
import com.compose.marvels.ui.theme.Red100
import com.compose.marvels.ui.theme.Red700
import com.compose.marvels.ui.theme.Red900
import com.compose.marvels.ui.theme.TitleText
import com.compose.marvels.ui.utils.Attribution
import com.compose.marvels.ui.utils.MyLogo
import com.compose.marvels.ui.utils.MyOutlinedTextField
import com.compose.marvels.ui.utils.MyTitle
import com.compose.marvels.ui.utils.MyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val mode by mainViewModel.mode.collectAsState()
    val apikey by mainViewModel.apiKey.collectAsState()
    val passwordVisibility by mainViewModel.passwordVisibility.collectAsState()
    val colorMode = remember { Animatable(Color.White) }

    LaunchedEffect(mode) {
        colorMode.animateTo(
            if (mode) Color.Black else Color.White,
            animationSpec = tween(2000)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(
                title = {
                    MyLogo(modifier = Modifier.fillMaxWidth()) {
                        mainViewModel.reset()
                        navController.popBackStack(Routes.Home.route, false)
                    }
                },
                navigationIcon = { MyTitle(title = stringResource(id = R.string.home_screen)) },
                actions = {
                    Switch(
                        checked = mode,
                        onCheckedChange = { mainViewModel.onModeChange() },
                        thumbContent = {
                            Icon(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clickable { mainViewModel.onModeChange() },
                                imageVector = if (mode) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                                contentDescription = "Dark mode",
                                tint = if (mode) Color.White else Orange800
                            )
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            uncheckedThumbColor = Color.White,
                            checkedTrackColor = Red100,
                            uncheckedTrackColor = Red100,
                            checkedBorderColor = Red900,
                            uncheckedBorderColor = Red900
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colorMode.value)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (apikey.isApiKey) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { mainViewModel.reset() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Red700
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.keys_button),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { navController.navigate(Routes.Gallery.route) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Red700
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.characters_button),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.dont_api_key),
                            style = TitleText,
                            color = if (mode) Color.White else Color.Black
                        )

                        MyOutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 24.dp,
                                    end = 24.dp,
                                    top = 8.dp
                                ),
                            value = apikey.apiKey,
                            onValueChange = { mainViewModel.onApikeyChange(it) },
                            label = stringResource(id = R.string.api_key),
                            mode = mode,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                        )

                        MyOutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 24.dp,
                                    end = 24.dp,
                                    top = 8.dp
                                ),
                            value = apikey.privateKey,
                            onValueChange = { mainViewModel.onPrivateKeyChange(it) },
                            label = stringResource(id = R.string.private_key),
                            mode = mode,
                            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val icon =
                                    if (passwordVisibility) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
                                IconButton(onClick = { mainViewModel.onVisibilityChange() }) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = "Toggle password visibility"
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                        )

                        Button(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { mainViewModel.onSaveClick() },
                            enabled = apikey.enabled,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Red700
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.save_button),
                                style = TitleText,
                            )
                        }
                    }
                }

                Attribution(mode)
            }
        }
    }
}