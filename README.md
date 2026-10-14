# Programa 5a - Regla de Simpson (Java 17)

Programa de Medicion de Software que integra la distribucion t de Student desde 0 hasta x.
Para cada ejecucion el usuario escribe un valor de x y los grados de libertad (DOF).

## Clases

- `App.java`: pide x y DOF por teclado, muestra el resultado y guarda `result.txt`.
- `Logic.java`: comienza con 10 segmentos y muestra las aproximaciones. Duplica los segmentos hasta que la diferencia de las ultimas dos aproximaciones sea menor que 0.00001.
- `GammaFunction.java`: calcula Gamma para enteros positivos y medios enteros positivos.
- `SimpsonIntegration.java`: calcula W, puntos xi, funcion t y suma ponderada de Simpson para una cantidad de segmentos.
- `Output.java`: escribe el resultado en un archivo.
- `AppTest.java`: una sola clase JUnit 4 con 17 pruebas. Los tres casos oficiales se leen de archivos distintos dentro de `src/test/resources/assets/`.

## Requisitos

- Java 17 (incluye Amazon Corretto 17)
- Maven
- Conexion a internet la primera vez que Maven descargue las dependencias

## Ejecutar pruebas y crear JAR

Abrir la terminal en la carpeta que contiene `pom.xml` y ejecutar:

```bash
mvn clean verify
```

Si todo termina con `BUILD SUCCESS`, ejecutar:

```bash
java -jar target/programa5a-simpson-1.0-SNAPSHOT.jar
```

El programa pregunta por x y DOF; se introducen por teclado, uno despues del otro.
Muestra Gamma, una tabla de iteraciones y el resultado. Guarda el resultado final en `result.txt`.
Para calcular otro caso, hay que ejecutar el JAR nuevamente.

## Casos oficiales del PDF

| Caso | x | DOF | p esperado |
| --- | --- | --- | --- |
| 1 | 1.1 | 9 | 0.35006 |
| 2 | 1.1812 | 10 | 0.36757 |
| 3 | 2.750 | 30 | 0.49500 |

Cada archivo `assets/casoN.txt` contiene: `x DOF p_esperado`.
JUnit los lee para comprobar el calculo, sin pedir datos por teclado.

## Reporte de cobertura

Tras ejecutar `mvn clean verify`, abrir:

```bash
xdg-open target/site/jacoco/index.html
```

La carpeta `target/` y `result.txt` estan ignorados por Git.

## GitHub (un PR hacia main)

1. Crear en GitHub un repositorio con README inicial, de modo que exista `main`.
2. Clonarlo: `git clone URL_DEL_REPOSITORIO`.
3. Entrar al repositorio: `cd NOMBRE_REPOSITORIO`.
4. Crear la rama: `git switch -c feature/programa5a`.
5. Copiar el contenido de esta carpeta (incluyendo `.github`) al repositorio clonado.
6. Ejecutar `mvn clean verify` y confirmar que las pruebas pasen.
7. `git add .` y `git commit -m "Implementar programa 5a Simpson con pruebas"`.
8. `git push -u origin feature/programa5a`.
9. Abrir un Pull Request de `feature/programa5a` a `main`.
10. Esperar la comprobacion `pruebas` de GitHub Actions. Configurar la regla de proteccion de `main` para exigir PR y comprobaciones correctas. Cuando todo este en verde, realizar merge.

El workflow `.github/workflows/ci.yml` usa Corretto 17 y `mvn -B clean verify`.

## PSP2

El PDF del Programa 5 tambien solicita registros de planificacion, estimacion PROBE,
revision del diseno y del codigo, pruebas, tiempo, defectos y postmortem.
Son registros que se elaboran con datos reales del desarrollo; no se generan automaticamente
por el programa ni por GitHub Actions.
