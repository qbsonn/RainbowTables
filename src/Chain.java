


/**
 *
 * Klasa przechowująca początkowy i końcowy element każdego łańcucha
 */
public class Chain implements Comparable<Chain>{


   private String startPoint;
    private String endPoint;

    /**
     *
     * Konstruktor
     * @param _start Początkowy element
     * @param _end Końcowy element
     */
    public Chain (String _start, String _end )
    {
        startPoint=_start;
        endPoint=_end;
    }

    /**
     * Metoda zwracająca punkt końcowy obieku
     * @return
     */
    public String getEndPoint()
    {

        return endPoint;
    }

    /**
     *
     * Metoda zwracająca punkt startowy obiektu
     * @return
     */
    public String getStartPoint()
    {
        return startPoint;
    }

    /**
     *
     * Metoda porównująca obiekty klasy Chain na podstawie ich punktów końcowych (używana przy sortowaniu łańcuchów w kolejności alfabetycznej wzgledem końcowego elementu
     * @param o
     * @return
     */
    @Override
    public int compareTo(Chain o) {

     return this.getEndPoint().compareTo(o.getEndPoint());
    }


    /**
     *
     * Metoda obliczająca hashcode obiektu klasy chain na podstawie ich punktów końcowych
     * @return
     */
    @Override
    public int hashCode() {
        return this.getEndPoint().hashCode();
    }

    /**
     *
     * Metoda porównująca czy obiekty klasy chain sa identyczne (czy mają takie same punkty końcowe)
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {

        return this.getEndPoint().equals(((Chain)obj).getEndPoint());
    }

}




