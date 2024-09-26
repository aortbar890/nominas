package nominas.Laboral;

public class CalculaNominas {
    
    public static void main(String[] args) throws DatosNoCorrecosException {
        try {
            Empleado emp1 = new Empleado(7, 12, "32000032G", "James Cosling", 'M');
            Empleado emp2 = new Empleado("32000031R", "Ada Lovelace", 'F');
            CalculaNominas.escribe(emp1, emp2);
            emp2.incrAnyos();
            emp1.setCategoria(9);
            System.out.println("Despues de la modificación:-----------------------");
            CalculaNominas.escribe(emp1, emp2);
            
        } catch (DatosNoCorrecosException e) {
            throw new DatosNoCorrecosException("Datos no correctos");
        }
    }
    
    private static void escribe(Empleado emp1, Empleado emp2) {
        System.out.println(emp1.imprime() + " , sueldo: " + Nomina.sueldo(emp1));
        System.out.println(emp2.imprime() + " , sueldo: " + Nomina.sueldo(emp2));
    }
    
}

