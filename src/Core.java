
public class Core {

    Board board;
    int maxDepth, maxMoves;
    final int winBit = (1 << 30);
    final int tieBit = (1 << 29);
    final int scoreBits = winBit | tieBit;
    final int countBits = (63 << 23);
    final int countBit = (1 << 23);
    final int infoBits = scoreBits | countBits;

    static int numMoves = 0;
    static int[] indices = { 3, 2, 4, 1, 5, 6, 0 };

    public Core(Board board, int maxDepth, int maxMoves) {
        this.board = board;
        this.maxDepth = maxDepth;
        this.maxMoves = maxMoves;
    }

    public int getBestMove(long piece) {
        numMoves = 0;
        return getBestMove(piece, 0);
    }

    // piece is all 1s for R or 0 for B
    public int getBestMove(long piece, int depth) {

        int best = 0;
        int bestCount = 0;
        for (int index : indices) {
            numMoves++;
            long bit = board.move(index, piece);
            if (bit != 0) {
                // if (depth == 0) {
                // System.out.println("=================================");
                // System.out.println("Turn: " + (piece == -1 ? "RED" : "YELLOW") + " Col: " +
                // index);
                // System.out.println(board);
                // }
                if (board.isWin(piece)) {
                    board.unmove(bit);
                    // if (depth == 0)
                    // System.out.println((index + 1) + ". Gonna win boi!");
                    return index | winBit;
                }

                if (depth == maxDepth) {
                    board.unmove(bit);
                    return tieBit;
                }

                int next = getBestMove(-1 - piece, depth + 1);
                int count = (next & countBits) + countBit;

                // other player lost
                if ((next & scoreBits) == 0) {
                    board.unmove(bit);
                    // if (depth == 0)
                    // System.out.println((index + 1) + ". Going to win!!");
                    return index | winBit | count;
                }

                // best is losing
                if ((best & scoreBits) == 0) {

                    // other player won
                    if ((next & tieBit) == 0) {
                        // if (depth == 0)
                        // System.out.println((index + 1) + ". Going to lose");
                        if (count > bestCount) {
                            bestCount = count;
                            best = index | count;
                        }
                        // other player tied
                    } else {
                        // if (depth == 0)
                        // System.out.println((index + 1) + ". EH");
                        best = index | tieBit;
                    }
                }

                board.unmove(bit);
            }
        }
        return best;
    }
}