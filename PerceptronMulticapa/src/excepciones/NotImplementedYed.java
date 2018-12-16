package excepciones;

/**
 *
 * Para las funciones no implementadas en el Perceptron Builder.
 * Son muchas las funciones que hay que hacer
 * 
 * @author Adrián
 */
public class NotImplementedYed extends RuntimeException {
    public NotImplementedYed() {}
    
    public NotImplementedYed(String e){
        super (e);
    }
    
    public NotImplementedYed(Throwable c) {
        super (c);
    }
    
    public NotImplementedYed(String e,Throwable c) {
        super (e,c);
    }
}
