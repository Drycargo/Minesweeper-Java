package MS;

import javax.swing.*;
import java.awt.*;

public class CustomizeDesigner extends JPanel {
    private JTextField Height;
    private JTextField Width;
    private JTextField Mine;
    private JButton OK;
    private JButton Cancel;
    private boolean ok;
    private JDialog custom;
    private Dimension screen;

    public CustomizeDesigner() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        screen = kit.getScreenSize();
        setLayout(new BorderLayout());
        JPanel data = new JPanel();
        data.setLayout(new GridLayout(3, 2));
        data.add(new JLabel("Height:"));
        data.add(Height = new JTextField(""));
        data.add(new JLabel("Width:"));
        data.add(Width = new JTextField(""));
        data.add(new JLabel("Mines:"));
        data.add(Mine = new JTextField(""));
        add(data, BorderLayout.CENTER);

        OK = new JButton("OK");
        OK.addActionListener(event -> {
            ok = true;
            custom.setVisible(false);
        });

        Cancel = new JButton("Cancel");
        Cancel.addActionListener(event -> custom.setVisible(false));

        JPanel bp = new JPanel();
        bp.setLayout(new GridLayout(2, 1));
        bp.add(OK);
        bp.add(Cancel);
        add(bp, BorderLayout.EAST);
    }

    public void seth(int h) {
        Height.setText(h + "");
    }

    public int geth() {
        return Integer.valueOf(Height.getText()).intValue();
    }

    public void setw(int w) {
        Width.setText(w + "");
    }

    public int getw() {
        return Integer.valueOf(Width.getText()).intValue();
    }

    public void setm(int m) {
        Mine.setText(m + "");
    }

    public int getm() {
        return Integer.valueOf(Mine.getText()).intValue();
    }

    public boolean shown(Component parent) {

        Frame owner = null;
        if (parent instanceof Frame)
            owner = (Frame) parent;
        else
            owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);

        if (custom == null || custom.getOwner() != owner) {
            custom = new JDialog(owner, true);
            custom.add(this);
            custom.getRootPane().setDefaultButton(OK);
            custom.pack();
            custom.setLocation(screen.width / 2, screen.height / 2);
            ok = false;
        }

        custom.setTitle("Custom File");
        custom.setVisible(true);
        return ok;
    }
}