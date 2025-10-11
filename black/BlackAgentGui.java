package othello.black;

public interface BlackAgentGui {
    void setAgent(BlackAgent a);   
    void show();   
    void hide();   
    void notifyUser(String message);   
    void dispose();
    void activateButton();
    javax.swing.JButton getButton(int btn);
}