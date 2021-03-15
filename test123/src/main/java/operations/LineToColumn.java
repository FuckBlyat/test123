package operations;

import pojo.Unit;

import java.util.ArrayList;
import java.util.HashSet;

public class LineToColumn {

    private final ArrayList <Unit> column  = new ArrayList<>();
    public ArrayList <Unit> getColumn() {
        return column;
    }
    public LineToColumn(String line, HashSet<String> setOfStrings, ArrayList <String> listOfStrings) {
        //проверка и перевод строки в список будущих чисел
        ArrayList<String> checkedList = new Check().getCheckedList(line);
        if (checkedList != null){
            //Проверка содержания строки в множестве. Если при добавлении новой строки его размер не изменится, то значит, что такая строка уже была добавлена в множество
            int oldSetSize = setOfStrings.size();
            setOfStrings.add(line);
            if (setOfStrings.size() != oldSetSize) {
                //Занесение корректных данных в listOfStrings
                listOfStrings.add(line);
                //Форматирование и занесение в столбцы
                int index = listOfStrings.size() - 1;
                for (int i = 0; i < 3; i++) {
                    String word = checkedList.get(i);
                    column.add(new Unit(index, word));
                }
            }
        }
    }
}
