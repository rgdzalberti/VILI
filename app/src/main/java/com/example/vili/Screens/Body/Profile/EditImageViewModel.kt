package com.example.vili.Screens.Body.Profile

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditImageViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var bitmap by savedStateHandle.saveable { mutableStateOf<Bitmap>(Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)) }

    fun uploadImage(bitmap: Bitmap, context: Context, editingPFP: Boolean) {
        val pngFile = bitmapToPNG(bitmap, context)
        when {
            editingPFP -> {if (pngFile != null) FBStorage.savePFP(pngFile)}
            else -> {
                if (pngFile != null) FBStorage.saveBanner(pngFile)
            }
        }

    }

}