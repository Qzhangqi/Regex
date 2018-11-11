import java.util.*;

public class State implements Comparable<State>{
    /*
     * 状态 id
     */
    Set<Integer> id = new TreeSet<>();

    /*
     * 是否为结束态
     */
    boolean isEnd;

    /*
     * 优先级 优先级高的先匹配
     */
    int Priority = 0;

    /*
     * 捕获起始态
     */
    boolean isCapstart = false;

    /*
     * 捕获结束态
     */
    boolean isCapend = false;

    /*
     *匹配字符串头
     */
    boolean isStrs = false;

    /*
     * 匹配字符串尾
     */
    boolean isStre = false;


    @Override
    public String toString() {
        String re = "";
        for (int i : id) {
            re += (i+".");
        }
        re = re.substring(0, re.length() - 1);

        if (isStrs)     re = 'A' + re;
        if (isStre)     re = 'B' + re;

        return re;
    }

    @Override
    public boolean equals(Object obj) {
        State state = (State)obj;
        return this.id.equals(state.id);
    }

    //id 相同 hashCode 就相同
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
