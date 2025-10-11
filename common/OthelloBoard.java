package othello.common;

import java.util.ArrayList;
import java.util.List;

public class OthelloBoard {
    // -1 = empty, 1 = black, 0 = white
    public int[][] board;

    public OthelloBoard() {
        board = new int[8][8];
        initBoard();
    }

    public void initBoard() {
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                board[i][j] = -1;
            }
        }
        // standard initial position
        board[3][3] = 0; // white
        board[3][4] = 1; // black
        board[4][3] = 1; // black
        board[4][4] = 0; // white
    }

    // Return list of valid moves as list of int[]{r,c}
    public List<int[]> getValidMoves(int player) {
        List<int[]> moves = new ArrayList<>();
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                if (board[r][c]==-1 && isValidMove(player, r, c)) {
                    moves.add(new int[]{r,c});
                }
            }
        }
        return moves;
    }

    public boolean hasAnyValidMove(int player) {
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                if (board[r][c]==-1 && isValidMove(player, r, c)) return true;
            }
        }
        return false;
    }

    public boolean isValidMove(int player, int row, int col) {
        if (row<0 || row>=8 || col<0 || col>=8) return false;
        if (board[row][col] != -1) return false;
        int opponent = (player == 1) ? 0 : 1;
        int[] dr = {-1,-1,-1,0,0,1,1,1};
        int[] dc = {-1,0,1,-1,1,-1,0,1};
        for (int d=0; d<8; d++){
            int r = row + dr[d];
            int c = col + dc[d];
            boolean hasOpponentBetween = false;
            while (r>=0 && r<8 && c>=0 && c<8 && board[r][c] == opponent){
                hasOpponentBetween = true;
                r += dr[d];
                c += dc[d];
            }
            if (hasOpponentBetween && r>=0 && r<8 && c>=0 && c<8 && board[r][c] == player){
                return true;
            }
        }
        return false;
    }

    // Apply move and flip pieces (assumes move is valid)
    public void applyMove(int player, int row, int col) {
        board[row][col] = player;
        int opponent = (player == 1) ? 0 : 1;
        int[] dr = {-1,-1,-1,0,0,1,1,1};
        int[] dc = {-1,0,1,-1,1,-1,0,1};
        for (int d=0; d<8; d++){
            int r = row + dr[d];
            int c = col + dc[d];
            boolean hasOpponentBetween = false;
            while (r>=0 && r<8 && c>=0 && c<8 && board[r][c] == opponent){
                hasOpponentBetween = true;
                r += dr[d];
                c += dc[d];
            }
            if (hasOpponentBetween && r>=0 && r<8 && c>=0 && c<8 && board[r][c] == player){
                // flip
                int fr = row + dr[d];
                int fc = col + dc[d];
                while (!(fr == r && fc == c)) {
                    board[fr][fc] = player;
                    fr += dr[d];
                    fc += dc[d];
                }
            }
        }
    }

    public int[] countPieces() {
        int black=0, white=0;
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                if (board[r][c] == 1) black++;
                if (board[r][c] == 0) white++;
            }
        }
        return new int[]{black, white};
    }

    // Utility copy if needed
    public int[][] copyBoard() {
        int[][] b = new int[8][8];
        for (int i=0;i<8;i++) System.arraycopy(board[i], 0, b[i], 0, 8);
        return b;
    }
}