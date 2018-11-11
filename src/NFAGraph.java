import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NFAGraph implements Cloneable {
    Node start;
    Node end;

    private NFAGraph cloneNFA; //克隆图时用
    private HashMap<Node, Node> cloneMap; //克隆图时用

    NFAGraph(Node start, Node end) {
        this.start = start;
        this.end   = end;
    }

    private NFAGraph() {
        this.start = null;
        this.end   = null;
    }

    /*
     * 可选
     */
    void addSToE() {
        start.addNextNode(' ', end);
    }

    /*
     * 串联
     */
    void seriesGraph(NFAGraph graph) {
        this.end.addNextNode(' ', graph.start);
        this.end = graph.end;
    }

    /*
     * 加捕获头尾
     */
    void captureGraph(String captureName) {
        if (captureName.equals("")) {
            Node start = new Node();
            start.addNextNode(Regex.CAPHEAD, this.start);
            this.start = start;

            Node end   = new Node();
            this.end.addNextNode(Regex.CAPEND, end);
            this.end = end;
        }
    }


    /*
     * 并联
     */
    void parallelGraph(NFAGraph graph) {
        Node start = new Node();
        Node end   = new Node();
        start.addNextNode(' ', graph.start);
        start.addNextNode(' ', this.start);
        this.end.addNextNode(' ', end);
        graph.end.addNextNode(' ', end);
        this.start = start;
        this.end = end;

    }

    /*
     * 重复 * 0 到无限次
     */
    void repeatGraph() {
        Node node = new Node();
        this.end.addNextNode(' ', node);
        node.addNextNode(' ', this.start);
        this.start = node;
        this.end   = node;
    }

    /*
     * 重复 + 1 到无限次
     */
    void repeatGraph0() {
        NFAGraph nfaGraph0 = this.clone();
        nfaGraph0.repeatGraph();
        this.end.addNextNode(' ', nfaGraph0.start);
        this.end = nfaGraph0.end;
    }

    /*
     * 重复 ? 0 到 1 次
     */
    void repeatGraph1() {
        this.addSToE();
    }

    /*
     * 重复 1 次
     */
    void repeatGraph2() {
        NFAGraph nfaGraph0 = this.clone();
        this.end.addNextNode(' ', nfaGraph0.start);
        this.end = nfaGraph0.end;
    }

    /*
     * 重复 n 次
     */
    void repeatGraph3(int n) {
        NFAGraph nfaGraph0 = this.clone();
        for (int i = 1; i < n; i++) {
            NFAGraph nfaGraph1 = nfaGraph0.clone();
            this.end.addNextNode(' ', nfaGraph1.start);
            this.end = nfaGraph1.end;
        }
    }

    /*
     * 重复 n 到 m 次 m = -1 表示无限
     */
    void repeatGraph4(int n, int m, boolean noGreed) {
        NFAGraph nfaGraph0 = this.clone();
        for (int i = 1; i < n; i++) {
            NFAGraph nfaGraph1 = nfaGraph0.clone();
            this.end.addNextNode(' ', nfaGraph1.start);
            this.end = nfaGraph1.end;
        }

        Node end = new Node();
        this.end.addNextNode(' ', end);

        for (int i = 0; i < m - n; i++) {
            NFAGraph nfaGraph1 = nfaGraph0.clone();
            this.end.addNextNode(' ', nfaGraph1.start);
            this.end = nfaGraph1.end;
            if (noGreed) this.end.addNextNode(Regex.NOGREED, end);
            else this.end.addNextNode(' ', end);
        }

        this.end = end;
    }

    /*
     * 否定预查
     */
    void noconGraph(int nocon) {
        Node start = new Node();
        Node end   = new Node();
        start.addNextNode(nocon, end);
        this.start = start;
        this.end = end;
    }

    /*
     * 非贪婪
     */
    void noGreedGraph() {
        Node node = new Node();
        this.end.addNextNode(Regex.NOGREED, node);
        this.end = node;
    }

    /*
     * 克隆此 NFA 图
     */
    @Override
    protected NFAGraph clone() {
        cloneNFA = new NFAGraph();
        cloneMap = new HashMap<>();

        nodeClone(start);

        return cloneNFA;
    }

    private Node nodeClone(Node node) {
        if (cloneMap.containsKey(node)) return cloneMap.get(node);

        Node node0 = new Node();
        cloneMap.put(node, node0);

        if (cloneNFA.start == null) cloneNFA.start = node0;

        HashMap<Integer, List<Node>> nextNodes0 = new HashMap<>();
        for (int i : node.nextNodes.keySet()) {
            List<Node> nodes = node.nextNodes.get(i);
            List<Node> nodes0 = new ArrayList<>();
            for (Node node1 : nodes) {
                nodes0.add(nodeClone(node1));
            }
            nextNodes0.put(i, nodes0);
        }

        node0.nextNodes = nextNodes0;

        if (node0.nextNodes.isEmpty()) cloneNFA.end = node0;

        return node0;
    }
}
