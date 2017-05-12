/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet
 * File: ANN
 * Description:
 *
 * ****************************************
 */
package hw03.model;

import hw03.model.data.LabeledInstance;
import hw03.model.data.LabeledInstances;
import hw03.model.data.UnlabeledInstance;
import hw03.model.data.UnlabeledInstances;
import hw03.model.strategy.ActivationStrategy;
import hw03.model.strategy.LogisticActivationStrategy;
import hw03.model.strategy.SoftplusActivationStrategy;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * ANN - This is the primary class that encapsulates an entire ANN
 *
 * @author brk009
 */
public class ANN implements Serializable {

    private int numInputs;
    private int numHidden;
    private int numOutputs;
    private int numLayers;

    private Layer[] layers;
    private Edges[] edgeConnections;

    public static double learningRate;
    public static double momentum;
    public int maxEpochs;
    public double errStopThresh;
    public double currSSE;
    public boolean actFunc;
    public int currEpoch;

    public int getNumInputs() {
        return numInputs;
    }

    public int getNumHidden() {
        return numHidden;
    }

    public int getNumOutputs() {
        return numOutputs;
    }

    public Edges[] getEdgeConnections() {
        return edgeConnections;
    }

    /**
     * Uncomment which activation strategy you want - keep in mind that they all
     * affect the selection of learningRate and momentum
     */
    public static ActivationStrategy activation;
//    public static ActivationStrategy activation = new LogisticActivationStrategy();
//    public static ActivationStrategy activation = new LinearActivationStrategy();
//    public static ActivationStrategy activation = new SoftplusActivationStrategy();  // needs much smaller learningRate
//    public static ActivationStrategy activation = new ReLUActivationStrategy();

    /**
     * ANN - construct a new ANN
     *
     * @param numInputs - Number of inputs
     * @param numHidden - Number of hidden nodes (set to 0 for 2-layer ANN)
     * @param numOutputs - Number of output nodes
     */
    public ANN(int numInputs, int numOutputs, int numHidden,
               double errStopThresh, int maxEpochs, boolean actFunc,
               double learningRate, double momentum) {
        this.numInputs = numInputs;
        this.numHidden = numHidden;
        this.numOutputs = numOutputs;
        this.numLayers = numHidden > 0 ? 3 : 2;
        this.errStopThresh = errStopThresh;
        this.maxEpochs = maxEpochs;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.actFunc = actFunc;

        if (this.actFunc) {
            this.activation = new LogisticActivationStrategy();
        }
        else {
            this.activation = new SoftplusActivationStrategy();
        }

        // For now, I'm hard-coding the configuration, though this can be easily
        // extended to deep learning (i.e. more than one hidden layer
        this.layers = new Layer[this.numLayers];
        this.layers[0] = new InputLayer(numInputs);
        if (numLayers == 2) {
            this.layers[1] = new Layer(numOutputs);
        }
        else {
            layers[1] = new Layer(numHidden);
            layers[2] = new Layer(numOutputs);
        }

        // Create the edges between layers
        this.edgeConnections = new Edges[this.numLayers - 1];
        for (int i = 0; i < edgeConnections.length; i++) {
            this.edgeConnections[i] = new Edges(layers[i], layers[i + 1]);
        }

        // Inform the layers what edges are connecting between them
        for (int i = 0; i < this.numLayers - 1; i++) {
            layers[i].setOutputToEdges(this.edgeConnections[i]);
            layers[i + 1].setInputFromEdges(this.edgeConnections[i]);
        }

    }

    /**
     * Feed in a dataset, and return an array of outputs representing the output
     * for each instance
     *
     * @param data - LabeledInstances
     * @return
     */
    public ArrayList<ArrayList<Double>> classifyInstances(LabeledInstances data) {
        ArrayList<ArrayList<Double>> resultList = new ArrayList<>();
        for (UnlabeledInstance inst : data) {
            resultList.add(classifyInstance(inst));
        }
        return resultList;
    }

    /**
     * Feed in a dataset of unlabeled instances, and return an array of outputs
     * representing the output of each instance
     *
     * @param data
     * @return
     * @throws NeuronException
     */
    public ArrayList<ArrayList<Double>> classifyInstances(
            UnlabeledInstances data) {
        ArrayList<ArrayList<Double>> resultList = new ArrayList<>();
        for (UnlabeledInstance inst : data) {
            resultList.add(classifyInstance(inst));
        }
        return resultList;
    }

    /**
     * Feed in a single unlabeled instance, and return the result(s).
     *
     * @param inst - the unlabeled instance
     * @return an ArrayList<Double> of results
     */
    public ArrayList<Double> classifyInstance(UnlabeledInstance inst) {
        getInputLayer().setInputValue(inst);
        for (int i = 0; i < this.edgeConnections.length; i++) {
            this.edgeConnections[i].computeForwardSum();
        }
        return getOutputLayer().getOutputValues();
    }

    /**
     * LEARN! Woah. Yeah, this looks really simple at the ANN level. All of the
     * heavy work is going on deeper...
     *
     * @param trainData - the labeled dataset of instances required to learn
     * @param doStochasticLearning - If true, we do stochastic learning,
     * meaning, we randomly draw instances from the dataset
     * @param batschSize - Number of instances to accumulate before updating
     * weights
     * @return - the average error over all instances
     */
    public double learn(LabeledInstances trainData, boolean doStochasticLearning,
                        int batchSize) {

        double totalError = 0.0;
        int numInBatch = 0;

        for (int i = 0; i < trainData.size(); i++) {
            LabeledInstance inst;
            if (doStochasticLearning) {
                inst = trainData.get((int) (Math.random() * trainData.size()));
            }
            else {
                inst = trainData.get(i);
            }
            totalError += backpropagateError(inst);
            numInBatch++;

            if (numInBatch == batchSize || i == trainData.size() - 1) {
                // Great! All of the delta weights have been computed and are stored inside
                // each edge. Now, update the weights and clear delta out for next time
                for (int j = 0; j < this.edgeConnections.length; j++) {
                    this.edgeConnections[j].updateWeightsAndClearDelta();
                }

                numInBatch = 0;
            }
        }

        return totalError / trainData.size();
    }

    /**
     * Learn from a single labeled instance. This function is getting closer to
     * the heart of learning in an ANN. This function handles feeding the
     * instance into the network, then doing backpropagation back through the
     * network to update the weights.
     *
     * @param trainInst
     * @return
     */
    public double backpropagateError(LabeledInstance trainInst) {

        // First, feed the instance through the network
        classifyInstance(trainInst);

        // Now, we work backward through the network. Starting at the output level,compute
        // all of the delta weight values between each level and work backwards.
        for (int i = this.edgeConnections.length - 1; i >= 0; i--) {
            this.edgeConnections[i].learn(trainInst.getTargets());
        }
        // Compute the error, though this is a lag variable, because the error
        // is really based on the output that was computed when we fed this instance
        // through. Not a big deal.
        return computeOutputError(trainInst,
                                  this.getOutputLayer().getOutputValues());

    }

    /**
     * Another helper function to compute the error between a target output and
     * the observed output reported in annOutput
     *
     * @param trainInst
     * @param annOutput
     * @return
     */
    public static double computeOutputError(LabeledInstance trainInst,
                                            ArrayList<Double> annOutput) {

        double error = 0.0;
        for (int i = 0; i < trainInst.getTargets().size(); i++) {
            // For the time, I'm just going to compute the error as the absolute
            // value between the target and observed output
            error += Math.abs(
                    trainInst.getTarget(i) - annOutput.get(i));
        }
        return error / trainInst.getTargets().size();
    }

    /**
     * Compute the average error between the target and observed output of the
     * ANN, passes as a parameter
     *
     * @param data - labeled data
     * @param annOutputs - observed outputs
     * @return the average error
     */
    public static double computeOutputError(LabeledInstances data,
                                            ArrayList<ArrayList<Double>> annOutputs) {
        double error = 0.0;
        for (int i = 0; i < data.size(); i++) {
            error += computeOutputError(data.get(i), annOutputs.get(i));
        }
        return error / data.size();
    }

    /**
     * Simple helper utility method to print the results of the ANN for a set of
     * given labeled instances
     *
     * @param annOutputs - The output of the ANN for every instance
     * @param data - the labeled instances themselves
     * @param doOnlyBad - if true, only output the instances outside the error
     * threshold
     * @param badErrorThreshold - value to use to specify the error threshold
     */
    public static void printResults(ArrayList<ArrayList<Double>> annOutputs,
                                    LabeledInstances data, boolean doOnlyBad,
                                    double badErrorThreshold) {

        if (!doOnlyBad) {
            badErrorThreshold = 0.0;
        }

        int badCount = 0;

        for (int i = 0; i < annOutputs.size(); i++) {
            if (computeOutputError(data.get(i), annOutputs.get(i)) >= badErrorThreshold) {
                System.out.println(data.get(i) + " output=" + annOutputs.get(i));
                badCount++;
            }
        }
        if (doOnlyBad) {
            System.out.println("Misclassified = " + badCount);
        }
    }

    /**
     * An early attempt to produce a string representation of the ANN. Yeah,
     * like, this String could be enormous depending on the network size.
     * However, it's critical to understanding the structure of the ANN.
     *
     * @return
     */
    @Override
    public String toString() {
        String s = "ANN: ";
        for (int i = 0; i < this.edgeConnections.length; i++) {
            s += "  edges{" + i + "->" + i + 1 + "} = " + this.edgeConnections[i] + "\n";
        }
        return s;
    }

    public Layer getLayer(int i) {
        return this.layers[i];
    }

    public Edges getEdges(int inLayerNum) {
        return this.edgeConnections[inLayerNum];
    }

    public InputLayer getInputLayer() {
        return (InputLayer) this.getLayer(0);
    }

    public Layer getOutputLayer() {
        return this.getLayer(this.layers.length - 1);
    }

    public int getNumLayers() {
        return this.layers.length;
    }

    /**
     * Useful method that returns the dimensions of the ANN layers as a list of
     * Integers. Might prove useful.
     *
     * @return ArrayList<Integer> of layer dimension sizes
     */
    public ArrayList<Integer> getANNLayerDim() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < this.getNumLayers(); i++) {
            result.add(layers[i].getNumNeurons());
        }
        return result;
    }

    /*
     * Main method -- this is merely a test method to test out an ANN on a
     * specified dataset.
     *
     * @param args
     * @throws FileNotFoundException
     * @throws NeuronException

    public static void main(String[] args) throws FileNotFoundException {
        // Read in a dataset
        LabeledInstances trainData = new LabeledInstances("./src/circTrain.csv",
                                                          true, 2);
        System.out.println(trainData);

        ANN theANN = new ANN(2, 1, 3, 0.001, 1000000, true, 0.2, 0.5);

        System.out.println(theANN);

        double totalError = Double.NaN;
        ArrayList<ArrayList<Double>> output = theANN.classifyInstances(trainData);
        printResults(output, trainData, true, 0.1);

        int epoch;
        for (epoch = 0; epoch < maxEpochs; epoch++) {
//            System.out.println("theANN: " + theANN);
            theANN.learn(trainData, true, 1);
//            System.out.println("totalError: " + totalError);
            if (epoch % 1000 == 0) {
                output = theANN.classifyInstances(trainData);
                printResults(output, trainData, true, 0.1);
                double error = computeOutputError(trainData, output);
                System.out.println(
                        "Epoch[" + epoch + "]  Average Error: " + error);
                if (error <= errStopThresh) {
                    System.out.println("SUCCESS!");
                    break;
                }

            }
        }
        System.out.println("DONE!");
        System.out.println("Epochs: " + epoch);

    }
     */
}
