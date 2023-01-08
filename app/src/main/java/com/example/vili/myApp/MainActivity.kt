package com.example.vili.myApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.chaquo.python.Python
import com.example.vili.myApp.theme.VILITheme
import viliApp.NavigationHost
import viliApp.getDeviceConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                MainFun()

                }

        }

}


@Preview
@Composable
fun MainFun(){
    val navController = rememberNavController()
    NavigationHost(navController = navController)
}