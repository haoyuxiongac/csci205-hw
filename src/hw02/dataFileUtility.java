/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 21, 2017
  * Time: 5:20:32 PM *
  * Project: csci205_hw
  * Package: hw02
  * File: dataFileUtility
  * Description:
  *
  * ****************************************
 */
package hw02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Utility class for handling data file inputs for training and testing s
 *
 * @author Jingya
 */
public final class dataFileUtility {

    /**
     * Read input file and handles exceptions.
     *
     * @return Scanner Scanner from the input file.
     */
    public static Scanner readInputFile(Scanner in) {

        String filename;
        Scanner fScanner;

        do {
            filename = in.next();

            try {
                File f = new File(filename);
                fScanner = new Scanner(f);
                break;

            } catch (FileNotFoundException e) {
                System.out.println("File not found, please try again!");
                System.out.print("Filename: ");
            }
        } while (true);

        return fScanner;
    }

    /**
     * Process input file, and store the information in a 2D array.
     *
     * @param fScanner
     * @return double[][] 2D array containing the input data
     */
    public static double[][] processInputFile(Scanner in, Scanner fScanner) {

        System.out.println("Does input file contain a header? ");
        System.out.println("\t1. Yes");
        System.out.println("\t2. No");

        int header = inputOptionUtility.oneOrTwo(in);

        if (header == 1) {
            fScanner.nextLine();
        }

        ArrayList<double[]> inputArray2D = new ArrayList<double[]>();

        while (fScanner.hasNextLine()) {
            String line = fScanner.nextLine();
            String[] parts = line.split(",");
            double[] ints = new double[parts.length];
            for (int i = 0; i < parts.length; i++) {
                ints[i] = Double.parseDouble(parts[i]);
            }
            inputArray2D.add(ints);
        }

        double[][] input2D = new double[inputArray2D.size()][];

        for (int i = 0; i < inputArray2D.size(); i++) {
            input2D[i] = inputArray2D.get(i);
        }
        return input2D;
    }

    public static double[][] processTestFile(int numIN, double[][] input2D) {

        if (input2D[0].length > numIN) {
            double[][] test2D = new double[input2D.length][];
            for (int i = 0; i < input2D.length; i++) {
                test2D[i] = Arrays.copyOfRange(input2D[i], 0, numIN);
            }
            return test2D;
        }
        else {
            return input2D;
        }
    }
}
