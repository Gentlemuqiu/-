package org.cqupt.initial;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.cqupt.game.GameJFrame;
import org.cqupt.Main;
import org.cqupt.bean.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class LoginJFrame extends JFrame implements MouseListener {


    JButton login = new JButton();
    JButton register = new JButton();
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JTextField code = new JTextField();
    JCheckBox agreeCheckBox =
            new JCheckBox("已阅读并同意服务协议和斗地主隐私保护指引");

    //正确的验证码
    JLabel rightCode = new JLabel();


    public LoginJFrame() {
        //初始化界面
        initJFrame();
        //在这个界面中添加内容
        initView();
        //让当前界面显示出来
        this.setVisible(true);
    }

    private static String getCode() {
        // 1. 定义字母和数字字符集合
        String chars =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random r = new Random();
        StringBuilder result = new StringBuilder();

        // 2. 随机生成5个字符（包括字母和数字）
        for (int i = 0; i < 5; i++) {
            result.append(chars.charAt(r.nextInt(chars.length())));
        }

        return result.toString(); // 返回生成的验证码
    }

    private void initView() {
        // 创建 "重邮欢迎你" 标签
        JLabel welcomeLabel =
                new JLabel("欢迎来到重邮斗地主", SwingConstants.CENTER);
        // 居中对齐
        welcomeLabel.setFont(
                new Font("华文行楷", Font.BOLD, 24));
        // 设置字体为加粗，大小为24
        welcomeLabel.setForeground(new Color(100, 149, 237));
        // 设置颜色为蓝色
        welcomeLabel.setBounds(0, 20, 400, 50);
        // 设置标签的大小和位置，居于顶部
        this.getContentPane().add(welcomeLabel);

        JLabel usernameLabel = new JLabel("用户名");
        usernameLabel.setFont(new Font("华文行楷", Font.PLAIN, 18));
        usernameLabel.setBounds(30, 100, 70, 40);
        // 设置标签的位置和大小
        this.getContentPane().add(usernameLabel);

        username.setBounds(100, 100, 200, 40);
        username.setFont(new Font("华文行楷", Font.PLAIN, 18));
        username.setHorizontalAlignment(JTextField.CENTER); // 内容居中
        username.setBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        // 设置边框
        this.getContentPane().add(username);

        // 创建密码标签
        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setFont(new Font("华文行楷", Font.PLAIN, 18));
        passwordLabel.setBounds(30, 160, 70, 40);
        // 设置标签的位置和大小
        this.getContentPane().add(passwordLabel);
        password.setBounds(100, 160, 200, 40);
        password.setFont(new Font("Serif", Font.PLAIN, 18));
        password.setHorizontalAlignment(JPasswordField.CENTER);
        password.setBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        this.getContentPane().add(password);

        // 创建验证码标签
        JLabel codeLabel = new JLabel("验证码");
        codeLabel.setFont(new Font("华文行楷", Font.PLAIN, 18));
        codeLabel.setBounds(30, 220, 70, 40);
        // 设置标签的位置和大小
        this.getContentPane().add(codeLabel);
        code.setBounds(100, 220, 200, 40);
        code.setFont(new Font("Serif", Font.PLAIN, 18));
        code.setHorizontalAlignment(JPasswordField.CENTER);
        code.setBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        this.getContentPane().add(code);


        //获取正确的验证码
        String codeStr = getCode();
        Font rightCodeFont = new Font("Serif", Font.PLAIN, 15);
        //设置颜色
        rightCode.setForeground(Color.RED);
        //设置字体
        rightCode.setFont(rightCodeFont);
        //设置内容
        rightCode.setText(codeStr);
        //绑定鼠标事件
        rightCode.addMouseListener(this);
        //位置和宽高
        rightCode.setBounds(320, 225, 100, 30);
        //添加到界面
        this.getContentPane().add(rightCode);


        agreeCheckBox.setBounds(50, 280, 300, 30);
        agreeCheckBox.setFont(new Font("华文行楷", Font.PLAIN, 12));
        agreeCheckBox.setBorderPainted(false);
        this.getContentPane().add(agreeCheckBox);

        login.setBounds(50, 340, 100, 50);
        login.setText("登录");
        login.setFont(new Font("华文行楷", Font.BOLD, 18));
        login.setBackground(new Color(173, 216, 237)); // 设置按钮背景颜色
        login.setForeground(new Color(83, 156, 107)); // 按钮文字颜色
        login.setFocusPainted(false); // 去除点击时的焦点框
        login.setBorder(
                BorderFactory.createLineBorder(
                        new Color(100, 149, 237), 1));
        login.addMouseListener(this);
        this.getContentPane().add(login);


        register.setBounds(250, 340, 100, 50);
        register.setText("注册");
        register.setFont(new Font("华文行楷", Font.BOLD, 18));
        register.setBackground(new Color(173, 216, 237)); // 设置按钮背景颜色
        register.setForeground(new Color(83, 156, 107)); // 按钮文字颜色
        register.setFocusPainted(false); // 去除点击时的焦点框
        register.setBorder(
                BorderFactory.createLineBorder(
                        new Color(100, 149, 237), 1));
        register.addMouseListener(this);
        this.getContentPane().add(register);

        // 设置背景渐变色
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(255, 182, 193); // 粉色
                Color color2 = new Color(173, 216, 230); // 淡蓝色
                GradientPaint gp =
                        new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        backgroundPanel.setBounds(0, 0, 400, 500);
        this.getContentPane().add(backgroundPanel);
    }

    private void initJFrame() {
        // 设置窗口的大小、标题等属性
        this.setSize(400, 500);
        this.setTitle("斗地主APP");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setLayout(null);
    }

    //点击
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == login) {
            //获取两个文本输入框中的内容
            String usernameInput = username.getText();
            String passwordInput = password.getText();
            //获取用户输入的验证码
            String codeInput = code.getText();


            //判断验证码是否为空
            if (codeInput.isEmpty()) {
                showJDialog("验证码不能为空");
                return;
            }

            //判断用户名和密码是否为空
            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                showJDialog("用户名或者密码为空");
                return;
            }

            //判断验证码是否正确
            if (!codeInput.equalsIgnoreCase(rightCode.getText())) {
                showJDialog("验证码输入错误");
                return;
            }
            if (!agreeCheckBox.isSelected()) {
                showJDialog("请同意相关协议");
                return;
            }

            //判断集合中是否包含当前用户对象
            //其实就是验证用户名和密码是否相同
            //contains底层是依赖equals方法判断的，所以需要重写equals方法
            User userInfo = new User(usernameInput, passwordInput);
            if (Main.userList.contains(userInfo)) {
                //关闭当前登录界面
                this.setVisible(false);
                FlatDarculaLaf.setup();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    GameJFrame gameJFrame = new GameJFrame();
                    gameJFrame.setVisible(true);
                });
            } else {
                showJDialog("用户名或密码错误");
            }
        } else if (e.getSource() == register) {
            SwingUtilities.invokeLater(() -> {
                RegisterFrame registerFrame = new RegisterFrame();
                registerFrame.setVisible(true);
            });
        } else if (e.getSource() == rightCode) {
            String code = getCode();
            rightCode.setText(code);
        }
    }

    private void showJDialog(String content) {
        // 创建一个弹框对象
        JDialog jDialog = new JDialog();
        // 设置弹框大小
        jDialog.setSize(300, 180);
        // 弹框置顶
        jDialog.setAlwaysOnTop(true);
        // 弹框居中
        jDialog.setLocationRelativeTo(null);
        // 弹框为模态
        jDialog.setModal(true);

        // 设置弹框背景色和布局
        jDialog.getContentPane()
                .setBackground(new Color(245, 245, 245)); // 淡灰色背景
        jDialog.setLayout(new GridBagLayout()); // 使用网格布局

        // 创建JLabel对象管理文字并添加到弹框中
        JLabel warning = new JLabel(content, SwingConstants.CENTER); // 内容居中
        warning.setFont(new Font("华文行楷", Font.BOLD, 16)); // 设置字体样式
        warning.setForeground(new Color(255, 69, 0)); // 设置字体颜色为橙色
        warning.setOpaque(true);
        warning.setBackground(new Color(245, 245, 245));

        // 创建一个面板来容纳提示信息
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(245, 245, 245));
        // 设置面板背景
        messagePanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // 内边距
        messagePanel.add(warning);

        // 将面板添加到弹框
        jDialog.add(messagePanel, new GridBagConstraints());

        // 设置一个确认按钮并添加
        JButton okButton = new JButton("确认");
        okButton.setFont(new Font("华文行楷", Font.PLAIN, 14));
        okButton.setFocusPainted(false);
        okButton.setBackground(new Color(100, 149, 237));
        // 设置按钮背景颜色
        okButton.setForeground(Color.WHITE); // 按钮文字颜色
        okButton.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 5, 15));
        // 按钮的内边距

        // 添加按钮点击事件，关闭弹窗
        okButton.addActionListener(e -> jDialog.dispose());

        // 将按钮添加到弹框
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(okButton);

        // 添加按钮面板到弹框
        jDialog.add(buttonPanel, new GridBagConstraints());

        // 设置弹框可见
        jDialog.setVisible(true);
    }

    //按下不松
    @Override
    public void mousePressed(MouseEvent e) {

    }

    //松开按钮
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    //鼠标划入
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    //鼠标划出
    @Override
    public void mouseExited(MouseEvent e) {

    }

}