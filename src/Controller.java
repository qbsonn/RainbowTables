import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.collections.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.File;
import javafx.stage.FileChooser;
import java.awt.Desktop;
import java.io.IOException;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.*;




/**
 * Created by Kuba on 2016-12-10.
 */
public class Controller  implements  Initializable {

    private InputData input;
    private ArrayList<String> charsets;
    Desktop desktop = Desktop.getDesktop();

    Generator generator;
    String directory = System.getProperty("user.dir");

    Stage stage;

    String startPointsFile;


    @FXML
    Button tablePathButton;

    @FXML
    Button okButton, selectFileButton, calculateTimeButton;

    @FXML
    TextField passLen, tableName, chainNum, chainLen;

    @FXML
    Pane pane;

    @FXML
    private Label showPathLabel;

    @FXML
    private Label showTimeLabel;

    @FXML
    private Label showPathFile;
    @FXML
    private ComboBox hash, startPointComboBox, charsetComboBox;


    @FXML
    public void handleButtonAction(ActionEvent event) {
        input = new InputData();

        if (tableName.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Podaj nazwę tablicy");

            alert.showAndWait();
            return;


        }

        input.setTableName(tableName.getText());


        input.setDirectory(directory + "/");


        if (charsetComboBox.getSelectionModel().isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wybierz zakres znaków");
            alert.showAndWait();
            return;


        }
        input.setCharset(charsetComboBox.getValue().toString());


        if (hash.getSelectionModel().isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wybierz funkcję hashująca");

            alert.showAndWait();
            return;
        }

        input.setHashType(hash.getValue().toString());


        try {
            input.setChainCount(Integer.parseInt(chainNum.getText()));
            input.setChainLen(Integer.parseInt(chainLen.getText()));
            input.setPwLegth(Integer.parseInt(passLen.getText()));

        } catch (NumberFormatException e) {
            //  e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Dlugosc hasła, ilośc łańcuchow i ilosc haseł w łańcuchach muszą być liczbami");

            alert.showAndWait();
            return;


        }
        if (startPointsFile != null) {
            input.loadStartPoints(startPointsFile);
        }

        okButton.setVisible(false);

        generator = new Generator(input);


        Thread generateTable = new Thread(generator);
        //generateTable.start();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GenerateWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();

            GenerateWindowController controller = loader.getController();

            controller.initialize();

            controller.pogressBar.progressProperty().bind(generator.progressProperty());

            controller.progressIndicator.progressProperty().bind(generator.progressProperty());

            controller.infoLabel.textProperty().bind(generator.messageProperty());




            generator.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    controller.endButton.setVisible(true);
                   controller.timeLine.stop();

                }
            });



        } catch (IOException e) {
            e.printStackTrace();
        }


        generateTable.start();

        // generator.addListener(this);
        Stage thisWindow = (Stage) okButton.getScene().getWindow();

        thisWindow.close();



    }



    public void handleCalculateTime(ActionEvent event) {
        input = new InputData();

        if (hash.getSelectionModel().isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wybierz funkcję hashująca");

            alert.showAndWait();
            return;


        }
        System.out.println(hash.getValue().toString());
        input.setHashType(hash.getValue().toString());


        if (charsetComboBox.getSelectionModel().isEmpty() == true) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wybierz zakres znaków");
            alert.showAndWait();
            return;


        }
        input.setCharset(charsetComboBox.getValue().toString());

        try {
            input.setChainCount(Integer.parseInt(chainNum.getText()));
            input.setChainLen(Integer.parseInt(chainLen.getText()));
            input.setPwLegth(Integer.parseInt(passLen.getText()));

        } catch (NumberFormatException e) {
            //  e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Dlugosc hasła, ilośc łańcuchow i ilosc haseł w łańcuchach muszą być liczbami");

            alert.showAndWait();
            return;
        }

        int calculateNumberOfPasswords = input.getChainLen() * input.getChainCount();


        showTimeLabel.setVisible(true);

        showTimeLabel.setText("Szacunkowy czas generacji: " + String.valueOf(calculateTime(input)) + " sekund. \n" + "Liczba haseł w tablicy: " + calculateNumberOfPasswords);


    }


    public void handlePathChoose(ActionEvent event) {

        stage = (Stage) tablePathButton.getScene().getWindow();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Scieżka zapisu tablicy");
        File defaultDirectory = new File("C:/");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(stage);

        directory = selectedDirectory.getAbsolutePath();
        showPathLabel.setText(directory);

    }

    public void handleStartPointFileChooser(ActionEvent event) {
        stage = (Stage) selectFileButton.getScene().getWindow();
        FileChooser chooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(extFilter);


        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(stage);
        startPointsFile = file.getAbsolutePath();
        showPathFile.setVisible(true);
        showPathFile.setText(startPointsFile);


    }

    public void handlestartComboBoxChange(ActionEvent event) {
        if (startPointComboBox.getValue().toString() == "Z pliku..") {

            selectFileButton.setVisible(true);
        } else
            selectFileButton.setVisible(false);
        startPointsFile = null;
        showPathFile.setVisible(false);

    }


    public long calculateTime(InputData _input) {
        Generator gen = new Generator(_input);
        long totalTime;
        totalTime = gen.calculateExample() * _input.getChainCount() * _input.getChainLen() / 1000000000;


        return totalTime;


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        charsets = new ArrayList<>();

        ObservableList<String> charsetOptions = FXCollections.observableArrayList();


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("charset.txt"));

        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();

            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Nie można znaleść pliku charset.txt!!!");

            alert.showAndWait();
            charsetOptions.add("Brak opcji, sprawdź plik charset.txt");
        }
        try {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                charsets.add(line);
                charsetOptions.add(line);


            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Błąd odczytu pliku charset.txt!!!");

            alert.showAndWait();
            charsetOptions.add("Brak opcji, sprawdź plik charset.txt");
        }

        charsetComboBox.setItems(charsetOptions);


        ObservableList<String> hashOptions =
                FXCollections.observableArrayList(
                        "MD5",
                        "SHA1"
                );
        hash.setItems(hashOptions);


        ObservableList<String> startPointsOptions =
                FXCollections.observableArrayList(
                        "Wygenerowane",
                        "Z pliku.."
                );
        startPointComboBox.setItems(startPointsOptions);
        startPointComboBox.getSelectionModel().selectFirst();


        selectFileButton.setVisible(false);
        showPathLabel.setText(directory);
        showPathFile.setText("");
        showPathFile.setVisible(false);
        showTimeLabel.setVisible(false);





    }
}


