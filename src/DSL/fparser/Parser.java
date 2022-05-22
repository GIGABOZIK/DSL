package DSL.fparser;

import DSL.flexer.Tokens;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Parser {
    public static final LinkedHashMap<String, Pattern> tokenA = new Tokens().tokenA;
    private LinkedList<Tokens.Token> tokenList;
    private int it = 0;

    private int errIt;
    private String errSeekType;
    public Parser(LinkedList<Tokens.Token> tokenList) { this.tokenList = tokenList; }

    public void pass() {}
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

    public boolean langCheck() throws ParseException {
//        lang -> expr+
//        System.out.println("\n\n\n");
//        System.out.println(tokenList);
        while (it < tokenList.size()) {
            if (!expr()) {
                throw new ParseException(tokenList.get(errIt), errSeekType);
//                return false;
            }
        }
        return true;
    }
    public boolean expr() {
        if (!declaration()) {
            if (!stmt()) {
                if (!checkToken("OL_COMMENT")) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean declaration() {
        if (!decl_func())
            if (!decl_var())
                return false;
        return true;
    }
    public boolean decl_func() { // ? ? ? ? ? ? ?
        if (!checkToken("TYPE_NAME")) {
            if (!checkToken("KW_VOID"))
                return false;
        }
        if (!checkToken("FUNC_NAME")) return false;
        if (!checkToken("SEP_L_BRACKET")) return false;
        if (!param_list()) return false;
        if (!checkToken("SEP_R_BRACKET")) return false;
        if (!stmts_block) return false;
        return true;
    }
    public boolean param_list() { // ? ? ? ? ? ?
        boolean result = true;
        while (result) {
            if (!checkToken("TYPE_NAME")) result = false;
            if (!checkToken("IDENT")) result = false;
            if (!checkToken("SEP_COMMA"))
                if (!param_list())
                    return result;
        }
        return result;
    }
    public boolean decl_var() {
        if (!checkToken("TYPE_NAME")) return false;
        if (!checkToken("IDENT"))
            if (!assign())
                return false;
        return true;
    }
    public boolean stmts_block() {
        if (!checkToken("SEP_L_BRACE")) return false;
        if (!stmt()) return false;
        while (stmt()) pass();
        if (!checkToken("SEP_R_BRACE")) return false;
        return true;
    }
    public boolean stmt() {
        if (!decl_var())
            if (!assign())
                if (!func())
                    if (!ifstmt())
                        if (!ternary())
                            if (!loop())
                                if (!rreturn())
                                    return false;
        return true;
    }
    public boolean assign() {
        if (!checkToken("IDENT")) return false;
        if (!checkToken("ASSIGN_OP")) return false;
        if (!value()) return false;
        return true;
    }
    public boolean value() { // ? ? ? ? ? ? ? ? ?
        checkToken("B_NOT");
        if (!checkToken("IDENT"))
            if (!checkToken("INT"))
                if (!checkToken("STRING"))
                    if (!func())
                        if (!operation())
                            if (!checkToken("KW_LOGIC_TRUE"))
                                if (!checkToken("KW_LOGIC_FALSE"))
                                    if (!ternary())
                                        return false;
        return true;
    }
    public boolean func() {
        if (!checkToken("FUNC_NAME")) return false;
        if (!checkToken("SEP_L_BRACKET")) return false;
        if (!arg_list()) return false;
        if (!checkToken("SEP_R_BRACKET")) return false;
        return true;
    }
    public boolean arg_list() { // * * * * * * * *
        if (!value()) return false;
        while (checkToken("SEP_COMMA")) {
            if (!value()) return false;
        }
        return true;
    }
    //
    //
    public boolean optn_math() {
        boolean result = true;
//        if (!(value())) result = false;
//        if (!(math_opr())) result = false;
//        if (!(value())) result = false;
        result = value();
        result = math_opr();
        result = value();
        return result;
    }
    public boolean math_opr() {
        return tokenList.get(it).equals("POW_OP")
                | tokenList.get(it).equals("MUL_OP")
                | tokenList.get(it).equals("DIV_OP")
                | tokenList.get(it).equals("REM_OP")
                | tokenList.get(it).equals("ADD_OP")
                | tokenList.get(it).equals("SUB_OP");
//        return result;
    }

}

