public class Main {
    public static void main(String[] args) {
        FileSystemSimulator simulator = new FileSystemSimulator();

        simulator.createDirectory("/home");
        simulator.createDirectory("/home/isaac");
        simulator.createFile("/home/isaac/arquivo1.txt", "Conteúdo do arquivo 1");
        simulator.createFile("/home/isaac/arquivo2.txt", "Conteúdo do arquivo 2");

        System.out.println("Conteúdo de /home/isaac:");
        simulator.listDirectory("/home/isaac");
        System.out.println();

        simulator.renameFile("/home/isaac/arquivo1.txt", "/home/isaac/novo.txt");

        System.out.println("Conteúdo de /home/isaac após renomeação de arquivo1.txt para novo.txt:");
        simulator.listDirectory("/home/isaac");
        System.out.println();

        simulator.copyFile("/home/isaac/novo.txt", "/home/isaac/copiado.txt");

        System.out.println("Conteúdo de /home/isaac após cópia de novo.txt para copiado.txt:");
        simulator.listDirectory("/home/isaac");
        System.out.println();

        simulator.deleteFile("/home/isaac/arquivo2.txt");

        System.out.println("Conteúdo de /home/isaac após deleção de arquivo2.txt:");
        simulator.listDirectory("/home/isaac");
        System.out.println();

        simulator.renameDirectory("/home/isaac", "/home/izequiel");

        System.out.println("Conteúdo de /home após renomeação de /home/isaac para /home/izequiel:");
        simulator.listDirectory("/home");
        System.out.println();

        simulator.deleteDirectory("/home/izequiel");

        simulator.createFile("/home/Unico_Arquivo_Aqui_Agora.txt", "Teste final!");

        System.out.println("Conteúdo de /home após deleção de /home/izequiel:");
        simulator.listDirectory("/home"); 
        System.out.println();
    }
}