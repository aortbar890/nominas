package nominas.Laboral;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class AltasEmpleados {

    private static final String URL = "jdbc:mariadb://localhost:3306/nominas";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root"; 

    private Scanner scanner = new Scanner(System.in);

    //Conexión a la base de datos
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    //Calcula el sueldo usando la clase Nomina
    private int calcularSueldo(int categoria, int anyos) throws DatosNoCorrecosException {
        return Nomina.sueldo(new Empleado(anyos, categoria, "", "", ' '));
    }

    //Actualiza empleados.txt
    private void actualizarEmpleadosTxt() throws IOException, SQLException {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM empleados");
                PrintWriter out = new PrintWriter(new FileWriter("empleados.txt"))) { //Sobreescribe el archivo

            while (rs.next()) {
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                char sexo = rs.getString("sexo").charAt(0);
                int anyos = rs.getInt("anyos");
                int categoria = rs.getInt("categoria");

                // Escribe la información del empleado en el archivo
                out.println(
                        String.format("%s, %s, %s, %d, %d", dni, nombre, Character.toString(sexo), anyos, categoria));
            }
        }
    }

    //Actualiza los sueldos en sueldos.dat
    private void actualizarSueldosDat() throws SQLException, IOException {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT dni, sueldo FROM nominas");
                PrintWriter out = new PrintWriter(new FileWriter("sueldos.dat"))) {

            while (rs.next()) {
                String dni = rs.getString("dni");
                int sueldo = rs.getInt("sueldo");

                //Escribe la información del sueldo en el archivo
                out.println(String.format("%s, %d", dni, sueldo));
            }
        }
    }

    // Menú de opciones
    public void mostrarMenu() throws SQLException, DatosNoCorrecosException, IOException {
        while (true) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Mostrar todos los datos de la tabla empleados");
            System.out.println("2. Mostrar el salario de un empleado");
            System.out.println("3. Modificar datos de un empleado");
            System.out.println("4. Recalcular y actualizar sueldo de un empleado");
            System.out.println("5. Recalcular y actualizar sueldos de todos los empleados");
            System.out.println("6. Realizar copia de seguridad de la base de datos");
            System.out.println("7. Salir");
            System.out.print("Selecciona una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    mostrarDatosEmpleados();
                    break;
                case 2:
                    mostrarSalarioEmpleado();
                    break;
                case 3:
                    modificarDatosEmpleado();
                    break;
                case 4:
                    recalcularSueldoEmpleado();
                    break;
                case 5:
                    recalcularSueldosTodosEmpleados();
                    break;
                case 6:
                    realizarCopiaSeguridad();
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // 1. Mostrar todos los datos de la tabla empleados
    private void mostrarDatosEmpleados() throws SQLException {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM empleados")) {

            System.out.println("\n--- Lista de Empleados ---");
            while (rs.next()) {
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                char sexo = rs.getString("sexo").charAt(0);
                int anyos = rs.getInt("anyos");
                int categoria = rs.getInt("categoria");
                System.out.printf("DNI: %s, Nombre: %s, Sexo: %c, Años: %d, Categoría: %d\n",
                        dni, nombre, sexo, anyos, categoria);
            }
        }
    }

    // 2. Mostrar el salario de un empleado recibiendo su DNI como parámetro
    private void mostrarSalarioEmpleado() throws SQLException {
        System.out.print("Introduce el DNI del empleado: ");
        String dni = scanner.nextLine();

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT sueldo FROM nominas WHERE dni = '" + dni + "'")) {

            if (rs.next()) {
                int sueldo = rs.getInt("sueldo");
                System.out.println("El sueldo del empleado con DNI " + dni + " es: " + sueldo);
            } else {
                System.out.println("Empleado no encontrado.");
            }
        }
    }

    // 3. Submenú para modificar datos de un empleado
    private void modificarDatosEmpleado() throws SQLException, DatosNoCorrecosException, IOException {
        System.out.print("Introduce el DNI del empleado que deseas modificar: ");
        String dni = scanner.nextLine();

        System.out.println("\n--- Modificar Datos ---");
        System.out.println("1. Modificar nombre");
        System.out.println("2. Modificar sexo");
        System.out.println("3. Modificar años trabajados");
        System.out.println("4. Modificar categoría");
        System.out.print("Selecciona una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); 

        switch (opcion) {
            case 1:
                System.out.print("Introduce el nuevo nombre: ");
                String nuevoNombre = scanner.nextLine();
                modificarDatoEmpleado(dni, "nombre", "'" + nuevoNombre + "'");
                break;
            case 2:
                System.out.print("Introduce el nuevo sexo (M/F): ");
                char nuevoSexo = scanner.nextLine().charAt(0);
                modificarDatoEmpleado(dni, "sexo", "'" + nuevoSexo + "'");
                break;
            case 3:
                System.out.print("Introduce los nuevos años trabajados: ");
                int nuevosAnyos = scanner.nextInt();
                modificarDatoEmpleado(dni, "anyos", String.valueOf(nuevosAnyos));
                break;
            case 4:
                System.out.print("Introduce la nueva categoría: ");
                int nuevaCategoria = scanner.nextInt();
                modificarDatoEmpleado(dni, "categoria", String.valueOf(nuevaCategoria));
                break;
            default:
                System.out.println("Opción no válida.");
        }

    }

    //Modifica un dato específico de un empleado
    private void modificarDatoEmpleado(String dni, String campo, String nuevoValor) throws SQLException {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            String updateQuery = String.format("UPDATE empleados SET %s = %s WHERE dni = '%s'", campo, nuevoValor, dni);
            stmt.executeUpdate(updateQuery);
            System.out.println("Dato actualizado correctamente.");
        }
    }

    // 4. Recalcular y actualizar el sueldo de un empleado
    private void recalcularSueldoEmpleado() throws SQLException, DatosNoCorrecosException, IOException {
        System.out.print("Introduce el DNI del empleado: ");
        String dni = scanner.nextLine();
        recalcularSueldoEmpleado(dni);
    }

    //Recalcula el sueldo de un empleado y actualizar la BD
    private void recalcularSueldoEmpleado(String dni) throws SQLException, DatosNoCorrecosException, IOException {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT anyos, categoria FROM empleados WHERE dni = '" + dni + "'")) {

            if (rs.next()) {
                int anyos = rs.getInt("anyos");
                int categoria = rs.getInt("categoria");
    
                int nuevoSueldo = calcularSueldo(categoria, anyos);
 
                String updateNominaQuery = String.format("UPDATE nominas SET sueldo = %d WHERE dni = '%s'", nuevoSueldo,
                        dni);
                stmt.executeUpdate(updateNominaQuery);

                System.out.println("Sueldo recalculado y actualizado para el empleado con DNI " + dni);
            } else {
                System.out.println("Empleado no encontrado.");
            }
        }
    }

    // 5. Recalcular y actualizar los sueldos de todos los empleados
    private void recalcularSueldosTodosEmpleados() throws SQLException, DatosNoCorrecosException, IOException {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT dni, anyos, categoria FROM empleados")) {

            while (rs.next()) {
                String dni = rs.getString("dni");
                int anyos = rs.getInt("anyos");
                int categoria = rs.getInt("categoria");

                int nuevoSueldo = calcularSueldo(categoria, anyos);

                String updateNominaQuery = String.format("UPDATE nominas SET sueldo = %d WHERE dni = '%s'", nuevoSueldo,
                        dni);
                stmt.executeUpdate(updateNominaQuery);
            }

            System.out.println("Sueldos recalculados y actualizados para todos los empleados.");
        }
    }

    // 6. Realizar una copia de seguridad de la base de datos
    private void realizarCopiaSeguridad() throws SQLException, IOException {
        actualizarEmpleadosTxt();
        actualizarSueldosDat();
        System.out.println("Copia de seguridad realizada correctamente.");
    }

    // // Método para dar de alta un nuevo empleado
    // public void altaEmpleado(String dni, String nombre, char sexo, Integer anyos,
    // Integer categoria)
    // throws SQLException, DatosNoCorrecosException, IOException {
    // try (Connection conn = getConnection();
    // Statement stmt = conn.createStatement()) {

    // // Asignar valores por defecto si no se proporcionan anyos y categoria
    // if (anyos == null) {
    // anyos = 0; // Valor por defecto para años
    // }
    // if (categoria == null) {
    // categoria = 1; // Valor por defecto para categoría
    // }

    // // Consulta de inserción de empleados
    // String insertEmpleadoQuery = String.format(
    // "INSERT INTO empleados (dni, nombre, sexo, anyos, categoria) VALUES ('%s',
    // '%s', '%s', %d, %d)",
    // dni, nombre, String.valueOf(sexo), anyos, categoria);

    // // Ejecutar la consulta de inserción
    // stmt.executeUpdate(insertEmpleadoQuery);

    // // Calcular sueldo
    // int sueldo = calcularSueldo(categoria, anyos);

    // // Insertar en la tabla nominas
    // String insertNominaQuery = String.format("INSERT INTO nominas (dni, sueldo)
    // VALUES ('%s', %d)", dni,
    // sueldo);
    // stmt.executeUpdate(insertNominaQuery);

    // // Actualizar empleados.txt
    // actualizarEmpleadosTxt();

    // System.out
    // .println("Empleado dado de alta correctamente con DNI: " + dni + " y sueldo
    // calculado: " + sueldo);
    // } catch (SQLException e) {
    // System.err.println("Error al insertar el empleado: " + e.getMessage());
    // throw e; // Lanzar la excepción para que pueda ser manejada más arriba si es
    // necesario
    // }
    // }

    // // Sobrecarga del método para alta de empleados desde un archivo
    // public void altaEmpleado(String archivo) throws IOException, SQLException,
    // DatosNoCorrecosException {
    // try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
    // String linea;
    // while ((linea = br.readLine()) != null) {
    // String[] datos = linea.split(",");

    // String dni = datos[0].trim();
    // String nombre = datos[1].trim();
    // char sexo = datos[2].trim().charAt(0);

    // Integer anyos = null;
    // Integer categoria = null;

    // // Si hay más de 3 valores en la línea, asignar años y categoría
    // if (datos.length >= 5) {
    // anyos = Integer.parseInt(datos[3].trim());
    // categoria = Integer.parseInt(datos[4].trim());
    // }

    // // Dar de alta el empleado
    // altaEmpleado(dni, nombre, sexo, anyos, categoria);
    // }
    // }
    // }

    // // Método para incrementar los años trabajados de un empleado
    // public void incrementarAnyosEmpleado(String dni) throws SQLException,
    // DatosNoCorrecosException, IOException {
    // String updateEmpleadoQuery = String.format("UPDATE empleados SET anyos =
    // anyos + 1 WHERE dni = '%s'", dni);
    // String selectEmpleadoQuery = String.format("SELECT nombre, sexo, categoria,
    // anyos FROM empleados WHERE dni = '%s'", dni);

    // try (Connection conn = getConnection();
    // Statement stmt = conn.createStatement()) {

    // // 1. Actualizar los años del empleado
    // stmt.executeUpdate(updateEmpleadoQuery);

    // // 2. Obtener los datos del empleado
    // ResultSet rs = stmt.executeQuery(selectEmpleadoQuery);

    // if (rs.next()) {
    // // No se usa nombre y sexo, pero están disponibles si los necesitas
    // // String nombre = rs.getString("nombre");
    // // char sexo = rs.getString("sexo").charAt(0);
    // int categoria = rs.getInt("categoria");
    // int anyos = rs.getInt("anyos"); // Nuevo número de años después del
    // incremento

    // // 3. Calcular el nuevo sueldo
    // int nuevoSueldo = calcularSueldo(categoria, anyos + 1); // Incremento manual

    // // 4. Actualizar el sueldo en la tabla nominas
    // String updateNominaQuery = String.format("UPDATE nominas SET sueldo = %d
    // WHERE dni = '%s'", nuevoSueldo, dni);
    // stmt.executeUpdate(updateNominaQuery);

    // // Imprimir el nuevo sueldo calculado
    // System.out.println("Nuevo sueldo calculado: " + nuevoSueldo);
    // }

    // // 5. Actualizar todos los datos en los documentos
    // actualizarEmpleadosTxt();
    // actualizarSueldosDat();

    // System.out.println("Años incrementados para el empleado con DNI: " + dni);
    // } catch (DatosNoCorrecosException e) {
    // System.err.println("Error en los datos: " + e.getMessage());
    // throw e;
    // }
    // }

    // // Método para modificar la categoría de un empleado
    // public void modificarCategoriaEmpleado(String dni, int nuevaCategoria) throws
    // SQLException, DatosNoCorrecosException, IOException {
    // String updateEmpleadoQuery = String.format("UPDATE empleados SET categoria =
    // %d WHERE dni = '%s'", nuevaCategoria, dni);
    // String selectEmpleadoQuery = String.format("SELECT nombre, sexo, anyos FROM
    // empleados WHERE dni = '%s'", dni);

    // try (Connection conn = getConnection();
    // Statement stmt = conn.createStatement()) {

    // // 1. Actualizar la categoría del empleado
    // stmt.executeUpdate(updateEmpleadoQuery);

    // // 2. Obtener los datos del empleado
    // ResultSet rs = stmt.executeQuery(selectEmpleadoQuery);

    // if (rs.next()) {
    // String nombre = rs.getString("nombre");
    // char sexo = rs.getString("sexo").charAt(0);
    // int anyos = rs.getInt("anyos");

    // // 3. Calcular el nuevo sueldo
    // int nuevoSueldo = calcularSueldo(nuevaCategoria, anyos);

    // // 4. Actualizar el sueldo en la tabla nominas
    // String updateNominaQuery = String.format("UPDATE nominas SET sueldo = %d
    // WHERE dni = '%s'", nuevoSueldo, dni);
    // stmt.executeUpdate(updateNominaQuery);
    // }

    // // 5. Actualizar todos los datos en los documentos
    // actualizarEmpleadosTxt();
    // actualizarSueldosDat();

    // System.out.println("Categoría modificada para el empleado con DNI: " + dni +
    // ". Nueva categoría: " + nuevaCategoria);
    // } catch (DatosNoCorrecosException e) {
    // System.err.println("Error en los datos: " + e.getMessage());
    // throw e;
    // }
    // }

    // Método principal para pruebas
    public static void main(String[] args) {
        AltasEmpleados ae = new AltasEmpleados();

        try {
            // Dar de alta a empleados desde un archivo
            // ae.altaEmpleado("empleadosNuevos.txt");

            // Incrementar los años trabajados de un empleado
            // ae.incrementarAnyosEmpleado("32000031R");

            // Modificar la categoría de un empleado
            // ae.modificarCategoriaEmpleado("32000032G", 9);

            ae.mostrarMenu();
        } catch (SQLException | IOException | DatosNoCorrecosException e) {
            e.printStackTrace();
        }
    }
}
