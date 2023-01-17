package viliApp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import viliApp.CentralizedData.Companion.getGameID
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var gameID by savedStateHandle.saveable { mutableStateOf("") }

    init {
        //Obtengo la referencia del juego primero en mi clase centralizada

        gameID = getGameID()

        //Hago una query en busca de información sobre este juego
    }

}