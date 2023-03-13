package com.example.vili.Screens.Body.MyList

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.vili.Model.Querys.FBAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import viliApp.FBCRUD
import viliApp.Game
import viliApp.UserGame
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var tabIndex by savedStateHandle.saveable { mutableStateOf(0) }
    var userGameList by savedStateHandle.saveable { mutableStateOf(listOf<UserGame>()) }
    var userPlanningList by savedStateHandle.saveable { mutableStateOf(listOf<Game>()) }

    var reorderList by savedStateHandle.saveable { mutableStateOf(false) }

    fun obtainLists(uid:String){
        viewModelScope.launch {
            FBCRUD.getUserGameList(uid)
                .collect { userGameList = it }}

        viewModelScope.launch {
            FBCRUD.getUserGamePlanningList(uid)
                .collect { userPlanningList = it }
        }
    }

    fun updateTabData(newValue: Int){
        tabIndex = newValue
    }

    fun sortList(newValue: Int) {

        var oldGameList = listOf<UserGame>()
        var oldPlanningList = listOf<Game>()

        //Triggeo la recomposición de la lista vaciando y actualizando la lista
        //Si simplemente sortease no funcionaría ya que los contenidos siguen siendo los mismos

        when (newValue) {

            0 -> {
                if (tabIndex == 0) {
                    oldGameList = userGameList.sortedBy { it.name }
                    userGameList = emptyList()
                    userGameList = oldGameList
                }
                else if (tabIndex == 1) {
                    oldPlanningList = userPlanningList.sortedBy { it.name }
                    userPlanningList = emptyList()
                    userPlanningList = oldPlanningList
                }

            }

            1 -> {
                oldGameList = userGameList.sortedByDescending { it.userScore }
                userGameList = emptyList()
                userGameList = oldGameList
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
