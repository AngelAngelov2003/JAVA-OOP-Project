import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome!");

        while (true){
            System.out.print("> ");
            String commandLine = scanner.nextLine();
            editor.executeCommand(commandLine);
        }
    }
}