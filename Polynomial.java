public class Polynomial {
    double[] arr;

    public Polynomial() {
        arr = new double[1];
        arr[0] = 0;
    }

    public Polynomial(double[] arr1) {
        arr = new double[arr1.length];
        arr = arr1.clone();
    }

    public Polynomial add(Polynomial p) {
        for(int i = 0; i < arr.length; i++) {
            p.arr[i] += arr[i];
        }
        return p;
    }

    public double evaluate(double x) {
        double result = 0;
        for(int i = 0; i < arr.length; i++) {
            result += arr[i] * Math.pow(x, i);
        }
        return result;
    }

    public boolean hasRoot(double root) {
        return evaluate(root) == 0;
    }
}