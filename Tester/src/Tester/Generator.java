

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

/**
 * Klasa odpowiadająca za cały proces generowania tablic
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
    private  List<Chain> uniqueChains;
    private  List<String> uniqueWordsInChain;
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

    private Set<Chain> chains;
    private Set<String> chain;
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
        chains=new HashSet<>();
        chain=new HashSet<>();
        pwLength=_input.getPwLegth();
        startPoints=new ArrayList<>();
        uniqueChains=new ArrayList<>();
        uniqueWordsInChain=new ArrayList<>();
        if (_input.getStartPoints().size()!=0)
        {
            startPoints=_input.getStartPoints();

        }


        directory=_input.getDirectory();

        //calculateStartPoints();

        hr=new HashAndReduct( hashType,charset);

        System.out.println("Nazwa tablicy: "+tableName+"algorytm: "+hashType+"dlugosc lan: "+chainLen+"ilosc: "+chainCount);



    }
    public List<Chain> getUnique(){
        return uniqueChains;
    }
    public List<String> getUniqueWordsInChain(){
        return uniqueWordsInChain;
    }

    public HashAndReduct getHashReduct(){
        return hr;
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
     * Metoda tworząca tablice dla każdej kombinacji dla łancuchów o długościach jeden i zlicza liczbe powtarzających się słow
     */
    public void initTable(ArrayList<String> startPoints, int choice)

    {
        System.out.println("Tworzenie");
        int total=chainCount*chainLen;
        int actual=0;

        long start=System.currentTimeMillis();
        byte[] word;
        for (int i=0;i<chainCount;i++)
        {       word=startPoints.get(i).getBytes(StandardCharsets.UTF_8);
            byte[] hash=null;

            //  hr.generateRandomIndex(pwLength) ;
            for (int j=0;j<chainLen;j++)
            {   if(choice==1)
            { hash=hr.calculateHash(word);
                word=hr.reduce(hash,j,pwLength);}
                if(choice==2)
                {  hash=hr.calculateHash(word);
                    word=hr.rainbowCrackReduce(pwLength, j, hash);}
                if(choice==3)
                {  hash=hr.calculateHash(word);
                    word=hr.reduceFunction(hash, j, pwLength);}
              /*  try {
                    System.out.println(new String(word, "UTF-8"));
                }
                catch(Exception e){}
                actual++;*/

            }
            String end=null;
            try {
                end = new String(word, "UTF-8");
            }
            catch(java.io.UnsupportedEncodingException e)
            {
                e.printStackTrace();

            }
            chains.add(new Chain(startPoints.get(i),end));
        }


        uniqueChains .addAll(chains);

        chains.clear();
        Collections.sort(uniqueChains);
        System.out.println("Trwa zapis tablicy do pliku");
        saveToFile();
        System.out.println("Zapis zakończony. Tablica jest gotowa do uzycia!");
//saveString();
        long stop=System.currentTimeMillis();

        System.out.println("Czas wykonania:"+(stop-start)/1000 +" sekund");

    }

    /**
     * Metoda odpowiadająca za tworzenie tablicy dla jednego łancucha i zliczanie liczby powtarzających się słow w tym łancuchu
     */
    public void initTable2(int choice)

    {
        System.out.println("Tworzenie");
        int total=chainCount*chainLen;
        Random rand=new Random();
        int code;
        long start=System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int j=0; j<pwLength; j++)
        {
            code= rand.nextInt(charset.length());
            sb.append( foundCharInCharset(code));

        }
        startPoints.add(sb.toString());
        byte[] word;

        word=startPoints.get(0).getBytes(StandardCharsets.UTF_8);
        byte[] hash=null;

        //  hr.generateRandomIndex(pwLength) ;
        for (int j=0;j<chainLen;j++)
        {
            hash=hr.calculateHash(word);
            word=hr.reduce(hash,j,pwLength);
            // word=hr.rainbowCrackReduce(pwLength, j, hash);
            String wordInChain=null;
            try {
                wordInChain=new String(word, "UTF-8");
            }
            catch(java.io.UnsupportedEncodingException e)
            {
                e.printStackTrace();

            }
            chain.add(wordInChain);

        }

        uniqueWordsInChain .addAll(chain);

        chain.clear();
        Collections.sort(uniqueWordsInChain);

        long stop=System.currentTimeMillis();

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
            FileOutputStream fos = new FileOutputStream(directory+tableName+hashType+".dat");


            fos.write("Lancuchy: ".getBytes());
            fos.write(String.valueOf(uniqueChains.size()).getBytes());


            fos.write(" Dlugosc: ".getBytes());
            fos.write(String.valueOf(chainLen).getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            fos.flush();


            fos.write("Dlugosc_hasla: ".getBytes());
            fos.write(String.valueOf(pwLength).getBytes());


            fos.write(" Hash: ".getBytes());
            fos.write(hashType.getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            fos.flush();

            fos.write("Charset: ".getBytes());
            fos.write(charset.getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            fos.flush();


            for (Chain key: uniqueChains){
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
        word=hr.reduce(hash,1,pwLength);
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