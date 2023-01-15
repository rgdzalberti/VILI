package viliApp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PreviewMainScreen(){
    mainScreen() //TODO navcontroller
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun mainScreen(){

    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

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

