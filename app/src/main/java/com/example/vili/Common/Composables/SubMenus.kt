package com.example.vili.Common.Composables

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vili.R
import com.example.vili.myApp.theme.ObscureBlack
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import viliApp.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


//region SearchMenu
@OptIn(ExperimentalPagerApi::class)
@Composable
fun SearchMenu(searchText: String, onValueChange: (String) -> Unit, gameList: List<Game>, nav: NavController) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    //region USERS
    //Actualizo la lista de usuarios. Sería mas correcto tenerlo en un ViewModel, pero para lo poco que es me parece contraproducente
    //crear otro archivo
    var userList by remember { mutableStateOf(listOf<UserProfile>()) }

    LaunchedEffect(Unit) {
        FBCRUD.getUsersProfiles().collect {
            if (userList != it) {
                userList = it.sortedBy { it.name }
            }
        }
    }
    //endregion

    Box() {

        Button(modifier = Modifier
            .fillMaxSize()
            .alpha(0f),
            onClick = { /*EVITA HITBOXES DE ATRÁS*/ }) {}

        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(1f)
                .background(Color(0xFF181818)),
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.10f)
                    .background(ObscureBlack), contentAlignment = Alignment.Center
            ) {


                //TEXTFIELD
                Column() {
                    OutlinedTextField(
                        value = searchText,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(1f),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            Color.White,
                            cursorColor = Color.Red,
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.White
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.search),
                                modifier = Modifier.size(30.dp),
                                tint = Color.White,
                                contentDescription = "searchIcon"
                            )
                        },
                        onValueChange = onValueChange,
                        label = { Text(text = "Introduce el término", color = Color.White) },
                    )
                }


            }

            Row(
                Modifier
                    .background(ObscureBlack)
                    .fillMaxWidth()
                    .fillMaxHeight(0.05f)) {
                TabDATASearch(tabIndex = TabDataSearch.tabIndex.value, updateTabIndex = TabDataSearch::updateTabData, pagerState = pagerState, scope = rememberCoroutineScope())
            }

            //RESULTADOS
            HorizontalPager(count = 2,state = pagerState) { page ->

                when(page){
                    0 -> {TabDataSearch.updateTabData(0); GameLazyList(nav, searchText, gameList = gameList)  }
                    1 -> {TabDataSearch.updateTabData(1)

                        UserLazyList(nav,searchText,userList)
                    }
                }

            }



        }
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabDATASearch(tabIndex: Int, updateTabIndex:(Int) -> Unit, pagerState: PagerState, scope: CoroutineScope){

    val tabData = listOf(
        "Juegos",
        "Usuarios",)

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

class TabDataSearch{
    companion object{
        var tabIndex = mutableStateOf(0)

        fun updateTabData(newValue: Int){
            tabIndex.value = newValue
        }
    }
}

//endregion

@Composable
fun SettingsMenu(logOut: () -> Unit, switchSettings: () -> Unit) {

    var offsetX by remember { mutableStateOf(0f) }
    val boxWidth =
        DeviceConfig.dpToFloat(DeviceConfig.DPwidthPercentage(DeviceConfig.screenWidth, 70))
    var returningDefault by remember { mutableStateOf(false) }

    val offsetAnimation: Dp by animateDpAsState(
        if (returningDefault) {
            0.dp;
        } else DeviceConfig.pxToDp(offsetX),
        tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )
    if (returningDefault) {
        LaunchedEffect(Unit) {
            delay(300)
            returningDefault = false
        }
    }


    Row(Modifier
        .fillMaxSize()
        .background(Color.Transparent)
        .offset {
            IntOffset(
                DeviceConfig
                    .dpToPx(offsetAnimation)
                    .roundToInt(), 0
            )
        }
        .pointerInput(Unit) {
            detectDragGestures(onDragEnd = {
                //Si se mueve lo suficiente a la izquierda se cierra
                if ((offsetX.absoluteValue * 0.7) > DeviceConfig.tinyFloatToBig(
                        boxWidth
                    )
                ) {
                    switchSettings()
                } else {
                    //Si no, vuelve a su posición
                    offsetX = 0f
                    returningDefault = true

                }

            }) { change, dragAmount ->
                change.consume()
                if (dragAmount.x < 0) {
                    offsetX += dragAmount.x
                } else if (dragAmount.x > 0 && offsetX < 0) {
                    offsetX += dragAmount.x
                }

            }
        }) {

        Box(
            Modifier

                .weight(boxWidth)
        ) {

            Button(modifier = Modifier.fillMaxSize(), onClick = {}) {}

            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color(0xFF000000)),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 15.dp)
                    .fillMaxHeight(0.05f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    onClick = {
                        logOut()
                    }) {
                    Text(text = "Logout", color = Color.Black, fontWeight = FontWeight.Bold)

                }
            }

        }
        Box(Modifier.weight(0.3f)) {
            //Boton invisible fuera para cerrar fondo
            Button(modifier = Modifier
                .fillMaxSize()
                .alpha(0f), onClick = { switchSettings() }) {}
        }


    }


}

@Composable
fun TopBar(switchSettings: () -> Unit, switchSearch: () -> Unit, nav: NavController) {

    //TOPBAR
    Row(
        Modifier
            .fillMaxWidth()
            .height(DeviceConfig.heightPercentage(5))
            .background(ObscureBlack),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .background(Color.Transparent) //Color(0xFF0A0A0A)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(start = 5.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { switchSettings() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.settings),
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
                .weight(0.50f),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(R.drawable.logovilipng), contentDescription = "")
        }
        Box(
            Modifier
                .background(Color.Transparent)
                .fillMaxHeight()
                .weight(0.33f)
                .padding(end = 5.dp), contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(onClick = { switchSearch() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.search),
                    "",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

        }


    }
}

@Composable
fun BackPressHandler(onBackPressed: () -> Unit, backPressedDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher)
{
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}