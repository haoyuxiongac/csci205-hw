/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 5, 2017
  * Time: 10:13:10 PM *
  * Project: csci205_hw
  * Package: hw01
  * File: HiddenLayer
  * Description:
  *
  * ****************************************
 */
package hw02;

import java.util.ArrayList;

/**
 *
 * @author haoyuxiong
 */
public class HiddenLayer extends Layer implements java.io.Serializable {

    /**
     * Constructor for HiddenLayer
     *
     * @param numOfPrevNodes
     * @param numOfNodes
     */
    public HiddenLayer(int numOfPrevNodes, int numOfNodes) {
        super(numOfPrevNodes, numOfNodes);

    }

    /**
     * get list of f(net)s.
     *
     * @return
     */
    @Override
    public double[] getfNets() {
        return super.getfNets();
    }

    /**
     * Main method for training hidden layer.
     *
     * @param kLittleDeltas
     * @param prevLayerNodes
     * @param prevFgets
     * @return double[] little delta list of the layer
     */
    public double[] train_HiddenLayer(double[] kLittleDeltas,
                                      ArrayList<Perceptron> prevLayerNodes,
                                      double[] prevFgets) {
        double[] returnLittleDelta = new double[this.getNumOfNodes()];
        double littleDelta;

        for (int i = 0; i < this.getNumOfNodes(); i++) {
            littleDelta = calculateLittleDeltaAtNoNode(i, this.getFnetsAtNoNode(
                                                       i), prevLayerNodes,
                                                       kLittleDeltas);
            this.getNodeAtNo(i).train_Perceptron(prevFgets, littleDelta);
            returnLittleDelta[i] = littleDelta;

        }

        return returnLittleDelta;

    }

    /**
     * Calculates the little delta at the noth node of the layer
     *
     * @param no
     * @param fgets
     * @param prevLayerNodes
     * @param kLittleDeltas
     * @return double little delta
     */
    public double calculateLittleDeltaAtNoNode(int no, double fgets,
                                               ArrayList<Perceptron> prevLayerNodes,
                                               double[] kLittleDeltas) {
        double error = 0;
        for (int i = 0; i < prevLayerNodes.size(); i++) {
            error += (prevLayerNodes.get(i).getWeightAtIndex(no) * kLittleDeltas[i]);
        }
        return error * fgets * (1 - fgets);
    }

}
