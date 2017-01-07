package Tester;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Patrycja on 2016-12-22.
 */

/**
 * Klasa odpowiadająca za cały proces testowania.
 */
public class Test {


    private InputData inputData;
    private InputData inputData2;
    public Generator generator;
    public Generator generator2;


    ArrayList<byte[]> bytesList=new ArrayList<>();
    public Test(){
        System.out.println("Jeśli chcesz przetestować liczbe duplikatów w łańcuchu wciśnij 1, jeśli w wielu łańcuchach o długości jeden 2" );
        String testChain;
        Scanner sc=new Scanner(System.in);
        testChain=sc.nextLine();
        if(testChain.equals("2"))
        {
            inputData=new InputData();
            System.out.println("Podaj nazwe tablicy");
            String tableName;
            tableName=sc.nextLine();
            inputData.setTableName(tableName);
            System.out.println("Podaj rodzaj funkcji haszującej(MD5 lub SHA-1)");
            String hashType;
            hashType=sc.nextLine();
            inputData.setHashType(hashType);
            inputData.setChainLen(1);
            inputData.setCharset("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            System.out.println("Podaj długość hasla");
            int pwLegth;
            pwLegth=sc.nextInt();
            inputData.setPwLegth(pwLegth);
            Bruteforce bruteforce = new Bruteforce(pwLegth);
            for (int length = bruteforce.min; length <= bruteforce.max; length++)
                bruteforce.generate("", 0, length);
            inputData.setChainCount(bruteforce.combinations.size());
            generator=new Generator(inputData);
            System.out.println("Wybierz funkcje redukcji 1-funkcja z generatora, 2-funkcja z RainbowCracka, 3-funkcja z RainbowCrackera");
            int choice;
            choice = sc.nextInt();
            generator.initTable(bruteforce.combinations, choice);
            int dupliacates;
            dupliacates=bruteforce.combinations.size() -generator.getUnique().size();
            System.out.println("Liczba dupikatów w łańcuchach o długości jeden : "+dupliacates+", procentowa ilość duplikatów: "+(((float)dupliacates/bruteforce.combinations.size())*100)+"%");
        }
        else {
            if (testChain.equals("1")) {
                inputData2 = new InputData();
                System.out.println("Podaj nazwe tablicy");
                String tableName;
                tableName = sc.nextLine();
                inputData2.setTableName(tableName);
                System.out.println("Podaj rodzaj funkcji haszującej(MD5 lub SHA-1)");
                String hashType;
                hashType = sc.nextLine();
                inputData2.setHashType(hashType);
                System.out.println("Podaj długość łancucha");
                int chainLen;
                chainLen = sc.nextInt();
                inputData2.setChainLen(chainLen);
                inputData2.setChainCount(1);
                inputData2.setCharset("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                System.out.println("Podaj długość hasla");
                int pwLegth;
                pwLegth = sc.nextInt();
                inputData2.setPwLegth(pwLegth);
                generator2 = new Generator(inputData2);
                System.out.println("Wybierz funkcje redukcji 1-funkcja z generatora, 2-funkcja z RainbowCracka, 3-funkcja z RainbowCrackera");
                int choice;
                choice = sc.nextInt();
                generator2.initTable2(choice);
                int dupliacates2;
                dupliacates2 = inputData2.getChainLen() - generator2.getUniqueWordsInChain().size();
                System.out.println("Liczba dupikatów w łańcuchu o długości " + inputData2.getChainLen() + ": " + dupliacates2 + ", procentowa ilość duplikatów: " + (((float) dupliacates2 / inputData2.getChainLen()) * 100) + "%");
            }
            else{System.out.println("Podałeś zły numer");}
        }



    }
}
