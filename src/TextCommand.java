public class TextCommand implements Command {
    private final TextEditor textEditor;

    public TextCommand(TextEditor textEditor) {
        this.textEditor = textEditor;
    }

    @Override
    public void execute(String[] args) {
        if (textEditor.getXmlParser() != null) {
            try {
                String id = args[1];
                String text = textEditor.getXmlParser().text(id);
                if (text != null) {
                    System.out.println("Text of element with id " + id + ": " + text);
                } else {
                    System.out.println("No text found for element with id " + id);
                }
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } else {
            System.out.println("No XML file is currently open.");
        }
    }
}
