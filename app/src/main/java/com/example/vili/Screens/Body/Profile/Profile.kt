package com.example.vili.Screens.Body.Profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.Common.Complex.BottomBar
import com.example.vili.Common.Complex.BottomBarClass
import com.example.vili.Common.Composables.BackPressHandler
import com.example.vili.Common.Composables.SearchMenu
import com.example.vili.Common.Composables.SettingsMenu
import com.example.vili.Common.Composables.TopBar
import com.example.vili.Model.Querys.FBAuth
import com.example.vili.Screens.Body.Home.MainScreenViewModel
import com.example.vili.Screens.Body.MyList.GameListBody
import com.example.vili.myApp.theme.LightBlack
import com.example.vili.myApp.theme.LightBlack2
import com.example.vili.myApp.theme.ObscureBlack
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import viliApp.Destinations
import viliApp.DeviceConfig
import viliApp.NavigationFunctions
import viliApp.systemBarColor

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Profile(
    profileID: String,
    nav: NavController,
    MainScreenViewModel: MainScreenViewModel = hiltViewModel(), viewModel: ProfileViewModel = hiltViewModel()
) {


    val pullRefreshState = rememberPullRefreshState(viewModel.refreshing, { viewModel.refresh() })

    //Guardo el UID nada mas entrar por si viajo a una lista
    /*
    if (profileID != null) {
        viewModel.setProfileUID(profileID)
        viewModel.init()
    }

     */

    //Upper Bar Color
    systemBarColor(color = ObscureBlack)
    //Actualizo el icono del Bottombar
    BottomBarClass.updateIndex(0)


    Scaffold(bottomBar = { BottomBar() }) {
        Box(
            Modifier
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())

        ) {
            TopBar(
                switchSettings = { MainScreenViewModel.switchSettings() },
                switchSearch = { MainScreenViewModel.switchSearch() },
                nav = nav
            )

            //Este es el cuerpo de la pantalla
            ProfileBody(nav = nav, profileID = profileID)


            //region SUBMENU SEARCH
            AnimatedVisibility(
                visible = MainScreenViewModel.enableSearchMenu,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> +fullWidth }, animationSpec = tween(
                        durationMillis = 500, easing = LinearOutSlowInEasing
                    )
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> +fullWidth }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutLinearInEasing
                    )
                )
            ) {
                //Si estoy en esta pantalla cuando le doy al botón de atrás no quiero cambiar de pantalla
                //Sino que este menú se vuelva a plegar
                BackPressHandler(MainScreenViewModel::searchBackPressed)
                SearchMenu(
                    MainScreenViewModel.searchText,
                    MainScreenViewModel::onSearchTextChange,
                    MainScreenViewModel.gameList,
                    nav
                )
            }
            //endregion

            //region SUBMENU SETTINGS
            AnimatedVisibility(
                visible = MainScreenViewModel.enableSettingsMenu,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(
                        durationMillis = 300, easing = LinearOutSlowInEasing
                    )
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(
                        durationMillis = 200, easing = FastOutLinearInEasing
                    )
                )
            ) {
                //Si estoy en esta pantalla cuando le doy al botón de atrás no quiero cambiar de pantalla
                //Sino que este menú se vuelva a plegar
                BackPressHandler(MainScreenViewModel::settingsBackPressed)
                SettingsMenu(
                    MainScreenViewModel::logOut,
                    MainScreenViewModel::switchSettings
                )
            }

            //endregion

            PullRefreshIndicator(viewModel.refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))

        }
    }

    //region Is Refreshing?
    if (viewModel.updatingImages==2){
        viewModel.updatingImages = 0
        viewModel.refreshing = false
    }
    //endregion

    //region NAVIGATE
    if (NavigationFunctions.changeScreen.value) {
        NavigationFunctions.changeScreen(-1)
        when (NavigationFunctions.screenID.value) {
            0 -> {}
            1 -> {
                nav.navigate(route = Destinations.MainScreen.ruta)
            }
            2 -> {
                nav.navigate(route = Destinations.ListScreen.ruta)
            }
            4->{
                //Si ha viajado a su propia lista se le cambia de pestaña
                if (profileID==FBAuth.UID){
                    nav.navigate(route = Destinations.ListScreen.ruta)
                } else {
                    //Si no, le muestro la lista, actualizando primero los datos
                    viewModel.updateGameListValues()
                    nav.navigate(route = Destinations.UserList.ruta) //TODO

                }
            }
        }
    }
    //endregion

    //region LOGOUT
    if (MainScreenViewModel.logOutnPop) {
        NavigationFunctions.NavigatePopLogOut(
            navController = nav,
            destination = Destinations.Pantalla1.ruta
        )
    }
    //endregion
}

@Composable
fun ProfileBody(
    profileID: String?,
    nav: NavController,
    MainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    viewModel: ProfileViewModel = hiltViewModel()
) {

    Column(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(100))
            .background(LightBlack)
    ) {




        //region Banner
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
                .background(Color.Gray)
        ) {
            Image(
                painter = rememberAsyncImagePainter(viewModel.bannerURL),
                contentDescription = "User banner picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (FBAuth.UID.toString()==profileID) {
                            CurrentImage.imageURL.value = viewModel.bannerURL
                            CurrentImage.editingPFP.value = false
                            nav.navigate(route = Destinations.EditImage.ruta)
                        }
                    },
                contentScale = ContentScale.Crop
            )
        }
        //endregion

        //region PFP + nombre
        Row(
            Modifier
                .offset(x = 10.dp, y = (-30).dp)
                .fillMaxWidth()
                .background(Color.Transparent)) {

            //region PFP
            Box(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .size(150.dp)
                    .padding(4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(viewModel.pfpURL),
                    contentDescription = "User profile picture",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxSize()
                        .clickable {
                            if (FBAuth.UID.toString()==profileID) {
                                CurrentImage.imageURL.value = viewModel.pfpURL
                                CurrentImage.editingPFP.value = true
                                nav.navigate(route = Destinations.EditImage.ruta)
                            }
                        },
                    contentScale = ContentScale.Crop
                )
            }
            //endregion

            //region Nombre
            val text = when{
                FBAuth.UID == profileID -> {"${FBAuth.getUserEmail()?.substringBefore("@")}"}
                else -> {"${profileID.toString().take(6)}"}
            }
            Row(
                Modifier
                    .background(Color.Transparent)
                    .offset(y = (40).dp)) {
                Text(modifier = Modifier.fillMaxWidth(), text = text, textAlign = TextAlign.Center, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
            //endregion
        }
        //endregion

        //region Some Stats
        Row(
            Modifier
                .fillMaxHeight(0.25f)
                .fillMaxWidth()
                .background(Color.Transparent)) {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(0.5f)
                    .padding(5.dp)
                    .background(LightBlack2, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
                    ) {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = viewModel.playedGames.toString(), color = Color.White, textAlign = TextAlign.Center, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Juegos Jugados", color = Color.White, textAlign = TextAlign.Center, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .weight(0.5f)
                    .padding(5.dp)
                    .background(LightBlack2, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = viewModel.planningGames.toString(), color = Color.White, textAlign = TextAlign.Center, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Juegos Planning", color = Color.White, textAlign = TextAlign.Center, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        //endregion


        //region navigate List
        Column(Modifier.padding(5.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .background(LightBlack2, RoundedCornerShape(10.dp))
                    .padding(5.dp)
                    .clickable { NavigationFunctions.changeScreen(4) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "VISITAR  LISTA",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        //endregion

    }

}

