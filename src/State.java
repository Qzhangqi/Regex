import java.util.*;

public class State implements Comparable<State>{
    Set<Integer> id = new TreeSet<>();
    boolean isEnd;
    int Priority = 0;
//    boolean isNoGreed = false;
    boolean isCapend = false;
    boolean isCapstart = false;
    boolean isStrs = false;
    boolean isStre = false;
    int isNocon = 0;

    private static int increase;

    @Override
    public String toString() {
        String re = "";
        for (int i : id) {
            re += (i+".");
        }
        re = re.substring(0, re.length() - 1);

//        if (isNoGreed)  re = 'G' + re;
//        if (isCapstart) re = 'S' + re;
//        if (isCapend)   re = 'E' + re;
        if (isNocon != 0)    re = 'C' + re;
        if (isStrs)     re = 'A' + re;
        if (isStre)     re = 'B' + re;

        return re;
    }

    @Override
    public boolean equals(Object obj) {
        State state = (State)obj;
        return this.id.equals(state.id);
    }

    @Override
    public int hashCode() {
        int hash = 0, k = 1;
        for (int i : id) {
            hash += (k * i);
            k *= 10;
        }
        return hash;
    }

    @Override
    public int compareTo(State o) {
        if (this.Priority > o.Priority)
            return -1;
        else if (this.Priority < o.Priority)
            return 1;
        else
            return 0;
    }
}
