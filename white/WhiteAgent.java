package othello.white;

import jade.core.Agent;
import jade.core.behaviours.*;

public class WhiteAgent extends Agent {
    private WhiteAgentGui whiteGui;

    public void setup() {
        // welcome message
        System.out.println("hi from white");

        // show ui
        whiteGui = new WhiteAgentGuiImpl();
        whiteGui.setAgent(this);
        whiteGui.show();
    }
}
