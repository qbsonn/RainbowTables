package Tester;

/**
 * Created by Patrycja on 2016-12-22.
 */
public class Main {
    public static void main(String[] args) {

       // Test test=new Test();
        Bruteforce bruteforce = new Bruteforce();

        for (int length = bruteforce.min; length <= bruteforce.max; length++) // Change bruteforce.min and bruteforce.max for number of characters to bruteforce.
        {   bruteforce.generate("", 0, length);} //prepend_string, pos, length
    }
}
