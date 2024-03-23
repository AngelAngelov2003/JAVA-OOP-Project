import java.io.*;

public class OpenCommand implements Command {

//    private final FileOperations fileOperations;
//
//    public OpenCommand(FileOperations fileOperations) {
//        this.fileOperations = fileOperations;
//    }
//
//    public void execute(String[] args) {
//        if (args.length != 2) {
//            System.out.println("Usage: open <file>");
//            return;
//        }
//        String fileName = args[1];
//        fileOperations.open(fileName);
//    }

    private final FileOperations fileOperations;

    public OpenCommand(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }

    public void execute(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: open <file>");
            return;
        }
        String fileName = args[1];
        openFile(fileName);
    }

    private void openFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder content = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                content.append(line).append("\n");
            }

            System.out.println("File content:\n" + content);
            fileOperations.open(fileName);
        } catch (IOException e) {
            System.out.println("This file does not exist. Creating the file:\n");
            try {
               if(new File(fileName).createNewFile()) {
                    System.out.println("File created successfully!");
                    fileOperations.open(fileName);
               }
            } catch (IOException ex) {
                System.out.println("Error: Unable to create the wanted file.");
            }
        }
    }
}
