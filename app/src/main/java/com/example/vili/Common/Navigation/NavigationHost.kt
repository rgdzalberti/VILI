package viliApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vili.Screens.Body.HomeScreen

@Composable
fun NavigationHost (navController: NavHostController,startDestination: String){
    NavHost(
        navController = navController,
        startDestination = startDestination )
    {
        composable(Destinations.Pantalla1.ruta){
           LoginScreen(navController = navController)
        }
        composable(Destinations.MainScreen.ruta){
            HomeScreen(nav = navController)
        }
        composable(Destinations.ListScreen.ruta){
            GameList(navController = navController)
        }        
        composable(route = Destinations.Pantalla3.ruta){
            GameDetails(navController)
        }


    }
}

class NavigationFunctions(){
    companion object{
        //region BottomBar Status
        var changeScreen = mutableStateOf(false)
        var screenID = mutableStateOf(0)

        fun changeScreen(screenID: Int){
            if (screenID != 0){this.screenID.value = screenID}
            changeScreen.value = !changeScreen.value
        }
        //endregion

        //region NavigationPOP
        //Uso esta función para eliminar la pantalla de registro de la pila. Una vez inicias sesion o te registras no puedes volver a esta
        //pantalla sin deslogearte antes
        @Composable
        fun NavigatePop(navController: NavController, destination: String) {
            navController.navigate(destination) {
                popUpTo(Destinations.Pantalla1.ruta) {
                    inclusive = true
                }
            }
        }

        @Composable
        fun NavigatePopLogOut(navController: NavController, destination: String) {
            LaunchedEffect(Unit) {
                navController.navigate(destination) {
                    popUpTo(Destinations.MainScreen.ruta) {
                        inclusive = true
                    }
                }
            }
        }
        //endregion
    }
}








