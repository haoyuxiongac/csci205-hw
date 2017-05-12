/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet
 * File: Edge
 * Description:
 *
 * ****************************************
 */
package hw03.model;

import java.io.Serializable;

/**
 * Edge
 *
 * This class represents an abstraction for one edge. An edge is a directed edge
 * between Neuron instances. A Neuron has a weight, and will also store the
 * delta weight that is computed during backprop
 *
 * @author brk009
 */
public class Edge implements Serializable {

    private final Neuron inNeuron;
    private final Neuron outNeuron;
    private double weight;
    private double deltaWeight;
    private double oldDeltaWeight;

    /**
     * Construct a new Edge between two Neurons.
     *
     * @param outNeuron
     * @param inNeuron
     */
    public Edge(Neuron outNeuron, Neuron inNeuron) {
        this.inNeuron = inNeuron;
        this.outNeuron = outNeuron;

        this.weight = Math.random() - 0.5;
        this.deltaWeight = 0.0;
        this.oldDeltaWeight = 0.0;
    }

    public Neuron getInNeuron() {
        return inNeuron;
    }

    public Neuron getOutNeuron() {
        return outNeuron;
    }

    public double getWeight() {
        return weight;
    }

    public double getDeltaWeight() {
        return deltaWeight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Update the weight with the accumulated delta, and then zero out the delta
     * weight value
     */
    public void updateWeightWithDelta(double momentum) {
        this.weight += (this.deltaWeight + momentum * this.oldDeltaWeight);
        this.oldDeltaWeight = this.deltaWeight;
        this.deltaWeight = 0.0;
    }

    /**
     * Update the delta weight accumulator
     *
     * @param deltaWeight
     */
    public void updateDeltaWeight(double deltaWeight) {
        this.deltaWeight += deltaWeight;
    }

    /**
     * Set the delta weight value
     *
     * @param deltaWeight
     */
    public void setDeltaWeight(double deltaWeight) {
        this.deltaWeight = deltaWeight;
    }

}

/**
 * A Bias edge is an edge to an output Neuron only. No input Neuron is specified
 *
 * @author brk009
 */
class BiasEdge extends Edge {

    public BiasEdge(Neuron outNeuron) {
        super(outNeuron, null);
    }
}
