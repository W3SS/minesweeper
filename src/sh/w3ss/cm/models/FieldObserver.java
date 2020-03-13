package sh.w3ss.cm.models;

@FunctionalInterface
public interface FieldObserver {
    public void EventOccurred(Field field, FieldEvent event);
}
