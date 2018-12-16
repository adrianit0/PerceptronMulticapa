package excepciones;

/**
 *
 * Error que se lanza cuando das un número inferior de inputs que el que realmente tiene el perceptrón
 * Se lanza tanto al enseñarle como al ejecutarlo
 * 
 * @author Adrián
 */
public class IncorrectInputSize extends RuntimeException {
    public IncorrectInputSize() {}
    
    public IncorrectInputSize(String e){
        super (e);
    }
    
    public IncorrectInputSize(Throwable c) {
        super (c);
    }
    
    public IncorrectInputSize(String e,Throwable c) {
        super (e,c);
    }
}
