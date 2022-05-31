package DSL;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CodeReader {
    public ArrayList<String> readInput() { return readInput(-1); }
    public ArrayList<String> readInput(int select) {
        switch (select) {
            case 0 -> {
                return readInput(new Scanner(System.in));
            }
            case 1 -> {
                Scanner scanFile = null;
                try {
                    scanFile = new Scanner(new File("somecode.txt"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return readInput(scanFile);
            }
            default -> {
                return readInput(1);
            }
        }
    }
    public ArrayList<String> readInput(Scanner sc) {
        ArrayList<String> strList = new ArrayList<>();
        int i = 0;
        while (sc != null && sc.hasNextLine()) {
            strList.add(i, sc.nextLine());
//            if (strList.get(i++).isEmpty()) {
            if (strList.get(i++).equals("END")) {
                strList.remove(i - 1);
                break;
            }
        }
        return strList;
    }
}
