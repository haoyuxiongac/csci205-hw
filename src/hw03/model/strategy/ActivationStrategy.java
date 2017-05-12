/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet.strategy
 * File: ActivationStrategy
 *
 * ****************************************
 */
package hw03.model.strategy;

/**
 * Interface representing an activation function as a strategy design pattern.
 * An Activation function is a function based on one float number, and a
 * derivative function.
 *
 * @author brk009
 */
public interface ActivationStrategy {

    public double f(double x);

    public double fDeriv(double x);

}
