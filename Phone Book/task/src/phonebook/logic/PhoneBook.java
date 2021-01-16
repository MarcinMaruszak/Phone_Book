package phonebook.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class PhoneBook {
    private FilesHandler filesHandler;
    ArrayList<String> contacts;
    ArrayList<String> find;
    Scanner scanner;

    public PhoneBook() {
        filesHandler = new FilesHandler();
        contacts = new ArrayList<>();
        find = new ArrayList<>();
    }


    public void search(String contactsFile, String findFile) {
        try {
            System.out.println("Start scanning files");
            Instant start = Instant.now();
            filesHandler.setScanner(contactsFile);
            contacts = filesHandler.getArrayFromFile();
            filesHandler.setScanner(findFile);
            find = filesHandler.getArrayFromFile();
            Instant end = Instant.now();

            Duration scanningDuration = Duration.between(start,end);

            ArrayList<String> found = new ArrayList<>();
            System.out.println("Start searching..");
            start = Instant.now();
            for (String s : find) {
                if (contacts.contains(s)) {
                    found.add(s);
                }
            }
            end = Instant.now();
            Duration searchDuration = Duration.between(start,end);

            System.out.format("Entries scanned %d. Time taken: %2d min. %2d sec. %3d ms.\n"
                    ,contacts.size()+ find.size(), scanningDuration.toMinutes(), scanningDuration.toSeconds()%60,
                    scanningDuration.toMillis()%1000);
            System.out.format("Found %d / %d entries. Time taken: %2d min. %2d sec. %3d ms."
                    , found.size(), find.size(), searchDuration.toMinutes(), searchDuration.toSeconds()%60,
                    searchDuration.toMillis()%1000);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
