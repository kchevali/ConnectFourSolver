import java.util.*;

class Main {
    public static void main(String[] args) {

        int height = 6, width = 7;
        Board board = new Board(height, width);

        // int[] moves = { 3, 5, 4, 5, 6, 4, 3, 4, 3, 3, 4, 4, 5, 4 };
        // int t = 0;
        // long b = 0;
        // for (int m : moves) {
        // // System.out.println("Moving to index: " + m);
        // b = board.move(m - 1, t);
        // // board.unmove(bit);
        // t = -1 - t;
        // }
        // System.out.println(board);
        // System.out.println("Is Win: " + board.isWin(-1 - t, b));

        // System.exit(0);
        boolean isPlayerFirst = true;// player is yellow
        int maxDepth = 10;
        int maxMoves = 201412519;

        Scanner scan = new Scanner(System.in);
        Core core = new Core(board, maxDepth, maxMoves);
        long turn = isPlayerFirst ? -1 : 0, bit;
        // int round = 0;

        do {
            turn = -1 - turn;
            int index;
            if (turn == 0) {
                // players turn
                System.out.println("\nWhich column did the player move? (Left = 1)");
                do {
                    index = scan.nextInt() - 1;
                    if (!board.isValid(index)) {
                        System.err.println("Invalid column!");
                    }
                } while (!board.isValid(index));
            } else {
                // cpu turn
                int move = core.getBestMove(turn);
                boolean isWin = (move & core.scoreBits) == core.winBit;
                boolean isLoss = (move & core.scoreBits) == 0;
                int finalHour = ((move & core.countBits) >> 23);
                index = move & (~core.infoBits);

                if (isLoss) {
                    for (int i : Core.indices) {
                        if (board.isValid(i)) {
                            index = i;
                            break;
                        }
                    }
                }

                System.out.println("Num of moves checked: " + Core.numMoves);
                System.out.println("Final hour: " + finalHour);
                System.out.println(
                        "CPU move to: " + (index + 1) + " score: " + (isWin ? "WIN" : (isLoss ? "LOSE" : "ZERO")));
            }
            bit = board.move(index, turn);
            // round++;
            System.out.println(board.display(0));
        } while (!board.isWin(turn) && !board.isTie());

        System.out.println(board.isWin(turn) ? ("The winner is: " + (turn == -1 ? "RED" : "YELLOW")) : "TIE");

    }
}
