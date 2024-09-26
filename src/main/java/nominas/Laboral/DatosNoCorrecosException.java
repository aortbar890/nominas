package nominas.Laboral;

/**
 * Excepción lanzada cuando los datos proporcionados para un empleado son incorrectos.
 */
public class DatosNoCorrecosException extends Exception {

    /**
     * Crea una nueva instancia de DatosNoCorrecosException sin un mensaje específico.
     */
    public DatosNoCorrecosException() {
    }

    /**
     * Crea una nueva instancia de DatosNoCorrecosException con un mensaje específico.
     * 
     * @param message El mensaje de la excepción.
     */
    public DatosNoCorrecosException(String message) {
        super(message);
    }
}
