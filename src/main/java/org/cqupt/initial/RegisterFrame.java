package org.cqupt.initial;

import org.cqupt.bean.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.cqupt.Main.userList;


public class RegisterFrame extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField; // 新增再次输入密码字段


    public RegisterFrame() {
        // 设置窗口标题
        setTitle("用户注册");
        // 设置窗口大小
        setSize(400, 350); // 增加窗口高度以适应新增的字段
        // 设置窗口居中
        setLocationRelativeTo(null);
        // 设置默认关闭操作
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置布局
        setLayout(new GridBagLayout());
        setAlwaysOnTop(true);

        // 添加注册界面标签和输入框
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // 欢迎标签
        JLabel welcomeLabel = new JLabel("欢迎注册");
        welcomeLabel.setFont(new Font("华文行楷", Font.BOLD, 24));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(welcomeLabel, constraints);

        // 用户名标签
        JLabel usernameLabel = new JLabel("用户名:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        add(usernameLabel, constraints);

        // 用户名输入框
        usernameField = new JTextField(15);
        constraints.gridx = 1;
        add(usernameField, constraints);

        // 密码标签
        JLabel passwordLabel = new JLabel("密码:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(passwordLabel, constraints);

        // 密码输入框
        passwordField = new JPasswordField(15);
        constraints.gridx = 1;
        add(passwordField, constraints);

        // 再次输入密码标签
        JLabel confirmPasswordLabel = new JLabel("确认密码:"); // 新增确认密码标签
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(confirmPasswordLabel, constraints);

        // 再次输入密码框
        confirmPasswordField = new JPasswordField(15); // 新增确认密码输入框
        constraints.gridx = 1;
        add(confirmPasswordField, constraints);

        // 注册按钮
        JButton registerButton = new JButton("注册");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        add(registerButton, constraints);

        // 注册按钮事件监听
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerUser();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    // 加载用户数据

    // 注册用户方法
    private void registerUser() throws IOException {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        // 获取确认密码

        if (username.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！",
                    "注册失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 检查两次输入的密码是否一致
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致！",
                    "注册失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 检查用户名是否已存在
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(this, "用户名已存在！", "注册失败",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 创建新用户对象并保存
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userList.add(newUser);

        // 将新用户信息写入文件
        BufferedWriter bw = new BufferedWriter(
                new FileWriter("user_info.txt", true)); // true表示追加
        bw.write(username + "\t\t" + password);
        bw.newLine();
        bw.flush();
        bw.close();

        JOptionPane.showMessageDialog(this, "注册成功！", "注册",
                JOptionPane.INFORMATION_MESSAGE);
        // 关闭当前的 JFrame
        this.dispose();
    }

}
