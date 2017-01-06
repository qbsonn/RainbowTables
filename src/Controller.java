import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.collections.*;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
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
    TextField minPassLen, tableName, chainNum, chainLen, maxPassLen;

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
    private CheckBox checkIsPerfectBox;

    /**
     * Metoda wywoływana po naciśnieciu przycisku OK
     * @param event
     */
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
            input.setMinPwLength(Integer.parseInt(minPassLen.getText()));
            input.setMaxPwLength(Integer.parseInt(maxPassLen.getText()));

        } catch (NumberFormatException e) {
            //  e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Dlugosc hasła, ilośc łańcuchow i ilosc haseł w łańcuchach muszą być liczbami całkowitymi");

            alert.showAndWait();
            return;


        }

        if (input.getChainCount()<1||input.getChainLen()<1||input.getMinPwLength()<1||input.getMaxPwLength()<1)
        {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Dlugosc hasła, ilośc łańcuchow i ilosc haseł w łańcuchach muszą być liczbą dodatnią");

            alert.showAndWait();
            return;
        }



        if (input.getMinPwLength()>input.getMaxPwLength())
        {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Minimalna długość hasła musi być większa liczbą niż maksymalna długość hasła!");

            alert.showAndWait();
            return;

        }





        if (startPointComboBox.getValue().toString()=="Z pliku..") {
            if (startPointsFile != null) {
                input.loadStartPoints(startPointsFile);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Wybrano opcje pobrania punktów startowych z pliku, ale nie podano ścieżki do pliku!");

                alert.showAndWait();
                return;

            }
        }


        if (checkIsPerfectBox.isSelected()==false)
        generator = new Generator(input,false);
        else
        generator=new Generator(input,true);

        Thread generateTable = new Thread(generator);
        //generateTable.start();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GenerateWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Generacja Tablicy");
            stage.setScene(new Scene((Pane) loader.load()));
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Czy napewno chcesz przerwać generacje tablicy?");

                ButtonType buttonTypeYes = new ButtonType("Tak");
                ButtonType buttonTypeNo = new ButtonType("Nie");

                alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeNo);



                Optional<ButtonType> result =alert.showAndWait();

                if (result.get() == buttonTypeYes) {


                    stage.close();
                    System.exit(1);
                }

            });

            GenerateWindowController controller = loader.getController();

          //  controller.initialize();
            controller.generate=generateTable;
            controller.interruptButton.setVisible(true);

            controller.pogressBar.progressProperty().bind(generator.progressProperty());

            controller.progressIndicator.progressProperty().bind(generator.progressProperty());

            controller.infoLabel.textProperty().bind(generator.messageProperty());




            generator.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    controller.interruptButton.setVisible(false);
                    controller.endButton.setVisible(true);

                   controller.stopClock=true;

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


    /**
     * Metoda wywoływana po naciśnieciu przycisku Oblicz czas generacji
     * @param event
     */
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
            input.setMinPwLength(Integer.parseInt(minPassLen.getText()));
            input.setMaxPwLength(Integer.parseInt(maxPassLen.getText()));

        } catch (NumberFormatException e) {
            //  e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Dlugosc hasła, ilośc łańcuchow i ilosc haseł w łańcuchach muszą być liczbami całkowitymi");

            alert.showAndWait();
            return;
        }

        if (input.getChainCount()<1||input.getChainLen()<1||input.getMinPwLength()<1||input.getMaxPwLength()<1)
        {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Dlugosc hasła, ilośc łańcuchow i ilosc haseł w łańcuchach muszą być liczbą dodatnią");

            alert.showAndWait();
            return;
        }



        if (input.getMinPwLength()>input.getMaxPwLength())
        {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Minimalna długość hasła musi być większa liczbą niż maksymalna długość hasła!");

            alert.showAndWait();
            return;

        }


        long calculateNumberOfPasswords = (long)input.getChainLen() * (long)input.getChainCount();


        showTimeLabel.setVisible(true);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss") ;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, (int)calculateTime(input));
        calendar.set(Calendar.SECOND, 0);

        long possiblePasswords=0;

                for (int i=input.getMinPwLength();i<=input.getMaxPwLength();i++) {
                  possiblePasswords+= (long) Math.pow(input.getCharset().length(), i);
                }
        double successRate=(double)calculateNumberOfPasswords/possiblePasswords*100;
        if (successRate>=100)
        {

            successRate=99;
        }

        String stringSucessRate = String.format("%.2f", successRate);

        double fileSize=((input.getMaxPwLength()+input.getMinPwLength()+2)*input.getChainCount()+50)/1000000;
        System.out.println("Suma:" +fileSize);

        //showTimeLabel.setText("Szacunkowy czas generacji: " + String.valueOf(calculateTime(input)) + " minut. \n" + "Liczba haseł w tablicy: " + calculateNumberOfPasswords);
        showTimeLabel.setText("Szacunkowy czas generacji: " + timeFormat.format(calendar.getTime()) + ". \n" + "Liczba haseł w tablicy: " + calculateNumberOfPasswords +"\n Liczba możliwych haseł dla charsetu: "+possiblePasswords+". \n Prawdopodobieństwo sukcesu: "+stringSucessRate+"%"+ "\n Pesymistyczna wielkość pliku: "+ fileSize+ " MB" );

    }

    /**
     * Metoda wywoływana przy naciśnieciu przycisku wyboru ścieżki zapisu tablicy
     * @param event
     */
    public void handlePathChoose(ActionEvent event) {

        stage = (Stage) tablePathButton.getScene().getWindow();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Scieżka zapisu tablicy");
        File defaultDirectory = new File("C:/");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(stage);
try {
    directory = selectedDirectory.getAbsolutePath();
    showPathLabel.setText(directory);
}
catch (java.lang.NullPointerException e)
{
    showPathLabel.setText(directory);

}

    }

    /**
     * Metoda wywoływana przy naciśnieciu przycisku wyboru pliku z początkowymi punktami łańcuchów
     * @param event
     */
    public void handleStartPointFileChooser(ActionEvent event) {
        stage = (Stage) selectFileButton.getScene().getWindow();
        FileChooser chooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(extFilter);


        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(stage);
        try {
            startPointsFile = file.getAbsolutePath();
            showPathFile.setVisible(true);
            showPathFile.setText(startPointsFile);
        }
        catch (java.lang.NullPointerException e)
        {
            e.printStackTrace();


        }


    }

    /**
     * Metoda wywoływana przy zmianie wyboru opcji generacji łańcuchów początkowych
     * @param event
     */
    public void handlestartComboBoxChange(ActionEvent event) {
        if (startPointComboBox.getValue().toString() == "Z pliku..") {

            selectFileButton.setVisible(true);
        } else
            selectFileButton.setVisible(false);
        startPointsFile = null;
        showPathFile.setVisible(false);

    }

    /**
     * Metoda obliczjaąca  czas generacji tablicy na podstawie ilości i długości łańcuchów
     * @param _input obiekt zawierająca dane wpisane do pól
     * @return
     */

    public float calculateTime(InputData _input) {
        Generator gen = new Generator(_input,"Example");
        float totalTime;
        float temp=0;
        for (int i=0;i<5;i++)
        {

            temp=(float)gen.calculateExample();
        }
        temp=temp/5;

        totalTime = temp * _input.getChainCount() * _input.getChainLen() / 1000000;


        return totalTime;


    }

    /**
     * Inicjalizacja
     * @param location
     * @param resources
     */
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


