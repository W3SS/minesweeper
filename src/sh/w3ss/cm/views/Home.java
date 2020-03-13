package sh.w3ss.cm.views;

import sh.w3ss.cm.models.Table;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Home extends JFrame {

    public Home() {
        Table tabletop = new Table(16, 30, 50);
        add(new Panel(tabletop));

        setTitle("Minesweeper");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Home();
    }
}
