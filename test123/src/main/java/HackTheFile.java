import comparator.IndexComparator;
import operations.Check;
import operations.LineToColumn;
import operations.SearchGroups;
import operations.SearchOverlaps;
import pojo.Unit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HackTheFile {
    //Для удобства реализации принял решение поделить список строк на отдельные числа и занести их в столбец состоящий
    // из объектов, хранящих в себе помимо значений индексы исходных строк
    private ArrayList<Unit> bigColumn;
    //Список для хранения корректных непустых строк из файла
    private ArrayList <String> listOfStrings;
    //Группы отобранных строк
    private ArrayList <ArrayList<String>> groups = new ArrayList<>();
    public ArrayList<ArrayList<String>> getGroups() {
        return groups;
    }
    public ArrayList<String> getGroup(int index) {
        return groups.get(index);
    }

    public HackTheFile(String readingFileName) {

        //запись из файла
        fIleToColumns(readingFileName);
        //Поиск и сортировка совпадений по значению и индексам
        //Другие элементы исходного столбца больше не понадобятся
        SearchOverlaps searchedOverlaps = new SearchOverlaps(bigColumn, listOfStrings);
        bigColumn = searchedOverlaps.getMatchingValues();

        //Множество индексов строк с более 2 совпадениями по элементам
        HashSet<Integer> matchingIndexes = searchedOverlaps.getMatchingIndexes();
        //Поиск групп
        groups = (new SearchGroups(bigColumn, listOfStrings, new ArrayList<>(matchingIndexes)).getGroups());
        //Сортировка полученных групп
        groups.sort(Comparator.comparing(ArrayList::size));
        Collections.reverse(groups);
    }
    //Метод считывает файл, записывает корректные строки в listOfStrings и распределяет их значения с индексами по "столбцам"
    private void fIleToColumns(String fileAddress){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileAddress))) {

            bigColumn = new ArrayList<>();
            //Множество для предварительной записи строк, гарантирующее отсутствие повторяющихся строк
            HashSet<String> setOfStrings = new HashSet<>();
            listOfStrings = new ArrayList<>();

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                //Проверка, форматирование и запись строк
                bigColumn.addAll(new LineToColumn(line, setOfStrings, listOfStrings).getColumn());
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}