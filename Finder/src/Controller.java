import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

/**
 * Created by Piotrek on 2016-12-27.
 */
public class Controller {

    private Finder finder;

    public void buttonOKClicked()
    {
       finder = new Finder("C:\\Users\\Piotrek\\Desktop\\eMD5.dat");
        //byte[] hash = "7e7e1fb52ad818f9c158ce36de22fdf6".getBytes();
        byte[] hash = DatatypeConverter.parseHexBinary("96f36e955a9951f12c2898ab1b68ba4f");
       // System.out.println("nowy:" + dd + " " + "dlugosc "+ dd.length);
        //System.out.println("hash: "+ hash);
        byte[] hash2 = finder.hashAndReduct.calculateHash(finder.hashAndReduct.fromStringToByte("CFE"));
        for (int i=0; i<hash.length; i++)
        {
            System.out.println("hash: "+ hash[i] + "   "+ hash2[i]);
        }
        byte[] currentHash = hash;
        //String currentValue = "JPK";
        String currentValue = finder.hashAndReduct.fromByteToString(finder.hashAndReduct.reduce(currentHash,1,finder.valueLength));

        for (int i =0; i< finder.chainLength; i++)
        {
            // sprawdź czy currentValue jest w tablicy
            System.out.println("Sprawdza czy "+ currentValue + " jest // u: ");
           int index = finder.exists(currentValue);
            if (index >= 0)
            {
                // Znalazł
                System.out.println("Znalazł");
                String value = finder.getFirstValue(index);
                if (value != null)
                {
                    System.out.println("Znalezione haslo: "+ value);
                    return;
                }
            }
            // wykonaj funkcję hashującą na currentValue
            currentHash = finder.hashAndReduct.calculateHash(finder.hashAndReduct.fromStringToByte(currentValue));
            // currentValue = redukcja z hasha
            currentValue = finder.hashAndReduct.fromByteToString(finder.hashAndReduct.reduce(currentHash,1,finder.valueLength));
        }
        System.out.println("Nie znalazl");

   /*   String value = "TES";
        byte[] ll = finder.hashAndReduct.fromStringToByte(value);
        for (byte l : ll)
            System.out.println(l);
        System.out.println("wyszlo "+finder.hashAndReduct.fromByteToString(ll)); */
    }
}
