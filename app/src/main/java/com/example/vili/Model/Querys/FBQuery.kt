package viliApp

import android.util.Log
import com.example.vili.Model.Querys.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait


class FBQuery {

    companion object {

        //Como hacer una query a Firebase no es instantanea es necesario el uso de coroutines
        //que son funciones que pueden esperar para recuperar los datos.

        //No se puede lanzar directamente una suspend function así que abrimos una CoroutineScope
        fun launchReturnGameList(): List<Game> = runBlocking{
                returnGameList()
        }

        //Devolver una lista de todos los juegos
        suspend fun returnGameList(): List<Game> {

            val db = Firebase.firestore
            val gameList = mutableListOf<Game>()

            gameList.clear()

            FirebaseFirestore.getInstance().collection("Games").get().await().forEach{
                gameList.add(Game(it.id,it.getString("Nombre").toString(),it.getString("Imagen").toString()))
            }

            return gameList
        }

    }
}