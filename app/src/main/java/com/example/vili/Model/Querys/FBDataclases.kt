package viliApp

import java.util.Date

data class Game( //SI SE MODIFICA CAMBIAR FB getGamerUserUnion
    var id: String = "",
    var name: String = "",
    var imageURL: String = "",
    var avgDuration: String = "",
    var description: String = "",
    var developers : String = "",
    var genres: String = "",
    var releaseDate: String = ""
) : java.io.Serializable

data class UserGame(
    var userScore: String = "",
    var userComment: String = "",
    var id: String = "",
    var name: String = "",
    var imageURL: String = "",
    var avgDuration: String = "",
    var description: String = "",
    var developers : String = "",
    var genres: String = "",
    var releaseDate: String = ""
): java.io.Serializable

data class GameBanner(
    val name: String = "",
    val imageURL : String = "",
    val gameID : String = "",
): java.io.Serializable