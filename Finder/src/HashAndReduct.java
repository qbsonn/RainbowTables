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


    int chainLen;

    /**
     * Tablica znaków z ktorych moga powstac slowa
     */
    byte[] byteCharset;
    String charset;

    public HashAndReduct(String _hashType, String _charset,int _chainLen)
    {
        hashType=_hashType;
        charset=_charset;
        byteCharset=toAsciiByte();
        chainLen=_chainLen;
    }


    /**
     *
     * Konwersja charsetu na tablice byte
     *
     * @return
     */
    public byte[] toAsciiByte()
    {
        byte[] array=new byte[charset.length()];

        for (int i=0;i<charset.length();i++)
        {
            char ch= charset.charAt(i);
            array[i] = (byte)ch;


        }
        String str=null;
        try {
            str = new String(array, "UTF-8");
        }
        catch(java.io.UnsupportedEncodingException e)
        {


        }
        //System.out.println(str);
        return array;
    }

    /**
     *
     *Przypisanie wartosci byte dla danego znaku
     * @param index
     * @return
     */
    public byte findInCharset(int index)
    {
        byte value=byteCharset[index];

        return value;
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
    public static String calculateHash(byte[] _bytesOfMessage, String hashType) {
        byte[] thedigest=null;
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            thedigest = md.digest(_bytesOfMessage);
        }
        catch ( java.security.NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : thedigest) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * Funkcja redukcji zwracajaca slowo z hasha
     *
     * @param _hash hash z który redukujemy
     * @param _functionNr   numer funkcji redukcji
     * @param _minPwLength minimalna dlugosc slowa
     * @param _maxPwLength maksymalna długośc słowa
     * @return obliczone słowo
     */
/*
    public byte[] reduce (byte[] _hash, int _functionNr, int _pwLength)
    {
        byte[] result = new byte[_pwLength];
        int j=0;
        int hashLength=_hash.length;
        int hashIndex = 0;
        for (int i = 0; i < _pwLength; i++) {
            hashIndex = hashIndex + _hash[j%hashLength]^_functionNr;
            j = j + _pwLength;


            result[i] = findInCharset( Math.abs(hashIndex) % charset.length());

        }
        return result;
    }
*/
    public byte[] reduce (byte[] _hash, int _functionNr, int _minPwLength,int _maxPwLength)
    {

        int currentLenght=_maxPwLength; //długość generowanego hasła
        int pom=0;

        if (_minPwLength==_maxPwLength)
        {
            currentLenght=_minPwLength;
        }

        else {
            int space = _maxPwLength - _minPwLength + 1; //Możliwe długości haseł

            float div = (float) chainLen / (float) space; //sprawdzanie czy dzielenie zmiennej chainLen i space jest liczba calkowita

            if (div % 1 == 0) {
                pom = 1;
            }


            for (int i = 1; i <= space; i++) {
                if (_functionNr <= (chainLen / space * i) - pom) {
                    currentLenght = _maxPwLength - (i - 1);
                    break;
                }
            }

        }



        byte[] result = new byte[currentLenght];
        int j=0;
        int hashLength=_hash.length;
        int hashIndex = 0;
        for (int i = 0; i < currentLenght; i++) {
            hashIndex = hashIndex + _hash[j%hashLength]^_functionNr;
            j = j + _minPwLength;


            result[i] = findInCharset( Math.abs(hashIndex) % charset.length());

        }
        return result;
    }


}