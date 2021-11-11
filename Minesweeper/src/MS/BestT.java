package MS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BestT extends JDialog {
    private JPanel scores;
    private JPanel operation;
    private JButton clear;
    private JButton can;
    private Dimension screen;
    private JLabel b1;
    private JLabel b2;
    private JLabel i1;
    private JLabel i2;
    private JLabel e1;
    private JLabel e2;

    public BestT(Frame owner, String[][] score) {
        super(owner, "Fastest Mine Sweepers");
        Toolkit kit = Toolkit.getDefaultToolkit();
        screen = kit.getScreenSize();
        scores = new JPanel();
        scores.setLayout(new GridLayout(3, 3));
        scores.add(new JLabel("Beginner: "));
        b1 = new JLabel(score[1][0] + "seconds ");
        scores.add(b1);
        b2 = new JLabel(score[0][0]);
        scores.add(b2);
        scores.add(new JLabel("Intermediate: "));
        i1 = new JLabel(score[1][1] + "seconds ");
        scores.add(i1);
        i2 = new JLabel(score[0][1]);
        scores.add(i2);
        scores.add(new JLabel("Expert: "));
        e1 = new JLabel(score[1][2] + "seconds ");
        scores.add(e1);
        e2 = new JLabel(score[0][2]);
        scores.add(e2);
        add(scores, BorderLayout.CENTER);

        operation = new JPanel();
        operation.setLayout(new GridLayout(1, 2));
        clear = new JButton("Clear");
        ActionListener A1 = event -> {
            for (int j = 0; j < 3; j++) {
                score[0][j] = "Player1";
                score[1][j] = "999";
            }
            b1.setText(score[1][0] + "seconds ");
            b2.setText(score[0][0]);
            i1.setText(score[1][1] + "seconds ");
            i2.setText(score[0][1]);
            e1.setText(score[1][2] + "seconds ");
            e2.setText(score[0][2]);
        };
        clear.addActionListener(A1);
        can = new JButton("Cancel");
        can.addActionListener(event -> setVisible(false));
        operation.add(clear);
        operation.add(can);
        add(operation, BorderLayout.SOUTH);

        pack();

        this.setLocation(screen.width / 2, screen.height / 2);
    }
}
