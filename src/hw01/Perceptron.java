/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 3, 2017
  * Time: 9:31:02 PM *
  * Project: csci205_hw
  * Package: Assignment01
  * File: Perceptron
  * Description:
  *
  * ****************************************
 */
package hw01;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author haoyuxiong
 */
public class Perceptron {

    private ArrayList<Double> weights;
    private double theta;
    final static double alpha = 0.2;
    private int numInp;

    /**
     * Constructor for the Perceptron Object
     *
     * @param numInp
     */
    public Perceptron(int numInp) {
        this.numInp = numInp;

        this.weights = new ArrayList<Double>();
        Random randomDoubleGenerator = new Random();
        this.theta = (randomDoubleGenerator.nextDouble() - 0.5) * 4.8 / numInp;
        for (int i = 0; i < numInp; i++) {
            this.weights.add(
                    (randomDoubleGenerator.nextDouble() - 0.5) * 4.8 / numInp);
        }

    }

    /**
     * @return ArrayList returns the weights stored in the perceptron
     */
    public ArrayList<Double> getWeights() {
        return weights;
    }

    /**
     *
     * @return int returns the number of input, which alternatively is the
     * number of weight
     */
    public int getNumInp() {
        return numInp;
    }

    /**
     *
     * @param numInp
     */
    public void setNumInp(int numInp) {
        this.numInp = numInp;
    }

    /**
     *
     * @param weights
     */
    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }

    /**
     * This functions takes in the f(net)s from the left layer and the little
     * Delta from the right layer
     *
     * @param fgets
     * @param littleDelta
     */
    public void train_Perceptron(double[] fgets, double littleDelta) {
        double changeOfWeight;

        double changeOfTheta = alpha * (-1) * littleDelta;
        this.theta += changeOfTheta;

        //System.out.printf("theta,%f, change, %f\n", this.theta, changeOfTheta);
        for (int i = 0; i < this.getNumInp(); i++) {
            //System.out.printf("fgetsi, %f\n", fgets[i]);
            changeOfWeight = Perceptron.calculateDeltaWeight(fgets[i],
                                                             littleDelta);
            //System.out.printf("weight change, %f\n", changeOfWeight);
            this.changeWeightAtIndex(changeOfWeight, i);
        }

    }

    /**
     *
     * @return double the theta
     */
    public double getTheta() {
        return theta;
    }

    /**
     *
     * @param theta
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }

    /**
     * This function takes in the previous f(net) and the little delta and
     * subsequently calculate the supposed change in the weight and returns it
     *
     * @param prevFnet
     * @param littleDelta
     * @return double it returns the calculated change in the weight
     */
    public static double calculateDeltaWeight(double prevFnet,
                                              double littleDelta) {
        //System.out.printf("delta,%f\n", littleDelta);
        return Perceptron.alpha * prevFnet * littleDelta;
    }

    /**
     *
     * This function calculates the classification output based on the sigmoidal
     * function
     *
     * @param classify_Data1
     * @return double returns the classified output
     */
    public double classify_Perceptron(double[] classify_Data1) {
        double net = (-1) * this.theta;
        for (int i = 0; i < this.weights.size(); i++) {
            net += classify_Data1[i] * this.weights.get(i);

        }
        return sigmoidal_activation_func(net);

    }

    /**
     *
     * @param net
     * @return int returns the result based on the step function
     */
    public static int step_function(double net) {
        if (net >= 0.5) {
            return 1;
        }
        else {
            return 0;
        }

    }

    /**
     * This function returns the activation output based on sig function
     *
     * @param net
     * @return double the activation function output based on the sig algorithm
     */
    public double sigmoidal_activation_func(double net) {

        return (double) 1 / (1 + Math.pow(Math.E, (-1) * net));

    }

    /**
     * This function sets the change of weight
     *
     * @param deltaWeight
     * @param index
     */
    public void changeWeightAtIndex(double deltaWeight, int index) {

        this.weights.set(index, deltaWeight + this.weights.get(index));

    }

    /**
     *
     * @param index
     * @return
     */
    public double getWeightAtIndex(int index) {
        return this.getWeights().get(index);

    }

}
