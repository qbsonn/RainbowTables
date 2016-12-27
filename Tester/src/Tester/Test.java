package Tester;

/**
 * Created by Patrycja on 2016-12-22.
 */

public class Test {

    private InputData inputData;
    public Generator generator;
    private int repeated;
    public Test(){

        inputData=new InputData();

        inputData.setTableName("nowa2");
        inputData.setHashType("SHA-1");
        inputData.setChainLen(3);
        inputData.setChainCount(10000);
        inputData.setCharset("ABCDEFGHIJKLMNOPRSTUVWXYZ");
        inputData.setPwLegth(15);

        generator=new Generator(inputData);
      /**  generator.initTable();
        for(int i=0;i<inputData.getChainCount();i++) {
            for(int j=i+1;j<inputData.getChainCount();j++){
                if(i!=j){

                    if (generator.chains.get(i).getEndPoint().equals(generator.chains.get(j).getEndPoint()))
                    {
                        repeated++;
                        System.out.println(generator.chains.get(i).getEndPoint());
                    }
                    else{ break;}
                }
            }
        }
        System.out.println("Liczba powtarzających się haseł: "+repeated);**/


    }
}
