/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 19, 2017
  * Time: 2:44:55 PM *
  * Project: csci205_hw
  * Package: hw02
  * File: ActivationFunction
  * Description:
  *
  * ****************************************
 */
package hw02;

/**
 *
 * @author haoyuxiong
 */
public interface ActivationFunction {

    /**
     *
     * @param input
     * @return
     */
    double getValue(double input);

}

class StepActivationFunction implements ActivationFunction {

    @Override
    public double getValue(double input) {
        if (input >= 0.5) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class SigmoidalActivationFunction implements ActivationFunction {

    @Override
    public double getValue(double input) {
        return (double) 1 / (1 + Math.pow(Math.E, (-1) * input));

    }

}

class SoftSignActivationFunction implements ActivationFunction {

    @Override
    public double getValue(double input) {
        return ((input / (1 + Math.abs(input))) + 1) / 2;

    }

}
