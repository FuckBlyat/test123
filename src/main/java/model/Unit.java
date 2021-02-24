package model;

//Класс с индексами исходных строк и значениями. Из его объектов будут состоять "столбцы"
public class Unit {

    private final int index;
    private final int value;

    public Unit(int index, int value){
        this.index = index;
        this.value = value;
    }
    public int getIndex() {
        return index;
    }
    public int getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "{" + index + ", " + value + "}";
    }
}
