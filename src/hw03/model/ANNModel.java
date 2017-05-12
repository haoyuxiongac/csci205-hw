/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Apr 1, 2017
  * Time: 11:17:13 AM *
  * Project: csci205_hw
  * Package: hw03.model
  * File: ANNModel
  * Description:
  *
  * ****************************************
 */
package hw03.model;

import hw03.model.strategy.LogisticActivationStrategy;
import hw03.model.strategy.SoftplusActivationStrategy;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author haoyuxiong
 */
public class ANNModel {

    private SimpleBooleanProperty sigmoidalChosen;

    private ANN ann;

    public ANN getAnn() {
        return ann;
    }

    public void takeInFileCreate(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {

        FileInputStream f = new FileInputStream(filename);
        ObjectInputStream configIn = new ObjectInputStream(f);
        this.ann = (ANN) configIn.readObject();
        sigmoidalChosen = new SimpleBooleanProperty(this.ann.actFunc);

        if (this.ann.actFunc) {
            this.ann.activation = new LogisticActivationStrategy();
        }
        else {
            this.ann.activation = new SoftplusActivationStrategy();
        }

        this.ann.currEpoch = 0;
    }

    public void takeInAttributeCreate(int numInp, int numOut,
                                      int numNodesInHiddenLayers, double minSSE,
                                      int maxEpoch,
                                      double alpha, double mu,
                                      SimpleBooleanProperty sigmoidal) {
        sigmoidalChosen = sigmoidal;
        this.ann = new ANN(numInp, numOut, numNodesInHiddenLayers, minSSE,
                           maxEpoch, sigmoidal.get(), alpha, mu);
    }

    public ANN getANN() {
        return ann;
    }
}
