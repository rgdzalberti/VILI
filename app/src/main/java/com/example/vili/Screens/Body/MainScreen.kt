package viliApp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
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
fun MainScreen(navController: NavController ){

    getDeviceConfig()
    systemBarColor(color = Color(0xFF0A0A0A))


    //TODO desplegable izq

    Scaffold(bottomBar = {BottomBar()}) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))) {


        }
    }


    
}

@Composable
fun BottomBar() {
    val selectedIndex = remember { mutableStateOf(1) }
    BottomNavigation(elevation = 10.dp, backgroundColor = Color.Black) {

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home,"") //TODO cambiar iconos
        },
            selected = (selectedIndex.value == 0),
            onClick = {
                selectedIndex.value = 0
            },
            selectedContentColor = Color.Red,
            unselectedContentColor = Color.White
        )

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home,"")
        },
            selected = (selectedIndex.value == 1),
            onClick = {
                selectedIndex.value = 1
            },
            selectedContentColor = Color.Red,
            unselectedContentColor = Color.White)

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Person,"")
        },
            selected = (selectedIndex.value == 2),
            onClick = {
                selectedIndex.value = 2
            },
            selectedContentColor = Color.Red,
            unselectedContentColor = Color.White)
    }
}

