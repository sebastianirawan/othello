package othello.black;

public interface BlackAgentGui {
    void setAgent(BlackAgent a);
    void showGUI();
    void notifyUser(String message);
    void updateBoardVisual(int[][] board);
    void activateValidMoves(int player); // 1=black
    void deactivateAll();
}