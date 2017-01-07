import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by Piotrek on 2016-12-26.
 */
public class Finder {

    private String[] lastValuesRainbowTable;
    private String rainbowTablePath;
    public int chainLength;
    private String charset;
    public int minValueLength, maxValueLength;
    private String hashType;
    public HashAndReduct hashAndReduct;


    public Finder(String rainbowTablePath)
    {
        this.rainbowTablePath = rainbowTablePath;
        loadTable();
       // System.out.println("zcgewp " + getFirstValue(exists("zcgewp")).getBytes());
        hashAndReduct = new HashAndReduct(hashType,charset,chainLength, minValueLength, maxValueLength);
    }

    public void loadTable()
    {
        String[] rainbowTable = null;
        try
        {
            FileReader fr = new FileReader(rainbowTablePath);
            LineNumberReader lnr = new LineNumberReader(fr);

            String[] line = lnr.readLine().split(" ");
            rainbowTable = new String[Integer.parseInt(line[1])];
            chainLength = Integer.parseInt(line[3]);
            line = lnr.readLine().split(" ");
            minValueLength = Integer.parseInt(line[1]);
            maxValueLength = Integer.parseInt(line[3]);
            hashType = line[5];
            line = lnr.readLine().split(" ");
            charset = line[1];

            for (int i=0; i< rainbowTable.length; i++)
            {
                line = lnr.readLine().split(" ");
                // line[0] = pierwsze haslo
                // line[1] = ostatnie haslo
                rainbowTable[i] = line[1];
            }
            fr.close();
        }
        catch(IOException e)
        {}
        lastValuesRainbowTable = rainbowTable;

    }

    /** Sprawdza czy istnieje łancuch o ostatnim słowie word
     * Jesli istnieje zwraca jego index a jesli nie to zwraca ujemna wartosc */
    public int exists(String word)
    {
        return Arrays.binarySearch(lastValuesRainbowTable, word);
    }

    public boolean exists(String word, int index)
    {
        if (index >= 0 && index < lastValuesRainbowTable.length )
            return word.matches(lastValuesRainbowTable[index]);
        else return false;
    }

    /** Wczytuje pierwsze słowo łancucha z łancucha o numerze index */
    public String getFirstValue(int index)
    {
        String firstValue = null;
        try
        {
            FileReader fr = new FileReader(rainbowTablePath);
            LineNumberReader lnr = new LineNumberReader(fr);

            for (int i=-3; i<index; i++)
                lnr.readLine();

            firstValue = lnr.readLine().split(" ")[0];
            fr.close();
        }
        catch(IOException e)
        {}
        return firstValue;
    }
}
