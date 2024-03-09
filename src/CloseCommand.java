public class CloseCommand implements Command {
    private final FileOperations fileOperations;

    public CloseCommand(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }

    public void execute(String[] args) {
        fileOperations.close();
    }
}
