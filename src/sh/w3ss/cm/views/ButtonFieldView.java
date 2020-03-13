package sh.w3ss.cm.views;

import sh.w3ss.cm.models.Field;
import sh.w3ss.cm.models.FieldEvent;
import sh.w3ss.cm.models.FieldObserver;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ButtonFieldView extends JButton implements FieldObserver, MouseListener {
    private final Color BG_DEFAULT = new Color(184, 184, 184);
    private final Color BG_MARK = new Color(8, 179, 247);
    private final Color BG_EXPLODE = new Color(189, 66, 68);
    private final Color TEXT_GREEN = new Color(0, 100, 0);

    private Field field;

    public ButtonFieldView(Field field) {
        setBackground(BG_DEFAULT);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));

        addMouseListener(this);
        field.registerObserver(this);
    }

    @Override
    public void EventOccurred(Field field, FieldEvent event) {
        switch(event) {
            case OPEN:
                applyStyleOpen();
                break;
            case MARK:
                applyStyleMark();
                break;
            case EXPLODE:
                applyStyleExplode();
                break;
            default:
                applyStyleDefault();
        }

        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });
    }

    private void applyStyleDefault() {
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }

    private void applyStyleExplode() {
        setBackground(BG_EXPLODE);
        setForeground(Color.WHITE);
        setText("X");
    }

    private void applyStyleMark() {
        setBackground(BG_MARK);
        setForeground(Color.BLACK);
        setText("M");
    }

    private void applyStyleOpen() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if(field.isMinedField()) {
            setBackground(BG_EXPLODE);
            return;
        }

        setBackground(BG_DEFAULT);

        switch (field.minesOnNeighborhood()) {
            case 1:
                setForeground(TEXT_GREEN);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }

        String value = !field.secureNeighborhood() ?
                field.minesOnNeighborhood() + "" : "";
        setText(value);
    }



    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1) {
            field.openning();
        } else {
            field.changeMark();
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
