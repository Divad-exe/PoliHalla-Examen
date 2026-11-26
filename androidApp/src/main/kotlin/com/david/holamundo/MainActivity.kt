package com.david.holamundo

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PoliHallaAndroid() }
    }
}

@Composable
private fun PoliHallaAndroid() {
    val contexto = LocalContext.current
    var alRecibirImagen by remember { mutableStateOf<((String) -> Unit)?>(null) }
    val selector = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) alRecibirImagen?.invoke(uri.toString())
        alRecibirImagen = null
    }

    App(
        elegirImagen = { respuesta ->
            alRecibirImagen = respuesta
            selector.launch("image/*")
        },
        elegirFecha = { fechaActual, respuesta ->
            val calendario = Calendar.getInstance()
            if (esFechaValida(fechaActual)) {
                val partes = fechaActual.split("/")
                calendario.set(partes[2].toInt(), partes[1].toInt() - 1, partes[0].toInt())
            }
            DatePickerDialog(
                contexto,
                { _, anio, mes, dia -> respuesta(String.format(Locale.ROOT, "%02d/%02d/%04d", dia, mes + 1, anio)) },
                calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        mostrarToast = { mensaje -> Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show() },
        imagen = { uri, nombre, modifier -> ImagenAndroid(uri, nombre, modifier) }
    )
}

@Composable
private fun ImagenAndroid(uri: String, nombre: String, modifier: Modifier) {
    val contexto = LocalContext.current
    var imagen by remember(uri) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(uri) {
        imagen = if (uri.isBlank()) null else withContext(Dispatchers.IO) {
            runCatching {
                val opciones = BitmapFactory.Options().apply { inSampleSize = 4 }
                contexto.contentResolver.openInputStream(Uri.parse(uri))?.use { flujo ->
                    BitmapFactory.decodeStream(flujo, null, opciones)?.asImageBitmap()
                }
            }.getOrNull()
        }
    }

    val imagenActual = imagen
    if (imagenActual == null) {
        ImagenPredeterminada(nombre, modifier)
    } else {
        Image(imagenActual, contentDescription = "Imagen de $nombre", modifier = modifier, contentScale = ContentScale.Crop)
    }
}
