package excepciones;

import java.io.IOException;

/**
 *
 * Se lanza cuando intentas cargas un perceptrón desde una ruta inválida.
 * 
 * @author Adrián
 */
public class PerceptronNotFoundException extends IOException {
    public PerceptronNotFoundException() {}
    
    public PerceptronNotFoundException(String e){
        super (e);
    }
    
    public PerceptronNotFoundException(Throwable c) {
        super (c);
    }
    
    public PerceptronNotFoundException(String e,Throwable c) {
        super (e,c);
    }
}
