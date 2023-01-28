package viliApp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var tabIndex by savedStateHandle.saveable { mutableStateOf(0) }


    init {

        if (CentralizedData.gameList.value.isEmpty()) {
            viewModelScope.launch {
                FBQuery.getUserGameList()
                    .collect { CentralizedData.gameList.value = it }
            }
        }


    }

    //TODO SORT GAMELIST DIFFERENT NO SE Q
    fun sortListByScore() {
        CentralizedData.gameList.value = CentralizedData.gameList.value.sortedBy { it.userScore }
    }

    fun reloadList(){
        viewModelScope.launch {
            FBQuery.getUserGameList()
                .onCompletion { CentralizedData.tellGameListToReload(false) }
                .collect { CentralizedData.gameList.value = it }
        }

        viewModelScope.launch {
            FBQuery.getUserGamePlanningList()
                .onCompletion { CentralizedData.tellGameListToReload(false) }
                .collect { CentralizedData.planningList.value = it }
        }

    }

    fun updateTabData(newValue: Int){
        tabIndex = newValue
    }

}
