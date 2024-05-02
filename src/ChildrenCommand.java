import java.util.List;

public class ChildrenCommand implements Command{
    private final TextEditor textEditor;

    public ChildrenCommand(TextEditor textEditor) {
        this.textEditor = textEditor;
    }

    @Override
    public void execute(String[] args) {
        if (textEditor.getXmlParser() != null) {
            try {
                String id = args[1];
                List<String> children = textEditor.getXmlParser().children(id);
                if (children.isEmpty()) {
                    System.out.println("No children found for element with id " + id);
                } else {
                    System.out.println("Children of element with id " + id + ":");
                    for (String child : children) {
                        System.out.println(child);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } else {
            System.out.println("No XML file is currently open.");
        }
    }
}
