/**
 * Created by Kuba on 2016-12-03.
 */

/**
 *
 * Klasa odpowiadajaca za pobranie danych wejsciowych podanych przez uzytkownika
 */
public class Input {


    /** Nazwa tablicy*/
    private String tableName;
    /**
     * Dlugosc lancucha
     */
    private int chainLen;
    /**
     * Liczba lancuchow w tablicy
     */
    private int chainCount;
    /**
     * Typ hasha
     */
    private String hashType;
    /**
     * Zakres znaków
     */
    private String charset;

    /**
     * Konstruktor obiektu klasy input
     */
        public Input()
        {

            charset="ABCDEFGHIJKLMNOPQRSTUVWXYZ" ;
        }

    /**Ustawienie nazwy tablicy
     * @param _name
     */
    public void setTableName(String _name)
    {
        tableName=_name;
    }

    /**
     * Ustawienie dlugosci lancucha
     * @param _chlen
     */
    public void setChainLen(int _chlen)
    {
        if (_chlen>0) {

            chainLen = _chlen;
        }
        else
            System.out.println("chainlen error");
    }

    /**
     * Ustawienie ilosci lancuchow
     * @param _chcount
     */
    public void setChainCount(int _chcount)
    {
        if (_chcount>0)
                 {
                     chainCount=_chcount;
        }
        else
            System.out.println("chaincount error");
    }

    /**
     *Ustawienie typu hasha
     * @param _hash
     */
    public void setHashType(String _hash) {
        if (_hash == "MD5" || _hash == "SHA-1") {
            hashType = _hash;
        } else
            System.out.println("hash error");
    }

    /**
     * Zwracanie nazwy tablicy
     * @return
     */
    public String getTableName() { return tableName;}

    /**
     * Zwracanie dlugosci lancucha
     * @return
     */
    public int getChainLen() { return chainLen;}

    /**
     * Zwracanie ilosci lancuchów
     * @return
     */
    public int getChainCount() {return chainCount;}

    /**
     * Zwracanie typu hasha
     * @return
     */
    public String getHashType(){ return hashType;}

    /**
     * Zwracanie zakresu znaków
     * @return
     */
    public String getCharset() { return charset;}


}
