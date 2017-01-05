package Tester;

import java.util.ArrayList;

/**
 * Created by Patrycja on 2016-12-22.
 */

public class Test {

    private InputData inputData;
    private InputData inputData2;
    public Generator generator;
    public Generator generator2;

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

        inputData2=new InputData();
        inputData2.setTableName("nowa22");
        inputData2.setHashType("MD5");
        inputData2.setChainLen(1000);
        inputData2.setChainCount(1);
        inputData2.setCharset("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        inputData2.setPwLegth(3);
        generator2=new Generator(inputData2);

     /**   for(int j=0;j<bruteforce.combinations.size();j++)
        { bytesList.add(generator.getHashReduct().calculateHash(bruteforce.combinations.get(j).getBytes())) ;


        }
        bruteforce.saveToFile("pliczek2");
**/

      //  generator.initTable(bruteforce.combinations);
        generator2.initTable2();
      /*  int dupliacates;
        //dupliacates=inputData.getChainLen()-
        dupliacates=bruteforce.combinations.size() -generator.getUnique().size();
        System.out.println("Liczba dupikatów w łańcuchach o długości jeden : "+dupliacates+", procentowa ilość duplikatów: "+(((float)dupliacates/bruteforce.combinations.size())*100)+"%");
      */  int dupliacates2;
        dupliacates2=inputData2.getChainLen()-generator2.getUniqueWordsInChain().size() ;
        System.out.println("Liczba dupikatów w łańcuchu o długości "+inputData2.getChainLen()+": " +dupliacates2+ ", procentowa ilość duplikatów: "+(((float)dupliacates2/inputData2.getChainLen())*100)+"%");

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
