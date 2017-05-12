/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2016
 *
 * Name: Prof. King
 * Project: NeuralNet
 * Package: neuralnet.data
 * File: LabeledInstance
 * Description:
 *
 * ****************************************
 */
package hw03.model.data;

import java.util.ArrayList;

/**
 * Encapsulates a labeled instance of data. The class adds an additional member
 * representing the target(s) / class label(s) for the data.
 *
 * @author brk009
 */
public class LabeledInstance extends UnlabeledInstance {

    private ArrayList<Double> target = null;

    /**
     * Construct a new empty Labeled Instance
     */
    public LabeledInstance() {
        super();
    }

    public LabeledInstance(double[] xVec, ArrayList<Double> target) {
        super(xVec);
        this.target = target;
    }

    public ArrayList<Double> getTargets() {
        return this.target;
    }

    public double getTarget(int i) {
        return this.target.get(i);
    }

    public void setTarget(ArrayList<Double> target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "LabeledInstance{" + super.toString() + "target=" + target + '}';
    }
}
