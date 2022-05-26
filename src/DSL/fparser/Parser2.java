package DSL.fparser;

import DSL.flexer.Tokens;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Parser2 {
    private LinkedList<Tokens.Token> tokenList;
    private int it = 0;

    private int errIt;
    private String errSeekType;

    public Parser2(LinkedList<Tokens.Token> tokenList) { this.tokenList = tokenList; }
    //
    public boolean langCheck() {
        if (checkMe("lang")) return true;
        else {
            System.out.println("'" + errSeekType + "' expected, instead was '" + tokenList.get(errIt) + "'");
            return false;
        }
    }
    //
    private boolean checkToken(String seekType) {
        boolean result = true;
        System.out.println("Checking '" + tokenList.get(it).getValue() + "' for Token '" + seekType + "'");
        if (!tokenList.get(it).getType().equals(seekType)) {
            result = false;
            errSeekType = seekType;
            errIt = it;
        }
        else {
            System.out.println(it + " FOUND: " + seekType + " = " + tokenList.get(it).getValue());
            it++;
            // СтРоИтЬ ДеРеВо ?????
            // Пока что просто выписывать подряд найденные штуки
        }
        return result;
    }
//    public void pass() {}
    private boolean checkMe(String rule) {
        boolean result = true;
        System.out.println("Checking Rule: " + rule);
        switch (rule) {
            // lang -> expr+
            case "lang" -> {
                while (it < tokenList.size()) {
                    if (!checkMe("expr")) {
//                        throw new ParseException(tokenList.get(errIt), errSeekType);
                        return false;
                    }
                }
            }
            // expr -> declaration | stmt | OL_COMMENT
            case "expr" -> {
                if (!(checkMe("declaration")
                        | checkMe("stmt")
                        | checkToken("OL_COMMENT")
                )) return false;
            }
            //
            case "declaration" -> {
                if (!(checkMe("decl_func") | checkMe("decl_var")
                )) return false;
            }
            //
            case "decl_func" -> { // ? ? ? ? ?
                if (!(checkToken("TYPE_NAME") | checkToken("KW_VOID")
                )) return false;
                if (!checkToken("FUNC_NAME")) return false;
                if (!checkToken("SEP_L_BRACKET")) return false;
                if (!(checkMe("param_list") | checkMe("nothing")
                )) return false;
                if (!checkToken("SEP_R_BRACKET")) return false;
                if (!checkMe("stmts_block")) return false;
            }
            //
            case "param_list" -> { // ? ? ? ? ?
                if (!checkToken("TYPE_NAME")) return false;
                if (!checkToken("IDENT")) return false;
                // hmm
                if (checkToken("SEP_COMMA"))
                    if (!checkMe("param_list")) { return false; }
                else return checkMe("nothing");
//                boolean result_local = checkToken("SEP_COMMA");
//                if (result_local)
//                if (checkToken("SEP_COMMA"))
//                    if (!checkMe("param_list")) return false;
//                else if (!checkMe("nothing")) return false;
            }
            //
            case "decl_var" -> {
                if (!checkToken("TYPE_NAME")) return false;
                if (!(checkMe("assign") | checkToken("IDENT")
                )) return false;
            }
            //
            case "stmts_block" -> { // + + + + +
                if (!checkToken("SEP_L_BRACE")) return false;
                // hmm
                if (!checkMe("stmt")) return false;
                while (true) {
                    if (!checkMe("stmt")) break;
                }
                //
                if (!checkToken("SEP_R_BRACE")) return false;
            }
            //
            case "stmt" -> {
                if (!(checkMe("decl_var")
                    | checkMe("assign")
                    | checkMe("func")
                    | checkMe("ifstmt")
//                    | checkMe("ternary")
                    | checkMe("loop")
                )) return false;
            }
            //
            case "assign" -> {
                if (!checkToken("IDENT")) return false;
                if (!checkToken("ASSIGN_OP")) return false;
                if (!checkMe("value")) return false;
            }
            //
            case "value" -> {
                if (!(checkToken("B_NOT") | checkMe("nothing")
                )) return false;
                if (!(checkToken("IDENT")
                        | checkToken("INT")
                        | checkToken("STRING")
                        | checkMe("func")
                        | checkMe("operation")
                        | checkToken("KW_LOGIC_TRUE")
                        | checkToken("KW_LOGIC_FALSE")
//                        | checkMe("ternary")
                )) return false;
            }
            //
            case "func" -> { // ? ? ? ? ?
                if (!checkToken("FUNC_NAME")) return false;
                if (!checkToken("SEP_L_BRACKET")) return false;
                if (!(checkMe("arg_list") | checkMe("nothing")
                )) return false;
                if (!checkToken("SEP_R_BRACKET")) return false;
            }
            //
            case "arg_list" -> { // * * * * *
                if (!checkMe("value")) return false;
//                while (true) {
//                    if (checkToken("SEP_COMMA"))
//                        if (!checkMe("arg_list")) { return false; }
//                    else break;
//                }
//                return true;
                if (checkToken("SEP_COMMA"))
                    if (!checkMe("arg_list")) { return false; }
                else return checkMe("nothing");
            }
            //
            case "operation" -> {
                if (!(checkMe("bracket_optn")
                        | checkMe("optn_math")
                        | checkMe("optn_bin")
                        | checkMe("optn_comp")
                )) return false;
            }
            //
            case "bracket_optn" -> {
                if (!checkToken("SEP_L_BRACKET")) return false;
                if (!checkMe("operation")) return false;
                if (!checkToken("SEP_R_BRACKET")) return false;
            }
            //
            case "optn_math" -> {
                if (!checkMe("value")) return false;
                if (!checkMe("math_opr")) return false;
                if (!checkMe("value")) return false;
            }
            case "math_opr" -> {
                if (!(checkToken("POW_OP")
                        | checkToken("MUL_OP")
                        | checkToken("DIV_OP")
                        | checkToken("REM_OP")
                        | checkToken("ADD_OP")
                        | checkToken("SUB_OP")
                )) return false;
            }
            //
            case "optn_bin" -> {
                if (!checkMe("value")) return false;
                if (!checkMe("bin_opr")) return false;
                if (!checkMe("value")) return false;
            }
            case "bin_opr" -> {
                if (!(checkToken("KW_LOGIC_AND")
                        | checkToken("KW_LOGIC_OR")
                        | checkToken("B_AND")
                        | checkToken("B_XOR")
                        | checkToken("B_OR")
//                        | checkToken("LOGIC_AND")
//                        | checkToken("LOGIC_OR")
                )) return false;
            }
            //
            case "optn_comp" -> {
                if (!checkMe("value")) return false;
                if (!checkMe("comp_opr")) return false;
                if (!checkMe("value")) return false;
            }
            case "comp_opr" -> { // Не все реализованы по отдельности
                if (!(checkToken("COMP_VAL")
                        | checkToken("COMP_EQL")
                )) return false;
            }
            //
            case "ifstmt" -> { // ? ? ? ? ?
                if (!checkToken("KW_IF")) return false;
                if (!checkMe("conditions_block")) return false;
                if (!checkMe("stmts_block")) return false;
                if (!(checkMe("elsestmt") | checkMe("nothing")
                )) return false;
            }
            //
            case "conditions_block" -> {
                if (!checkToken("SEP_L_BRACKET")) return false;
                if (!checkMe("value")) return false;
                if (!checkToken("SEP_R_BRACKET")) return false;
            }
            // condition -> value
            case "elsestmt" -> {
                if (!checkToken("KW_ELSE")) return false;
                if (!(checkMe("ifstmt") | checkMe("stmts_block")
                )) return false;
            }
            // ternary -> ...
            case "loop" -> {
                if (!(checkMe("while")
                        | checkMe("do_while")
                        | checkMe("for")
                )) return false;
            }
            case "while" -> {
                if (!checkToken("KW_WHILE")) return false;
                if (!checkMe("conditions_block")) return false;
                if (!checkMe("stmts_block")) return false;
            }
            case "do_while" -> {
                if (!checkToken("KW_DO")) return false;
                if (!checkMe("stmts_block")) return false;
                if (!checkToken("KW_WHILE")) return false;
                if (!checkMe("conditions_block")) return false;
            }
            case "for" -> {
                if (!checkToken("KW_FOR")) return false;
                if (!checkToken("SEP_L_BRACKET")) return false;

                if (!(checkMe("for_init") | checkMe("nothing")
                )) return false;
                if (!checkToken("SEP_SEMICOLON")) return false;

                if (!(checkMe("value") | checkMe("nothing")
                )) return false;
                if (!checkToken("SEP_SEMICOLON")) return false;

                if (!(checkMe("for_incr") | checkMe("nothing")
                )) return false;

                if (!checkToken("SEP_R_BRACKET")) return false;
                if (!checkMe("stmts_block")) return false;
            }
            case "for_init" -> {
                if (!(checkToken("TYPE_NAME") | checkMe("nothing")
                )) return false;
                if (!checkMe("assign")) return false;
            }
            case "for_incr" -> {
                if (!checkMe("assign")) return false;
            }
            //
            //
            //////
            //
            case "nothing" -> {
                System.out.println("?");
                return result;
            }
            default -> {
                return result;
            }
        }
        return result;
    }

}
