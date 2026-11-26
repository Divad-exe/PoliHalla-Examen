package com.david.holamundo

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import holamundokmp.shared.generated.resources.Res
import holamundokmp.shared.generated.resources.objeto_botella
import holamundokmp.shared.generated.resources.objeto_general
import holamundokmp.shared.generated.resources.objeto_llaves
import holamundokmp.shared.generated.resources.objeto_mochila
import org.jetbrains.compose.resources.painterResource

private val Tinta = Color(0xFF172440)
private val Gris = Color(0xFF586B85)

@Composable
fun ImagenPredeterminada(nombre: String, modifier: Modifier = Modifier) {
    val recurso = when {
        nombre.contains("mochila", ignoreCase = true) -> Res.drawable.objeto_mochila
        nombre.contains("llave", ignoreCase = true) -> Res.drawable.objeto_llaves
        nombre.contains("botella", ignoreCase = true) -> Res.drawable.objeto_botella
        else -> Res.drawable.objeto_general
    }
    Image(painterResource(recurso), contentDescription = "Imagen de $nombre", modifier = modifier, contentScale = ContentScale.Crop)
}

@Composable
fun PantallaInicio(
    padding: PaddingValues,
    recientes: List<Reporte>,
    alReportar: (TipoReporte) -> Unit,
    alVerTodos: () -> Unit,
    alEditar: (Reporte) -> Unit,
    alVerDetalle: (Reporte) -> Unit,
    alEliminar: (Reporte) -> Unit,
    imagen: @Composable (String, String, Modifier) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("PoliHalla", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(14.dp))
            Text("¿Perdiste algo?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Tinta)
            Text("Reporta una pérdida o un hallazgo en el campus de la EPN.", color = Gris)
        }
        item { Button(onClick = { alReportar(TipoReporte.PERDIDO) }, modifier = Modifier.fillMaxWidth()) { Text("Reportar pérdida") } }
        item { OutlinedButton(onClick = { alReportar(TipoReporte.ENCONTRADO) }, modifier = Modifier.fillMaxWidth()) { Text("Reportar hallazgo") } }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Avisos recientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Tinta)
                TextButton(onClick = alVerTodos) { Text("Ver todos") }
            }
        }
        if (recientes.isEmpty()) item { MensajeVacio("Todavía no hay reportes", "Crea un aviso para empezar.") }
        items(recientes, key = { it.id }) { reporte ->
            TarjetaReporte(reporte, { alEditar(reporte) }, { alVerDetalle(reporte) }, { alEliminar(reporte) }, imagen)
        }
    }
}

@Composable
fun PantallaReportes(
    padding: PaddingValues,
    titulo: String,
    reportes: List<Reporte>,
    busqueda: String,
    alBuscar: (String) -> Unit,
    filtro: String,
    alFiltrar: (String) -> Unit,
    alAgregar: () -> Unit,
    alEditar: (Reporte) -> Unit,
    alVerDetalle: (Reporte) -> Unit,
    alEliminar: (Reporte) -> Unit,
    imagen: @Composable (String, String, Modifier) -> Unit
) {
    Box(Modifier.fillMaxSize().padding(padding)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Tinta) }
            item {
                OutlinedTextField(
                    value = busqueda, onValueChange = alBuscar, modifier = Modifier.fillMaxWidth(),
                    label = { Text("Buscar objeto o lugar") }, singleLine = true, shape = RoundedCornerShape(14.dp)
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Todos", "Perdidos", "Encontrados").forEach { opcion ->
                        FilterChip(selected = filtro == opcion, onClick = { alFiltrar(opcion) }, label = { Text(opcion) })
                    }
                }
            }
            if (reportes.isEmpty()) item { MensajeVacio("No se encontraron reportes", "Prueba otra búsqueda o agrega un aviso.") }
            items(reportes, key = { it.id }) { reporte ->
                TarjetaReporte(reporte, { alEditar(reporte) }, { alVerDetalle(reporte) }, { alEliminar(reporte) }, imagen)
            }
        }
        FloatingActionButton(onClick = alAgregar, modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)) {
            Text("+", fontSize = 28.sp)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TarjetaReporte(
    reporte: Reporte,
    alEditar: () -> Unit,
    alVerDetalle: () -> Unit,
    alEliminar: () -> Unit,
    imagen: @Composable (String, String, Modifier) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize().combinedClickable(onClick = alEditar, onLongClick = alEliminar),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                imagen(reporte.imagenUri, reporte.nombre, Modifier.size(76.dp).clip(RoundedCornerShape(12.dp)))
                Column(Modifier.weight(1f)) {
                    Text(reporte.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = Tinta)
                    Text(reporte.ubicacion, color = Gris, style = MaterialTheme.typography.bodyMedium)
                    Text(reporte.fecha, color = Gris, style = MaterialTheme.typography.bodySmall)
                }
                EtiquetaEstado(reporte)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = alVerDetalle) { Text("Ver detalle") }
                TextButton(onClick = alEliminar) { Text("Eliminar", color = Color(0xFFB3261E)) }
            }
        }
    }
}

@Composable
private fun EtiquetaEstado(reporte: Reporte) {
    val texto = if (reporte.recuperado) "Recuperado" else reporte.tipo.etiqueta
    val fondo = if (reporte.recuperado) Color(0xFFDDEBFF) else if (reporte.tipo == TipoReporte.PERDIDO) Color(0xFFFFE7B3) else Color(0xFFCEF3E8)
    Surface(color = fondo, shape = RoundedCornerShape(50)) {
        Text(texto, modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp), style = MaterialTheme.typography.labelSmall, color = Tinta)
    }
}

@Composable
fun PantallaDetalle(
    padding: PaddingValues,
    reporte: Reporte,
    alVolver: () -> Unit,
    alEditar: () -> Unit,
    alEliminar: () -> Unit,
    imagen: @Composable (String, String, Modifier) -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = alVolver) { Text("‹ Volver") }
        Text("Detalle del reporte", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Tinta)
        imagen(reporte.imagenUri, reporte.nombre, Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(18.dp)))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(reporte.nombre, Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Tinta)
            EtiquetaEstado(reporte)
        }
        Text("Ubicación: ${reporte.ubicacion}", color = Gris)
        Text("Fecha: ${reporte.fecha}", color = Gris)
        Text("Descripción", fontWeight = FontWeight.Bold, color = Tinta)
        Text(reporte.descripcion, color = Tinta)
        Button(onClick = alEditar, modifier = Modifier.fillMaxWidth()) { Text("Editar reporte") }
        OutlinedButton(onClick = alEliminar, modifier = Modifier.fillMaxWidth()) { Text("Eliminar reporte") }
    }
}

@Composable
fun PantallaFormulario(
    padding: PaddingValues,
    datos: DatosFormulario,
    editando: Boolean,
    mostrarErrores: Boolean,
    alCambiar: (DatosFormulario) -> Unit,
    alVolver: () -> Unit,
    alElegirImagen: (() -> Unit)?,
    alElegirFecha: (() -> Unit)?,
    alGuardar: () -> Unit,
    imagen: @Composable (String, String, Modifier) -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = alVolver) { Text("‹ Volver") }
        Text(if (editando) "Editar reporte" else "Nuevo reporte", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Tinta)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TipoReporte.entries.forEach { tipo ->
                FilterChip(selected = datos.tipo == tipo, onClick = { alCambiar(datos.copy(tipo = tipo)) }, label = { Text(tipo.etiqueta) })
            }
        }
        imagen(datos.imagenUri, datos.nombre.ifBlank { "Objeto" }, Modifier.fillMaxWidth().height(135.dp).clip(RoundedCornerShape(16.dp)))
        if (alElegirImagen != null) OutlinedButton(onClick = alElegirImagen, modifier = Modifier.fillMaxWidth()) {
            Text(if (datos.imagenUri.isBlank()) "Adjuntar foto desde el dispositivo" else "Cambiar foto")
        }
        OutlinedTextField(
            value = datos.nombre, onValueChange = { alCambiar(datos.copy(nombre = it)) },
            label = { Text("Nombre del objeto *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            isError = mostrarErrores && datos.nombre.isBlank()
        )
        OutlinedTextField(
            value = datos.ubicacion, onValueChange = { alCambiar(datos.copy(ubicacion = it)) },
            label = { Text("Ubicación en el campus *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            isError = mostrarErrores && datos.ubicacion.isBlank()
        )
        OutlinedTextField(
            value = datos.fecha, onValueChange = { alCambiar(datos.copy(fecha = it.take(10))) },
            label = { Text("Fecha (dd/mm/aaaa) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            isError = mostrarErrores && !esFechaValida(datos.fecha)
        )
        if (alElegirFecha != null) TextButton(onClick = alElegirFecha) { Text("Elegir fecha en calendario") }
        OutlinedTextField(
            value = datos.descripcion, onValueChange = { alCambiar(datos.copy(descripcion = it)) },
            label = { Text("Descripción *") }, modifier = Modifier.fillMaxWidth(), minLines = 3,
            isError = mostrarErrores && datos.descripcion.isBlank()
        )
        if (editando) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) { Text("Recuperado", fontWeight = FontWeight.Bold); Text("Marca esta opción cuando el objeto regrese a su dueño.", style = MaterialTheme.typography.bodySmall) }
                Switch(checked = datos.recuperado, onCheckedChange = { alCambiar(datos.copy(recuperado = it)) })
            }
        }
        if (mostrarErrores && (datos.nombre.isBlank() || datos.ubicacion.isBlank() || datos.descripcion.isBlank() || !esFechaValida(datos.fecha))) {
            Text("Completa los campos obligatorios y escribe una fecha válida.", color = MaterialTheme.colorScheme.error)
        }
        Button(onClick = alGuardar, modifier = Modifier.fillMaxWidth()) { Text(if (editando) "Guardar cambios" else "Publicar reporte") }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun MensajeVacio(titulo: String, mensaje: String) {
    Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp)).padding(20.dp)) {
        Text(titulo, fontWeight = FontWeight.Bold, color = Tinta)
        Text(mensaje, color = Gris)
    }
}
