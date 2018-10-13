package org.xstefank.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserList {

    private List<String> list = new ArrayList<>();
    private File file;

    public UserList(String dirName, String fileName) {
        if (dirName != null && fileName != null) {
            this.file = new File(dirName, fileName);
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line = br.readLine();
                    while (line != null) {
                        if (!line.startsWith("#")) {
                            list.add(line);
                        }
                        line = br.readLine();
                    }
                } catch (IOException e) {
                    throw new IllegalStateException("Cannot read file " + fileName, e);
                }
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new IllegalStateException("Cannot create file with name " + fileName, e);
                }
            }
        } else {
            throw new NullPointerException("Parameters in PersistentList cannot be null");
        }
    }

    public void addUser(String username){
        if (list.add(username)) {
                try (FileWriter fw = new FileWriter(file, true)){
                    fw.write(username + "\n");
                } catch (IOException e) {
                    throw new IllegalStateException("Cannot write user to file", e);
                }
            }
    }

    public void removeUser(String username) {
        if (list.remove(username)) {
            try (FileWriter fw = new FileWriter(file)) {
                for (String listUsername : list) {
                    fw.write(listUsername + "\n");
                }
            } catch (IOException e) {
                throw new IllegalStateException("Cannot remove user from file", e);
            }
        }
    }

    public boolean hasUsername(String username) {
        return list.contains(username);
    }

    public String getFilePath() {
        return file.getPath();
    }
}
