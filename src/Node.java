import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Node {
    /*
     * 用整形表示路径上的字符 /d 299 /w 298
     * List<Node> 表示这个字符可以转向的 NextNode
     */
    Map<Integer, List<Node>> nextNodes;

    /*
     * 是否已经遍历过
     */
    boolean look    = false;

    /*
     * 是否是开始节点
     */
    boolean isStart = false;

    /*
     * 是否是结束节点
     */
    boolean isEnd   = false;

    boolean isNoGreed = false;
//    boolean isCapend = false;
//    boolean isCapstart = false;
    boolean isStre = false;
    boolean isStrs = false;
    int isNocon = 0;

    /*
     * 节点的 id
     */
    int id;

    /*
     * 用于生成 Node 的自增长 id
     */
    static int nodeIncreaseId = 0;

    /*
     * 生成的所有节点的集合
     * 用于多次遍历时的恢复
     */
    static ArrayList<Node> allNodes = new ArrayList<>();

    /*
     * 自带 id 的构造方法
     */
    Node() {
        id = nodeIncreaseId++;
        nextNodes = new HashMap<>();
        allNodes.add(this);
    }

    /*
     * 返回 id 列表
     */
    int getId() {
        return id;
    }

    /*
     * 恢复所有节点的 Look
     */
    static void unLook() {
        for (Node node : allNodes) {
            node.look = false;
        }
    }

    /*
     * 添加一个 NextNode
     */
    public void addNextNode(int pathChar, Node nextNode) {
        if (nextNodes.containsKey(pathChar)) {
            nextNodes.get(pathChar).add(nextNode);
        } else {
            ArrayList<Node> nextNodes0 = new ArrayList<>();
            nextNodes0.add(nextNode);
            nextNodes.put(pathChar, nextNodes0);
        }
    }

    public void addNextNode(char pathChar, Node nextNode) {
        addNextNode((int) pathChar, nextNode);
    }

}