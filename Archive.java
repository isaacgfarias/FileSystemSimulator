import java.util.Set;
import java.util.HashSet;
import java.util.Random;


public class Archive {
    private String name;
    private int iNode;
    private Set<Integer> existingINodes = new HashSet<Integer>();

    public Archive(String name) {
        this.setName(name);
        this.setiNode();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String text) {
        text.substring(0, text.lastIndexOf("/")).toLowerCase();
        this.name = text;
    }

    private void setiNode() {
        Random r = new Random();
        int chosenINode = 0;
        while (true) {
            chosenINode = r.nextInt(1000, 2000);
            if (!existingINodes.contains(chosenINode)) {
                this.iNode = chosenINode;
                break;
            } 
        }
    }

    public int getiNode() {
        return this.iNode;
    }

    public String getContent() { return ""; }    

    

}
