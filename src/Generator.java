/**
 * Created by Kuba on 2016-12-03.
 */
public class Generator {


    String tableName;
    int chainLen;
    int chainCount;
    String hashType;
    String charset;
    HashAndReduct hr;




    public Generator(Input _input)
    {
        tableName=_input.getTableName();
        chainLen=_input.getChainLen();
        chainCount=_input.getChainCount();
        hashType=_input.getHashType();
        charset=_input.getCharset();


    hr=new HashAndReduct( hashType,charset);

    }


    public void initTable()
    {




    }


}
