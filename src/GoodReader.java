import java.io.IOException;

public class GoodReader {

    private String str;
    private int length;
    private int next = 0;
    private int mark = 0;
    private int mark2 = 0;

    GoodReader(String s) {
        this.str = s;
        this.length = s.length();
    }

    private void ensureOpen() throws IOException {
        if (str == null)
            throw new IOException("Stream closed");
    }

    int read() throws IOException {
        ensureOpen();
        if (next >= length)
            return -1;
        return str.charAt(next++);
    }

    /*
     * 读取直到遇到 uch 这个字符
     * 返回不包含 uch 但 uch 已被读取
     */
    public String readUntilCh(char uch) throws IOException {
        StringBuffer re = new StringBuffer();

        int tch = this.read();
        while (tch != uch) {
            re.append((char) tch);
            if (!this.ready())
                throw new IOException("readUntilCh() 找不到指定字符");
            tch = this.read();
        }

        return String.valueOf(re);
    }

    /*
     * 预读取一个字符
     */
    public int peek() throws IOException {
        ensureOpen();
        if (next>= length)
            return -1;
        return str.charAt(next);
    }

    /*
     * 读取到结束
     */
    public String readUntilEnd() throws IOException {
        StringBuffer re = new StringBuffer();

        while (this.ready()) {
            re.append((char) this.read());
        }

        return String.valueOf(re);
    }

    /*
     * 读取 （） 中内容
     */
    public String readContentInBracket() throws IOException {
        StringBuffer re = new StringBuffer();
        int bracketnums = 0;      //内层括号数

        int tch = this.read();
        while (tch != (int)')' || bracketnums != 0) {
            if (tch == (int)'(')
                bracketnums++;
            if (tch == (int)')')
                bracketnums--;

            re.append((char) tch);
            if (!this.ready())
                throw new IOException("readUntilCh() 找不到指定字符");
            tch = this.read();
        }

        return String.valueOf(re);
    }



    public int read(char cbuf[], int off, int len) throws IOException {
        ensureOpen();
        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        if (next >= length)
            return -1;
        int n = Math.min(length - next, len);
        str.getChars(next, next + n, cbuf, off);
        next += n;
        return n;
    }

    /*
     * 当前是否在字符串头
     */
    public boolean isHead() {
        return next == 1;
    }

    /*
     * 当前是否在字符串尾
     */
    public boolean isEnd() {
        return next == length;
    }

    /*
     * 退回一个字符
     */
    public void unread() throws IOException {
        if (next > 0)
            next--;
        else
            throw new IOException("无法退回");
    }

    public long skip(long ns) throws IOException {
        ensureOpen();
        if (next >= length)
            return 0;
        long n = Math.min(length - next, ns);
        n = Math.max(-next, n);
        next += n;
        return n;
    }

    public boolean ready() throws IOException {
        if (next >= length)
            return false;
        else
            return true;
    }

    public boolean markSupported() {
        return true;
    }

    public void mark(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0){
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        ensureOpen();
        mark = next;
    }

    public void reset() throws IOException {
        ensureOpen();
        next = mark;
    }

    public void mark2(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0){
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        ensureOpen();
        mark2 = next;
    }

    public void reset2() throws IOException {
        ensureOpen();
        next = mark2;
    }

    public void close() {
        str = null;
    }
}

