package viliApp

data class Game( //SI SE MODIFICA CAMBIAR FB getGamerUserUnion
    var id: String = "",
    var name: String = "",
    var imageURL: String = ""
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