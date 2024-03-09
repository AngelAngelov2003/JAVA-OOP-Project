public class SaveAsCommand implements Command {
    private final FileOperations fileOperations;

    public SaveAsCommand(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }

    public void execute(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: saveas <file>");
            return;
        }
        String fileName = args[1];
        fileOperations.saveAs(fileName);
    }
}
