package viliApp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var selectedIndex by savedStateHandle.saveable { mutableStateOf(1) }

    fun updateIndex(newIndex : Int){
        selectedIndex = newIndex
    }



}