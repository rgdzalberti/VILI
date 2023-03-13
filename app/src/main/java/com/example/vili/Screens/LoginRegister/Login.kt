package viliApp

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.TransformOrigin
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
import com.example.vili.Model.Querys.FBAuth
import com.example.vili.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import viliApp.DeviceConfig.Companion.heightPercentage
import viliApp.NavigationFunctions.Companion.NavigatePop


@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController())
}

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {

    var isLogging by remember { mutableStateOf(true) }

    //Llamo a mi funcion que cambia el color de la barra y la función que obtiene los dp de la pantalla
    systemBarColor(color = Color(0xFF0A0A0A))
    getDeviceConfig()

    //Si el ViewModel recibe la orden de cambiar de pantalla aquí se cumple
    if (viewModel.popNContinue) {
        viewModel.popNContinue = false
        NavigatePop(navController = navController, destination = Destinations.MainScreen.ruta)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0A0A0A), Color(0xFF181717)),
                )
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //region LOGO
        Column(
            modifier = Modifier
                .height(heightPercentage(40))
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Logo()
        }
        //endregion

        //region TextFields
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
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                EmailTextField(value = viewModel.email, onValueChange = viewModel::onEmailChange)
                PasswordTextField(
                    value = viewModel.password, onValueChange = viewModel::onPasswordChange
                )
                if (!isLogging) {
                    ConfirmPasswordTextField(
                        viewModel.repeatPassword, viewModel::onRepeatPasswordChange
                    )
                }
                Spacer(Modifier.height(heightPercentage(3)))
                validateButton(
                    navController,
                    isLogging,
                    viewModel.email,
                    viewModel.password,
                    viewModel::turnPopNContinue
                )
                ClickableText(isLogging, {isLogging = !isLogging})


            }

        }
        //endregion


    }

}


@Composable
fun LoginFields(
    isLogging: Boolean,
    toggleLogin: () -> Unit,
    email: String,
    password: String,
    repeatPassword: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    nav: NavController,
    turnPopNContinue: () -> Unit
) {
    EmailTextField(email, onEmailChange)
    PasswordTextField(password, onPasswordChange)
    if (!isLogging) {
        ConfirmPasswordTextField(repeatPassword, onRepeatPasswordChange)
    }
    Spacer(Modifier.height(heightPercentage(3)))
    validateButton(nav, isLogging, email, password, turnPopNContinue)
    ClickableText(isLogging, toggleLogin)


}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Logo() {

    val image: Painter = painterResource(id = R.drawable.logovilipng)

    var startAnimation by remember { mutableStateOf(false) }
    //Cuando la pantalla carga le digo que lance las animaciones
    LaunchedEffect(Unit) { startAnimation = true }

    AnimatedVisibility(
        visible = startAnimation,
        enter = slideInVertically(initialOffsetY = { -40 }) + expandVertically(
            expandFrom = Alignment.Top
        ) + scaleIn(
            transformOrigin = TransformOrigin(0.5f, 0f)
        ) + fadeIn(initialAlpha = 0.3f),
        exit = fadeOut()
    ) {
        Image(painter = image, contentDescription = "logo")
    }


}

@Composable
fun EmailTextField(value: String, onValueChange: (String) -> Unit) {

    Column() {
        OutlinedTextField(
            value = value,
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
fun PasswordTextField(value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 25.dp),

        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }

        val icon = if (passwordVisibility) painterResource(id = R.drawable.view)
        else painterResource(id = R.drawable.hide)

        OutlinedTextField(
            value = value,
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
                        painter = icon, tint = Color.White, contentDescription = "Visibility Icon"
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
fun ConfirmPasswordTextField(text: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 25.dp),

        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }

        val icon = if (passwordVisibility) painterResource(id = R.drawable.view)
        else painterResource(id = R.drawable.hide)

        OutlinedTextField(
            value = text,
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
                        painter = icon, tint = Color.White, contentDescription = "Visibility Icon"
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
fun validateButton(
    nav: NavController,
    isLogging: Boolean,
    email: String,
    password: String,
    turnPopNContinue: () -> Unit
) {

    val context = LocalContext.current

    Button(
        onClick = {

            when (isLogging) {
                true -> {
                    FBAuth.onLogIn(email, password, context) { success ->
                        if (success) {
                            FBAuth.updateUserUID(Firebase.auth.uid.toString())
                            turnPopNContinue()
                        }
                    }
                }
                false -> {
                    FBAuth.onSignUp(email, password, context) { success ->
                        if (success) {
                            turnPopNContinue()
                        }
                    }
                }
            }

        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {

        Text(
            text = (if (isLogging) {
                "Iniciar Sesión"
            } else {
                "Registrar"
            }), color = Color.Black
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















