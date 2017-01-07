import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javax.xml.bind.DatatypeConverter;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.FileOutputStream;


/**
 * Created by Kuba on 2016-12-03.
 */
public class Generator extends Task implements Runnable {

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
    private  List<Chain> chains;
    /**
     * Tablica przechowujaca punkty startowe lancuchow
     */
    private Set<String> startPoints;
    /**
     * Minimalna dlugosc generowanych hasel
     */
    private int minPwLength;

    /**
     * Maksymalna długośc generowanych haseł
     */


    private int maxPwLength;

    /**
     * Sciezka zapisu
     */
    private String directory;


    /**
     * Konstruktor klasy Generator
     * @param _input
     */

    private Set<Chain> perfectChains;


    boolean perfectTable;


    public Generator(InputData _input, boolean _perfect)
    {
        tableName=_input.getTableName();
        chainLen=_input.getChainLen();
        chainCount=_input.getChainCount();
        hashType=_input.getHashType();
        charset=_input.getCharset();
        perfectChains=new HashSet<>();
        minPwLength=_input.getMinPwLength();
        maxPwLength=_input.getMaxPwLength();
        startPoints=new HashSet<>();
        chains=new ArrayList<>();
        perfectTable=_perfect;
        if (_input.getStartPoints().size()!=0)
        {
            startPoints.addAll(_input.getStartPoints());

        }


        directory=_input.getDirectory();

        //calculateStartPoints();


    hr=new HashAndReduct( hashType,charset,chainLen,minPwLength,maxPwLength);

        System.out.println("Nazwa tablicy: "+tableName+"algorytm: "+hashType+"dlugosc lan: "+chainLen+"ilosc: "+chainCount);



    }


  public  Generator(InputData _input, String _type)
    {


        chainLen=_input.getChainLen();
        chainCount=_input.getChainCount();
        hashType=_input.getHashType();
        charset=_input.getCharset();
        perfectChains=new HashSet<>();
        minPwLength=_input.getMinPwLength();
        maxPwLength=_input.getMaxPwLength();
        startPoints=new HashSet<>();




        hr=new HashAndReduct( hashType,charset, chainLen,minPwLength,maxPwLength);

        System.out.println("Prognoza->> Nazwa tablicy: "+tableName+"algorytm: "+hashType+"dlugosc lan: "+chainLen+"ilosc: "+chainCount);

    }


/*
    public void calculateStartPoints()
    {

        updateMessage("Generowanie punktów początkowych...");
        Random rand=new Random();
        int code;

        int alreadyDone=startPoints.size();

        for (int i=0;i<chainCount-alreadyDone;i++)
        {

            StringBuilder sb = new StringBuilder();
           for (int j=0; j<pwLength; j++)
           {
             code= rand.nextInt(charset.length());
               sb.append( foundCharInCharset(code));

           }
                startPoints.add(sb.toString());
           // updateProgress(i,chainLen*chainCount+chainCount+chainLen);


        }


        System.out.println("Utworzono poczatkowe punkty");
    }
*/

    /** Metoda obliczająca punkty początkowe łańcuchów*/

    public void calculateStartPoints()
    {

        updateMessage("Generowanie punktów początkowych...");
        Random rand=new Random();

        double possiblePasswords=Math.pow(charset.length(),maxPwLength);
        int code;
        double max=0;
        int alreadyDone=startPoints.size();
        updateProgress(0,100);

        if (possiblePasswords<chainCount)
        {
            max=possiblePasswords;
        }
        else
        max=chainCount;

        while (startPoints.size()<max)
        {
            if (startPoints.size()==max/2)
                updateProgress(0.3,100);

            StringBuilder sb = new StringBuilder();
            for (int j=0; j<maxPwLength; j++)
            {
                code= rand.nextInt(charset.length());

                sb.append( foundCharInCharset(code));

            }
            startPoints.add(sb.toString());
   //         System.out.println(sb.toString());


        }
        updateProgress(1,100);

        System.out.println("Utworzono poczatkowe punkty");
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

    /**
     * Metoda zwracająca znak na podstawie jego kodu
     * @param _code kod znaku
     * @return znak
     */
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
        updateMessage("Trwa generacja tablicy... To może chwilę potrwać...");
        System.out.println("Tworzenie");
        updateProgress(1.5,100);
        int total=chainCount*chainLen;
        int actual=2*total/100;

        long start=System.currentTimeMillis();
      byte[] word;


        for (String startPoint : startPoints)

        {       word=startPoint.getBytes(StandardCharsets.UTF_8);
                byte[] hash=null;


            for (int j=0;j<chainLen;j++)
            {
                hash=hr.calculateHash(word);
               //convertHash(hash);
               // word=hr.reduce(hash,1,pwLength);
                word=hr.reduce(hash,j,minPwLength,maxPwLength);

                try {
                    System.out.println(new String(word, "UTF-8"));
                }
                catch(Exception e){}

                /*
                String str=null;
                try {
                    str = new String(word, "UTF-8");
                }
                catch(java.io.UnsupportedEncodingException e)
                {
                    e.printStackTrace();

                }

                System.out.println(str);
                */
                actual++;
                updateProgress(actual,total*1.04);


            }
            String end=null;
            try {
                end = new String(word, "UTF-8");
            }
            catch(java.io.UnsupportedEncodingException e)
            {
                e.printStackTrace();

            }
            chains.add(new Chain(startPoint,end));

        }

       System.out.println("Utworzono tablice" +chains.size());
         updateProgress(98,100);
        updateMessage("Generacja zakończona. Trwa sortowanie");

       // uniqueChains.addAll(chains);


        //chains.clear();
        updateProgress(99,100);
        Collections.sort(chains);

      //  updateProgress(actual+chainLen,total+chainLen+chainCount);
        //System.out.println("posortowano");
        updateMessage("Trwa zapis tablicy do pliku");
        saveToFile();
        updateProgress(100,100);
        updateMessage("Zapis zakończony. Tablica jest gotowa do uzycia!");
//saveString();
        long stop=System.currentTimeMillis();

/*
        for (Chain key: chains){


            System.out.println(key.getStartPoint());
            System.out.println(key.getEndPoint());

          //  printHash( chains.get(key));



        }
        */

        System.out.println("Czas wykonania:"+(stop-start)/1000 +" sekund");






    }


    public void initPerfectTable()

    {
        updateMessage("Trwa generacja tablicy... To może chwilę potrwać...");
        System.out.println("Tworzenie");
        updateProgress(2,100);
        int total=chainCount*chainLen;
        int actual=2*total/100;

        long start=System.currentTimeMillis();
        byte[] word;


        for (String startPoint : startPoints)

        {       word=startPoint.getBytes(StandardCharsets.UTF_8);
            byte[] hash=null;


            for (int j=0;j<chainLen;j++)
            {
                hash=hr.calculateHash(word);
                //convertHash(hash);
                // word=hr.reduce(hash,1,pwLength);
                word=hr.reduce(hash,j,minPwLength,maxPwLength);
            /*
                try {
                    System.out.println(new String(word, "UTF-8"));
                }
                catch(Exception e){}
*/
                /*
                String str=null;
                try {
                    str = new String(word, "UTF-8");
                }
                catch(java.io.UnsupportedEncodingException e)
                {
                    e.printStackTrace();

                }

                System.out.println(str);
                */
                actual++;
                updateProgress(actual,total*1.04);


            }
            String end=null;
            try {
                end = new String(word, "UTF-8");
            }
            catch(java.io.UnsupportedEncodingException e)
            {
                e.printStackTrace();

            }
            perfectChains.add(new Chain(startPoint,end));

        }

        // System.out.println("Utworzono tablice");
        updateProgress(98,100);
        updateMessage("Generacja zakończona. Trwa sortowanie");

         chains.addAll(perfectChains);


        perfectChains.clear();
        updateProgress(99,100);
        Collections.sort(chains);

        //  updateProgress(actual+chainLen,total+chainLen+chainCount);
        //System.out.println("posortowano");
        updateMessage("Trwa zapis tablicy do pliku");
        saveToFile();
        updateProgress(100,100);
        updateMessage("Zapis zakończony. Tablica jest gotowa do uzycia!");
//saveString();
        long stop=System.currentTimeMillis();

/*
        for (Chain key: chains){


            System.out.println(key.getStartPoint());
            System.out.println(key.getEndPoint());

          //  printHash( chains.get(key));



        }
        */

        System.out.println("Czas wykonania:"+(stop-start)/1000 +" sekund");






    }



    /**
     * Metoda konwertujaca hash z typu byte na string i wyswietlajaca hash w konsoli
     * @param _hash
     */

    public void printHash(byte[] _hash)
    {

        StringBuilder sb = new StringBuilder();
        for (byte b : _hash ) {
            sb.append(String.format("%02X ", b));
        }
    //    System.out.println(sb.toString());


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
     * Metoda konwertująca hash w postaci bitowej na postać szesnastkową
     * @param _hash hash
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
     * Metoda zapisujaca tablice do pliku
     */
    public void saveToFile()
    {
        try {
            FileOutputStream fos = new FileOutputStream(directory+tableName+hashType+".dat");


            fos.write("Lancuchy: ".getBytes());
            fos.write(String.valueOf(chains.size()).getBytes());


            fos.write(" Dlugosc: ".getBytes());
            fos.write(String.valueOf(chainLen).getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            fos.flush();


            fos.write("Min_Dlugosc_hasla: ".getBytes());
            fos.write(String.valueOf(minPwLength).getBytes());

            fos.write(" Max_Dlugosc_hasla: ".getBytes());
            fos.write(String.valueOf(maxPwLength).getBytes());


            fos.write(" Hash: ".getBytes());
            fos.write(hashType.getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            fos.flush();

            fos.write("Charset: ".getBytes());
            fos.write(charset.getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            fos.flush();


            for (Chain key: chains){
                fos.write(key.getStartPoint().getBytes());
                fos.write(" ".getBytes());
                fos.write(key.getEndPoint().getBytes());
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


    public void saveString()
    {
        try {
            PrintWriter fos = new PrintWriter(tableName+hashType+"s"+".txt");
            int i=1;

            for (Chain key: chains){
                fos.println(i+ " "+key.getStartPoint()+" "+key.getEndPoint());

             i++;
            }
            fos.close();
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();

        }

    }

    /**
     * Metoda zwracająca czas przetworzenia jednego elementu łańcucha
     * @return czas przetwarzania
     */
    public long calculateExample()
    {

        Random rand=new Random();
        int code;
    String startPoint;

        StringBuilder sb = new StringBuilder();
        for (int j=0; j<minPwLength; j++)
        {
            code= rand.nextInt(charset.length());
            sb.append( foundCharInCharset(code));

        }
        startPoint=sb.toString();
        byte[] word;

        word=startPoint.getBytes(StandardCharsets.UTF_8);
        byte[] hash=null;


        long start=System.nanoTime();
        hash=hr.calculateHash(word);
        word=hr.reduce(hash,1,minPwLength,maxPwLength);
        long stop=System.nanoTime();
        long time=stop-start;
        System.out.println("Czas: "+time);

        return time;
    }



    @Override
    protected Object call() throws Exception {

        calculateStartPoints();
        System.out.println("Liczba poczatku: "+startPoints.size());
        if (perfectTable==false)
        initTable();
        else
        initPerfectTable();

        return null;
    }


    }

