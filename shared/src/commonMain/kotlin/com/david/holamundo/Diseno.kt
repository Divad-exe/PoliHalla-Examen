package com.david.holamundo

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

internal val AzulPrincipal = Color(0xFF2563EB)
internal val AzulOscuro = Color(0xFF163879)
internal val Tinta = Color(0xFF152B4B)
internal val TextoSecundario = Color(0xFF6C7D96)
internal val Fondo = Color(0xFFF4F7FC)
internal val Borde = Color(0xFFE3EAF4)
internal val AzulClaro = Color(0xFFE9F1FF)

internal enum class IconoPoli {
    INICIO, LISTA, USUARIO, BUSCAR, UBICACION, FECHA, FLECHA, VOLVER,
    MAS, FOTO, EDITAR, ELIMINAR, VERIFICADO, CAMPUS
}

// Iconos propios, dibujados con Compose para no depender de una biblioteca de UI externa.
@Composable
internal fun Icono(
    tipo: IconoPoli,
    modifier: Modifier = Modifier,
    color: Color = AzulPrincipal
) {
    Canvas(modifier) {
        val u = size.minDimension / 24f
        val dx = (size.width - 24f * u) / 2f
        val dy = (size.height - 24f * u) / 2f
        fun p(x: Float, y: Float) = Offset(dx + x * u, dy + y * u)
        fun linea(x1: Float, y1: Float, x2: Float, y2: Float) {
            drawLine(color, p(x1, y1), p(x2, y2), strokeWidth = 1.8f * u, cap = StrokeCap.Round)
        }
        fun marco(x: Float, y: Float, w: Float, h: Float, radio: Float = 2.5f) {
            drawRoundRect(
                color = color, topLeft = p(x, y), size = Size(w * u, h * u),
                cornerRadius = CornerRadius(radio * u), style = Stroke(1.8f * u)
            )
        }
        fun trazo(vararg puntos: Pair<Float, Float>, cerrar: Boolean = false) {
            val path = Path().apply {
                puntos.forEachIndexed { i, punto ->
                    val posicion = p(punto.first, punto.second)
                    if (i == 0) moveTo(posicion.x, posicion.y) else lineTo(posicion.x, posicion.y)
                }
                if (cerrar) close()
            }
            drawPath(path, color, style = Stroke(1.8f * u, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
        when (tipo) {
            IconoPoli.INICIO -> {
                trazo(3f to 10f, 12f to 3f, 21f to 10f)
                trazo(5.5f to 9f, 5.5f to 20f, 18.5f to 20f, 18.5f to 9f)
                trazo(9.5f to 20f, 9.5f to 14f, 14.5f to 14f, 14.5f to 20f)
            }
            IconoPoli.LISTA -> {
                marco(4f, 3f, 16f, 18f)
                listOf(8f, 12f, 16f).forEach { y -> linea(9f, y, 17f, y); drawCircle(color, 0.9f * u, p(7f, y)) }
            }
            IconoPoli.USUARIO -> {
                drawCircle(color, 3.5f * u, p(12f, 8f), style = Stroke(1.8f * u))
                val path = Path().apply {
                    moveTo(p(4.5f, 20f).x, p(4.5f, 20f).y)
                    quadraticTo(p(5f, 14f).x, p(5f, 14f).y, p(12f, 14f).x, p(12f, 14f).y)
                    quadraticTo(p(19f, 14f).x, p(19f, 14f).y, p(19.5f, 20f).x, p(19.5f, 20f).y)
                }
                drawPath(path, color, style = Stroke(1.8f * u, cap = StrokeCap.Round))
            }
            IconoPoli.BUSCAR -> {
                drawCircle(color, 6.4f * u, p(10f, 10f), style = Stroke(1.8f * u))
                linea(14.8f, 14.8f, 20f, 20f)
            }
            IconoPoli.UBICACION -> {
                val path = Path().apply {
                    moveTo(p(12f, 21f).x, p(12f, 21f).y)
                    cubicTo(p(10f, 18f).x, p(10f, 18f).y, p(5f, 13.5f).x, p(5f, 13.5f).y, p(5f, 9.5f).x, p(5f, 9.5f).y)
                    cubicTo(p(5f, 5f).x, p(5f, 5f).y, p(8f, 3f).x, p(8f, 3f).y, p(12f, 3f).x, p(12f, 3f).y)
                    cubicTo(p(16f, 3f).x, p(16f, 3f).y, p(19f, 5f).x, p(19f, 5f).y, p(19f, 9.5f).x, p(19f, 9.5f).y)
                    cubicTo(p(19f, 13.5f).x, p(19f, 13.5f).y, p(14f, 18f).x, p(14f, 18f).y, p(12f, 21f).x, p(12f, 21f).y)
                    close()
                }
                drawPath(path, color, style = Stroke(1.8f * u))
                drawCircle(color, 2.2f * u, p(12f, 9.5f), style = Stroke(1.8f * u))
            }
            IconoPoli.FECHA -> {
                marco(4f, 5f, 16f, 15f)
                linea(4f, 9f, 20f, 9f); linea(8f, 3f, 8f, 7f); linea(16f, 3f, 16f, 7f)
                drawCircle(color, 1.2f * u, p(9f, 13f)); drawCircle(color, 1.2f * u, p(15f, 13f))
            }
            IconoPoli.FLECHA -> { linea(4f, 12f, 20f, 12f); trazo(14f to 6f, 20f to 12f, 14f to 18f) }
            IconoPoli.VOLVER -> { linea(20f, 12f, 4f, 12f); trazo(10f to 6f, 4f to 12f, 10f to 18f) }
            IconoPoli.MAS -> { linea(12f, 5f, 12f, 19f); linea(5f, 12f, 19f, 12f) }
            IconoPoli.FOTO -> {
                marco(3f, 6f, 18f, 14f)
                trazo(8f to 6f, 9.5f to 3.8f, 14.5f to 3.8f, 16f to 6f)
                drawCircle(color, 3.4f * u, p(12f, 13f), style = Stroke(1.8f * u))
            }
            IconoPoli.EDITAR -> {
                trazo(4f to 20f, 5f to 15f, 16.5f to 3.5f, 20.5f to 7.5f, 9f to 19f, 4f to 20f)
                linea(14.5f, 5.5f, 18.5f, 9.5f)
            }
            IconoPoli.ELIMINAR -> {
                linea(4f, 7f, 20f, 7f); trazo(9f to 7f, 9f to 4f, 15f to 4f, 15f to 7f)
                trazo(6f to 7f, 7f to 20f, 17f to 20f, 18f to 7f)
                linea(10f, 11f, 10f, 16f); linea(14f, 11f, 14f, 16f)
            }
            IconoPoli.VERIFICADO -> {
                drawCircle(color, 8.5f * u, p(12f, 12f), style = Stroke(1.8f * u))
                trazo(8f to 12f, 11f to 15f, 16f to 9f)
            }
            IconoPoli.CAMPUS -> {
                trazo(3f to 9f, 12f to 4f, 21f to 9f, 3f to 9f)
                linea(4f, 20f, 20f, 20f); linea(3f, 22f, 21f, 22f)
                listOf(7f, 12f, 17f).forEach { x -> linea(x, 10f, x, 18f) }
            }
        }
    }
}
