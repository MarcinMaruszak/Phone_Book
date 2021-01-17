package phonebook.logic;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class PhoneBook {
    private FilesHandler filesHandler;
    ArrayList<String> contacts;
    ArrayList<String> find;

    public PhoneBook() {
        filesHandler = new FilesHandler();
        contacts = new ArrayList<>();
        find = new ArrayList<>();
    }


    public void search(String contactsFile, String findFile) {
        try {
            filesHandler.setScanner(contactsFile);
            contacts = filesHandler.getArrayFromFile();
            filesHandler.setScanner(findFile);
            find = filesHandler.getArrayFromFile();

            System.out.println("Start searching (linear search)...");
            Instant start = Instant.now();
            ArrayList<String> foundLinear = linearSearch();
            Instant end = Instant.now();
            Duration linearDuration = Duration.between(start, end);

            System.out.format("Found %d / %d entries. Time taken: %2d min. %2d sec. %3d ms." +
                            "\n\nStart searching (bubble sort + jump search)...\n"
                    , foundLinear.size(), find.size(), linearDuration.toMinutes(), linearDuration.toSeconds() % 60,
                    linearDuration.toMillis() % 1000);

            Duration sortDuration = bubbleSort(contacts);

            if(sortDuration.getSeconds()<60){
                start = Instant.now();
                ArrayList<String> foundJumpArray = jumpSearch();
                end = Instant.now();
                Duration jumpDuration = Duration.between(start, end);
                Duration totalDuration = sortDuration.plus(jumpDuration);
                System.out.format("Found %d / %d entries. Time taken: %2d min. %2d sec. %3d ms.\n"
                        +"Sorting time: %2d min. %2d sec. %3d ms.\n"+
                        "Searching time: %2d min. %2d sec. %3d ms."
                        , foundJumpArray.size(), find.size(), totalDuration.toMinutes(),
                        totalDuration.toSeconds() % 60, totalDuration.toMillis() % 1000,
                        sortDuration.toMinutes(),sortDuration.toSeconds()%60,
                        sortDuration.toMillis()%1000,
                        jumpDuration.toMinutes(),jumpDuration.toSeconds()%60,jumpDuration.toMillis()%1000);
            }else {
                start = Instant.now();
                foundLinear = linearSearch();
                end = Instant.now();
                linearDuration = Duration.between(start, end);
                Duration totalDuration = sortDuration.plus(linearDuration);
                System.out.format("Found %d / %d entries. Time taken: %2d min. %2d sec. %3d ms.\n"
                                +"Sorting time: %2d min. %2d sec. %3d ms. - STOPPED, moved to linear search\n"+
                                "Searching time: %2d min. %2d sec. %3d ms."
                        , foundLinear.size(), find.size(), totalDuration.toMinutes(),
                        totalDuration.toSeconds() % 60, totalDuration.toMillis() % 1000,
                        sortDuration.toMinutes(),sortDuration.toSeconds()%60,
                        sortDuration.toMillis()%1000,
                        linearDuration.toMinutes(),linearDuration.toSeconds()%60,linearDuration.toMillis()%1000);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> linearSearch() {
        ArrayList<String> found = new ArrayList<>();
        for (String findStr : find) {
            for (String contact : contacts) {
                if (contact.equals(findStr)) {
                    found.add(findStr);
                }
            }
        }
        return found;
    }

    private Duration bubbleSort(ArrayList<String> arrayList) {
        Instant check = Instant.now();
        Instant start = Instant.now();

        for (int i = 0; i < arrayList.size() - 1; i++) {
            for (int j = 0; j < arrayList.size() - i - 1; j++) {
                if (arrayList.get(j).compareTo(arrayList.get(j + 1)) > 0) {
                    String s = arrayList.set(j, arrayList.get(j + 1));
                    arrayList.set(j + 1, s);
                }
            }
            check = Instant.now();
            if (Duration.between(start, check).toSeconds() > 60) {
                return Duration.between(start, check);
            }
        }
        return Duration.between(start, check);
    }

    private ArrayList<String> jumpSearch() {
        ArrayList<String> found = new ArrayList<>();
        int jump = (int) Math.sqrt(contacts.size());
        for (String query : find) {
            int left = -jump + 1;
            int right = 0;

            if (query.equals(contacts.get(0))) {
                found.add(query);
            }
            while (right < contacts.size() - 1) {
                left += jump;
                right = Math.min(right + jump, contacts.size() - 1);
                if (query.compareTo(contacts.get(right)) <= 0) {
                    int i = backSearch(contacts, left, right, query);
                    if(i!=0){
                        found.add(contacts.get(i));
                    }
                    break;
                }
            }
        }
        return found;
    }

    private int backSearch(ArrayList<String> contacts, int left, int right, String query) {
        for (int i =right; i >= left; i--) {
            if (contacts.get(i).equals(query)){
                return i;
            }
        }
        return -1;
    }
}
