package viliApp

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*


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
                gameList.add(Game(it.id,it.getString("Nombre").toString(),it.getString("Imagen").toString(),it.getString("AVGDuracion").toString(),it.getString("Descripcion").toString(),it.getString("Developers").toString(),it.getString("Generos").toString(), it.getDate("ReleaseDate")!!))
            }

            emit(gameList)
        }

        //Get an specific game in the collection
        fun getGame(gameID: String): Flow<Game> = flow{

            val db = Firebase.firestore
            var gameReturn : Game = Game()

            db.collection("Games").document(gameID).get().addOnSuccessListener {
                gameReturn = Game(it.id,it.getString("Nombre").toString(),it.getString("Imagen").toString(),it.getString("AVGDuracion").toString(),it.getString("Descripcion").toString(),it.getString("Developers").toString(),it.getString("Generos").toString(), it.getDate("ReleaseDate")!!)
            }.await()

            emit(gameReturn)
        }

        //Guardar entrada de juego en la lista del usuario pertinente
        fun saveGameToUserList(gameID: String, score: Int = 0, comment:String = ""){

            val db = Firebase.firestore
            val data = UserGameEntry(gameID,score,comment)

            //Guardo la referencia al juego en el pertinente registro del usuario
            val userUID = FirebaseAuth.getInstance().currentUser?.uid
            val reference = db.collection("UserDATA").document(userUID.toString())

            reference.get().addOnSuccessListener {
                if (it.get("userGameList") != null)
                {
                    //Como el campo existe, actualizo los datos
                    reference.update("userGameList", FieldValue.arrayUnion(data))
                }
                else{
                    //El campo no existe, así que lo creo y después añado los datos
                    val emptyArray = mutableListOf<UserGameEntry>()
                    reference.set(hashMapOf("userGameList" to emptyArray))
                }
            }

        }

        //Obtener lista de juegos de usuario
        fun getUserGameList(): Flow<List<UserGameEntry>> = flow{

            val db = Firebase.firestore
            val userUID = FirebaseAuth.getInstance().currentUser?.uid
            val reference = db.collection("UserDATA").document(userUID.toString())

            val gameList = mutableListOf<UserGameEntry>()
            val gameUserUnionList = mutableListOf<GameUserUnion>()

            reference.get()
                .addOnSuccessListener { document ->

                    //Primero obtengo los datos del usuario sobre cada juego, es decir, comentarios y score
                    val gameEntry = document.get("userGameList") as List<*>

                    gameEntry.forEach {
                        val text = it.toString()
                        var gameID = text.substringAfter("gameID="); gameID = gameID.substringBefore(",")
                        var score = text.substringAfter("score="); score = score.substringBefore(",")
                        var comment = text.substringAfter("comment="); comment = comment.substringBefore("}")

                        gameList.add(UserGameEntry(gameID,score.toInt(),comment))
                    }
                }.await()
            emit(gameList)

        }


        fun removeGameFromUserList(gameID: String){
            val db = Firebase.firestore
            val userUID = FirebaseAuth.getInstance().currentUser?.uid
            val reference = db.collection("UserDATA").document(userUID.toString())


            reference.get()
                .addOnSuccessListener {
                    val oldList = it.get("userGameList") as List<*>
                    val newList = oldList.filter { !it.toString().contains(gameID) }

                    CentralizedData.tellGameListToReload()

                    reference.set(hashMapOf("userGameList" to newList))

                }
        }


    }
}


