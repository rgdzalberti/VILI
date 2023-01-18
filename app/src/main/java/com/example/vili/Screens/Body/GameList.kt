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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.Screens.Body.GameListViewModel
import viliApp.CentralizedData.Companion.updateGameID


@Preview
@Composable
fun previewGameList() {
    gameList(rememberNavController())
}

@Composable
fun gameList(navController: NavController,viewModel: GameListViewModel = hiltViewModel()) {

    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

    //TODO hacer un topbar en condiciones
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(DeviceConfig.heightPercentage(10))
        .background(Color.Red)) {
        Button(onClick = { viewModel.sortListByScore()}) {

        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(100))
            .background(Color(0xFF161616))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        if (viewModel.gameList.size != 0) {
            CalculateGamesContent(gameList = viewModel.gameList,navController)
        }
    }

}

@Composable
fun gameBox(
    nav: NavController,
    key: String = "",
    titulo: String = "",
    imageURL: String = "",
    Rating: Int = 0,
    invisible: Boolean = false
) {

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

            //Animación de carga mientras no hay imagenes
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
                        .clickable {
                            updateGameID(key)
                            nav.navigate(Destinations.Pantalla3.ruta)
                            //TODO("Te lleva al menu del juego")
                        },
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
fun CalculateGamesContent(gameList: MutableList<GameUserUnion>, nav: NavController) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var index = 0

        //Como los muestro en rows de 2 miro la longitud de la lista y la divido en 2
        //Además le sumo uno por si se queda en decimal que tire hacia arriba
        repeat(if (gameList.size%2==0) gameList.size / 2 else (gameList.size / 2) + 1) {


            item {
                Row()
                {

                    Surface(elevation = 15.dp) {
                        gameBox(
                            nav,
                            gameList[index].gameID,
                            gameList[index].name,
                            gameList[index].imageURL
                        )
                    }

                    Spacer(modifier = Modifier.width(30.dp))

                    //Si no existe este index se muestra una caja invible y no interactuable para mantener la interfaz organizada
                    if (index != gameList.size - 1) {
                        Surface(elevation = 15.dp) {
                            gameBox(
                                nav,
                                gameList[index + 1].gameID,
                                gameList[index + 1].name,
                                gameList[index + 1].imageURL
                            )
                        }
                    }
                    else
                    {
                        gameBox(nav,"","","",0, true)
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))

                index = index + 2

            }
        }

    }
}

    
