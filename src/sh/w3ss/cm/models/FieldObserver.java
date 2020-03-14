package sh.w3ss.cm.models;

import sh.w3ss.cm.models.Field;
import sh.w3ss.cm.models.FieldEvent;

@FunctionalInterface
public interface FieldObserver {
    public void eventOccurred(Field field, FieldEvent event);
}
