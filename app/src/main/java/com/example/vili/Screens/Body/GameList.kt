package viliApp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.saveable
import coil.compose.rememberAsyncImagePainter
import com.example.vili.Model.Querys.Game
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


@Preview
@Composable
fun previewGameList() {
    gameList()

}

@Composable
fun gameList() {

    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

    var gameList by remember { mutableStateOf( FBQuery.launchReturnGameList().sortedBy { it.name }) } //TODO añadir opciones recomopsables en un viewmodel quizas?


    Column(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(100))
            .background(Color(0xFF161616))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        CalculateGamesContent(gameList = gameList)
    }

}

@Composable
fun gameBox(
    titulo: String = "",
    imageURL: String = "",
    Rating: Int = 0,
    invisible: Boolean = false
) { //TODO quitar defaults de titulo e imageURL y añadir key del juego

    var rating = ""
    repeat(Rating) { rating += "★" }


        Box(
            modifier = Modifier
                .background(if (invisible) Color.Transparent else Color.Black)
                .height(DeviceConfig.heightPercentage(30))
                .width(DeviceConfig.widthPercentage(40))
                .alpha(if (invisible) 0f else 1f)
            ,
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

            //Imagen - Si esta invisible no se muestra para evitar el click null
            if (!invisible) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { TODO("Te lleva al menu del juego") },
                    painter = rememberAsyncImagePainter(imageURL),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            //Columna semitransparente con el nombre y puntuación
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(47.dp)
                    .background(Color(0x860A0A0A))
            ) {

                Text(
                    text = titulo,
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                    color = Color.White
                )
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

@Composable
fun CalculateGamesContent(gameList: List<Game>) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var index = 0

        //Como los muestro en rows de 2 miro la longitud de la lista y la divido en 2
        //Además le sumo uno por si se queda en decimal que tire hacia arriba
        repeat((gameList.size / 2)+1) {


            item {
                Row()
                {

                    Surface(elevation = 15.dp) {
                        gameBox(
                            gameList[index].name,
                            gameList[index].imageURL
                        )
                    }

                    Spacer(modifier = Modifier.width(30.dp))

                    //Si no existe este index se muestra una caja invible y no interactuable para mantener la interfaz organizada
                    if (index != gameList.size - 1) {
                        Surface(elevation = 15.dp) {
                            gameBox(
                                gameList[index + 1].name,
                                gameList[index + 1].imageURL
                            )
                        }
                    }
                    else
                    {
                            gameBox(
                                "","",0, true
                            )

                    }



                }
                Spacer(modifier = Modifier.height(20.dp))

                index = index + 2

            }
        }

    }
}

    
