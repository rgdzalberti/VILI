package viliApp

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
class MainScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var selectedIndex by savedStateHandle.saveable { mutableStateOf(1) }
    var gameList by savedStateHandle.saveable { mutableStateOf(listOf<Game>()) }


    init {
        viewModelScope.launch {
            FBQuery.getGameList()
                .collect { gameList = it as MutableList<Game> }
        }

        //LISTA
        if (CentralizedData.gameList.value.isEmpty()) {
            viewModelScope.launch {
                FBQuery.getUserGameList()
                    .collect { CentralizedData.gameList.value = it }
            }
        }
    }

    fun updateIndex(newIndex : Int){
        selectedIndex = newIndex
    }



}