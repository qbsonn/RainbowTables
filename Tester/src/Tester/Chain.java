package Tester;
/**
 * Created by Kuba on 2016-12-05.
 */
public class Chain implements Comparable<Chain>{


    private String startPoint;
    private String endPoint;


    public Chain (String _start, String _end )
    {
        startPoint=_start;
        endPoint=_end;
    }


    public String getEndPoint()
    {

        return endPoint;
    }

    public String getStartPoint()
    {
        return startPoint;
    }

    @Override
    public int compareTo(Chain o) {

        return this.getEndPoint().compareTo(o.getEndPoint());
    }


    @Override
    public int hashCode() {
        return this.getEndPoint().hashCode();
    }


    @Override
    public boolean equals(Object obj) {

        return this.getEndPoint().equals(((Chain)obj).getEndPoint());
    }

}




