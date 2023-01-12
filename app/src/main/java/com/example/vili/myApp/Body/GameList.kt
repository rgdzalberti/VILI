package viliApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@Preview
@Composable
fun previewGameList() {
    gameList()


}

@Composable
fun gameList() {

    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

    //LazyColumn de Headers y Bodies
    //entryHeader()

    Column(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(100))
            .background(Color(0xFF161616))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {


            LazyColumn(modifier = Modifier.fillMaxSize()) {

                repeat(6) {      //TODO eliminar repeat, solo es para debug <- Implementar correctamente
                    item {
                        Row()
                        {
                            Surface(elevation = 15.dp) {
                                gameBox()
                            }

                            Spacer(modifier = Modifier.width(30.dp))
                            gameBox()
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }


                //TODO bucle rescatando datos de la BBDD cada multiplo de 3 es un spacer y cada 4 un salto de linea o por ahi
            }


    }


}

@Composable
fun gameBox(
    titulo: String = "",
    imageURL: String = "",
    Rating: Int = 0
) { //TODO quitar defaults de titulo e imageURL y añadir key del juego

    var rating = ""
    repeat(Rating) { rating += "★" }



    Box(
        modifier = Modifier
            .background(Color.Black)
            .height(DeviceConfig.heightPercentage(30))
            .width(DeviceConfig.widthPercentage(40)),
        contentAlignment = Alignment.BottomCenter
    ) {

        //Animación de carga mientras no hay imagenes //TODO quizas parar la animacion de carga tras un tiempo?
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier, Color.Red)
        }

        //Imagen
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clickable { TODO("Te lleva al menu del juego") },
            painter = rememberAsyncImagePainter("https://howlongtobeat.com/games/41753_The_Last_of_Us_Part_II.jpg"),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        //Columna semitransparente con el nombre y puntuación
        Column(
            Modifier
                .fillMaxWidth()
                .height(47.dp) //TODO if no rating entonse restarle a esto la mitad o por ahi
                .background(Color(0x860A0A0A))
        ) {

            Text(
                text = "The Last Of Us Part II",
                modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                color = Color.White
            ) //TODO insertar titulo
            if (Rating != 0) {
                Text(
                    text = rating,
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.White
                )
            }
        }
    }

}


//DEPRECATED
@Preview
@Composable
fun entryHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(20))
            .background(Color.Red)
    ) {

        Row(
            Modifier
                .fillMaxSize()
                .background(Color.Transparent), verticalAlignment = Alignment.CenterVertically
        ) {
            //AsyncImage(model = "https://howlongtobeat.com/games/41753_The_Last_of_Us_Part_II.jpg?width=250", contentDescription = "game image")
            Image(
                painter = rememberAsyncImagePainter("https://howlongtobeat.com/games/41753_The_Last_of_Us_Part_II.jpg?width=850"),
                contentDescription = null
            )
            //Contentsacle = contentscale.crop , modifier size, .clip
        }

    }
}
