package viliApp

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.R
import com.example.vili.myApp.theme.ObscureBlack
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Preview
@Composable
fun PreviewMainScreen() {
    HomeScreen(rememberNavController())
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewModel: MainScreenViewModel = hiltViewModel()) {

    getDeviceConfig()
    systemBarColor(color = ObscureBlack)


    Scaffold(
        bottomBar = { BottomBar(viewModel::updateIndex, viewModel, viewModel.bottomBar) },


        ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))
        ) {


            //Depende del index seleccionado abajo la pantalla se recomposeará

            when (viewModel.selectedIndex) {

                0 -> {}
                1 -> {
                    Box() {

                        //Compruebo si el usuario le da al boton de desloguear para cambiar la pantalla y popearla
                        if (viewModel.logOutnPop) {
                            NavigatePopLogOut(
                                navController = navController,
                                destination = Destinations.Pantalla1.ruta
                            )
                        }

                        //MENU SETTINGS
                        if (viewModel.gameList.isNotEmpty()) {

                            MainScreenSkin(
                                viewModel.gameList,
                                navController,
                                viewModel.bannerList,
                                viewModel.recommendedList,
                                viewModel::switchSearch,
                                viewModel::switchSettings,
                                viewModel.clickable,
                                viewModel::updatePendingValues,
                                viewModel::updateLazyState,
                                viewModel.rowState0,
                                viewModel.rowState1,
                                viewModel.rowState2,
                                viewModel.pager,
                                viewModel.banner,
                                viewModel.columnState,
                            )
                        }

                        //MENU SEARCH
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
                                navController
                            )
                        }

                        //MENU SEARCH

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
                    }
                }


                2 -> {
                    GameList(navController)
                }

            }


        }
    }


}

@Composable
fun BottomBar(updateIndex: (Int) -> Unit, viewModel: MainScreenViewModel, showBottom: Boolean) {

    AnimatedVisibility(
        visible = showBottom, enter = slideInVertically(
            initialOffsetY = { fullHeight -> +fullHeight }, animationSpec = tween(
                durationMillis = 300, easing = LinearOutSlowInEasing
            )
        ), exit = slideOutVertically(
            targetOffsetY = { fullHeight -> +fullHeight }, animationSpec = tween(
                durationMillis = 300, easing = FastOutLinearInEasing
            )
        )
    ) {
        BottomNavigation(
            modifier = Modifier.fillMaxHeight(0.07f),
            elevation = 10.dp,
            backgroundColor = Color(0xDD050505)
        ) {

            BottomNavigationItem(icon = {
                Icon(imageVector = Icons.Default.Home, "")
            }, selected = (viewModel.selectedIndex == 1), onClick = {
                updateIndex(1)
            }, selectedContentColor = Color.Red, unselectedContentColor = Color.White
            )

            BottomNavigationItem(icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.list),
                    "",
                    modifier = Modifier.size(30.dp)
                )
            }, selected = (viewModel.selectedIndex == 2), onClick = {
                updateIndex(2)
            }, selectedContentColor = Color.Red, unselectedContentColor = Color.White
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreenSkin(
    gameList: List<Game>,
    nav: NavController,
    bannerList: List<GameBanner>,
    reccommList: List<Game>,
    switchSearch: () -> Unit,
    switchSettings: () -> Unit,
    clickable: Boolean,
    updatePendingValues: () -> Unit,
    updateLazyState: (Int, Int) -> Unit,
    rowState0: Int,
    rowState1: Int,
    rowState2: Int,
    pagerState: Int,
    bannerState: Int,
    columnState: Int

) {


    val stateList = listOf<LazyListState>(
        rememberLazyListState(),
        rememberLazyListState(),
        rememberLazyListState(),
        rememberLazyListState(),
        rememberLazyListState(),
        rememberLazyListState()
    )
    val pagerController = rememberPagerState()

    LaunchedEffect(Unit) {
        stateList[0].scrollToItem(0, rowState0)
        stateList[1].scrollToItem(0, rowState1)
        stateList[2].scrollToItem(0, rowState2)
        stateList[3].scrollToItem(0, columnState)
        pagerController.scrollToPage(pagerState)
    }



    Column() {

        TopBar(switchSettings = { switchSettings() }, switchSearch = { switchSearch() }, nav = nav)

        LazyColumn(
            Modifier.fillMaxSize(), state = stateList[3]
        ) {
            updateLazyState(5, stateList[3].firstVisibleItemScrollOffset)

            item {

                //PRIMERA FILA
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Juegos Nuevos",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(DeviceConfig.heightPercentage(32))

                ) {

                    LazyRow(
                        Modifier
                            .fillMaxSize()
                            .padding(5.dp), state = stateList[0]
                    ) {
                        updateLazyState(0, stateList[0].firstVisibleItemScrollOffset)
                        item {
                            Row() {
                                for (i in 0 until 10) {
                                    val game = gameList[i]
                                    Surface(elevation = 15.dp) {
                                        GameBox(nav, game.id, game.name, game.imageURL)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                }

                            }
                        }

                    }
                }

                //BANNERS
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

                                HorizontalPager(count = 3, state = pagerController) { page ->
                                    updateLazyState(3, pagerController.currentPage)
                                    Image(
                                        modifier = Modifier
                                            .padding(top = 20.dp, bottom = 20.dp)
                                            .fillMaxSize()
                                            .clickable {
                                                if (clickable) {
                                                    CentralizedData.updateGameID(bannerList[page].gameID)
                                                    nav.navigate(Destinations.Pantalla3.ruta)
                                                    updatePendingValues()
                                                }
                                            },
                                        painter = rememberAsyncImagePainter(bannerList[page].imageURL),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )

                                }

                            }


                        }

                    }
                }

                //SEGUNDA FILA
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Próximos lanzamientos",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(DeviceConfig.heightPercentage(32))

                ) {

                    LazyRow(
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp), state = stateList[1]
                    ) {
                        updateLazyState(1, stateList[1].firstVisibleItemScrollOffset)
                        item {
                            Row() {
                                for (i in 10 until 20) {
                                    val game = gameList[i]
                                    Surface(elevation = 15.dp) {
                                        GameBox(nav, game.id, game.name, game.imageURL)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                }

                            }
                        }

                    }
                }

                //TERCERA FILA
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tus recomendados",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(DeviceConfig.heightPercentage(32))

                ) {

                    LazyRow(
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp), state = stateList[2]
                    ) {
                        updateLazyState(2, stateList[2].firstVisibleItemScrollOffset)
                        item {
                            Row() {
                                for (i in 0 until reccommList.size) {
                                    val game = reccommList[i]
                                    Surface(elevation = 15.dp) {
                                        GameBox(nav, game.id, game.name, game.imageURL)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                }

                            }
                        }

                    }
                }

                //Esto suma espacio abajo para subir el ultimo LazyRow ya que lo cubre el bottombar
                Spacer(Modifier.height(50.dp))
            }

        }
    }

}

@Composable
fun SearchMenu(
    searchText: String, onValueChange: (String) -> Unit, gameList: List<Game>, nav: NavController
) {

    Box() {

        Button(modifier = Modifier
            .fillMaxSize()
            .alpha(0f),
            onClick = { /*EVITA HITBOXES DE ATRÁS*/ }) {}

        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(1f)
                .background(Color(0xFF181818)),
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.15f)
                    .clip(RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp))
                    .background(ObscureBlack), contentAlignment = Alignment.Center
            ) {


                //TEXTFIELD
                Column() {
                    OutlinedTextField(
                        value = searchText,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(1f),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            Color.White,
                            cursorColor = Color.Red,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.White
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.search),
                                modifier = Modifier.size(30.dp),
                                tint = Color.White,
                                contentDescription = "searchIcon"
                            )
                        },
                        onValueChange = onValueChange,
                        label = { Text(text = "Introduce un título", color = Color.White) },
                    )
                }


            }

            //RESULTADOS
            LazyList(nav, searchText, gameList = gameList, isGameListB = true)


        }
    }
}


@Composable
fun SettingsMenu(logOut: () -> Unit, switchSettings: () -> Unit) {

    var offsetX by remember { mutableStateOf(0f) }
    val boxWidth =
        DeviceConfig.dpToFloat(DeviceConfig.DPwidthPercentage(DeviceConfig.screenWidth, 70))
    var returningDefault by remember { mutableStateOf(false) }

    val offsetAnimation: Dp by animateDpAsState(
        if (returningDefault) {
            0.dp;
        } else DeviceConfig.pxToDp(offsetX),
        tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )
    if (returningDefault) {
        LaunchedEffect(Unit) {
            delay(300)
            returningDefault = false
        }
    }


    Row(Modifier
        .fillMaxSize()
        .background(Color.Transparent)
        .offset {
            IntOffset(
                DeviceConfig
                    .dpToPx(offsetAnimation)
                    .roundToInt(), 0
            )
        }
        .pointerInput(Unit) {
            detectDragGestures(onDragEnd = {
                //Si se mueve lo suficiente a la izquierda se cierra
                if ((offsetX.absoluteValue * 0.7) > DeviceConfig.tinyFloatToBig(
                        boxWidth
                    )
                ) {
                    switchSettings()
                } else {
                    //Si no, vuelve a su posición
                    offsetX = 0f
                    returningDefault = true

                }

            }) { change, dragAmount ->
                change.consume()
                if (dragAmount.x < 0) {
                    offsetX += dragAmount.x
                } else if (dragAmount.x>0 && offsetX<0){
                    offsetX += dragAmount.x
                }

            }
        }) {

        Box(
            Modifier

                .weight(boxWidth)
        ) {

            Button(modifier = Modifier.fillMaxSize(), onClick = {}) {}

            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color(0xFF000000)),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 15.dp)
                    .fillMaxHeight(0.05f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    onClick = {
                        logOut()
                    }) {
                    Text(text = "Logout", color = Color.Black, fontWeight = FontWeight.Bold)

                }
            }

        }
        Box(Modifier.weight(0.3f)) {
            //Boton invisible fuera para cerrar fondo
            Button(modifier = Modifier
                .fillMaxSize()
                .alpha(0f), onClick = { switchSettings() }) {}
        }


    }


}

@Composable
fun TopBar(switchSettings: () -> Unit, switchSearch: () -> Unit, nav: NavController) {

    //TOPBAR
    Row(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(5))
            .background(ObscureBlack),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .background(Color.Transparent) //Color(0xFF0A0A0A)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(start = 5.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { switchSettings() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                    "",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

        }
        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxHeight()
                .weight(0.50f),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(R.drawable.logovilipng), contentDescription = "")
        }
        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(end = 5.dp), contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(onClick = { switchSearch() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.search),
                    "",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

        }


    }
}


@Composable
fun BackPressHandler(
    onBackPressed: () -> Unit,
    backPressedDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}
