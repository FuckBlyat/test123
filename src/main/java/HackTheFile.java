import comparator.IndexComparator;
import comparator.ValueComparator;
import model.Unit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HackTheFile {
    //Для удобства реализации принял решение поделить список строк на 3 списка-"столбца" из объектов Unit, содержащих индексы исходных строк и
    //значения - отформатированные определённым способом в int числа
    private ArrayList <Unit> column0;
    private ArrayList <Unit> column1;
    private ArrayList <Unit> column2;

    //Список для хранения корректных непустых строк из файла
    private ArrayList <String> listOfStrings;

    //список элементов с совпадениями по индексу
    private final ArrayList <Integer> matchingIndexes;

    //Группы отобранных строк
    private final ArrayList <ArrayList<String>> groups = new ArrayList<>();

    public ArrayList<ArrayList<String>> getGroups() {
        return groups;
    }
    public ArrayList<String> getGroup(int index) {
        return groups.get(index);
    }

    public HackTheFile(String readingFileName) {

        //запись из файла
        fIleToColumns(readingFileName);

        //Поиск совпадений по значению для каждого "столбца" отдельно.
        //Другие элементы исходных "столбцов" больше не понадобятся
        column0 = findMatchingValues(column0);
        column1 = findMatchingValues(column1);
        column2 = findMatchingValues(column2);

        //Поиск совпадений по индексу, которые означают, что в строках с этими индексами элементы из
        // разных "столбцов" пересекаются и на их основе можно составлять "лесенки" из парных элементов
        matchingIndexes = findMatchingIndexes();
        //Поиск групп с 2 и более совпадениями в разных "столбцах"
        findAdvancedGroups();
        //Поиск простых групп с совпадениями в 1 столбце
        findSimpleGroups(column0);
        findSimpleGroups(column1);
        findSimpleGroups(column2);
        //Сортировка полученных групп
        groups.sort(Comparator.comparing(ArrayList::size));
        Collections.reverse(groups);
    }

    //Метод считывает файл, записывает корректные строки в listOfStrings и распределяет их значения с индексами по "столбцам"
    private void fIleToColumns(String fileAddress){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileAddress))) {

            column0 = new ArrayList<>();
            column1 = new ArrayList<>();
            column2 = new ArrayList<>();
            listOfStrings = new ArrayList<>();

            String line;

            while (bufferedReader.ready()) {
                //Шаблон размещения пунктуционных знаков {"";"";""}.
                String template = "\"\";\"\";\"\"";
                //Присваивает строку из файла, но если она пуста (== пустой шаблон), то присваивает следующую.
                do line = bufferedReader.readLine();
                while (line.equals(template));

                StringBuilder comparing = new StringBuilder();
                //Цикл записывает пунктуционные знаки из исходной строки
                for (int i = 0, j = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '"' || line.charAt(i) == ';') {
                        comparing.setLength(comparing.length() + 1);
                        comparing.setCharAt(j, line.charAt(i));
                        j++;
                    }
                }
                if (!(comparing.toString().equals(template) && line.charAt(0) == '"' && line.charAt(line.length() - 1) == '"')) {
                    continue;
                }
                //проверка порядка и содержания пунктуционных знаков
                if (!((line.charAt(line.indexOf(";", 0) + 1)) == '"' &&
                        (line.charAt(line.indexOf(";", 0) - 1)) == '"' &&
                        (line.charAt(line.indexOf(";", line.indexOf(";")) + 1)) == '"' &&
                        (line.charAt(line.indexOf(";", line.indexOf(";")) - 1)) == '"')) {
                    continue;
                }
                listOfStrings.add(line);
                int index = listOfStrings.size() - 1;

                ArrayList<Integer> row = stringToIntegers(line);
                //Распределение строки по "столбцам"
                column0.add(new Unit(index, row.get(0)));
                column1.add(new Unit(index, row.get(1)));
                column2.add(new Unit(index, row.get(2)));
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    //Метод делит входную строку на подстроки, после чего форматирует их в int так,
    //чтобы полученные числа входили в диапазон от -2147483648 до 2147483647 с целью экономии ресурсов и увеличения производительности.
    //Формат исходных строк позволяет это сделать, обрезая "83", "100000", "200000" в начале слов.
    //Здесь же проводится финальная проверка строк.
    private ArrayList<Integer> stringToIntegers(String line){

        ArrayList<Integer> arrayListWords = new ArrayList<>();
        StringBuilder word;
        int number;
        String [] dummyNumbers = {"83" , "100000", "200000"};

        for (int i = 0, wordIndex = 0, nextIndex; i < line.length(); i++) {

            if(line.charAt(i) == '"'){
                if (line.charAt(i + 1) == '"'){
                    number = 0;
                    i++;
                }
                else {
                    nextIndex = line.indexOf('"', i + 1);
                    word = new StringBuilder(line.substring(i + 1, nextIndex));

                    word.delete(0, dummyNumbers[wordIndex].length());

                    if (word.toString().equals(""))
                        number = 0;
                    else if (Long.parseLong(word.toString()) > Integer.MAX_VALUE) {
                        arrayListWords.add(0);
                        arrayListWords.add(0);
                        arrayListWords.add(0);
                        break;
                    }
                    else number = Integer.parseInt(word.toString());

                    i = nextIndex;
                }
                arrayListWords.add(number);
                wordIndex ++;
            }
            if (wordIndex == dummyNumbers.length)
                wordIndex = 0;
        }
        return arrayListWords;
    }

    //Метод ищет совпадения по значению, убирает одинаковые в listOfStrings строки и возвращает полученные элементы
    private ArrayList <Unit> findMatchingValues(ArrayList <Unit> column){
        //Сортировка по значению
        column.sort(new ValueComparator());

        ArrayList<Unit> searched = new ArrayList<>();

        for (int i = 0; i < column.size() - 1; i++) {

            if (column.get(i).getValue() != 0) {
                //убирает одинаковые исходные строки, оставляя одну
                while (listOfStrings.get(column.get(i).getIndex()).equals(listOfStrings.get(column.get(i + 1).getIndex()))) {
                    column.remove(i + 1);
                }
                //добавляет все строки с совпадениями по значению
                if (column.get(i).getValue() == column.get(i + 1).getValue()) {
                    searched.add(column.get(i));
                    while (column.get(i).getValue() == column.get(i + 1).getValue()) {
                        i++;
                        searched.add(column.get(i));
                        if (i == column.size() - 1) break;
                    }
                }
            }
        }
        return searched;
    }

    //Индексы-исключения строк, относящихся к сложным группам, для удобного поиска простых групп
    private ArrayList <Integer> indexExclusions;
    //Метод ищет в списках совпадающих по значениям элементов совпадающие индексы
    private ArrayList <Integer> findMatchingIndexes(){

        ArrayList <Integer> searched = new ArrayList<>();
        //создание общего столбца для удобства поиска индексов
        ArrayList<Unit> commonColumn = new ArrayList<>();
        commonColumn.addAll(column0);
        commonColumn.addAll(column1);
        commonColumn.addAll(column2);

        indexExclusions = new ArrayList<>();
        //Сортировка по индексу
        commonColumn.sort(new IndexComparator());
        for (int i = 1; i < commonColumn.size(); i++) {
            if(commonColumn.get(i - 1).getIndex() == commonColumn.get(i).getIndex()){
                searched.add(commonColumn.get(i).getIndex());
                indexExclusions.add(commonColumn.get(i).getIndex());
            }
        }
        return searched;
    }

    //Метод собирает сложные группы из строк, имеющих совпадения в более чем одном "столбце", и добавляет их в groups
    private void findAdvancedGroups (){

        ArrayList<String> group = new ArrayList<>();

        //Находит соответствия строк в каждом отдельном столбце и добавляет их в группу
        class findAdvancedGroup{
            findAdvancedGroup(ArrayList<Unit> column, int i){

                for (int j = 0; j < column.size(); j++) {
                    if (column.get(j).getIndex() == matchingIndexes.get(i)){
                        //Записывает элементы с одинаковым значением с индексом выше исходного, если они есть
                        int k = j + 1;
                        while (column.get(j).getValue() == column.get(k).getValue()){
                            group.add(listOfStrings.get(column.get(k).getIndex()));
                            indexExclusions.add(column.get(k).getIndex());
                            k++;
                        }
                        //Записывает элементы с одинаковым значением с индексом ниже исходного, если они есть
                        k = j - 1;
                        while (column.get(j).getValue() == column.get(k).getValue()){
                            group.add(listOfStrings.get(column.get(k).getIndex()));
                            indexExclusions.add(column.get(k).getIndex());
                            k--;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < matchingIndexes.size(); i++) {
            group.add(listOfStrings.get(matchingIndexes.get(i)));
            new findAdvancedGroup(column0, i);
            new findAdvancedGroup(column1, i);
            new findAdvancedGroup(column2, i);

            groups.add(new ArrayList<>(group));
            //овобождает группу для следующей итерации
            group.removeAll(group);
        }
    }
    //Метод собирает простые группы из строк, имеющих совпадения лишь в одном "столбце", и добавляет их в groups
    private void findSimpleGroups (ArrayList <Unit> column){


        for (int i = 0; i < column.size() - 1; i++) {
            if (!indexExclusions.contains(column.get(i).getIndex())){
                ArrayList <String> group = new ArrayList<>();

                group.add(listOfStrings.get(column.get(i).getIndex()));

                while (column.get(i).getValue() == column.get(i + 1).getValue()){
                    group.add(listOfStrings.get(column.get(i + 1).getIndex()));
                    i++;
                    if (i == column.size() - 1) break;
                }
                groups.add(group);
            }
        }
    }
}
