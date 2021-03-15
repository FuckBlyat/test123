package pojo;

//Класс с индексами исходных строк и значениями. Из его объектов будут состоять "столбцы".

import java.util.ArrayList;

public class Unit {

    private final int index;
    private final double value;

    public Unit(int index, String word) {
        this.index = index;
        if (word != null)
            //Форматирование и запись значения
            value = Double.parseDouble(word.substring(1, word.length() - 1));
        else value = 0.0;
    }
    public int getIndex() {
        return index;
    }
    public double getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "{" + index + ", " + value + "}";
    }
}
