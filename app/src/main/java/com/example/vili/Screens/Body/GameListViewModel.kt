package com.example.vili.Screens.Body

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.vili.Model.Querys.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import viliApp.FBQuery
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    //Si la lista esta vacía realiza una query que inicialmente está sorted por nombre
    var gameList by savedStateHandle.saveable { mutableStateOf(mutableListOf<Game>()) }

    init {
        //Si esta vacía la lista (no está guardada en cache) se rescatan los datos de la red
        if (gameList.isEmpty()) {

            viewModelScope.launch {
                FBQuery.getGameList().collect {gameList = it.sortedBy { it.name } as MutableList<Game>}
            }
        }

    }

    //TODO SORT GAMELIST DIFFERENT NO SE Q
    fun sortListByScore(){
        gameList = gameList.sortedBy { it.id } as MutableList<Game>
    }




}
/*
    var gameList by savedStateHandle.saveable { mutableStateOf(mutableListOf<Game>()) }

    init {
        viewModelScope.launch{
            FBQuery.getGameList().collect{
                gameList = it.sortedBy { it.name } as MutableList<Game>
            }
        }
    }

    //TODO SORT GAMELIST DIFFERENT NO SE Q
    fun sortListByScore(){
        gameList = gameList.sortedBy { it.id } as MutableList<Game>
    }
 */