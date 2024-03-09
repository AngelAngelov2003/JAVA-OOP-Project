public class ExitCommand implements Command {
    public void execute(String[] args) {
        System.out.println("Exiting the program...");
        System.exit(0);
    }
}
