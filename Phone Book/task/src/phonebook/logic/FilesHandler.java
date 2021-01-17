package phonebook.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FilesHandler {
    Scanner scanner;

    public FilesHandler() {

    }

    public void setScanner(String file) throws FileNotFoundException {
        this.scanner =  new Scanner(new File("D:\\Downloads-Chrome\\"+file));
    }

    public ArrayList<String> getArrayFromFile(){
        ArrayList<String> arrayList = new ArrayList<>();
        while (scanner.hasNext()){
            String s = scanner.nextLine();
            s=s.replaceAll("\\d","").trim();
            arrayList.add(s);
        }
        return arrayList;
    }
}
