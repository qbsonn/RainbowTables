import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Created by Kuba on 2016-12-03.
 */

/**
 * Klasa obliczajaca hasha i redukuja hashe w celu utworzenia nowych slow
 */
public class HashAndReduct {

    /**
     * Pole przechowujące typ funkcji hashujacej
     */

    String hashType;


    /**
     * Tablica znaków z ktorych moga powstac slowa
     */

    String charset;

    /**
     * Konstruktor
     * @param _hashType typ hasha
     * @param _charset zakres znaków
     */

    public HashAndReduct(String _hashType, String _charset)
    {
        hashType=_hashType;
        charset=_charset;


    }

    /**
     * Funkcja obliczająca i zwracająca obliczony hash
     *
     * @param _text tekst na podstawie ktorego liczony jest hash
     * @return obliczony hash
     */
    public byte[] calculateHash(String _text) {
        byte[] bytesOfMessage = _text.getBytes(StandardCharsets.UTF_8);
        byte[] thedigest=new byte[1];
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
             thedigest = md.digest(bytesOfMessage);

            System.out.println(thedigest);



        }
        catch ( java.security.NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return thedigest;

    }

    /**
     * Funkcja redukcji zwracajaca slowo z hasha
     *
     * @param _hash hash z który redukujemy
     * @param _functionNr   numer funkcji redukcji
     * @param _pwLength dlugosc slowa
     * @param _chainNumber numer łańcucha
     * @return obliczone słowo
     */

    public byte[] reduce (byte[] _hash, int _functionNr, byte _pwLength, int _chainNumber)
    {
        byte[] result = new byte[_pwLength];
        int j=0;
        int hashLength=_hash.length;
        int hashIndex = 0;
        for (int i = 0; i < _pwLength; i++) {
            hashIndex = hashIndex + _hash[(_chainNumber+j)%hashLength]^_functionNr;
            j = j + _pwLength;
            result[i] = (byte)( Math.abs(hashIndex) % charset.length());
        }
        return result;
    }






}






