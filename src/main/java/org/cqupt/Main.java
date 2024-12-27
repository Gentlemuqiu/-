package org.cqupt;

import org.cqupt.bean.User;
import org.cqupt.initial.LoginJFrame;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static List<User> userList = new ArrayList<>();

    public static void main(String[] args) {
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            LoginJFrame loginJFrame = new LoginJFrame();
            loginJFrame.setVisible(true);
        });


    }

    private static void loadData() throws IOException {
        File file = new File("user_info.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] str = line.split("\t\t");
            User user = new User();
            user.setUsername(str[0]);
            user.setPassword(str[1]);
            Main.userList.add(user);
        }
        br.close();
    }

}