package phonebook;

import phonebook.logic.PhoneBook;

public class Main {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.search("directory.txt", "find.txt");
    }
}
