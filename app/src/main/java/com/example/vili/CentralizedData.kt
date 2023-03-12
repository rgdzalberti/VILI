package viliApp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class CentralizedData {

    companion object{

        var gameList = mutableStateOf(listOf<UserGame>())
        var planningList = mutableStateOf(listOf<Game>())

        var profileID = mutableStateOf("")
        var userProfileList = mutableStateOf(listOf<UserProfile>())

        //Mas detalles
        var gameID:String = ""

        fun updateGameID(newGameID:String){
            gameID = newGameID
        }

        @JvmName("getGameID1")
        fun getGameID():String{
            return gameID
        }


        var recomposeUI =  mutableStateOf(false)
        fun tellGameListToReload(newValue : Boolean){
            recomposeUI.value = newValue

        }


    }

}