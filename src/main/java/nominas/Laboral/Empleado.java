package nominas.Laboral;

/**
 * Representa a un empleado que extiende de Persona y tiene una categoría y años de servicio.
 */
public class Empleado extends Persona {
    
    /** La categoría del empleado. */
    private int categoria;
    
    /** Los años de servicio del empleado. */
    public int anyos;

    /**
     * Crea una nueva instancia de Empleado con DNI, nombre y sexo.
     * 
     * @param dni El DNI del empleado.
     * @param nombre El nombre del empleado.
     * @param sexo El sexo del empleado ('M' para masculino, 'F' para femenino).
     */
    public Empleado(String dni, String nombre, char sexo) {
        super(dni, nombre, sexo);
        categoria = 1;
        anyos = 0;
    }

    /**
     * Crea una nueva instancia de Empleado con años de servicio, categoría, DNI, nombre y sexo.
     * 
     * @param anyos Los años de servicio del empleado.
     * @param categoria La categoría del empleado.
     * @param dni El DNI del empleado.
     * @param nombre El nombre del empleado.
     * @param sexo El sexo del empleado ('M' para masculino, 'F' para femenino).
     * @throws DatosNoCorrecosException Si la categoría o los años de servicio son inválidos.
     */
    public Empleado(int anyos, int categoria, String dni, String nombre, char sexo) throws DatosNoCorrecosException {
        super(dni, nombre, sexo);
        if (categoria < 0 || categoria > 10 || anyos < 0) {
            throw new DatosNoCorrecosException();
        }
        this.anyos = anyos;
        this.categoria = categoria;
    }

    /**
     * Establece la categoría del empleado.
     * 
     * @param categoria La nueva categoría del empleado.
     */
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene la categoría del empleado.
     * 
     * @return La categoría del empleado.
     */
    public int getCategoria() {
        return categoria;
    }

    /**
     * Incrementa los años de servicio del empleado en uno.
     */
    public void incrAnyos() {
        anyos++;
    }

    /**
     * Imprime una representación en cadena del empleado.
     * 
     * @return Una cadena que contiene el nombre, DNI, sexo, categoría y años de servicio del empleado.
     */
    public String imprime() {
        return nombre + " " + dni + " " + sexo + " " + categoria + " " + anyos;
    }
}
