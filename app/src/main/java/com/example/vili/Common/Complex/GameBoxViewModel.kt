package com.example.vili.Common.Complex

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import viliApp.CentralizedData
import javax.inject.Inject



@HiltViewModel
class GameBoxViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    //Este viewmodel existe para eliminar un bug en el cual puedes spammear clicks a una imagen clickable y se stackean pantallas

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


}