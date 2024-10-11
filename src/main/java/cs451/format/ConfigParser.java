package cs451.format;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigParser {

    private String path;
    private int nbMes;
    private int receiverId;

    public boolean populate(String value) {
        File file = new File(value);
        path = file.getPath();

        try (Scanner scanner = new Scanner(file)) {
            for (int i = 0; i < 2; i++) {
                if (scanner.hasNextInt()) {
                    if (i == 0)
                        nbMes = scanner.nextInt();
                    else
                        receiverId = scanner.nextInt();
                } else {
                    System.err.println("Problem with config file");
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Problem with config file");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getPath() {
        return path;
    }

    public int getNbMes() {
        return nbMes;
    }
    
    public int getReceiverId() {
        return receiverId;
    }
}
