/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.csvgenerator;

/**
 *
 * @author GLB-029
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import whitepages.entity.People;
import whitepages.gui.WhitepagesGui;

public class CsvGenerator extends Thread {

    private List<People> peopleList;

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "FIRST NAME,MIDDLE NAME,LAST NAME,AGE,PHONE NUMBER,STREET ADDRESS,CITY,STATE,ZIPCODE";
    String fileName;
    People people;

    public CsvGenerator(String fileName, List<People> peopleList) {
        this.fileName = fileName;
        this.peopleList = peopleList;
    }

    public CsvGenerator(People people, String fileName) {
        this.fileName = fileName;
        this.people = people;
    }

    @Override
    public void run() {
        FileWriter fileWriter = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                fileWriter = new FileWriter(file);
                fileWriter.append(FILE_HEADER);
                fileWriter.append(NEW_LINE_SEPARATOR);

                fileWriter.append(people.getFirstname());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getMiddlename());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getLastname());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getAge());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getLandline());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getStreetAddress());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getCity());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getState());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getZipcode());
                fileWriter.append(NEW_LINE_SEPARATOR);

                fileWriter.flush();
            } else {
                fileWriter = new FileWriter(file, true);
                fileWriter.append(people.getFirstname());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getMiddlename());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getLastname());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getAge());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getLandline());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getStreetAddress());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getCity());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getState());
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append(people.getZipcode());
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.flush();

            }
//            WhitepagesGui.outputText.append("\nFile generated... your file location is " + fileName.replace("\\\\", "\\"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in CsvFileWriter !!!");
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("\nError while flushing/closing fileWriter !!!");
            }
        }
    }
}
