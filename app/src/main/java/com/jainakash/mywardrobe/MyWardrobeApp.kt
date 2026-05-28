package com.jainakash.mywardrobe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.jainakash.mywardrobe.navigation.MyWardrobeNavHost

@Composable
fun MyWardrobeApp() {
    val context = LocalContext.current
    val appContainer = remember { AppContainer(context) }

    MyWardrobeNavHost(appContainer = appContainer)
}

