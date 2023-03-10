package viliApp

sealed class Destinations (val ruta : String) {

    object Pantalla1: Destinations("Login")
    object Pantalla2: Destinations("MainScreen")
    object Pantalla3: Destinations("GameDetails")


}