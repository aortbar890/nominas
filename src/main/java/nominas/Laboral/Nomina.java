package nominas.Laboral;

/**
 * Contiene métodos relacionados con la nómina de empleados.
 */
public class Nomina {

    /** El sueldo base para cada categoría de empleado. */
    private static final int SUELDO_BASE [] = {50000, 70000, 90000, 110000, 130000, 150000, 170000, 190000, 210000, 230000};

    /**
     * Calcula el sueldo total de un empleado basado en su categoría y años de servicio.
     * 
     * @param emp El empleado para el cual se calcula el sueldo.
     * @return El sueldo total del empleado.
     */
    public static int sueldo(Empleado emp) {
        return SUELDO_BASE[emp.getCategoria() - 1] + 5000 * emp.anyos;
    }
}
