package operations;

import java.util.ArrayList;

public class LineDivision {

    private final ArrayList<String> words = new ArrayList<>();
    public ArrayList <String> getWords(String line){
        //Деление строки на слова
        int indexFrom = 0;
        int indexTo = indexTo = line.indexOf(';', indexFrom);

        addSubstring(line.substring(indexFrom, indexTo));

        while (indexTo < line.length() - 1) {
            indexFrom = indexTo + 1;
            String substringToEnd = line.substring(indexFrom);
            if (substringToEnd.contains(";")) {
                indexTo = line.indexOf(';', indexFrom);
                addSubstring(line.substring(indexFrom, indexTo));
            } else {
                addSubstring(substringToEnd);
                break;
            }
        }
        if (line.charAt(line.length() - 1) == ';') {
            words.add(null);
        }
        return words;
    }
    //Добавление слова либо null в случае его отсутствия
    private void addSubstring(String str){
        if (str.equals("") || str.equals("\"\"")) {
            words.add(null);
        } else {
            words.add(str);
        }
    }
}
