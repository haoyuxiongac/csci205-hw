/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 31, 2017
  * Time: 8:55:33 PM *
  * Project: csci205_hw
  * Package: hw03.view
  * File: DesignController
  * Description:
  *
  * ****************************************
 */
package hw03.view;

import hw03.model.ANNModel;
import hw03.model.data.LabeledInstances;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jingya
 */
public class DesignController {

    private ANNModel theModel = new ANNModel();
    private ANNTask theTask;
    @FXML
    private Button learnBtn;
    @FXML
    private Tab fileTab;
    @FXML
    private Tab configTab;
    @FXML
    private TextField numIN;
    @FXML
    private TextField numOUT;
    @FXML
    private TextField numNeurons;
    @FXML
    private TextField maxSSE;
    @FXML
    private TextField maxEpoch;
    @FXML
    private RadioButton actFuncRadio1;
    @FXML
    private RadioButton actFuncRadio2;
    @FXML
    private TextField alpha;
    @FXML
    private TextField momentum;
    @FXML
    private Button exitBtn;
    @FXML
    private Button classifyBtn;
    @FXML
    private Pane canvasPane;
    @FXML
    private Label currentSSE;
    @FXML
    private Label currentEpoch;
    @FXML
    private Button stepBtn;
    @FXML
    private Button pauseBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button fileUploadBtn;
    @FXML
    private Button applyConfigBtn;
    @FXML
    private Button applyMomentumBtn;
    @FXML
    private TextField fileNameBox;

    private ArrayList<Circle> inputLayerNodes;
    private ArrayList<Circle> hiddenLayerNodes;
    private ArrayList<Circle> outputLayerNodes;
    private ArrayList<ArrayList<Label>> inputWeights;
    private ArrayList<ArrayList<Label>> outputWeights;
    private Dialog dialog;
    private LabeledInstances data;
    private ArrayList<ArrayList<Double>> resultList = new ArrayList<ArrayList<Double>>();
    private SimpleBooleanProperty ifPause = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty ifStep = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty ifComplete;

    @FXML
    void initialize() {
        theTask = null;
        //theModel.getSigmoidalChosen().bind(actFuncRadio1.selectedProperty());

    }

    @FXML
    void exitBtn(ActionEvent event) {

        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();

    }

    @FXML
    void applyConfigBtn(ActionEvent event) {

        try {
            this.theModel.takeInAttributeCreate(Integer.parseInt(
                    this.numIN.getText()), Integer.parseInt(
                                                this.numOUT.getText()),
                                                Integer.parseInt(
                                                        this.numNeurons.getText()),
                                                Double.parseDouble(
                                                        this.maxSSE.getText()),
                                                Integer.parseInt(
                                                        this.maxEpoch.getText()),

                                                Double.parseDouble(
                                                        this.alpha.getText()),
                                                Double.parseDouble(
                                                        this.momentum.getText()),
                                                new SimpleBooleanProperty(
                                                        this.actFuncRadio1.selectedProperty().getValue()));
            clearGraph();
            generateGraph();

        } catch (NumberFormatException numberFormatException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect input!");
            alert.setHeaderText("Incorrect input specified!");
            alert.show();
        }
    }

    @FXML
    void fileUploadBtn(ActionEvent event) throws FileNotFoundException, IOException, ClassNotFoundException {

        try {
            this.theModel.takeInFileCreate(this.fileNameBox.getText());
            clearGraph();
            generateGraph();

        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input file not found!");
            alert.setHeaderText("Incorrect input specified!");
            alert.show();
        } catch (IOException e1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect input filename");
            alert.setHeaderText("Incorrect input specified!");
            alert.show();

        }

    }

    @FXML
    void applyMomentumBtn(ActionEvent event) {

        if (!ifPause.get()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Learning in progress!");
            alert.setHeaderText("Pause before applying the changes!");
            alert.show();
        }
        else {
            theModel.getANN().momentum = Double.parseDouble(
                    this.momentum.getText());
            theModel.getANN().learningRate = Double.parseDouble(
                    this.alpha.getText());
        }
    }

    @FXML
    void learnBtn(ActionEvent event) throws Exception {

        dialog = new TextInputDialog("Please input the file directory");
        dialog.setTitle("Learn");
        dialog.setHeaderText("Enter the directory");
        Optional<String> result = dialog.showAndWait();

        String entered = "None yet";
        if (result.isPresent()) {

            entered = result.get();
        }

        try {
            Integer[] target = new Integer[theModel.getANN().getNumOutputs()];
            int numIp = theModel.getANN().getNumInputs();
            for (int i = 0; i < theModel.getANN().getNumOutputs(); i++) {
                target[i] = numIp + i;
            }
            boolean labeled = false;
            try {
                File f = new File(entered);
                Scanner fScanner = new Scanner(f);
                String line = fScanner.nextLine();
                String[] parts = line.split(",");
                double a = Double.parseDouble(parts[0]);

            } catch (NumberFormatException e) {
                labeled = false;
            }
            data = new LabeledInstances(entered, labeled, target);
            theTask = new ANNTask(theModel, data);
            //theTask.call();

            Thread th = new Thread(theTask);
            th.setDaemon(true);
            th.start();

            currentEpoch.textProperty().bind(theTask.messageProperty());
            currentSSE.textProperty().bind(theTask.valueProperty().asString(
                    "%.5f"));
            ifPause.set(false);

        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect input filename");
            alert.setHeaderText("Incorrect input specified!");
            alert.show();

        }

    }

    @FXML
    void classifyBtn(ActionEvent event) throws FileNotFoundException {

        dialog = new TextInputDialog("Please input the file path");
        dialog.setTitle("Classify");
        dialog.setHeaderText("Enter the path");
        Optional<String> result = dialog.showAndWait();

        String entered = "None yet";
        if (result.isPresent()) {

            entered = result.get();
        }

        try {
            Integer[] target = new Integer[theModel.getANN().getNumOutputs()];
            int numIp = theModel.getANN().getNumInputs();
            for (int i = 0; i < theModel.getANN().getNumOutputs(); i++) {
                target[i] = numIp + i;
            }
            boolean labeled = false;
            try {
                File f = new File(entered);
                Scanner fScanner = new Scanner(f);
                String line = fScanner.nextLine();
                String[] parts = line.split(",");
                double a = Double.parseDouble(parts[0]);

            } catch (NumberFormatException e) {
                labeled = false;
            }
            data = new LabeledInstances(entered, labeled, target);
            resultList = theModel.getANN().classifyInstances(data);

            dialog = new TextInputDialog("Please input the output file path");
            dialog.setTitle("Output File");
            dialog.setHeaderText("Enter the path");
            result = dialog.showAndWait();

            String outFilePath = "None yet";
            if (result.isPresent()) {

                outFilePath = result.get();
            }

            writeOutputs(resultList, outFilePath);

        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect input filename");
            alert.setHeaderText("Incorrect input specified!");
            alert.show();

        }

    }

    @FXML
    void stepBtn(ActionEvent event) {
        if (!ifPause.get()) {
            this.pauseBtn.fire();
        }
        else {
            this.ifStep.set(true);
            theTask = new ANNTask(theModel, data);

            Thread th = new Thread(theTask);
            th.setDaemon(true);
            th.start();

            currentEpoch.textProperty().bind(theTask.messageProperty());
            currentSSE.textProperty().bind(theTask.valueProperty().asString(
                    "%.5f"));
        }
    }

    @FXML
    void pauseBtn(ActionEvent event) {
        try {
            if (!ifPause.get()) {
                theTask.cancel(false);
            }
            else {
                ifStep.set(false);
                theTask = new ANNTask(theModel, data);

                Thread th = new Thread(theTask);
                th.setDaemon(true);
                th.start();

                currentEpoch.textProperty().bind(theTask.messageProperty());
                currentSSE.textProperty().bind(theTask.valueProperty().asString(
                        "%.5f"));
            }
            ifPause.set(!ifPause.get());

        } catch (NullPointerException e) {
        }
    }

    @FXML
    void saveBtn(ActionEvent event) throws FileNotFoundException, IOException {
        if (theModel.getANN() != null) {
            dialog = new TextInputDialog("Please input the config file path");
            dialog.setTitle("Save Configuration");
            dialog.setHeaderText("Enter the file path");
            Optional<String> result = dialog.showAndWait();

            String entered = "None yet";
            if (result.isPresent()) {

                entered = result.get();

                FileOutputStream f = new FileOutputStream(entered);
                ObjectOutputStream configOut = new ObjectOutputStream(f);
                configOut.writeObject(theModel.getANN());
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Model not available yet");
            alert.setHeaderText("Construct an ANN model first!");
            alert.show();
        }
    }

    void clearGraph() {
        canvasPane.getChildren().clear();
    }

    /**
     * Generate the corresponding visualization for the ANN
     *
     *
     */
    void generateGraph() {

        inputLayerNodes = new ArrayList<Circle>();
        hiddenLayerNodes = new ArrayList<Circle>();
        outputLayerNodes = new ArrayList<Circle>();
        inputWeights = new ArrayList<ArrayList<Label>>();
        outputWeights = new ArrayList<ArrayList<Label>>();
        NumberBinding radBinding = canvasPane.heightProperty().divide(
                Math.max(
                        this.theModel.getANN().getNumInputs(),
                        this.theModel.getANN().getNumHidden()) * 2).add(
                -30);
        NumberBinding centerXInputsBinding = canvasPane.widthProperty().divide(4);
        NumberBinding centerXHiddenBinding = canvasPane.widthProperty().divide(2);
        NumberBinding centerXOutputsBinding = canvasPane.widthProperty().divide(
                4).multiply(3);
        for (int i = 0; i < this.theModel.getANN().getNumInputs(); i++) {
            Circle c = new Circle(50);
            NumberBinding centerYBinding = canvasPane.heightProperty().divide(
                    (this.theModel.getANN().getNumInputs() + 1)).multiply(i + 1);

            c.setFill(Color.ORANGE);
            c.radiusProperty().bind(radBinding);
            c.centerXProperty().bind(centerXInputsBinding);
            c.centerYProperty().bind(centerYBinding);
            inputLayerNodes.add(c);
        }

        for (int i = 0; i < this.theModel.getANN().getNumHidden(); i++) {
            Circle c = new Circle(50);
            NumberBinding centerYBinding = canvasPane.heightProperty().divide(
                    (this.theModel.getANN().getNumHidden() + 1)).multiply(i + 1);
            c.setFill(Color.LIGHTCORAL);
            c.radiusProperty().bind(radBinding);
            c.centerXProperty().bind(centerXHiddenBinding);
            c.centerYProperty().bind(centerYBinding);
            hiddenLayerNodes.add(c);
        }

        for (int i = 0; i < this.theModel.getANN().getNumOutputs(); i++) {
            Circle c = new Circle(50);
            NumberBinding centerYBinding = canvasPane.heightProperty().divide(
                    (this.theModel.getANN().getNumOutputs() + 1)).multiply(i + 1);
            c.setFill(Color.PINK);
            c.radiusProperty().bind(radBinding);
            c.centerXProperty().bind(centerXOutputsBinding);
            c.centerYProperty().bind(centerYBinding);
            outputLayerNodes.add(c);
        }

        inputWeights = new ArrayList();
        for (int i = 0; i < inputLayerNodes.size(); i++) {
            ArrayList<Label> labelOfWeights = new ArrayList();
            for (int j = 0; j < hiddenLayerNodes.size(); j++) {
                Line line = new Line();
                line.startXProperty().bind(
                        inputLayerNodes.get(i).centerXProperty());
                line.startYProperty().bind(
                        inputLayerNodes.get(i).centerYProperty());
                line.endXProperty().bind(
                        hiddenLayerNodes.get(j).centerXProperty());
                line.endYProperty().bind(
                        hiddenLayerNodes.get(j).centerYProperty());

                line.setStrokeWidth(3);
                line.setStroke(Color.BLUE);
                canvasPane.getChildren().add(line);

                Label weight = new Label();
                weight.rotateProperty().bind(calcDegree(line));
                Paint c = Color.rgb(255, 255, 255, 0.7);
                weight.setBackground(new Background(new BackgroundFill(
                        c, new CornerRadii(5), new Insets(2))));
                weight.translateXProperty().bind(line.endXProperty().subtract(
                        radBinding.multiply(2)));
                weight.translateYProperty().bind(line.startYProperty().add(
                        line.endYProperty()).divide(2).add(-15));
                weight.setText(String.format("%.2f",
                                             theModel.getANN().getEdgeConnections()[0].getEdges()[j][i].getWeight()));
                weight.setPadding(new Insets(3));
                labelOfWeights.add(weight);
            }
            inputWeights.add(labelOfWeights);
        }

        outputWeights = new ArrayList();
        for (int i = 0; i < hiddenLayerNodes.size(); i++) {
            ArrayList<Label> labelOfWeights = new ArrayList();
            for (int j = 0; j < outputLayerNodes.size(); j++) {
                Line line = new Line();

                line.startXProperty().bind(
                        hiddenLayerNodes.get(i).centerXProperty());
                line.startYProperty().bind(
                        hiddenLayerNodes.get(i).centerYProperty());
                line.endXProperty().bind(
                        outputLayerNodes.get(j).centerXProperty());
                line.endYProperty().bind(
                        outputLayerNodes.get(j).centerYProperty());
                line.setStrokeWidth(3);
                line.setStroke(Color.GREEN);
                canvasPane.getChildren().add(line);

                Label weight = new Label();

                weight.rotateProperty().bind(calcDegree(line));
                Paint c = Color.rgb(255, 255, 255, 0.7);
                weight.setBackground(new Background(new BackgroundFill(
                        c, new CornerRadii(5), new Insets(2))));
                weight.translateXProperty().bind(line.startXProperty().add(
                        line.endXProperty()).divide(2));
                weight.translateYProperty().bind(line.startYProperty().add(
                        line.endYProperty()).divide(2));
                weight.setText(String.format("%.2f",
                                             theModel.getANN().getEdgeConnections()[1].getEdges()[j][i].getWeight()));
                labelOfWeights.add(weight);

            }
            outputWeights.add(labelOfWeights);

        }

        canvasPane.getChildren().addAll(inputLayerNodes);
        canvasPane.getChildren().addAll(hiddenLayerNodes);
        canvasPane.getChildren().addAll(outputLayerNodes);

        for (int i = 0; i < outputWeights.size(); i++) {
            canvasPane.getChildren().addAll(outputWeights.get(i));
        }

        for (int i = 0; i < inputWeights.size(); i++) {
            canvasPane.getChildren().addAll(inputWeights.get(i));
        }
    }

    /**
     * Calculate the degree of rotation for the weight label
     *
     * @param line - the line which label will be on
     * @return DoubleProperty that can be bind with rotateProperty
     */
    public DoubleProperty calcDegree(Line line) {
        double x = line.getEndX() - line.getStartX();
        double y = line.getEndY() - line.getStartY();
        return new SimpleDoubleProperty(Math.toDegrees(Math.atan(y / x)));
    }

    /**
     * Update the weight information on the graph
     */
    public void updateData() {
        for (int i = 0; i < inputLayerNodes.size(); i++) {
            for (int j = 0; j < hiddenLayerNodes.size(); j++) {
                inputWeights.get(i).get(j).setText(String.format("%.2f",
                                                                 theModel.getANN().getEdgeConnections()[0].getEdges()[j][i].getWeight()));
            }
        }
        for (int i = 0; i < hiddenLayerNodes.size(); i++) {
            for (int j = 0; j < outputLayerNodes.size(); j++) {
                outputWeights.get(i).get(j).setText(String.format("%.2f",
                                                                  theModel.getANN().getEdgeConnections()[1].getEdges()[j][i].getWeight()));
            }
        }
    }

    /**
     * Write the given resultList to the filePath provided
     *
     * @param resultList - a list of outputs classified
     * @param filePath - path for the output file
     */
    public void writeOutputs(ArrayList<ArrayList<Double>> resultList,
                             String filePath) {
        PrintWriter out;
        try {
            out = new PrintWriter(filePath);
            for (int i = 0; i < resultList.size(); i++) {
                for (int j = 0; j < resultList.get(i).size(); j++) {
                    if (j != 0) {
                        out.print(", ");
                    }
                    out.printf("%d", Math.round(resultList.get(i).get(j)));
                }
                out.println();
            }
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException");
        }
    }

    /**
     * Set ANN Model
     *
     * @param theModel
     */
    public void setModel(ANNModel theModel) {
        this.theModel = theModel;
    }

    class ANNTask extends Task<Double> {

        private ANNModel theModel;
        private LabeledInstances data;

        public ANNTask(ANNModel theModel, LabeledInstances data) {
            this.theModel = theModel;
            this.data = data;
        }

        @Override
        protected Double call() throws Exception {
            Double totalError = 0.0;
            ArrayList<ArrayList<Double>> output = theModel.getANN().classifyInstances(
                    data);
            int epoch;
            for (epoch = theModel.getANN().currEpoch; epoch < this.theModel.getANN().maxEpochs; epoch++) {
                theModel.getANN().currEpoch = epoch;
                if (isCancelled()) {
                    //change button
                    break;
                }
                totalError = theModel.getANN().learn(data, true, 1);
                if (epoch % 1000 == 0) {
                    output = theModel.getANN().classifyInstances(data);
                    totalError = theModel.getANN().computeOutputError(data,
                                                                      output);
                    if (totalError <= theModel.getANN().errStopThresh) {
                        System.out.println("SUCCESS!");
                        break;
                    }

                }

                updateValue(totalError);
                updateMessage(String.format("%d", epoch));
                updateProgress(epoch, this.theModel.getANN().maxEpochs);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        DesignController.this.updateData();
                    }
                });
                Thread.sleep(1);

                if (ifStep.get()) {
                    this.cancel(false);
                }
            }

            return totalError;
        }
    }
}
