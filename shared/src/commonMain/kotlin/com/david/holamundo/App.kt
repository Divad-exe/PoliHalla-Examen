package com.david.holamundo

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val reportesSaver = listSaver<SnapshotStateList<Reporte>, String>(
    save = { reportes ->
        listOf("1") + reportes.flatMap { reporte ->
            listOf(
                reporte.id.toString(), reporte.nombre, reporte.ubicacion, reporte.fecha,
                reporte.descripcion, reporte.tipo.name, reporte.recuperado.toString(), reporte.imagenUri
            )
        }
    },
    restore = { valores ->
        valores.drop(1).chunked(8).mapNotNull { datos ->
            if (datos.size != 8) return@mapNotNull null
            val id = datos[0].toLongOrNull() ?: return@mapNotNull null
            val tipo = TipoReporte.entries.firstOrNull { it.name == datos[5] } ?: return@mapNotNull null
            Reporte(id, datos[1], datos[2], datos[3], datos[4], tipo, datos[6].toBoolean(), datos[7])
        }.toMutableStateList()
    }
)

private val formularioSaver = listSaver<DatosFormulario, String>(
    save = { form -> listOf(form.nombre, form.ubicacion, form.fecha, form.descripcion, form.tipo.name, form.recuperado.toString(), form.imagenUri) },
    restore = { datos ->
        DatosFormulario(
            nombre = datos[0], ubicacion = datos[1], fecha = datos[2], descripcion = datos[3],
            tipo = TipoReporte.entries.firstOrNull { it.name == datos[4] } ?: TipoReporte.PERDIDO,
            recuperado = datos[5].toBoolean(), imagenUri = datos[6]
        )
    }
)

@Composable
fun App(
    elegirImagen: (((String) -> Unit) -> Unit)? = null,
    elegirFecha: ((String, (String) -> Unit) -> Unit)? = null,
    mostrarToast: (String) -> Unit = {},
    imagen: @Composable (String, String, Modifier) -> Unit = { _, nombre, modifier ->
        ImagenPredeterminada(nombre, modifier)
    }
) {
    val reportes = rememberSaveable(saver = reportesSaver) { reportesIniciales().toMutableStateList() }
    var pantalla by rememberSaveable { mutableStateOf("Inicio") }
    var origenDelDetalle by rememberSaveable { mutableStateOf("Reportes") }
    var origenDelFormulario by rememberSaveable { mutableStateOf("Reportes") }
    var reporteSeleccionado by rememberSaveable { mutableStateOf(-1L) }
    var reporteAEliminar by rememberSaveable { mutableStateOf(-1L) }
    var editando by rememberSaveable { mutableStateOf(false) }
    var formulario by rememberSaveable(stateSaver = formularioSaver) { mutableStateOf(DatosFormulario()) }
    var mostrarErrores by rememberSaveable { mutableStateOf(false) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var filtro by rememberSaveable { mutableStateOf("Todos") }

    fun abrirFormulario(reporte: Reporte?, tipoInicial: TipoReporte = TipoReporte.PERDIDO) {
        origenDelFormulario = pantalla
        editando = reporte != null
        reporteSeleccionado = reporte?.id ?: -1L
        formulario = if (reporte == null) DatosFormulario(tipo = tipoInicial) else DatosFormulario(
            reporte.nombre, reporte.ubicacion, reporte.fecha, reporte.descripcion,
            reporte.tipo, reporte.recuperado, reporte.imagenUri
        )
        mostrarErrores = false
        pantalla = "Formulario"
    }

    fun abrirDetalle(reporte: Reporte) {
        origenDelDetalle = pantalla
        reporteSeleccionado = reporte.id
        pantalla = "Detalle"
    }

    val listaVisible = reportes.filter { reporte ->
        val coincideBusqueda = busqueda.isBlank() || reporte.nombre.contains(busqueda, ignoreCase = true) ||
            reporte.ubicacion.contains(busqueda, ignoreCase = true)
        val coincideFiltro = when (filtro) {
            "Perdidos" -> reporte.tipo == TipoReporte.PERDIDO
            "Encontrados" -> reporte.tipo == TipoReporte.ENCONTRADO
            else -> true
        }
        coincideBusqueda && coincideFiltro
    }

    MaterialTheme(colorScheme = lightColorScheme(
        primary = AzulPrincipal,
        secondary = AzulOscuro,
        background = Fondo,
        surface = Color.White,
        surfaceVariant = AzulClaro,
        onSurface = Tinta
    )) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Fondo,
            bottomBar = {
                AnimatedVisibility(
                    visible = pantalla in listOf("Inicio", "Reportes", "Mis reportes"),
                    enter = fadeIn(tween(180)) + slideInVertically(tween(220)) { it / 3 },
                    exit = fadeOut(tween(120)) + slideOutVertically(tween(180)) { it / 3 }
                ) {
                    NavigationBar(containerColor = Color.White, tonalElevation = 3.dp) {
                        listOf(
                            "Inicio" to IconoPoli.INICIO,
                            "Reportes" to IconoPoli.LISTA,
                            "Mis reportes" to IconoPoli.USUARIO
                        ).forEach { (destino, icono) ->
                            NavigationBarItem(
                                selected = pantalla == destino,
                                onClick = { pantalla = destino },
                                icon = { Icono(icono, Modifier.size(22.dp), if (pantalla == destino) AzulPrincipal else TextoSecundario) },
                                label = { Text(destino) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedTextColor = AzulPrincipal,
                                    unselectedTextColor = TextoSecundario,
                                    indicatorColor = AzulClaro
                                )
                            )
                        }
                    }
                }
            }
        ) { padding ->
            AnimatedContent(
                targetState = pantalla,
                modifier = Modifier.fillMaxSize(),
                transitionSpec = {
                    fun orden(destino: String) = when (destino) {
                        "Inicio" -> 0
                        "Reportes" -> 1
                        "Mis reportes" -> 2
                        "Detalle" -> 3
                        else -> 4
                    }
                    val direccion = if (orden(targetState) >= orden(initialState)) 1 else -1
                    (fadeIn(tween(240)) + slideInHorizontally(tween(240)) { direccion * it / 18 }) togetherWith
                        (fadeOut(tween(160)) + slideOutHorizontally(tween(200)) { -direccion * it / 22 })
                },
                label = "Cambio de pantalla"
            ) { pantallaActual ->
            when (pantallaActual) {
                "Inicio" -> PantallaInicio(
                    padding = padding,
                    recientes = reportes.take(3),
                    total = reportes.size,
                    perdidos = reportes.count { it.tipo == TipoReporte.PERDIDO && !it.recuperado },
                    encontrados = reportes.count { it.tipo == TipoReporte.ENCONTRADO && !it.recuperado },
                    alReportar = { abrirFormulario(null, it) },
                    alVerTodos = { pantalla = "Reportes" },
                    alEditar = { abrirFormulario(it) },
                    alVerDetalle = { abrirDetalle(it) },
                    alEliminar = { reporteAEliminar = it.id },
                    imagen = imagen
                )
                "Reportes", "Mis reportes" -> PantallaReportes(
                    padding = padding,
                    titulo = if (pantallaActual == "Reportes") "Reportes del campus" else "Mis reportes",
                    reportes = listaVisible,
                    busqueda = busqueda,
                    alBuscar = { busqueda = it },
                    filtro = filtro,
                    alFiltrar = { filtro = it },
                    alAgregar = { abrirFormulario(null) },
                    alEditar = { abrirFormulario(it) },
                    alVerDetalle = { abrirDetalle(it) },
                    alEliminar = { reporteAEliminar = it.id },
                    imagen = imagen
                )
                "Detalle" -> {
                    val reporte = reportes.firstOrNull { it.id == reporteSeleccionado }
                    if (reporte != null) PantallaDetalle(
                        padding, reporte, alVolver = { pantalla = origenDelDetalle },
                        alEditar = { abrirFormulario(reporte) },
                        alEliminar = { reporteAEliminar = reporte.id }, imagen = imagen
                    )
                }
                "Formulario" -> PantallaFormulario(
                    padding = padding,
                    datos = formulario,
                    editando = editando,
                    mostrarErrores = mostrarErrores,
                    alCambiar = { formulario = it },
                    alVolver = { pantalla = origenDelFormulario },
                    alElegirImagen = elegirImagen?.let { selector -> { selector { uri -> formulario = formulario.copy(imagenUri = uri) } } },
                    alElegirFecha = elegirFecha?.let { selector -> { selector(formulario.fecha) { fecha -> formulario = formulario.copy(fecha = fecha) } } },
                    alGuardar = {
                        mostrarErrores = true
                        if (formulario.nombre.isNotBlank() && formulario.ubicacion.isNotBlank() &&
                            formulario.descripcion.isNotBlank() && esFechaValida(formulario.fecha)) {
                            if (editando) {
                                actualizarReporte(reportes, reporteSeleccionado, formulario)
                            } else {
                                agregarReporte(reportes, formulario)
                            }
                            busqueda = ""
                            filtro = "Todos"
                            pantalla = "Reportes"
                        }
                    },
                    imagen = imagen
                )
            }
            }
        }

        val seleccionado = reportes.firstOrNull { it.id == reporteAEliminar }
        if (seleccionado != null) {
            AlertDialog(
                onDismissRequest = { reporteAEliminar = -1L },
                containerColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                icon = {
                    Surface(color = Color(0xFFFFEFEF), shape = RoundedCornerShape(16.dp)) {
                        Icono(IconoPoli.ELIMINAR, Modifier.padding(12.dp).size(24.dp), Color(0xFFB45353))
                    }
                },
                title = { Text("¿Eliminar reporte?", color = Tinta, fontWeight = FontWeight.ExtraBold) },
                text = { Text("Se eliminará el reporte de ${seleccionado.nombre}.", color = TextoSecundario) },
                confirmButton = {
                    TextButton(onClick = {
                        eliminarReporte(reportes, seleccionado.id)
                        reporteAEliminar = -1L
                        if (pantalla == "Detalle") pantalla = "Reportes"
                        mostrarToast("Reporte eliminado correctamente")
                    }) { Text("Eliminar", color = Color(0xFFB3261E)) }
                },
                dismissButton = { TextButton(onClick = { reporteAEliminar = -1L }) { Text("Cancelar") } }
            )
        }
    }
}
