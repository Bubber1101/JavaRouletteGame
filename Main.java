import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import static javax.swing.SwingUtilities.isRightMouseButton;

public class Main extends JFrame {
    Glass g;
    int coins = 30;
    Integer betVal;
    Integer betRed;
    JTextField drawnNumText = new JTextField(9);
    JTextField isWinText = new JTextField(8);
    JTextArea history = new JTextArea(5, 10);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new Main());
    }

    public Main() {


        JPanel table = new JPanel();

        JTable t = new JTable(new RTableModel());
        table.add(t);
        t.setDefaultRenderer(Object.class, new RTableRenderer());
        t.setRowHeight(100);
        t.addMouseListener(new RMouseListener());

        JLabel coinCount = new JLabel("Coins = " + coins);
        Timer timer = new Timer(100, e -> {
            coinCount.setText("Coins = " + coins);
            if (coins <= 0) {
                JOptionPane.showMessageDialog(this, "<html>You don't have enough money to place this bet " +
                        "<br /> Sorry, you lost the game<br /> Casino always wins eh?</html>");

                System.exit(0);
            }
        });
        timer.start();


        Guzik draw = new Guzik();

        JPanel top = new JPanel();
        top.setLayout(new FlowLayout());
/*        JTextField drawnNumText = new JTextField(1);
        JTextField drawnColText = new JTextField(1);
        JTextField isWinText = new JTextField(1);*/
        top.add(drawnNumText);
        top.add(isWinText);
        history.setLineWrap(true);
        add(top, BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
        add(draw, BorderLayout.SOUTH);
        add(coinCount, BorderLayout.EAST);
        add(history, BorderLayout.WEST);



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        setVisible(true);
    }

    private class RTableModel extends AbstractTableModel {
        int nums[][] = {{0, 1}, {2, 3}, {4, 5}, {6, 7}, {8, 9}};

        @Override
        public int getRowCount() {
            return 5;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return nums[rowIndex][columnIndex];
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }


    }

    private class RTableRenderer extends JLabel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int x = (Integer) table.getValueAt(row, column);
            setOpaque(true);
            setBackground(Color.BLUE);

            if (x % 2 == 0) {
                setBackground(Color.GREEN);
            } else {
                setBackground(Color.RED);
            }
            setHorizontalAlignment(JLabel.CENTER);
            setText("" + x);
            setFont(new Font("monospaced", Font.PLAIN, 50));
            return this; //////
        }
    }

    public class RMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            JTable tab = (JTable) e.getSource();
            int r = tab.rowAtPoint(e.getPoint());
            int c = tab.columnAtPoint(e.getPoint());
            int val = (Integer) tab.getValueAt(r, c);
            boolean red = true;
            if (val % 2 == 0) red = false;
            if (isRightMouseButton(e) && betRed == null) {
                if (red == true) {
                    betRed = 1;
                } else betRed = 0;
                coins -= 2;
            } else if (!isRightMouseButton(e) && betVal == null) {
                betVal = val;
                coins -= 5;
            }
            System.out.println(betRed);
            System.out.println(betVal);
        }
    }

    private class Guzik extends JButton implements ActionListener {
        Random rand = new Random();
        int won;
        Integer wonRed;
        int coinsbefore;

        public Guzik() {
            setText("Roll the Roulette!");
            addActionListener(this::actionPerformed);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (betVal != null || betRed != null) {
                coinsbefore = coins;
                won = rand.nextInt(10);
                drawnNumText.setText("Rolled: " + won);
                if (won % 2 == 0) wonRed = 0;
                else wonRed = 1;

                if (betRed != null && betRed == wonRed) {
                    coins += 4;
                }
                if (betVal != null && betVal == won) {
                    coins += 10;
                }
                if (coinsbefore == coins) {
                    isWinText.setText("YOU LOSE");
                    addToHis(won, true);
                } else {
                    isWinText.setText("YOU WIN");
                    addToHis(won, false);

                    g = new Glass();
                    setGlassPane(g);
                    getGlassPane().setVisible(true);
                    ((JPanel)getGlassPane()).setOpaque(false);


                }
            } else JOptionPane.showMessageDialog(this, "Place your bet first!");
            betRed = null;
            betVal = null;
        }
    }

    public void addToHis(int i, boolean result) {
        String s = history.getText();
        String[] lines = s.split("\\n");
        history.setText(null);
        if (result == true) history.append(i + "  LOST\n");
        else history.append(i + "  WON \n");
        for (int x = 0; x < 4 && x < lines.length; x++) {
            history.append(lines[x] + "\n");
        }
    }
}

