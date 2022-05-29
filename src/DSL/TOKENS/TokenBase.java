package DSL.TOKENS;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class TokenBase {
    // BASE TOKEN LIST:
    private final LinkedHashMap<String, Pattern> baseTokenList = new LinkedHashMap<>();
    {
        baseTokenList.put("OL_COMMENT", Pattern.compile("#.*")); // * хз
        // multi_line_comment // * нинада...
        //
        baseTokenList.put("SPACE", Pattern.compile("\s+")); // * пробелы могут и пригодиться (:
        //
//        baseTokenList.put("KW_BEGIN", Pattern.compile("begin")); // * мб убрать
//        baseTokenList.put("KW_END", Pattern.compile("end")); // * мб убрать
        baseTokenList.put("KW_WHILE", Pattern.compile("while")); // $$$
        baseTokenList.put("KW_DO", Pattern.compile("do")); //
        baseTokenList.put("KW_IF", Pattern.compile("if")); // $$$
//        baseTokenList.put("KW_THEN", Pattern.compile("then")); // * мб убрать
        baseTokenList.put("KW_ELSE", Pattern.compile("else")); // $$$
        baseTokenList.put("KW_FOR", Pattern.compile("for")); // $$$
        //+@ to
        baseTokenList.put("KW_BREAK", Pattern.compile("break")); // * own@
//        baseTokenList.put("KW_RETURN", Pattern.compile("return")); // * own@
        baseTokenList.put("KW_READ", Pattern.compile("read")); // * мб убрать (NO)
        baseTokenList.put("KW_WRITE", Pattern.compile("write")); // * мб убрать (NO)
        //
//        baseTokenList.put("KW_VOID", Pattern.compile("Void")); // * own@
//        baseTokenList.put("KW_VAR", Pattern.compile("Var")); // * мб убрать и сделать через TYPE_NAME
//        baseTokenList.put("KW_BOOL", Pattern.compile("Bool")); // * аналогично
//        baseTokenList.put("KW_INT", Pattern.compile("Int")); // * аналогично
//        baseTokenList.put("KW_STRING", Pattern.compile("String")); // * аналогично
        //
//        baseTokenList.put("KW_LOGIC_AND", Pattern.compile("and")); // * мб убрать
//        baseTokenList.put("KW_LOGIC_OR", Pattern.compile("or")); // * мб убрать
//        baseTokenList.put("KW_LOGIC_NOT", Pattern.compile("not")); // *own@ мб убрать
        //
        baseTokenList.put("KW_LOGIC_TRUE", Pattern.compile("True")); //
        baseTokenList.put("KW_LOGIC_FALSE", Pattern.compile("False")); //
        //
        baseTokenList.put("IDENT", Pattern.compile("[a-z]([_a-zA-Z\\d])*")); // $$$
        baseTokenList.put("INT", Pattern.compile("0|(-?[1-9](\\d)*)")); // $$$
        //+own@ числа с плавающей точкой мб добавить еще
        baseTokenList.put("STRING", Pattern.compile("\"(.*)\"")); //
//        baseTokenList.put("FUNC_NAME", Pattern.compile("[a-z][_a-zA-Z\\d]{2,}")); // *
//        baseTokenList.put("TYPE_NAME", Pattern.compile("[A-Z][_a-zA-Z\\d]{2,}")); // * own@
        //сюда?@ COMMENT
        //+@ Доступ через указатель
//        baseTokenList.put("POW_OP", Pattern.compile("\\*\\*")); // *
        baseTokenList.put("MUL_OP", Pattern.compile("\\*")); // $$$
        baseTokenList.put("DIV_OP", Pattern.compile("/")); // $$$
//        baseTokenList.put("REM_OP", Pattern.compile("%")); // *
        //+@ Преобразование типа
        baseTokenList.put("ADD_OP", Pattern.compile("\\+")); // $$$
        baseTokenList.put("SUB_OP", Pattern.compile("-")); // $$$
        //+@ Битовый Сдвиг влево-вправо
        baseTokenList.put("COMP_L_EQ", Pattern.compile("<="));
        baseTokenList.put("COMP_M_EQ", Pattern.compile(">="));
        baseTokenList.put("COMP_LESS", Pattern.compile("<")); // $$$
        baseTokenList.put("COMP_MORE", Pattern.compile(">")); // $$$
        baseTokenList.put("COMP_EQ", Pattern.compile("==")); // $$$
        baseTokenList.put("COMP_NEQ", Pattern.compile("!=")); //
        baseTokenList.put("B_AND", Pattern.compile("&")); //
        baseTokenList.put("B_XOR", Pattern.compile("\\^")); //
        baseTokenList.put("B_OR", Pattern.compile("\\|")); //
        baseTokenList.put("B_NOT", Pattern.compile("~")); // * ! или ~
//        baseTokenList.put("LOGIC_AND", Pattern.compile("&&")); // *
//        baseTokenList.put("LOGIC_OR", Pattern.compile("\\|\\|")); // *
        //+@    ++ -- += -=    мб.. или нет..
        baseTokenList.put("ASSIGN_OP", Pattern.compile("=")); // $$$
        //
        baseTokenList.put("SEP_END_LINE", Pattern.compile("(;3)")); // * END_LINE $$$
//        baseTokenList.put("SEP_END_LINE", Pattern.compile("(;\\))")); // * END_LINE
        //
//        baseTokenList.put("SEP_QUE_MARK", Pattern.compile("\\?")); // * для ternary
        baseTokenList.put("SEP_L_BRACKET", Pattern.compile("\\(")); // $$$
        baseTokenList.put("SEP_R_BRACKET", Pattern.compile("\\)")); // $$$
        baseTokenList.put("SEP_L_BRACE", Pattern.compile("\\{")); // $$$
        baseTokenList.put("SEP_R_BRACE", Pattern.compile("}")); // $$$
        baseTokenList.put("SEP_SEMICOLON", Pattern.compile(";\s")); // $$$
//        baseTokenList.put("SEP_COLON", Pattern.compile(":")); //
        baseTokenList.put("SEP_COMMA", Pattern.compile(",")); //
//        baseTokenList.put("SEP_DOT", Pattern.compile("\\.")); //
//        baseTokenList.put("SEP_QUOTE", Pattern.compile("['\"]")); // *
        //
    }
    public LinkedHashMap<String, Pattern> getBaseTokenList() {
        return baseTokenList;
    }
}
