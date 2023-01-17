package viliApp

import android.util.Log
import com.example.vili.Model.Querys.Game
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class FBQuery {

    companion object {

        //Como hacer una query no es instantanea necesitamos hacer uso de coroutines
        //Para ello utilizaremos Flow, un colector de datos asincrono
        //Esto significa que no utiliza la CPU principal para conseguir los datos
        //Y por tanto que no se congela la aplicación hasta que carguen los datos

        //Get all games in the collection
        fun getGameList(): Flow<List<Game>> = flow {

            val db = Firebase.firestore
            val gameList = mutableListOf<Game>()

            db.collection("Games").get().await().forEach{
                gameList.add(Game(it.id,it.getString("Nombre").toString(),it.getString("Imagen").toString()))
            }

            emit(gameList)
        }

        //Get an specific game in the collection
        fun getGame(gameID:String): Flow<Game> = flow{

            val db = Firebase.firestore
            var gameReturn : Game = Game()

            db.collection("Games").document(gameID).get().addOnSuccessListener {
                gameReturn = Game(gameID,it.getString("Nombre").toString(),it.getString("Imagen").toString())
            }.await()



            emit(gameReturn)
        }


    }
}


