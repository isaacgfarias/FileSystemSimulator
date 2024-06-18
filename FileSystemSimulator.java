import java.io.*;
import java.util.*;

public class FileSystemSimulator {
    private Set<Archive> disk;
    private Journal journal;

    public FileSystemSimulator() {
        disk = new HashSet<Archive>();
        journal = new Journal();
    }

    // Métodos de checagem privados
    private boolean alreadyExists(String path) {
        for (Archive a : disk) 
            if (a.getName() == path)
                return true;
        return false;
    }

    private boolean fileDirectoryExists(String filePath) {
        for (Archive a : disk) {
            if (a.getClass() == Directory.class)
                return filePath.substring(0, filePath.lastIndexOf("/")) == a.getName();
        }
        return false;
    }

    // método criando um diretório
    public void createDirectory(String path) {
        if (this.alreadyExists(path)) return;
        Directory newFolder = new Directory(path);
        disk.add(newFolder); //add diretório ao conjunto destes
        journal.addEntry("CREATE_DIRECTORY:" + path); // registrando a operação no journal
    }

    // método criando um arquivo e conteúdo
    public void createFile(String path, String content) {
        if (this.alreadyExists(path)) return;
        if (this.fileDirectoryExists(path)) {System.out.println("Directory not found!"); return;}
        File newFile = new File(path, content);
        disk.add(newFile); //add arquivo e seu conteúdo ao mapa destes
        journal.addEntry("CREATE_FILE:" + path + ":" + content);
    }

    // método listando conteúdo de um diretório
    public void listDirectory(String path) {
        // iterando sobre diretórios e imprimindo os que começam com o caminho dado
        for (Archive a : disk) {
            if (a.getClass() == Directory.class) {
                if (a.getName().startsWith(path + "/")) {
                    System.out.println("Directory: " + a.getName());
                }
            }
            if (a.getClass() == File.class) {
                if (a.getName().startsWith(path + "/")) {
                    System.out.println("File: " + a.getName());
                }
            }
        }
    }

    // método renomeando arquivos
    public void renameFile(String oldPath, String newPath) {
        boolean isIn = false;
        Archive oldFile = null;

        for (Archive f : disk) {
            if (f.getName() == oldPath) {
                isIn = true; 
                if (f.getClass() == File.class) oldFile = f;
            }
        }

        if (isIn) {
            String content = oldFile.getContent(); // remove antigo e pega seu conteúdo
            disk.add(new File(newPath, content)); // add arquivo com novo caminho e conteúdo
            journal.addEntry("RENAME_FILE:" + oldPath + ":" + newPath);
        }
    }

    // método renomeando diretórios
    public void renameDirectory(String oldPath, String newPath) {
        Set<Archive> directoriesToRemove = new HashSet<>();
        Set<Archive> directoriesToAdd = new HashSet<>();

        // renomeando diretórios que correspondem ao caminho dado
        for (Archive dir : disk) {
            if (dir.getName().equals(oldPath) || dir.getName().startsWith(oldPath + "/")) {
                Archive renamedDir = dir;
                renamedDir.setName(dir.getName().replaceFirst("^" + oldPath, newPath));
                directoriesToRemove.add(dir); // marcando o antigo para remover
                directoriesToAdd.add(renamedDir); // add o diretório novo
            }
        }

        // atualizando diretórios
        disk.removeAll(directoriesToRemove);
        disk.addAll(directoriesToAdd);
        journal.addEntry("RENAME_DIRECTORY:" + oldPath + ":" + newPath); // registrando a operação no journal

        Set<File> renamedFiles = new HashSet<>();
        String filePath = null;
        for (Archive f : disk) {
            filePath = f.getName();
            if (filePath.startsWith(oldPath + "/")) {
                String newFilePath = filePath.replaceFirst("^" + oldPath, newPath);
                renamedFiles.add(new File(newFilePath, f.getContent()));
            }
        }

        // atualizando arquivos
        for (Archive f : disk) {
            if (f.getName().startsWith(oldPath + "/")) disk.remove(f);
        }
        disk.addAll(renamedFiles);
            
    }

    // método copiando arquivos
    public void copyFile(String sourcePath, String destinationPath) {
        Archive targetFile = null;
        boolean isContained = false;
        for (Archive f : disk) if (f.getName().equals(sourcePath)) {
            isContained = true;
            targetFile = f;
        }
        if (isContained) {
            String content = targetFile.getContent(); // pegando conteúdo do arquivo original
            disk.add(new File(destinationPath, content)); // copiando conteúdo para o novo caminho
            journal.addEntry("COPY_FILE:" + sourcePath + ":" + destinationPath);
        }
    }

    // método salvando o estado do sistema de arquivos no journal
    public void saveToFile(String filename) throws IOException {
        journal.saveToFile(filename); // salvando operações journal nos arquivos
    }

    // método carregando estados anteriores do sistema de arquivos a partir do journal
    public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
        journal.loadFromFile(filename); // carregando operações antigas de um arquivo
        
        String fileContent = "";
        String action, path = "";
        String[] parts = null;

        // reinicializando a estrutura do sistema
        for (String entry : journal.getEntries()) {
            parts = entry.split(":");
            action = parts[0];
            path = parts[1];


            switch (action) {
                case "CREATE_DIRECTORY":
                    disk.add(new Directory(path)); // add diretorio criado
                    break;
                case "CREATE_FILE":
                    String content = parts[2];
                    disk.add(new File(path, content)); // add arquivo e seu conteúdo
                    break;
                case "RENAME_FILE":
                    String oldPath = path;
                    String newPath = parts[2];

                    boolean isOldPathContained = false;
                    Archive oldPathFile = null;
                    for (Archive f : disk) {
                        if (f.getName().equals(oldPath)) 
                                isOldPathContained = true; oldPathFile = f;
                    }
                    
                    if (isOldPathContained) {
                        fileContent = oldPathFile.getContent(); // remove arquivos antigos
                        disk.remove(oldPathFile);
                        disk.add(new File(newPath, fileContent)); // add arquivos novos e caminhos
                    }
                    break;
                case "RENAME_DIRECTORY": // renomeando diretórios
                    String oldDirPath = path;
                    String newDirPath = parts[2];
                    Set<Archive> updatedDirs = new HashSet<>();
                    for (Archive dir : disk) {
                        if (dir.getName().equals(oldDirPath) || dir.getName().startsWith(oldDirPath + "/")) {
                            Archive renamedDir = dir;
                            renamedDir.setName(dir.getName().replaceFirst("^" + oldDirPath, newDirPath));
                            updatedDirs.add(renamedDir);
                        } else {
                            updatedDirs.add(dir);
                        }
                    }
                    disk = updatedDirs; // atualizando novos diretórios(renomeados)
                    break;
                case "COPY_FILE":
                    String sourcePath = path;
                    String destPath = parts[2];

                    boolean isSourcePathContained = false;
                    Archive SourcePathFile = null;
                    for (Archive f : disk) {
                        if (f.getName().equals(sourcePath)) 
                            isSourcePathContained = true; SourcePathFile = f;
                    }

                    if (isSourcePathContained) {
                        fileContent = SourcePathFile.getContent(); // pega conteúdos dos arquivos originais
                        disk.add(new File(destPath, fileContent)); // copiando para novo caminho
                    }
                    break;
                default:
                    System.out.println("Ação não reconhecida: " + action);
                    break;
            }
        }
    }

    // método apagando arquivos
    public void deleteFile(String path) {
        Iterator<Archive> iterator = disk.iterator();
        while (iterator.hasNext()) {
            Archive f = iterator.next();
            if (f.getName().equals(path)) {
                iterator.remove();
            }
        }
        journal.addEntry("DELETE_FILE:" + path);
    }
    

    // método deletando diretórios e seus arquivos contidos
    public void deleteDirectory(String path) {
        Set<Archive> filesToRemove = new HashSet<>();
        for (Archive file : disk) {
            if (file.getName().startsWith(path + "/")) {
                filesToRemove.add(file); // marcando arquivos para remover
                journal.addEntry("DELETE_FILE:" + file);
            }
        }
        disk.removeAll(filesToRemove); // removendo todos os arquivos

        Set<Archive> directoriesToRemove = new HashSet<>();
        for (Archive dir : disk) {
            if (dir.getName().equals(path) || dir.getName().startsWith(path + "/")) {
                directoriesToRemove.add(dir); // marcando diretório para remover
                journal.addEntry("DELETE_DIRECTORY:" + dir);
            }
        }
        disk.removeAll(directoriesToRemove); // removendo todos os diretórios
    }


}