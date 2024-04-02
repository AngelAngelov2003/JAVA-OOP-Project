public interface FileOperations {
    void open(String filePath);
    void close();
    void save(String fileContent);
    void saveAs(String fileName);
    boolean isOpen();
}
