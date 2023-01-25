package viliApp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.R

@Preview
@Composable
fun previewGameDetails(){
    GameDetails(rememberNavController())
}

@Composable
fun GameDetails(navController:NavController,viewModel: GameDetailsViewModel = hiltViewModel()){

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF161616))) {

        if (viewModel.enableMoreOptions) {
            MoreOptions(viewModel::statusMoreOptions,viewModel::addGameToUserList,viewModel::removeGameFromUserList,viewModel.gameID)
        }

        //TopBar
        Row(
            Modifier
                .fillMaxWidth()
                .height(DeviceConfig.heightPercentage(5))
                .background(Color.Black)) {

            Box(
                Modifier
                    .fillMaxHeight()
                    .weight(0.50f)){
            //Go Back
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrowback),
                    tint = Color.White,
                    contentDescription = "Visibility Icon"
                )
            }
            }

            Box(
                Modifier
                    .fillMaxHeight()
                    .weight(0.50f),
                contentAlignment = Alignment.CenterEnd

            ) {
                //More Options (EDIT)

                IconButton(onClick = {
                    viewModel.statusMoreOptions()
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                        tint = Color.White,
                        contentDescription = "Visibility Icon"
                    )
                }


            }
            
        }
        
        //Cajetilla Portada
        Row(
            Modifier
                .fillMaxWidth()
                .height(DeviceConfig.heightPercentage(30))) {
            Portada(viewModel)

            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(Modifier.matchParentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = viewModel.gameData.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
                Text(text = viewModel.gameData.releaseDate, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.padding(top = 5.dp))
                    if (viewModel.gameData.avgDuration.isNotBlank()) {GenreBox(genre ="${viewModel.gameData.avgDuration} H" , Color.Black)

                    }
                }
            }

        }
        
        //BODY

        //Description
        Column(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(1f)
                .padding(top = 15.dp)
                , horizontalAlignment = Alignment.CenterHorizontally) {

            Box(modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Black)
                .fillMaxHeight()
                .fillMaxWidth(0.97f)
                .padding(5.dp)
                ){
                //TODO responsible height + show more
                Text(text = viewModel.gameData.description, color = Color.White, fontSize = 15.sp)
            }
        }


        //GENEROS
        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(10.dp)
            , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            viewModel.listaGenres.forEach {
                //Primero chekio que no es el quinto index para adelante
                val index = viewModel.listaGenres.indexOf(it)
                if (index < 4) {GenreBox(genre = it)} else {
                    //Le digo que hay más de 4 y que lo ponga en una segunda fila
                    viewModel.updateMoreGenres(true)
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }

        if (viewModel.moreGenres) {
            Row(
                Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                for (i in 4 until viewModel.listaGenres.size){
                    GenreBox(genre = viewModel.listaGenres[i])
                    Spacer(modifier = Modifier.width(10.dp))
                }

            }
        }
        
    }

}

@Composable
fun Portada(viewModel:GameDetailsViewModel){
    
    Box(modifier = Modifier
        .height(220.dp)
        .width(160.dp)
        .padding(5.dp)
        .background(Color.Black)){
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberAsyncImagePainter(viewModel.gameData.imageURL),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }

    
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

@Composable
fun GenreBox(genre: String, color: Color = Color.Black){

    Box(
        Modifier
            .clip(RoundedCornerShape(3.dp))
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text = " " + genre + " ", color = Color.White, fontSize = 15.sp)
    }

}