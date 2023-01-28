package viliApp

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
import com.example.vili.R
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
fun gameList(navController: NavController, viewModel: GameListViewModel = hiltViewModel(), MainScreenViewModel:MainScreenViewModel = hiltViewModel()) {

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
        .height(DeviceConfig.heightPercentage(10))
        .background(Color.Black)) {

        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)) {
            TopBarList(goHome = MainScreenViewModel::updateIndex, dropDown = viewModel::sortList , nav = navController,viewModel.tabIndex)
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

@Composable
fun TopBarList(goHome: (Int) -> Unit, dropDown: (Int) -> Unit, nav:NavController,page:Int){

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
                .background(Color.Transparent) //Color(0xFF0A0A0A)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(start = 5.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { goHome(1) }) {
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
            modifier = Modifier.background(Color(0xFF1B1B1B)).width(100.dp),
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

    
