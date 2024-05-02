import java.io.*;

public class OpenCommand implements Command {
    private final FileOperations fileOperations;

    public OpenCommand(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }

    public void execute(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: open <file>");
            return;
        }
        String fileName = args[1];
        fileOperations.open(fileName);
    }
}
