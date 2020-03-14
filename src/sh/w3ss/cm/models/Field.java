package sh.w3ss.cm.models;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final int line;
    private final int column;

    private boolean opened = false;
    private boolean mined = false;
    private boolean marked = false;

    private List<Field> neighborhood = new ArrayList<>();
    private List<FieldObserver> observers = new ArrayList<>();

    public Field(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public void registryObserver(FieldObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(FieldEvent event) {
        observers.stream().forEach(o -> {
                o.eventOccurred(this, event);});
    }

    boolean addNeighborhood(Field neighbor) {
            boolean differentLine = line != neighbor.line;
            boolean differentColumn = column != neighbor.column;
            boolean diagonalNeighbor = differentLine && differentColumn;

            int deltaLine = Math.abs(line - neighbor.line);
            int deltaColumn = Math.abs(column - neighbor.column);
            int deltaField = deltaLine + deltaColumn;

            if (deltaField == 1 && !diagonalNeighbor) {
                neighborhood.add(neighbor);
                return true;
            } else if (deltaField == 2 && diagonalNeighbor) {
                neighborhood.add(neighbor);
                return true;
            } else {
                return false;
            }
    }

    public void changeMark() {
            if(!opened) {
                marked = !marked;

                if(marked) {
                    notifyObservers(FieldEvent.MARK);
                } else {
                    notifyObservers(FieldEvent.UNMARK);
                }
            }
    }

    public boolean openning() {
        if(!opened && !marked) {
                if(mined) {
                    notifyObservers(FieldEvent.EXPLODE);
                    return true;
                }

                setOpen(true);

                if(secureNeighborhood()) {
                    neighborhood.forEach(v -> v.openning());
                }

                return true;
        } else{
            return false;
        }
    }

    public boolean secureNeighborhood() {
        return neighborhood.stream().noneMatch(n -> n.mined);
    }

    void setMine() {
        mined = true;
    }

    void setOpen(boolean opened) {
        this.opened = opened;
        if (opened) notifyObservers(FieldEvent.OPEN);
    }

    public boolean isMined() {
        return mined;
    }

    public boolean isMarked() {
        return marked;
    }

    public boolean isOpened() {
        return opened;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    boolean reachedGoal() {
        boolean unhiddenAll = !mined && opened;
        boolean protectAll = mined && marked;
        return unhiddenAll || protectAll;
    }

    public int minesOnNeighborhood() {
        return (int) neighborhood.stream().filter(n -> n.mined).count();
    }

    void restart() {
        mined = false;
        opened = false;
        marked = false;
        notifyObservers(FieldEvent.RESTART);
    }
}

