/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet
 * File: Neuron
 * Description:
 *
 * ****************************************
 */
package hw03.model;

import java.io.Serializable;

/**
 *
 * @author brk009
 */
public class Neuron implements Serializable {

    private double netInput;
    private double output;

    public Neuron() {
    }

    public double setNetInputAndActivateOutput(double net) {
        netInput = net;
        output = ANN.activation.f(net);

        return output;
    }

    public double getNetInput() {
        return netInput;
    }

    public double getActivatedOutput() {
        return output;
    }

    public double getDerivOfActivatedOutput() {
        return ANN.activation.fDeriv(netInput);
    }

    public void overrideActivatedOutput(double output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "Neuron{" + "netInput=" + netInput + ", output=" + output + '}';
    }
}
