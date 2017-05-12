/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet
 * File: Layer
 * Description:
 *
 * ****************************************
 */
package hw03.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author brk009
 */
public class Layer implements Serializable {

    private int numNeurons;
    private Neuron[] neurons;
    private Edges inputFromEdges;
    private Edges outputToEdges;

    public Layer(int numNeurons) {
        this.numNeurons = numNeurons;
        neurons = new Neuron[numNeurons];
        for (int i = 0; i < numNeurons; i++) {
            neurons[i] = new Neuron();
        }
        inputFromEdges = null;
        outputToEdges = null;
    }

    public Neuron[] getNeurons() {
        return this.neurons;
    }

    public int getNumNeurons() {
        return numNeurons;
    }

    public Neuron getNeuron(int i) {
        return neurons[i];
    }

    public double getInputValueOf(int i) {
        return neurons[i].getNetInput();
    }

    public double getOutputValueOf(int i) {
        return neurons[i].getActivatedOutput();
    }

    public ArrayList<Double> getInputValues() {
        ArrayList<Double> list = new ArrayList<Double>();
        for (int i = 0; i < numNeurons; i++) {
            list.add(neurons[i].getNetInput());
        }
        return list;
    }

    public ArrayList<Double> getOutputValues() {
        ArrayList<Double> list = new ArrayList<Double>();
        for (int i = 0; i < numNeurons; i++) {
            list.add(neurons[i].getActivatedOutput());
        }
        return list;
    }

    /**
     * Set up the reference to the edges that this layer is sitting between. If
     * this is an InputLayer, then inputFromEdges is null. If this is an
     * OutputLayer then outputToEdges is null
     *
     * @param inputFromEdges
     * @param outputToEdges
     */
    void setEdges(Edges inputFromEdges, Edges outputToEdges) {
        this.inputFromEdges = inputFromEdges;
        this.outputToEdges = outputToEdges;
    }

    void setInputFromEdges(Edges inputFromEdges) {
        this.inputFromEdges = inputFromEdges;
    }

    void setOutputToEdges(Edges outputToEdges) {
        this.outputToEdges = outputToEdges;
    }

    public Edges getInputFromEdges() {
        return this.inputFromEdges;
    }

    public Edges getOutputToEdges() {
        return this.outputToEdges;
    }

    public boolean isInputLayer() {
        return this.inputFromEdges == null;
    }

    public boolean isOutputLayer() {
        return this.outputToEdges == null;
    }

    @Override
    public String toString() {
        return "Layer{" + "numNeurons=" + numNeurons + ", neurons=" + Arrays.toString(
                neurons) + '}';
    }

}
