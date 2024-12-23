package src;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        Double[] t = {1., 1.};

        HornersScheme frame = new HornersScheme(t);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
