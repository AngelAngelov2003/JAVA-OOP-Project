public class SelectCommand implements Command{
    private final TextEditor textEditor;

    public SelectCommand(TextEditor textEditor) {
        this.textEditor = textEditor;
    }

    @Override
    public void execute(String[] args) {
        if (textEditor.getXmlParser() != null) {
            try {
                String id = args[1];
                String key = args[2];
                String value = textEditor.getXmlParser().select(id, key);
                System.out.println("Value for id " + id + " and key " + key + ": " + value);
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } else {
            System.out.println("No XML file is currently open.");
        }
    }
}
