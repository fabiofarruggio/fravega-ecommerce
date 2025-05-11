# fravega-ecommerce

**Objetivo**  
Soportar la ejecución de tests funcionales de UI y de API en Kotlin, tanto en local como en cualquier entorno donde Frávega esté desplegado y sea accesible.  
El framework combina Playwright para UI, RestAssured para API, TestNG como runner, Allure para reportes y Ktlint para estilo de código.

---

## Requisitos

- **Java 11**  
  https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html  
- **Kotlin 1.6.21**  
- **Maven 3.6+**  
  https://maven.apache.org/download.cgi  
- **Git**  
- **Allure Command-Line**  
  https://docs.qameta.io/allure/#_installing_a_commandline  

> ⚠️ Asegurate de tener la variable `JAVA_HOME` apuntando a tu JDK11 y de agregar Maven a tu `PATH`.  

---

## Estructura del proyecto

```

fravega-ecommerce/
├── pom.xml
├── Dockerfile
├── src/
│   ├── main/
│   │   ├── kotlin/com/fravega/...        # helpers, clients, conf, pages, support, utils, model/api/validator…
│   │   └── resources/
│   │       └── properties/...            # hosts, endpoints, credenciales, etc.
│   └── test/
│       ├── kotlin/com/fravega/
│       │   ├── frontend/                 # Playwright + TestNG UI tests
│       │   │   ├── dataCreation/
│       │   │   ├── e2e/
│       │   │   ├── payments/
│       │   │   └── productionTests/
│       │   └── backend/                  # RestAssured API tests
│       └── resources/
│           ├── fravega-qa-suite.xml      # suite TestNG
│           ├── log4j.properties
│           └── logback-test.xml
└── README.md

````

---

## Configuración de propiedades

En `src/main/resources/properties/` definís los distintos ambientes (por ejemplo: desarrollo, prueba, producción), endpoints, credenciales, etc.  

Al ejecutar, pasás la propiedad `-Denvironment` para apuntar al archivo `properties.{environment}.yml` o `.properties` que corresponda.

---

## Ejecución de tests

Orquestación vía Maven/Surefire + TestNG:

```bash
mvn clean test \
  -Dgroups=e2e,regression \
  -DexcludedGroups=api \
  -Dheadless=false
````

* **groups**: grupos de TestNG a incluir (`e2e`, `smoke`, `productionTests`, etc.).
* **excludedGroups**: opcional, para excluir tests.
* **headless**: ejecuta Playwright en modo headless (`true` o `false`).

---

## Reportes Allure

1. Ejecutás los tests como arriba.

2. Generás el reporte:

   ```bash
   allure generate target/allure-results --clean -o target/allure-report --single-file
   ```

3. Abrís `report/index.html` en tu navegador.

---

## Docker

1. **Build**

   ```bash
   docker build -t your-registry.com/fravega-ecommerce:latest .
   ```
2. **Push** (previo `docker login`)

   ```bash
   docker push your-registry.com/fravega-ecommerce:latest
   ```
3. **Pull**

   ```bash
   docker pull your-registry.com/fravega-ecommerce:latest
   ```

En tu pipeline de CI podés usar este contenedor o replicar los pasos del `Dockerfile` directamente.

---

## Check de estilo con Ktlint

* **Ver errores**

  ```bash
  mvn antrun:run@ktlint
  ```
* **Auto-formatear**

  ```bash
  mvn antrun:run@ktlint-format
  ```

---

## Clonar repositorio

```bash
git clone https://github.com/fabiofarruggio/fravega-ecommerce
```

```
```
