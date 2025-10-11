package othello.white;

public interface TicAgentGUI {
    void setAgent(TicAgent a);
    void showGUI();
    void notifyUser(String message);
    void updateBoardVisual(int[][] board);
    void activateValidMoves(int player); // 1=black
    void deactivateAll();
}