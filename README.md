# DSL :// ;3
* somecode.txt - код для обработки
* src/
  * DSL/
    * descra_lang.md - описание грамматики
    * NODES/ - узлы синтаксического дерева
      * Node: UnOpNode, BinOpNode, IdNode, IntNode, StringNode, IfElseNode, WhileNode, ForNode (.java) 
    * TOKENS/ - токенs
      * TokenBase, Token (.java)
    * Main (.java)
      * CodeReader (.java)
    * Lexer (.java)
    * Parser (.java)
      * ParseException (.java)
    * Interpreter (.java)