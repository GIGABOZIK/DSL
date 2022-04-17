package DSL.flexer;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final LinkedList<String> code;
    public static final LinkedHashMap<String, Pattern> tokenA = new Tokens().tokenA;

    public Lexer(LinkedList<String> code) {
        this.code = code;
    }

    public LinkedList<Tokens.Token> getTokens() { // Метод поиска и возврата токенов (Можно минимизировать, но лень пока)
        LinkedList<Tokens.Token> tokList = new LinkedList<>(); // Создали список токенов

        for (String strG : code) { // Перебираем элементы-строки
            while (!strG.isEmpty()) {
                int pos = 0; // Устанавливаем позицию поиска в начало
                int tokCnt = 0; // Сбрасываем счетчик
                for (String tokenName : tokenA.keySet()) { // Перебираем доступные токены
                    Pattern rgxPtn = tokenA.get(tokenName); // *
                    Matcher rgxMtr = rgxPtn.matcher(strG); //*--^
                    if (rgxMtr.find()) { // Если найдена подходящая по паттерну токена подстрока
                        if (rgxMtr.start() == pos) { //  Если подстрока начинается в нужной позиции
                            tokCnt++; // Увеличиваем счетчик
                            pos = rgxMtr.end(); // Устанавливаем курсор сразу после найденной подстроки
                            String tokenValue = strG.substring(rgxMtr.start(), rgxMtr.end()); // Создаем значение токена*
                            Tokens.Token newTok = new Tokens.Token(tokenName, tokenValue); // Создаем токен*
//                            if (!tokenName.equals("SPACE")) // * Избегание пробелов ?...
//                                System.out.println(newTok); // * Вывод найденного токена
                            tokList.add(newTok); // Добавляем новый токен в список токенов
                        }
                    }
                }
                if (tokCnt == 0) pos++; // Если не найдено, ищем со следующей позиции
                strG = strG.substring(pos); // Обрезаем строку * --^
            }
//            System.out.println("EOL"); // * Для наглядности вывода
        }
//        System.out.println("\nRESULT:\n"); // *
//        for (Token token : tokList) System.out.println(token); // *
        return tokList;
    }
}
