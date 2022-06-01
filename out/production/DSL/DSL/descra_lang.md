___
# Требования:
* Бесконечные скобки
* Базовые арифметические операции
* For + While
* LinkedList + HashSet

___
## Укороченный вариант:
* lang -> expr+
  * expr -> (assign_expr | stmt_if | loop_while | loop_for | io_console)
    * assign_expr -> init ';3'
      * init -> IDENT ASSIGN_OP value
        * value -> Compare | Condition | AddSub | MulDiv | Brackets | UnValue
          * Compare -> value (comp_token value)*
            * comp_token -> COMP_LESS | COMP_L_EQ | COMP_MORE | COMP_M_EQ | COMP_EQ | COMP_NEQ
          * condition -> value comp_token value
          * AddSub -> value (ADD_OP | SUB_OP value)*
          * MulDiv -> value (MUL_OP | DIV_OP value)*
          * Brackets -> '(' value ')'
          * UnValue -> INT | IDENT | STRING
    * io_console -> KW_WRITE value ';3'            $$ KW_READ..
    * stmt_loop_for -> KW_FOR '(' init ';' condition ';' expr ')' stmt_body
        * stmt_body -> '{' expr+ '}'
    * stmt_loop_while -> KW_WHILE '(' condition ')' stmt_body
    * stmt_if -> KW_IF '(' condition ')' stmt_body stmt_else?
      * stmt_else -> KW_ELSE stmt_body
* Упрощения:
  * '(')' -> SEP_ 'L'R' _BRACKET
  * '{'}' -> SEP_ 'L'R' _BRACE
  * ';' -> SEP_SEMICOLON
  * ';3' -> SEP_END_LINE

___
## Длинный, почти полностью проработанный вариант, но, как оказалось, труднореализуемый ((
* lang -> expr+
* expr -> declaration | stmt | OL_COMMENT
* declaration -> decl_func | decl_var             $$ | decl_class
    * decl_func -> (TYPE_NAME | KW_VOID) FUNC_NAME SEP_L_BRACKET param_list? SEP_R_BRACKET stmts_block
        * param_list -> TYPE_NAME IDENT (SEP_COMMA param_list)?
    * decl_var -> TYPE_NAME (assign | IDENT)
* stmts_block -> SEP_L_BRACE stmt+ SEP_R_BRACE
    * stmt -> decl_var | assign | func | ifstmt | ternary | loop      $$ | return | io_console | break\KW_BREAK   + SEP_END_LINE
        * assign -> IDENT ASSIGN_OP value
            * value -> B_NOT? (IDENT | INT | STRING | func | operation | KW_LOGIC_TRUE | KW_LOGIC_FALSE | ternary)
                * func -> FUNC_NAME SEP_L_BRACKET arg_list? SEP_R_BRACKET          $$ (FUNC_NAME | (IDENT (SEP_DOT FUNC_NAME)*))
                    * arg_list -> value (SEP_COMMA arg_list)?         // (SEP_COMMA value)*
                * operation -> bracket_optn | (optn_math | optn_bin | optn_comp)
                    * bracket_optn -> SEP_L_BRACKET operation SEP_R_BRACKET
                    * optn_math -> value math_opr value
                        * math_opr -> POW_OP | MUL_OP | DIV_OP | REM_OP | ADD_OP | SUB_OP
                    * optn_bin -> value bin_opr value
                        * bin_opr -> KW_LOGIC_AND | KW_LOGIC_OR | B_AND | B_XOR | B_OR | LOGIC_AND | LOGIC_OR
                    * optn_comp -> value comp_opr value
                        * comp_opr -> COMP_VAL | COMP_EQL         // Не все реализованы
        * ifstmt -> KW_IF conditions_block stmts_block elsestmt?
            * conditions_block -> SEP_L_BRACKET condition SEP_R_BRACKET
                * condition -> value
            * elsestmt -> KW_ELSE (ifstmt | stmts_block)
        * $$ ternary -> conditions_block SEP_QUE_MARK (value | ternary) SEP_COLON (value | ternary)
        * loop -> while | do_while | for
            * while -> KW_WHILE conditions_block stmts_block
            * do_while -> KW_DO stmts_block KW_WHILE conditions_block
            * for ->  KW_FOR SEP_L_BRACKET for_init? SEP_SEMICOLON condition? SEP_SEMICOLON for_incr? SEP_R_BRACKET stmts_block
                * for_init -> TYPE_NAME? assign           $$ ???
                * for_incr -> assign          $$ ???
        * $$ return -> KW_RETURN value?
* ...
* Заметки
  * ==== B_NOT ==== ? ==== B_NOT? value
  * ==== SPACE добавить везде... ==== Сделать отдельную версию на копии ==== ?
  * (НЕТ)

___
# Попытки в грамматику
___
## 04.04.2022

Грамматика:
* бесконечные скобки
* for, while, if 
* функция (с поддержкой вложенных функций в параметрах)

* lang -> expr+
    * expr -> init_expr | kw_expr | func_expr | func_decl
        * init_expr -> type? IDENT ASSIGN_OP init_value
            * type -> 
            * init_value -> br_expr | nobr_expr
                * br_expr -> (SEP_L_BRACKET init_value SEP_R_BRACKET)
                * nobr_expr -> (value (op_line init_value)*)
                    * value -> IDENT | INT | STRING | func_expr
                    * op_line -> op_arithmetic | op_compare
        * kw_expr -> kw_while_expr | ...
            * kw_while_expr -> KW_WHILE br_expr SEP_L_BRACE expr+ SEP_R_BRACE
        * func_expr -> IDENT SEP_L_BRACKET parameters? SEP_R_BRACKET
            * parameters -> (value SEP_COMMA)*
        * func_decl -> KW_FUNC IDENT SEP_L_BRACKET parameters_list? SEP_R_BRACKET SEP_L_BRACE expr+ SEP_R_BRACE

### Попытки в for и бесконечные скобки - Nasway:
* lang -> expr+
* expr -> init | for
    * init -> VAR ASSIGN_OP expr_value
    * for -> FOR_KW L_BRACKET (init?|value?) DIV condition? DIV init? R_BRACKET block
        * block -> (L_BRACE expr* R_BRACE) | expr
        * condition -> value (LESS | MORE | EQ) value
        * expr_value -> value (OP value)*
            * value -> VAR | DIGIT
    * br_expr -> (value OP)* L_BRACKET (br_expr|expr_value)+ R_BRACKET (OP value)*

___
## chursinov (Github):
* lang -> expr+
* expr -> (if|while_do|do_while) (WHIlE LB condition RB)? ASSIGN_OP (expr_val)+ ENDLINE
    * if -> IF LB condition RB
        * condition -> VAR COMPARISON_OP (expr_val)+
            * expr_val -> value | OP_VALUE
                * value -> VAR | DIGIT
    * while_do -> WHILE LB condition RB
    * do_while -> DO

___
## Тёма:
* lang -> expr+
* expr -> (expr_assign | if_op | while_op | for_op | func) NEXTCOM
    * expr_assign -> VAR ASSIGN_OP expr_value
        * expr_value -> (value | expr_br) (OP value)*
            * expr_br -> L_BRACKET expr_value R_BRACKET
            * value -> VAR | DIGIT
    * if_op -> IF L_BRACKET condition R_BRACKET body (else_op){0,1}
        * body -> L_BRACE expr R_BRACE
            * else_op -> ELSE body
        * condition -> VAR COMP_OP value
    * while_op -> WHILE L_BRACKET condition R_BRACKET body
    * func -> FUNC_NAME L_BRACKET (func_param)* R_BRACKET body
        * func_param -> (VAR | DIV func_param)*

___
## Наброски:

1) Выражения
   1) Выражение в скобках (поддержка бесконечных скобок)
      1) Выражение без скобок
         1) Вызов функции, обращение к переменной, операции
   2) Циклы
2) Циклы
   1) Цикл for
      1) KW for
      2) инициализация цикла for
      3) тело цикла
   2) Цикл while
      1) KW while
      2) инициализация цикла while
      3) тело цикла
3) Условные конструкции
   1) условие if
      1) else
4) Переменные
   1) Объявление и инициализация переменной
      1) Объявление переменной
      2) Инициализация переменной
   2) Обращение к переменной
5) Функции
   1) Описание функции
      1) Набор параметров
   2) Вызов функции (с поддержкой вызова внутри списка аргументов)
      1) Набор аргументов
6) 

///
* expr -> declaration | initialization | action
    * declaration -> decl_class | decl_func | decl_var
    * initialization -> decl_var? init_var
///


1) Объявить (declare)
   1) класс, функция, переменная
2) Инициализировать (initialize)
3) Выполнить остальное

* ! main_func
*  
* func_decl
* param_decl
* stmt_decl
* var_decl -> type    variable
*  
* func_list -> func_def+
* func_def -> type    funcname    param_list  block
*  
* block -> var_decl_list  stmt_list
* var_decl_list -> var_decl+
* stmt_list ->    assign_stmt+
    * assign_stmt -> variable  operation
    * operation -> 
*  
* binary_operator
* compare_operator
* math_operator
*  
* while, for
*  
* return









