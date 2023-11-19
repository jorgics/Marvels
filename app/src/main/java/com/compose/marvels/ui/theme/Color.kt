package com.compose.marvels.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Red100 = Color(0xFFFFDFDF)
val Red300 = Color(0xFFFF9595)
val Red500 = Color(0xFFFF2626)
val Red700 = Color(0xFFD60000)

val Black600 = Color(0xFF5d5d5d)
val Black800 = Color(0xFF454545)

val RedGradiant = Brush.linearGradient(
    colors = listOf(Red700, Red500)
)

val BlackGradiant = Brush.linearGradient(
    colors = listOf(Black800, Black600)
)