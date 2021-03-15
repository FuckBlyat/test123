package operations;

import java.util.ArrayList;
import java.util.Arrays;

public class Check{

    public ArrayList<String> words = new ArrayList<>();

    //Возвращает список проверенных и подготовленных к форматированию в числа элементов (слов) входной строки
    public ArrayList <String> getCheckedList(String line) {
        //Первичная проверка строки
        StringBuilder comparing = new StringBuilder();
        for (int i = 0, j = 0; i < line.length(); i++) {
            if (line.charAt(i) == ';') {
                comparing.setLength(comparing.length() + 1);
                comparing.setCharAt(j, line.charAt(i));
                j++;
            }
        }
        if (comparing.toString().equals(";;")) {
            //Деление строки на слова
            return new LineDivision().getWords(line);
        }
        else return null;
    }
}
