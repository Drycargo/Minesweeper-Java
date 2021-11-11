package MS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

public class ms extends JFrame {
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screen = kit.getScreenSize();
    public boolean first_click;
    public int mode;
    public int length = 9;
    public int height = 9;
    public int chang;
    public int kuan;
    public int m_num = 10;
    public JPanel board;
    public JPanel game;
    public JTextField mine;
    public JTextField time;
    public JButton begin;
    public bloc[][] mines;
    public JLabel blank = new JLabel(" ");
    public ActionListener a2;
    public boolean start = true;
    public boolean lose = false;//ͬʱ��ʾʤ���ͽ���
    public static final Font f3 = new Font("Serif", Font.BOLD, 15);
    public MH mh;
    public JMenuBar mb;
    public CustomizeDesigner CustomFile;
    public timer TIMER;
    public int[] dimensions;
    public String[][] besttimes;
    public Properties setting;
    public File SF;
    public BestTime scoreD;

    public ms() {
        dimensions = new int[3];
		/*dimensions[0]=dimensions[1]=9;
		dimensions[2]=10;*/
        besttimes = new String[2][3];
		/*besttimes[0][0]=besttimes[0][1]=besttimes[0][2]="Player1";
		besttimes[1][0]=besttimes[1][1]=besttimes[1][2]="999";*/


        String info = System.getProperty("MS.settings");
        File MSDir = new File(info, ".information");
        if (!MSDir.exists())
            MSDir.mkdir();
        SF = new File(MSDir, "program.properties");

        Properties DFT = new Properties();
        DFT.setProperty("Height", "9");
        DFT.setProperty("Width", "9");
        DFT.setProperty("Mines", "10");
        DFT.setProperty("BeginnerBest_Name", "Player1");
        DFT.setProperty("BeginnerBest_Time", "999");
        DFT.setProperty("InterBest_Name", "Player1");
        DFT.setProperty("InterBest_Time", "999");
        DFT.setProperty("ExpertBest_Name", "Player1");
        DFT.setProperty("ExpertBest_Time", "999");
        DFT.setProperty("chang", "350");
        DFT.setProperty("kuan", "300");

        setting = new Properties(DFT);

        if (SF.exists()) {
            try (InputStream in = new FileInputStream(SF)) {
                setting.load(in);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        chang = Integer.parseInt(setting.getProperty("chang"));
        kuan = Integer.parseInt(setting.getProperty("kuan"));
        dimensions[0] = length = Integer.parseInt(setting.getProperty("Height"));
        dimensions[1] = height = Integer.parseInt(setting.getProperty("Width"));
        dimensions[2] = m_num = Integer.parseInt(setting.getProperty("Mines"));
        besttimes[0][0] = setting.getProperty("BeginnerBest_Name");
        besttimes[0][1] = setting.getProperty("InterBest_Name");
        besttimes[0][2] = setting.getProperty("ExpertBest_Name");
        besttimes[1][0] = setting.getProperty("BeginnerBest_Time");
        besttimes[1][1] = setting.getProperty("InterBest_Time");
        besttimes[1][2] = setting.getProperty("ExpertBest_Time");

        mb = new JMenuBar();
        this.setJMenuBar(mb);

        mines = new bloc[30][30];

        board = new JPanel();
        board.setLayout(new GridLayout(1, 5));

        mine = new JTextField("" + m_num, 3);
        time = new JTextField("0", 3);
        begin = new JButton("ouo");

        mine.setEditable(false);
        time.setEditable(false);

        board.add(mine);
        board.add(blank);
        board.add(begin);
        board.add(new JLabel(" "));
        board.add(time);

        ActionListener a1 = event -> {
            for (int i = 0; i < length; i++)
                for (int j = 0; j < height; j++)
                    mines[i][j].setEnabled(true);
            Sta();
        };
        begin.addActionListener(a1);
        begin.setMargin(new Insets(0, 0, 0, 0));


        JMenu GAME = new JMenu("Game");//�˵���&�˵�
        mb.add(GAME);

        JMenuItem NG = new JMenuItem("New Game");//����Ϸ
        NG.addActionListener(a1);
        GAME.add(NG);
        NG.setAccelerator(KeyStroke.getKeyStroke("F2"));
        GAME.addSeparator();

        ButtonGroup BG = new ButtonGroup();
        JRadioButtonMenuItem Beginner = new JRadioButtonMenuItem("Beginner");
        ActionListener aB = event -> {
            if (game != null) {
                game.removeAll();
                game.setVisible(false);
            }
            start = true;
            chang = 350;
            kuan = 300;
            length = 9;
            height = 9;
            m_num = 10;
            begin.setText("ouo");
            lose = false;
            mode = 0;
            Sta();
        };
        Beginner.addActionListener(aB);
        JRadioButtonMenuItem Inter = new JRadioButtonMenuItem("Intermediate");
        ActionListener aI = event -> {
            if (game != null) {
                game.removeAll();
                game.setVisible(false);
            }
            start = true;
            chang = 530;
            kuan = 480;
            length = 16;
            height = 16;
            m_num = 40;
            begin.setText("ouo");
            lose = false;
            mode = 1;
            Sta();
        };
        Inter.addActionListener(aI);
        JRadioButtonMenuItem Expert = new JRadioButtonMenuItem("Expert");
        ActionListener aE = event -> {
            if (game != null) {
                game.removeAll();
                game.setVisible(false);
            }
            start = true;
            chang = 500;
            kuan = 650;
            length = 16;
            height = 30;
            m_num = 99;
            begin.setText("ouo");
            lose = false;
            mode = 2;
            Sta();
        };
        Expert.addActionListener(aE);
        JRadioButtonMenuItem CST = new JRadioButtonMenuItem("Custom File");
        CST.addActionListener(new CA());
        GAME.add(Beginner);
        GAME.add(Inter);
        GAME.add(Expert);
        GAME.add(CST);
        BG.add(Beginner);
        BG.add(Inter);
        BG.add(Expert);
        BG.add(CST);
        GAME.addSeparator();

        JMenuItem BT = new JMenuItem("Best Times");
        GAME.add(BT);
        BT.addActionListener(event -> {
            scoreD = new BestTime(ms.this, besttimes);
            scoreD.setVisible(true);
        });
        GAME.addSeparator();

        JMenuItem ex = new JMenuItem("Exit");
        ex.addActionListener(event -> {
            System.exit(0);
        });
        GAME.add(ex);

        a2 = event -> {
            int a = ((bloc) event.getSource()).gethang();
            int b = ((bloc) event.getSource()).getlie();
            trigger(a, b);
        };

        mh = new MH();

        if (dimensions[0] == 9 && dimensions[1] == 9 && dimensions[2] == 10)//ȷ��ģʽ
        {
            mode = 0;
            Beginner.setSelected(true);
        } else if (dimensions[0] == 16 && dimensions[1] == 16 && dimensions[2] == 40) {
            mode = 1;
            Inter.setSelected(true);
        } else if (dimensions[0] == 30 && dimensions[1] == 16 && dimensions[2] == 99) {
            mode = 2;
            Expert.setSelected(true);
        } else {
            mode = 3;
            CST.setSelected(true);
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setting.setProperty("Height", "" + length);
                setting.setProperty("Width", "" + height);
                setting.setProperty("Mines", "" + m_num);
                setting.setProperty("BeginnerBest_Name", besttimes[0][0]);
                setting.setProperty("BeginnerBest_Time", besttimes[1][0]);
                setting.setProperty("InterBest_Name", besttimes[0][1]);
                setting.setProperty("InterBest_Time", besttimes[1][1]);
                setting.setProperty("ExpertBest_Name", besttimes[0][2]);
                setting.setProperty("ExpertBest_Time", besttimes[1][2]);
                setting.setProperty("chang", "" + getHeight());
                setting.setProperty("kuan", "" + getWidth());
                try (OutputStream out = new FileOutputStream(SF)) {
                    setting.store(out, "Minesweeper Settings");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });

        Sta();

    }

    public void setlength(int l) {
        length = l;
    }

    public int getlength() {
        return length;
    }

    public void setheight(int h) {
        height = h;
    }

    public int getheight() {
        return height;
    }

    public void setm_num(int m) {
        m_num = m;
    }

    public int getm_num() {
        return m_num;
    }

    private class CA implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (CustomFile == null)
                CustomFile = new CustomizeDesigner();
            CustomFile.seth(length);
            CustomFile.setw(height);
            CustomFile.setm(m_num);

            if (CustomFile.shown(ms.this)) {
                int th = CustomFile.geth();
                int tw = CustomFile.getw();
                int tm = CustomFile.getm();

                if (th < 9)
                    length = 9;
                else if (th > 24)
                    length = 24;
                else
                    length = th;

                if (tw < 9)
                    height = 9;
                else if (tw > 30)
                    height = 30;
                else
                    height = tw;

                if (tm < 10)
                    m_num = 10;
                else if (tm > (int) (0.9 * length * height))
                    m_num = (int) (0.9 * length * height);
                else
                    m_num = tm;

            }
            if (game != null) {
                game.removeAll();
                game.setVisible(false);
            }
            begin.setText("ouo");
            lose = false;
            start = true;
            mode = 3;
            Sta();
        }
    }

    public void Sta() {

        this.setLocation((screen.width - chang) / 2, (screen.height - kuan) / 2);
        this.setSize(chang, kuan);
        first_click = false;

        //����
        if (start) {

            game = new JPanel();
            game.setLayout(new GridLayout(length, height));
            for (int i = 0; i < length; i++)
                for (int j = 0; j < height; j++) {
                    mines[i][j] = new bloc(i, j);
                    game.add(mines[i][j]);
                }
            game.setVisible(true);
            start = false;
        } else {
            for (int i = 0; i < length; i++)
                for (int j = 0; j < height; j++) {
                    mines[i][j].setnum(0);
                    mines[i][j].setflag(0);
                    mines[i][j].setEnabled(true);
                    mines[i][j].setText("");
                }
            begin.setText("ouo");
            lose = false;
        }
        mine.setText(m_num + "");
        time.setText("" + 0);

        add(board, BorderLayout.NORTH);
        add(game, BorderLayout.CENTER);
        pack();
    }

    public void trigger(int a, int b) {
        if (mines[a][b].getflag() == 1)
            return;
        if (!first_click) {
            //���ŵ���
            int gen;
            int temper;
            int[] temp = new int[m_num];

            //do {
            gen = 0;
            for (int i = 0; i < m_num; i++)
                temp[i] = -1;

            while (gen < m_num) {
                temper = (int) (Math.random() * length * height);
                if (!comp(temp, temper) && (length * a + b != temper)
                        && (length * (a + 1) + b != temper) && (length * (a - 1) + b != temper)
                        && (length * a + b + 1 != temper) && (length * a + b - 1 != temper)
                        && (length * (a + 1) + b + 1 != temper) && (length * (a - 1) + b + 1 != temper)
                        && (length * (a - 1) + b - 1 != temper) && (length * (a + 1) + b - 1 != temper)) {
                    temp[gen] = temper;
                    gen++;
                }
            }

            for (int unit : temp) {
                mines[(int) unit / height][unit % height].setnum(-1);
            }
            //��������
            for (int x = 0; x < length; x++)
                for (int y = 0; y < height; y++) {
                    int self = count(x, y);
                    if (mines[x][y].getnum() != -1)
                        mines[x][y].setnum(self);
                }
            //}while(count(a,b)!=0);
            mine.setText("" + m_num);
            time.setText("0");
            first_click = true;
            mines[a][b].clicked();
            TIMER = new timer();
            Thread t = new Thread(TIMER);
            t.start();
        }

        if (!lose) {
            mines[a][b].setEnabled(false);

            if (mines[a][b].getnum() != -1)//����
            {
                int self = mines[a][b].getnum();
                mines[a][b].clicked();
                if (self != 0) {
                    mines[a][b].setText("" + self);
                } else//�ո�
                {
                    blow_up(a, b, mines);
                }

                int all = 0;

                for (int i = 0; i < length; i++)
                    for (int j = 0; j < height; j++)
                        if (mines[i][j].isEnabled())
                            all++;
                if (all == m_num)//ʤ��
                {
                    lose = true;
                    begin.setText("^w^");
                    for (int i = 0; i < length; i++)
                        for (int j = 0; j < height; j++)
                            if (mines[i][j].getnum() == -1)
                                mines[i][j].setText("F");
                    if (mode != 3) {
                        if (Integer.parseInt(time.getText()) < Integer.parseInt(besttimes[1][mode])) {
                            besttimes[0][mode] = JOptionPane.showInputDialog("You have beat the record!Input your name:");
                            besttimes[1][mode] = time.getText();
                        }
                    }
                }
            } else//����
            {
                mines[a][b].setText("*");
                for (int i = 0; i < length; i++)
                    for (int j = 0; j < height; j++) {
                        if (mines[i][j].getnum() == -1) {
                            mines[i][j].setText("*");
                            mines[i][j].setEnabled(false);
                        }
                        if (mines[i][j].getnum() != -1 && mines[i][j].getflag() == 1) {
                            mines[i][j].setText("x");
                            mines[i][j].setEnabled(false);
                        }
                    }
                begin.setText("x.x");
                lose = true;
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(kuan, chang);
    }

    public boolean comp(int[] list, int orig) {
        for (int unit : list)
            if (unit == orig)
                return true;
        return false;
    }

    public int count(int x, int y) {
        int total = 0;
        //���鿿��
        if (x == 0) {
            if (mines[x + 1][y].getnum() == -1)
                total++;
            //���Ͻ�
            if (y == 0) {
                if (mines[x][y + 1].getnum() == -1)
                    total++;
                if (mines[x + 1][y + 1].getnum() == -1)
                    total++;
            }
            //���Ͻ�
            else if (y == (height - 1)) {
                if (mines[x][y - 1].getnum() == -1)
                    total++;
                if (mines[x + 1][y - 1].getnum() == -1)
                    total++;
            }
            //ʣ�����
            else {
                if (mines[x][y - 1].getnum() == -1)
                    total++;
                if (mines[x][y + 1].getnum() == -1)
                    total++;
                if (mines[x + 1][y - 1].getnum() == -1)
                    total++;
                if (mines[x + 1][y + 1].getnum() == -1)
                    total++;
            }
        }
        //���鿿��
        else if (x == (length - 1)) {
            if (mines[x - 1][y].getnum() == -1)
                total++;
            //���½�
            if (y == 0) {
                if (mines[x][y + 1].getnum() == -1)
                    total++;
                if (mines[x - 1][y + 1].getnum() == -1)
                    total++;
            }
            //���½�
            else if (y == (height - 1)) {
                if (mines[x][y - 1].getnum() == -1)
                    total++;
                if (mines[x - 1][y - 1].getnum() == -1)
                    total++;
            }
            //ʣ�����
            else {
                if (mines[x][y - 1].getnum() == -1)
                    total++;
                if (mines[x][y + 1].getnum() == -1)
                    total++;
                if (mines[x - 1][y - 1].getnum() == -1)
                    total++;
                if (mines[x - 1][y + 1].getnum() == -1)
                    total++;
            }
            //�ڵ�
        } else {
            if (mines[x + 1][y].getnum() == -1)
                total++;
            if (mines[x - 1][y].getnum() == -1)
                total++;
            //����
            if (y == 0) {
                if (mines[x][y + 1].getnum() == -1)
                    total++;
                if (mines[x + 1][y + 1].getnum() == -1)
                    total++;
                if (mines[x - 1][y + 1].getnum() == -1)
                    total++;
            }
            //����
            else if (y == (height - 1)) {
                if (mines[x][y - 1].getnum() == -1)
                    total++;
                if (mines[x + 1][y - 1].getnum() == -1)
                    total++;
                if (mines[x - 1][y - 1].getnum() == -1)
                    total++;
            }
            //ʣ�����
            else {
                if (mines[x][y - 1].getnum() == -1)
                    total++;
                if (mines[x][y + 1].getnum() == -1)
                    total++;
                if (mines[x + 1][y - 1].getnum() == -1)
                    total++;
                if (mines[x + 1][y + 1].getnum() == -1)
                    total++;
                if (mines[x - 1][y - 1].getnum() == -1)
                    total++;
                if (mines[x - 1][y + 1].getnum() == -1)
                    total++;
            }
        }
        return total;
    }

    public void traceleft(int a, int b, boolean[] zero) {
        zero[b] = true;

        int min = b - 1;
        while (min >= 0 && mines[a][min].getnum() == 0) {
            zero[min] = true;
            mines[a][min].setEnabled(false);
            min--;
        }
        if (min != -1) {
            mines[a][min].setEnabled(false);
            mines[a][min].setText("" + count(a, min));
            zero[min] = false;
        }
    }

    public void traceright(int a, int b, boolean[] zero) {
        zero[b] = true;

        int max = b + 1;
        while (max < height && mines[a][max].getnum() == 0) {
            zero[max] = true;
            mines[a][max].setEnabled(false);
            max++;
        }
        if (max != height) {
            mines[a][max].setEnabled(false);
            mines[a][max].setText("" + count(a, max));
            zero[max] = false;
        }
    }

    public void move(boolean[] zero, boolean[] tempor) {
        for (int i = 0; i < zero.length; i++)
            zero[i] = tempor[i];
    }

    public int how_many_true(boolean[] zero) {
        int total = 0;
        for (boolean unit : zero)
            if (unit)
                total++;
        return total;
    }

    public void blow_up(int a, int b, bloc[][] mines) {
        if (mines[a][b].getflag() != 1)
            mines[a][b].clicked();
        if (a - 1 >= 0 && mines[a - 1][b].isEnabled() && mines[a][b].getnum() == 0)
            blow_up(a - 1, b, mines);
        if (a + 1 < length && mines[a + 1][b].isEnabled() && mines[a][b].getnum() == 0)
            blow_up(a + 1, b, mines);
        if (b - 1 >= 0 && mines[a][b - 1].isEnabled() && mines[a][b].getnum() == 0)
            blow_up(a, b - 1, mines);
        if (b + 1 < height && mines[a][b + 1].isEnabled() && mines[a][b].getnum() == 0)
            blow_up(a, b + 1, mines);

        if (a - 1 >= 0 && b - 1 >= 0 && mines[a - 1][b - 1].isEnabled() && mines[a][b].getnum() == 0)
            blow_up(a - 1, b - 1, mines);
        if (a + 1 < length && b - 1 >= 0 && mines[a + 1][b - 1].isEnabled() && mines[a][b].getnum() == 0)
            blow_up(a + 1, b - 1, mines);
        if (a - 1 >= 0 && b + 1 < height && mines[a - 1][b + 1].isEnabled() && mines[a][b].getnum() == 0)
            blow_up(a - 1, b + 1, mines);
        if (a + 1 < length && b + 1 < height && mines[a + 1][b + 1].isEnabled() && mines[a][b].getnum() == 0)
            blow_up(a + 1, b + 1, mines);
        return;
    }

    private class bloc extends JButton// implements MouseListener
    {
        private int number;
        private int hang;
        private int lie;
        boolean click;
        boolean press;
        int flag;

        public bloc(int i, int j) {
            number = 0;
            press = false;
            click = false;
            flag = 0;
            hang = i;
            lie = j;
            this.addMouseListener(mh);
            this.setFont(f3);
            this.setMargin(new Insets(0, 0, 0, 0));
        }

        public void setpress(boolean b) {
            press = b;
        }

        public boolean getpress() {
            return press;
        }

        public void setnum(int m) {
            number = m;
        }

        public int getflag() {
            return flag;
        }

        public void setflag(int f) {
            flag = f;
        }

        public int gethang() {
            return hang;
        }

        public int getlie() {
            return lie;
        }

        public int getnum() {
            return number;
        }

        public boolean been_clicked() {
            return click;
        }

        public void clicked() {
            click = true;
            if (getnum() != -1)
                setEnabled(false);
            if (getnum() != 0 && getnum() != -1)
                setText("" + getnum());
            else if (getnum() == 0)
                setText("");
        }
    }

    private class MH extends MouseAdapter {
        private boolean pressed = false;

        public void mouseClicked(MouseEvent e) {
            if (!lose) {
                int a = ((bloc) e.getSource()).gethang();
                int b = ((bloc) e.getSource()).getlie();
                if (e.getModifiersEx() != (e.BUTTON1_DOWN_MASK + e.BUTTON3_DOWN_MASK)) {
                    if (e.getButton() == MouseEvent.BUTTON1 && !pressed) {
                        trigger(a, b);
                    } else if (e.getButton() == MouseEvent.BUTTON3 && !pressed) {
                        if (!mines[a][b].isEnabled())
                            return;
                        if (mines[a][b].getflag() == 0) {
                            mines[a][b].setflag(1);
                            mines[a][b].setText("F");
                            int now = Integer.valueOf(mine.getText()).intValue() - 1;
                            mine.setText(now + "");
                        } else if (mines[a][b].getflag() == 1) {
                            mines[a][b].setflag(2);
                            mines[a][b].setText("?");
                            int now = Integer.valueOf(mine.getText()).intValue() + 1;
                            mine.setText(now + "");
                        } else if (mines[a][b].getflag() == 2) {
                            mines[a][b].setflag(0);
                            mines[a][b].setText("");
                        }
                    }
                }
            }
        }

        public void mousePressed(MouseEvent e) {
            if (!lose) {
                int a = ((bloc) e.getSource()).gethang();
                int b = ((bloc) e.getSource()).getlie();
                if (e.getModifiersEx() == (e.BUTTON1_DOWN_MASK + e.BUTTON3_DOWN_MASK)) {
                    for (int i = a - 1; i <= a + 1; i++)
                        for (int j = b - 1; j <= b + 1; j++)
                            if (i >= 0 && i < length && j >= 0 && j < height && mines[i][j].isEnabled() && mines[i][j].getflag() != 1) {
                                mines[i][j].setEnabled(false);
                                mines[i][j].setpress(true);
                            }
                    pressed = true;
                    return;
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (!lose) {
                int a = ((bloc) e.getSource()).gethang();
                int b = ((bloc) e.getSource()).getlie();
                int total = 0;
                for (int i = a - 1; i <= a + 1; i++)
                    for (int j = b - 1; j <= b + 1; j++) {
                        if (i >= 0 && i < length && j >= 0 && j < height) {
                            if (mines[i][j].getflag() != 1 && mines[i][j].getpress()) {
                                mines[i][j].setEnabled(true);
                                mines[i][j].setpress(false);
                            } else if (mines[i][j].getflag() == 1)
                                total++;
                        }
                    }
                if (total == mines[a][b].getnum() && !mines[a][b].isEnabled())
                    for (int i = a - 1; i <= a + 1; i++)
                        for (int j = b - 1; j <= b + 1; j++)
                            if (i >= 0 && i < length && j >= 0 && j < height && mines[i][j].getflag() != 1)
                                trigger(i, j);
                pressed = false;
            }
        }
    }

    private class timer implements Runnable {
        int now_time;

        public timer() {
            now_time = Integer.valueOf(time.getText()).intValue();
        }

        public void run() {
            try {
                for (int i = 1; i <= 999; i++) {
                    if (lose)
                        break;
                    time.setText("" + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
            }
        }
    }
}

