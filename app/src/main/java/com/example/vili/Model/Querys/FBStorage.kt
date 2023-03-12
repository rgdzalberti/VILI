import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

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
        fun getPFPURL(uid:String, callback: (String) -> Unit){
            val ref = storage.reference.child("users/${Firebase.auth.uid}/pfp.png")

            //Compruebo que hay una sesión iniciada
            if (Firebase.auth.uid != null) {

                ref.downloadUrl
                    .addOnSuccessListener {url->
                        callback(url.toString())
                    }
                    .addOnFailureListener{exception->
                        callback("")
                        Log.e("FBStorage",exception.toString() + " b")
                    }

            } else Log.e("FBStorage", "No hay sesión iniciada. Abortando get de URL.")
        }
        //endregion

        //region getBanner URL
        fun getBannerURL(uid:String = "${Firebase.auth.uid}", callback: (String) -> Unit){
            val ref = storage.reference.child("users/${Firebase.auth.uid}/banner.png")

            //Compruebo que hay una sesión iniciada
            if (Firebase.auth.uid != null) {

                ref.downloadUrl
                    .addOnSuccessListener {url->
                        callback(url.toString())
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