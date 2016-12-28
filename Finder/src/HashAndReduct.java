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
     * @param _bytesOfMessage tekst na podstawie ktorego liczony jest hash
     * @return obliczony hash
     */
    public byte[] calculateHash(byte[] _bytesOfMessage) {
        byte[] thedigest=null;
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            thedigest = md.digest(_bytesOfMessage);
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
     * @return obliczone słowo
     */

    public byte[] reduce (byte[] _hash, int _functionNr, int _pwLength)
    {
        byte[] result = new byte[_pwLength];
        int j=0;
        int hashLength=_hash.length;
        int hashIndex = 0;
        for (int i = 0; i < _pwLength; i++) {
            hashIndex = hashIndex + _hash[j%hashLength]^_functionNr;
            j = j + _pwLength;

            result[i] = (byte)( Math.abs(hashIndex) % charset.length());

        }
        return result;
    }

    public String fromByteToString(byte[] _text) {

        StringBuilder sb = new StringBuilder();
        for (byte b : _text) {


            sb.append(foundCharInCharset(b));
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    public byte[] fromStringToByte(String text)
    {
       byte[] bytes = new byte[text.length()];
        int j=0;
       for (char a : text.toCharArray())
       {
           int x = -1;
           for (int i=0;i<charset.length();i++)
           {
               if (a == charset.charAt(i)) {
                   x = i;
                   break;
               }
           }
           bytes[j] = (byte)x;
           j++;
       }
       return bytes;
    }

    public char foundCharInCharset(byte _byte)
    {

        int number=(int)_byte;
        int x=-1;
//System.out.println(charset);
//System.out.println(number+"num");
        for (int i=0;i<charset.length();i++)
        {
            if (number ==i)
            {  x=i;
                break;
            }
        }
        //   System.out.println("pozy: "+x);

        return charset.charAt(x);
    }
}