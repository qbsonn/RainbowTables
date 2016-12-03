/**
 * Created by Kuba on 2016-12-03.
 */



public class Main {

    public static void main ( String args []) {
     Input in=new Input();

        in.setTableName("nowa");
        in.setChainCount(3);
        in.setChainLen(3);
        in.setHashType("MD5");

        Generator gen=new Generator(in);




    }
}
