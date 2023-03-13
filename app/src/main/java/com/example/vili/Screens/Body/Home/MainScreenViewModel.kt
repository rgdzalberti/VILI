package com.example.vili.Screens.Body.Home

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.vili.Common.Complex.BottomBarClass
import com.example.vili.Common.Complex.BottomBarClass.Companion.turnBottomBar
import com.example.vili.Model.Querys.FBAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import viliApp.FBCRUD
import viliApp.Game
import viliApp.GameBanner
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var gameList by savedStateHandle.saveable { mutableStateOf(listOf<Game>()) }

    //Lista de banners
    var bannerList by savedStateHandle.saveable { mutableStateOf(listOf<GameBanner>()) }
    var recommendedList by savedStateHandle.saveable { mutableStateOf(mutableListOf<Game>()) }

    //Side Menus
    var enableSearchMenu by savedStateHandle.saveable { mutableStateOf(false) }
    var enableSettingsMenu by savedStateHandle.saveable { mutableStateOf(false) }

    //Log Out?
    var logOutnPop by savedStateHandle.saveable { mutableStateOf(false) }

    //Search Textfield
    var searchText by savedStateHandle.saveable { mutableStateOf("") }

    init {

        //Si no existe UserDATA para este user lo creo
        viewModelScope.launch {
            FBCRUD.createUserData()
        }

        //Obtengo la lista de juegos para lanzar recomendacions en la pantalla principal
        viewModelScope.launch {
            FBCRUD.getGameList()
                .onCompletion { repeat (5) {recommendedList.add(gameList.random())} } // Aquí lanzo recomendaciones según la lista de games
                .collect { gameList = it as MutableList<Game> }
        }

        //Ahora inicializo la lista de banners
        viewModelScope.launch {
            FBCRUD.getGameBanners().collect { bannerList = it }
        }

    }

    //region Side Menus
    //2 funciones para controlar si se está mostrando los side menus
    fun switchSearch(){
        enableSearchMenu = !enableSearchMenu
        //Controlo cuando aparece/desaparece la barra de abajo
        turnBottomBar()
    }

    fun switchSettings(){
        enableSettingsMenu = !enableSettingsMenu
        //Controlo cuando aparece/desaparece la barra de abajo
        turnBottomBar()
    }


    fun searchBackPressed(){
        switchSearch()
    }

    fun settingsBackPressed(){
        switchSettings()
    }
    //endregion

    fun logOut(){
        Firebase.auth.signOut()
        BottomBarClass.turnBottomBar()
        logOutnPop = !logOutnPop
    }

    fun onSearchTextChange(newValue: String) {
        this.searchText = newValue
    }

    //region BugFix Spam Banner
    var clickable by savedStateHandle.saveable { mutableStateOf(true) }
    var clickableDelay by savedStateHandle.saveable { mutableStateOf(false) }
    fun updatePendingValues(){
        clickable = !clickable
        clickableDelay = !clickableDelay

        if (clickableDelay) {
            viewModelScope.launch {
                delay(500)
                clickable = true
                clickableDelay = false
            }
        }
    }
    //endregion


}