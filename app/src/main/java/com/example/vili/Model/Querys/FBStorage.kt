import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.tasks.await
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FBStorage{
    companion object{

        val storage = Firebase.storage

        //region savePFP
        fun savePFP(png: File){
            val ref = storage.reference.child("users")
            val fileBytes = png.readBytes()

            //Compruebo que hay una sesión iniciada
            if (Firebase.auth.uid != null) {

                ref.child("${Firebase.auth.uid}/pfp.png").putBytes(fileBytes)
                    .addOnSuccessListener {
                        //Sin problemas
                    }
                    .addOnFailureListener{exception->
                        //ERROR
                        Log.e("FBStorage",exception.stackTraceToString())
                    }

            } else Log.e("FBStorage", "No hay sesión iniciada. Abortando subida de PNG.")

        }
        //endregion

        //region saveBanner
        fun saveBanner(png: File){
            val ref = storage.reference.child("users")
            val fileBytes = png.readBytes()

            //Compruebo que hay una sesión iniciada
            if (Firebase.auth.uid != null) {

                ref.child("${Firebase.auth.uid}/banner.png").putBytes(fileBytes)
                    .addOnSuccessListener {
                        //Sin problemas
                    }
                    .addOnFailureListener{exception->
                        //ERROR
                        Log.e("FBStorage",exception.toString() + " c")
                    }

            } else Log.e("FBStorage", "No hay sesión iniciada. Abortando subida de PNG.")

        }
        //endregion


        //region getPFP URL
        suspend fun getPFPURL(uid:String):String = suspendCoroutine{ continuation->
            val ref = storage.reference.child("users/${uid}/pfp.png")

            //Compruebo que hay una sesión iniciada
            if (Firebase.auth.uid != null) {

                ref.downloadUrl
                    .addOnSuccessListener {url->
                        if (url.toString().isBlank()) continuation.resume("https://pbs.twimg.com/media/FprEeyJXwAI-hre.jpg") else continuation.resume(url.toString())
                    }
                    .addOnFailureListener{exception->
                        continuation.resume("")
                        Log.e("FBStorage",exception.toString() + " b")
                    }

            } else Log.e("FBStorage", "No hay sesión iniciada. Abortando get de URL.")
        }
        //endregion




        //region getBanner URL
        fun getBannerURL(uid:String, callback: (String) -> Unit){
            val ref = storage.reference.child("users/${uid}/banner.png")

            //Compruebo que hay una sesión iniciada
            if (Firebase.auth.uid != null) {

                ref.downloadUrl
                    .addOnSuccessListener {url->
                        if (url.toString().isBlank()) callback("https://wallpaperaccess.com/full/2635957.jpg") else callback(url.toString())
                    }
                    .addOnFailureListener{exception->
                        callback("")
                        Log.e("FBStorage",exception.toString() + " a")
                    }

            } else Log.e("FBStorage", "No hay sesión iniciada. Abortando get de URL.")
        }
        //endregion

    }
}