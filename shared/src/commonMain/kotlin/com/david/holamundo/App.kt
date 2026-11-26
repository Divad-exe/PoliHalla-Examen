package com.david.holamundo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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

private val Azul = Color(0xFF1556C5)

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

    MaterialTheme(colorScheme = lightColorScheme(primary = Azul, background = Color(0xFFF7F9FD), surface = Color.White)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFF7F9FD),
            bottomBar = {
                if (pantalla in listOf("Inicio", "Reportes", "Mis reportes")) {
                    NavigationBar(containerColor = Color.White) {
                        listOf("Inicio" to "⌂", "Reportes" to "☰", "Mis reportes" to "●").forEach { (destino, simbolo) ->
                            NavigationBarItem(
                                selected = pantalla == destino,
                                onClick = { pantalla = destino },
                                icon = { Text(simbolo) },
                                label = { Text(destino) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            when (pantalla) {
                "Inicio" -> PantallaInicio(
                    padding = padding,
                    recientes = reportes.take(3),
                    alReportar = { abrirFormulario(null, it) },
                    alVerTodos = { pantalla = "Reportes" },
                    alEditar = { abrirFormulario(it) },
                    alVerDetalle = { abrirDetalle(it) },
                    alEliminar = { reporteAEliminar = it.id },
                    imagen = imagen
                )
                "Reportes", "Mis reportes" -> PantallaReportes(
                    padding = padding,
                    titulo = if (pantalla == "Reportes") "Reportes del campus" else "Mis reportes",
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

        val seleccionado = reportes.firstOrNull { it.id == reporteAEliminar }
        if (seleccionado != null) {
            AlertDialog(
                onDismissRequest = { reporteAEliminar = -1L },
                title = { Text("¿Eliminar reporte?") },
                text = { Text("Se eliminará el reporte de ${seleccionado.nombre}.") },
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
