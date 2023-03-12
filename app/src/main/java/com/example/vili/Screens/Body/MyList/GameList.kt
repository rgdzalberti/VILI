package com.example.vili.Screens.Body.MyList

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vili.Common.Complex.BottomBar
import com.example.vili.Common.Complex.BottomBarClass
import com.example.vili.Common.Composables.BackPressHandler
import com.example.vili.R
import com.example.vili.Screens.Body.Home.MainScreenViewModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import viliApp.*

@Preview
@Composable
fun previewGameList() {
    GameList(rememberNavController())
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameList(navController: NavController, viewModel: GameListViewModel = hiltViewModel()) {

    //Actualizo el icono del BottomBar
    BottomBarClass.updateIndex(2)

    Scaffold(bottomBar = { BottomBar() })
    {
        GameListBody(navController = navController)
    }

    //Icono arriba izquierda popear pantalla
    if (viewModel.popBack){
        BottomBarClass.updateIndex(1)
        viewModel.popBack()
        navController.popBackStack()
    }
    //PopStackBack con OS
    BackPressHandler(onBackPressed = { navController.popBackStack()  })

    //Aquí cambio la pantalla si se toca el bottomBar
    if (NavigationFunctions.changeScreen.value){
        NavigationFunctions.changeScreen(-1)
        when(NavigationFunctions.screenID.value){
            0 -> {navController.navigate(route = Destinations.Profile.ruta)}
            1 -> {navController.navigate(route = Destinations.MainScreen.ruta)}
            2 -> {}
        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GameListBody(navController:NavController, viewModel: GameListViewModel = hiltViewModel(), MainScreenViewModel: MainScreenViewModel = hiltViewModel()){
    systemBarColor(color = Color.Black)
    getDeviceConfig()

    //No se puede poner en el viewmodel. Utilizo esto para controlar la pestaña y su animación
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    //Se hace la comprobación de si se ha hecho algún delete desde la última vez que estuvo en esta pantalla
    //Si la respuesta es sí, esto será true y se actualizará la lista localmente también.
    if (CentralizedData.recomposeUI.value == true) {
        reloadGameList(viewModel::reloadList)
    }

    Column() {


    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.1f)
        .background(Color.Black)) {

        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)) {
            TopBarList(dropDown = viewModel::sortList , nav = navController,viewModel.tabIndex,viewModel::popBack)
        }


        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            TabDATAList(viewModel.tabIndex,viewModel::updateTabData,pagerState,scope)
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
}


//Esta es la función llamada para actualizar la lista localmente
fun reloadGameList(reloadList: () -> Unit){
    reloadList()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabDATAList(tabIndex: Int, updateTabIndex:(Int) -> Unit, pagerState: PagerState, scope: CoroutineScope){

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

@Composable
fun TopBarList(dropDown: (Int) -> Unit, nav:NavController, page:Int, navigate:()->Unit){

    //TOPBAR
    Row(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(5))
            .background(Color.Black),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(start = 5.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { BottomBarClass.updateIndex(1); navigate() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrowback),
                    "",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

        }

        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(end = 5.dp),
            contentAlignment = Alignment.CenterEnd
        ) {

            DropDownSort(sortList = dropDown,page)

        }


    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownSort(sortList: (Int) -> Unit, page: Int) {

    var listItems = emptyList<String>()

    if (page == 0){
        listItems = listOf("Nombre", "Score")
    }
    else if (page==1){
        listItems = listOf("Nombre")
    }

    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }


    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.filterlist),
            "",
            modifier = Modifier.size(30.dp),
            tint = Color.White
        )

        // menu
        ExposedDropdownMenu(
            modifier = Modifier
                .background(Color(0xFF1B1B1B))
                .width(100.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(modifier = Modifier.background(Color(0xFF1B1B1B)),onClick = {
                    selectedItem = selectedOption

                    //Actualizo en mi viewModel el index seleccionado para cuando se confirme
                    sortList(listItems.indexOf(selectedItem))

                    expanded = false
                }) {
                    Text(text = selectedOption, color = Color.White)
                }
            }
        }
    }
}
