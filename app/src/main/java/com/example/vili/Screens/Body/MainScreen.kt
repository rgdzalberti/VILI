package viliApp

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

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

    Scaffold(bottomBar = {BottomBar(viewModel::updateIndex,viewModel)}) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))) {


            //Depende del index seleccionado abajo la pantalla se recomposeará

            when(viewModel.selectedIndex){

                0 -> {}
                1 -> 
                {
                    //TODO TOPBAR para buscar guejos
                    if (viewModel.gameList.isNotEmpty()) {
                        MainScreenSkin(viewModel.gameList, navController,viewModel.bannerList,viewModel.recommendedList)
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
fun BottomBar(updateIndex: (Int) -> Unit, viewModel: MainScreenViewModel) {

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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreenSkin(gameList: List<Game>,nav:NavController, bannerList : List<GameBanner>, reccommList: List<Game>){

    //TOPBAR
    Row(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(5))
            .background(Color(0xFFF70000))
    , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(
            Modifier
                .background(Color.Green) //Color(0xFF0A0A0A)
                .fillMaxHeight()
                .weight(0.33f),
            contentAlignment = Alignment.Center
        ){
            Text(text = "aa")
        }
        Box(
            Modifier
                .background(Color.Red)
                .fillMaxHeight()
                .weight(0.50f),
            contentAlignment = Alignment.Center){
            Text(text = "aa")
        }
        Box(
            Modifier
                .background(Color.Green)
                .fillMaxHeight()
                .weight(0.33f),
            contentAlignment = Alignment.Center){
            Text(text = "aa")
        }
        /* BARRA BUSQUEDA SEGUIR CON EL BIEN
        Box(Modifier.fillMaxHeight(0.7f).fillMaxWidth(0.6f).background(Color.Green)){
            Text(text = "aaa")
            //TODO TEXTFIELD
        }

         */
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




