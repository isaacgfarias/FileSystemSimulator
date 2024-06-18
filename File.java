
public class File extends Archive {
    // private String address;
    private String content;

    public File(String name, String content) {
        super(name);
        // if (this.getName().contains("/")) this.separateNameFromAddress();
        this.setContent(content);
    }

    // private void separateNameFromAddress() {
    //     String filePath = this.getName();
    //     String[] parts = filePath.split("/");
    //     this.setName(parts[parts.length - 1]);

    //     this.setName(filePath.split("/")[filePath.split("/").length - 1]);


    //     this.setAddress(filePath.substring(0, filePath.lastIndexOf("/")));
    // }

    // public void setAddress(String address) {
    //     this.address = address;
    // }

    // public String getAddress() {
    //     return this.address;
    // }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}