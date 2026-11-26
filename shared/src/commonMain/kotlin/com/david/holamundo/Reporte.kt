package com.david.holamundo

data class Reporte(
    val id: Long,
    val nombre: String,
    val ubicacion: String,
    val fecha: String,
    val descripcion: String,
    val tipo: TipoReporte,
    val recuperado: Boolean = false,
    val imagenUri: String = ""
)

enum class TipoReporte(val etiqueta: String) {
    PERDIDO("Perdido"),
    ENCONTRADO("Encontrado")
}

data class DatosFormulario(
    val nombre: String = "",
    val ubicacion: String = "",
    val fecha: String = "",
    val descripcion: String = "",
    val tipo: TipoReporte = TipoReporte.PERDIDO,
    val recuperado: Boolean = false,
    val imagenUri: String = ""
)

fun reportesIniciales(): List<Reporte> = listOf(
    Reporte(1, "Mochila negra", "Biblioteca", "16/11/2026", "Mochila con cierre gris y bolsillo frontal.", TipoReporte.PERDIDO),
    Reporte(2, "Llaves", "Bloque de Sistemas", "15/11/2026", "Llavero con dos llaves y una cinta azul.", TipoReporte.ENCONTRADO),
    Reporte(3, "Botella azul", "Cafetería", "14/11/2026", "Botella reutilizable de color azul.", TipoReporte.PERDIDO)
)

private fun crearReporte(id: Long, datos: DatosFormulario): Reporte = Reporte(
    id = id,
    nombre = datos.nombre.trim(),
    ubicacion = datos.ubicacion.trim(),
    fecha = datos.fecha,
    descripcion = datos.descripcion.trim(),
    tipo = datos.tipo,
    recuperado = datos.recuperado,
    imagenUri = datos.imagenUri
)

fun agregarReporte(reportes: MutableList<Reporte>, datos: DatosFormulario): Reporte {
    val id = (reportes.maxOfOrNull { it.id } ?: 0L) + 1L
    val reporte = crearReporte(id, datos)
    reportes.add(0, reporte)
    return reporte
}

fun actualizarReporte(reportes: MutableList<Reporte>, id: Long, datos: DatosFormulario): Boolean {
    val indice = reportes.indexOfFirst { it.id == id }
    if (indice < 0) return false
    reportes[indice] = crearReporte(id, datos)
    return true
}

fun eliminarReporte(reportes: MutableList<Reporte>, id: Long): Boolean {
    val indice = reportes.indexOfFirst { it.id == id }
    if (indice < 0) return false
    reportes.removeAt(indice)
    return true
}

fun esFechaValida(texto: String): Boolean {
    val partes = texto.split("/")
    if (partes.size != 3 || partes[0].length != 2 || partes[1].length != 2 || partes[2].length != 4) return false
    val dia = partes[0].toIntOrNull() ?: return false
    val mes = partes[1].toIntOrNull() ?: return false
    val anio = partes[2].toIntOrNull() ?: return false
    if (anio !in 1900..9999 || mes !in 1..12) return false
    val bisiesto = anio % 4 == 0 && (anio % 100 != 0 || anio % 400 == 0)
    val diasDelMes = when (mes) {
        2 -> if (bisiesto) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
    return dia in 1..diasDelMes
}
