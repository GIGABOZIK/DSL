package DSL2;

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

        // Интерпретатор
        //
    }
    //
    public static void main2(String[] args) {
        // Сразу результат по стандартным параметрам
//        new Lexer().getTokens();
//        new Parser().parseLang();
//        new Exegete().execute();
    }
}
