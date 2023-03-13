package viliApp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.myApp.theme.LightBlack

fun <T: Any> filterByText(filterText: String, genericList: List<T>, selector: (T) -> String): List<T> {
    return genericList.filter { selector(it).contains(filterText, ignoreCase = true) }
        .sortedWith(compareBy(selector))
}

@Composable
fun GameLazyRow(title:String="",nav:NavController,gameList:List<Game>){

    if (title.isNotBlank()) {Text(modifier = Modifier.padding(5.dp), text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)}

    LazyRow(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(32))
            .padding(5.dp)){
        items(gameList.size) { item->
            Surface(elevation = 15.dp, modifier = Modifier.padding(end = 8.dp)) {
                GameBox(nav = nav,gameList[item].id,gameList[item].name,gameList[item].imageURL)
            }
        }
    }

}

@Composable
fun GameLazyList(nav: NavController,searchText: String,gameList: List<Game>, sortSettings:Boolean = false){

    val filteredList =  if (!sortSettings) {(filterByText(searchText, gameList) { it.name })} else {gameList}

    LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2), contentPadding = PaddingValues(bottom = 50.dp)) {
        items(filteredList.size) { item ->
            Surface(
                modifier = Modifier
                    .height(DeviceConfig.heightPercentage(35))
                    .padding(10.dp),
                elevation = 5.dp,
            ) {
                GameBox(nav = nav,filteredList[item].id,filteredList[item].name,filteredList[item].imageURL)
            }
        }
    }

}


@Composable
fun UserGameLazyList(nav: NavController,searchText: String,userGameList: List<UserGame>, sortSettings:Boolean = false){

    val filteredList =  if (!sortSettings) {(filterByText(searchText, userGameList) { it.name })} else {userGameList}

    LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2), contentPadding = PaddingValues(bottom = 50.dp)) {
        items(filteredList.size) { item ->
            Surface(
                modifier = Modifier
                    .height(DeviceConfig.heightPercentage(35))
                    .padding(10.dp),
                elevation = 5.dp,
            ) {
                GameBox(nav = nav,filteredList[item].id,filteredList[item].name,filteredList[item].imageURL,filteredList[item].userScore.toInt())
            }
        }
    }
}

@Composable
fun UserLazyList(nav: NavController, searchText: String, userProfileList: List<UserProfile>) {

    val filteredList = filterByText(searchText, userProfileList) { it.name }

    LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)) {
        items(filteredList.size) { item ->
            Surface(
                modifier = Modifier
                    .height(DeviceConfig.heightPercentage(35))
                    .padding(10.dp),
                elevation = 5.dp,
            ) {
                UserBox(nav,url = filteredList[item].imageURL, name = filteredList[item].name.take(6), id = filteredList[item].name)
            }
        }
    }
}

