package Tester;
import java.lang.Number;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by Kuba on 2016-12-03.
 */

/**
 * Klasa obliczajaca hasha i redukuja hashe w celu utworzenia nowych slow
 */
public class HashAndReduct {

    /**
     * Pole przechowujące typ funkcji hashujacej
     */

    String hashType;


    /**
     * Tablica znaków z ktorych moga powstac slowa
     */

    String charset;

    byte[] byteCharset;

    /**
     *
     * Konwersja charsetu na tablice byte
     *
     * @return
     */
    public byte[] toAsciiByte()
    {
        byte[] array=new byte[charset.length()];

        for (int i=0;i<charset.length();i++)
        {
            char ch= charset.charAt(i);
            array[i] = (byte)ch;


        }
        String str=null;
        try {
            str = new String(array, "UTF-8");
        }
        catch(java.io.UnsupportedEncodingException e)
        {


        }
        //System.out.println(str);
        return array;
    }

    /**
     *
     *Przypisanie wartosci byte dla danego znaku
     * @param index
     * @return
     */
    public byte findInCharset(int index)
    {
        byte value=byteCharset[index];

        return value;
    }

    /**
     * Konstruktor
     * @param _hashType typ hasha
     * @param _charset zakres znaków
     */

    public HashAndReduct(String _hashType, String _charset)
    {
        hashType=_hashType;
        charset=_charset;
        byteCharset=toAsciiByte();
    }

    /**
     * Funkcja obliczająca i zwracająca obliczony hash
     *
     * @param _bytesOfMessage tekst na podstawie ktorego liczony jest hash
     * @return obliczony hash
     */
    public byte[] calculateHash(byte[] _bytesOfMessage) {
        byte[] thedigest=null;
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            thedigest = md.digest(_bytesOfMessage);
        }
        catch ( java.security.NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return thedigest;


    }

    /**
     * Funkcja redukcji zwracajaca slowo z hasha
     *
     * @param _hash hash z który redukujemy
     * @param _functionNr   numer funkcji redukcji
     * @param _pwLength dlugosc slowa
     * @return obliczone słowo
     */

    public byte[] reduce (byte[] _hash, int _functionNr, int _pwLength) {
        byte[] result = new byte[_pwLength];
        int j = 0;
        int hashLength = _hash.length;
        int hashIndex = 0;
        for (int i = 0; i < _pwLength; i++) {
            hashIndex = hashIndex + _hash[j % hashLength] ^ _functionNr;
            j = j + _pwLength;


            result[i] = findInCharset(Math.abs(hashIndex) % charset.length());

        }
        return result;}
   /** public void rainbowCrackReduce(){
            int i;
            for (i = m_nPlainLenMax - 1; i >= m_nPlainLenMin - 1; i--)
            {
                if (m_nIndex >= m_nPlainSpaceUpToX[i])
                {
                    m_nPlainLen = i + 1;
                    break;
                }
            }

            uint64 nIndexOfX = m_nIndex - m_nPlainSpaceUpToX[m_nPlainLen - 1];

	/*
	// Slow version
	for (i = m_nPlainLen - 1; i >= 0; i--)
	{
		m_Plain[i] = m_PlainCharset[nIndexOfX % m_nPlainCharsetLen];
		nIndexOfX /= m_nPlainCharsetLen;
	}


            // Fast version
            for (i = m_nPlainLen - 1; i >= 0; i--)
            {
                #ifdef _WIN32
                if (nIndexOfX < 0x100000000I64)
                break;
                #else
                if (nIndexOfX < 0x100000000llu)
                break;
                #endif

                m_Plain[i] = m_PlainCharset[nIndexOfX % m_nPlainCharsetLen];
                nIndexOfX /= m_nPlainCharsetLen;
            }
            unsigned int nIndexOfX32 = (unsigned int)nIndexOfX;
            for (; i >= 0; i--)
            {
                //m_Plain[i] = m_PlainCharset[nIndexOfX32 % m_nPlainCharsetLen];
                //nIndexOfX32 /= m_nPlainCharsetLen;

                unsigned int nPlainCharsetLen = m_nPlainCharsetLen;
                unsigned int nTemp;
                #ifdef _WIN32
                __asm
                {
                    mov eax, nIndexOfX32
                    xor edx, edx
                    div nPlainCharsetLen
                    mov nIndexOfX32, eax
                    mov nTemp, edx
                }
                #else
                __asm__ __volatile__ (	"mov %2, %%eax;"
                "xor %%edx, %%edx;"
                "divl %3;"
                "mov %%eax, %0;"
                "mov %%edx, %1;"
                : "=m"(nIndexOfX32), "=m"(nTemp)
                : "m"(nIndexOfX32), "m"(nPlainCharsetLen)
                : "%eax", "%edx"
                );
                #endif
                m_Plain[i] = m_PlainCharset[nTemp];
            }
    }**/
  public byte[] reduceFunction (byte[] hash, int _functionNr, int _pwLength) {
      byte[] result = new byte[_pwLength];
       long a, b, c, d;

       long charset_offset = (long) _functionNr % charset.length();
       long PasswordLength = (long)_pwLength;
       long Device_Table_Index = 1;

        a = (hash[3]*(256*256*256) + hash[2]*(256*256) + hash[1]*256 + hash[0]);
        b = (hash[7]*(256*256*256) + hash[6]*(256*256) + hash[5]*256 + hash[4]);
        c = (hash[11]*(256*256*256) + hash[10]*(256*256) + hash[9]*256 + hash[8]);
        d = (hash[15]*(256*256*256) + hash[14]*(256*256) + hash[13]*256 + hash[12]);

        long z;
        z = (a+_pwLength+Device_Table_Index) % (256*256*256);
        result[0] =byteCharset[(int)((z % 256) + charset_offset)];
        if (PasswordLength == 1) {
            return result;}

        z /= 256;
        result[1] = byteCharset [(int)((z % 256) + charset_offset)];
        if (PasswordLength == 2) {return result;}
        z /= 256;
        result[2] = byteCharset [(int)((z % 256) + charset_offset)];
        if (PasswordLength == 3) {return result;}

        // Second 3
        z = (b+_functionNr+Device_Table_Index) % (256*256*256);
        result[3] = byteCharset[(int)((z % 256) + charset_offset)];
        if (PasswordLength == 4) {return result;}
        z /= 256;
        result[4] =byteCharset[(int)((z % 256) + charset_offset)];
        if (PasswordLength == 5) {return result;}
        z /= 256;
        result[5] =byteCharset [(int)((z % 256) + charset_offset)];
        if (PasswordLength == 6) {return result;}

        z = (c+_functionNr+Device_Table_Index) % (256*256*256);
        result[6] = byteCharset[(int)((z % 256) + charset_offset)];
        if (PasswordLength == 7) {return result;}
        z /= 256;
       result[7] =byteCharset[(int)((z % 256) + charset_offset)];
        if (PasswordLength == 8) {return result;}
        z /= 256;
        result[8] =byteCharset [(int)((z % 256) + charset_offset)];
        if (PasswordLength == 9) {return result;}

        z = (d+_functionNr+Device_Table_Index) % (256*256*256);
        result[9] = byteCharset[(int)((z % 256) + charset_offset)];
        if (PasswordLength == 10) {return result;}
        z /= 256;
        result[10] = byteCharset [(int)((z % 256) + charset_offset)];
        if (PasswordLength == 11) {return result;}
        z /= 256;
        result[11] = byteCharset[(int)((z % 256) + charset_offset)];
        if (PasswordLength == 12) {return result;}

      return result;
  }
  /**  void GRTChainRunnerSHA1::reduceFunction(unsigned char *password, unsigned char *hash, uint32_t CurrentStep) {
        UINT4 a, b, c, d;

        uint32_t charset_offset = CurrentStep % this->charsetLength;
        uint32_t PasswordLength = this->TableHeader->getPasswordLength();
        uint32_t Device_Table_Index = this->TableHeader->getTableIndex();

        a = (hash[3]*(256*256*256) + hash[2]*(256*256) + hash[1]*256 + hash[0]);
        b = (hash[7]*(256*256*256) + hash[6]*(256*256) + hash[5]*256 + hash[4]);
        c = (hash[11]*(256*256*256) + hash[10]*(256*256) + hash[9]*256 + hash[8]);
        d = (hash[15]*(256*256*256) + hash[14]*(256*256) + hash[13]*256 + hash[12]);

        UINT4 z;
        // Reduce it
        // First 3
        z = (UINT4)(a+CurrentStep+Device_Table_Index) % (256*256*256);
        password[0] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 1) {return;}
        z /= 256;
        password[1] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 2) {return;}
        z /= 256;
        password[2] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 3) {return;}

        // Second 3
        z = (UINT4)(b+CurrentStep+Device_Table_Index) % (256*256*256);
        password[3] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 4) {return;}
        z /= 256;
        password[4] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 5) {return;}
        z /= 256;
        password[5] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 6) {return;}

        z = (UINT4)(c+CurrentStep+Device_Table_Index) % (256*256*256);
        password[6] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 7) {return;}
        z /= 256;
        password[7] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 8) {return;}
        z /= 256;
        password[8] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 9) {return;}

        z = (UINT4)(d+CurrentStep+Device_Table_Index) % (256*256*256);
        password[9] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 10) {return;}
        z /= 256;
        password[10] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 11) {return;}
        z /= 256;
        password[11] = (UINT4)this->charset[(z % 256) + charset_offset];
        if (PasswordLength == 12) {return;}

    }
**/

}