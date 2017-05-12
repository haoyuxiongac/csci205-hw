/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Apr 2, 2017
  * Time: 9:49:14 PM *
  * Project: csci205_hw
  * Package: hw03
  * File: ANNMain
  * Description:
  *
  * ****************************************
 */
package hw03;

import hw03.model.ANNModel;
import hw03.view.DesignController;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Jingya
 */
public class ANNMain extends Application {

    private ANNModel annModel;

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Class thisClass = ANNMain.class;
        InputStream in = thisClass.getResourceAsStream(
                "../hw03/view/design.fxml");
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(thisClass.getResource("../hw03/view/design.fxml"));

        Parent root = loader.load(in);

        annModel = new ANNModel();
        DesignController ctrl = loader.getController();
        ctrl.setModel(annModel);

        Scene scene = new Scene(root);
        primaryStage.setTitle("ANN");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
