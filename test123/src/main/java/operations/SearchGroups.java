package operations;

import comparator.ValueComparator;
import pojo.Unit;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchGroups {

    //Группы отобранных строк
    private final ArrayList <ArrayList<String>> groups;
    public ArrayList<ArrayList<String>> getGroups() {
        return groups;
    }

    public SearchGroups(ArrayList <Unit> searchedColumn, ArrayList <String> listOfStrings, ArrayList<Integer> matchingIndexes){

        //Класс поиска простых групп (имеющих совпадения лишь по значениям в списке отсортированных значений)
        class FindSimpleGroups {
            private final ArrayList <ArrayList<String>> groups = new ArrayList<>();
            public ArrayList<ArrayList<String>> getGroups() {
                return groups;
            }
            public FindSimpleGroups(ArrayList <Unit> column){
                //Множество индексов строк для исключения дублирования
                HashSet <Integer> groupIndexes = new HashSet<>();
                HashSet <String> group = new HashSet<>();
                //Запись первого элемента первой группы
                group.add(listOfStrings.get(column.get(0).getIndex()));
                groupIndexes.add(column.get(0).getIndex());

                for (int i = 0; i < column.size() - 1; i++) {
                    //Если текущий элемент столбца равен следующему
                    if (column.get(i).getValue() == column.get(i + 1).getValue()){
                        //Если в множестве индексов группы нет индекса следующего элемента
                        if (!groupIndexes.contains(column.get(i + 1).getIndex())) {
                            //запись строки с индексом следующего элемента столбца в группу
                            group.add(listOfStrings.get(column.get(i + 1).getIndex()));
                            groupIndexes.add(column.get(i + 1).getIndex());
                        }
                    } else {
                        //Если в множестве индексов группы отсутствует индекс следующего элемента
                        if (!groupIndexes.contains(column.get(i + 1).getIndex())) {
                            //Запись группы и инициализация следующей
                            groups.add(new ArrayList<>(group));
                            group = new HashSet<>();
                            groupIndexes = new HashSet<>();
                            compileAdvancedGroups(groups);

                        }//запись строки с индексом следующего элемента столбца в группу
                        group.add(listOfStrings.get(column.get(i + 1).getIndex()));
                        groupIndexes.add(column.get(i + 1).getIndex());
                    }
                }
                //Запись последней группы
                groups.add(new ArrayList<>(group));
                compileAdvancedGroups(groups);
            }

        }
        //Предварительно собранные группы
        groups = new FindSimpleGroups(searchedColumn).getGroups();
    }
    //Выявление мостовых совпадений полученной группы с предыдущими и "склеивание" в случае находки
    private void compileAdvancedGroups(ArrayList <ArrayList<String>> groups){

        ArrayList<String> currentGroup = groups.get(groups.size() - 1);
        for (int i = 0; i < groups.size() - 1; ) {
            ArrayList<String> comparingGroup = groups.get(i);
            boolean haveOverlaps = false;
            for (String s : currentGroup) {
                for (String value : comparingGroup) {
                    if (s.equals(value)) {
                        haveOverlaps = true;
                        break;
                    }
                    if (haveOverlaps)
                        break;
                }
            }
            if (haveOverlaps) {

                HashSet <String> maybeGroup = new HashSet<>(currentGroup);
                maybeGroup.addAll(comparingGroup);
                groups.remove(i);
                groups.set(groups.size() - 1, new ArrayList<>(maybeGroup));
                currentGroup = groups.get(groups.size() - 1);
            } else i++;
        }
    }
}
