package DSL2;

import DSL2.TOKENS.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Ввод
        ArrayList<String> code = new Main().readInput(); // Прочитали код
        System.out.println("\nВведенный код:");
        int lineCnt = 0;
        for (String line : code) System.out.println(++lineCnt + ".  " + line);
        // Лексер
        ArrayList<Token> tokenList = new Lexer(code).getTokens(); // Получили токены от лексера
        System.out.println("\nНайденные токены:");
        for (Token token : tokenList) System.out.println(token);
        // Парсер

        // Интерпретатор
        //
    }
    //
    // Ввод
    private ArrayList<String> readInput() { return readInput(-1); }
    private ArrayList<String> readInput(int select) {
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
    private ArrayList<String> readInput(Scanner sc) {
        ArrayList<String> strList = new ArrayList<>();
        int i = 0;
        while (sc != null && sc.hasNextLine()) {
            strList.add(i, sc.nextLine());
            if (strList.get(i++).isEmpty()) {
                strList.remove(i - 1);
                break;
            }
        }
        return strList;
    }
}
