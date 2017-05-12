/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet
 * File: Edges
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
public class Edges implements Serializable {

    private Layer inLayer;
    private Layer outLayer;
    private Edge[][] edges;
    private BiasEdge[] thetaEdges;
    private double[] outSensitivity;

    public Edge[][] getEdges() {
        return edges;
    }

    public Edges(Layer inLayer, Layer outLayer) {

        this.inLayer = inLayer;
        this.outLayer = outLayer;

        // Account for the bias value. The bias will be stored in last input
        // position
        edges = new Edge[outLayer.getNumNeurons()][inLayer.getNumNeurons()];
        thetaEdges = new BiasEdge[outLayer.getNumNeurons()];

        for (int j = 0; j < outLayer.getNumNeurons(); j++) {
            for (int i = 0; i < inLayer.getNumNeurons(); i++) {
                this.edges[j][i] = new Edge(outLayer.getNeuron(j),
                                            inLayer.getNeuron(i));
            }
            thetaEdges[j] = new BiasEdge(outLayer.getNeuron(j));
        }

        outSensitivity = new double[outLayer.getNumNeurons()];
    }

    public Layer getInLayer() {
        return this.inLayer;
    }

    public Layer getOutLayer() {
        return this.outLayer;
    }

    public double[] getOutSensitivy() {
        return this.outSensitivity;
    }

    public double getWeightOf(int j, int i) {
        return this.edges[j][i].getWeight();
    }

    public Edge getEdge(int out, int in) {
        return this.edges[out][in];
    }

    /**
     * This is a essential method for dealing with the feedforward algorithm
     * This method computes the net input for every output neuron. It does not
     * return anything, but only propagates all of the inputs of the output
     * neurons.
     */
    public void computeForwardSum() {

        // For each output neuron...
        for (int j = 0; j < outLayer.getNumNeurons(); j++) {

            // Compute the weighted sum all the inputs into this neuron
            double net = 0.0;
            for (int i = 0; i < inLayer.getNumNeurons(); i++) {
                net += inLayer.getNeuron(i).getActivatedOutput()
                       * edges[j][i].getWeight();
            }
            // And, add in the theta/bias!
            net -= thetaEdges[j].getWeight();

            // Set the net input into this output neuron, and activate it!
            outLayer.getNeuron(j).setNetInputAndActivateOutput(net);
        }
    }

    /**
     * This is the backpropagation algorithm. As we know, the back propagation
     * is different depending on which edges are being updated.
     *
     * @param targetOutput
     */
    public void learn(ArrayList<Double> targetOutput) {
        // Assume that input has already been forwarded, and all internal neurons
        // are correct
        boolean isOutputEdges = false;
        if (this.outLayer.getOutputToEdges() == null) {
            isOutputEdges = true;
        }

        // For each neuron in the output layer...
        for (int j = 0; j < outLayer.getNumNeurons(); j++) {
            double sensitivity;
            Neuron nOut = outLayer.getNeuron(j);
            double error = 0.0;

            // Compute the error... which entirely depends on whether we are
            // updating the output edges, or higher up the network
            if (isOutputEdges) {
                // Compute the error
                error = nOut.getActivatedOutput() - targetOutput.get(j);
            }
            else {
                // We need to back propagate all edges from ALL of the outgoing
                // edges of Neuron nOut
                Edges downEdges = this.getOutLayer().getOutputToEdges();

                // Now compute the sum of the backpropagated error from every one
                // of the outgoing edges of neuron j.
                for (int k = 0; k < downEdges.getOutLayer().getNumNeurons(); k++) {
                    double term = downEdges.getOutSensitivy()[k] * downEdges.getWeightOf(
                            k, j);
                    error += term;
                }
            }
            // We're going to compute a "sensitivy" term. As noted above, memoizing
            // this term makes the computation much more efficient!
            sensitivity = error * nOut.getDerivOfActivatedOutput();
            outSensitivity[j] = sensitivity;

            // Compute delta weight values
            for (int i = 0; i < inLayer.getNumNeurons(); i++) {
                edges[j][i].updateDeltaWeight(
                        -ANN.learningRate * sensitivity * inLayer.getOutputValueOf(
                                i));
//                edges[j][i].updateDeltaWeight(
//                        sensitivity * inLayer.getOutputValueOf(i));
//                edges[j][i].setDeltaWeight(
//                        -ANN.learningRate * sensitivity * inLayer.getOutputValueOf(
//                                i) + ANN.momentum * edges[j][i].getDeltaWeight());
            }
            // Bias adjustment
            thetaEdges[j].setDeltaWeight(-ANN.learningRate * sensitivity * -1.0);
//            thetaEdges[j].setDeltaWeight(sensitivity * 1.0);
        }

    }

    public void updateWeightsAndClearDelta() {
        for (int j = 0; j < edges.length; j++) {
            for (int i = 0; i < edges[0].length; i++) {
                edges[j][i].updateWeightWithDelta(ANN.momentum);
            }
            thetaEdges[j].updateWeightWithDelta(ANN.momentum);
        }
    }

    @Override
    public String toString() {
        return "Edges{\t" + "inLayer=" + inLayer
               + "\toutLayer=" + outLayer + '\n'
               + "\tedges=" + Arrays.deepToString(edges) + '\n'
               + "\toutSensitivity=" + Arrays.toString(outSensitivity) + '}';
    }

}
