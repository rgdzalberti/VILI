package viliApp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import viliApp.CentralizedData.Companion.updateGameID


@Preview
@Composable
fun previewGameList() {
    gameList(rememberNavController())
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun gameList(navController: NavController,viewModel: GameListViewModel = hiltViewModel()) {

    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

    //No se puede poner en el viewmodel. Utilizo esto para controlar la pestaña y su animación
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    //Se hace la comprobación de si se ha hecho algún delete desde la última vez que estuvo en esta pantalla
    //Si la respuesta es sí, esto será true y se actualizará la lista localmente también.
    if (CentralizedData.recomposeUI.value == true) {
        reloadGameList(viewModel::reloadList)
    }

    //TODO hacer un topbar en condiciones
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(DeviceConfig.heightPercentage(10))
        .background(Color.Black)) {
        /*
        Button(onClick = { viewModel.sortListByScore()}) {

        }
        Button(onClick = { viewModel.reloadList()}) {

        }

         */
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(Color.Red)) {
            Text(text = "aa")
        }

        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            tabData(viewModel.tabIndex,viewModel::updateTabData,pagerState,scope)
        }

    }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFF161616)),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        if (CentralizedData.gameList.value.size != 0) {

            //WELTA

            HorizontalPager(count = 2,state = pagerState) { page ->

                when(page){
                    0 -> {viewModel.updateTabData(0);CalculateGamesContent(gameList = CentralizedData.gameList.value,navController)}
                    1 -> {viewModel.updateTabData(1);CalculateGamesContentPlanning(gameList = CentralizedData.planningList.value,navController); }
                }

            }




        }
    }

}

@Composable
fun gameBox(
    nav: NavController,
    key: String = "",
    titulo: String = "",
    imageURL: String = "",
    Rating: Int = 0,
    invisible: Boolean = false
) {

    var rating = ""
    repeat(Rating) { rating += "★" }

        Box(
            modifier = Modifier
                .background(if (invisible) Color.Transparent else Color.Black)
                .height(DeviceConfig.heightPercentage(30))
                .width(DeviceConfig.widthPercentage(40))
                .alpha(if (invisible) 0f else 1f)
            ,
            contentAlignment = Alignment.BottomCenter
        ) {

            //Animación de carga mientras no hay imagenes
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier, Color.Red)
            }

            //Imagen - Si esta invisible no se muestra para evitar el click null
            if (!invisible) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            updateGameID(key)
                            nav.navigate(Destinations.Pantalla3.ruta)
                        },
                    painter = rememberAsyncImagePainter(imageURL),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            //Columna semitransparente con el nombre y puntuación
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(47.dp)
                    .background(Color(0x860A0A0A))
            ) {

                Text(
                    text = titulo,
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                    color = Color.White
                )
                if (Rating != 0) {
                    Text(
                        text = rating,
                        modifier = Modifier.padding(start = 5.dp),
                        color = Color.White
                    )
                }
            }
        }


}

@Composable
fun CalculateGamesContent(gameList: List<UserGame>, nav: NavController) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 3.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var index = 0

            //Como los muestro en rows de 2 miro la longitud de la lista y la divido en 2
            //Además le sumo uno por si se queda en decimal que tire hacia arriba
            repeat(if (gameList.size % 2 == 0) gameList.size / 2 else (gameList.size / 2) + 1) {

                item {
                    Row()
                    {

                        Surface(elevation = 15.dp) {
                            gameBox(
                                nav,
                                gameList[index].id,
                                gameList[index].name,
                                gameList[index].imageURL
                            )
                        }

                        Spacer(modifier = Modifier.width(30.dp))

                        //Si no existe este index se muestra una caja invible y no interactuable para mantener la interfaz organizada
                        if (index != gameList.size - 1) {
                            Surface(elevation = 15.dp) {
                                gameBox(
                                    nav,
                                    gameList[index + 1].id,
                                    gameList[index + 1].name,
                                    gameList[index + 1].imageURL
                                )
                            }
                        } else {
                            gameBox(nav, "", "", "", 0, true)
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    index = index + 2

                }
            }

        }
}

//TODO ESTO MU CUTRE MU CUTRE
@Composable
fun CalculateGamesContentPlanning(gameList: List<Game>, nav: NavController) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var index = 0

        //Como los muestro en rows de 2 miro la longitud de la lista y la divido en 2
        //Además le sumo uno por si se queda en decimal que tire hacia arriba
        repeat(if (gameList.size % 2 == 0) gameList.size / 2 else (gameList.size / 2) + 1) {

            item {
                Row()
                {

                    Surface(elevation = 15.dp) {
                        gameBox(
                            nav,
                            gameList[index].id,
                            gameList[index].name,
                            gameList[index].imageURL
                        )
                    }

                    Spacer(modifier = Modifier.width(30.dp))

                    //Si no existe este index se muestra una caja invible y no interactuable para mantener la interfaz organizada
                    if (index != gameList.size - 1) {
                        Surface(elevation = 15.dp) {
                            gameBox(
                                nav,
                                gameList[index + 1].id,
                                gameList[index + 1].name,
                                gameList[index + 1].imageURL
                            )
                        }
                    } else {
                        gameBox(nav, "", "", "", 0, true)
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))

                index = index + 2

            }
        }
    }
}

//Esta es la función llamada para actualizar la lista localmente
fun reloadGameList(reloadList: () -> Unit){
    reloadList()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun tabData(tabIndex: Int, updateTabIndex:(Int) -> Unit, pagerState: PagerState, scope: CoroutineScope){

    val tabData = listOf(
        "Completado",
        "Planning",)

    TabRow(
        // Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
        // Override the indicator, using the provided pagerTabIndicatorOffset modifier
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        // Add tabs for all of our pages
        tabData.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier.background(Color.Black),
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                    pagerState.scrollToPage(index)} },
            )
        }
    }
}

    
