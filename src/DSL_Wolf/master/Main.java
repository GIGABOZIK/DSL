package DSL_Wolf.master;
import java.io.FileReader;
import java.io.IOException;
public class Main {
   // Загороднев Дмитрий ИВБО-03-20
   
   // Крутые цвета для программы :)
   public static final String ANSI_RESET = "\u001B[0m";
   public static final String ANSI_BLACK = "\u001B[30m";
   public static final String ANSI_RED = "\u001B[31m";
   public static final String ANSI_GREEN = "\u001B[32m";
   public static final String ANSI_YELLOW = "\u001B[33m";
   public static final String ANSI_BLUE = "\u001B[34m";
   public static final String ANSI_PURPLE = "\u001B[35m";
   public static final String ANSI_CYAN = "\u001B[36m";
   public static final String ANSI_WHITE = "\u001B[37m";  

	public static String checkFile(){
		String code="";
		try(FileReader reader = new FileReader("src/DSL_Wolf/Code.txt"))
		{
			int c;
			while((c=reader.read())!=-1){

				code=code.concat(String.valueOf((char)c));
			}
		}
		catch(IOException ex){
			System.out.println(ex.getMessage());
		}
		return code;
	}

    public static void main(String[] args) {
		String s=checkFile();
		System.out.println(ANSI_BLUE + "\n\nИсходный код программы:" + ANSI_WHITE);
		System.out.println(s);
		System.out.printf("\n" + ANSI_BLUE + "\nЗначения лексера:" + ANSI_WHITE);
		Lexer lexer=new Lexer(s);
		Parser parser=new Parser(lexer.analyze());
		System.out.println("\n" + ANSI_PURPLE + "[n] " + ANSI_WHITE + "- номер токена данных." + ANSI_WHITE);
		System.out.println(ANSI_BLUE + "\nВычисления интерпретатора:" + ANSI_WHITE);
		RootNode root = parser.parseTokens();
		Interpreter interpreter = new Interpreter();
		for(int i = 0; i < root.codeStr.size(); i++) {
			interpreter.run(root.codeStr.get(i));
		}
		/*
		* Interpreter itprt = new Interpreter(root);
		* itprt.run();
		*
		* for (rootP : root.codeStr) {
		* 	interpreter.run(rootP);
		* }
		* */
    }
}
