package viliApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.vili.R

@Preview
@Composable
fun previewGameDetails(){
    GameDetails()
}

@Composable
fun GameDetails(viewModel: GameDetailsViewModel = hiltViewModel()){

    //TODO METERLE UNA SKIN WAPA A ESTO


    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Yellow)) {

        if (viewModel.enableMoreOptions) {MoreOptions(viewModel::statusMoreOptions,viewModel::addGameToUserList,viewModel::removeGameFromUserList,viewModel.gameID)}

        //TopBar
        Column(
            Modifier
                .fillMaxWidth()
                .height(DeviceConfig.heightPercentage(5))
                .background(Color.Black)) {

            IconButton(onClick = {
                viewModel.statusMoreOptions()
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.keypass),
                    tint = Color.White,
                    contentDescription = "Visibility Icon"
                )
            }
            
        }
        
        //Cajetilla Portada
        Row(
            Modifier
                .fillMaxWidth()
                .height(DeviceConfig.heightPercentage(40))) {
            Portada(viewModel)
            Text(text = "aaa")
        }

        Text(text = viewModel.gameData.description)
        Text(text = viewModel.gameData.avgDuration)
        Text(text = viewModel.gameData.genres)
        Text(text = viewModel.gameData.releaseDate.toString())
        
        
    }

}

@Composable
fun Portada(viewModel:GameDetailsViewModel){
    Image(
        modifier = Modifier
            .height(DeviceConfig.heightPercentage(30)),
        painter = rememberAsyncImagePainter(viewModel.gameData.imageURL),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

//TODO ABRIR MENU METER EN LISTA CHULON CHULON
@Composable
fun MoreOptions(disableMoreOptions: () -> Unit, saveToUserList: ()-> Unit, deleteFromUserList: (String)-> Unit,gameID: String){

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(Color(0x540A0A0A)) //TODO CAMBIAR ESTE COLOR A UNO CHULON ALOMEJON
            .zIndex(1f), contentAlignment = Alignment.Center) {

        Box(
            Modifier
                .width(DeviceConfig.widthPercentage(80))
                .height(DeviceConfig.heightPercentage(40))
                .background(Color.Red)) {
            Text(text = "aaa")


            Column() {
                Button(onClick = { disableMoreOptions() }) {

                }

                Button(onClick = { saveToUserList() }) {
                    Text("AÑADIR A LISTA USUARIO")
                }

                Button(onClick = { deleteFromUserList(gameID) }) {
                    Text("BORRAR DE LA LISTA USUARIO")
                }
            }



        }

    }

}