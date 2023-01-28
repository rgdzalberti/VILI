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

    var listaGenres = listOf<String>()
    var moreGenres by savedStateHandle.saveable { mutableStateOf(false) }

    //Valor del dropdown
    var ddValue by savedStateHandle.saveable { mutableStateOf(0) }

    //MoreOptions
    var completed by savedStateHandle.saveable { mutableStateOf(false) }
    var planned by savedStateHandle.saveable { mutableStateOf(false) }

    init {
        //Obtengo la referencia del juego primero en mi clase centralizada

        gameID = getGameID()

        //Hago una query en busca de información sobre este juego
        viewModelScope.launch{
            FBQuery.getGame(gameID)
                .collect {
                    gameData = it; listaGenres = gameData.genres.split(",")

                    //Inicializo por primera vez mis variables completed y planned aquí
                    completed = CentralizedData.gameList.value.any { it.id == gameID }
                    planned = CentralizedData.planningList.value.any { it.id == gameID }
                }
        }

    }

    fun statusMoreOptions(){
        enableMoreOptions = !enableMoreOptions
    }

    //COMPLETED
    fun addGameToUserList(score: Int = 0, comment: String = ""){
        FBQuery.saveGameToUserList(gameData, score, comment)
    }

    fun removeGameFromUserList(gameID: String){

        var wasGameInList = false
        FBQuery.removeGameFromUserList(gameID)

    }
    //PLANNING
    fun addGameToUserPlanningList(score: Int = 0, comment: String = ""){
        FBQuery.saveGameToUserPlanningList(gameData)
    }

    fun removeGameFromUserPlanningList(gameID: String){

        var wasGameInList = false
        FBQuery.removeGameFromUserPlanningList(gameID)

    }

    fun updateMoreGenres(newValue: Boolean){
        moreGenres = newValue
    }

    fun dropDownValue(dropIndex: Int){
        ddValue = dropIndex
    }

    fun updateCompleted(){
        completed = !completed
    }

    fun updatePlanned(){
        planned = !planned
    }

}