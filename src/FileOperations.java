public interface FileOperations {
    void open(String filePath);
    void close();
    void save();
    void saveAs(String fileName);
    boolean isOpen();
}
