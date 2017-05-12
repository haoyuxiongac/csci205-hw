/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet.data
 * File: UnlabeledInstances
 * Description:
 *
 * ****************************************
 */
package hw03.model.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author brk009
 */
public class UnlabeledInstances extends ArrayList<UnlabeledInstance> {

    /**
     * Construct a new empty Data object
     */
    public UnlabeledInstances() {
        super();
    }

    /**
     * Construct a new Data object that encapsulates an array of Instances.
     *
     * @param theData
     */
    public UnlabeledInstances(ArrayList<UnlabeledInstance> theData) {
        super(theData);
    }

    /**
     * Construct a new Data object from a CSV file of numbers.
     *
     * @param sCSVFileName
     * @throws FileNotFoundException
     */
    public UnlabeledInstances(String sCSVFileName, boolean skipHeader) throws FileNotFoundException {
        this();

        Scanner in = new Scanner(new File(sCSVFileName));
        while (in.hasNextLine()) {
            String s = in.nextLine();
            // Skip over blank lines
            if (s.length() == 0) {
                continue;
            }

            // If there is a header in the file, then skip it
            if (skipHeader) {
                skipHeader = false;
                continue;
            }

            String[] tokens = s.split(",");
            UnlabeledInstance xVec = new UnlabeledInstance();
            for (String sToken : tokens) {
                Double num = Double.parseDouble(sToken);
                xVec.add(num);
            }
            this.add(xVec);
        }
    }

    /**
     * @return the number of Instances in the dataset
     */
    public int getNumInstances() {
        return this.size();
    }

    @Override
    public String toString() {
        return "Instances{" + super.toString() + '}';
    }

}
