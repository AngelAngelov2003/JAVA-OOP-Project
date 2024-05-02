import java.util.Arrays;

public class SetCommand implements Command{
    private final TextEditor textEditor;

    public SetCommand(TextEditor textEditor) {
        this.textEditor = textEditor;
    }

    @Override
    public void execute(String[] args) {
        if (textEditor.getXmlParser() != null) {
            try {
                String id = args[1];
                String key = args[2];
                String value = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                textEditor.getXmlParser().set(id, key, value);
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        } else {
            System.out.println("No XML file is currently open.");
        }
    }
}
