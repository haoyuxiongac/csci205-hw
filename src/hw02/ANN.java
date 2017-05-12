/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 7, 2017
  * Time: 11:02:43 PM *
  * Project: csci205_hw
  * Package: hw01
  * File: ANN
  * Description:
  *
  * ****************************************
 */
package hw02;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Main class for the ANN structure, contains multiple outputs and layers.
 *
 * @author haoyuxiong
 */
public class ANN implements java.io.Serializable {

    /**
     * A list of subANN
     */
    private ArrayList<SUB_ANN> subANNList;

    /**
     * Number of inputs
     */
    private int numInp;

    /**
     * Number of layers
     */
    private int numLayers;

    /**
     * Number of nodes per hidden layer
     */
    private int numNodesInHiddenLayers;

    /**
     * Minimum SSE that controls the stop of the train iteration
     */
    public static double minSSE;

    /**
     * Number of outputs
     */
    private int numOut;

    public static int maxEpoch;

    private static PrintWriter out;

    public static int actFuncNum;

    /**
     * Constructor for ANN
     *
     * @param numInp
     * @param numOut
     * @param numOfLayers
     * @param numNodesInHiddenLayers
     * @param minSSE
     */
    public ANN(int numInp, int numOut, int numOfLayers,
               int numNodesInHiddenLayers, double minSSE, int maxEpoch,
               int actFuncNum) throws FileNotFoundException {
        this.numInp = numInp;
        this.numOut = numOut;
        ANN.actFuncNum = actFuncNum;
        this.numLayers = numOfLayers;
        this.numNodesInHiddenLayers = this.numNodesInHiddenLayers;
        ANN.minSSE = minSSE;
        this.maxEpoch = maxEpoch;
        subANNList = new ArrayList<SUB_ANN>();
        for (int i = 0; i < numOut; i++) {
            subANNList.add(new SUB_ANN(numInp, numOfLayers,
                                       numNodesInHiddenLayers));
        }
        this.out = new PrintWriter("ANNTrainingLog.csv");
    }

    public static PrintWriter getOut() {
        return out;
    }

    /**
     * Get the subANN list
     *
     * @return List of subANN
     */
    public ArrayList<SUB_ANN> getSubANNList() {
        return subANNList;
    }

    /**
     * Set the subANN list
     *
     * @param subANNList
     */
    public void setSubANNList(ArrayList<SUB_ANN> subANNList) {
        this.subANNList = subANNList;
    }

    /**
     * Get the number of inputs
     *
     * @return int number of inputs
     */
    public int getNumInp() {
        return numInp;
    }

    /**
     * Set the number of inputs
     *
     * @param numInp
     */
    public void setNumInp(int numInp) {
        this.numInp = numInp;
    }

    /**
     * Get number of nodes per hidden layer
     *
     * @return int number of nodes per hidden layer
     */
    public int getNumNodesInHiddenLayers() {
        return numNodesInHiddenLayers;
    }

    /**
     * Set number of nodes in hidden layer
     *
     * @param numNodesInHiddenLayers
     */
    public void setNumNodesInHiddenLayers(int numNodesInHiddenLayers) {
        this.numNodesInHiddenLayers = numNodesInHiddenLayers;
    }

    /**
     * Get minimum SSE
     *
     * @return double minimum SSE
     */
    public static double getMinSSE() {
        return minSSE;
    }

    /**
     * Set minimum SSE
     *
     * @param minSSE
     */
    public static void setMinSSE(double minSSE) {
        ANN.minSSE = minSSE;
    }

    /**
     * Get number of outputs
     *
     * @return int number of outputs
     */
    public int getNumOut() {
        return numOut;
    }

    /**
     * Set number of outputs
     *
     * @param numOut int number of outputs
     */
    public void setNumOut(int numOut) {
        this.numOut = numOut;
    }

    /**
     * Main method for ANN training, loops through the subANNs.
     *
     * @param data
     * @return List of subANNs
     * @throws java.io.FileNotFoundException
     */
    public ArrayList<SUB_ANN> Train_ANN(double[][] data) throws FileNotFoundException {
        this.out.write("ANN,Time and Date," + new Date().toString() + "\n");
        for (int i = 0; i < this.numOut; i++) {
            this.subANNList.get(i).train_SUB_ANN(data, i);
            this.out.write("Output" + i + "\n");
        }
        this.out.write("Training Ended\n");
        this.out.write("Time and Date," + new Date().toString());
        this.out.close();
        return subANNList;
    }

    /**
     * Main method for ANN classification.
     *
     * @param data
     * @return double[][] output data 2D array
     */
    public double[][] Classify_ANN(double[][] data) {
        double[][] output2DArray = new double[data.length][this.numOut];
        double output;
        StepActivationFunction step = new StepActivationFunction();
        SigmoidalActivationFunction sig = new SigmoidalActivationFunction();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < this.numOut; j++) {
                output = this.subANNList.get(j).Classify_SUB_ANN(data[i]);
                output2DArray[i][j] = output;

            }
        }
        return output2DArray;

    }
}
