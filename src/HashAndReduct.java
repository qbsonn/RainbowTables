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
     * Długośc łańcuchów w redukowanej tablicy
     */

    int chainLen;


    int space;
    long passwordSpace;
    long [] possiblepasswords;

    byte[] byteCharset;

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
     * Konstruktor
     * @param _hashType typ hasha
     * @param _charset zakres znaków
     */

    public HashAndReduct(String _hashType, String _charset, int _chainLen,int _minPwLength, int _maxPwLength)
    {
        hashType=_hashType;
        charset=_charset;
        byteCharset=toAsciiByte();
        chainLen=_chainLen;

         space = _maxPwLength - _minPwLength + 1; //Możliwe długości haseł


       possiblepasswords=new long [space];
        int startLenth=_maxPwLength;
        passwordSpace=0;
        long temp=0;
        for (int i=0; i<space;i++)
        {
            temp=(long)Math.pow(charset.length(),startLenth);

            if (i>0)
            {
                possiblepasswords[i]=temp+possiblepasswords[i-1];
            }
            else possiblepasswords[i]=temp;
            //  System.out.println(possiblepasswords[i]);
            passwordSpace+=temp;
            startLenth--;
        }




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
     * @param _minPwLength minimalna dlugosc slowa
     * @param _maxPwLength maksymalna długość słowa
     * @return obliczone słowo
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
            /*
            int space = _maxPwLength - _minPwLength + 1; //Możliwe długości haseł


            long [] possiblepasswords=new long [space];
            int startLenth=_maxPwLength;
            long passwordSpace=0;
            long temp=0;
            for (int i=0; i<space;i++)
            {
               temp=(long)Math.pow(charset.length(),startLenth);

                if (i>0)
                {
                    possiblepasswords[i]=temp+possiblepasswords[i-1];
                }
                else possiblepasswords[i]=temp;
              //  System.out.println(possiblepasswords[i]);
                passwordSpace+=temp;
                startLenth--;
            }

*/



            float div = (float) chainLen / (float) passwordSpace; //sprawdzanie czy dzielenie zmiennej chainLen i space jest liczba calkowita

            if (div % 1 == 0) {
                pom = 1;
            }


            for (int i = 1; i <= space; i++) {
                double max=(chainLen *(float)(  (float)possiblepasswords[i-1]/(float)passwordSpace))- pom;
                if (_functionNr <= max) {
                    currentLenght = _maxPwLength - (i - 1);
                    break;
                }

/*
            for (int i = 1; i <= space; i++) {
                if (_functionNr <= (chainLen / space * i) - pom) {
                    currentLenght = _maxPwLength - (i - 1);
                    break;
                }
                */


            }

        }



        byte[] result = new byte[currentLenght];
        int j=0;
        int hashLength=_hash.length;
        int hashIndex = 0;
        for (int i = 0; i < currentLenght; i++) {
            hashIndex = hashIndex + _hash[j%hashLength]^_functionNr;
            j = j + 7;


            result[i] = findInCharset( Math.abs(hashIndex) % charset.length());

        }
        return result;
    }
   

}