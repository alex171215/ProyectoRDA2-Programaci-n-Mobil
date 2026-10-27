# EcoTrip 2026 — Sistema Resiliente de Planificación de Rutas

Examen Práctico RDA-2 · Programación Móvil · PUCE

## Integrantes
| Rol | Integrante | Responsabilidad |
|---|---|---|
| Rol 1 | Paúl Rosero | Arquitecto Core y Persistencia |
| Rol 2 | Alejandro Flores | Desarrollador UI - Formulario |
| Rol 3 | Joselyn Cadena | Desarrollador UI - Material You |
| Rol 4 | Michael Molina | Arquitecto de Flujo (Navegación) |
| Rol 5 | Gabriel Aguinaga | Calidad (QA) y Documentación |

## Entorno obligatorio
- Android Studio con **Gradle JDK = JetBrains Runtime 21** (Settings → Build, Execution, Deployment → Build Tools → Gradle).
- `compileSdk = 36`, `targetSdk = 36` (Android 16), `minSdk = 26`.
- Dependencias **solo** desde `gradle/libs.versions.toml`.
- Emulador **API 36** con imagen *Google APIs* o *Google Play* (necesaria para Material You).
- UI 100 % Jetpack Compose: prohibidos layouts XML y `findViewById`.

## Estructura de paquetes acordada
```
com.puce.ecotrip
├── MainActivity.kt
├── data/
│   ├── model/PreferenciaViaje.kt        // data class inmutable + enum TipoTransporte
│   └── PreferenciasRepository.kt        // DataStore (nombre + baja huella)
├── navigation/
│   ├── Rutas.kt                         // @Serializable object Formulario / data class Resumen(...)
│   └── EcoTripNavHost.kt                // NavHost, popUpTo + launchSingleTop
└── ui/
    ├── EcoTripViewModel.kt              // SavedStateHandle (destino, días) + estado reactivo
    ├── screens/FormularioScreen.kt
    ├── screens/ResumenScreen.kt
    └── theme/                           // Color dinámico (Material You)
```

## Flujo de trabajo Git
- `main` protegida: todo entra por Pull Request aprobado por QA (Rol 5).
- Una rama por hito; cada integrante commitea con **su** cuenta (`git config user.email` = email de GitHub).

| Commit obligatorio | Rama |
|---|---|
| `feat: data models and routes` | `feat/models-routes` |
| `feat: viewmodel and dataStore implementation` | `feat/viewmodel-datastore` |
| `feat: compose ui layout with material design 3` | `feat/ui-md3` |
| `feat: type-safe navigation and testing` | `feat/navigation` |

Commits de soporte: `chore:` (configuración), `docs:` (informe y evidencias), `fix:` (correcciones), `test:`.

## Auditoría QA
```bash
bash scripts/qa-audit.sh
```
Las capturas del emulador (rotación, muerte de proceso, cambio de wallpaper) se guardan en `docs/evidencias/`.
