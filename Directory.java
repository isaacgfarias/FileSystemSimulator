import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Directory extends Archive{
    private List<Archive> elements;

    public Directory(String name) {
        super(name);
        verifyName();
        this.elements = new ArrayList<Archive>(); 
    }

    private void verifyName() {
        String regex = "^(.+)(?=\\.[^.]+$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.getName());

        if (matcher.find()) this.setName(matcher.group());
    }

    public void addElement(Archive a) {
        this.elements.add(a);
    }

    public void listElements() {
        for(Archive a : this.elements) {
            System.out.println(a.getiNode() + " - " + a.getName());
        }
    }

    public void listElements(String type) {
        switch (type.toLowerCase()) {
            case "file":
                for(Archive a : this.elements) {
                    if (a.getClass() == File.class) System.out.println(a.getiNode() + " - " + a.getName());
                }
                break;
            case "directory":
                for(Archive a : this.elements) {
                    if (a.getClass() == Directory.class) System.out.println(a.getiNode() + " - " + a.getName());
                }
                break;

        }
    }

    public void removeElement(int node) {
        for (Archive a : this.elements) {
            if (node == a.getiNode()) this.elements.remove(a);
        }
    }




    
}
