package Tester;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Patrycja on 2016-12-25.
 */
public class Bruteforce {

    private char[] charset;
    public int min; //var added for min char length
    public int max; //var added for max char length
    public ArrayList<String> combinations;

    public Bruteforce(int pwLength) {
        combinations=new ArrayList<>();
        charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        min = pwLength; //char min start
        max = pwLength; //char max end
    }

    public void generate(String str, int pos, int length) {
        if (length == 0) {
            combinations.add(str);
        } else {

            //This if statement resets the char position back to the very first character in the character set ('a'), which makes this a complete solution to an all combinations bruteforce!
            if (pos != 0) {
                pos = 0;
            }

            for (int i = pos; i < charset.length; i++) {
                generate(str + charset[i], i, length - 1);

            }
        }
    }
    public void saveToFile(String File)
    {
        try {
            PrintWriter fos = new PrintWriter(File+".txt");

            for (int i=0;i<combinations.size();i++){
                fos.println(i+1 + " "+combinations.get(i));

            }
            fos.close();
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();

        }

    }
}
