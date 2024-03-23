import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveAsCommand implements Command {
    private final FileOperations fileOperations;
    private TextEditor textEditor;

    public SaveAsCommand(FileOperations fileOperations, TextEditor textEditor) {
        this.fileOperations = fileOperations;
        this.textEditor = textEditor;
    }

    public void execute(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: saveas <file>");
            return;
        }
        String fileName = args[1];
        fileOperations.saveAs(fileName);
        saveFile(fileName);
    }

    private void saveFile(String fileName) {
        if (!fileOperations.isOpen()) {
            System.out.println("No file is currently open.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String content = textEditor.getContent();
            writer.write(content);
            System.out.println("File saved as " + fileName);
        } catch (IOException e) {
            System.out.println("Error occurred while saving the file.");
            e.printStackTrace();
        }
    }
}
