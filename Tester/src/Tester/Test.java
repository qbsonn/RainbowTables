package Tester;

import java.util.ArrayList;

/**
 * Created by Patrycja on 2016-12-22.
 */

public class Test {

    private InputData inputData;
    public Generator generator;
    private int repeated;
    Bruteforce bruteforce = new Bruteforce();
    ArrayList<byte[]> bytesList=new ArrayList<>();
    public Test(){

        for (int length = bruteforce.min; length <= bruteforce.max; length++) // Change bruteforce.min and bruteforce.max for number of characters to bruteforce.
            bruteforce.generate("", 0, length); //prepend_string, pos, length
        
        inputData=new InputData();
        inputData.setTableName("nowa2");
        inputData.setHashType("MD5");
        inputData.setChainLen(1);
        inputData.setChainCount(bruteforce.combinations.size());
        inputData.setCharset("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        inputData.setPwLegth(5);
        generator=new Generator(inputData);

     /**   for(int j=0;j<bruteforce.combinations.size();j++)
        { bytesList.add(generator.getHashReduct().calculateHash(bruteforce.combinations.get(j).getBytes())) ;


        }
        bruteforce.saveToFile("pliczek2");
**/

        generator.initTable(bruteforce.combinations);
        int dupliacates;
        dupliacates=bruteforce.combinations.size() -generator.getUnique().size();
        System.out.println("Liczba dupikatów: "+dupliacates+", procentowa ilość duplikatów: "+(((float)dupliacates/bruteforce.combinations.size())*100)+"%");
       /** for(int i=0;i<inputData.getChainCount();i++) {
            int j=i+1;
            if(j<inputData.getChainCount()){

                if (generator.chains.get(i).getEndPoint().equals(generator.chains.get(j).getEndPoint()))
                {
                    repeated++;
                    System.out.println(generator.chains.get(i).getEndPoint());
                }
            }

        }
        System.out.println("Liczba powtarzających się haseł: "+repeated);**/




    }
}
