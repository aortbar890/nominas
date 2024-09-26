package nominas.Laboral;

/**
 * Representa a una persona con un nombre, un DNI y un sexo.
 */
public class Persona {
    /** El nombre de la persona. */
    public String nombre;
    
    /** El DNI de la persona. */
    public String dni;
    
    /** El sexo de la persona ('M' para masculino, 'F' para femenino). */
    public char sexo;

    /**
     * Crea una nueva instancia de Persona con DNI, nombre y sexo.
     * 
     * @param dni El DNI de la persona.
     * @param nombre El nombre de la persona.
     * @param sexo El sexo de la persona ('M' para masculino, 'F' para femenino).
     */
    public Persona(String dni, String nombre, char sexo) {
        this.dni = dni;
        this.nombre = nombre;
        this.sexo = sexo;
    }

    /**
     * Crea una nueva instancia de Persona con nombre y sexo.
     * 
     * @param nombre El nombre de la persona.
     * @param sexo El sexo de la persona ('M' para masculino, 'F' para femenino).
     */
    public Persona(String nombre, char sexo) {
        this.nombre = nombre;
        this.sexo = sexo;
    }

    /**
     * Establece el DNI de la persona.
     * 
     * @param dni El nuevo DNI de la persona.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Imprime una representación en cadena de la persona.
     * 
     * @return Una cadena que contiene el nombre y el DNI de la persona.
     */
    public String Imprime() {
        return nombre + " " + dni;
    }
}
