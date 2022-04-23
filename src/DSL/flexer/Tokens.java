package DSL.flexer;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class Tokens {

    public LinkedHashMap<String, Pattern> tokenA = new LinkedHashMap<String, Pattern>();

    {
        tokenA.put("OL_COMMENT", Pattern.compile("#.*")); // *
        // multi_line_comment // * всякое может случиться.. а может и не случиться (:
        //
//        tokenA.put("SPACE", Pattern.compile("\s+")); // * пробелы могут и пригодиться (:
        //
        tokenA.put("KW_BEGIN", Pattern.compile("begin")); // * мб убрать
        tokenA.put("KW_END", Pattern.compile("end")); // * мб убрать
        tokenA.put("KW_WHILE", Pattern.compile("while")); //
        tokenA.put("KW_DO", Pattern.compile("do")); //
        tokenA.put("KW_IF", Pattern.compile("if")); //
        tokenA.put("KW_THEN", Pattern.compile("then")); //
        tokenA.put("KW_ELSE", Pattern.compile("else")); //
        tokenA.put("KW_FOR", Pattern.compile("for")); //
        //+@ to
//        tokenA.put("KW_BREAK", Pattern.compile("break")); // * @own
        tokenA.put("KW_RETURN", Pattern.compile("return")); // * @own
        tokenA.put("KW_READ", Pattern.compile("read")); // * мб убрать
        tokenA.put("KW_WRITE", Pattern.compile("write")); // * мб убрать
        //
        tokenA.put("KW_VOID", Pattern.compile("Void")); // * @own
        tokenA.put("KW_VAR", Pattern.compile("Var")); // * мб убрать и сделать через TYPE_NAME
        tokenA.put("KW_BOOL", Pattern.compile("Bool")); // * аналогично
        tokenA.put("KW_INT", Pattern.compile("Int")); // * аналогично
        tokenA.put("KW_STRING", Pattern.compile("String")); // * аналогично
        //
        tokenA.put("KW_LOGIC_AND", Pattern.compile("and")); // * мб убрать
        tokenA.put("KW_LOGIC_OR", Pattern.compile("or")); // * мб убрать
        //
        tokenA.put("KW_LOGIC_TRUE", Pattern.compile("True")); //
        tokenA.put("KW_LOGIC_FALSE", Pattern.compile("False")); //
        //
        tokenA.put("IDENT", Pattern.compile("[a-z]([_a-zA-Z\\d])*")); //
        tokenA.put("INT", Pattern.compile("0|(-?[1-9](\\d)*)")); //
        //+own@ числа с плавающей точкой мб добавить еще
        tokenA.put("STRING", Pattern.compile("\"(.*)\"")); //
        tokenA.put("FUNC_NAME", Pattern.compile("[a-zA-Z][_a-zA-Z\\d]{2,}")); // *
        tokenA.put("TYPE_NAME", Pattern.compile("[A-Z][_a-zA-Z\\d]{2,}")); // * @own
        //сюда?@ COMMENT
        //+@ Доступ через указатель
        tokenA.put("POW_OP", Pattern.compile("\\*\\*")); //
        tokenA.put("MUL_OP", Pattern.compile("\\*")); //
        tokenA.put("DIV_OP", Pattern.compile("/")); //
        tokenA.put("REM_OP", Pattern.compile("%")); //
        //+@ Преобразование типа
        tokenA.put("ADD_OP", Pattern.compile("\\+")); //
        tokenA.put("SUB_OP", Pattern.compile("-")); //
        //+@ Битовый Сдвиг влево-вправо
        tokenA.put("COMP_VAL", Pattern.compile("<=?|>=?")); //
        tokenA.put("COMP_EQL", Pattern.compile("==|!=")); //
        tokenA.put("B_AND", Pattern.compile("&")); //
        tokenA.put("B_XOR", Pattern.compile("\\^")); //
        tokenA.put("B_OR", Pattern.compile("\\|")); //
        tokenA.put("B_NOT", Pattern.compile("!")); // * @own ************************************************************
        tokenA.put("LOGIC_AND", Pattern.compile("&&")); //
        tokenA.put("LOGIC_OR", Pattern.compile("\\|\\|")); //
        //+@    cond ? (a_if_true) : (b_if_false) // В ПАРСЕРЕ мб сделать
        //+@    ++ -- += -=    мб добавлю.. а мб и нет..
        tokenA.put("ASSIGN_OP", Pattern.compile("=")); //
        //
        tokenA.put("SEP_END_LINE", Pattern.compile("(;3)")); // * хз какой оставить и оставлять ли вообще
//        tokenA.put("SEP_END_LINE", Pattern.compile("(;\\))")); // * хз какой оставить и оставлять ли вообще
        //
        tokenA.put("SEP_L_BRACKET", Pattern.compile("\\(")); //
        tokenA.put("SEP_R_BRACKET", Pattern.compile("\\)")); //
        tokenA.put("SEP_L_BRACE", Pattern.compile("\\{")); //
        tokenA.put("SEP_R_BRACE", Pattern.compile("}")); //
        tokenA.put("SEP_SEMICOLON", Pattern.compile(";\s")); //
        tokenA.put("SEP_COLON", Pattern.compile(":")); //
        tokenA.put("SEP_COMMA", Pattern.compile(",")); //
        tokenA.put("SEP_DOT", Pattern.compile("\\.")); //
        tokenA.put("SEP_QUOTE", Pattern.compile("['\"]")); // * мб убрать
        //
    }

    public static class Token {
        private final String type;
        private final String value;
    //    private int posL; // index
    //    private int posR;

        public Token(String type, String value) {
            this.type = type;
            this.value = value;
        }

        // *
    //    public Token(String type, String value, int posL, int posR) {
    //        this.type = type;
    //        this.value = value;
    //        this.posL = posL;
    //        this.posR = posR;
    //    }
    //    public int getPosL() {
    //        return posL;
    //    }
    //    public void setPosL(int posL) {
    //        this.posL = posL;
    //    }
    //    public int getPosR() {
    //        return posR;
    //    }
    //    public void setPosR(int posR) {
    //        this.posR = posR;
    //    }
    //    @Override
    //    public String toString() {
    //        return "Token{" +
    //                "type='" + type + '\'' +
    //                ", value='" + value + '\'' +
    //                ", posL=" + posL +
    //                ", posR=" + posR +
    //                '}';
    //    }
        // *

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
