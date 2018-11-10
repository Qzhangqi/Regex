import java.io.IOException;
import java.io.Reader;

public class GoodReader extends Reader {

    private String str;
    private int length;
    private int next = 0;
    private int mark = 0;
    private int mark2 = 0;

    public GoodReader(String s) {
        this.str = s;
        this.length = s.length();
    }

    private void ensureOpen() throws IOException {
        if (str == null)
            throw new IOException("Stream closed");
    }

    public int read() throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (next >= length)
                return -1;
            return str.charAt(next++);
        }
    }

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

    public int peek() throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (next>= length)
                return -1;
            return str.charAt(next);
        }
    }

    public String readUntilEnd() throws IOException {
        StringBuffer re = new StringBuffer();

        while (this.ready()) {
            re.append((char) this.read());
        }

        return String.valueOf(re);
    }

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
        synchronized (lock) {
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
    }

    public boolean isHead() {
        if (next == 1)
            return true;
        else
            return false;
    }

    public boolean isEnd() {
        if (next == length)
            return true;
        else
            return false;
    }

    public void unread() {
        next--;
    }

    public long skip(long ns) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (next >= length)
                return 0;
            // Bound skip by beginning and end of the source
            long n = Math.min(length - next, ns);
            n = Math.max(-next, n);
            next += n;
            return n;
        }
    }

    public boolean ready() throws IOException {
        synchronized (lock) {
            if (next >= length)
                return false;
            else
                return true;
        }
    }

    public boolean markSupported() {
        return true;
    }

    public void mark(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0){
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (lock) {
            ensureOpen();
            mark = next;
        }
    }

    public void reset() throws IOException {
        synchronized (lock) {
            ensureOpen();
            next = mark;
        }
    }

    public void mark2(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0){
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (lock) {
            ensureOpen();
            mark2 = next;
        }
    }

    public void reset2() throws IOException {
        synchronized (lock) {
            ensureOpen();
            next = mark2;
        }
    }

    public void close() {
        str = null;
    }
}

