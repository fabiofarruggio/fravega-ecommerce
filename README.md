# fravega-ecommerce

**Objetivo**  
Este repositorio contiene un framework robusto de automatización de pruebas funcionales para UI y APIs en Kotlin.  
El propósito es asegurar la calidad de funcionalidades críticas en entornos accesibles, incluyendo producción.  
Se utiliza Playwright para UI, RestAssured para APIs, TestNG como framework de ejecución, Allure para reportería y Ktlint para estilo de código.

---

## 🔧 Requisitos

- **Java 11**  
- **Kotlin 1.6.21**  
- **Maven 3.6+**  
- **Git**  
- **Allure Command-Line**  
- **Docker** (opcional, pero recomendado para CI y local)

> ⚠️ Asegurate de tener `JAVA_HOME` correctamente configurado y `mvn` disponible en tu `PATH`.

---

## 📁 Estructura del proyecto

```

fravega-ecommerce/
├── pom.xml                         # Configuración del proyecto y dependencias
├── Dockerfile                      # Imagen Docker basada en Playwright Java + Allure
├── src/
│   ├── main/kotlin/com/fravega/    # Código productivo: clients, helpers, conf, pages, utils, etc.
│   ├── main/resources/properties/  # Configuración productiva
│   ├── test/kotlin/com/fravega/    # Casos de prueba de UI y API
│   └── test/resources/             # Suite TestNG, logs y configuración de pruebas
└── README.md

````

---

## ▶️ Ejecución local de tests

```bash
mvn clean test \
  -Dgroups=e2e,regression \
  -DexcludedGroups=api \
  -Dheadless=false
````

### Parámetros comunes:

* `groups`: TestNG tags a ejecutar (`e2e`, `smoke`, `frontend`, `backend`, etc.)
* `excludedGroups`: excluir grupos de tests si es necesario
* `headless`: ejecuta Playwright sin UI (por defecto `true` en CI)

> En esta demo se apunta directamente a producción, sin necesidad de configurar múltiples entornos.

---

## 📊 Reportes Allure (modo local)

1. Ejecutar los tests

2. Generar el reporte:

   ```bash
   allure generate target/allure-results --clean -o target/allure-report --single-file
   ```

3. Abrir:

   ```bash
   target/allure-report/index.html
   ```

---

## ⚙️ CI/CD en GitHub Actions

Este proyecto se integra con GitHub Actions para CI/CD automático. El flujo está dividido en 3 etapas:

### 1. `build`

* Se ejecuta solo ante cambios relevantes (`Dockerfile`, `src`, `pom.xml`, etc.).
* Construye y sube la imagen Docker a GHCR (GitHub Container Registry).

### 2. `test`

* Ejecuta los tests funcionales dentro del contenedor publicado.
* Soporta ejecución manual desde GitHub con selección de grupo de tests.

### 3. `pages`

* Genera y publica el reporte de Allure en GitHub Pages.

🔗 **Último reporte Allure publicado:**
[https://fabiofarruggio.github.io/fravega-ecommerce](https://fabiofarruggio.github.io/fravega-ecommerce)

---

## 🚀 Ejecutar workflow manualmente desde GitHub

Podés correr los tests desde la pestaña **Actions** del repositorio seleccionando el workflow `CI & Pages` y presionando "Run workflow".

### Parámetro disponible:

* `test_group`: grupo de tests a ejecutar (`all`, `frontend`, `backend`)

### Comportamiento:

| test\_group | Maven ejecutado                    |
| ----------- | ---------------------------------- |
| `all`       | `mvn clean test`                   |
| `frontend`  | `mvn clean test -Dgroups=frontend` |
| `backend`   | `mvn clean test -Dgroups=backend`  |

---

## 🐳 Docker (ejecución manual/local)

### 1. Construir imagen localmente

```bash
docker build -t ghcr.io/<tu-usuario>/<repo>:latest .
```

### 2. Login y push al registry

```bash
echo $GITHUB_TOKEN | docker login ghcr.io -u <tu-usuario> --password-stdin
docker push ghcr.io/<tu-usuario>/<repo>:latest
```

### 3. Ejecutar los tests

```bash
docker run --rm \
  -e GOREST_TOKEN=<token> \
  -v $(pwd)/target:/usr/src/app/target \
  ghcr.io/<tu-usuario>/<repo>:latest clean test -Dgroups=frontend
```

---

## ✨ Check de estilo con Ktlint

### Ver errores de formato

```bash
mvn antrun:run@ktlint
```

### Autoformatear

```bash
mvn antrun:run@ktlint-format
```

---

## 📥 Clonar el repositorio

```bash
git clone https://github.com/fabiofarruggio/fravega-ecommerce
cd fravega-ecommerce
```
