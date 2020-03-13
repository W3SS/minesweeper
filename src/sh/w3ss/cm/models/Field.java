package sh.w3ss.cm.models;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final int line;
    private final int column;

    private boolean openField = false;
    private boolean minedField = false;
    private boolean markedField = false;

    private static List<Field> neighborhood = new ArrayList<>();
    private List<FieldObserver> observers = new ArrayList<>();

    public Field(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public void registerObserver(FieldObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(FieldEvent event) {
        observers.stream()
                .forEach(o -> o.EventOccurred(this, event));
    }

    public boolean addNeighborhood(Field possibleNeighbor) {
        boolean differentLine = line != possibleNeighbor.line;
        boolean differentColumn = column != possibleNeighbor.column;
        boolean diagonalNeighbor = differentLine && differentColumn;

        int deltaLine = Math.abs(line - possibleNeighbor.line);
        int deltaColumn = Math.abs(column - possibleNeighbor.column);
        int deltaField = deltaLine + deltaColumn;

        if (deltaField == 1) {
            neighborhood.add(possibleNeighbor);
            return true;
        } else if (deltaField == 2 && diagonalNeighbor) {
            neighborhood.add(possibleNeighbor);
            return true;
        } else {
            return false;
        }
    }

    public void changeMark() {
        if(!openField) {
            markedField = !markedField;

            if(markedField) {
                notifyObservers(FieldEvent.MARK);
            } else {
                notifyObservers(FieldEvent.UNMARK);
            }
        }
    }

    public boolean openning() {

        if(!openField && !markedField) {
            if(minedField) {
                notifyObservers(FieldEvent.EXPLODE);
                return true;
            }

            setOpenField(true);

            if(secureNeighborhood()) {
                neighborhood.forEach(Field::openning);
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean secureNeighborhood() {
        return neighborhood.stream().noneMatch(neighbor -> neighbor.minedField);
    }

    public void setMinedField() {
        minedField = true;
    }

    public void setOpenField(boolean openField) {
        this.openField = openField;

        if (openField) {
            notifyObservers(FieldEvent.OPEN);
        }
    }

    public boolean isMinedField() {
        return minedField;
    }

    public boolean isMarkedField() {
        return markedField;
    }

    public boolean isOpened() {
        return openField;
    }

    public boolean isClosed() {
        return !isOpened();
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public boolean reachedGoal() {
        boolean unhiddenAll = !minedField && openField;
        boolean protectAll = minedField && markedField;
        return unhiddenAll || protectAll;
    }

    public int minesOnNeighborhood() {
        return (int) neighborhood.stream().filter(n -> n.minedField).count();
    }

    void restart() {
        minedField = false;
        openField = false;
        markedField = false;
        notifyObservers(FieldEvent.RESTART);
    }
}

