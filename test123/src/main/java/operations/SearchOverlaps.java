package operations;

import comparator.IndexComparator;
import comparator.ValueComparator;
import pojo.Unit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class SearchOverlaps {

    //Список совпадений по значению
    private final ArrayList<Unit> matchingValues = new ArrayList<>();
    public ArrayList<Unit> getMatchingValues() {
        return matchingValues;
    }
    //Поиск совпадений по значению
    public SearchOverlaps(ArrayList<Unit> column, ArrayList<String> listOfStrings) {

        column.sort(new ValueComparator());

        for (int i = 0; i < column.size() - 1; i++) {
            if (column.get(i).getValue() != 0.0) {
                //Пропускает одинаковые исходные строки, оставляя одну
                while (listOfStrings.get(column.get(i).getIndex()).equals(listOfStrings.get(column.get(i + 1).getIndex()))) {
                    i++;
                }
                //добавляет все строки с совпадениями по значению
                if (column.get(i).getValue() == (column.get(i + 1).getValue())) {
                    matchingValues.add(column.get(i));
                    while (column.get(i).getValue() == (column.get(i + 1).getValue())) {
                        i++;
                        matchingValues.add(column.get(i));
                        if (i == column.size() - 1) break;
                    }
                }
            }
        }
        this.column = column;
    }
    private ArrayList<Unit> column;

    //Множество индексов строк с более 2 совпадениями по элементам
    private final HashSet<Integer> matchingIndexes = new HashSet<>();
    public HashSet<Integer> getMatchingIndexes() {
        //Поиск совпадений индексов
        column = new ArrayList<>(matchingValues);
        column.sort(new IndexComparator());

        for (int i = 0; i < column.size() - 1; i++) {
            if (column.get(i).getIndex() == column.get(i + 1).getIndex()) {
                matchingIndexes.add(column.get(i).getIndex());
            }
        }
        ArrayList <Integer> indexes = new ArrayList<>(matchingIndexes);
        indexes.sort(Integer::compare);
        return matchingIndexes;
    }
}
