package viliApp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun PreviewMainScreen() {
    mainScreen(rememberNavController())
}

@Composable 
fun mainScreen(navController: NavController){

    //Llamo a mi funcion que cambia el color de la barra y la función que obtiene los dp de la pantalla
    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))) {

        myGameList()
    }
    
}

@Composable
fun myGameList(){

    //Container de la lista
    Column(
        Modifier
            .height(DeviceConfig.heightPercentage(30))
            .fillMaxWidth()
            .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally) {

        //Container de elementos de entrada
        entradaContainer()


    }
    

    
}

//Parametros entrada URL IMAGEN, TITULO, DESCRIPCION, SCORE TODO
@Composable
fun entradaContainer() {

    //El size de una entrada para calcular correctamente los elementos contenidos por la entrada
    var size by remember { mutableStateOf(Pair<Dp,Dp>(0.dp,0.dp)) } //HeightXWidth

    //Container de elementos de entrada
    Row(
        Modifier
            .width(DeviceConfig.widthPercentage(100))
            .height(DeviceConfig.heightPercentage(10))
            .background(Color.Red)
            .onSizeChanged {
                size = Pair((it.height / DeviceConfig.dpi).dp, (it.width / DeviceConfig.dpi).dp);
            }, verticalAlignment = Alignment.CenterVertically
    ) {

        //Container Imagen
        entradaImage(size)

        //Container de los demás elementos, pues estarán a la misma altura centrados
        Column(
            Modifier
                .background(Color.Yellow)
                .width(
                    DeviceConfig.DPwidthPercentage(
                        size.second - DeviceConfig.widthPercentage(20),
                        98
                    )

                )
                .height(DeviceConfig.heightPercentage(5)), verticalArrangement = Arrangement.Center) { // A el width del padre le resto el width que ocupa ya la imagen


            Row(
                Modifier
                    .height(DeviceConfig.heightPercentage(5))
                    .background(Color.Green), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "TITULO") //TODO TITULO

                Row(Modifier.width(400.dp),horizontalArrangement = Arrangement.End){
                    //Container Comentario
                    Column(
                        Modifier
                            .background(Color.Gray)
                            .height(DeviceConfig.heightPercentage(5)), verticalArrangement = Arrangement.Center) {
                        Text(text = "aaaa") //TODO COMENTARIO
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Column(
                        Modifier
                            .background(Color.Magenta)
                            .height(DeviceConfig.heightPercentage(5)), verticalArrangement = Arrangement.Center) {
                        Text(text = "yoshi") //TODO ESTRELLAS
                    }
                }

            }

        }

    }
}

    @Composable
    fun entradaImage(size : Pair<Dp,Dp>) {
        Column(
            Modifier
                .height(
                    DeviceConfig.DPheightPercentage(
                        size.first,
                        100
                    )

                ) //Calculo el espacio relativo al padre  en % para que se vea igual en todos los dispositivos
                .width(DeviceConfig.widthPercentage(20))
                .clip(RoundedCornerShape(20.dp))
                .padding(5.dp)
                .background(Color.Blue),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "a")
            //Image TODO con modifier clickable para q te lleve a otro menu
        }
    }



