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
        Scanner sc = new Scanner(System.in);
        Main f = new Main();
        // f.testLexer();
        // f.testParser();
         f.testFull1();
//         f.testFull2();

        // * * * * * * * * *
        // Добавить вывод номера строки и позиции в строке для лексемы
        // * * * * * * * * *
    }

    //
    void testFull1() {
        LinkedList<String> code = readInput();
        System.out.println("Your input:\n " + code); // * Проверка ввода

        Lexer lxr = new Lexer(code);
        LinkedList<Tokens.Token> tokList = lxr.getTokens();

        System.out.println("\nTokens:\n");
        for (Tokens.Token token : tokList) System.out.println(token);

        Parser psr = new Parser(tokList);
        psr.start();
    }
    private void testFull2() {
        new Parser(new Lexer(readInput()).getTokens()).start();
    }
    //

    // Ввод
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
}