package viliApp

import android.util.Log
import android.util.Size
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size.Companion.ORIGINAL
import com.example.vili.R
import java.net.URL


@Preview
@Composable
fun previewGameList(){
    gameList()
}

@Composable
fun gameList(){

    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

    //LazyColumn de Headers y Bodies
    //entryHeader()

    Column(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(80))
            .background(Color.Black)
            .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally)
    {
        Row()
        {
            entryImage()
            Spacer(modifier = Modifier.width(30.dp))
            entryImage()
        }
    }
    


}

@Composable
fun entryImage(titulo:String = "",imageURL:String = "",Rating:Int = -1){ //TODO quitar defaults de titulo e imageURL
    
    Box(modifier = Modifier
        .background(Color.Green)
        .height(DeviceConfig.heightPercentage(30))
        .width(DeviceConfig.widthPercentage(40)),
        contentAlignment = Alignment.BottomCenter
    ){
        Image(modifier = Modifier.fillMaxSize().clickable{ TODO("Te lleva al menu del juego")  },painter = rememberAsyncImagePainter("https://howlongtobeat.com/games/41753_The_Last_of_Us_Part_II.jpg"), contentDescription = null, contentScale = ContentScale.Crop)

        Column(
            Modifier
                .fillMaxWidth()
                .height(55.dp) //TODO if no rating entonse restarle a esto la mitad o por ahi
                .background(Color(0x860A0A0A))) {
            Text(text = "The Last Of Us Part II",modifier = Modifier.padding(start = 5.dp, top = 5.dp), color = Color.White) //TODO insertar titulo
            if (Rating != -1) {
                Text(
                    text = "${repeat(Rating){"★"}}", //TODO poner estrellas
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.White
                )
            }
        }
    }
    
}

@Preview
@Composable
fun entryHeader(){

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(DeviceConfig.heightPercentage(20))
        .background(Color.Red)
    ){

        Row(
            Modifier
                .fillMaxSize()
                .background(Color.Transparent), verticalAlignment = Alignment.CenterVertically) {
            //AsyncImage(model = "https://howlongtobeat.com/games/41753_The_Last_of_Us_Part_II.jpg?width=250", contentDescription = "game image")
            Image(painter = rememberAsyncImagePainter("https://howlongtobeat.com/games/41753_The_Last_of_Us_Part_II.jpg?width=850"), contentDescription = null)
            //Contentsacle = contentscale.crop , modifier size, .clip
        }

    }
}