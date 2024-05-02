public class HelpCommand implements Command {
    public void execute(String[] args) {
        System.out.println("The following commands are supported:");
        System.out.println("open <file>\topens <file>");
        System.out.println("close\t\tcloses currently opened file");
        System.out.println("save\t\tsaves the currently open file");
        System.out.println("saveas <file>\tsaves the currently open file as <file>");
        System.out.println("help\t\tprints this information");
        System.out.println("exit\t\texits the program");
        System.out.println("print\t\tprints the XML content");
        System.out.println("select <id> <key>\treturns an attribute value given an element id and attribute key");
        System.out.println("set <id> <key> <value>\tassigns a value to an attribute");
        System.out.println("children <id>\t\tlists the attributes of nested elements");
        System.out.println("child <id> <n>\t\taccesses the nth descendant of an element");
        System.out.println("text <id>\t\taccesses the text of an element");
        System.out.println("delete <id> <key>\tdeletes element attribute by key");
        System.out.println("newchild <id>\t\tadds a new descendant of an element");
        System.out.println("xpath <id> <XPath>\tperforms simple XPath 2.0 queries on an element");
    }
}
