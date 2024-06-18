public class Main {
    public static void main(String[] args) {
        FileSystemSimulator simulator = new FileSystemSimulator();

        // criando diretórios e os arquivos
        simulator.createDirectory("/home");
        simulator.createDirectory("/home/isaac");
        simulator.createFile("/home/isaac/arquivo1.txt", "Conteúdo do arquivo 1");
        simulator.createFile("/home/isaac/arquivo2.txt", "Conteúdo do arquivo 2");

        // listando conteúdo do diretório
        System.out.println("Conteúdo de /home/isaac:");
        simulator.listDirectory("/home/isaac");
        System.out.println();

        // renomeando arquivo
        simulator.renameFile("/home/isaac/arquivo1.txt", "/home/isaac/novo.txt");

        // após mudança
        System.out.println("Conteúdo de /home/isaac após renomeação de arquivo1.txt para novo.txt:");
        simulator.listDirectory("/home/isaac");
        System.out.println();

        // copiando arquivo
        simulator.copyFile("/home/isaac/novo.txt", "/home/isaac/copiado.txt");

        // após mudança
        System.out.println("Conteúdo de /home/isaac após cópia de novo.txt para copiado.txt:");
        simulator.listDirectory("/home/isaac");
        System.out.println();

        // apagando arquivo
        simulator.deleteFile("/home/isaac/arquivo2.txt");

        // após mudança
        System.out.println("Conteúdo de /home/isaac após deleção de arquivo2.txt:");
        simulator.listDirectory("/home/isaac");
        System.out.println();

        // renomeando diretório
        simulator.renameDirectory("/home/isaac", "/home/izequiel");

        // após mudança
        System.out.println("Conteúdo de /home após renomeação de /home/isaac para /home/izequiel:");
        simulator.listDirectory("/home");
        System.out.println();

        // apagando diretório
        simulator.deleteDirectory("/home/izequiel");

        simulator.createFile("/home/Unico_Arquivo_Aqui_Agora.txt", "Teste final!");

        // após mudança
        System.out.println("Conteúdo de /home após deleção de /home/izequiel:");
        simulator.listDirectory("/home"); 
        System.out.println();
    }
}