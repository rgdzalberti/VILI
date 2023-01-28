package viliApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LazyList(nav: NavController, searchText: String = "", gameList: List<Game> = emptyList(), userGameList: List<UserGame> = emptyList(), isGameListB: Boolean = false, isUserGameListB: Boolean = false){

    //TODO PADDING + ABAJO DPS

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF0A0A0A))
        .padding(top = 15.dp)) {

        //Primero hago una lista filtrando el término de búsqueda (Para GameList)
        var gameListFiltered = filterByText(gameList,searchText)

        var repeatTimes =
            when{
                isGameListB -> {
                    if (gameListFiltered.size % 2 == 0) gameListFiltered.size / 2 else ((gameListFiltered.size - 1) / 2)
                }
                isUserGameListB -> {
                    if (userGameList.size % 2 == 0) userGameList.size / 2 else ((userGameList.size - 1) / 2)
                }
                else -> {0}
            }

        //Content
        //Si existen juegos filtrados por texto entonces se muestran, como alternativa si se ha introducido un userGameList se muestra también
        if (gameListFiltered.size != 0 && isGameListB || userGameList.size != 0 && isUserGameListB) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                var index = 0

                item {
                    repeat(repeatTimes) {

                        Row() {
                            Surface(elevation = 15.dp) {

                                if (isGameListB) {
                                    GameBox(
                                        nav,
                                        gameListFiltered[index].id,
                                        gameListFiltered[index].name,
                                        gameListFiltered[index].imageURL
                                    )
                                }
                                else if (isUserGameListB){
                                    GameBox(
                                        nav,
                                        userGameList[index].id,
                                        userGameList[index].name,
                                        userGameList[index].imageURL,
                                        userGameList[index].userScore.toInt()
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(30.dp))
                            Surface(elevation = 15.dp) {
                                if (isGameListB) {
                                    GameBox(
                                        nav,
                                        gameListFiltered[index + 1].id,
                                        gameListFiltered[index + 1].name,
                                        gameListFiltered[index + 1].imageURL
                                    )
                                }
                                else if (isUserGameListB){
                                    GameBox(
                                        nav,
                                        userGameList[index + 1].id,
                                        userGameList[index + 1].name,
                                        userGameList[index + 1].imageURL,
                                        userGameList[index + 1].userScore.toInt()
                                    )
                                }
                            }

                            index = index + 2

                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    //Si es un solo elemento, entonces salta esto
                    if (gameListFiltered.size % 2 != 0 || userGameList.size % 2 != 0) {

                        Row() {
                            Surface(elevation = 15.dp) {
                                if (isGameListB) {
                                    GameBox(
                                        nav,
                                        gameListFiltered[gameListFiltered.size - 1].id,
                                        gameListFiltered[gameListFiltered.size - 1].name,
                                        gameListFiltered[gameListFiltered.size - 1].imageURL
                                    )
                                }
                                else if (isUserGameListB){
                                    GameBox(
                                        nav,
                                        userGameList[userGameList.size - 1].id,
                                        userGameList[userGameList.size - 1].name,
                                        userGameList[userGameList.size - 1].imageURL,
                                        userGameList[userGameList.size - 1].userScore.toInt()
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.width(30.dp))
                            GameBox(nav, "", "", "", 0, true)
                        }

                    }


                }


            }

        }
    }
}

@Composable
fun filterByText(gameList: List<Game>, searchText: String): List<Game>{

    var gameListFiltered = mutableListOf<Game>()

    gameList.forEach {
        if (it.name.lowercase().contains(searchText.lowercase())) {
            gameListFiltered.add(it)
        }
    }

    if (gameListFiltered.size != 0 ){
        gameListFiltered = gameListFiltered.sortedBy { it.name } as MutableList<Game>
    }

    return gameListFiltered
}