import sh.w3ss.cm.models.Field;
import sh.w3ss.cm.exceptions.ExplosionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
    private Field field ;

    @BeforeEach
    public void initiateField() {
        field = new Field(3,3);
    }

    @Test
    public void leftNeighborhoodDistanceTest() {
        Field neighbor = new Field(3,2);
        boolean result = field.addNeighborhood(neighbor);
        assertTrue(result);
    }

    @Test
    public void rightNeighborhoodDistanceTest() {
        Field neighbor = new Field(3, 4);
        boolean result = field.addNeighborhood(neighbor);
        assertTrue(result);
    }

    @Test
    public void upNeighborhoodDistanceTest() {
        Field neighbor = new Field(2, 3);
        boolean result = field.addNeighborhood(neighbor);
        assertTrue(result);
    }

    @Test
    public void downNeighborhoodDistanceTest() {
        Field neighbor = new Field(4, 3);
        boolean result = field.addNeighborhood(neighbor);
        assertTrue(result);
    }

    @Test
    public void twoFieldsNeighborhoodDistanceTest() {
        Field neighbor = new Field(2, 2);
        boolean result = field.addNeighborhood(neighbor);
        assertTrue(result);
    }


    @Test
    public void nonNeighborhoodTest() {
        Field neighbor = new Field(1, 1);
        boolean result = field.addNeighborhood(neighbor);
        assertFalse(result);
    }

    @Test
    public void defaultValueMarkedAttributeTest() {
        assertFalse(field.isMarkedField());
    }

    @Test
    public void changeMarkTest() {
        field.changeMark();
        assertTrue(field.isMarkedField());
    }

    @Test
    public void changeMarkInTwoCallsTest() {
        field.changeMark();
        field.changeMark();
        assertTrue(field.isMarkedField());
    }

    @Test
    public void openNotMinedNotMarkedFieldTest() {
        assertTrue(field.openning());
    }

    @Test
    void openNonMinedMarked() {
        field.changeMark();
        assertFalse(field.openning());
    }

    @Test
    public void openMinedMarkedFieldTest() {
        field.changeMark();
        field.setMinedField();
        assertFalse(field.openning());
    }

    @Test
    public void openMinedNonMarkedTest() {
        field.setMinedField();

        assertThrows(ExplosionException.class, () -> {
            field.openning();
        });

    }

    @Test
    public void openOneNeighborsTest() {

        Field field11 = new Field(1, 1);
        Field field22 = new Field(2, 2);
        field22.addNeighborhood(field11);

        field.addNeighborhood(field22);
        field.openning();

        assertTrue(field22.isOpened() && field11.isOpened());
    }

    @Test
    public void openWithTwoNeighborsTest() {

        Field field11 = new Field(1, 1);
        Field field12 = new Field(1, 1);
        field12.setMinedField();

        Field field22 = new Field(2, 2);
        field22.addNeighborhood(field12);
        field22.addNeighborhood(field11);

        field.addNeighborhood(field22);
        field.openning();

        assertTrue(field22.isOpened() && field11.isClosed());
    }
}
