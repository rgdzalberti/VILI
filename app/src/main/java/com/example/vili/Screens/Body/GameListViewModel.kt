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
class GameListViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var tabIndex by savedStateHandle.saveable { mutableStateOf(0) }


    init {

        if (CentralizedData.gameList.value.isEmpty()) {
            viewModelScope.launch {
                FBCRUD.getUserGameList()
                    .collect { CentralizedData.gameList.value = it }
            }
        }


    }

    fun reloadList(){
        viewModelScope.launch {
            FBCRUD.getUserGameList()
                .onCompletion { CentralizedData.tellGameListToReload(false) }
                .collect { CentralizedData.gameList.value = it }
        }

        viewModelScope.launch {
            FBCRUD.getUserGamePlanningList()
                .onCompletion { CentralizedData.tellGameListToReload(false) }
                .collect { CentralizedData.planningList.value = it }
        }

    }

    fun updateTabData(newValue: Int){
        tabIndex = newValue
    }

    fun sortList(newValue: Int){

        when(newValue){

            0 -> {
                if (tabIndex==0){CentralizedData.gameList.value = CentralizedData.gameList.value.sortedBy { it.name }}
                else if (tabIndex==1){CentralizedData.planningList.value = CentralizedData.planningList.value.sortedBy { it.name }}

            }

            1 -> {
                CentralizedData.gameList.value = CentralizedData.gameList.value.sortedByDescending { it.userScore }
            }

        }

    }

    //region NavigateHome
    var popBack by savedStateHandle.saveable { mutableStateOf(false) }
    fun popBack(){
        popBack = !popBack
    }
    //endregion


}
