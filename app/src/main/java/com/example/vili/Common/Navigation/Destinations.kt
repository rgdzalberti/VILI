package viliApp

sealed class Destinations (val ruta : String) {

    object Pantalla1: Destinations("Login")
    object MainScreen: Destinations("MainScreen")
    object ListScreen: Destinations("GameList")
    object Pantalla3: Destinations("GameDetails")


}