package viliApp

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.FloatRange
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.firebase.firestore.model.mutation.Overlay
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewMainScreen(){
    HomeScreen(rememberNavController())
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewModel: MainScreenViewModel = hiltViewModel() ){

    getDeviceConfig()
    systemBarColor(color = Color(0xFF0A0A0A))

    Scaffold(bottomBar = {BottomBar(viewModel::updateIndex,viewModel,viewModel.bottomBar)}) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))) {


            //Depende del index seleccionado abajo la pantalla se recomposeará

            when(viewModel.selectedIndex){

                0 -> {}
                1 -> 
                {
                    
                    //MENU SEARCH
                    AnimatedVisibility(
                        visible = viewModel.enableSearchMenu,
                        enter = slideInHorizontally(
                            initialOffsetX = { fullWidth -> +fullWidth },
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                        exit = slideOutHorizontally(
                            targetOffsetX = { fullWidth -> +fullWidth },
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutLinearInEasing
                            )
                        )
                    ) {
                        //Si estoy en esta pantalla cuando le doy al botón de atrás no quiero cambiar de pantalla
                        //Sino que este menú se vuelva a plegar
                        BackPressHandler(viewModel::searchBackPressed)
                        searchMenu()
                    }

                    //MENU SEARCH
                    AnimatedVisibility(
                        visible = viewModel.enableSettingsMenu,
                        enter = slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                        exit = slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutLinearInEasing
                            )
                        )
                    ) {
                        //Si estoy en esta pantalla cuando le doy al botón de atrás no quiero cambiar de pantalla
                        //Sino que este menú se vuelva a plegar
                        BackPressHandler(viewModel::settingsBackPressed)
                        settingsMenu()
                    }

                    //MENU SETTINGS
                    if (viewModel.gameList.isNotEmpty()) {
                        MainScreenSkin(viewModel.gameList, navController,viewModel.bannerList,viewModel.recommendedList,viewModel::switchSearch,viewModel::switchSettings)
                    }


                }
                2 ->
                {
                    gameList(navController)
                }

            }


        }
    }


    
}

@Composable
fun BottomBar(updateIndex: (Int) -> Unit, viewModel: MainScreenViewModel,showBottom: Boolean) {

    AnimatedVisibility(
        visible = showBottom,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> +fullHeight },
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> +fullHeight  },
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutLinearInEasing
            )
        )
    ){
        BottomNavigation(modifier = Modifier.fillMaxHeight(0.07f),elevation = 10.dp, backgroundColor = Color(0xDD050505)) {

            BottomNavigationItem(icon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.person),"", modifier = Modifier.size(30.dp))

            },
                selected = (viewModel.selectedIndex == 0),
                onClick = {
                    updateIndex(0)
                },
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.White
            )

            BottomNavigationItem(icon = {
                Icon(imageVector = Icons.Default.Home,"")
            },
                selected = (viewModel.selectedIndex == 1),
                onClick = {
                    updateIndex(1)
                },
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.White)

            BottomNavigationItem(icon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.list),"", modifier = Modifier.size(30.dp))
            },
                selected = (viewModel.selectedIndex == 2),
                onClick = {
                    updateIndex(2)
                },
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.White)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreenSkin(gameList: List<Game>,nav:NavController, bannerList : List<GameBanner>, reccommList: List<Game>, switchSearch:() -> Unit, switchSettings:() -> Unit){

    //TOPBAR
    Row(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(5))
            .background(Color(0xFF030303))
    , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(
            Modifier
                .background(Color.Transparent) //Color(0xFF0A0A0A)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(start = 5.dp),
                contentAlignment = Alignment.CenterStart
        ){
            IconButton(onClick = { switchSettings() }) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.settings),"", modifier = Modifier.size(30.dp),tint = Color.White)
            }

        }
        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxHeight()
                .weight(0.50f),
            contentAlignment = Alignment.Center){
            Image(painter = painterResource(R.drawable.logovilipng), contentDescription = "")
        }
        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(end = 5.dp),
            contentAlignment = Alignment.CenterEnd){
            IconButton(onClick = { switchSearch() }) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.search),"", modifier = Modifier.size(30.dp), tint = Color.White)
            }

        }

    }

    LazyColumn(Modifier.fillMaxSize()) {

        item {

            //PRIMERA FILA
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Juegos Nuevos", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(DeviceConfig.heightPercentage(32))

            ) {
                
                LazyRow(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {

                    item {
                        Row() {
                            for (i in 0 until 10) {
                                val game = gameList[i]
                                Surface(elevation = 15.dp) {
                                    gameBox(nav, game.id, game.name, game.imageURL)
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
                    .background(Color(0xFF141414))

            ) {

                LazyRow(
                    Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    item {

                        Column(modifier = Modifier
                            .fillParentMaxSize()
                            .background(Color.Transparent)
                            ){



                            HorizontalPager(count = 3, ) { page ->
                                // Our page content
                                Image(
                                    modifier = Modifier
                                        .padding(top = 20.dp, bottom = 20.dp)
                                        .fillMaxSize()
                                        .clickable {
                                            CentralizedData.updateGameID(bannerList[page].gameID)
                                            nav.navigate(Destinations.Pantalla3.ruta)
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
                    .padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Próximos lanzamientos", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(DeviceConfig.heightPercentage(32))

            ) {

                LazyRow(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {

                    item {
                        Row() {
                            for (i in 10 until 20) {
                                val game = gameList[i]
                                Surface(elevation = 15.dp) {
                                    gameBox(nav, game.id, game.name, game.imageURL)
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
                    .padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Tus recomendados", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(DeviceConfig.heightPercentage(32))

            ) {

                LazyRow(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {

                    item {
                        Row() {
                            for (i in 0 until reccommList.size) {
                                val game = reccommList[i] //TODO INIT DE ESTO
                                Surface(elevation = 15.dp) {
                                    gameBox(nav, game.id, game.name, game.imageURL)
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

@Composable
fun searchMenu(){


    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Red),
    ) {
        Text(text = "aa")
    }
    
}

@Composable
fun settingsMenu(){


    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.6f)
            .background(Color.Red)) {
        Text(text = "aaa")
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







