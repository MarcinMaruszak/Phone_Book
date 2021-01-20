package phonebook.logic;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;

public class PhoneBook {
    private FilesHandler filesHandler;
    ArrayList<String> contacts;
    ArrayList<String> find;
    Hashtable<String, String> hashContacts;

    public PhoneBook() {
        filesHandler = new FilesHandler();
        contacts = new ArrayList<>();
        find = new ArrayList<>();
        hashContacts = new Hashtable<>();
    }


    public void search(String contactsFile, String findFile) {
        try {
            fillArrays(contactsFile, findFile);

            System.out.println("Start searching (linear search)...");
            Instant start = Instant.now();
            int found = linearSearch(new ArrayList<>(contacts));
            Instant end = Instant.now();
            printLinResults(Duration.between(start, end), found);

            bubblesSortAndJumpSearch(new ArrayList<>(contacts));

            quickSortAndBinarySearch(contactsFile, findFile);

            hashSearch(contactsFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void hashSearch(String contactsFile) {
        System.out.println("Start searching (hash table)...");

        Duration fillDuration = fillHashTable(contactsFile);

        Instant start = Instant.now();
        int found = hashSearch();
        Instant end = Instant.now();

        Duration searchDuration = Duration.between(start, end);
        Duration totalDuration = fillDuration.plus(searchDuration);

        System.out.printf("Found %d / %d entries. Time taken: %2d min. %2d sec. %3d ms.\n" +
                        "Creating time: %2d min. %2d sec. %3d ms.\n" +
                        "Searching time: %2d min. %2d sec. %3d ms.\n",
                found, find.size(), totalDuration.toMinutes(),
                totalDuration.toSeconds() % 60, totalDuration.toMillis() % 1000,
                fillDuration.toMinutes(), fillDuration.toSeconds() % 60, fillDuration.toMillis() % 1000,
                searchDuration.toMinutes(), searchDuration.toSeconds() % 60, searchDuration.toMillis() % 1000);
    }

    private Duration fillHashTable(String contactsFile) {
        try {
            filesHandler.setScanner(contactsFile);
            Instant start = Instant.now();
            hashContacts = filesHandler.getHashtable();
            Instant end = Instant.now();
            return Duration.between(start, end);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Duration.ofMillis(0);
        }

    }

    private int hashSearch() {
        int found = 0;
        for (String s : find) {
            if (hashContacts.get(s) != null) {
                found++;
            }
        }
        return found;
    }

    private void quickSortAndBinarySearch(String fileName, String findName) {
        System.out.println("Start searching (quick sort + binary search)...");

        Duration quickSortDuration = quickSort(fileName, findName);
        Instant start = Instant.now();
        int found = binarySearch(contacts);
        Instant end = Instant.now();
        Duration searchDuration = Duration.between(start, end);
        Duration totalDuration = quickSortDuration.plus(searchDuration);

        System.out.printf("Found %d / %d entries. Time taken: %2d min. %2d sec. %3d ms.\n" +
                        "Sort time: %2d min. %2d sec. %3d ms.\n" +
                        "Searching time: %2d min. %2d sec. %3d ms.\n\n",
                found, find.size(), totalDuration.toMinutes(),
                totalDuration.toSeconds() % 60, totalDuration.toMillis() % 1000,
                quickSortDuration.toMinutes(), quickSortDuration.toSeconds() % 60, quickSortDuration.toMillis() % 1000,
                searchDuration.toMinutes(), searchDuration.toSeconds() % 60, searchDuration.toMillis() % 1000);
    }

    private int binarySearch(ArrayList<String> contacts) {
        int found = 0;
        for (String query : find) {
            if (binarySearch(contacts, query, 0, contacts.size() - 1) != -1) {
                found++;
            }
        }
        return found;
    }

    private int binarySearch(ArrayList<String> contacts, String query, int left, int right) {
        if (left > right) {
            return -1;
        }
        int mid = left + (right - left) / 2;
        String contact = contacts.get(mid);
        if (contact.equals(query)) {
            return mid;
        } else if (contact.compareTo(query) < 0) {
            return binarySearch(contacts, query, mid + 1, right);
        } else {
            return binarySearch(contacts, query, left, mid - 1);
        }
    }


    private Duration quickSort(String fileName, String findName) {

        try {
            Instant start = Instant.now();
            fillArrays(fileName, findName);
            quickSort(contacts, 0, contacts.size() - 1);
            Instant end = Instant.now();
            return Duration.between(start, end);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Duration.ofMillis(0);
        }

    }

    private void quickSort(ArrayList<String> contacts, int left, int right) {
        if (left < right) {
            int pivotIndex = pivotIndex(contacts, left, right);
            quickSort(contacts, left, pivotIndex - 1);
            quickSort(contacts, pivotIndex + 1, right);
        }
    }

    private int pivotIndex(ArrayList<String> contacts, int left, int right) {
        String pivot = contacts.get(right);
        int pivotIndex = left;
        for (int i = left; i < right; i++)
            if (contacts.get(i).compareTo(pivot) < 0) {
                swap(contacts, i, pivotIndex);
                pivotIndex++;
            }
        swap(contacts, right, pivotIndex);
        return pivotIndex;
    }

    private void swap(ArrayList<String> contacts, int i, int pivotIndex) {
        String s = contacts.set(i, contacts.get(pivotIndex));
        contacts.set(pivotIndex, s);
    }


    private void fillArrays(String contactsFile, String findFile) throws FileNotFoundException {
        filesHandler.setScanner(contactsFile);
        contacts = filesHandler.getArrayFromFile();
        filesHandler.setScanner(findFile);
        find = filesHandler.getArrayFromFile();
    }

    private int linearSearch(ArrayList<String> contacts) {
        int size = 0;
        for (String findStr : find) {
            for (String contact : contacts) {
                if (contact.equals(findStr)) {
                    size++;
                }
            }
        }
        return size;
    }

    private void printLinResults(Duration duration, int size) {
        System.out.format("Found %d / %d entries. Time taken: %2d min. %2d sec. %3d ms." +
                        "\n\n"
                , size, find.size(), duration.toMinutes(), duration.toSeconds() % 60,
                duration.toMillis() % 1000);
    }

    private void bubblesSortAndJumpSearch(ArrayList<String> contacts) {
        System.out.println("Start searching (bubble sort + jump search)...");
        Duration bubbleSortDuration = bubbleSort(contacts);

        int found;
        Instant start = Instant.now();
        if (bubbleSortDuration.getSeconds() < 30) {
            found = jumpSearch(contacts);
        } else {
            found = linearSearch(contacts);
        }
        Instant end = Instant.now();
        Duration searchDuration = Duration.between(start, end);
        Duration totalDuration = bubbleSortDuration.plus(Duration.between(start, end));

        System.out.format("Found %d / %d entries. Time taken: %2d min. %2d sec. %3d ms.\n"
                        + "Sorting time: %2d min. %2d sec. %3d ms." +
                        (bubbleSortDuration.getSeconds() > 30 ? " - STOPPED, moved to linear search." : "") +
                        "\nSearching time: %2d min. %2d sec. %3d ms.\n\n"
                , found, find.size(), totalDuration.toMinutes(),
                totalDuration.toSeconds() % 60, totalDuration.toMillis() % 1000,
                bubbleSortDuration.toMinutes(), bubbleSortDuration.toSeconds() % 60,
                bubbleSortDuration.toMillis() % 1000,
                searchDuration.toMinutes(), searchDuration.toSeconds() % 60, searchDuration.toMillis() % 1000);
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
            if (Duration.between(start, check).toSeconds() > 30) {
                return Duration.between(start, check);
            }
        }
        return Duration.between(start, check);
    }

    private int jumpSearch(ArrayList<String> contacts) {
        int found = 0;

        int jump = (int) Math.sqrt(contacts.size());
        for (String query : find) {
            int left = -jump + 1;
            int right = 0;

            if (query.equals(contacts.get(0))) {
                found++;
            }
            while (right < contacts.size() - 1) {
                left += jump;
                right = Math.min(right + jump, contacts.size() - 1);
                if (query.compareTo(contacts.get(right)) <= 0) {
                    int i = backSearch(contacts, left, right, query);
                    if (i != 0) {
                        found++;
                    }
                    break;
                }
            }
        }
        return found;
    }

    private int backSearch(ArrayList<String> contacts, int left, int right, String query) {
        for (int i = right; i >= left; i--) {
            if (contacts.get(i).equals(query)) {
                return i;
            }
        }
        return -1;
    }
}
