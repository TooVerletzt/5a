package com.medicionSoftware;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.junit.Test;

public class AppTest {

    private void caso(String archivo) {
        InputStream entrada = getClass().getResourceAsStream("/assets/" + archivo);
        assertNotNull(entrada);
        try (Scanner datos = new Scanner(entrada)) {
            datos.useLocale(Locale.US);
            double x = datos.nextDouble();
            int dof = datos.nextInt();
            double esperado = datos.nextDouble();
            assertEquals(esperado, new Logic().calcular(x, dof, false), 0.00001);
        }
    }

    // Tres casos oficiales del PDF
    @Test public void caso1() { caso("caso1.txt"); }
    @Test public void caso2() { caso("caso2.txt"); }
    @Test public void caso3() { caso("caso3.txt"); }

    @Test public void gammaEntera() {
        GammaFunction g = new GammaFunction();
        assertEquals(1, g.computeIntGamma(1), 0.000001);
        assertEquals(24, g.computeIntGamma(5), 0.000001);
        assertThrows(IllegalArgumentException.class, () -> g.computeIntGamma(0));
    }

    @Test public void gammaDecimal() {
        GammaFunction g = new GammaFunction();
        assertEquals(Math.sqrt(Math.PI), g.computeDblGamma(0.5), 0.000001);
        assertEquals(1, g.computeDblGamma(1), 0.000001);
        assertEquals(Math.sqrt(Math.PI) / 2, g.computeDblGamma(1.5), 0.000001);
        assertEquals(1.329340388, g.computeDblGamma(2.5), 0.000001);
    }

    @Test public void gammaIncorrecta() {
        GammaFunction g = new GammaFunction();
        assertThrows(IllegalArgumentException.class, () -> g.computeDblGamma(0));
        assertThrows(IllegalArgumentException.class, () -> g.computeDblGamma(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> g.computeDblGamma(0.25));
    }

    @Test public void datosIncorrectos() {
        assertThrows(IllegalArgumentException.class, () -> new SimpsonIntegration(-1, 9));
        assertThrows(IllegalArgumentException.class, () -> new SimpsonIntegration(Double.NaN, 9));
        assertThrows(IllegalArgumentException.class, () -> new SimpsonIntegration(1, 0));
    }

    @Test public void ancho() {
        SimpsonIntegration s = new SimpsonIntegration(1.1, 9);
        assertEquals(0.11, s.computeW(10, 1.1), 0.000001);
        assertThrows(IllegalArgumentException.class, () -> s.computeW(0, 1.1));
        assertThrows(IllegalArgumentException.class, () -> s.computeW(9, 1.1));
    }

    @Test public void puntosYBases() {
        SimpsonIntegration s = new SimpsonIntegration(1.1, 9);
        double[] puntos = s.computeXi(10);
        assertEquals(11, puntos.length);
        assertEquals(1.1, puntos[10], 0.000001);
        double[] bases = s.computeFirstBaseTerms(10, puntos, 9);
        assertEquals(1.0, bases[0], 0.000001);
        assertEquals(1 + 1.1 * 1.1 / 9, bases[10], 0.000001);
    }

    @Test public void basesIncorrectas() {
        SimpsonIntegration s = new SimpsonIntegration(1.1, 9);
        assertThrows(IllegalArgumentException.class, () -> s.computeFirstBaseTerms(2, new double[3], 0));
        assertThrows(IllegalArgumentException.class, () -> s.computeFirstBaseTerms(2, null, 9));
        assertThrows(IllegalArgumentException.class, () -> s.computeFirstBaseTerms(2, new double[2], 9));
    }

    @Test public void exponenteYCoeficiente() {
        SimpsonIntegration s = new SimpsonIntegration(1.1, 9);
        assertEquals(-5, s.computeExponent(9), 0.000001);
        assertEquals(0.38803490887, s.computeCoefficient(9), 0.000001);
        assertThrows(IllegalArgumentException.class, () -> s.computeExponent(0));
        assertThrows(IllegalArgumentException.class, () -> s.computeCoefficient(0));
    }

    @Test public void multiplicadores() {
        SimpsonIntegration s = new SimpsonIntegration(1.1, 9);
        double[] f = s.computeFxi(10), t = s.computeFinalTerms(10);
        assertEquals(f[0], t[0], 0.000001);
        assertEquals(4 * f[1], t[1], 0.000001);
        assertEquals(2 * f[2], t[2], 0.000001);
        assertEquals(f[10], t[10], 0.000001);
    }

    @Test public void integralInicial() {
        SimpsonIntegration s = new SimpsonIntegration(1.1, 9);
        assertEquals(0.3500589, s.computeFinalValue(10), 0.0000001);
        assertThrows(IllegalArgumentException.class, () -> s.computeFinalValue(9));
    }

    @Test public void integralConCero() {
        assertEquals(0, new Logic().calcular(0, 9, false), 0.000001);
    }

    @Test public void integralConMasIteraciones() {
        assertEquals(Math.atan(10) / Math.PI,
                new Logic().calcular(10, 1, true), 0.00001);
    }

    @Test public void integralSinConvergencia() {
        assertThrows(IllegalStateException.class,
                () -> new Logic().calcular(1e100, 1, false));
    }

    @Test public void archivoDeSalida() throws Exception {
        Path archivo = Files.createTempFile("simpson", ".txt");
        try {
            new Output().writeData(archivo.toString(), "prueba");
            assertEquals("prueba", Files.readString(archivo));
        } finally { Files.deleteIfExists(archivo); }
    }

    // Ejecuta App sin modificar result.txt.
    private String ejecutar(String[] args, String entrada, String archivo) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream resultado = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(resultado, true, StandardCharsets.UTF_8));
            App.ejecutar(args, archivo);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return resultado.toString(StandardCharsets.UTF_8);
    }

    @Test public void appConArgumentos() throws Exception {
        assertNotNull(new App());
        Path archivo = Files.createTempFile("resultado", ".txt");
        try {
            String salida = ejecutar(new String[] {"0", "1.1", "9", "10"}, "", archivo.toString());
            assertFalse(salida.contains("Precision alcanzada"));
            assertTrue(salida.contains("Calculado=0.35006"));
            assertTrue(Files.readString(archivo).contains("Calculado=0.35006"));
            assertTrue(ejecutar(new String[] {"0", "1.1", "9", "4"}, "", archivo.toString()).contains("n=4"));
        } finally { Files.deleteIfExists(archivo); }
    }

    @Test public void appConTeclado() throws Exception {
        Path archivo = Files.createTempFile("resultado", ".txt");
        try {
            String salida = ejecutar(new String[0], "0\n1.1812\n10\n10\n", archivo.toString());
            assertTrue(salida.contains("Ingresa el limite inferior"));
            assertTrue(salida.contains("Calculado=0.36757"));
        } finally { Files.deleteIfExists(archivo); }
    }

    @Test public void appDatosIncorrectos() {
        App.main(new String[] {"0", "1.1", "9"});
        assertTrue(ejecutar(new String[] {"0", "texto", "9", "10"}, "", "result.txt").contains("Error:"));
        assertTrue(ejecutar(new String[] {"0", "1.1", "texto", "10"}, "", "result.txt").contains("Error:"));
        assertTrue(ejecutar(new String[] {"1", "1.1", "9", "10"}, "", "result.txt").contains("Error:"));
        assertTrue(ejecutar(new String[] {"0", "1.1", "9", "0"}, "", "result.txt").contains("Error:"));
        assertTrue(ejecutar(new String[] {"0", "1.1", "9", "9"}, "", "result.txt").contains("Error:"));
        assertTrue(ejecutar(new String[] {"0", "-1", "9", "10"}, "", "result.txt").contains("Error:"));
        assertTrue(ejecutar(new String[] {"0", "1e100", "1", "10"}, "", "result.txt").contains("Error:"));
        assertTrue(ejecutar(new String[0], "", "result.txt").contains("Error:"));
    }

    @Test public void appErrorAlGuardar() throws Exception {
        Path carpeta = Files.createTempDirectory("simpson");
        try {
            String salida = ejecutar(new String[] {"0", "1.1", "9", "10"}, "", carpeta.toString());
            assertTrue(salida.contains("Error:"));
        } finally { Files.deleteIfExists(carpeta); }
    }
}