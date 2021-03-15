package comparator;

import pojo.Unit;

import java.util.Comparator;
//Компаратор для реализации сортировки по значению объектов Unit со значениями типа Double
public class ValueComparator implements Comparator<Unit> {

    @Override
    public int compare(Unit o1, Unit o2) {
        return Double.compare(o1.getValue(), o2.getValue());
    }
}
