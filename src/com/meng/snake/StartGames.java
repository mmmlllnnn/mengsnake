package com.meng.snake;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class StartGames {
    public static void main(String[] args) {
        //1.绘制一个JFrame窗口
        JFrame frame = new JFrame("贪吃蛇小作战");
        frame.setBounds(10,10,920,720);//窗口大小
        frame.setResizable(false);//不可调整大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭按钮，游戏可以关闭

        //2.将panel面板添加到窗口
        frame.add(new GamePanel());

        frame.setVisible(true);//显示按钮

    }
}
