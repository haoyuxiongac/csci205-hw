/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet.data
 * File: UnlabeledInstance
 * Description:
 *
 * ****************************************
 */
package hw03.model.data;

import java.util.ArrayList;

/**
 *
 * @author brk009
 */
public class UnlabeledInstance extends ArrayList<Double> {

    public UnlabeledInstance() {
        super();
    }

    /**
     * Initialize an Instance with a array of values
     *
     * @param xVec
     */
    public UnlabeledInstance(double[] xVec) {
        super(xVec.length);
        for (double x : xVec) {
            this.add(x);
        }
    }

}
