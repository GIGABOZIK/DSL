package DSL2;

import DSL2.NODES.RootNode;
import DSL2.TOKENS.Token;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        main1();
    }
    //
    public static void main1() {
        // Ввод
        ArrayList<String> code = new CodeReader().readInput(); // Прочитали код
        System.out.println("\nВведенный код:");
        int lineCnt = 0;
        for (String line : code) System.out.println(++lineCnt + ".  " + line);
        // Лексер
        ArrayList<Token> tokenList = new Lexer(code).getTokens(); // Получили токены от лексера
        System.out.println("\nНайденные токены:");
        for (Token token : tokenList) System.out.println(token);
        // Парсер
        RootNode rootNode = new Parser(tokenList).parseLang();
        System.out.println("\nСинтаксический анализ не выявил проблем!");
        // Интерпретатор

        System.out.println("Результат работы программы:");
        new Interpreter(rootNode).execute();
        //
    }
    //
    public static void main2() {
        // Сразу результат по стандартным параметрам (чтение из файла)
//        for (Token token : new Lexer().getTokens()) System.out.println(token);
//        new Parser().parseLang();
        new Interpreter().execute();
    }
}
