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
    private boolean checkToken(String seekType) {
        boolean result = true;
        if (!tokenList.get(it).getType().equals(seekType)) {
            result = false;
            errSeekType = seekType;
            errIt = it;
        }
        else {
            it++;
            // СтРоИтЬ ДеРеВо ?????
            // Пока что просто выписывать подряд найденные штуки
        }
        return result;
    }
    public void pass() {}
    private boolean checkMe(String rule) {
        boolean result = true;
        String tokT = "OL_COMMENT";
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
                boolean result_local = checkToken("SEP_COMMA");
//                if (result_local)
                if (checkToken("SEP_COMMA"))
                    if (!checkMe("param_list")) return false;
                else if (!(checkMe("nothing"))) return false;
            }
            //
            case "decl_var" -> {
                if (!checkToken("TYPE_NAME")) return false;
                if (!(checkToken("IDENT") | checkMe("assign")
                )) return false;
            }
            //
            case "stmts_block" -> { // + + + + +
                if (!checkToken("SEP_L_BRACE")) return false;
                // hmm
                if (!checkMe("stmt")) return false;
                while (checkMe("stmt")) pass();
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
//                        | checkToken("KW_LOGIC_TRUE")
//                        | checkToken("KW_LOGIC_FALSE")
//                        | checkMe("ternary")
                )) return false;
            }
            //
            // *продолжить*
            //
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
