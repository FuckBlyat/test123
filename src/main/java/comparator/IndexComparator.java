package comparator;

import model.Unit;

import java.util.Comparator;

//Компаратор для реализации сортировки по индексу объектов Unit
public class IndexComparator implements Comparator<Unit> {

    @Override
    public int compare(Unit o1, Unit o2) {
        return Integer.compare(o1.getIndex(), o2.getIndex());
    }
}
