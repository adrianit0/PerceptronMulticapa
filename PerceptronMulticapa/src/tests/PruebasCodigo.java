package tests;

/**
 *
 * @author Adrián
 */
public class PruebasCodigo {

    public static void main(String[] args) {
        TestAND test1 = new TestAND();
        TestOR test2 = new TestOR();
        TestXOR test3 = new TestXOR();
        TestNOT test4 = new TestNOT();
        PiedraPapelTijeras test5 = new PiedraPapelTijeras();

        test1.start();
        test2.start();
        test3.start();
        test4.start();
    }
}
