package viliApp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.vili.Model.Querys.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import viliApp.CentralizedData.Companion.getGameID
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var gameID by savedStateHandle.saveable { mutableStateOf("") }
    var gameData by savedStateHandle.saveable { mutableStateOf(Game()) }

    init {
        //Obtengo la referencia del juego primero en mi clase centralizada

        gameID = getGameID()

        //Hago una query en busca de información sobre este juego
        viewModelScope.launch{
            FBQuery.getGame(gameID).collect {gameData = it;Log.i("wawa",it.name + " aaa")}
        }

    }

}