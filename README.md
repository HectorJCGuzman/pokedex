# Pokédex App - Android

Una aplicación de Pokédex nativa para Android construida con Kotlin y el kit de herramientas de desarrollo de UI moderno de Android.

<p align="center">
    <img src="https://github.com/HectorJCGuzman/pokedex/blob/main/assets/ListPreview_1.png" width="300"/>
    <img src="https://github.com/HectorJCGuzman/pokedex/blob/main/assets/DetailsPreview_1.png" width="300"/>
    <img src="https://github.com/HectorJCGuzman/pokedex/blob/main/assets/Encounter_1.png" width="300"/>
<p>

## Características

-   **Lista de Pokémon:** Navega a través de una lista de Pokémon con scroll limitado solo por la cantidad de pokemones en la API de PokeAPI.
-   **Detalles del Pokémon:** Consulta información detallada como estadísticas, tipos, altura y peso.
-   **Gestión de Favoritos:** Marca y guarda tus Pokémon preferidos.
-   **Encuentros Aleatorios:** La app utiliza tu ubicación para encontrar Pokémon a medida que te mueves.
-   **Soporte Offline:** Los datos e imágenes de los Pokémon consultados se guardan en el dispositivo para que puedas acceder a ellos sin conexión a internet.
-   **Interfaz Adaptable:** La pantalla de detalles se ajusta a diferentes tamaños de pantalla (móvil vs. tablet).

## Arquitectura y Patrones

Este proyecto sigue los principios de **Clean Architecture** para mantener una separación clara de responsabilidades, lo que resulta en un código más escalable, mantenible y testeable.

-   **`UI Layer`**: Desarrollada completamente con **Jetpack Compose** y siguiendo el patrón **MVVM** (Model-View-ViewModel). La comunicación entre la UI y los ViewModels se realiza a través de un flujo de datos unidireccional (UDF) utilizando `StateFlow`.

-   **`Domain Layer`**: Contiene la lógica de negocio pura, encapsulada en **Casos de Uso** (Use Cases) que son independientes de otras capas.

-   **`Data Layer`**: Implementa el **Patrón Repositorio** para actuar como única fuente de verdad (Single Source of Truth), gestionando datos desde una API remota y una base de datos local.

-   **Inyección de Dependencias**: Se utiliza un contenedor de dependencias manual (`AppContainer`) para proveer las instancias necesarias a lo largo de la aplicación.

## Tecnologías Utilizadas

-   **Lenguaje**: [Kotlin](https://kotlinlang.org/)
-   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
-   **Asincronía**: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
-   **Arquitectura**: MVVM + Clean Architecture
-   **Navegación**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
-   **Networking**: [Retrofit](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson)
-   **Base de Datos Local**: [Room](https://developer.android.com/training/data-storage/room)
-   **Carga de Imágenes**: [Coil](https://coil-kt.github.io/coil/)
-   **Ciclo de Vida**: [Lifecycle ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
-   **Geolocalización**: [Google Play Services for Location](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary)

## Instalación

1.  Clona el repositorio:
    ```bash
    git clone https://github.com/HectorJCGuzman/pokedex.git
    ```
2.  Abre el proyecto en Android Studio.
3.  Sincroniza las dependencias de Gradle.
4.  Ejecuta la aplicación en un emulador o dispositivo físico.

**Nota**: La aplicación requiere permisos de `ACCESS_FINE_LOCATION` para la funcionalidad de encuentros basada en la ubicación.
