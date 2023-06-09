import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

public class Polynomial {
    double[] arr;
    int[] arrInt;

    public Polynomial() {
        arr = null;
        arrInt = null;
    }

    public Polynomial(double[] arr1, int[] arrInt1) {
        arr = new double[arr1.length];
        arr = arr1.clone();
        arrInt = new int[arrInt1.length];
        arrInt = arrInt1.clone();
    }

    public Polynomial(String filename) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(filename));
        String text = scan.nextLine();
        String[] arrTerms = text.split("((?=[-+]))"); // Keep the delimiter when splitting
        arr = new double[arrTerms.length];
        arrInt = new int[arrTerms.length];
        
        for(int i = 0; i < arrTerms.length; i++) {
            if(!arrTerms[i].contains("x")) {
                arr[i] = Double.parseDouble(arrTerms[i]);
                arrInt[i] = 0;
            }
            else {
                arr[i] = Double.parseDouble(arrTerms[i].substring(0, arrTerms[i].indexOf("x")));
                try {
                    arrInt[i] = Integer.parseInt(arrTerms[i].substring(arrTerms[i].lastIndexOf("x") + 1));
                } catch(Exception e) {
                    arrInt[i] = 1;
                }
            }
        }
        scan.close();
        sort(arr, arrInt);
    }

    public void sort(double[] parr, int[] parrInt) {
        // sort arrInt and move the arr values accordingly
        for(int i = 0; i < parr.length; i++) {
            for(int j = 0; j < parr.length; j++) {
                if(parrInt[i] > parrInt[j]) {
                    int temp = parrInt[i];
                    parrInt[i] = parrInt[j];
                    parrInt[j] = temp;

                    double temp1 = parr[i];
                    parr[i] = parr[j];
                    parr[j] = temp1;
                }
            }
        }
    }

    public int inArrayInt(int[] parrInt, int val) {
        for(int i = 0; i < parrInt.length; i++) {
            if(parrInt[i] == val) {
                return i;
            }
        }
        return -1;
    }

    public Polynomial add(Polynomial p) {
        if(p.arr[0] == 0) {
            return new Polynomial(arr, arrInt);
        }
        if(arr[0] == 0) {
            return new Polynomial(p.arr, p.arrInt);
        }

        // Find the length for the resulting array
        int count = arr.length + p.arr.length;

        double[] result = new double[count];
        int[] resultInt = new int[count];
        int k = 1;
        int idx;
        int zeros = 0;
        for(int i = 0; i < count; i++) {
            if(i < arr.length) {
                idx = inArrayInt(resultInt, arrInt[i]);
                if(idx != -1) {
                    if(result[idx] + arr[i] == 0) {
                        zeros++;
                    }
                    result[idx] += arr[i];
                }
                else {
                    result[k] = arr[i];
                    resultInt[k] = arrInt[i];
                    k++;
                }
            }

            if(i < p.arr.length) {
                idx = inArrayInt(resultInt, p.arrInt[i]);
                if(idx != -1) {
                    if(result[idx] + p.arr[i] == 0) {
                        zeros++;
                    }
                    result[idx] += p.arr[i];
                }
                else {
                    result[k] = p.arr[i];
                    resultInt[k] = p.arrInt[i];
                    k++;
                }
            }
        }

        sort(result, resultInt);

        // dispose unused values and 0's
        double[] newArr = new double[k - zeros];
        int[] newArrInt = new int[k - zeros];
        int zero = 0;
        for(int i = 0; i < k - zeros; i++) {
            if(result[i] == 0) {
                zero++;
            }
            newArr[i] = result[i + zero];
            newArrInt[i] = resultInt[i + zero];
        }

        if(newArr.length == 0) {
            newArr = new double[1];
            newArrInt = new int[1];
            newArr[0] = 0;
            newArrInt[0] = 0;
        }

        return new Polynomial(newArr, newArrInt);
    }

    public double evaluate(double x) {
        double result = 0;
        try {
            for(int i = 0; i < arr.length; i++) {
                result += arr[i] * Math.pow(x, arrInt[i]);
            }
        } catch(Exception e) {
            return 0;
        }
        
        return result;
    }

    public boolean hasRoot(double root) {
        return evaluate(root) == 0;
    }

    public Polynomial multiply(Polynomial p) {
        if(p.arr[0] == 0) {
            return new Polynomial(p.arr, p.arrInt);
        }
        if(arr[0] == 0) {
            return new Polynomial(arr, arrInt);
        }

        int count = p.arr.length * arr.length;

        double[] result = new double[count];
        int[] resultInt = new int[count];

        int k = 0;
        int done = 0;
        int zeros = 0;

        // Multiply and add all like terms
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < p.arr.length; j++) {
                done = 0;
                for(int h = 0; h < k; h++) {
                    if(resultInt[h] == arrInt[i] + p.arrInt[j]) {
                        result[h] += arr[i] * p.arr[j];
                        if(result[h] == 0) {
                            zeros++;
                        }
                        done = 1;
                    }
                }
                if(done == 0) {
                    result[k] = arr[i] * p.arr[j];
                    resultInt[k] = arrInt[i] + p.arrInt[j];
                    k++;
                }
            }
        }

        // Skip values of 0 and dispose unused values
        double[] newArr = new double[k - zeros];
        int[] newArrInt = new int[k - zeros];
        int zero = 0;
        for(int i = 0; i < k - zeros; i++) {
            if(result[i] == 0) {
                zero++;
            }
            newArr[i] = result[i + zero];
            newArrInt[i] = resultInt[i + zero];
        }

        sort(newArr, newArrInt);

        return new Polynomial(newArr, newArrInt);
    }

    public void saveToFile(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
        FileWriter writer = new FileWriter(file, false);
        String s = "";
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] > 0 && i != 0) {
                s += "+";
            }
            s += String.valueOf(arr[i]);
            if(arrInt[i] > 0) {
                s += "x";
                s += String.valueOf(arrInt[i]);
            }
        }
        writer.write(s);
        writer.close();
    }
}