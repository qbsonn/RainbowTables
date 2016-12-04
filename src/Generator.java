import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;
import java.io.FileOutputStream;

/**
 * Created by Kuba on 2016-12-03.
 */
public class Generator {

    /**
     * Nazwa tablicy
     */
    private String tableName;
    /**
     * Dlugosc lancucha
     */
    private int chainLen;
    /**
     * Liczba lancuchow
     */
    private int chainCount;
    /**
     * Typ hasha
     */
    private String hashType;
    /**
     * Zakres znakow
     */
    private String charset;
    /**
     * Obiekt odpowiadajacy za obliczanie hashy i ich redukowanie
     */
    private HashAndReduct hr;
    /**
     * Struktura danych przechowujaca lancuchy
     */
    private  HashMap<String,byte[]> chains;
    /**
     * Tablica przechowujaca punkty startowe lancuchow
     */
    private String[] startPoints;
    /**
     * Dlugosc generowanych hasel
     */
    private int pwLength;




    /**
     * Konstruktor klasy Generator
     * @param _input
     */

    public Generator(Input _input)
    {
        tableName=_input.getTableName();
        chainLen=_input.getChainLen();
        chainCount=_input.getChainCount();
        hashType=_input.getHashType();
        charset=_input.getCharset();
        chains=new HashMap<>();
        pwLength=_input.getPwLegth();
        startPoints=new String[chainCount];

        calculateStartPoints();

    hr=new HashAndReduct( hashType,charset);

    }






    public void calculateStartPoints()
    {
        Random rand=new Random();
        int code;
        for (int i=0;i<chainCount;i++)
        {

            StringBuilder sb = new StringBuilder();
           for (int j=0; j<pwLength; j++)
           {
             code= rand.nextInt(charset.length());
               sb.append( foundCharInCharset(code));

           }
                startPoints[i]=sb.toString();


        }


    }


    /**
     * Metoda zwracajaca znak z charsetu na podstawie argumentu typu byte
     * @param _byte znak w postaci bajtowej
     * @return znak char
     */

    public char foundCharInCharset(byte _byte)
    {

        int number=(int)_byte;
        int x=-1;
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

    public char foundCharInCharset(int _code)
    {
        int x=-1;

        for (int i=0;i<charset.length();i++)
        {
            if (_code ==i)
            {  x=i;
                break;


            }

        }
        //   System.out.println("pozy: "+x);

        return charset.charAt(x);
    }


    /**
     * Metoda tworząca tablice
     */
    public void initTable()
    {
        String word;
        for (int i=0;i<chainCount;i++)
        {       word=startPoints[i];
                byte[] hash=null;


            for (int j=0;j<chainLen;j++)
            {
                hash=hr.calculateHash(word);
               // convertHash(hash);
               word=fromByteToString(hr.reduce(hash,j,pwLength,i));


            }

            chains.put(startPoints[i],hash);

        }

        for (String key: chains.keySet()){


            System.out.println(key);
           convertHash( chains.get(key));



        }

        saveToFile();
    }

    /**
     * Metoda konwertujaca hash z typu byte na string i wyswietlajaca hash w konsoli
     * @param _hash
     */

    public void convertHash(byte[] _hash)
    {

        StringBuilder sb = new StringBuilder();
        for (byte b : _hash ) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb.toString());


    }

    /**
     * Metoda konwertujaca slowo z postaci byte na typ string
     * @param _text wyraz w formacie byte
     * @return
     */



    public String fromByteToString(byte[] _text) {

        StringBuilder sb = new StringBuilder();
        for (byte b : _text) {


            sb.append(foundCharInCharset(b));
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * Metoda zapisujaca tablice do pliku
     */

    public void saveToFile()
    {
        try {
            FileOutputStream fos = new FileOutputStream(tableName+hashType+".txt");

            for (String key: chains.keySet()){

                fos.write(key.getBytes());
                fos.write(" ".getBytes());
                fos.write(chains.get(key));
                fos.write(System.getProperty("line.separator").getBytes());
                fos.flush();




            }
            fos.close();
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();

        }



    }


}
