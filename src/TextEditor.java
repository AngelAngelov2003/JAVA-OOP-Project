import java.util.HashMap;
import java.util.Map;

public class TextEditor implements FileOperations {
    private String currentFile;
    private final Map<String, Command> commands;

    public TextEditor() {
        commands = new HashMap<>();
        commands.put("open", new OpenCommand(this));
        commands.put("close", new CloseCommand(this));
        commands.put("save", new SaveCommand(this));
        commands.put("saveas", new SaveAsCommand(this));
        commands.put("help", new HelpCommand());
        commands.put("exit", new ExitCommand());
    }

    @Override
    public void open(String fileName) {
        System.out.println("Successfully opened " + fileName);
        currentFile = fileName;
    }

    @Override
    public void close() {
        System.out.println("Successfully closed " + currentFile);
        currentFile = null;
    }

    @Override
    public void save() {
        System.out.println("Successfully saved " + currentFile);
    }

    @Override
    public void saveAs(String fileName) {
        System.out.println("Successfully saved as " + fileName);
        currentFile = fileName;
    }

    public void executeCommand(String commandLine) {
        String[] parts = commandLine.split(" ");
        String commandName = parts[0];
        Command command = commands.get(commandName);
        if (command == null) {
            System.out.println("Command not found. Type 'help' for available commands.");
            return;
        }
        command.execute(parts);
    }
}
