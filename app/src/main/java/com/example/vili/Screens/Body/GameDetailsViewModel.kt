package viliApp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import viliApp.CentralizedData.Companion.getGameID
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var gameID by savedStateHandle.saveable { mutableStateOf("") }
    var gameData by savedStateHandle.saveable { mutableStateOf(Game()) }
    var enableMoreOptions by savedStateHandle.saveable { mutableStateOf(false) }

    init {
        //Obtengo la referencia del juego primero en mi clase centralizada

        gameID = getGameID()

        //Hago una query en busca de información sobre este juego
        viewModelScope.launch{
            FBQuery.getGame(gameID).collect {gameData = it}

        }

    }

    fun statusMoreOptions(){
        enableMoreOptions = !enableMoreOptions
    }

    fun addGameToUserList(score: Int = 0, comment: String = ""){
        FBQuery.saveGameToUserList(gameID, score, comment)
    }

    fun removeGameFromUserList(gameID: String){

        var wasGameInList = false

        viewModelScope.launch {

            FBQuery.removeGameFromUserList(gameID)
                .onCompletion {
                    if (wasGameInList == false) {
                        //TODO LANZAR TOAST DE NO PUEDES ELIMINAR UN JUEGO QUE NO ESTÁ EN TU LISTA
                        //Ademas no estaria nada mal arreglarlo q no funca
                        Log.i("wawa", "Este juego no está en tu lista")
                    }

                    //Se recompone la lista para que esté actualizada en caso de que vuelva
                    CentralizedData.updateRecomposeList()
                }
                .collect {
                    wasGameInList = it
                }
        }

    }

}