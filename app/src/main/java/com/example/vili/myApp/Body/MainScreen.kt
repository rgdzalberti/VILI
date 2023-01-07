package viliApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun PreviewMainScreen() {
    mainScreen(rememberNavController())
}

@Composable 
fun mainScreen(navController: NavController){
    
    Column(Modifier.fillMaxSize().background(Color(0xFF0A0A0A))) {
        
    }
    
}

