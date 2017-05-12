/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet.strategy
 * File: ReLUActivationStrategy
 *
 * ****************************************
 */package hw03.model.strategy;

public class ReLUActivationStrategy implements ActivationStrategy {

    @Override
    public double f(double x) {
        return Math.max(0.0f, x);
    }

    @Override
    public double fDeriv(double x) {
        return x >= 0 ? 1.0f : 0.0f;
    }

}
