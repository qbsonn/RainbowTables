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
        finder = new Finder("C:\\Users\\Piotrek\\Desktop\\test4literyMD5.dat");
        //byte[] hash = DatatypeConverter.parseHexBinary("eb8fc9f5bfe7a2b69bee0c035ec5e5d0");
        byte[] hash = DatatypeConverter.parseHexBinary(textField.getText());

        String correctValue;
        for (int i=finder.chainLength; i>=0; i--)
        {
            if ((correctValue = findValueInAllChains2(hash,i)) != null)
            {
                System.out.println(i+" Poszukiwane haslo to: "+ correctValue);
               // break;
            }
        }
        System.out.println("Koniec");
    }

    private String findValueInAllChains(byte[] hash, int startReductionFunction)
    {
        byte[] currentHash = hash;
        String currentValue = null;
        try {
            currentValue = new String(finder.hashAndReduct.reduce(currentHash,startReductionFunction,finder.valueLength), "UTF-8");
        }
        catch (UnsupportedEncodingException e){System.out.println("Exception");}

        for (int i = startReductionFunction+1; i<= finder.chainLength ; i++)
        {
            // sprawdź czy currentValue jest w tablicy
            System.out.println("Sprawdza czy "+ currentValue + " jest");
             int index = finder.exists(currentValue);
            if(index >= 0)
            {
                // Znalazł
                //System.out.println(startReductionFunction+" Znalazł");
                String value = finder.getFirstValue(index);
                if (value != null)
                {
                   // System.out.println("Pierwsze: "+ value);
                    String correctValue = findValueInChain(hash,value);
                    if (correctValue != null)
                        return correctValue;
                }
            }
            // wykonaj funkcję hashującą na currentValue
            currentHash = finder.hashAndReduct.calculateHash(currentValue.getBytes());
            // currentValue = redukcja z hasha
            try {
                currentValue = new String(finder.hashAndReduct.reduce(currentHash,i,finder.valueLength), "UTF-8");
            }
            catch (UnsupportedEncodingException e){System.out.println("Exception");}
        }
        //System.out.println("Nie znalazl "+ startReductionFunction);
        return null;
    }
    private String findValueInAllChains2(byte[] hash, int startReductionFunction)
    {
        byte[] currentHash = hash;
        byte[] currentValue = finder.hashAndReduct.reduce(currentHash,startReductionFunction,finder.valueLength);

        for (int i = startReductionFunction+1; i< finder.chainLength ; i++)
        {
           // System.out.println("Sprawdza czy "+ currentValue + " jest");
            currentHash = finder.hashAndReduct.calculateHash(currentValue);
            currentValue = finder.hashAndReduct.reduce(currentHash,i,finder.valueLength);
        }
        try {
            int index = finder.exists(new String(currentValue, "UTF-8"));
            if (index >= 0)
            {
                System.out.println(startReductionFunction+" Znalazł");
                String value = finder.getFirstValue(index);
                if (value != null)
                {
                    String correctValue = findValueInChain(hash, value);
                    if (correctValue != null)
                        return correctValue;
                }
            }
        }
        catch (UnsupportedEncodingException e){System.out.println("Exception");}
        return null;
    }
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
                currentValue = new String(finder.hashAndReduct.reduce(currentHash,i,finder.valueLength), "UTF-8");
            }
            catch (UnsupportedEncodingException e){System.out.println("Exception");}
        }
        return null;
    }
}
