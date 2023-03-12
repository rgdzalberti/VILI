package com.example.vili.Screens.Body.Profile

import FBStorage
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.vili.Model.Querys.FBAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var pfpURL by savedStateHandle.saveable { mutableStateOf("") }
    var bannerURL by savedStateHandle.saveable { mutableStateOf("") }

    init {
        refresh()
    }

    fun refresh(){
        FBAuth.UID?.let {
            FBStorage.getPFPURL(it) { url ->
                if (url.isBlank()) { pfpURL = "https://pbs.twimg.com/media/FprEeyJXwAI-hre.jpg"} else pfpURL = url
            }
        }

        FBAuth.UID?.let {
            FBStorage.getBannerURL(it) { url ->
                if (url.isBlank()) { bannerURL = "https://wallpaperaccess.com/full/2635957.jpg"} else bannerURL = url
            }
        }
    }



}