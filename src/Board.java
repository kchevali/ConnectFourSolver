
public class Board {

    long board, pieces;// array of columns
    int height, width;
    long fullColumn;
    int moves;
    int totalMoves;

    int[] delta = new int[4];

    public Board(int height, int width) {
        board = 0;
        pieces = 0;
        this.height = height;
        this.width = width;
        this.fullColumn = (1L << height) - 1;
        this.totalMoves = height * width;
        this.moves = 0;
        delta[0] = 1;
        for (int i = 0; i < 3; i++)
            delta[i + 1] = height + i;
    }

    /**
     * Moves a piece into a column
     * 
     * @param piece 0 for yellow or -1 for red
     * @param index the column number
     * @return the bit that was inserted or 0 for no move possible
     */
    public long move(int index, long piece) {
        int x = index * (height + 1);
        long column = (board >> x) & fullColumn;
        // System.out.println("Column: " + column);
        if (column == fullColumn)
            return 0;
        long bit = column == 0 ? (1L << x) : Long.highestOneBit(column) << (x + 1);
        // System.out.println("Bit: " + bit);
        board |= bit;
        pieces |= (bit & piece);
        moves++;
        return bit;
    }

    public boolean isValid(int index) {
        return index >= 0 && (board & (1L << (height * (index + 1) + index - 1))) == 0;
    }

    public boolean isTie() {
        return moves == totalMoves;
    }

    public void unmove(long bit) {
        bit = ~bit;
        board &= bit;
        pieces &= bit;
        moves--;
    }

    public boolean isWin(long piece) {
        long mask = board & (piece ^ (~pieces));
        // # Horizontal check
        long m = mask & (mask >> 7);
        if ((m & (m >> 14)) != 0)
            return true;

        // # Diagonal \
        m = mask & (mask >> 6);
        if ((m & (m >> 12)) != 0)
            return true;
        // # Diagonal /
        m = mask & (mask >> 8);
        if ((m & (m >> 16)) != 0)
            return true;
        // # Vertical
        m = mask & (mask >> 1);
        if ((m & (m >> 2)) != 0)
            return true;
        // # Nothing found
        return false;
    }

    public String display(long last) {
        StringBuilder b = new StringBuilder();
        for (long bit = (1L << (height - 1)); bit != 0; bit >>= ((height + 1) * width) + 1) {
            for (int j = 0; j < width; j++, bit <<= (height + 1)) {
                if ((board & bit) > 0)
                    b.append(bit == last ? "\u001B[34m" : ((pieces & bit) == 0 ? "\u001B[33m" : "\u001B[31m"))
                            .append("●\u001B[0m");
                else
                    b.append(" ");
            }
            b.append("\n");
        }
        for (int i = 1; i <= width; i++) {
            b.append(i);
        }
        b.append("\n");
        return b.toString();
    }

    public String showBinary(long mask, long lastPlaced) {
        StringBuilder b = new StringBuilder();
        for (long bit = (1L << (height - 1)); bit != 0; bit >>= ((height + 1) * width) + 1) {
            for (int j = 0; j < width; j++, bit <<= (height + 1)) {
                b.append((mask & bit) > 0 ? "●" : " ");
            }
            b.append("\n");
        }
        for (int i = 1; i <= width; i++) {
            b.append(i);
        }
        b.append("\n");
        return b.toString();
    }
}