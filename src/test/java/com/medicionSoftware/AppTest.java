package com.medicionSoftware;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.junit.Test;
public class AppTest {
    // Lee los tres casos oficiales de la carpeta assets.
    private double[] leerCaso(String archivo) {
        InputStream entrada = getClass().getResourceAsStream("/assets/" + archivo);
        try (Scanner datos = new Scanner(entrada)) {
            datos.useLocale(Locale.US);
            return new double[] {datos.nextDouble(), datos.nextInt(), datos.nextDouble()};
        }
    }
    // Ejecuta App y recupera lo que imprime en la terminal.
    private String ejecutar(String[] args, String entrada, String archivo) {
        InputStream anteriorIn = System.in;
        PrintStream anteriorOut = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
            if (archivo == null) App.main(args);
            else App.ejecutar(args, archivo);
        } finally {
            System.setIn(anteriorIn);
            System.setOut(anteriorOut);
        }
        return salida.toString(StandardCharsets.UTF_8);
    }
    // Casos de prueba indicados en el PDF.
    @Test
    public void caso1() {
        double[] d = leerCaso("caso1.txt");
        assertEquals(d[2], new Logic().calcular(d[0], (int)d[1], false), 0.00001);
    }
    @Test
    public void caso2() {
        double[] d = leerCaso("caso2.txt");
        assertEquals(d[2], new Logic().calcular(d[0], (int)d[1], false), 0.00001);
    }
    @Test
    public void caso3() {
        double[] d = leerCaso("caso3.txt");
        assertEquals(d[2], new Logic().calcular(d[0], (int)d[1], false), 0.00001);
    }
    // Pruebas de la funcion Gamma.
    @Test
    public void gammaEnteraUno() {
        assertEquals(1, new GammaFunction().computeIntGamma(1), 0.000001);
    }
    @Test
    public void gammaEnteraCinco() {
        assertEquals(24, new GammaFunction().computeIntGamma(5), 0.000001);
    }
    @Test
    public void gammaEnteraCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new GammaFunction().computeIntGamma(0));
    }
    @Test
    public void gammaDeUnMedio() {
        assertEquals(Math.sqrt(Math.PI),
                new GammaFunction().computeDblGamma(0.5), 0.000001);
    }
    @Test
    public void gammaDeUno() {
        assertEquals(1, new GammaFunction().computeDblGamma(1), 0.000001);
    }
    @Test
    public void gammaDeTresMedios() {
        assertEquals(Math.sqrt(Math.PI) / 2,
                new GammaFunction().computeDblGamma(1.5), 0.000001);
    }
    @Test
    public void gammaNoFinita() {
        assertThrows(IllegalArgumentException.class,
                () -> new GammaFunction().computeDblGamma(Double.NaN));
    }
    @Test
    public void gammaDecimalCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new GammaFunction().computeDblGamma(0));
    }
    @Test
    public void gammaCuarto() {
        assertThrows(IllegalArgumentException.class,
                () -> new GammaFunction().computeDblGamma(0.25));
    }
    // Validaciones de los parametros de Simpson.
    @Test
    public void simpsonXNegativa() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(-1, 9));
    }
    @Test
    public void simpsonXNoFinita() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(Double.NaN, 9));
    }
    @Test
    public void simpsonDofCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(1, 0));
    }
    @Test
    public void anchoCorrecto() {
        assertEquals(0.11, new SimpsonIntegration(1.1, 9).computeW(10, 1.1), 0.000001);
    }
    @Test
    public void anchoCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(1, 9).computeW(0, 1));
    }
    @Test
    public void anchoImpar() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(1, 9).computeW(9, 1));
    }
    @Test
    public void puntosDeSimpson() {
        double[] puntos = new SimpsonIntegration(1.1, 9).computeXi(10);
        assertEquals(1.1, puntos[10], 0.000001);
    }
    @Test
    public void terminosBase() {
        SimpsonIntegration s = new SimpsonIntegration(1.1, 9);
        assertEquals(1 + 1.1 * 1.1 / 9,
                s.computeFirstBaseTerms(10, s.computeXi(10), 9)[10], 0.000001);
    }
    @Test
    public void baseDofCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(1, 9).computeFirstBaseTerms(2, new double[3], 0));
    }
    @Test
    public void baseNula() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(1, 9).computeFirstBaseTerms(2, null, 9));
    }
    @Test
    public void baseLongitudIncorrecta() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(1, 9).computeFirstBaseTerms(2, new double[2], 9));
    }
    @Test
    public void exponenteCorrecto() {
        assertEquals(-5, new SimpsonIntegration(1.1, 9).computeExponent(9), 0.000001);
    }
    @Test
    public void exponenteDofCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(1, 9).computeExponent(0));
    }
    @Test
    public void coeficienteCorrecto() {
        assertEquals(0.38803490887,
                new SimpsonIntegration(1.1, 9).computeCoefficient(9), 0.000001);
    }
    @Test
    public void coeficienteDofCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpsonIntegration(1, 9).computeCoefficient(0));
    }
    @Test
    public void funcionDeStudent() {
        SimpsonIntegration s = new SimpsonIntegration(1.1, 9);
        assertEquals(s.computeCoefficient(9), s.computeFxi(10)[0], 0.000001);
    }
    @Test
    public void multiplicadoresSimpson() {
        SimpsonIntegration s = new SimpsonIntegration(1.1, 9);
        double[] f = s.computeFxi(10), t = s.computeFinalTerms(10);
        assertArrayEquals(new double[] {f[0], 4*f[1], 2*f[2], f[10]},
                new double[] {t[0], t[1], t[2], t[10]}, 0.000001);
    }
    @Test
    public void integralInicial() {
        assertEquals(0.3500589,
                new SimpsonIntegration(1.1, 9).computeFinalValue(10), 0.0000001);
    }
    // Pruebas de convergencia y limites de iteraciones.
    @Test
    public void integralConCero() {
        assertEquals(0, new Logic().calcular(0, 9, false), 0.000001);
    }
    @Test
    public void integralConMasIteraciones() {
        assertEquals(Math.atan(10) / Math.PI, new Logic().calcular(10, 1, true), 0.00001);
    }
    @Test
    public void integralSinConvergencia() {
        assertThrows(IllegalStateException.class,
                () -> new Logic().calcular(1e100, 1, false));
    }
    @Test
    public void segmentosCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new Logic().calcular(1.1, 9, 0, false));
    }
    @Test
    public void segmentosImpares() {
        assertThrows(IllegalArgumentException.class,
                () -> new Logic().calcular(1.1, 9, 9, false));
    }
    // Salida de archivos y programa principal.
    @Test
    public void guardaResultado() throws Exception {
        Path archivo = Files.createTempFile("simpson-", ".txt");
        try {
            new Output().writeData(archivo.toString(), "prueba");
            assertEquals("prueba", Files.readString(archivo));
        } finally { Files.deleteIfExists(archivo); }
    }
    @Test
    public void appConArgumentos() throws Exception {
        new App(); // Tambien ejecuta el constructor de App.
        Path archivo = Files.createTempFile("simpson-", ".txt");
        try {
            String texto = ejecutar(new String[] {"0", "1.1", "9", "10"}, "", archivo.toString());
            assertTrue(texto.contains("Calculado=0.35006"));
        } finally { Files.deleteIfExists(archivo); }
    }
    @Test
    public void appConTeclado() throws Exception {
        Path archivo = Files.createTempFile("simpson-", ".txt");
        try {
            String texto = ejecutar(new String[0], "0\n1.1812\n10\n10\n", archivo.toString());
            assertTrue(texto.contains("Calculado=0.36757"));
        } finally { Files.deleteIfExists(archivo); }
    }
    @Test
    public void appCantidadIncorrecta() {
        assertTrue(ejecutar(new String[] {"0", "1.1", "9"}, "", null).contains("Uso:"));
    }
    @Test
    public void appDatosNoNumericos() {
        String a = ejecutar(new String[] {"0", "texto", "9", "10"}, "", "result.txt");
        String b = ejecutar(new String[] {"0", "1.1", "texto", "10"}, "", "result.txt");
        assertTrue(a.contains("Error:") && b.contains("Error:"));
    }
    @Test
    public void appLimiteInferiorIncorrecto() {
        assertTrue(ejecutar(new String[] {"1", "1.1", "9", "10"}, "", "result.txt").contains("Error:"));
    }
    @Test
    public void appTecladoIncorrecto() {
        assertTrue(ejecutar(new String[0], "", "result.txt").contains("Error:"));
    }
    @Test
    public void appErrorAlGuardar() throws Exception {
        Path carpeta = Files.createTempDirectory("simpson-");
        try {
            String texto = ejecutar(new String[] {"0", "1.1", "9", "10"}, "", carpeta.toString());
            assertTrue(texto.contains("Error:"));
        } finally { Files.deleteIfExists(carpeta); }
    }
}