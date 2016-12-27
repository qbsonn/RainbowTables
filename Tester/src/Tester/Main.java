package Tester;

import java.util.ArrayList;

/**
 * Created by Patrycja on 2016-12-22.
 */
public class Main {
    public static void main(String[] args) {

        // Test test=new Test();
       HashAndReduct hashreduct = new HashAndReduct("MD5", "ABCDEFGHIJKLMNOPRSTUVWXYZ");
        Bruteforce bruteforce = new Bruteforce();
        ArrayList <byte[]> bytesList=new ArrayList<>();


        for (int length = bruteforce.min; length <= bruteforce.max; length++) // Change bruteforce.min and bruteforce.max for number of characters to bruteforce.
            bruteforce.generate("", 0, length); //prepend_string, pos, length


        for(int j=0;j<bruteforce.combinations.size();j++)
        { bytesList.add(hashreduct.calculateHash(bruteforce.combinations.get(j).getBytes())) ;
        System.out.println(bytesList.get(j));
        }
        bruteforce.saveToFile("pliczek2");
    }
}
