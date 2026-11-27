package com.david.holamundo

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import holamundokmp.shared.generated.resources.Res
import holamundokmp.shared.generated.resources.objeto_botella
import holamundokmp.shared.generated.resources.objeto_general
import holamundokmp.shared.generated.resources.objeto_llaves
import holamundokmp.shared.generated.resources.objeto_mochila
import org.jetbrains.compose.resources.painterResource

@Composable
fun ImagenPredeterminada(nombre: String, modifier: Modifier = Modifier) {
    val recurso = when {
        nombre.contains("mochila", ignoreCase = true) -> Res.drawable.objeto_mochila
        nombre.contains("llave", ignoreCase = true) -> Res.drawable.objeto_llaves
        nombre.contains("botella", ignoreCase = true) -> Res.drawable.objeto_botella
        else -> Res.drawable.objeto_general
    }
    Image(
        painter = painterResource(recurso),
        contentDescription = "Imagen de $nombre",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun CabeceraMarca() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(color = AzulPrincipal, shape = RoundedCornerShape(14.dp)) {
            Icono(IconoPoli.UBICACION, Modifier.padding(10.dp).size(22.dp), Color.White)
        }
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Text("PoliHalla", fontSize = 20.sp, lineHeight = 23.sp, fontWeight = FontWeight.ExtraBold, color = Tinta)
            Text("CAMPUS EPN", fontSize = 10.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.SemiBold, color = TextoSecundario)
        }
        Surface(color = Color.White, shape = RoundedCornerShape(50), border = BorderStroke(1.dp, Borde)) {
            Row(Modifier.padding(horizontal = 11.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icono(IconoPoli.CAMPUS, Modifier.size(16.dp), AzulPrincipal)
                Spacer(Modifier.width(5.dp))
                Text("EPN", color = AzulOscuro, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Portada(alReportar: () -> Unit) {
    Box(
        Modifier.fillMaxWidth().heightIn(min = 224.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF123A87), Color(0xFF2563EB), Color(0xFF4A8BF4))))
    ) {
        Canvas(Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            drawCircle(Color.White.copy(alpha = 0.07f), h * 0.52f, Offset(w * 0.96f, h * 0.07f))
            drawCircle(Color(0xFFB6DCFF).copy(alpha = 0.12f), h * 0.34f, Offset(w * 0.86f, h * 0.58f))
            drawCircle(Color.White.copy(alpha = 0.17f), h * 0.22f, Offset(w * 0.86f, h * 0.58f), style = Stroke(2.dp.toPx()))
            drawCircle(Color.White.copy(alpha = 0.90f), h * 0.12f, Offset(w * 0.83f, h * 0.48f), style = Stroke(4.dp.toPx()))
            drawLine(Color.White.copy(alpha = 0.90f), Offset(w * 0.88f, h * 0.58f), Offset(w * 0.95f, h * 0.73f), strokeWidth = 6.dp.toPx(), cap = StrokeCap.Round)
            drawRoundRect(Color.White.copy(alpha = 0.16f), Offset(w * 0.68f, h * 0.18f), Size(w * 0.12f, h * 0.14f), CornerRadius(8.dp.toPx()))
            drawCircle(Color(0xFFBCE3FF), 4.dp.toPx(), Offset(w * 0.74f, h * 0.25f))
            drawCircle(Color.White.copy(alpha = 0.8f), 2.5.dp.toPx(), Offset(w * 0.90f, h * 0.19f))
            drawCircle(Color.White.copy(alpha = 0.55f), 2.dp.toPx(), Offset(w * 0.69f, h * 0.77f))
        }
        Column(Modifier.fillMaxWidth(0.72f).padding(start = 20.dp, top = 19.dp, bottom = 16.dp)) {
            Text("JUNTOS EN EL CAMPUS", fontSize = 9.sp, letterSpacing = 1.2.sp, color = Color(0xFFC6DFFF), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Lo que se pierde,\npuede volver.", fontSize = 23.sp, lineHeight = 27.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(7.dp))
            Text("Ayudemos a que cada objeto encuentre su camino.", fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFFDFEAFF))
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = alReportar,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = AzulOscuro),
                contentPadding = PaddingValues(horizontal = 14.dp),
                modifier = Modifier.height(39.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icono(IconoPoli.MAS, Modifier.size(17.dp), AzulPrincipal)
                Spacer(Modifier.width(5.dp))
                Text("Reportar pérdida", fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }
        }
    }
}

@Composable
private fun Resumen(total: Int, perdidos: Int, encontrados: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        DatoResumen("Avisos", total, Modifier.weight(1f))
        DatoResumen("Perdidos", perdidos, Modifier.weight(1f))
        DatoResumen("Hallados", encontrados, Modifier.weight(1f))
    }
}

@Composable
private fun DatoResumen(etiqueta: String, valor: Int, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Color.White, shape = RoundedCornerShape(17.dp), border = BorderStroke(1.dp, Borde)) {
        Column(Modifier.padding(horizontal = 13.dp, vertical = 12.dp)) {
            AnimatedContent(targetState = valor, label = "Cantidad de reportes") { cantidad ->
                Text(cantidad.toString().padStart(2, '0'), fontSize = 20.sp, lineHeight = 23.sp, fontWeight = FontWeight.ExtraBold, color = AzulOscuro)
            }
            Text(etiqueta, fontSize = 11.sp, color = TextoSecundario, maxLines = 1)
        }
    }
}

@Composable
fun PantallaInicio(
    padding: PaddingValues,
    recientes: List<Reporte>,
    total: Int,
    perdidos: Int,
    encontrados: Int,
    alReportar: (TipoReporte) -> Unit,
    alVerTodos: () -> Unit,
    alEditar: (Reporte) -> Unit,
    alVerDetalle: (Reporte) -> Unit,
    alEliminar: (Reporte) -> Unit,
    imagen: @Composable (String, String, Modifier) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { CabeceraMarca() }
        item { Portada { alReportar(TipoReporte.PERDIDO) } }
        item {
            Surface(
                onClick = { alReportar(TipoReporte.ENCONTRADO) },
                shape = RoundedCornerShape(17.dp), color = Color.White, border = BorderStroke(1.dp, Borde)
            ) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = AzulClaro, shape = RoundedCornerShape(12.dp)) {
                        Icono(IconoPoli.VERIFICADO, Modifier.padding(9.dp).size(20.dp), AzulPrincipal)
                    }
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Encontré un objeto", color = Tinta, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Publica un hallazgo en el campus", color = TextoSecundario, fontSize = 11.sp)
                    }
                    Icono(IconoPoli.FLECHA, Modifier.size(19.dp), AzulPrincipal)
                }
            }
        }
        item { Resumen(total, perdidos, encontrados) }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Avisos recientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = Tinta)
                    Text("Últimos reportes del campus", fontSize = 12.sp, color = TextoSecundario)
                }
                TextButton(onClick = alVerTodos, contentPadding = PaddingValues(horizontal = 4.dp)) {
                    Text("Ver todos", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(Modifier.width(3.dp))
                    Icono(IconoPoli.FLECHA, Modifier.size(16.dp), AzulPrincipal)
                }
            }
        }
        if (recientes.isEmpty()) item { MensajeVacio("Todavía no hay reportes", "Crea un aviso para empezar.") }
        items(recientes, key = { it.id }) { reporte ->
            TarjetaReporte(reporte, { alEditar(reporte) }, { alVerDetalle(reporte) }, { alEliminar(reporte) }, imagen, Modifier.animateItem())
        }
    }
}

@Composable
private fun Encabezado(titulo: String, descripcion: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("POLIHALLA  /  CAMPUS EPN", fontSize = 10.sp, letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
        Text(titulo, fontSize = 27.sp, lineHeight = 32.sp, fontWeight = FontWeight.ExtraBold, color = Tinta)
        Text(descripcion, fontSize = 13.sp, lineHeight = 19.sp, color = TextoSecundario)
    }
}

@Composable
private fun OpcionFiltro(texto: String, seleccionada: Boolean, alSeleccionar: () -> Unit) {
    val fondo by animateColorAsState(if (seleccionada) AzulPrincipal else Color.White, tween(200), label = "Color del filtro")
    val tinta by animateColorAsState(if (seleccionada) Color.White else TextoSecundario, tween(200), label = "Texto del filtro")
    Surface(
        onClick = alSeleccionar,
        modifier = Modifier.semantics { selected = seleccionada },
        color = fondo,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, if (seleccionada) AzulPrincipal else Borde)
    ) {
        Text(texto, Modifier.padding(horizontal = 17.dp, vertical = 10.dp), color = tinta, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
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
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Encabezado(titulo, "Cada aviso es una oportunidad de ayudar.") }
            item {
                OutlinedTextField(
                    value = busqueda, onValueChange = alBuscar, modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar objeto o lugar", fontSize = 14.sp) },
                    leadingIcon = { Icono(IconoPoli.BUSCAR, Modifier.size(20.dp), TextoSecundario) },
                    singleLine = true, shape = RoundedCornerShape(17.dp),
                    colors = coloresCampo()
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Todos", "Perdidos", "Encontrados").forEach { opcion ->
                        OpcionFiltro(opcion, filtro == opcion) { alFiltrar(opcion) }
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("EXPLORAR AVISOS", Modifier.weight(1f), fontSize = 10.sp, letterSpacing = 1.2.sp, color = TextoSecundario, fontWeight = FontWeight.Bold)
                    AnimatedContent(targetState = reportes.size, label = "Resultados") { cantidad ->
                        Text(cantidad.toString() + if (cantidad == 1) " resultado" else " resultados", fontSize = 11.sp, color = TextoSecundario)
                    }
                }
            }
            if (reportes.isEmpty()) item { MensajeVacio("No se encontraron reportes", "Prueba otra búsqueda o agrega un aviso.") }
            items(reportes, key = { it.id }) { reporte ->
                TarjetaReporte(reporte, { alEditar(reporte) }, { alVerDetalle(reporte) }, { alEliminar(reporte) }, imagen, Modifier.animateItem())
            }
        }
        ExtendedFloatingActionButton(
            onClick = alAgregar,
            modifier = Modifier.align(Alignment.BottomEnd).padding(18.dp),
            containerColor = AzulPrincipal, contentColor = Color.White,
            shape = RoundedCornerShape(18.dp),
            icon = { Icono(IconoPoli.MAS, Modifier.size(20.dp), Color.White) },
            text = { Text("Nuevo reporte", fontWeight = FontWeight.Bold) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TarjetaReporte(
    reporte: Reporte,
    alEditar: () -> Unit,
    alVerDetalle: () -> Unit,
    alEliminar: () -> Unit,
    imagen: @Composable (String, String, Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().animateContentSize()
            .combinedClickable(onClick = alEditar, onLongClick = alEliminar),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Borde),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(21.dp)
    ) {
        Column(Modifier.padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                imagen(reporte.imagenUri, reporte.nombre, Modifier.size(82.dp).clip(RoundedCornerShape(15.dp)))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    EtiquetaEstado(reporte)
                    Text(reporte.nombre, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, lineHeight = 19.sp, color = Tinta, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icono(IconoPoli.UBICACION, Modifier.size(13.dp), TextoSecundario)
                        Spacer(Modifier.width(4.dp))
                        Text(reporte.ubicacion, color = TextoSecundario, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
            Spacer(Modifier.height(11.dp))
            HorizontalDivider(color = Borde)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(reporte.fecha, Modifier.weight(1f), color = TextoSecundario, fontSize = 11.sp)
                TextButton(onClick = alVerDetalle, contentPadding = PaddingValues(horizontal = 8.dp)) {
                    Text("Ver detalle", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                TextButton(onClick = alEliminar, contentPadding = PaddingValues(horizontal = 8.dp)) {
                    Text("Eliminar", color = Color(0xFFB45353), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun EtiquetaEstado(reporte: Reporte) {
    val texto = if (reporte.recuperado) "Recuperado" else reporte.tipo.etiqueta
    val fondo = if (reporte.recuperado) Color(0xFFE9F1FF) else if (reporte.tipo == TipoReporte.PERDIDO) Color(0xFFFFF2D7) else Color(0xFFE2F6ED)
    val tinta = if (reporte.recuperado) Color(0xFF2559B4) else if (reporte.tipo == TipoReporte.PERDIDO) Color(0xFF8A5A12) else Color(0xFF237251)
    Surface(color = fondo, shape = RoundedCornerShape(50)) {
        Text(texto.uppercase(), modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp), fontSize = 9.sp, letterSpacing = 0.5.sp, fontWeight = FontWeight.ExtraBold, color = tinta)
    }
}

@Composable
private fun BotonVolver(alVolver: () -> Unit) {
    TextButton(onClick = alVolver, contentPadding = PaddingValues(start = 0.dp, end = 10.dp)) {
        Icono(IconoPoli.VOLVER, Modifier.size(20.dp), AzulPrincipal)
        Spacer(Modifier.width(7.dp))
        Text("Volver", color = AzulPrincipal, fontWeight = FontWeight.Bold)
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
        Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        BotonVolver(alVolver)
        Encabezado("Detalle del reporte", "Conoce la información de este aviso.")
        Box(Modifier.fillMaxWidth().height(225.dp).clip(RoundedCornerShape(24.dp)).background(AzulClaro)) {
            imagen(reporte.imagenUri, reporte.nombre, Modifier.fillMaxSize())
            Box(Modifier.align(Alignment.TopEnd).padding(14.dp)) { EtiquetaEstado(reporte) }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("REPORTE #" + reporte.id.toString().padStart(2, '0'), fontSize = 10.sp, letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
            Text(reporte.nombre, fontSize = 26.sp, lineHeight = 30.sp, fontWeight = FontWeight.ExtraBold, color = Tinta)
        }
        Surface(color = Color.White, shape = RoundedCornerShape(19.dp), border = BorderStroke(1.dp, Borde)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
                DatoDetalle(IconoPoli.UBICACION, "Ubicación", reporte.ubicacion)
                HorizontalDivider(color = Borde)
                DatoDetalle(IconoPoli.FECHA, "Fecha", reporte.fecha)
            }
        }
        Text("Descripción", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Tinta)
        Text(reporte.descripcion, color = TextoSecundario, fontSize = 14.sp, lineHeight = 21.sp)
        Button(onClick = alEditar, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(15.dp)) {
            Icono(IconoPoli.EDITAR, Modifier.size(18.dp), Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Editar reporte", fontWeight = FontWeight.Bold)
        }
        OutlinedButton(onClick = alEliminar, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(15.dp), border = BorderStroke(1.dp, Borde)) {
            Icono(IconoPoli.ELIMINAR, Modifier.size(18.dp), Color(0xFFB45353))
            Spacer(Modifier.width(8.dp))
            Text("Eliminar reporte", color = Color(0xFFB45353), fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun DatoDetalle(icono: IconoPoli, titulo: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(color = AzulClaro, shape = RoundedCornerShape(12.dp)) {
            Icono(icono, Modifier.padding(9.dp).size(18.dp), AzulPrincipal)
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(titulo.uppercase(), fontSize = 9.sp, letterSpacing = 0.8.sp, fontWeight = FontWeight.Bold, color = TextoSecundario)
            Text(valor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Tinta)
        }
    }
}

@Composable
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AzulPrincipal,
    unfocusedBorderColor = Borde,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedLabelColor = AzulPrincipal,
    cursorColor = AzulPrincipal
)

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
        Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        BotonVolver(alVolver)
        Encabezado(if (editando) "Editar reporte" else "Nuevo reporte", "Cuéntanos un poco más sobre el objeto.")
        Text("TIPO DE REPORTE", fontSize = 10.sp, letterSpacing = 1.1.sp, color = TextoSecundario, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TipoReporte.entries.forEach { tipo ->
                OpcionFiltro(tipo.etiqueta, datos.tipo == tipo) { alCambiar(datos.copy(tipo = tipo)) }
            }
        }
        Surface(color = Color.White, shape = RoundedCornerShape(19.dp), border = BorderStroke(1.dp, Borde)) {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                imagen(datos.imagenUri, datos.nombre.ifBlank { "Objeto" }, Modifier.size(82.dp).clip(RoundedCornerShape(14.dp)))
                Spacer(Modifier.width(13.dp))
                Column(Modifier.weight(1f)) {
                    Text("Foto del objeto", fontWeight = FontWeight.ExtraBold, color = Tinta, fontSize = 14.sp)
                    Text("Una imagen ayuda a identificarlo", color = TextoSecundario, fontSize = 11.sp, lineHeight = 15.sp)
                    if (alElegirImagen != null) {
                        TextButton(onClick = alElegirImagen, contentPadding = PaddingValues(0.dp)) {
                            Icono(IconoPoli.FOTO, Modifier.size(16.dp), AzulPrincipal)
                            Spacer(Modifier.width(5.dp))
                            Text(if (datos.imagenUri.isBlank()) "Adjuntar foto" else "Cambiar foto", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        Text("INFORMACIÓN DEL OBJETO", fontSize = 10.sp, letterSpacing = 1.1.sp, color = TextoSecundario, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = datos.nombre, onValueChange = { alCambiar(datos.copy(nombre = it)) },
            label = { Text("Nombre del objeto *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(16.dp), colors = coloresCampo(),
            isError = mostrarErrores && datos.nombre.isBlank()
        )
        OutlinedTextField(
            value = datos.ubicacion, onValueChange = { alCambiar(datos.copy(ubicacion = it)) },
            label = { Text("Ubicación en el campus *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            leadingIcon = { Icono(IconoPoli.UBICACION, Modifier.size(20.dp), TextoSecundario) },
            shape = RoundedCornerShape(16.dp), colors = coloresCampo(),
            isError = mostrarErrores && datos.ubicacion.isBlank()
        )
        OutlinedTextField(
            value = datos.fecha, onValueChange = { alCambiar(datos.copy(fecha = it.take(10))) },
            label = { Text("Fecha (dd/mm/aaaa) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            leadingIcon = { Icono(IconoPoli.FECHA, Modifier.size(20.dp), TextoSecundario) },
            shape = RoundedCornerShape(16.dp), colors = coloresCampo(),
            isError = mostrarErrores && !esFechaValida(datos.fecha)
        )
        if (alElegirFecha != null) TextButton(onClick = alElegirFecha, contentPadding = PaddingValues(horizontal = 2.dp)) {
            Text("Elegir fecha en calendario", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        OutlinedTextField(
            value = datos.descripcion, onValueChange = { alCambiar(datos.copy(descripcion = it)) },
            label = { Text("Descripción *") }, modifier = Modifier.fillMaxWidth(), minLines = 3,
            shape = RoundedCornerShape(16.dp), colors = coloresCampo(),
            isError = mostrarErrores && datos.descripcion.isBlank()
        )
        if (editando) {
            Surface(color = Color.White, shape = RoundedCornerShape(17.dp), border = BorderStroke(1.dp, Borde)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.weight(1f)) {
                        Text("Objeto recuperado", fontWeight = FontWeight.Bold, color = Tinta)
                        Text("Actívalo cuando regrese a su dueño.", color = TextoSecundario, fontSize = 12.sp)
                    }
                    Switch(checked = datos.recuperado, onCheckedChange = { alCambiar(datos.copy(recuperado = it)) })
                }
            }
        }
        AnimatedVisibility(
            visible = mostrarErrores && (datos.nombre.isBlank() || datos.ubicacion.isBlank() || datos.descripcion.isBlank() || !esFechaValida(datos.fecha)),
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(140))
        ) {
            Text("Completa los campos obligatorios y escribe una fecha válida.", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }
        Button(onClick = alGuardar, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(15.dp)) {
            Text(if (editando) "Guardar cambios" else "Publicar reporte", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Icono(IconoPoli.FLECHA, Modifier.size(18.dp), Color.White)
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun MensajeVacio(titulo: String, mensaje: String) {
    Surface(color = Color.White, shape = RoundedCornerShape(20.dp), border = BorderStroke(1.dp, Borde)) {
        Column(Modifier.fillMaxWidth().padding(22.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Surface(color = AzulClaro, shape = RoundedCornerShape(13.dp)) {
                Icono(IconoPoli.BUSCAR, Modifier.padding(10.dp).size(20.dp), AzulPrincipal)
            }
            Spacer(Modifier.height(2.dp))
            Text(titulo, fontWeight = FontWeight.ExtraBold, color = Tinta)
            Text(mensaje, color = TextoSecundario, fontSize = 13.sp, lineHeight = 19.sp)
        }
    }
}
