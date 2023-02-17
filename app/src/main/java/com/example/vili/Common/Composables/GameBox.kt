package viliApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun GameBox(
    nav: NavController,
    key: String = "",
    titulo: String = "",
    imageURL: String = "",
    Rating: Int = 0,
    invisible: Boolean = false
) {

    var rating = ""
    repeat(Rating) { rating += "★" }

    var modTitle = if (titulo.length >= 17) {"${titulo.subSequence(0,17)}..."} else titulo

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
                        CentralizedData.updateGameID(key)
                        nav.navigate(Destinations.Pantalla3.ruta)
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
                text = modTitle,
                maxLines = 1,
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