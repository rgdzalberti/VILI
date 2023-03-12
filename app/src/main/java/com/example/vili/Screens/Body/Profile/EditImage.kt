package com.example.vili.Screens.Body.Profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.R
import java.io.File
import java.io.FileOutputStream


@Composable
@Preview
fun PreviewEditImage(){
    EditImage(editingPFP = true,rememberNavController(),"")
}

@Composable
fun EditImage(editingPFP:Boolean,nav:NavController, imageURL: String,viewModel: EditImageViewModel = hiltViewModel()){


    EditImageBody(nav = nav, imageURL = imageURL, editingPFP = editingPFP)



}

@Composable
fun EditImageBody(editingPFP:Boolean,nav:NavController, imageURL: String, viewModel: EditImageViewModel = hiltViewModel()){

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Row(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.05f)
            .background(Color.Transparent)) {

        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(start = 5.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = {nav.popBackStack()}) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrowback),
                    "",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

        }

        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(start = 5.dp),
            contentAlignment = Alignment.CenterEnd
        ) {

            Row() {

            IconButton(onClick = { launcher.launch("image/*")}) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                    "",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(onClick = { bitmap.value?.let { viewModel.uploadImage(it,context,editingPFP); nav.popBackStack() } }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.done),
                    "",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
            }

        }



    }


    //region Imagen
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        when{
            imageUri == null -> {
                Image(
                    painter =rememberAsyncImagePainter(imageURL),
                    contentDescription = "User profile picture",
                    modifier = Modifier
                        .aspectRatio(1f))

            }
            else->{

                //La imagen se ha actualizado así que se mostrará la nueva
                //Además se lanza código para actualizar la PFP y subirlo a la BBDD


                imageUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value = MediaStore.Images
                            .Media.getBitmap(context.contentResolver, it)

                    } else {
                        val source = ImageDecoder
                            .createSource(context.contentResolver, it)
                        bitmap.value = ImageDecoder.decodeBitmap(source)
                    }

                    bitmap.value?.let { btm ->
                        Image(
                            bitmap = btm.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.aspectRatio(1f)
                        )
                    }
                }
            }
        }

    }
    //endregion



}



class CurrentImage(){
    companion object{
        var imageURL = mutableStateOf("")
        var editingPFP = mutableStateOf(false)
    }
}

fun bitmapToPNG(bitmap: Bitmap,context: Context):File?{

    try {
        val png = File(context.cacheDir, "filename.png")
        png.createNewFile()

        val outputStream = FileOutputStream(png)

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        outputStream.flush()
        outputStream.close()

        return png
    }catch (e:java.lang.Exception){
        Log.e("bitmapToPNG","El bitmap está vacío")
        return null
    }

}