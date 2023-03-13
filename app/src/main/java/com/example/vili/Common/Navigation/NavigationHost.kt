package viliApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vili.Screens.Body.GameDetails.GameDetails
import com.example.vili.Screens.Body.Home.HomeScreen
import com.example.vili.Screens.Body.MyList.GameList
import com.example.vili.Screens.Body.MyList.GameListBody
import com.example.vili.Screens.Body.Profile.CurrentImage
import com.example.vili.Screens.Body.Profile.Profile
import com.example.vili.Screens.Body.Profile.EditImage

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
        composable("${Destinations.ListScreen.ruta}/{uid}", arguments = listOf(navArgument("uid"){type = NavType.StringType})){ backStackEntry ->
            backStackEntry.arguments?.getString("uid")
                ?.let { GameList(navController=navController, uid = it) }
        }
        composable(route = "${Destinations.Pantalla3.ruta}/{gameID}", arguments = listOf(navArgument("gameID"){type = NavType.StringType})){ backStackEntry ->
            backStackEntry.arguments?.getString("gameID")
                ?.let { GameDetails(navController = navController, gameID = it) }
        }
        composable(route = "${Destinations.Profile.ruta}/{uid}", arguments = listOf(navArgument("uid"){type = NavType.StringType})){ backStackEntry ->
            backStackEntry.arguments?.getString("uid")
                ?.let { Profile(nav=navController, profileID = it) }
        }
        composable(route = Destinations.EditImage.ruta){
            EditImage(nav=navController, imageURL = CurrentImage.imageURL.value, editingPFP = CurrentImage.editingPFP.value)
        }
        composable("${Destinations.UserList.ruta}/{uid}", arguments = listOf(navArgument("uid"){type = NavType.StringType})){ backStackEntry ->
            backStackEntry.arguments?.getString("uid")
                ?.let { GameListBody(navController=navController, uid = it) }
        }


    }
}

class NavigationFunctions(){
    companion object{
        //region BottomBar Status
        var changeScreen = mutableStateOf(false)
        var screenID = mutableStateOf(0)

        fun changeScreen(screenID: Int){
            if (screenID != -1){this.screenID.value = screenID}
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








