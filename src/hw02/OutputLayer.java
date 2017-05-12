/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 6, 2017
  * Time: 12:37:32 AM *
  * Project: csci205_hw
  * Package: hw01
  * File: OutputLayer
  * Description:
  *
  * ****************************************
 */
package hw02;

/**
 *
 * @author haoyuxiong
 */
public class OutputLayer extends Layer implements java.io.Serializable {

    /**
     *
     * @param numOfPrevNodes
     */
    public OutputLayer(int numOfPrevNodes) {

        super(numOfPrevNodes, 1);

    }

    /**
     * classify the layer and the single output node associate with it, then
     * return the result
     *
     * @param data
     * @return double returns the classification result of the output node
     */
    public double classify_OutputNode(double[] data) {
        double fGets;
        fGets = this.getNodeAtNo(0).classify_Perceptron(data);
        this.getfNets()[0] = fGets;
        return fGets;
    }

    /**
     * Using the classified actual output and the expected output to calculate
     * the little delta for the output node
     *
     * @param expected
     * @param actual
     * @return double returns the little delta calculated for the output node
     */
    public double calculateLittleDeltaOfOutputLayer(double expected,
                                                    double actual) {
        return actual * (1 - actual) * (expected - actual);
    }

    /**
     *
     * This function takes in the actual and expected output of the output node,
     * returns the calculated little delta list, which actually only includes
     * one of the output node
     *
     * @param actual
     * @param expected
     * @param fNets
     * @return double[] returns the little delta list
     */
    public double[] train_OutputNode(double actual, double expected,
                                     double[] fNets) {
        double[] returnLittleDelta = new double[1];
        double LittleDeltaOfOutputNode = this.calculateLittleDeltaOfOutputLayer(
                expected,
                actual);
        returnLittleDelta[0] = LittleDeltaOfOutputNode;
        this.getNodeAtNo(0).train_Perceptron(fNets,
                                             LittleDeltaOfOutputNode);
        //System.out.printf("%f, %f\n", expected, actual);
        return returnLittleDelta;
    }

}
