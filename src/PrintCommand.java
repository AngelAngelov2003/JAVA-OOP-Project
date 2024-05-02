public class PrintCommand implements Command{
    private final TextEditor textEditor;

    public PrintCommand(TextEditor textEditor) {
        this.textEditor = textEditor;
    }

    @Override
    public void execute(String[] args) {
        if (textEditor.getXmlParser() != null) {
            textEditor.getXmlParser().print();
        } else {
            System.out.println("No XML file is currently open.");
        }
    }
}
