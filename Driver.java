import java.io.IOException;
import java.util.Arrays;

public class Driver {
    public static void main(String [] args) throws IOException {
        Polynomial p = new Polynomial();
        System.out.println(p.evaluate(3));
        double [] c1 = {3,3};
        int[] a1 = {0,1};
        Polynomial p1 = new Polynomial(c1, a1);
        double [] c2 = {-3,-3};
        int[] a2 = {0,1};
        Polynomial p2 = new Polynomial(c2, a2);
        Polynomial s = p1.add(p2);

        System.out.println("s(1) = " + s.evaluate(1));

        if(s.hasRoot(1)) {
            System.out.println("1 is a root of s");
        }
        else {
            System.out.println("1 is not a root of s");
        }

        Polynomial ad = p1.add(p2);
        System.out.println(Arrays.toString(ad.arr) + ", " + Arrays.toString(ad.arrInt));


        Polynomial mu = p1.multiply(p2);
        System.out.println(Arrays.toString(mu.arr) + ", " + Arrays.toString(mu.arrInt));

        p2.saveToFile("polynomial.txt");
        Polynomial q = new Polynomial("polynomial.txt");
        System.out.println(Arrays.toString(q.arr) + ", " + Arrays.toString(q.arrInt));
    }
}