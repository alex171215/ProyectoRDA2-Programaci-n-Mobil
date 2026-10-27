#!/usr/bin/env bash
# Auditoría rápida EcoTrip RDA-2. Ejecutar desde la raíz: bash scripts/qa-audit.sh
SRC=app/src/main/java
fail=0
check_empty () { # $1 título, $2 salida (debe ser vacía)
  if [ -n "$2" ]; then echo "❌ $1"; echo "$2"; fail=1; else echo "✅ $1"; fi
}
check_present () { # $1 título, $2 salida (debe existir)
  if [ -z "$2" ]; then echo "⚠️  $1: no encontrado aún"; else echo "✅ $1"; fi
}

echo "== Prohibiciones =="
check_empty "Sin layouts XML"            "$(find app/src -path '*res/layout*' -name '*.xml')"
check_empty "Sin findViewById"           "$(grep -rn 'findViewById' app/src)"
check_empty "Sin operador !!"            "$(grep -rn '!!' $SRC)"
check_empty "Sin colores hardcodeados fuera de ui/theme" "$(grep -rn 'Color(0x' $SRC | grep -v '/ui/theme/')"
check_empty "Sin dependencias con versión literal en app/build.gradle.kts" "$(grep -nE '\"[a-z0-9.\-]+:[a-z0-9.\-]+:[0-9]' app/build.gradle.kts)"

echo "== Requisitos del examen =="
check_present "Rutas @Serializable"          "$(grep -rn '@Serializable' $SRC)"
check_present "data class PreferenciaViaje"  "$(grep -rn 'data class PreferenciaViaje' $SRC)"
check_present "SavedStateHandle"             "$(grep -rn 'SavedStateHandle' $SRC)"
check_present "DataStore"                    "$(grep -rn 'preferencesDataStore' $SRC)"
check_present "toIntOrNull"                  "$(grep -rn 'toIntOrNull' $SRC)"
check_present "popUpTo"                      "$(grep -rn 'popUpTo' $SRC)"
check_present "launchSingleTop"              "$(grep -rn 'launchSingleTop' $SRC)"
check_present "toRoute (lectura tipada)"     "$(grep -rn 'toRoute' $SRC)"
check_present "Scaffold"                     "$(grep -rn 'Scaffold(' $SRC)"
check_present "TopAppBar"                    "$(grep -rn 'TopAppBar(' $SRC)"
check_present "FloatingActionButton"         "$(grep -rn 'FloatingActionButton(' $SRC)"
check_present "Color dinámico"               "$(grep -rn 'dynamicLightColorScheme' $SRC)"

echo "== Entorno =="
grep -nE 'compileSdk|targetSdk|JavaVersion|JvmTarget' app/build.gradle.kts
exit $fail
