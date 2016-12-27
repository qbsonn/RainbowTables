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


    public Finder(String rainbowTablePath)
    {
        this.rainbowTablePath = rainbowTablePath;
        lastValuesRainbowTable = loadTable();
        //System.out.println("AKKAD " + getFirstValue(exists("AKKAD")));
       // System.out.println("HMDYB " + getFirstValue(exists("HMDYB")));
       // System.out.println("a " + exists("a"));
    }

    public String[] loadTable()
    {
        String[] rainbowTable = null;
        try
        {
            FileReader fr = new FileReader(rainbowTablePath);
            LineNumberReader lnr = new LineNumberReader(fr);

            String[] line = lnr.readLine().split(" ");
            rainbowTable = new String[Integer.parseInt(line[1])];

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
        return rainbowTable;
    }

    /** Sprawdza czy istnieje łancuch o ostatnim słowie word
     * Jesli istnieje zwraca jego index a jesli nie to zwraca ujemna wartosc */
    public int exists(String word)
    {
        return Arrays.binarySearch(lastValuesRainbowTable, word);
    }

    /** Wczytuje pierwsze słowo łancucha z łancucha o numerze index */
    public String getFirstValue(int index)
    {
        String firstValue = null;
        try
        {
            FileReader fr = new FileReader(rainbowTablePath);
            LineNumberReader lnr = new LineNumberReader(fr);

            for (int i=-1; i<index; i++)
                lnr.readLine();

            firstValue = lnr.readLine().split(" ")[0];
            fr.close();
        }
        catch(IOException e)
        {}
        return firstValue;
    }
}
