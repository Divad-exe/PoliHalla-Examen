package com.david.holamundo

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReporteTest {
    @Test
    fun permiteCrearConsultarEditarYEliminarEnMemoria() {
        val reportes = reportesIniciales().toMutableList()
        val datos = DatosFormulario(
            nombre = "  Cuaderno  ", ubicacion = "Biblioteca", fecha = "26/11/2026",
            descripcion = "  Cuaderno azul  ", tipo = TipoReporte.ENCONTRADO,
            imagenUri = "content://imagenes/1"
        )

        val creado = agregarReporte(reportes, datos)
        assertEquals(4, reportes.size)
        assertEquals("Cuaderno", reportes.first().nombre)
        assertEquals("content://imagenes/1", reportes.first().imagenUri)
        assertEquals(creado, reportes.firstOrNull { it.id == creado.id })

        assertTrue(actualizarReporte(reportes, creado.id, datos.copy(nombre = "Cuaderno verde", recuperado = true)))
        assertEquals("Cuaderno verde", reportes.first().nombre)
        assertTrue(reportes.first().recuperado)
        assertEquals(creado.id, reportes.first().id)

        assertTrue(eliminarReporte(reportes, creado.id))
        assertEquals(3, reportes.size)
        assertFalse(reportes.any { it.id == creado.id })
    }

    @Test
    fun ignorarCambiosSobreUnReporteInexistente() {
        val reportes = reportesIniciales().toMutableList()
        val originales = reportes.toList()
        assertFalse(actualizarReporte(reportes, 999L, DatosFormulario()))
        assertFalse(eliminarReporte(reportes, 999L))
        assertEquals(originales, reportes)
    }

    @Test
    fun aceptaFechasRealesYAnioBisiesto() {
        assertTrue(esFechaValida("16/11/2026"))
        assertTrue(esFechaValida("29/02/2024"))
    }

    @Test
    fun rechazaFechasInvalidasYFormatoIncorrecto() {
        assertFalse(esFechaValida("29/02/2025"))
        assertFalse(esFechaValida("31/04/2026"))
        assertFalse(esFechaValida("1/1/2026"))
        assertFalse(esFechaValida(""))
    }
}
