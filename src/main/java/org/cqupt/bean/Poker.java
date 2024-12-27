package org.cqupt.bean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Poker extends JLabel implements MouseListener {

    private static final Image scaledImage = new ImageIcon(
            "D:\\Code\\JAVA\\java_code" +
                    "\\FarmersFightLandlord\\src\\main\\" +
                    "java\\org\\cqupt\\image\\background.jpg").getImage()
            .getScaledInstance(71, 96, Image.SCALE_SMOOTH);

    //属性
    //1.牌的名字 格式： 数字- 数字
    private String name;

    //2.牌显示正面还是反面
    private boolean up;
    //3.是否可以点击
    private boolean canClick = false;
    //4.是否被点击过
    private boolean clicked = false;

    public Poker(String name, boolean up) {
        // 将背面图片缩放为固定宽高 (避免宽高为0)

        this.name = name;
        this.up = up;
        //判断当前的牌是显示正面还是反面
        if (this.up) {
            //正面
            this.turnFront();
        } else {
            //反面
            this.turnRear();
        }
        //设置牌的宽高
        this.setSize(71, 96);
        //把牌显示出来
        this.setVisible(true);
        //添加鼠标监听器, 点击时，向上弹，
        this.addMouseListener(this);
    }

    //显示正面
    public void turnFront() {
        this.setIcon(new ImageIcon(
                "D:\\Code\\JAVA\\java_code\\FarmersFightLandlord\\src\\main\\java\\org\\cqupt\\image\\" +
                        name + ".png"));
        this.up = true;
    }

    public void turnRear() {
        this.setIcon(new ImageIcon(scaledImage));
        this.up = false;
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //点击
        //判断当前的牌是否可以被点击
        if (canClick) {
            Point from = this.getLocation();
            //判断当前牌是否被点击
            int step = 0;
            if (clicked) {
                step = 20;
            } else {
                step = -20;
            }
            clicked = !clicked;
            //获取目标当前的位置
            Point to = new Point(from.x, from.y + step);
            this.setLocation(to);
        }
    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public boolean isClicked() {
        return clicked;
    }

}
