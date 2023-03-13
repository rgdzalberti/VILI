package com.example.vili.Screens.Body.Home

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.Common.Complex.BottomBar
import com.example.vili.Common.Complex.BottomBarClass
import com.example.vili.Common.Composables.*
import com.example.vili.myApp.theme.LightBlack
import com.example.vili.myApp.theme.ObscureBlack
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import viliApp.*
import viliApp.NavigationFunctions.Companion.NavigatePopLogOut

@Preview
@Composable
fun PreviewTTT(){
    HomeScreen(nav = rememberNavController())
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(nav: NavController, viewModel: HomeScreenViewModel = hiltViewModel()){


    //Modifico la barra superior
    systemBarColor(color = ObscureBlack)

    //Actualizo el icono del Bottombar
    BottomBarClass.updateIndex(1)

    //La función que contiene la pantalla está contenida por una Box, esto se hace porque dentro de la Box
    //en la parte de abajo se ponen los submenus para que se superpongan

    Scaffold(bottomBar = { BottomBar(nav) }) {
        Box() {
            //Este es el cuerpo de la pantalla
            HomeBody(nav)

            //region SUBMENU SEARCH
            androidx.compose.animation.AnimatedVisibility(
                visible = viewModel.enableSearchMenu, enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> +fullWidth }, animationSpec = tween(
                        durationMillis = 500, easing = LinearOutSlowInEasing
                    )
                ), exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> +fullWidth }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutLinearInEasing
                    )
                )
            ) {
                //Si estoy en esta pantalla cuando le doy al botón de atrás no quiero cambiar de pantalla
                //Sino que este menú se vuelva a plegar
                BackPressHandler(viewModel::searchBackPressed)
                SearchMenu(
                    viewModel.searchText,
                    viewModel::onSearchTextChange,
                    viewModel.gameList,
                    nav
                )
            }
            //endregion

            //region SUBMENU SETTINGS
            androidx.compose.animation.AnimatedVisibility(
                visible = viewModel.enableSettingsMenu, enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(
                        durationMillis = 300, easing = LinearOutSlowInEasing
                    )
                ), exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(
                        durationMillis = 200, easing = FastOutLinearInEasing
                    )
                )
            ) {
                //Si estoy en esta pantalla cuando le doy al botón de atrás no quiero cambiar de pantalla
                //Sino que este menú se vuelva a plegar
                BackPressHandler(viewModel::settingsBackPressed)
                SettingsMenu(viewModel::logOut, viewModel::switchSettings)
            }
            //endregion
        }
    }

    //region LOGOUT
    if (viewModel.logOutnPop){
        NavigatePopLogOut(
            navController = nav,
            destination = Destinations.Pantalla1.ruta
        )
    }
    //endregion

}

@Composable
fun HomeBody(nav:NavController,viewModel: HomeScreenViewModel = hiltViewModel()){

    Column(Modifier.background(LightBlack)) {

        TopBar(switchSettings = { viewModel.switchSettings() }, switchSearch = { viewModel.switchSearch() }, nav = nav)

        LazyColumn(Modifier.fillMaxSize()) {

            item {

                //Lazy Row
                if (viewModel.gameList.isNotEmpty()) GameLazyRow(title = "Juegos Nuevos", nav = nav, gameList = viewModel.gameList.subList(0,10))

                //Banners
                if (viewModel.bannerList.isNotEmpty()) BannerPager(nav = nav)

                //Segunda Lazy Row
                if (viewModel.gameList.isNotEmpty()) GameLazyRow(title = "Próximos lanzamientos", nav = nav, gameList = viewModel.gameList.subList(10,20))

                //Tercera Lazy Row
                if (viewModel.gameList.isNotEmpty()) GameLazyRow(title = "Recomendados", nav = nav, gameList = viewModel.recommendedList)

                //Esto suma espacio abajo para subir el ultimo LazyRow ya que lo cubre el BottomBar
                Spacer(Modifier.height(50.dp))
            }

        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerPager(nav: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {

    Column(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(40))
            .background(Color(0xFF131313))

    ) {

        LazyRow(
            Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {

            item {

                Column(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .background(Color.Transparent)
                ) {

                    if (viewModel.bannerList.isNotEmpty()) {
                        HorizontalPager(count = 3) { page ->
                            Image(
                                modifier = Modifier
                                    .padding(top = 20.dp, bottom = 20.dp)
                                    .fillMaxSize()
                                    .clickable {
                                        if (viewModel.clickable) {
                                            nav.navigate("${Destinations.Pantalla3.ruta}/${viewModel.bannerList[page].gameID}")
                                            viewModel.updatePendingValues()
                                        }
                                    },
                                painter = rememberAsyncImagePainter(viewModel.bannerList[page].imageURL),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )

                        }
                    }

                }


            }

        }
    }
}