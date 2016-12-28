import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Piotrek on 2016-12-27.
 */
public class Controller {

    private Finder finder;
    @FXML TextField textField;

    public void buttonOKClicked()
    {
        finder = new Finder("C:\\Users\\Piotrek\\Desktop\\dzialaMD5.dat");
        //byte[] hash = DatatypeConverter.parseHexBinary("eb8fc9f5bfe7a2b69bee0c035ec5e5d0");
        byte[] hash = DatatypeConverter.parseHexBinary(textField.getText());


        byte[] currentHash = hash;
        String currentValue = null;
        try {
            currentValue = new String(finder.hashAndReduct.reduce(currentHash,1,finder.valueLength), "UTF-8");
        }
        catch (UnsupportedEncodingException e){System.out.println("Exception");}

        for (int i =0; i< finder.chainLength; i++)
        {
            // sprawdź czy currentValue jest w tablicy
            //System.out.println("Sprawdza czy "+ currentValue + " jest");
            int index = finder.exists(currentValue);
            if (index >= 0)
            {
                // Znalazł
                System.out.println("Znalazł");
                String value = finder.getFirstValue(index);
                if (value != null)
                {
                    System.out.println("Znalezione haslo: "+ value);
                    String correctValue = findValue(hash,value);
                    System.out.println("Poszukiwane haslo to: "+ correctValue);
                    return;
                }
            }
            // wykonaj funkcję hashującą na currentValue
            currentHash = finder.hashAndReduct.calculateHash(currentValue.getBytes());
            // currentValue = redukcja z hasha
            try {
                currentValue = new String(finder.hashAndReduct.reduce(currentHash,1,finder.valueLength), "UTF-8");
            }
            catch (UnsupportedEncodingException e){System.out.println("Exception");}
        }
        System.out.println("Nie znalazl");

    }

    private String findValue(byte[] hash, String firstValue)
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
                currentValue = new String(finder.hashAndReduct.reduce(currentHash,1,finder.valueLength), "UTF-8");
            }
            catch (UnsupportedEncodingException e){System.out.println("Exception");}
        }
        return null;
    }
}
