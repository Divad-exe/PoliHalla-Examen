# PoliHalla — Examen

Aplicación de ejemplo para registrar objetos perdidos y encontrados en el campus. El proyecto usa Kotlin Multiplatform y Compose Multiplatform. Se creó a partir de la estructura de HolaMundoKMP.

## Abrir en Android Studio

1. Abre en Android Studio la carpeta del proyecto que contiene `settings.gradle.kts`, mediante **File > Open**.
2. Espera a que Gradle termine de sincronizar. El proyecto está configurado con el Android SDK 37 y JDK 21; Android Studio indicará si falta algún componente.
3. Selecciona la configuración `androidApp` y un dispositivo o emulador Android.
4. Presiona **Run**.

## Cómo probar el CRUD

1. En **Reportes**, consulta los tres registros de ejemplo. Busca por nombre o ubicación y usa los filtros de tipo.
2. Presiona **Nuevo reporte**, completa nombre, ubicación, fecha y descripción, y publica el reporte. Puedes elegir una fotografía del dispositivo. Si no la eliges se muestra una ilustración de ejemplo.
3. Toca una tarjeta para abrir el formulario de edición. Modifica sus datos, activa **Recuperado** y guarda.
4. Usa **Ver detalle** para consultar un aviso. En **Reportes** o **Mis reportes**, mantén presionada una tarjeta o pulsa **Eliminar**. Confirma para ver el Toast nativo de Android. Si cancelas, el reporte se conserva.

Los datos viven en memoria. Al cerrar la aplicación por completo y volverla a iniciar se cargan nuevamente los registros de ejemplo. Las vistas de Reportes y Mis reportes comparten los datos locales de esta demostración; no se ha implementado autenticación ni sincronización entre usuarios. La selección de fotos, el calendario y el Toast se conectan a Android. La interfaz y los modelos se encuentran en el módulo compartido de KMP.

## Diseño y animaciones

- Una portada con ilustración, acciones principales y contadores de reportes, junto con tarjetas, campos y navegación en tonos azules.
- Transiciones de desvanecimiento y desplazamiento entre pantallas mediante `AnimatedContent`.
- Animación al agregar, quitar o reorganizar tarjetas con `animateItem`; los filtros y contadores también responden visualmente a los cambios.
- El diálogo, los campos, los botones, la barra de navegación y el interruptor usan componentes de Compose Material 3. Los iconos se dibujan con Compose sin añadir bibliotecas externas de interfaz.

## Evidencias para el taller

Muestra el listado, la creación, la edición, la confirmación de eliminación y el Toast. En Android Studio, abre **Tools > Layout Inspector**, selecciona el proceso de PoliHalla y registra la jerarquía que muestra el inspector. Comprueba también la rotación con un formulario parcialmente completado.
