package excepciones;

/**
 *
 * Error que se lanza cuando das un número inferior de outpus que el que realmente tiene el perceptrón
 * Solo se lanza cuando le estas enseñando
 * 
 * @author Adrián
 */
public class IncorrectOutputSize extends RuntimeException {
    public IncorrectOutputSize() {}
    
    public IncorrectOutputSize(String e){
        super (e);
    }
    
    public IncorrectOutputSize(Throwable c) {
        super (c);
    }
    
    public IncorrectOutputSize(String e,Throwable c) {
        super (e,c);
    }
}