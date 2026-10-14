package com.medicionSoftware;

import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        ejecutar(args, "result.txt");
    }

    static void ejecutar(String[] args, String archivoSalida) {
        try {
            double a, b;
            int dof, n;

            if (args.length == 4) {
                a = Double.parseDouble(args[0]);
                b = Double.parseDouble(args[1]);
                dof = Integer.parseInt(args[2]);
                n = Integer.parseInt(args[3]);

            } else if (args.length == 0) {
                Scanner teclado = new Scanner(System.in);
                teclado.useLocale(Locale.US);

                System.out.print("Ingresa el limite inferior (a): ");
                a = teclado.nextDouble();

                System.out.print("Ingresa el limite superior (b): ");
                b = teclado.nextDouble();

                System.out.print("Ingresa los grados de libertad (DOF): ");
                dof = teclado.nextInt();

                System.out.print("Ingresa el numero inicial de segmentos (n): ");
                n = teclado.nextInt();

            } else {
                System.out.println(
                    "Uso: java -jar target/programa5a-simpson-1.0-SNAPSHOT.jar a b dof n");
                return;
            }

            if (a != 0) {
                throw new IllegalArgumentException(
                    "En el ejercicio, el limite inferior debe ser 0.");
            }

            Logic logica = new Logic();
            double p = logica.calcular(b, dof, n, false);

            String resultado = String.format(Locale.US,
                    "a=%.4f | b=%.4f | dof=%d | n=%d | Esperado=N/A | Calculado=%.5f%n",
                    a, b, dof, n, p);

            System.out.println("\n" + resultado.trim());

            Output salida = new Output();
            salida.writeData(archivoSalida, resultado);

            System.out.println("Archivo " + archivoSalida + " generado");

        } catch (NumberFormatException | NoSuchElementException e) {
            System.out.println(
                "Error: verifica que a y b sean decimales y dof y n enteros.");

        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}