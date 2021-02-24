package comparator;

import model.Unit;

import java.util.Comparator;

//Компаратор для реализации сортировки по значению объектов Unit
public class ValueComparator implements Comparator<Unit> {

    @Override
    public int compare(Unit o1, Unit o2) {
        return Integer.compare(o1.getValue(), o2.getValue());
    }
}
