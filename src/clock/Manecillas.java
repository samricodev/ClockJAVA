package clock;

import javax.swing.*;

public class Manecillas extends JFrame {
    Clock clock;
    public Manecillas() {
        setSize(500, 500);
        setTitle("Reloj");
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        clock = new Clock();
        add(clock);
    }

    public static void main(String[] args) {
        new Manecillas().setVisible(true);
    }
}
