package viliApp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.internal.Contexts.getApplication

@Preview
@Composable
fun PreviewMainScreen(){
    MainScreen(rememberNavController())
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController, viewModel: MainScreenViewModel = hiltViewModel() ){

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
                1 -> {
                    Text(text = "aaaa")}
                2 ->
                {
                    gameList()
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

