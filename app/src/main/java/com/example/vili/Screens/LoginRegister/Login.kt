package viliApp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vili.R
import viliApp.DeviceConfig.Companion.heightPercentage


@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController())
}

@Composable
fun LoginScreen(navController: NavController, viewModel : LoginViewModel = hiltViewModel()) {

    var isLogging by remember { mutableStateOf(true) }

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
                .height(
                    if (isLogging) {
                        250.dp
                    } else 310.dp
                )
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

                ////////////////

                loginFields(isLogging, { isLogging = !isLogging },viewModel.email,viewModel.password,viewModel.repeatPassword,viewModel::onEmailChange,viewModel::onPasswordChange,viewModel::onRepeatPasswordChange, viewModel::onLogInClick, viewModel::onSignUp)


                if (viewModel.toastID != -1){
                    showToast(viewModel.toastID,isLogging,navController)
                    viewModel.disableToast()
                }



            }

        }


    }

}


@Composable
fun showToast(toastID: Int,isLogging: Boolean,nav:NavController){

    //Un toast con más información
    var toastMes = ""

    if (isLogging){
        when(toastID){

            0 ->
            {
                toastMes = "Sesión Iniciada"
                NavigatePop(navController = nav, destination = Destinations.Pantalla2.ruta)
            }
            1 -> toastMes = "Hay algún campo vacío"
            2 -> toastMes = "El formato del correo es incorrecto"
            3 -> toastMes = "El usuario no existe"
            6 -> toastMes = "No existe este usuario"
            7 -> toastMes = "Datos incorrectos"
        }
    }
    else
        when(toastID){
            0 ->
            {
                toastMes = "Registrado con éxito"
                NavigatePop(navController = nav, destination = Destinations.Pantalla2.ruta)
            }
            1 -> toastMes = "No dejes campos vacíos"
            2 -> toastMes = "El formato del email es incorrecto"
            3 -> toastMes = "Las contraseñas no coinciden"
            4 -> toastMes = "La contraseña es demasiado corta"
            6 -> toastMes = "Ya existe un usuario con ese correo"
        }

    Toast.makeText(LocalContext.current, toastMes, Toast.LENGTH_SHORT).show()
}



@Composable
fun logo() {

    val image: Painter = painterResource(id = R.drawable.logovilipng)

    Image(painter = image, contentDescription = "logo") //TODO animar logo con keyframes quiza spring linear fading


}


@Composable
fun loginFields(isLogging: Boolean, toggleLogin: () -> Unit, email:String, password:String, repeatPassword:String, onEmailChange: (String) -> Unit,onPasswordChange: (String) -> Unit,onRepeatPasswordChange: (String) -> Unit,onLogin: () -> Unit,onSignUp: () -> Unit) {
    EmailTextField(email,onEmailChange)
    PasswordTextField(password,onPasswordChange)
    if (!isLogging) {
        ConfirmPasswordTextField(repeatPassword,onRepeatPasswordChange)
    }
    Spacer(Modifier.height(heightPercentage(3)))
    validateButton(isLogging,onLogin,onSignUp)
    ClickableText(isLogging, toggleLogin)


}


@Composable
fun EmailTextField(uiState: String, onValueChange: (String) -> Unit) {

    Column()
    {
        OutlinedTextField(
            value = uiState,
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
            onValueChange = onValueChange,
            label = { Text(text = "Introduce tu e-mail", color = Color.White) },
            placeholder = { Text(text = "Introduce tu e-mail", color = Color.White) },


            )
    }
}


@Composable
fun PasswordTextField(uiState: String, onValueChange: (String) -> Unit) {
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
            value = uiState,
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                Color.White,
                cursorColor = Color.Red,
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.White
            ),
            onValueChange = {
                onValueChange(it)
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

@Composable
fun ConfirmPasswordTextField(uiState: String, onValueChange: (String) -> Unit) {
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
            value = uiState,
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                Color.White,
                cursorColor = Color.Red,
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.White
            ),
            onValueChange = {
                onValueChange(it)
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.keypass),
                    tint = Color.White,
                    contentDescription = "personIcon"
                )

            },


            placeholder = { Text(text = "Repite la contraseña", color = Color.White) },
            label = { Text(text = "Repite la contraseña", color = Color.White) },
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

@Composable
fun validateButton(isLogging: Boolean,onLogin: () -> Unit,onSignUp: () -> Unit) {


    Button(
        onClick =
        if (isLogging) onLogin else onSignUp,

        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {

        Text(
            text = (if (isLogging) {
                "Iniciar Sesión"
            } else {
                "Registrar"
            }),
            color = Color.Black
        )
    }
}


@Composable
fun ClickableText(isLogging: Boolean, toggleLogin: () -> Unit) {
    Text(
        text = AnnotatedString(
            if (isLogging) {
                "¿No tienes cuenta? Registrate"
            } else "¿Ya tienes cuenta? Inicia Sesión"
        ),
        style = TextStyle(color = Color.White, textDecoration = TextDecoration.Underline),
        modifier = Modifier.clickable(onClick = { toggleLogin() })
    )
}














