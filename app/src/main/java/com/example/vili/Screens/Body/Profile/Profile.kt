package com.example.vili.Screens.Body.Profile

import android.annotation.SuppressLint
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
import com.example.vili.myApp.theme.LightBlack
import com.example.vili.myApp.theme.ObscureBlack
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import viliApp.Destinations
import viliApp.NavigationFunctions
import viliApp.systemBarColor

@Composable
@Preview
fun PreviewProfile() {

    Profile(nav = rememberNavController())
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Profile(
    profileID: String? = Firebase.auth.uid,
    nav: NavController,
    MainScreenViewModel: MainScreenViewModel = hiltViewModel(), viewModel: ProfileViewModel = hiltViewModel()
) {

    val refreshing by remember {
        mutableStateOf(false)
    }
    val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.refresh() })

    //Upper Bar Color
    systemBarColor(color = ObscureBlack)
    //Actualizo el icono del Bottombar
    BottomBarClass.updateIndex(0)

    Scaffold(bottomBar = { BottomBar() }) {

            Box(
                Modifier
                    .pullRefresh(pullRefreshState)
                    //.verticalScroll(rememberScrollState())

            ) {


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

                PullRefreshIndicator(
                    refreshing,
                    pullRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )


            }



    }

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
            .fillMaxSize()
            .background(LightBlack)
    ) {


        TopBar(
            switchSettings = { MainScreenViewModel.switchSettings() },
            switchSearch = { MainScreenViewModel.switchSearch() },
            nav = nav
        )

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
                        CurrentImage.imageURL.value = viewModel.bannerURL
                        CurrentImage.editingPFP.value = false
                        nav.navigate(route = Destinations.EditImage.ruta)
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
                            CurrentImage.imageURL.value = viewModel.pfpURL
                            CurrentImage.editingPFP.value = true
                            nav.navigate(route = Destinations.EditImage.ruta)
                        },
                    contentScale = ContentScale.Crop
                )
            }
            //endregion

            //region Nombre
            Row(
                Modifier
                    .background(Color.Transparent)
                    .offset(y = (40).dp)) {
                Text(modifier = Modifier.fillMaxWidth(), text = "${FBAuth.getUserEmail()?.substringBefore("@")}", textAlign = TextAlign.Center, color = Color.White, fontSize = 30.sp)
            }
            //endregion
        }
        //endregion

    }

}
