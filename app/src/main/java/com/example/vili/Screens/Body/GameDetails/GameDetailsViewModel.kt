package com.example.vili.Screens.Body.GameDetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.vili.Model.Querys.FBAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import viliApp.FBCRUD
import viliApp.Game
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var gameData by savedStateHandle.saveable { mutableStateOf(Game()) }
    var enableMoreOptions by savedStateHandle.saveable { mutableStateOf(false) }

    var listaGenres = listOf<String>()

    //Valor del dropdown
    var ddValue by savedStateHandle.saveable { mutableStateOf(0) }

    //MoreOptions
    var completed by savedStateHandle.saveable { mutableStateOf(false) }
    var planned by savedStateHandle.saveable { mutableStateOf(false) }
    var stars by savedStateHandle.saveable { mutableStateOf(0) }

    fun updateValues(gameID: String) {
        //Hago una query en busca de información sobre este juego
        viewModelScope.launch {
            FBCRUD.getGame(gameID).collect {
                gameData = it; listaGenres = gameData.genres.split(",")
            }
        }

        viewModelScope.launch {
            //Inicializo por primera vez mis variables completed y planned aquí
            FBCRUD.getUserGameList(FBAuth.UID.value)
                .collect { completed = it.any { it.id == gameID } }
        }

        viewModelScope.launch {
            FBCRUD.getUserGamePlanningList(FBAuth.UID.value)
                .collect { planned = it.any { it.id == gameID } }
        }


        viewModelScope.launch {
            //Si el juego está completado, esta query de abajo nunca dará null
            if (completed) {
                FBCRUD.getUserGameList(FBAuth.UID.value)
                    .collect { stars = it.find { it.id == gameID }?.userScore!!.toInt() }
            }
        }


    }

    fun statusMoreOptions(){
        enableMoreOptions = !enableMoreOptions
    }

    //COMPLETED
    fun addGameToUserList(score: Int = 0, comment: String = ""){
        FBCRUD.saveGameToUserList(gameData, score, comment)
    }

    fun removeGameFromUserList(gameID: String){
        FBCRUD.removeGameFromUserList(gameID)
    }
    //PLANNING
    fun addGameToUserPlanningList(score: Int = 0, comment: String = ""){
        FBCRUD.saveGameToUserPlanningList(gameData)
    }

    fun removeGameFromUserPlanningList(gameID: String){
        FBCRUD.removeGameFromUserPlanningList(gameID)
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

    fun updateStars(newValue: Int){
        stars = newValue
    }

}