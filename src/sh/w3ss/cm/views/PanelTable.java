package sh.w3ss.cm.views;

import sh.w3ss.cm.models.Table;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class PanelTable extends JPanel {
    public PanelTable(Table table) {
        setLayout(new GridLayout(
                table.getLines(),
                table.getColumns()
        ));

        table.forEachField(c -> add(new ButtonFieldView(c)));
        table.registryObserver(e -> {
            SwingUtilities.invokeLater(() -> {
                    if(e.isWon()) {
                        JOptionPane.showMessageDialog(
                                this, "You Win :)");
                    } else {
                        JOptionPane.showMessageDialog(
                                this, "You Lose :(");
                    }

                    table.restart();
            });
        });
    }
}
