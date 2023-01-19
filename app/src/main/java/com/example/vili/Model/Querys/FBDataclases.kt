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
    var releaseDate: Date = Date()
) : java.io.Serializable

data class UserGameEntry(
    var gameID: String,
    var score: Int = 0,
    var comment: String = ""
): java.io.Serializable

data class GameUserUnion( //SI SE MODIFICA CAMBIAR FB getGamerUserUnion
    var gameID: String,
    var score: Int = 0,
    var comment: String = "",
    var name: String = "",
    var imageURL: String = ""
): java.io.Serializable