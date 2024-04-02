import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TextEditor implements FileOperations {
    private String currentFile;
    private String content;
    private final Map<String, Command> commands;

    public TextEditor() {
        commands = new HashMap<>();
        commands.put("open", new OpenCommand(this));
        commands.put("close", new CloseCommand(this));
        commands.put("save", new SaveCommand(this));
        commands.put("saveas", new SaveAsCommand(this, this));
        commands.put("help", new HelpCommand());
        commands.put("exit", new ExitCommand());
    }

    @Override
    public void open(String fileName) {

        System.out.println("Successfully opened " + fileName);
        currentFile = fileName;
        content = readFileContent(fileName);
    }

    @Override
    public void close() {
        System.out.println("Successfully closed " + currentFile);
        currentFile = null;
        content = null;
    }

    public void save(String fileContent) {
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile, true))) {
                writer.write(content);
                writer.newLine();
                System.out.println("Successfully saved content to " + currentFile);
            } catch (IOException e) {
                System.out.println("Error occurred while saving to the file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("No file is currently open.");
        }
    }

    @Override
    public void saveAs(String fileName) {
        System.out.println("Successfully saved as " + fileName);
        currentFile = fileName;
        if (content != null) {
            writeFileContent(fileName, content);
        } else {
            System.out.println("No content to save.");
        }
    }

    @Override
    public boolean isOpen() {
        return currentFile != null;
    }

    private String readFileContent(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            System.out.println("Error occurred while reading the file.");
            e.printStackTrace();
            return null;
        }
    }

    private void writeFileContent(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error occurred while writing the file.");
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
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
