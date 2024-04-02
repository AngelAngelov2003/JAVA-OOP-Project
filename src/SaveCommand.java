public class SaveCommand implements Command {
    private final FileOperations fileOperations;

    public SaveCommand(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }

    public void execute(String[] args) {

        fileOperations.save(args[1]);
    }
}