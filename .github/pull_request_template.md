## Descripción
<!-- Qué agrega este PR y a qué commit obligatorio corresponde -->

## Checklist QA (lo revisa el Rol 5 antes de aprobar)
- [ ] Compila con `compileSdk = 36`, `targetSdk = 36` y Gradle JDK = JBR 21
- [ ] Toda dependencia nueva está en `gradle/libs.versions.toml` (sin versiones sueltas en `build.gradle.kts`)
- [ ] Sin layouts XML ni `findViewById`
- [ ] Sin `!!`; nulos controlados con `?.` / `?:`; números con `toIntOrNull()`
- [ ] Modelos con `val` (inmutables)
- [ ] La UI solo usa `MaterialTheme.colorScheme` (sin `Color(0x...)` fuera de `ui/theme`)
- [ ] Las pantallas reciben estado y lambdas (state hoisting); sin lógica de negocio en composables
- [ ] `scripts/qa-audit.sh` ejecutado sin hallazgos
