package viliApp

import android.graphics.drawable.VectorDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vili.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.w3c.dom.Text
import viliApp.DeviceConfig.Companion.heightPercentage
import viliApp.DeviceConfig.Companion.returnHeight
import viliApp.DeviceConfig.Companion.returnWidth


@Preview
@Composable
fun Preview() {
    loginScreen(rememberNavController())
}

@Composable
fun loginScreen(navController: NavController) {

    //Llamo a mi funcion que cambia el color de la barra y la función que obtiene los dp de la pantalla
    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

    //Everything Container
    Column(
        Modifier
            .fillMaxSize()
            //.background(Color(0xFF0A0A0A))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0A0A0A), Color(0xFF181717)),

                    /*
                    start = Offset(0f, 0f), // top left corner
                    end = Offset(
                        returnHeight()
                            .toString()
                            .substringBefore('.')
                            .toFloat(),
                        returnWidth()
                            .toString()
                            .substringBefore('.')
                            .toFloat()
                    ) // bottom right corner

                     */
                )
            ), horizontalAlignment = Alignment.CenterHorizontally

    ) {


        //LOGO
        Column(
            modifier = Modifier
                .height(heightPercentage(40))
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            logo()
        }

        Column(
            Modifier
                .height(200.dp)
                .width(330.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF1A1919)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //CAMPOS TEXT
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                loginFields()

            }
        }


    }

}

@Composable
fun logo() {

    val image: Painter = painterResource(id = R.drawable.logovili)

    Image(painter = image, contentDescription = "logo")

}

@Composable
fun loginFields() {
    EmailTextField()
    PasswordTextField()
    //loginWithGoogle()
}


@Composable
fun EmailTextField() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Column()
    {
        OutlinedTextField(
            value = text,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 25.dp),
            singleLine = true,
            //TextOverflow = Ellipsis,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                Color.White,
                cursorColor = Color.Red,
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.White
            ),
            leadingIcon = {
                Icon(
                    //    modifier = Modifier.size(34.dp),
                    //    imageVector = ImageVector.vectorResource(id = R.drawable.ic_your_icon)
                    imageVector = Icons.Default.Person,
                    tint = Color.White,
                    contentDescription = "personIcon"
                )
            },
            //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
            onValueChange = {
                text = it
            },
            label = { Text(text = "Introduce tu e-mail", color = Color.White) },
            placeholder = { Text(text = "Introduce tu e-mail", color = Color.White) },


            )
    }
}


@Composable
fun PasswordTextField() {
    Column(
        modifier = Modifier.padding(horizontal = 25.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }

        val icon = if (passwordVisibility)
            painterResource(id = R.drawable.view)
        else
            painterResource(id = R.drawable.hide)

        OutlinedTextField(
            value = password,
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                Color.White,
                cursorColor = Color.Red,
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.White
            ),
            onValueChange = {
                password = it

            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.keypass),
                    tint = Color.White,
                    contentDescription = "personIcon"
                )

            },


            placeholder = { Text(text = "Contraseña", color = Color.White) },
            label = { Text(text = "Contraseña", color = Color.White) },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = icon,
                        tint = Color.White,
                        contentDescription = "Visibility Icon"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation()
        )
    }
}










