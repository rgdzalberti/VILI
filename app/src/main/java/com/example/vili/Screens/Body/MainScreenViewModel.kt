package viliApp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var selectedIndex by savedStateHandle.saveable { mutableStateOf(1) }
    var gameList by savedStateHandle.saveable { mutableStateOf(listOf<Game>()) }

    //Lista de banners
    var bannerList by savedStateHandle.saveable { mutableStateOf(listOf<GameBanner>()) }
    var recommendedList by savedStateHandle.saveable { mutableStateOf(mutableListOf<Game>()) }

    //Side Menus
    var enableSearchMenu by savedStateHandle.saveable { mutableStateOf(false) }
    var enableSettingsMenu by savedStateHandle.saveable { mutableStateOf(false) }

    //Bottom bar
    var bottomBar by savedStateHandle.saveable { mutableStateOf(true) }

    init {
        //Obtengo la lista de juegos para lanzar recomendacions en la pantalla principal
        viewModelScope.launch {
            FBQuery.getGameList()
                .onCompletion { repeat (5) {recommendedList.add(gameList.random())} } // Aquí lanzo recomendaciones según la lista de games
                .collect { gameList = it as MutableList<Game> }
        }

        //Además inicializo la lista del jugador por si quiere ver sus estadisticas en el perfil
        if (CentralizedData.gameList.value.isEmpty()) {
            viewModelScope.launch {
                FBQuery.getUserGameList()
                    .collect { CentralizedData.gameList.value = it }
            }
        }

        //Ahora inicializo la lista de banners
        viewModelScope.launch {
            FBQuery.getGameBanners().collect { bannerList = it }
        }

    }

    fun updateIndex(newIndex : Int){
        selectedIndex = newIndex
    }

    //2 funciones para controlar si se está mostrando los side menus
    fun switchSearch(){
        enableSearchMenu = !enableSearchMenu
        //Controlo cuando aparece/desaparece la barra de abajo
        enableBottomBar()
    }

    fun switchSettings(){
        enableSettingsMenu = !enableSettingsMenu
        //Controlo cuando aparece/desaparece la barra de abajo
        enableBottomBar()
    }

    fun enableBottomBar(){
        bottomBar = !bottomBar
    }

    //2 funciones para controlar cuando le das a back en los side menus
    fun searchBackPressed(){
        switchSearch()
    }

    fun settingsBackPressed(){
        switchSettings()
    }

}