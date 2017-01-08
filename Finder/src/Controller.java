import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Created by Piotrek on 2016-12-27.
 */
public class Controller implements Initializable{

    private Finder finder;
    @FXML TextField finderHashTextField;
    @FXML ComboBox comboBox;
    @FXML TextField generatorTextField;
    @FXML TextField generatorHashTextField;
    @FXML TextField finderValueTextField;
    @FXML Label rainbowTableLabel;
    @FXML Label label;
    /** Zmienna określająca czy program wykonuje proces wyszukiwania hasha*/
    private boolean busy;

    /** Metoda wywoływana po starcie aplikacji*/
    public void initialize(URL location, ResourceBundle resources)
    {
        comboBox.getItems().add("MD5");
        comboBox.getItems().add("SHA1");
        busy = false;
    }

    /** Metoda wywoływana po kliknięciu przycisku Szukaj
     * Rozpoczyna działanie wątku, który wyszukuje hasło w tablicy. */
    public void buttonSzukajClicked()
    {
        if (!busy) {
            if (finder == null) {
                System.out.println("Nie ma teczowej tablicy");
                return;
            }
            finderValueTextField.setText("");
            new Thread(){
                public void run()
                {
                    lookThroughRainbowTable();
                }
            }.start();
        }
    }

    /** Metoda przeszukująca tęczową tablicę w celu znalezienia hasła*/
    public void lookThroughRainbowTable()
    {
        byte[] hash = DatatypeConverter.parseHexBinary(finderHashTextField.getText());

        String correctValue;
        for (int i=finder.chainLength; i>=0; i--)
        {
            final int index = i;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    label.setText("Zaczyna od: "+ index);
                }
            });

            if ((correctValue = findValueInAllChains(hash,i)) != null)
            {
                System.out.println(i+" Poszukiwane haslo to: "+ correctValue);
                finderValueTextField.setText(correctValue);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        label.setText("Wyszukiwanie zakończone");
                    }
                });
                break;
            }
        }
        if (finderValueTextField.getText().matches("")) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    label.setText(sdf.format(cal.getTime()) + " Nie znaleziono hasła");
                }
            });
        }
    }

    /** Metoda przeszukująca tęczową tablicę od odpowiedniej pozycji określonej przez zmienną startReductionFunction */
    private String findValueInAllChains(byte[] hash, int startReductionFunction)
    {
        byte[] currentHash = hash;
        byte[] currentValue = finder.hashAndReduct.reduce(currentHash,startReductionFunction,finder.minValueLength,finder.maxValueLength);

        for (int i = startReductionFunction+1; i< finder.chainLength ; i++)
        {
            currentHash = finder.hashAndReduct.calculateHash(currentValue);
            currentValue = finder.hashAndReduct.reduce(currentHash,i,finder.minValueLength,finder.maxValueLength);
        }
        try {
            int index = finder.exists(new String(currentValue, "UTF-8"));
            if (index >= 0)
            {
                String correctValue = tryToFindCorrectValue(hash,index);
                if(correctValue != null)
                    return correctValue;

                boolean exists = true;
                int j = 1;
                while (exists)
                {
                    exists = false;
                    if (finder.exists(new String(currentValue, "UTF-8"),index-j))
                    {
                        exists = true;
                        correctValue = tryToFindCorrectValue(hash,index-j);
                        if (correctValue != null)
                            return correctValue;
                    }
                    if (finder.exists(new String(currentValue, "UTF-8"),index+j))
                    {
                        exists = true;
                        correctValue = tryToFindCorrectValue(hash,index+j);
                        if (correctValue != null)
                            return correctValue;
                    }
                    j++;
                }
            }
        }
        catch (UnsupportedEncodingException e){}
        return null;
    }

    /** Metoda przeszukująca dany łańcuch określony przez numer index */
    private String tryToFindCorrectValue(byte[] hash,int index)
    {
        String value = finder.getFirstValue(index);
        if (value != null)
        {
            String correctValue = findValueInChain(hash, value);
            return correctValue;
        }
        return null;
    }

    /** Metoda przechodząca od pierwszego słowa w łańcuchu i szukająca hasha */
    private String findValueInChain(byte[] hash, String firstValue)
    {
        String currentValue = firstValue;
        byte[] currentHash;

        for (int i=0; i<= finder.chainLength; i++)
        {
            currentHash = finder.hashAndReduct.calculateHash(currentValue.getBytes());
            boolean isTheSame = true;
            for (int j=0;j<hash.length;j++)
            {
                if (hash[j] != currentHash[j])
                {
                    isTheSame = false;
                    break;
                }
            }
            if (isTheSame)
                return currentValue;

            try {
                currentValue = new String(finder.hashAndReduct.reduce(currentHash,i,finder.minValueLength,finder.maxValueLength), "UTF-8");
            }
            catch (UnsupportedEncodingException e){}
        }
        return null;
    }

    /** Wywoływane po naciśnięciu przycisku ">>" */
    public void displayHashOnFinderHashTextField()
    {
        finderHashTextField.setText(generatorHashTextField.getText());
    }

    /** Wywoływane po nasićnięciu przycisku "OK" w Generatorze skrotów*/
    public void generateHash()
    {
        String value = generatorTextField.getText();
        generatorHashTextField.setText(HashAndReduct.calculateHash(value.getBytes(),(String)comboBox.getSelectionModel().getSelectedItem()));
    }

    /** Metoda wczytująca tęczową tablicę z pliku, wywoływana po wciśnięciu przycisku */
    public void getRainbowTable()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null)
        {
            finder = new Finder(selectedFile.getAbsolutePath());
            rainbowTableLabel.setText(selectedFile.getAbsolutePath());
        }
    }
}
