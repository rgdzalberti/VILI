package viliApp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.Common.Complex.GameBoxViewModel
import com.example.vili.myApp.theme.LightBlack

@Composable
fun GameBox(
    nav: NavController,
    key: String = "",
    titulo: String = "",
    imageURL: String = "",
    Rating: Int = 0,
    viewModel: GameBoxViewModel = hiltViewModel()
) {

    var rating = ""
    repeat(Rating) { rating += "★" }

    var modTitle = if (titulo.length >= 17) {
        "${titulo.subSequence(0, 15)}..."
    } else titulo

    Box(
        modifier = Modifier
            .background(Color.Black)
            .height(DeviceConfig.heightPercentage(30))
            .width(DeviceConfig.widthPercentage(40))
            .alpha(1f),
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
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clickable
                {
                    if (viewModel.clickable) {
                        nav.navigate("${Destinations.Pantalla3.ruta}/${key}")
                        viewModel.updatePendingValues()
                    }
                },
            painter = rememberAsyncImagePainter(imageURL),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )


        //Columna semitransparente con el nombre y puntuación
        Column(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
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
                    modifier = Modifier.padding(start = 5.dp, bottom = 0.dp),
                    color = Color.White
                )
            }
        }
    }


}

@Composable
fun UserBox(nav:NavController,url: String, name: String, id:String) {
    Box(
        Modifier
            .fillMaxSize()
            .clickable { nav.navigate(route = "${Destinations.Profile.ruta}/${id}") }) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlack),
            painter = rememberAsyncImagePainter(model = url),
            contentDescription = "User PFP",
            contentScale = ContentScale.FillBounds
        )


        Column(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0x860A0A0A))
                .align(Alignment.BottomStart)
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = name,
                maxLines = 1,
                color = Color.White
            )
        }

    }
}

