/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: YOUR NAME
 * Date: Oct 7, 2016
 * Time: 9:05:36 AM
 *
 * Project: NeuralNet
 * Package: neuralnet2
 * File: SoftplusActivationStrategy
 * Description:
 *
 * ****************************************
 */
package hw03.model.strategy;

import java.io.Serializable;

public class SoftplusActivationStrategy implements ActivationStrategy,
                                                   Serializable {

    @Override
    public double f(double x) {
        return Math.log(1 + Math.exp(x));
    }

    @Override
    public double fDeriv(double x) {
        return (1 / (1 + Math.exp(-x)));
    }

}
