package DSL;

import DSL.TOKENS.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final ArrayList<String> code; // Массив строк введенного кода
    private int position = 0; // Количество токенов

    // Список используемых токенов
    private final LinkedHashMap<String, Pattern> baseTokenList = new TokenBase().getBaseTokenList();

    public Lexer() { this.code = new CodeReader().readInput(); }
    public Lexer(ArrayList<String> code) { this.code = code; }

    // Основной метод
    public ArrayList<Token> getTokens() {
        ArrayList<Token> tokenList = new ArrayList<>();
        int lineCnt = 0; // Номер текущей строки
        int linePos; // Позиция в текущей строке
        for (String line : code) {
            lineCnt++;
            linePos = 0;
            while (linePos < line.length()) { // !=
                int posSeek = linePos;
                int tokenFound = 0;
                for (String tokenName : baseTokenList.keySet()) {
                    Pattern rgxPtn = baseTokenList.get(tokenName);
                    Matcher rgxMtr = rgxPtn.matcher(line);
                    if (rgxMtr.find(posSeek)) {
                        if (rgxMtr.start() == posSeek) {
                            tokenFound++;
                            linePos = rgxMtr.end();
                            String nTokenValue = line.substring(posSeek, linePos);
                            if (!tokenName.equals("SPACE")) {//*@ Избегание пробелов ?...
                                Token nToken = new Token(tokenName, nTokenValue, ++position, lineCnt);
//                                System.out.println(tokenNew); //*@ Вывод найденного токена
                                tokenList.add(nToken);
                            }
                            break;
                        }
                    }
                }
                if (tokenFound == 0) {
                    System.out.println("Token not found in line " + lineCnt + " at pos " + posSeek + " :(");
                }
            }
        }
        return tokenList;
    }
}
