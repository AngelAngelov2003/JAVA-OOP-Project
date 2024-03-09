public class HelpCommand implements Command {
    public void execute(String[] args) {
        System.out.println("The following commands are supported:");
        System.out.println("open <file>\topens <file>");
        System.out.println("close\t\tcloses currently opened file");
        System.out.println("save\t\tsaves the currently open file");
        System.out.println("saveas <file>\tsaves the currently open file as <file>");
        System.out.println("help\t\tprints this information");
        System.out.println("exit\t\texits the program");
    }
}
