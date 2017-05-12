/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet.data
 * File: LabeledInstances
 * Description:
 *
 * ****************************************
 */
package hw03.model.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Encapsulates a list of labeled instances. It is a toss up to know whether
 * this should be extended from ArrayList<LabeledInstance> or whether this
 * should extend UnlabeledInstances. Either approach would have worked.
 *
 * @author brk009
 */
public class LabeledInstances extends ArrayList<LabeledInstance> {

    public LabeledInstances() {

    }

    /**
     * Create an object containing all of the Labeled instances. The last
     * parameter is a variable argument list of indices that contain the target
     * for the class
     *
     * @param sCSVFileName - String representing a CSV file
     * @param targetIndices - A variable list of target indices representing the
     * index position(s) of the target class.
     * @throws FileNotFoundException
     */
    public LabeledInstances(String sCSVFileName, boolean skipHeader,
                            Integer... targetIndices) throws FileNotFoundException {
        this();

        Scanner in = new Scanner(new File(sCSVFileName));

        // First, make sure the targetIndices are sorted
        Arrays.sort(targetIndices);
        ArrayList<Integer> targetIndexList = new ArrayList<>();
        for (Integer t : targetIndices) {
            targetIndexList.add(t);
        }

        // Read through each line...
        while (in.hasNextLine()) {
            String s = in.nextLine();
            // Skip over blank lines
            if (s.length() == 0) {
                continue;
            }

            // If there is a header, then skip it
            if (skipHeader) {
                skipHeader = false;
                continue;
            }

            // Split the CSV line into tokens
            String[] tokens = s.split(",");

            // Set up the labeled instance and the target array
            LabeledInstance xVec = new LabeledInstance();
            ArrayList<Double> targets = new ArrayList<Double>(
                    targetIndices.length);

            for (int i = 0; i < tokens.length; i++) {

                // Parse the string to a double
                Double num = Double.parseDouble(tokens[i]);

                // Iterate through the list of target indices...
                if (targetIndexList.contains(i)) {
                    targets.add(num);
                }
                else {
                    xVec.add(num);
                }
            }
            xVec.setTarget(targets);
            this.add(xVec);
        }
    }

}
