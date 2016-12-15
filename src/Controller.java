import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.collections.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;



/**
 * Created by Kuba on 2016-12-10.
 */
public class Controller  implements  Initializable {

    private InputData input;
    Generator generator;


    @FXML
    Button okButton;

    @FXML
    TextField passLen, tableName, chainNum, chainLen;

    @FXML
    Pane pane;


    @FXML
    private ComboBox hash;


    @FXML
    public void handleButtonAction(ActionEvent event) {
        input=new InputData();
        input.setTableName(tableName.getText());




    if (hash.getSelectionModel().isEmpty()==true)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText("Wybierz funkcję hashująca");

        alert.showAndWait();
        return;


    }

System.out.println(hash.getValue().toString());
        input.setHashType(hash.getValue().toString());



        try {
            input.setChainCount(Integer.parseInt(chainNum.getText()));
            input.setChainLen(Integer.parseInt(chainLen.getText()));
            input.setPwLegth(Integer.parseInt(passLen.getText()));

        }
        catch (NumberFormatException e)
        {
          //  e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Dlugosc hasła, ilośc łańcuchow i ilosc haseł w łańcuchach muszą być liczbami");

            alert.showAndWait();
            return;


        }


        generator=new Generator(input);
        generator.initTable();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {




        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "MD5",
                        "SHA1"
                );
       hash.setItems(options);

    }
   }

