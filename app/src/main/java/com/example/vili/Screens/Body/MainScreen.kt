package viliApp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

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


    //TODO desplegable izq
    Scaffold(bottomBar = {BottomBar(viewModel::updateIndex,viewModel)}) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))) {


            //Depende del index seleccionado abajo la pantalla se recomposeará

            //TODO CONSISTENCIA DE COLORES SWIPE MENU QUIZÁS
            when(viewModel.selectedIndex){

                0 -> {}
                1 -> 
                {
                    //TODO TOPBAR para buscar guejos
                    if (viewModel.gameList.isNotEmpty()) {
                        MainScreenSkin(viewModel.gameList, navController)
                    }
                }
                2 ->
                {
                    //TODO barra de abajo desaparece y aparece la de la fun animacion
                    gameList(navController)
                }

            }


        }
    }


    
}

@Composable
fun BottomBar(updateIndex: (Int) -> Unit, viewModel: MainScreenViewModel) {

    BottomNavigation(elevation = 10.dp, backgroundColor = Color.Black) {

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home,"") //TODO cambiar iconos
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
            Icon(imageVector = Icons.Default.ThumbUp,"")
        },
            selected = (viewModel.selectedIndex == 2),
            onClick = {
                updateIndex(2)
            },
            selectedContentColor = Color.Red,
            unselectedContentColor = Color.White)
    }
}

@Composable
fun MainScreenSkin(gameList: List<Game>,nav:NavController){

        LazyRow(
            Modifier
                .fillMaxSize()
                .padding(8.dp)) {

            item {
                Row() {
                    for (i in 0 until 5) {
                        val game = gameList[i]
                        gameBox(nav,game.id,game.name,game.imageURL)
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                }
            }

        }
    
}




