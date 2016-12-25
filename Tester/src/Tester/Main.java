package Tester;

/**
 * Created by Patrycja on 2016-12-22.
 */
public class Main {
    public static void main(String[] args) {

       // Test test=new Test();
        new Bruteforce() {
            public void element(char[] result, int offset, int length) {
                System.out.println(new String(result, offset, length));
            }
        }.generate("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }
}
