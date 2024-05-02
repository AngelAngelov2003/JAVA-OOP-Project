public class ChildCommand implements Command{
    private final TextEditor textEditor;

    public ChildCommand(TextEditor textEditor) {
        this.textEditor = textEditor;
    }

    @Override
    public void execute(String[] args) {
        if (textEditor.getXmlParser() != null) {
            try {
                String id = args[1];
                int n = Integer.parseInt(args[2]);
                String child = textEditor.getXmlParser().child(id, n);
                if (child != null) {
                    System.out.println("The " + n + "th child of element with id " + id + " is: " + child);
                } else {
                    System.out.println("No child found at index " + n + " for element with id " + id);
                }
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } else {
            System.out.println("No XML file is currently open.");
        }
    }
}
