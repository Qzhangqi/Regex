import java.util.*;

public class StateTable {
    private State[][] stateTable = new State[20][20];
    private Map<Integer, Integer> chMapx = new HashMap<>(); //转换条件到数组横坐标的映射
    private Map<State, Integer>  stateMapy = new HashMap<>(); //状态到数组纵坐标的映射
    private Map<Integer, State>  hashMapState = new HashMap<>(); //状态到数组纵坐标的映射

    private int y = 0; //纵坐标
    private int x = 0; //横坐标


    /*
     * 获取一个 唯一 状态
     */
    State getState(State state) {
        return hashMapState.get(state.hashCode());
    }

    /**
     * 获取一个状态集 (匹配时用)
     * @param x 横坐标
     * @param y 纵坐标
     * @return 对于状态
     */
    PriorityQueue<State> getStates(int x, State y) {
        PriorityQueue<State> states = new PriorityQueue<>();
        //先确定纵坐标
        Integer iy = stateMapy.get(y);
        Integer ix = null;

        if ((ix = chMapx.get(Regex.ANYONE)) != null) {
            if (stateTable[ix][iy] != null)
                states.add(stateTable[ix][iy]);
            ix = null;
        }
        if ((ix = chMapx.get(Regex.CAPHEAD)) != null) {
            if (stateTable[ix][iy] != null) {
                stateTable[ix][iy].isCapstart = true;
                stateTable[ix][iy].Priority = -1;
                states.add(stateTable[ix][iy]);
            }
            ix = null;
        }
        if ((ix = chMapx.get(Regex.CAPEND)) != null) {
            if (stateTable[ix][iy] != null) {
                stateTable[ix][iy].isCapend = true;
                stateTable[ix][iy].Priority = -1;
                states.add(stateTable[ix][iy]);
            }
            ix = null;
        }
        if (48 <= x
                && x >= 57
                && (ix = chMapx.get(Regex.DIGIT)) != null) {
            if (stateTable[ix][iy] != null)
                states.add(stateTable[ix][iy]);
            ix = null;
        }
        if ((48 > x || x < 57)
                && (ix = chMapx.get(Regex.NODIGIT)) != null) {
            if (stateTable[ix][iy] != null)
                states.add(stateTable[ix][iy]);
            ix = null;
        }
        if ((x == 95 || (x >= 97 && x <= 122) || (x >= 65 && x <= 90))
                && (ix = chMapx.get(Regex.CHARA)) != null) {
            if (stateTable[ix][iy] != null)
                states.add(stateTable[ix][iy]);
            ix = null;
        }
        if ((x != 95 && (x < 97 || x > 122) && (x < 65 || x > 90))
                && (ix = chMapx.get(Regex.NOCHARA)) != null) {
            if (stateTable[ix][iy] != null)
                states.add(stateTable[ix][iy]);
            ix = null;
        }
        if (Character.isSpaceChar(x)
                && (ix = chMapx.get(Regex.BLANK)) != null) {
            if (stateTable[ix][iy] != null)
                states.add(stateTable[ix][iy]);
            ix = null;
        }
        if (!Character.isSpaceChar(x)
                && (ix = chMapx.get(Regex.NOBLANK)) != null) {
            if (stateTable[ix][iy] != null)
                states.add(stateTable[ix][iy]);
            ix = null;
        }
        if ((ix = chMapx.get(x)) != null) {
            if (stateTable[ix][iy] != null)
                states.add(stateTable[ix][iy]);
            ix = null;
        }

        return states;
    }

    /*
     * 获取第一个纵坐标
     */
    State getFirstY() {
        for (State state : stateMapy.keySet()) {
            if (stateMapy.get(state) == 0)
                return state;
        }

        return null;
    }

    /*
     * 添加一个横坐标
     */
    void addAbscissa(char ch) {
        addAbscissa((int)ch);
    }

    void addAbscissa(int ch) {
        if (!chMapx.containsKey(ch))
            chMapx.put(ch, x++);
    }

    //添加一个纵坐标
    void addOrdinate(State state) {
        stateMapy.put(state, y++);
        hashMapState.put(state.hashCode(), state);
    }

    /*
     * 是否含有这个横坐标了
     */
    boolean containOrdinate(State state) {
        for (State state1 : stateMapy.keySet()) {
            if (state1.equals(state))
                return true;
        }

        return false;
    }

    /**
     * 在状态中添加一个 id 如果状态还不存在 就添加一个状态
     * @param x 横坐标
     * @param y 纵坐标
     */
    void addState(char x, State y, Node node0, Node node) {
        int ix = chMapx.get((int)x);
        int iy = stateMapy.get(y);

        if (stateTable[ix][iy] == null) {
            stateTable[ix][iy] = new State();
            stateTable[ix][iy].id.add(node0.id);
        } else {
            stateTable[ix][iy].id.add(node0.id);
        }

        if (node0.isEnd)
            stateTable[ix][iy].isEnd = true;
        if (node.isNoGreed)
            stateTable[ix][iy].Priority = 1;
        if (node.isStrs)
            stateTable[ix][iy].isStrs = true;
    }

    /**
     * 将一行状态 添加入状态队列中
     * @param state 要添加的那一行的纵坐标
     * @param states 要添加入的队列
     */
    void add(State state, ArrayList<State> states) {
        int iy = stateMapy.get(state);

        for (int i = 0; i < x; i++) {
            if (stateTable[i][iy] != null)
                states.add(stateTable[i][iy]);
        }
    }

    /*
     * 输出  匹配表
     */
    void showTable() {
        System.out.println("-----------showTable-----------");

        System.out.printf("%9c", ' ');
        System.out.print("  |");
        for (int i = 0; i < x; i++) {
            for (int ch : chMapx.keySet()) {
                if (chMapx.get(ch) == i) {
                    if (ch < 800)
                        System.out.printf("%6c", ch);
                    else if (ch == 800)
                        System.out.printf("%6s", "\\d");
                    else if (ch == 801)
                        System.out.printf("%6s", "\\D");
                    else if (ch == 802)
                        System.out.printf("%6s", "\\n");
                    else if (ch == 803)
                        System.out.printf("%6s", "\\r");
                    else if (ch == 804)
                        System.out.printf("%6s", "\\s");
                    else if (ch == 805)
                        System.out.printf("%6s", "\\S");
                    else if (ch == 806)
                        System.out.printf("%6s", "\\w");
                    else if (ch == 807)
                        System.out.printf("%6s", "\\W");
                    else if (ch == 808)
                        System.out.printf("%6s", ".");
                    else if (ch == 850)
                        System.out.printf("%6s", "CAPS");
                    else if (ch == 851)
                        System.out.printf("%6s", "CAPE");
                    else
                        System.out.printf("%6s", "FF");
                    System.out.print("  |");
                }
            }
        }
        System.out.println();


        for (int i = 0; i < y; i++) {

            for (State s: stateMapy.keySet()) {

                if (stateMapy.get(s) == i) {
                    if (s.isEnd) System.out.printf("%9s", "end " + s);
                    else System.out.printf("%9s", s);
                    System.out.print("  |");
                }
            }

            for (int j = 0; j < x; j++) {
                if (stateTable[j][i] != null) {
                    System.out.printf("%6s", stateTable[j][i]);
                    System.out.print("  |");
                }
                else {
                    System.out.printf("%6c", ' ');
                    System.out.print("  |");
                }
            }
            System.out.println();
        }

        System.out.println("-----------showTable-----------");
    }
}
