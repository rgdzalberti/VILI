
package com.example.vili.Screens.Body.Profile

import FBStorage
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import viliApp.FBCRUD

@HiltViewModel
class ProfileViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var pfpURL by savedStateHandle.saveable { mutableStateOf("") }
    var bannerURL by savedStateHandle.saveable { mutableStateOf("") }

    var refreshing by savedStateHandle.saveable { mutableStateOf(false) }
    var updatingImages by savedStateHandle.saveable { mutableStateOf(0) }

    //region Stats Variables
    var playedGames by savedStateHandle.saveable { mutableStateOf(0) }
    var planningGames by savedStateHandle.saveable { mutableStateOf(0) }
    //endregion

    fun updateData(uid:String) {

        viewModelScope.launch {
            updateImages(uid)
        }

        //region Stats
        viewModelScope.launch {
            FBCRUD.getUserGameList(uid)
                .collect { playedGames = it.size }
        }

        viewModelScope.launch {
            FBCRUD.getUserGamePlanningList(uid)
                .collect { planningGames = it.size }
        }
    }
        //endregion


    //region Funciones para el Swipe Refresh


    fun refresh(uid:String){
        pfpURL = ""
        bannerURL = ""
        refreshing = true

        viewModelScope.launch {
            updateImages(uid)
        }

    }

    suspend fun updateImages(uid: String) {

        val pfp = FBStorage.getPFPURL(uid)
        if (!pfp.isBlank()) pfpURL = pfp; updatingImages++

        val banner = FBStorage.getBannerURL(uid) { url ->
            if (!url.isBlank()) bannerURL = url; updatingImages++
        }
    }
    //endregion
}