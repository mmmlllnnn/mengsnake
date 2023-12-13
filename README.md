# mengsnake
Java 1小时完成贪吃蛇 源码

古久之前学习java时的练手项目，跟着敲了一遍。
供初学者学习。

--- 
<br>
<br>
<br>
<br>
之前写的文章。废话真多，好逊(没脸看,一巴掌把自己打飞，扭曲的在带地上爬行)
<br>
<br>
<br>
<br>
<br>
<br>

---

# 1小时编写java练习项目----贪吃蛇。

实现效果下图所示：
![](https://s1.ax1x.com/2022/04/13/LM42md.png)

------------


## 环境：
- jdk15
- IDEA 2021.01

## 项目结构：
新建一个普通java项目，src目录下新建 com.meng.snake 包编写java类；新建 statics 包放置静态资源。
1. Data----------链接statics中的图片资源(蛇头、蛇身、食物）
2. GamePanel----绘制面板内容，显示所有的游戏画面（主要）
3. StartGames----项目启动入口，绘制Frame窗口，添加 GamePanel

![](https://s1.ax1x.com/2022/04/13/LMILiq.png)

------------


## StartGames启动类:

绘制一个920×720的 Jframe,不可调整其大小，设置关闭事件。
将 GamePanel对象 添加到窗口中。
```java
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

```

------------
## Data（静态资源）:
将statics包中蛇的身体、头部、食物，通过URL生成ImageIcon对象，便于直接调用。

```java
package com.meng.snake;

import javax.swing.*;
import java.net.URL;
//图片数据存放
public class Data {

    //顶部图片
    public static URL headurl=Data.class.getResource("/statics/header.png");
    public static ImageIcon header=new ImageIcon(headurl);

    //蛇的脑袋
    public static URL upurl=Data.class.getResource("/statics/up.png");
    public static URL lefturl=Data.class.getResource("/statics/left.png");
    public static URL righturl=Data.class.getResource("/statics/right.png");
    public static URL downurl=Data.class.getResource("/statics/down.png");
    public static ImageIcon up=new ImageIcon(upurl);
    public static ImageIcon left=new ImageIcon( lefturl);
    public static ImageIcon right=new ImageIcon(righturl);
    public static ImageIcon down=new ImageIcon(downurl);
    //身体
    public static URL bodyurl=Data.class.getResource("/statics/body.png");
    public static ImageIcon body=new ImageIcon(bodyurl);
    //食物
    public static URL foodurl=Data.class.getResource("/statics/food.png");
    public static ImageIcon food=new ImageIcon(foodurl);
}

```

------------


## GamePanel(游戏主体)：
1.初始化：初始蛇的位置，第一次食物生成，键盘监听，获取键盘焦点，计时器启动
2.移动：键盘监听 上下左右健，按下时蛇头位置加20像素，如果碰到画面边界，则蛇头位置改为画幅的另一侧。遍历身体的每一节位置，依次往前移动一节。
3.吃食物：判断蛇头的位置和食物位置重合，蛇身长度lenth++,再次随机生成位置不与蛇重合的食物
4.改变游戏速度：键盘监听X健，按下后 timer.setDelay(100)，设置计时器刷新时间
5.死亡：遍历蛇身每节位置和蛇头坐标对比，一致时停止游戏。
6.提示：积分提示，空格暂停提示，加速提示，死亡提示

```java
package com.meng.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    int lenth; //蛇的长度
    int[] snakex = new int[600];//蛇的 x坐标
    int[] snakey = new int[500];//蛇的 y坐标
    String fx = "R";//R L U D蛇头朝向上下左右
    boolean isStart = false;//游戏是否开始
    int x=100;//游戏速度，默认100毫秒刷新一次
    Timer timer = new Timer(x, this);
    //食物
    int foodx;//食物坐标
    int foody;
    Random random = new Random();//随机生成食物
    //判断失败
    boolean isFail = false;


    //构造器调用初始化方法
    public GamePanel() {
        init();//初始化参数
        this.setFocusable(true);//获取键盘焦点
        this.addKeyListener(this);//设置监听器
        timer.start();//时间动起来
        foodx = 20 + 20 * random.nextInt(42);//第一次生成食物位置
        foody = 60 + 20 * random.nextInt(27);

    }

    //初始化
    public void init() {
        lenth = 3;fx = "R";
        snakex[0] = 100;snakey[0] = 100;//蛇头部位置
        snakex[1] = 80;snakey[1] = 100;//第一节身体
        snakex[2] = 60;snakey[2] = 100;//第二节身体
    }

    //画板：蛇 食物 g是画笔--所有的东西都在这里显示
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//清屏
        this.setBackground(Color.WHITE);
        g.setColor(Color.black);
        g.setFont(new Font("幼圆", Font.BOLD, 40));
        g.drawString("Java 贪吃蛇", 300, 50);


        //绘制一个游戏区域
        g.fillRect(20, 60, 865, 605);

        //画一条静态的蛇
        if (fx.equals("R")) {//画蛇头
            Data.right.paintIcon(this, g, snakex[0], snakey[0]);
        } else if (fx.equals("L")) {
            Data.left.paintIcon(this, g, snakex[0], snakey[0]);
        } else if (fx.equals("U")) {
            Data.up.paintIcon(this, g, snakex[0], snakey[0]);
        } else if (fx.equals("D")) {
            Data.down.paintIcon(this, g, snakex[0], snakey[0]);
        }

        //吃食物判断
        if (snakex[0] == foodx && snakey[0] == foody) {
            lenth++;
            foodx = 20 + 20 * random.nextInt(42);//吃完后再次生成食物
            foody = 60 + 20 * random.nextInt(27);
            for (int i = 0; i <lenth ; i++) {
                if(foodx==snakex[i] && foody==snakey[i]){
                    foodx = 20 + 20 * random.nextInt(42);//如果生成食物和身体重合就重新生成
                    foody = 60 + 20 * random.nextInt(27);
                }
            }
        }

        for (int i = 1; i < lenth; i++) {//蛇的身体长度通过循环画上去
            Data.body.paintIcon(this, g, snakex[i], snakey[i]);
        }
        //画食物
        Data.food.paintIcon(this, g, foodx, foody);

        //右上角积分
        g.setColor(Color.black);
        g.setFont(new Font("微软雅黑", Font.BOLD, 20));
        g.drawString("长度"+lenth, 700, 50);
        g.drawString("分数"+(lenth*5-15), 800, 50);
        g.drawString("按下 x 健改变速度", 25, 50);
        //添加游戏提示
        if (isStart == false) {
            g.setColor(Color.white);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("按下空格开始游戏", 300, 300);
        }

        //失败提醒
        if (isFail) {
            g.setColor(Color.white);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("you die,按下空格重新开始", 250, 300);
            g.drawString("你的分数是："+(lenth*5-15), 320, 350);
        }

    }

    //键盘监听
    @Override
    public void keyPressed(KeyEvent e) {//键盘按下未释放
        //获取按下的健是
        int keyCode = e.getKeyCode();
        //按下空格键改变游戏状态，暂停和死后重新开始
        if (keyCode == KeyEvent.VK_SPACE) {
            if (isFail) {//如果死了游戏再来一遍
                isFail = false;
                init();//重新初始化
            } else {
                isStart = !isStart;//更改游戏状态
            }
        }
        //按下X健改变游戏速度
        if(keyCode==KeyEvent.VK_X){
            x=x/2;
            if(x<=12){
                x=100;
            }
            timer.setDelay(x);
        }

        repaint();//刷新页面
        //键盘控制走向
        if (keyCode == KeyEvent.VK_LEFT && fx != "R") {
            fx = "L";
        } else if (keyCode == KeyEvent.VK_RIGHT && fx != "L") {
            fx = "R";
        } else if (keyCode == KeyEvent.VK_UP && fx != "D") {
            fx = "U";
        } else if (keyCode == KeyEvent.VK_DOWN && fx != "U") {
            fx = "D";
        }


    }

    //定时器,每间隔多少时间执行一次
    @Override
    public void actionPerformed(ActionEvent e) {
        //游戏开始状态下执行的操作
        if (isStart && isFail == false) {
            for (int i = lenth - 1; i > 0; i--) {//身体依次向前移动一节
                snakex[i] = snakex[i - 1];
                snakey[i] = snakey[i - 1];
            }

            //判断fx让头部向不同方向移动
            if (fx.equals("R")) {
                snakex[0] = snakex[0] + 20;
                if (snakex[0] >860) snakex[0] = 20;//判断边界x
            } else if (fx.equals("L")) {
                snakex[0] = snakex[0] - 20;
                if (snakex[0] < 20) snakex[0] = 860;//判断边界x
            } else if (fx.equals("U")) {
                snakey[0] = snakey[0] - 20;
                if (snakey[0] < 60) snakey[0] = 640;//判断边界y
            } else if (fx.equals("D")) {
                snakey[0] = snakey[0] + 20;
                if (snakey[0] > 640) snakey[0] = 60;//判断边界y
            }

            //撞到身体 死亡判断
            for (int i = 1; i < lenth; i++) {
                if (snakex[0] == snakex[i] && snakey[0] == snakey[i]) {
                    isFail = true;
                }
            }
            repaint();//刷新页面
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //释放
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //键盘按下并弹起
    }


}

```

------------


# 截图：

![](https://s1.ax1x.com/2022/04/13/LMOg8s.png)
![](https://s1.ax1x.com/2022/04/13/LMLo9A.png)