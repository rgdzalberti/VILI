
package com.example.vili.Screens.Body.Profile

import FBStorage
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.vili.Model.Querys.FBAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlinx.coroutines.async
import viliApp.CentralizedData
import viliApp.FBCRUD

/*
@HiltViewModel
class ProfileViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var pfpURL by savedStateHandle.saveable { mutableStateOf("") }
    var bannerURL by savedStateHandle.saveable { mutableStateOf("") }

    var refreshing by savedStateHandle.saveable { mutableStateOf(false) }

    init {
        viewModelScope.launch {
            updateImages().collect()
        }
    }

    fun refresh(){

        refreshing = true

        pfpURL = ""
        bannerURL = ""

        viewModelScope.launch {
            updateImages().collect {
                if (it) refreshing = false; Log.i("wawa","aaaa")
            }
        }

    }

    fun updateImages(): Flow<Boolean> = flow{

        var loaded = 0

        FBAuth.UID?.let {
            FBStorage.getPFPURL(it) { url ->
                if (url.isBlank()) { pfpURL = "https://pbs.twimg.com/media/FprEeyJXwAI-hre.jpg"} else pfpURL = url
                loaded++
                if (loaded==2) {emit(true)}
                ; Log.i("wawa","1111")
            }
        }

        FBAuth.UID?.let {
            FBStorage.getBannerURL(it) { url ->
                if (url.isBlank()) { bannerURL = "https://wallpaperaccess.com/full/2635957.jpg"} else bannerURL = url
                loaded++
                ; Log.i("wawa","2222")
            }
        }


    }



}
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var profileID by savedStateHandle.saveable { mutableStateOf("") }

    var pfpURL by savedStateHandle.saveable { mutableStateOf("") }
    var bannerURL by savedStateHandle.saveable { mutableStateOf("") }

    var refreshing by savedStateHandle.saveable { mutableStateOf(false) }

    init {
        viewModelScope.launch {
            updateImages()
        }
    }
    //region Funciones para el Swipe Refresh

    fun refresh(){

        refreshing = true

        pfpURL = ""
        bannerURL = ""


        viewModelScope.launch {
            if (updateImages()) {refreshing=false; Log.i("wawa","aaaa")}
        }

    }
    suspend fun updateImages():Boolean = coroutineScope{

        var loaded = 0

        val pfp = async {
            FBAuth.UID?.let {
                FBStorage.getPFPURL(it) { url ->
                    if (url.isBlank()) {
                        pfpURL = "https://pbs.twimg.com/media/FprEeyJXwAI-hre.jpg"
                    } else pfpURL = url
                    loaded++
                    ; Log.i("wawa", "1111")
                }
            }
        }

        val banner = async {
            FBAuth.UID?.let {
                FBStorage.getBannerURL(it) { url ->
                    if (url.isBlank()) {
                        bannerURL = "https://wallpaperaccess.com/full/2635957.jpg"
                    } else bannerURL = url
                    loaded++
                    ; Log.i("wawa", "2222")
                }
            }
        }

        pfp.await()
        banner.await()
        true
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
        CentralizedData.gameList.value = emptyList()
        CentralizedData.planningList.value = emptyList()

        //Ahora las relleno
        viewModelScope.launch {
            FBCRUD.getUserGameList()
                .collect { CentralizedData.gameList.value = it.sortedByDescending { it.userScore } }

            viewModelScope.launch {
                FBCRUD.getUserGamePlanningList()
                    .collect { CentralizedData.planningList.value = it.sortedBy { it.name }}
            }
        }


    }
    //endregion

}