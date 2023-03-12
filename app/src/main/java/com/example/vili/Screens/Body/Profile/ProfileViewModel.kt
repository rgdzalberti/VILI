
package com.example.vili.Screens.Body.Profile

import FBStorage
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
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import viliApp.CentralizedData
import viliApp.FBCRUD
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class ProfileViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var profileID by savedStateHandle.saveable { mutableStateOf("") }

    var pfpURL by savedStateHandle.saveable { mutableStateOf("") }
    var bannerURL by savedStateHandle.saveable { mutableStateOf("") }

    var refreshing by savedStateHandle.saveable { mutableStateOf(false) }
    var updatingImages by savedStateHandle.saveable { mutableStateOf(0) }

    //region Stats Variables
    var playedGames by savedStateHandle.saveable { mutableStateOf(0) }
    var planningGames by savedStateHandle.saveable { mutableStateOf(0) }
    //endregion

    init {
        profileID = Firebase.auth.uid.toString()

        viewModelScope.launch {
            updateImages()
        }

        //region Stats
        viewModelScope.launch {
            FBCRUD.getUserGameList()
                .collect { playedGames = it.size }
        }

        viewModelScope.launch {
            FBCRUD.getUserGamePlanningList()
                .collect { planningGames = it.size }
        }


    }

    fun init() {


        viewModelScope.launch {
            updateImages()
        }

        //region Stats
        viewModelScope.launch {
            FBCRUD.getUserGameList()
                .collect { playedGames = it.size }
        }

        viewModelScope.launch {
            FBCRUD.getUserGamePlanningList()
                .collect { planningGames = it.size }
        }
    }
        //endregion


    //region Funciones para el Swipe Refresh


    fun refresh(){
        pfpURL = ""
        bannerURL = ""
        refreshing = true

        viewModelScope.launch {
            updateImages()
        }

    }

    suspend fun updateImages(){


        profileID.let {
            val pfp = FBStorage.getPFPURL(it)
            if (!pfp.isBlank()) pfpURL = pfp; updatingImages++
        }

        profileID.let {
            val banner = FBStorage.getBannerURL(it){ url->
                if (!url.isBlank()) bannerURL = url ; updatingImages++
            }
        }

    }
    //endregion

    //region Se guarda el UID para identificar a que lista viajar
    fun setProfileUID(UID:String){
        this.profileID = UID
    }
    //endregion

    //region update Game List (Por si viaja a la lista del usuario)
    fun updateGameListValues(){


        //Primero las vacío para que no muestre los juegos anteriores por un momento si la conexión es lenta
        /*
        CentralizedData.gameList.value = emptyList()
        CentralizedData.planningList.value = emptyList()

         */

        /*

        //Ahora las relleno
        viewModelScope.launch {
            CentralizedData.gameList.value = FBCRUD.getUserGameList(profileID).sortedByDescending { it.userScore }
        }

            viewModelScope.launch {

                CentralizedData.planningList.value = FBCRUD.getUserGamePlanningList(profileID).sortedBy { it.name }
            }

         */


    }
    //endregion


}