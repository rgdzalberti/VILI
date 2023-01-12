package viliApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewMainScreen(){
    mainScreen() //TODO navcontroller
}

@Composable
fun mainScreen(){

    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

    Column(Modifier.fillMaxSize().background( Color(0xFF0A0A0A))) {
        
    }

    //TODO Footer
    //TODO desplegable izq
    
}