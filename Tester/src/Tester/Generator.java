package Tester;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.FileOutputStream;

/**
 * Created by Kuba on 2016-12-03.
 */
public class Generator  {

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
    public  List<Chain> chains;
    /**
     * Tablica przechowujaca punkty startowe lancuchow
     */
    private ArrayList<String> startPoints;
    /**
     * Dlugosc generowanych hasel
     */
    private int pwLength;

    /**
     * Sciezka zapisu
     */
    private String directory;


    /**
     * Konstruktor klasy Generator
     * @param _input
     */

    public Generator(InputData _input)
    {
        tableName=_input.getTableName();
        chainLen=_input.getChainLen();
        chainCount=_input.getChainCount();
        hashType=_input.getHashType();
        charset=_input.getCharset();
        chains=new LinkedList<>();
        pwLength=_input.getPwLegth();
        startPoints=new ArrayList<>();
        if (_input.getStartPoints().size()!=0)
        {
            startPoints=_input.getStartPoints();

        }


        directory=_input.getDirectory();

        calculateStartPoints();

        hr=new HashAndReduct( hashType,charset);

        System.out.println("Nazwa tablicy: "+tableName+"algorytm: "+hashType+"dlugosc lan: "+chainLen+"ilosc: "+chainCount);



    }


    public void calculateStartPoints()
    {
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


        }

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
        int total=chainCount*chainLen;
        int actual=0;

        long start=System.currentTimeMillis();
        System.out.println(start);
        byte[] word;
        for (int i=0;i<chainCount;i++)
        {       word=startPoints.get(i).getBytes(StandardCharsets.UTF_8);
            byte[] hash=null;


            for (int j=0;j<chainLen;j++)
            {
                hash=hr.calculateHash(word);
                //convertHash(hash);
                word=hr.reduce(hash,j,pwLength,i);
                actual++;
                //   updateProgress(actual,total+1);


            }

            chains.add(new Chain(startPoints.get(i), fromByteToString(word)));

        }

        // System.out.println("Utworzono tablice");
        //   updateMessage("Generacja zakończona. Trwa sortowanie");
        Collections.sort(chains);

        //System.out.println("posortowano");
        //   updateMessage("Trwa zapis tablicy do pliku");
        saveToFile();
        //   updateProgress(actual+1,total+1);
        //   updateMessage("Zapis zakończony. Tablica jest gotowa do uzycia!");
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
            FileOutputStream fos = new FileOutputStream(tableName+hashType+".dat");
            int i=1;

            for (Chain key: chains){
                fos.write(String.valueOf(i).getBytes());
                fos.write(" ".getBytes());
                fos.write(key.getStartPoint().getBytes());
                fos.write(" ".getBytes());
                fos.write(key.getEndPoint().getBytes());
                fos.write(System.getProperty("line.separator").getBytes());
                fos.flush();
                i++;
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


    public long calculateExample()
    {

        Random rand=new Random();
        int code;
        String startPoint;

        StringBuilder sb = new StringBuilder();
        for (int j=0; j<pwLength; j++)
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
        word=hr.reduce(hash,1,pwLength,1);
        long stop=System.nanoTime();
        long time=stop-start;
        System.out.println("Czas: "+time);

        return time;
    }




    /**@Override
    protected Object call() throws Exception {
    initTable();

    return null;
    }**/
}

