package nominas.Laboral;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ProcesarEmpleados {

    // Método para escribir el sueldo en sueldos.dat
    public static void escribirSueldoEnArchivo(String dni, int sueldo) throws IOException {
        try (DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream("sueldos.dat", true))) {
            dataOutput.writeUTF(dni);
            dataOutput.writeInt(sueldo);
            System.out.println("Escribiendo en sueldos.dat: DNI=" + dni + ", Sueldo=" + sueldo);
        }
    }

    // Método para procesar empleados desde un archivo
    public static void procesarEmpleadosDesdeArchivo(String inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                try {
                    if (parts.length == 5) {
                        int anyos = Integer.parseInt(parts[0].trim());
                        int categoria = Integer.parseInt(parts[1].trim());
                        String dni = parts[2].trim();
                        String nombre = parts[3].trim();
                        char sexo = parts[4].trim().charAt(0);

                        Empleado empleado = new Empleado(anyos, categoria, dni, nombre, sexo);
                        int sueldo = Nomina.sueldo(empleado);

                        // Escribir en el archivo de sueldos
                        escribirSueldoEnArchivo(dni, sueldo);

                    } else if (parts.length == 3) {
                        String dni = parts[0].trim();
                        String nombre = parts[1].trim();
                        char sexo = parts[2].trim().charAt(0);

                        Empleado empleado = new Empleado(dni, nombre, sexo);
                        int sueldo = Nomina.sueldo(empleado);

                        // Escribir en el archivo de sueldos
                        escribirSueldoEnArchivo(dni, sueldo);

                    } else {
                        System.err.println("Formato incorrecto en la línea: " + line);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error de formato numérico en la línea: " + line);
                } catch (DatosNoCorrecosException e) {
                    System.err.println("Datos incorrectos en la línea: " + line + ". " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Cambia la ruta del archivo según sea necesario
        String inputFile = "empleados.txt";

        // Procesar empleados desde el archivo
        procesarEmpleadosDesdeArchivo(inputFile);
    }
}
