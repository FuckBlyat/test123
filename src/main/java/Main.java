
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Date;

public class Main {

    public static void main (String [] args) {
        //Отсчёт времени выполнения программы
        double postTime = (double) (new Date()).getTime() / 1000;

        //Имя файла из задачи
        String readingFileName = "C:\\Users\\illyc\\Desktop\\test\\lng.csv";

        //Адрес для записи полученного результата
        String writingFileName = "C:\\Users\\illyc\\Desktop\\test\\result.txt";

        //Находит группы
        HackTheFile result = new HackTheFile(readingFileName);

        //Запись полученного результата
        try(FileWriter writer = new FileWriter(writingFileName))
        {
            writer.write("Total groups found: " + result.getGroups().size() + '\n');
            for (int i = 0; i < result.getGroups().size(); i++) {

                writer.write("Group " + (i + 1) + '\n');
                for (int j = 0; j < result.getGroup(i).size(); j++) {

                    writer.write(result.getGroup(i).get(j) + '\n');
                }
            }
            com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
            //Вывод затраченного времени и поверхностной информации о системе
            writer.write("Completed in " + (((double) (new Date()).getTime() / 1000) - postTime) +
                    " seconds on PC with " + os.getAvailableProcessors() + " cores and " + os.getTotalMemorySize()/1000000000 + "gb RAM");
            writer.flush();
        }
        catch(IOException ex){

            System.err.println(ex.getMessage());
        }
    }
}

