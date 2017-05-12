/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 6, 2017
  * Time: 12:48:20 AM *
  * Project: csci205_hw
  * Package: hw01
  * File: Layer
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
public class Layer {

    private int numOfPrevNodes;
    private int numOfNodes;
    private ArrayList<Perceptron> Nodes;
    private double minSSE;
    private double[] fNets;

    /**
     * The constructor for the Layer class
     *
     * @param numOfPrevNodes
     * @param numOfNodes
     */
    public Layer(int numOfPrevNodes, int numOfNodes) {
        this.numOfNodes = numOfNodes;
        this.numOfPrevNodes = numOfPrevNodes;
        this.Nodes = new ArrayList<Perceptron>();
        for (int i = 0; i < numOfNodes; i++) {
            this.Nodes.add(new Perceptron(numOfPrevNodes));

        }
        fNets = new double[numOfNodes];

    }

    /**
     *
     * @return double[] this returns the set of f(net) after classification
     */
    public double[] getfNets() {
        return fNets;
    }

    /**
     *
     * @param index
     * @return double this returns the calculated f(net) at inputted index after
     * classification
     */
    public double getFnetsAtNoNode(int index) {
        return this.fNets[index];
    }

    /**
     *
     * @param fNets
     */
    public void setfNets(double[] fNets) {
        this.fNets = fNets;
    }

    /**
     *
     * @return int this returns the number of previous node as defined by inputs
     */
    public int getNumOfPrevNodes() {
        return numOfPrevNodes;
    }

    /**
     *
     * @param numOfPrevNodes
     */
    public void setNumOfPrevNodes(int numOfPrevNodes) {
        this.numOfPrevNodes = numOfPrevNodes;
    }

    /**
     *
     * @return int this returns the number of Nodes
     */
    public int getNumOfNodes() {
        return numOfNodes;
    }

    /**
     *
     * @param numOfNodes
     */
    public void setNumOfNodes(int numOfNodes) {
        this.numOfNodes = numOfNodes;
    }

    /**
     *
     * @return ArrayList returns the nodes
     */
    public ArrayList<Perceptron> getNodes() {
        return Nodes;
    }

    /**
     *
     * @param Nodes
     */
    public void setNodes(ArrayList<Perceptron> Nodes) {
        this.Nodes = Nodes;
    }

    /**
     * Get the node at the inputted index
     *
     * @param index
     * @return Perceptron this returns the perceptron at the indicated index
     */
    public Perceptron getNodeAtNo(int index) {
        return this.getNodes().get(index);

    }

    /**
     *
     * @return double the minimum SSE that the user inputted in.
     */
    public double getMinSSE() {
        return minSSE;
    }

    /**
     *
     * @param minSSE
     */
    public void setMinSSE(double minSSE) {
        this.minSSE = minSSE;
    }

    /**
     * classify the layer and get the sets of results for each of the nodes
     *
     * @param data
     * @return double[] returns the f(net) after the classification
     */
    public double[] classify_Layer(double[] data
    ) {
        double fgets;

        for (int i = 0; i < this.numOfNodes; i++) {
            fgets = this.Nodes.get(i).classify_Perceptron(data);
            this.fNets[i] = fgets;
            //System.out.printf("fgets, %f\n", fgets);
        }
        return this.getfNets();

    }

}
