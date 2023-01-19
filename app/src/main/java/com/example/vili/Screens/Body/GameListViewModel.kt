package viliApp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    //Si la lista esta vacía realiza una query que inicialmente está sorted por nombre
    var gameList by savedStateHandle.saveable { mutableStateOf(mutableListOf<GameUserUnion>()) }

    init {
        var userGameList by savedStateHandle.saveable { mutableStateOf(mutableListOf<UserGameEntry>()) }
        var gameDataList by savedStateHandle.saveable { mutableStateOf(mutableListOf<Game>()) }
        //Si esta vacía la lista (no está guardada en cache) se rescatan los datos de la red
        if (gameList.isEmpty()) {

            viewModelScope.launch {

                //Colecciono userGameList y gameDataList en ese orden, cuando el segundo ha terminado
                //Recupero la lista final, GameUserUnion
                FBQuery.getUserGameList()
                    .onCompletion {
                        userGameList.forEach {
                            FBQuery.getGame(it.gameID).onCompletion {
                                if (gameDataList.size == userGameList.size) {
                                    //AQUÍ termina la segunda colección e inicia la final
                                    gameList = CentralizedData.convertToGameUserUnion(gameDataList,userGameList).sortedBy { it.name } as MutableList<GameUserUnion>
                                }
                            }
                                .collect { gameDataList.add(it) }
                        }
                    }
                    .collect {
                        userGameList = it as MutableList<UserGameEntry>
                    }

                /*
                FBQuery.saveGameToUserList("DCj5eN7di9CnLh5C5FHY")
                FBQuery.saveGameToUserList("cAx4JHxRo5HTogqYSQ9B")
                FBQuery.saveGameToUserList("muQ4vWcKWOHnrzVKk4sg")
                */

                //TODO BORRAR ESTO DE ARRIBA
            }
        }

    }

    //TODO SORT GAMELIST DIFFERENT NO SE Q
    fun sortListByScore() {
        gameList = gameList.sortedBy { it.score } as MutableList<GameUserUnion>
    }

    fun reloadGameList(){

        var userGameList = mutableListOf<UserGameEntry>()
        var gameDataList = mutableListOf<Game>()
        //Si esta vacía la lista (no está guardada en cache) se rescatan los datos de la red

        viewModelScope.launch {
            //Colecciono userGameList y gameDataList en ese orden, cuando el segundo ha terminado
            //Recupero la lista final, GameUserUnion
            FBQuery.getUserGameList()
                .onCompletion {
                    userGameList.forEach {
                        FBQuery.getGame(it.gameID).onCompletion {
                            if (gameDataList.size == userGameList.size) {
                                //AQUÍ termina la segunda colección e inicia la final
                                gameList = CentralizedData.convertToGameUserUnion(gameDataList,userGameList).sortedBy { it.name } as MutableList<GameUserUnion>
                            }
                        }
                            .collect { gameDataList.add(it) }
                    }
                }
                .collect {
                    //Con este if soluciono el bug de cuando solo hay 1 elemento y se elimina no se borra de la lista UI
                    if (it.isEmpty()) {  gameList = mutableListOf<GameUserUnion>()}

                    userGameList = it as MutableList<UserGameEntry>
                }


        }

        CentralizedData.tellGameListToReload()

    }




}
