package MS;

import javax.swing.*;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        EventQueue.invokeLater(() ->
        {
            JFrame f1 = new MineSweeperGame();
            f1.setTitle("Mine_sweeper");
            f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f1.setVisible(true);
        });
    }
}