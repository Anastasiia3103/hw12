package hw12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileManager {
    private static Path currentDirectory = Paths.get (System.getProperty ("user.dir"));

    public static void main (String[] args) {
        try (BufferedReader reader = new BufferedReader (new InputStreamReader (System.in))) {
            printPrompt ();
            String line;
            while ((line = reader.readLine ()) != null) {
                executeCommand (line.trim ());
                printPrompt ();
            }
        } catch (IOException e) {
            System.out.println ("An error occurred: " + e.getMessage ());
        }
    }

    private static void executeCommand (String command) {
        String[] tokens = command.split (" ");
        String action = tokens[0];

        switch (action) {
            case "cd":
                if (tokens.length > 1){
                    changeDirectory (tokens[1]);
                }
                else {
                    System.out.println ("Usage: cd <directory>");
                }
                break;
            case "cp":
                if (tokens.length > 2){
                    copyFile (tokens[1], tokens[2]);
                }
                else {
                    System.out.println ("Usage: cp <source> <destination>");
                }
                break;
            case "ls":
                listFiles ();
                break;
            case "pwd":
                printWorkingDirectory ();
                break;
            case "exit":
                System.exit (0);
                break;
            default:
                System.out.println ("Invalid command");
        }
    }

    private static void changeDirectory (String directory) {
        Path newDir;
        if (directory.equals ("..")){
            newDir = currentDirectory.getParent ();
        }
        else if (directory.startsWith ("/")){
            newDir = Paths.get (directory);
        }
        else {
            newDir = currentDirectory.resolve (directory);
        }

        if (Files.isDirectory (newDir)){
            currentDirectory = newDir;
        }
        else {
            System.out.println ("Directory not found: " + newDir);
        }
    }

    private static void copyFile (String source, String destination) {
        Path sourcePath = currentDirectory.resolve (source);
        Path destPath = currentDirectory.resolve (destination);

        try {
            Files.copy (sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println ("File copied successfully");
        } catch (IOException e) {
            System.out.println ("An error occurred while copying the file: " + e.getMessage ());
        }
    }

    private static void listFiles () {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream (currentDirectory)) {
            for (Path path : directoryStream) {
                BasicFileAttributes attrs = Files.readAttributes (path, BasicFileAttributes.class);
                String fileType = attrs.isDirectory () ? "Directory" : "File";
                System.out.println (fileType + "\t" + path.getFileName ());
            }
        } catch (IOException e) {
            System.out.println ("An error occurred while listing files: " + e.getMessage ());
        }
    }

    private static void printWorkingDirectory () {
        System.out.println (currentDirectory);
    }

    private static void printPrompt () {
        System.out.print (currentDirectory + "> ");
    }
}


