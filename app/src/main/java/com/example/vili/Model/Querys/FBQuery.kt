package viliApp

import android.util.Log
import androidx.compose.ui.graphics.Outline
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.reflect.GenericArrayType
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
                gameList.add(Game(it.id,it.getString("Nombre").toString(),it.getString("Imagen").toString(),it.getString("AVGDuracion").toString(),it.getString("Descripcion").toString(),it.getString("Developers").toString(),it.getString("Generos").toString(), it.getString("ReleaseDate").toString()))
            }

            emit(gameList)
        }

        //Get an specific game in the collection
        fun getGame(gameID: String): Flow<Game> = callbackFlow{

            val db = Firebase.firestore
            var gameReturn : Game = Game()

            db.collection("Games").document(gameID).get().addOnSuccessListener {
                gameReturn = Game(it.id,it.getString("Nombre").toString(),it.getString("Imagen").toString(),it.getString("AVGDuracion").toString(),it.getString("Descripcion").toString(),it.getString("Developers").toString(),it.getString("Generos").toString(), it.getString("ReleaseDate").toString())
                trySend(gameReturn)
            }
            awaitClose { channel.close() }

        }

        //Guardar entrada de juego en la lista del usuario pertinente
        fun saveGameToUserList(game: Game, score: Int = 0, comment:String = ""){

            val db = Firebase.firestore
            val data = UserGame("[$score]","[$comment]","[${game.id}]","[${game.name}]","[${game.imageURL}]","[${game.avgDuration}]","[${game.description}]","[${game.developers}]","[${game.genres}]","[${game.releaseDate}]")

            //Guardo la referencia al juego en el pertinente registro del usuario
            val userUID = FirebaseAuth.getInstance().currentUser?.uid
            val reference = db.collection("UserDATA").document(userUID.toString())

            if (game.id != "[]") {

                reference.get().addOnSuccessListener {
                    if (it.get("userGameList") != null) {
                        //Como el campo existe, actualizo los datos
                        reference.update("userGameList", FieldValue.arrayUnion(data))
                    } else {
                        //El campo no existe, así que lo creo y después añado los datos
                        val emptyArray = mutableListOf<UserGame>()
                        reference.set(hashMapOf("userGameList" to emptyArray))
                        reference.update("userGameList", FieldValue.arrayUnion(data))
                    }
                    CentralizedData.tellGameListToReload(true)
                }
            }


        }

        //Obtener lista de juegos de usuario
        fun getUserGameList(): Flow<List<UserGame>> = callbackFlow{

            val db = Firebase.firestore
            val userUID = FirebaseAuth.getInstance().currentUser?.uid
            val reference = db.collection("UserDATA").document(userUID.toString())

            val gameList = mutableListOf<UserGame>()

            reference.get()
                .addOnSuccessListener { document ->

                    if (document.get("userGameList") != null)
                    {
                        //Como el campo existe, saco los datos
                        val gameEntry = document.get("userGameList") as List<*>

                        gameEntry.forEach {

                            val text = it.toString()
                            var userComment = text.substringAfter("userComment=["); userComment = userComment.substringBefore("]")
                            var developers = text.substringAfter("developers=["); developers = developers.substringBefore("]")
                            var releaseDate = text.substringAfter("releaseDate=["); releaseDate = releaseDate.substringBefore("]")
                            var genres = text.substringAfter("genres=["); genres = genres.substringBefore("]")
                            var imageURL = text.substringAfter("imageURL=["); imageURL = imageURL.substringBefore("]")
                            var name = text.substringAfter("name=["); name = name.substringBefore("]")
                            var description = text.substringAfter("description=["); description = description.substringBefore("]")
                            var userScore = text.substringAfter("userScore=["); userScore = userScore.substringBefore("]")
                            var id = text.substringAfter("id=["); id = id.substringBefore("]")
                            var avgDuration = text.substringAfter("avgDuration=["); avgDuration = avgDuration.substringBefore("]")


                            gameList.add(UserGame(userScore,userComment,id,name,imageURL,avgDuration,description,developers,genres,releaseDate))
                        }
                    }
                    else{
                        //El campo no existe, así que lo creo para la proxima vez
                        val emptyArray = mutableListOf<UserGame>()
                        reference.set(hashMapOf("userGameList" to emptyArray))
                    }
                    trySend(gameList)

                }
            awaitClose { channel.close() }
        }

        fun removeGameFromUserList(gameID: String){
            val db = Firebase.firestore
            val userUID = FirebaseAuth.getInstance().currentUser?.uid
            val reference = db.collection("UserDATA").document(userUID.toString())


            reference.get()
                .addOnSuccessListener {
                    val oldList = it.get("userGameList") as List<*>
                    val newList = oldList.filter { !it.toString().contains(gameID) }


                    reference.set(hashMapOf("userGameList" to newList))
                    CentralizedData.tellGameListToReload(true)
                }

        }

        fun getGameBanners():Flow<List<GameBanner>> = callbackFlow{
            val db = Firebase.firestore
            val reference = db.collection("gameBanners")

            val gameBannerList = mutableListOf<GameBanner>()

            reference.get().await().forEach {
                gameBannerList.add(GameBanner(it.getString("name").toString(),it.getString("imageURL").toString(),it.getString("gameID").toString()))
            }

            trySend(gameBannerList)

            awaitClose { channel.close() }
        }


    }
}


