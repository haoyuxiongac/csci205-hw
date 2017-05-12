/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: Haoyu Xiong and Jingya Wu
  * Date: Mar 3, 2017
  * Time: 8:53:25 PM *
  * Project: csci205_hw
  * Package: Assignment01
  * File: SUB_ANN
  * Description:
  *
  * ****************************************
 */
package hw01;

import java.util.ArrayList;

/**
 *
 * @author haoyuxiong
 */
public class SUB_ANN {

    private ArrayList<HiddenLayer> hiddenLayerList;

    private OutputLayer outputLayer;

    private int numOfLayers;

    private int numOfLayerLayers;

    private int numIp;

    private int numNodesInHiddenLayers;

    /**
     * Constructor for SUB_ANN class
     *
     * @param numInp
     * @param numOfLayers
     * @param numNodesInHiddenLayers
     */
    public SUB_ANN(int numInp, int numOfLayers,
                   int numNodesInHiddenLayers) {
        System.out.println("Constructor");
        this.numOfLayers = numOfLayers;

        this.numOfLayerLayers = this.numOfLayers - 1;

        this.numIp = numInp;

        this.numNodesInHiddenLayers = numNodesInHiddenLayers;

        this.hiddenLayerList = new ArrayList<HiddenLayer>();

        if (numOfLayers > 2) {
            this.outputLayer = new OutputLayer(numNodesInHiddenLayers);
        }
        else {
            this.outputLayer = new OutputLayer(numIp);
        }

        int numPrevNodes = numInp;

        for (int i = 0; i < numOfLayers - 2; i++) {
            this.hiddenLayerList.add(new HiddenLayer(numPrevNodes,
                                                     numNodesInHiddenLayers));
            numPrevNodes = numNodesInHiddenLayers;

        }
        /*
        if (numOfLayers - 2 > 0) {

            this.hiddenLayerList.add(new HiddenLayer(numPrevNodes, 1));
        }
         */
    }

    /**
     * This function calculates the f(net) of all the nodes in ANN and outputs
     *
     * @param inputDataAtRowJ
     * @param no
     * @return double returns the error of this iteration
     */
    public double Feed_Forward_Train_SUB_ANN(
            double[] inputDataAtRowJ, int no) {
        double[] outputDataRow = inputDataAtRowJ;

        for (int z = 0; z < this.numOfLayerLayers - 1; z++) {

            outputDataRow = this.hiddenLayerList.get(z).classify_Layer(
                    outputDataRow);

        }
        double actualOutput = this.outputLayer.classify_OutputNode(
                outputDataRow);

        double expectedOutput = inputDataAtRowJ[this.numIp + no];
        //System.out.printf("%f, %f\n", expectedOutput, actualOutput);
        return expectedOutput - actualOutput;
    }

    /**
     * This function goes back after feed_forward and change the weights of
     * nodes based on little delta accordingly
     *
     * @param actual
     * @param expected
     * @param inputDataAtRowJ
     */
    public void Back_Propagation_Train_SUB_ANN(double actual, double expected,
                                               double[] inputDataAtRowJ) {
        double[] outputDataRow;
        ArrayList<Perceptron> prevLayerNodes;
        double[] prevFgets;
        if (this.numOfLayerLayers - 1 > 0) {
            //train OutputLayer
            outputDataRow = this.outputLayer.train_OutputNode(
                    actual,
                    expected,
                    this.hiddenLayerList.get(
                            this.numOfLayerLayers - 2).getfNets());

            //between OutputLayer and firstHiddenLayer
            prevLayerNodes = this.outputLayer.getNodes();

            for (int z = this.numOfLayerLayers - 2; z > 0; z--) {
                outputDataRow = this.hiddenLayerList.get(
                        z).train_HiddenLayer(
                                outputDataRow,
                                prevLayerNodes,
                                this.hiddenLayerList.get(z - 1).getfNets()
                        );
                prevLayerNodes = this.hiddenLayerList.get(z).getNodes();

            }

            outputDataRow = this.hiddenLayerList.get(0).train_HiddenLayer(
                    outputDataRow, prevLayerNodes, inputDataAtRowJ);

        }
        else {
            this.outputLayer.train_OutputNode(actual,
                                              expected,
                                              inputDataAtRowJ
            );

        }

    }

    /**
     *
     * @param data
     * @param no
     */
    public void train_SUB_ANN(double[][] data, int no) {
        int row = data.length;
        double SSE;
        double error;
        double actualOutput;
        double expectedOutput;
        do {
            SSE = 0;

            for (int j = 0; j < row; j++) {

                error = this.Feed_Forward_Train_SUB_ANN(data[j], no);
                SSE += Math.pow(error, 2);
                //System.out.printf("Error: %f, SSE: %f\n", error, SSE);
                expectedOutput = data[j][this.numIp + no];
                actualOutput = expectedOutput - error;

                this.Back_Propagation_Train_SUB_ANN(actualOutput, expectedOutput,
                                                    data[j]);

            }

        } while (SSE >= ANN.minSSE);
    }

    /**
     *
     * @param data
     * @return
     */
    public double Classify_SUB_ANN(double[] data) {
        double[] outputDataRow = data;

        for (int z = 0; z < this.numOfLayerLayers - 1; z++) {

            outputDataRow = this.hiddenLayerList.get(z).classify_Layer(
                    outputDataRow
            );

        }
        double actualOutput = this.outputLayer.classify_OutputNode(
                outputDataRow);

        return actualOutput;
    }

    /**
     *
     * @return
     */
    public ArrayList<HiddenLayer> getHiddenLayerList() {
        return hiddenLayerList;
    }

    /**
     *
     * @param hiddenLayerList
     */
    public void setHiddenLayerList(ArrayList<HiddenLayer> hiddenLayerList) {
        this.hiddenLayerList = hiddenLayerList;
    }

    /**
     *
     * @return
     */
    public OutputLayer getOutputLayer() {
        return outputLayer;
    }

    /**
     *
     * @param outputLayer
     */
    public void setOutputLayer(OutputLayer outputLayer) {
        this.outputLayer = outputLayer;
    }

    /**
     *
     * @return
     */
    public int getNumOfLayers() {
        return numOfLayers;
    }

    /**
     *
     * @param numOfLayers
     */
    public void setNumOfLayers(int numOfLayers) {
        this.numOfLayers = numOfLayers;
    }

    /**
     *
     * @return
     */
    public int getNumOfLayerLayers() {
        return numOfLayerLayers;
    }

    /**
     *
     * @param numOfLayerLayers
     */
    public void setNumOfLayerLayers(int numOfLayerLayers) {
        this.numOfLayerLayers = numOfLayerLayers;
    }

    /**
     *
     * @return
     */
    public int getNumIp() {
        return numIp;
    }

    /**
     *
     * @param numIp
     */
    public void setNumIp(int numIp) {
        this.numIp = numIp;
    }

    /**
     *
     * @return
     */
    public int getNumNodesInHiddenLayers() {
        return numNodesInHiddenLayers;
    }

    /**
     *
     * @param numNodesInHiddenLayers
     */
    public void setNumNodesInHiddenLayers(int numNodesInHiddenLayers) {
        this.numNodesInHiddenLayers = numNodesInHiddenLayers;
    }

}
