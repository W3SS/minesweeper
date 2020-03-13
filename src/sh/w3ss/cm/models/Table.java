package sh.w3ss.cm.models;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Table implements FieldObserver {

    private final int lines;
    private final int columns;
    private final int mines;

    private final List<Field> fields = new ArrayList<>();
    private final List<Consumer<ResultEvent>> observers = new ArrayList<>();

    public Table(int lines, int columns, int mines) {
        this.lines = lines;
        this.columns = columns;
        this.mines = mines;

        generateField();
        associateNeighborhood();
        sortingMines();
    }

    public void forEachField(Consumer<Field> function) {
        fields.forEach(function);
    }

    public void registryObserver(Consumer<ResultEvent> observer) {
        observers.add(observer);
    }

    private void notifyObservers(boolean result) {
        observers.stream().forEach(o -> o.accept(new ResultEvent(result)));
    }

    public void setOpen(int line, int column) {
        fields.parallelStream().filter(c -> c.getLine() == line && c.getColumn() == column).findFirst()
                .ifPresent(c -> c.setOpenField(true));
    }

    public void changeMarked(int line, int column) {
        fields.parallelStream().filter(c -> c.getLine() == line && c.getColumn() == column).findFirst()
                .ifPresent(c -> c.changeMark());
    }

    private void generateField() {
        for (int line = 0; line < lines; line++) {
            for (int column = 0; column < columns; column++) {
                Field field = new Field(line, column);
                field.registerObserver(this);
                fields.add(field);
            }
        }
    }

    private void associateNeighborhood() {
        for (Field c1 : fields) {
            for (Field c2 : fields) {
                c1.addNeighborhood(c2);
            }
        }
    }

    private void sortingMines() {
        long armedMines = 0;
        Predicate<Field> mined = c -> c.isMinedField();

        do {
            int random = (int) (Math.random() * fields.size());
            fields.get(random).setMinedField();
            armedMines = fields.stream().filter(mined).count();
        } while (armedMines < mines);
    }

    public boolean reachedGoal() {
        return fields.stream().allMatch(c -> c.reachedGoal());
    }

    public void restart() {
        fields.stream().forEach(c -> c.restart());
        sortingMines();
    }

    public int getLines() {
        return lines;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    public void EventOccurred(Field field, FieldEvent event) {
        if (event == FieldEvent.EXPLODE) {
            showMines();
            notifyObservers(false);
        } else if (field.reachedGoal()) {
            notifyObservers(true);
        }
    }

    private void showMines() {
        fields.stream()
                .filter(c -> c.isMinedField())
                .filter(c -> !c.isMarkedField())
                .forEach(c -> c.setOpenField(true));
    }
}
