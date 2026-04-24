package com.example.myapplication5

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication5.ui.theme.MyApplication5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication5Theme {
                // Estado de navegación: loginMyApplication5Theme, registro, galla
                var pantalla by remember { mutableStateOf("login") }

                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    // Navegación principal
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



// --- 1. PANTALLA DE LOGIN ---
@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5)) // Fondo gris claro
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Bienvenido",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF1976D2), // Azul
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (usuario.trim() == "admin" && password.trim() == "1234") {
                    onLoginSuccess()
                } else {
                    mensaje = "❌ Credenciales incorrectas"
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Iniciar Sesión", fontSize = 18.sp)
        }

        if (mensaje.isNotEmpty()) {
            Text(mensaje, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
        }
    }
}


// --- 2. PANTALLA DE REGISTRO ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onIrAGalla: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("Masculino") }
    var pais by remember { mutableStateOf("Colombia") }
    var resultado by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Formulario de Registro", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF388E3C))
            Spacer(Modifier.height(20.dp))
        }

        item {
            OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
        }

        item {
            Text("Sexo:", modifier = Modifier.fillMaxWidth())
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(sexo == "Masculino", onClick = { sexo = "Masculino" })
                Text("M")
                Spacer(Modifier.width(20.dp))
                RadioButton(sexo == "Femenino", onClick = { sexo = "Femenino" })
                Text("F")
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = { onIrAGalla() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)) // Naranja
            ) {
                Text("Ir a Pantalla Galla")
            }
            Spacer(Modifier.height(12.dp))
        }

        item {
            Button(
                onClick = { resultado = "Registrado: $nombre ($sexo)" },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
            ) {
                Text("Registrar")
            }
        }

        if (resultado.isNotEmpty()) {
            item { Text(resultado, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold) }
        }

        item {
            TextButton(onClick = onLogout) {
                Text("Cerrar Sesión", color = Color.Red)
            }
        }
    }
}




// --- 3. NUEVA PANTALLA: GALLA ---

// Banner de imágenes deslizable
@Composable
fun BannerCarousel() {
    val images = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3
    )

    val pagerState = rememberPagerState(pageCount = { images.size })

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) { page ->

            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .background(
                            color = if (pagerState.currentPage == index)
                                Color.DarkGray
                            else
                                Color.LightGray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}


// Reproducción de vídeo
@Composable
fun SimpleVideoPlayer() {
    val context = LocalContext.current

    AndroidView(
        factory = {
            VideoView(it).apply {
                val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.video1}")
                setVideoURI(uri)

                val mediaController = MediaController(context)
                mediaController.setAnchorView(this)
                setMediaController(mediaController)

                start()
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
        // ENCABEZADO CON IMAGEN (Representada por un Box con color)

        BannerCarousel()

        // CONTENIDO PRINCIPAL (VIDEO Y BOTÓN)
        Column(
            modifier = Modifier.weight(1f).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Reproductor de Video", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            SimpleVideoPlayer()

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Este video muestra la integración de contenido multimedia dentro de una aplicación móvil desarrollada con Jetpack Compose.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(24.dp))

            Button(onClick = { showPopup = true }) {
                Text("Mostrar Información Extra (Popup)")
            }
        }

        // FOOTER
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFE1BEE7)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("© 2026 - Desarrollo Móvil IUE", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        }
    }

    // POPUP (ALERT DIALOG)
    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            confirmButton = {
                Button(onClick = { showPopup = false }) { Text("Entendido") }
            },
            title = { Text("Detalle de Galla") },
            text = { Text("Esta es la tercera pantalla donde puedes ver contenido multimedia y detalles avanzados.") }
        )
    }
}





