package DSL;

import DSL.flexer.Lexer;
import DSL.flexer.Tokens;
import DSL.fparser.Parser2;
import DSL2.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        System.out.println("\nРаспознаваемые токены:\n" + new Tokens().tokenA); // Проверка токенов
        Main f = new Main();

        // Ввод
//        System.out.println("Выбери тип ввода: (0 - файл, 1 - консоль)");
//        Scanner sc = new Scanner(System.in);
//        LinkedList<String> code = f.readInput(sc.nextInt());
        LinkedList<String> code = f.readInput();

//         Проверка ввода
//        System.out.println("\nВведенный код:\n " + code);
        System.out.println("\nВведенный код:");
        int lineCnt = 0;
        for (String line : code) System.out.println(++lineCnt + ". " + line);

        // Работа лексера
        Lexer lxr = new Lexer(code); // Передали код лексеру
        LinkedList<Tokens.Token> tokenList = lxr.getTokens(); // Получили токены от лексера

//         Вывести все токены
        System.out.println("\nНайденные токены:");
        for (Tokens.Token token : tokenList) System.out.println(token);
        System.out.println("Lexer: OK");
        // Работа парсера
        System.out.println("\nРезультат работы парсера:");
        Parser2 psr = new Parser2(tokenList); // Передали токены парсеру
        if (psr.langCheck()) { // СДЕЛАТЬ ЧОТА
            System.out.println("Parser: OK");
        } else {
            System.out.println("Parser: ERROR");
        }
    }

    // Ввод
    private LinkedList<String> readInput() { return readInput(-1); }
    private LinkedList<String> readInput(Integer sel) {
        switch (sel) {
            case 0 -> {
//                System.out.println("INPUT - CONSOLE");
                return readInput(new Scanner(System.in));
            }
            case 1 -> {
//                System.out.println("INPUT - FILE");
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
    private LinkedList<String> readInput(Scanner sc) {
        LinkedList<String> strList = new LinkedList<>();
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