# DogApp - Gestión de Citas Veterinarias

Aplicación móvil Android desarrollada en Kotlin para la administración y registro de pacientes y citas veterinarias. Proyecto desarrollado para el curso de DESARROLLO DE APLICACIONES PARA DISPOSITIVOS MÓVILES (Grupo 8).

## Integrantes
* Juan Jose Majin Montilla - 2040177
* James Calero Hurtado - 224361
* Jhoan Sebastian Fernandez Velasquez - 2222772
* Juan Pablo Charry Ramirez - 2040579

---

## Funcionalidades (Historias de Usuario)

* **HU 1.0 - Login y Autenticación:** Acceso seguro mediante autenticación biométrica (lector de huella digital).
* **HU 2.0 - Home Administrador de Citas:** Visualización reactiva del listado de turnos y citas médicas registradas en una lista interactiva.
* **HU 3.0 - Nueva Cita:** Formulario de registro con validaciones en tiempo real, `AutoCompleteTextView` para selección de razas consumidas desde la API externa de Dog CEO, y guardado local.
* **HU 4.0 - Detalle de Cita:** Consulta de la información completa del paciente, propietario y opción para cancelar/eliminar la cita.
* **HU 5.0 - Editar Cita:** Modificación y actualización de los datos de citas existentes.

---

## Arquitectura y Tecnologías

El proyecto sigue una arquitectura **MVVM (Model - View - ViewModel)** desacoplada con el patrón **Repository**:

* **Lenguaje:** Kotlin
* **Persistencia local:** Room (SQLite) con soporte para `LiveData`
* **Consumo de servicios web:** Retrofit 2 + Gson Converter (Dog CEO REST API)
* **Concurrencia:** Kotlin Coroutines (`viewModelScope`, `Dispatchers.IO`)
* **Navegación:** Jetpack Navigation Component (Single Activity Architecture)
* **Carga de imágenes:** Glide
* **Seguridad / Hardware:** AndroidX Biometric API
* **Interfaz de usuario:** Material Design Components, DataBinding y ViewBinding

---

## Requisitos de Ejecución

* **Android Studio:** Ladybug / Koala o superior
* **JDK:** 17 o 21
* **Min SDK:** 24 (Android 7.0)
* **Target SDK:** 36
