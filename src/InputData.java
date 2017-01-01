/**
 * Created by Kuba on 2016-12-03.
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * Klasa odpowiadajaca za pobranie danych wejsciowych podanych przez uzytkownika
 */
public class InputData {


    /** Nazwa tablicy*/
    private String tableName;
    /**
     * Dlugosc lancucha
     */
    private int chainLen;
    /**
     * Liczba lancuchow w tablicy
     */
    private int chainCount;
    /**
     * Typ hasha
     */
    private String hashType;
    /**
     * Zakres znaków
     */
    private String charset;
    /**
     * Tablica zawierajaca punkty startowe lancuchow
     */
    private String []startPoints;
    /**
     * Dlugosc hasel generowanych w tablicy
     */
    private int pwLegth;


    /**
     * Sciezka w ktorej zostanie zapisana tablica
     */
    private String directory;

    private ArrayList<String> loadedStartPoints;

    /**
     * Konstruktor obiektu klasy input
     */
        public InputData()
        {
            //charset="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            loadedStartPoints=new ArrayList<>();
        }


    /**
     * Ustawianie dlugosci hasel
     * @param _length
     */
    public void setPwLegth(int _length)
    {
        pwLegth=_length;

    }
    /**Ustawienie nazwy tablicy
     * @param _name
     */
    public void setTableName(String _name)
    {
        tableName=_name;
    }

    /**
     * Ustawienie dlugosci lancucha
     * @param _chlen
     */
    public void setChainLen(int _chlen)
    {
        if (_chlen>0) {

            chainLen = _chlen;
        }
        else
            System.out.println("chainlen error");
    }

    /**
     * Metoda ustawiajaca charset
     *
     */

    public void setCharset(String _charset)
    {
        charset=_charset;

    }

    /**
     * Ustawienie ilosci lancuchow
     * @param _chcount
     */
    public void setChainCount(int _chcount)
    {
        if (_chcount>0)
                 {
                     chainCount=_chcount;
        }
        else
            System.out.println("chaincount error");
    }

    /**
     *Ustawienie typu hasha
     * @param _hash
     */
    public void setHashType(String _hash) {
        if (_hash == "MD5" || _hash == "SHA1") {
            hashType = _hash;
        } else
            System.out.println("hash error");
    }

    /**
     * Zwracanie nazwy tablicy
     * @return
     */
    public String getTableName() { return tableName;}

    /**
     * Zwracanie dlugosci lancucha
     * @return
     */
    public int getChainLen() { return chainLen;}

    /**
     * Zwracanie ilosci lancuchów
     * @return
     */
    public int getChainCount() {return chainCount;}

    /**
     * Zwracanie typu hasha
     * @return
     */
    public String getHashType(){ return hashType;}

    /**
     * Zwracanie zakresu znaków
     * @return
     */
    public String getCharset() { return charset;}

    /**
     * Zwraca dlugosc hasla
     * @return
     */
    public int getPwLegth(){return pwLegth;}

    /**
     *
     * Ustawia ścieżkę zapisu tablicy
     * @param _directory ścieżka
     */

    public void setDirectory(String _directory)
    {


        directory=_directory;

    }

    /**
     * Zwraca sciezke w ktorej zostanie zapsiana tablica
     * @return
     */
    public String getDirectory(){return directory;}

    /**
     *
     * Metoda wczytująca punkty początkowe z pliku
     * @param path ścieżka do pliku
     */
    public void loadStartPoints(String path)
    {

        BufferedReader br=null;
        try {
            br = new BufferedReader(new FileReader(path));

        }
        catch (java.io.FileNotFoundException e)
        {
            e.printStackTrace();

        }
        try{
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                loadedStartPoints.add(line);

            }
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();

        }


        for (String b :loadedStartPoints)
        {
            System.out.println(b);

        }

    }

    /**
     * Metoda zwracająca tablice punktów początkowych
     * @return
     */
    public ArrayList<String>  getStartPoints()
    {
        return loadedStartPoints;

    }

}
