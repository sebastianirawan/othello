package othello.white;

import javax.swing.JFrame;

public class WhiteAgentGuiImpl extends JFrame implements WhiteAgentGui {
    private WhiteAgent myAgent;

    public WhiteAgentGuiImpl() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        btn1 = new javax.swing.JButton();

        setSize(400, 300);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }

    public void setAgent(WhiteAgent a) {
        myAgent = a;
        setTitle(myAgent.getName());
    }

    private javax.swing.JButton btn1;
}
