/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet
 * File: InputLayer
 * Description:
 *
 * ****************************************
 */
package hw03.model;

import hw03.model.data.UnlabeledInstance;

/**
 * An InputLayer is a layer of Neurons, hoewever, the output of these neurons is
 * really the input value.
 *
 * @author brk009
 */
public class InputLayer extends Layer {

    public InputLayer(int numInputs) {
        super(numInputs);
    }

    public void setInputValue(UnlabeledInstance inst) {
        // We are hardcoding the output of every neuron in the input layer
        // based on the instance we are feeding into it
        for (int i = 0; i < inst.size(); i++) {
            this.getNeuron(i).overrideActivatedOutput(inst.get(i));
        }
    }

}
