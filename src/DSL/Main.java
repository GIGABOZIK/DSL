package DSL;

import DSL.flexer.Lexer;
import DSL.flexer.Tokens;
import DSL.fparser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println(new Tokens().tokenA); // Проверка токенов
        Main f = new Main();

        // Ввод
//        System.out.println("Выбери тип ввода: (0 - файл, 1 - консоль)");
//        Scanner sc = new Scanner(System.in);
//        LinkedList<String> code = f.readInput(sc.nextInt());
        LinkedList<String> code = f.readInput();

        // Проверка ввода
//        System.out.println("Your input:\n " + code);
        int lineCnt = 0;
        for (String line : code) System.out.println(++lineCnt + ". " + line);

        // Работа лексера
        Lexer lxr = new Lexer(code); // Передали код лексеру
        LinkedList<Tokens.Token> tokenList = lxr.getTokens(); // Получили токены от лексера

        // Вывести все токены
        System.out.println("\nTokens:\n");
        for (Tokens.Token token : tokenList) System.out.println(token);

        // Работа парсера
        Parser psr = new Parser(tokenList); // Передали токены парсеру
        psr.start(); // СДЕЛАТЬ ЧОТА
    }

    // Ввод
    private LinkedList<String> readInput() { return readInput(-1); }
    private LinkedList<String> readInput(Integer sel) {
        switch (sel) {
            case 1 -> {
                System.out.println("INPUT - FILE");
                Scanner scanFile = null;
                try {
                    scanFile = new Scanner(new File("somecode.txt"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return readInput(scanFile);
            }
            case 2 -> { // Console
                System.out.println("INPUT - CONSOLE");
                return readInput(new Scanner(System.in));
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

    /*
    //
    private LinkedList<String> readInput() {
        System.out.println("Выбери тип ввода: (0 - файл, 1 - консоль)");
        Scanner sc = new Scanner(System.in);
        switch (sc.nextInt()) {
            case 0 -> {
                System.out.println("INPUT - FILE");

                sc = null;
                try {
                    sc = new Scanner(new File("somecode.txt"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            case 1 -> {
                System.out.println("INPUT - CONSOLE");

                sc = new Scanner(System.in);
            }
            default -> {
                System.exit(1);
            }
        }
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
    //
     */
}