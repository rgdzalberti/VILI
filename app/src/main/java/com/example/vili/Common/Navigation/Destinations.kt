package viliApp

sealed class Destinations (val ruta : String) {

    object Pantalla1: Destinations("Login")
    object MainScreen: Destinations("MainScreen")
    object ListScreen: Destinations("GameList")
    object Pantalla3: Destinations("GameDetails")
    object Profile: Destinations("Profile")

    object EditImage: Destinations("EditImage")

    object UserList:Destinations("UserList")


}