package viliApp

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FBQuery{

    companion object{


        fun returnGameList(){
            val db = Firebase.firestore
            val gameList = mutableListOf<String>()

            val gameListDB = db.collection("Games").get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        Log.i("FirebaseREAD",document.data.toString())
                    }
                }
        }


    }


}