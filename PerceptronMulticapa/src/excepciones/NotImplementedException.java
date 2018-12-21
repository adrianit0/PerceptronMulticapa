package excepciones;

/**
 *
 * Para las funciones no implementadas en el Perceptron Builder.
 * Son muchas las funciones que hay que hacer
 * 
 * @author Adrián
 */
public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {}
    
    public NotImplementedException(String e){
        super (e);
    }
    
    public NotImplementedException(Throwable c) {
        super (c);
    }
    
    public NotImplementedException(String e,Throwable c) {
        super (e,c);
    }
}
