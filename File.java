
public class File extends Archive {
    // private String address;
    private String content;

    public File(String name, String content) {
        super(name);
        this.setContent(content);
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}