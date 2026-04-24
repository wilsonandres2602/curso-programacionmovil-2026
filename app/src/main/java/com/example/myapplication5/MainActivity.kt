package com.example.myapplication5

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myapplication5.ui.theme.MyApplication5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication5Theme {

                var pantalla by remember { mutableStateOf("login") }

                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    when (pantalla) {

                        "login" -> LoginScreen(
                            modifier = Modifier.padding(padding),
                            onLoginSuccess = { pantalla = "registro" }
                        )

                        "registro" -> RegistroScreen(
                            modifier = Modifier.padding(padding),
                            onLogout = { pantalla = "login" },
                            onIrAGalla = { pantalla = "galla" }
                        )

                        "galla" -> GallaScreen(
                            modifier = Modifier.padding(padding),
                            onBack = { pantalla = "registro" }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenido", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(usuario, { usuario = it }, label = { Text("Usuario") })
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            password,
            { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(32.dp))

        Button(onClick = {
            if (usuario == "admin" && password == "1234") {
                onLoginSuccess()
            } else {
                mensaje = "Credenciales incorrectas"
            }
        }) {
            Text("Iniciar Sesión")
        }

        if (mensaje.isNotEmpty()) {
            Text(mensaje, color = Color.Red)
        }
    }
}

@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onIrAGalla: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("Masculino") }
    var resultado by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Registro", fontSize = 22.sp)
            Spacer(Modifier.height(20.dp))
        }

        item {
            OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
            Spacer(Modifier.height(16.dp))
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(sexo == "Masculino", onClick = { sexo = "Masculino" })
                Text("M")
                Spacer(Modifier.width(16.dp))
                RadioButton(sexo == "Femenino", onClick = { sexo = "Femenino" })
                Text("F")
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Button(onClick = { onIrAGalla() }) {
                Text("Ir a Galla")
            }
            Spacer(Modifier.height(12.dp))
        }

        item {
            Button(onClick = {
                resultado = "Registrado: $nombre ($sexo)"
            }) {
                Text("Registrar")
            }
        }

        item {
            Spacer(Modifier.height(12.dp))
            Text(resultado)
        }

        item {
            TextButton(onClick = onLogout) {
                Text("Cerrar sesión", color = Color.Red)
            }
        }
    }
}

@Composable
fun VideoPlayer(url: String) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun GallaScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    var showPopup by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {

        AsyncImage(
            model = "https://picsum.photos/800/300",
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Reproductor de Video")

            Spacer(Modifier.height(8.dp))

            VideoPlayer("https://www.w3schools.com/html/mov_bbb.mp4")

            Spacer(Modifier.height(24.dp))

            Button(onClick = { showPopup = true }) {
                Text("Mostrar Información Extra")
            }
        }

        Surface(color = Color(0xFFE1BEE7)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("© 2026 - Desarrollo Móvil IUE")
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        }
    }

    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            confirmButton = {
                Button(onClick = { showPopup = false }) {
                    Text("Cerrar")
                }
            },
            title = { Text("Información") },
            text = { Text("Contenido adicional mostrado correctamente.") }
        )
    }
}




