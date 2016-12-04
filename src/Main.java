import java.nio.charset.StandardCharsets;

/**
 * Created by Kuba on 2016-12-03.
 */



public class Main {

    public static void main ( String args []) {

     Input in=new Input();


        in.setTableName("nowa");
        in.setChainCount(1000);
        in.setChainLen(1000);
        in.setHashType("SHA-1");
        in.setPwLegth(7);

        Generator gen=new Generator(in);
        gen.initTable();

/*
        char a = 'A';       // line 1
        byte b = (byte)a;   // line 2

        char c = (char) (b & 0xFF);  // line 3
        System.out.println((char)c + " " + (int)c);
*/

    }
}
