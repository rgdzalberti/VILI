package viliApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vili.R

@Preview
@Composable
fun previewGameDetails() {
    GameDetails(rememberNavController())
}

@Composable
fun GameDetails(navController: NavController, viewModel: GameDetailsViewModel = hiltViewModel()) {

    //Caja para el submenú
    Box() {

        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF161616))
        ) {

            //region TopBar
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(DeviceConfig.heightPercentage(5))
                    .background(Color.Black)
            ) {

                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(0.50f)
                ) {
                    //Go Back
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.arrowback),
                            tint = Color.White,
                            contentDescription = "Visibility Icon"
                        )
                    }
                }

                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(0.50f), contentAlignment = Alignment.CenterEnd

                ) {
                    IconButton(onClick = {
                        viewModel.statusMoreOptions()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = com.example.vili.R.drawable.edit),
                            tint = Color.White,
                            contentDescription = "Visibility Icon"
                        )
                    }
                }
            }
            //endregion

            //region PORTADA Y TEXTO DERECHA
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF161616))
                    .height(DeviceConfig.heightPercentage(30))
            ) {
                Portada(viewModel)

                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        Modifier.matchParentSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 3.dp, end = 3.dp),
                            text = viewModel.gameData.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = viewModel.gameData.releaseDate,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.padding(top = 5.dp))
                        if (viewModel.gameData.avgDuration.isNotBlank()) {
                            GenreBox(genre = "${viewModel.gameData.avgDuration} Horas", Color.Black)

                        }
                    }
                }

            }
            //endregion

            //region BODY
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF161616), Color(0x9F3D2121)
                            )
                        )
                    )
            ) {
                //region DESCRIPCION
                Column(
                    Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = viewModel.gameData.description,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
                //endregion

                //region GENEROS
                Spacer(modifier = Modifier.height(20.dp))
                LazyRow {
                    viewModel.listaGenres.forEach {
                        item {
                            Spacer(modifier = Modifier.width(10.dp))
                            GenreBox(genre = it)
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
                //endregion
            }
            //endregion

        }
        if (viewModel.enableMoreOptions) {
            MoreOptions(
                viewModel::statusMoreOptions,
                viewModel::addGameToUserList,
                viewModel::removeGameFromUserList,
                viewModel.gameID,
                viewModel::dropDownValue,
                viewModel.ddValue,
                viewModel::addGameToUserPlanningList,
                viewModel::removeGameFromUserPlanningList,
                viewModel.planned,
                viewModel.completed,
                viewModel::updateCompleted,
                viewModel::updatePlanned,
                viewModel.stars,
                viewModel::updateStars
            )
        }
    }


}

@Composable
fun Portada(viewModel: GameDetailsViewModel) {


    Box(
        modifier = Modifier
            .height(220.dp)
            .width(160.dp)
            .padding(5.dp)
            .background(Color.Transparent)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp)),
            painter = rememberAsyncImagePainter(viewModel.gameData.imageURL),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }


}


@Composable
fun MoreOptions(
    disableMoreOptions: () -> Unit,
    saveToUserList: (Int) -> Unit,
    deleteFromUserList: (String) -> Unit,
    gameID: String,
    updateDDV: (Int) -> Unit,
    ddValue: Int,
    saveToUserPlanningList: () -> Unit,
    deleteFromUserPlanningList: (String) -> Unit,
    planned: Boolean,
    completed: Boolean,
    updateCompleted: () -> Unit,
    updatePlanning: () -> Unit,
    currentIndex: Int,
    updateStars: (Int) -> Unit
) {

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xCE0A0A0A)), contentAlignment = Alignment.Center
    ) {

        Button(modifier = Modifier
            .fillMaxSize()
            //Un boton invisible de fondo para que se cierre el menu cuando se clicka fuera
            //Además actualizo el index del dropdown seleccionado a 0 pues se resetea al salir
            .alpha(0f), onClick = { disableMoreOptions(); updateDDV(0) }) {}

        Box(
            Modifier
                .width(DeviceConfig.widthPercentage(80))
                .height(DeviceConfig.heightPercentage(40))
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF020202))
        ) {
            Text(text = "aaa")

            Button(modifier = Modifier
                .fillMaxSize()
                .alpha(0f), onClick = { /*ANTIMATERIA*/ }) {}

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Row(
                    Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF1B1B1B))
                ) { Text(text = "") }

                //INFO BODY
                Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {


                    Text(
                        text = "Status",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Column(
                        Modifier
                            .fillMaxHeight(0.35f)
                            .fillMaxWidth(0.5f)
                            .clip(RoundedCornerShape(5.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DropDownDetails(updateDDV)
                    }
                    if (ddValue == 0 && !completed) {
                        ratingStars(currentIndex, updateStars)
                    }
                }
                //////////

                Row(
                    Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .weight(1f, false)
                        .background(Color(0xFF1B1B1B)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    when (ddValue) {

                        //Completado
                        0 -> {

                            //Si ya está el juego en la lista se da la opción de eliminar
                            if (completed) {
                                ConfirmButton("Eliminar",
                                    { deleteFromUserList(gameID);updateCompleted() })
                            } else {
                                ConfirmButton(
                                    "Añadir",
                                    { saveToUserList(currentIndex); updateCompleted() })
                            }

                        }

                        //Planning
                        1 -> {
                            //Si ya está el juego en la lista se da la opción de eliminar
                            if (planned) {
                                ConfirmButton("Eliminar",
                                    { deleteFromUserPlanningList(gameID); updatePlanning() })
                            } else {
                                ConfirmButton(
                                    "Añadir",
                                    { saveToUserPlanningList(); updatePlanning() })
                            }
                        }

                    }


                }

            }


        }

    }

}

@Composable
fun GenreBox(genre: String, color: Color = Color.Red) {

    Box(
        Modifier
            .clip(RoundedCornerShape(9.dp))
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
            .background(color), contentAlignment = Alignment.Center
    ) {
        Text(text = " " + genre + " ", color = Color.White, fontSize = 15.sp)
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownDetails(updateDDV: (Int) -> Unit) {

    val listItems = arrayOf("Completado", "Planning")

    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }


    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
        expanded = !expanded
    }) {

        // text field
        TextField(
            value = selectedItem,
            textStyle = TextStyle.Default.copy(fontSize = 14.sp),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,

                    )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                backgroundColor = Color(0xFF1B1B1B),
                textColor = Color.White,
                trailingIconColor = Color.White,
                focusedTrailingIconColor = Color.White,
                disabledLeadingIconColor = Color.White,
                focusedIndicatorColor = Color(0xFF1B1B1B)
            )
        )

        // menu
        ExposedDropdownMenu(modifier = Modifier.background(Color(0xFF1B1B1B)),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            listItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(modifier = Modifier.background(Color(0xFF1B1B1B)), onClick = {
                    selectedItem = selectedOption

                    //Actualizo en mi viewModel el index seleccionado para cuando se confirme
                    updateDDV(listItems.indexOf(selectedItem))

                    expanded = false
                }) {
                    Text(text = selectedOption, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ConfirmButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .padding(end = 5.dp),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {
        Text(text = text, color = Color.Black)
    }
}

@Composable
fun ratingStars(currentIndex: Int, updateStars: (Int) -> Unit) {

    val starList = listOf<Int>(1, 2, 3, 4, 5)

    Row() {
        starList.forEach {
            IconButton(onClick = { updateStars(it) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = if (currentIndex >= it) {
                            R.drawable.starfilled
                        } else R.drawable.star
                    ), "", modifier = Modifier.size(30.dp), tint = Color.Yellow
                )
            }
        }
    }

}



