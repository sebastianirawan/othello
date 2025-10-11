package othello.white;

public interface WhiteAgentGui {
    void setAgent(WhiteAgent a);
    void showGUI();
    void notifyUser(String message);
    void updateBoardVisual(int[][] board);
    void activateValidMoves(int player); // player: 0=white,1=black
    void deactivateAll();
}
