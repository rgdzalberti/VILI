package viliApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(DeviceConfig.heightPercentage(5))
        .background(Color.Black)) {

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

            HorizontalPager(count = 2,state = pagerState) { page ->

                when(page){
                    0 -> {viewModel.updateTabData(0); LazyList(navController, userGameList = CentralizedData.gameList.value, isUserGameListB = true)  }
                    1 -> {viewModel.updateTabData(1); LazyList(navController, gameList = CentralizedData.planningList.value, isGameListB = true) }
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
                    pagerState.scrollToPage(index)}
                          },
            )
        }
    }
}

    
