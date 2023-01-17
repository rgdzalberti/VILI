package viliApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Preview
@Composable
fun previewGameDetails(){
    GameDetails()
}

@Composable
fun GameDetails(viewModel: GameDetailsViewModel = hiltViewModel()){

    Column(Modifier.fillMaxSize().background(Color.Red)) {
        Text(text = viewModel.gameID)
    }

}