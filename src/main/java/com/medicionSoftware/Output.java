package com.medicionSoftware;

import java.io.FileWriter;
import java.io.IOException;

public class Output {

    public void writeData(String nombreArchivo, String texto) throws IOException {
        try (FileWriter archivo = new FileWriter(nombreArchivo)) {
            archivo.write(texto);
        }
    }
}
