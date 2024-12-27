package org.cqupt.game;

import org.cqupt.util.Common;
import org.cqupt.util.Model;
import org.cqupt.bean.MusicPlayer;
import org.cqupt.util.PokerType;
import org.cqupt.bean.Poker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.cqupt.game.GameJFrame.backgroundMusic;

public class PlayerOperation extends Thread {
    //  游戏界面引用，用于控制游戏界面
    private final GameJFrame gameJFrame;
    //是否继续运行，控制玩家操作的线程
    private boolean isRun = true;

    //倒计时秒数
    private int time;

    public PlayerOperation(GameJFrame gameJFrame, int time) {
        this.gameJFrame = gameJFrame;
        this.time = time;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    @Override
    public void run() {
        // 时间充足，且第一次抢地主，倒计时开始
        while (time > -1 && this.isRun) {
            GameJFrame.time[1].setText("倒计时:" + time--);
            //每隔一毫秒刷新一次动画
            sleep(1);
        }
        // 倒计时结束，默认未抢地主。
        if (time == -1) {
            GameJFrame.time[1].setText("不抢");
        }
        // 抢地主结束，隐藏抢地主按钮
        gameJFrame.becomeLord[0].setVisible(false);
        gameJFrame.becomeLord[1].setVisible(false);
        // 设置玩家可点击
        for (Poker poker2 : gameJFrame.playerList.get(1)) {
            poker2.setCanClick(true);// 可被点击
        }

        if (GameJFrame.time[1].getText().equals("抢地主")) {
            gameJFrame.playerList.get(1).addAll(gameJFrame.lordList);
            // 地主牌翻开
            openLord(true);
            sleep(2);

            // 重新排序
            Common.order(gameJFrame.playerList.get(1));
            Common.rePosition(gameJFrame.playerList.get(1), 1);
            //因为是地主，所以不能不出
            gameJFrame.showCard[1].setEnabled(false);
            setLord(1);
        } else {
            //取消地主按钮显示
            gameJFrame.becomeLord[0].setVisible(false);
            gameJFrame.becomeLord[1].setVisible(false);
            if (Common.getScore(gameJFrame.playerList.get(0)) <
                    Common.getScore(gameJFrame.playerList.get(2))) {
                GameJFrame.time[2].setText("抢地主");
                GameJFrame.time[2].setVisible(true);
                setLord(2);
                openLord(true);

                sleep(3);
                gameJFrame.playerList.get(2).addAll(gameJFrame.lordList);
                Common.order(gameJFrame.playerList.get(2));
                Common.rePosition(gameJFrame.playerList.get(2), 2);
                openLord(false);
            } else {
                GameJFrame.time[0].setText("抢地主");
                GameJFrame.time[0].setVisible(true);
                setLord(0);
                openLord(true);

                sleep(3);

                gameJFrame.playerList.get(0).addAll(gameJFrame.lordList);
                Common.order(gameJFrame.playerList.get(0));
                Common.rePosition(gameJFrame.playerList.get(0), 0);
                openLord(false);
            }
        }

        //不是地主，不能先出牌
        turnOn(false);
        for (int i = 0; i < 3; i++) {
            GameJFrame.time[i].setText("不要");
            GameJFrame.time[i].setVisible(false);
        }
        gameJFrame.turn = gameJFrame.lordFlag;
        //轮流出牌
        while (true) {
            if (gameJFrame.turn == 1) {
                gameJFrame.showCard[1].setEnabled(
                        !GameJFrame.time[0].getText().equals("不要") ||
                                !GameJFrame.time[2].getText().equals("不要"));
                turnOn(true);
                timeWait(30, 1);
                turnOn(false);
                gameJFrame.turn = (gameJFrame.turn + 1) % 3;
                if (win()) break;
            }
            if (gameJFrame.turn == 0) {
                computer0();
                gameJFrame.turn = (gameJFrame.turn + 1) % 3;
                if (win()) break;
            }
            if (gameJFrame.turn == 2) {
                computer2();
                gameJFrame.turn = (gameJFrame.turn + 1) % 3;
                if (win()) break;
            }
        }
    }

    //分为抢地主和不抢地主，不抢地主的话，牌看不到。 抢的话，可以看到
    public void openLord(boolean is) {
        for (int i = 0; i < 3; i++) {
            if (is) gameJFrame.lordList.get(i).turnFront();
            else {
                gameJFrame.lordList.get(i).turnRear();
            }
            gameJFrame.lordList.get(i).setCanClick(true);
        }
    }

    //抢完地主后，显示地主图标。谁是地主，在谁那儿显示
    public void setLord(int i) {
        Point point = new Point();
        if (i == 1) {
            point.x = 80;
            point.y = 430;
            gameJFrame.lordFlag = 1;
        }
        if (i == 0) {
            point.x = 80;
            point.y = 20;
            gameJFrame.lordFlag = 0;
        }
        if (i == 2) {
            point.x = 700;
            point.y = 20;
            gameJFrame.lordFlag = 2;
        }
        gameJFrame.lord.setLocation(point);
        gameJFrame.lord.setVisible(true);
    }

    public void turnOn(boolean flag) {
        gameJFrame.showCard[0].setVisible(flag);
        gameJFrame.showCard[1].setVisible(flag);
    }

    public void timeWait(int n, int player) {
        // 每次出牌前，把上次的牌隐藏了
        if (!gameJFrame.currentList.get(player).isEmpty())
            Common.hideCards(gameJFrame.currentList.get(player));
        if (player == 1) {
            int time = n;
            while (!gameJFrame.nextPlayer && time >= 0) {
                GameJFrame.time[player].setText("倒计时:" + time);
                GameJFrame.time[player].setVisible(true);

                sleep(1);

                time--;
            }
            if (time == -1) {
                ShowCard(1);
            }
            gameJFrame.nextPlayer = false;
        } else {
            for (int i = n; i >= 0; i--) {

                sleep(1);

                GameJFrame.time[player].setText("倒计时:" + i);
                GameJFrame.time[player].setVisible(true);
            }
        }
        GameJFrame.time[player].setVisible(false);
    }

    //模拟自动出牌
    public void ShowCard(int role) {
        //检查顺序，先检查是否有炸弹，然后是飞机，连队，单张，顺子 ， 最开始是地主出，可以随心出
        int[] orders = new int[]{4, 3, 5, 2, 1};
        Model model = Common.getModel(gameJFrame.playerList.get(role), orders);
        ArrayList<String> list = new ArrayList<>();
        if (GameJFrame.time[(role + 1) % 3].getText().equals("不要") &&
                GameJFrame.time[(role + 2) % 3].getText().equals("不要")) {
            // 优先出顺子
            if (!model.a123.isEmpty()) {
                list.add(model.a123.get(model.a123.size() - 1));

                // 再出飞机
            } else if (!model.a111222.isEmpty()) {
                String[] name = model.a111222.get(0).split(",");
                //飞机是三个三个的，所以要除以3, 飞机也不能不带东西
                if (name.length / 3 <= model.a1.size()) {
                    list.add(model.a111222.get(model.a111222.size() - 1));
                    for (int i = 0; i < name.length / 3; i++)
                        list.add(model.a1.get(i));
                } else if (name.length / 3 <= model.a2.size()) {
                    list.add(model.a111222.get(model.a111222.size() - 1));
                    for (int i = 0; i < name.length / 3; i++)
                        list.add(model.a2.get(i));
                }

                // 然后出连对
            } else if (!model.a112233.isEmpty()) {
                list.add(model.a112233.get(model.a112233.size() - 1));

                // 再出三张
            } else if (!model.a3.isEmpty()) {
                if (!model.a1.isEmpty()) {
                    list.add(model.a1.get(model.a1.size() - 1));
                } else if (!model.a2.isEmpty()) {
                    list.add(model.a2.get(model.a2.size() - 1));
                }
                list.add(model.a3.get(model.a3.size() - 1));

                // 接着出对子
            } else if (!model.a2.isEmpty()) {
                list.add(model.a2.get(model.a2.size() - 1));

                // 单张
            } else if (!model.a1.isEmpty()) {
                list.add(model.a1.get(model.a1.size() - 1));

                // 最后出炸弹
            } else if (!model.a4.isEmpty()) {
                //自动出牌不会出4带二
                list.add(model.a4.get(0));
            }
        } else {
            //如果不是地主的话
            if (role != gameJFrame.lordFlag) {
                // 用来判断是否最终放弃出牌
                int f = 0;
                if (GameJFrame.time[gameJFrame.lordFlag].getText()
                        .equals("不要")) {
                    f = 1;
                }
                //对抗，检查队友是否出了较为高级的拍，如果是的话，则放弃出牌，只压单张和双张
                if ((role + 1) % 3 == gameJFrame.lordFlag) {
                    if ((Common.judgeType(
                            gameJFrame.currentList.get((role + 2) % 3)) !=
                            PokerType.c1 || Common.judgeType(
                            gameJFrame.currentList.get((role + 2) % 3)) !=
                            PokerType.c2) &&
                            gameJFrame.currentList.get(gameJFrame.lordFlag)
                                    .isEmpty())
                        f = 1;
                    if (!gameJFrame.currentList.get((role + 2) % 3).isEmpty() &&
                            Common.getValue(
                                    gameJFrame.currentList.get((role + 2) % 3)
                                            .get(0)) > 13)
                        f = 1;
                }
                if (f == 1) {
                    GameJFrame.time[role].setVisible(true);
                    GameJFrame.time[role].setText("不要");
                    return;
                }
            }
            //决定当前角色是否有必要主动出牌
            int can = 0;
            //如果对手牌数少于或等于 5，则 can = 1，表示当前地主需要谨慎，因为对手牌量已经很少。
            if (role == gameJFrame.lordFlag) {
                if (gameJFrame.playerList.get((role + 1) % 3).size() <= 5 ||
                        gameJFrame.playerList.get((role + 2) % 3).size() <= 5)
                    can = 1;
            } else {
                //如果地主的手牌数少于或等于 5，则 can = 1，表示当前角色需要注意，地主可能即将打完牌。
                if (gameJFrame.playerList.get(gameJFrame.lordFlag).size() <= 5)
                    can = 1;
            }

            ArrayList<Poker> player;
            if (GameJFrame.time[(role + 2) % 3].getText().equals("不要"))
                player = gameJFrame.currentList.get((role + 1) % 3);
            else player = gameJFrame.currentList.get((role + 2) % 3);

            PokerType cType = Common.judgeType(player);

            if (cType == PokerType.c1) {
                if (can == 1)
                    model = Common.getModel(gameJFrame.playerList.get(role),
                            new int[]{4, 1, 3, 2, 5});
                AI_1(model.a1, player, list);
            } else if (cType == PokerType.c2) {
                if (can == 1)
                    model = Common.getModel(gameJFrame.playerList.get(role),
                            new int[]{4, 2, 3, 5, 1});
                AI_1(model.a2, player, list);
            } else if (cType == PokerType.c3) {
                AI_1(model.a3, player, list);
            } else if (cType == PokerType.c4) {
                AI_1(model.a4, player, list);
            } else if (cType == PokerType.c31) {
                if (can == 1)
                    model = Common.getModel(gameJFrame.playerList.get(role),
                            new int[]{4, 3, 1, 2, 5});
                AI_2(model.a3, model.a1, player, list);
            } else if (cType == PokerType.c32) {
                if (can == 1)
                    model = Common.getModel(gameJFrame.playerList.get(role),
                            new int[]{4, 3, 2, 5, 1});
                AI_2(model.a3, model.a2, player, list);
            } else if (cType == PokerType.c411) {
                AI_5(model.a4, model.a1, player, list);
            } else if (cType == PokerType.c422) {
                AI_5(model.a4, model.a2, player, list);
            } else if (cType == PokerType.c123) {
                if (can == 1)
                    model = Common.getModel(gameJFrame.playerList.get(role),
                            new int[]{4, 5, 3, 2, 1});
                AI_3(model.a123, player, list);
            } else if (cType == PokerType.c112233) {
                if (can == 1)
                    model = Common.getModel(gameJFrame.playerList.get(role),
                            new int[]{4, 2, 3, 5, 1});
                AI_3(model.a112233, player, list);
            } else if (cType == PokerType.c11122234) {
                AI_4(model.a111222, model.a1, player, list);
            } else if (cType == PokerType.c1112223344) {
                AI_4(model.a111222, model.a2, player, list);
            }
            if (list.isEmpty() && can == 1 && !model.a4.isEmpty()) {
                AI_1(model.a4, player, list);
            }

        }
        // 每次轮到这个人的时候，清空当前人的牌
        gameJFrame.currentList.get(role).clear();
        //list集合存储的即为当前要出的牌
        if (!list.isEmpty()) {
            Point point = new Point();
            if (role == 0) point.x = 200;
            if (role == 2) point.x = 550;
            if (role == 1) {
                //跟据牌的数量，确定出牌的位置
                point.x = (770 / 2) -
                        (gameJFrame.currentList.get(1).size() + 1) * 15 / 2;
            }
            //堆叠拜访
            point.y = (400 / 2) - (list.size() + 1) * 15 / 2;
            ArrayList<Poker> temp = new ArrayList<>();
            for (int i = 0, len = list.size(); i < len; i++) {
                List<Poker> pokers =
                        getCardByName(gameJFrame.playerList.get(role),
                                list.get(i));
                temp.addAll(pokers);
            }
            temp = Common.getOrder2(temp);
            for (Poker poker : temp) {
                Common.move(poker, poker.getLocation(), point);
                point.y += 15;
                //把牌置为最顶层
                GameJFrame.container.setComponentZOrder(poker, 0);
                //放入当前出的牌堆中
                gameJFrame.currentList.get(role).add(poker);
                //从当前人的牌中移除
                gameJFrame.playerList.get(role).remove(poker);
            }
            //重新计算位置
            Common.rePosition(gameJFrame.playerList.get(role), role);
        } else {
            GameJFrame.time[role].setVisible(true);
            GameJFrame.time[role].setText("不要");
        }
        //把牌全部翻正面
        for (Poker poker : gameJFrame.currentList.get(role))
            poker.turnFront();
    }

    public void AI_3(List<String> model, List<Poker> player,
                     List<String> list) {

        for (String string : model) {
            String[] s = string.split(",");
            if (s.length == player.size() &&
                    getValueInt(string) > Common.getValue(player.get(0))) {
                list.add(string);
                return;
            }
        }
    }

    public void AI_4(List<String> model1, List<String> model2,
                     List<Poker> player, List<String> list) {
        player = Common.getOrder2(player);
        int len1 = model1.size();
        int len2 = model2.size();

        if (len1 < 1 || len2 < 1) return;
        for (String string : model1) {
            String[] s = string.split(",");
            String[] s2 = model2.get(0).split(",");
            if ((s.length / 3 <= len2) &&
                    (s.length * (3 + s2.length) == player.size()) &&
                    getValueInt(string) > Common.getValue(player.get(0))) {
                list.add(string);
                for (int j = 1; j <= s.length / 3; j++)
                    list.add(model2.get(len2 - j));
            }
        }
    }

    public void AI_5(List<String> model1, List<String> model2,
                     List<Poker> player, List<String> list) {
        player = Common.getOrder2(player);
        int len1 = model1.size();
        int len2 = model2.size();

        if (len1 < 1 || len2 < 2) return;
        for (String s : model1) {
            if (getValueInt(s) > Common.getValue(player.get(0))) {
                list.add(s);
                for (int j = 1; j <= 2; j++)
                    list.add(model2.get(len2 - j));
            }
        }
    }

    public void AI_1(List<String> model, List<Poker> player,
                     List<String> list) {

        for (int len = model.size(), i = len - 1; i >= 0; i--) {
            if (getValueInt(model.get(i)) > Common.getValue(player.get(0))) {
                list.add(model.get(i));
                break;
            }
        }

    }

    public void AI_2(List<String> model1, List<String> model2,
                     List<Poker> player, List<String> list) {
        player = Common.getOrder2(player);
        int len1 = model1.size();
        int len2 = model2.size();
        if (len1 < 1 || len2 < 1) return;
        for (int i = len1 - 1; i >= 0; i--) {
            //直接高端压制
            if (getValueInt(model1.get(i)) > Common.getValue(player.get(0))) {
                list.add(model1.get(i));
                break;
            }
        }
        list.add(model2.get(len2 - 1));
        if (list.size() < 2) list.clear();
    }

    public int getValueInt(String n) {
        String[] name = n.split(",");
        String s = name[0];
        int i = Integer.parseInt(s.substring(2));
        if (s.charAt(0) == '5') i += 3;
        if (s.substring(2).equals("1") || s.substring(2).equals("2")) i += 13;
        return i;
    }


    public List getCardByName(List<Poker> list, String n) {
        String[] name = n.split(",");
        ArrayList cardsList = new ArrayList();
        int j = 0;
        for (int i = 0, len = list.size(); i < len; i++) {
            if (j < name.length && list.get(i).getName().equals(name[j])) {
                cardsList.add(list.get(i));
                i = 0;
                j++;
            }
        }
        return cardsList;
    }

    public boolean win() {
        backgroundMusic.stop();
        for (int i = 0; i < 3; i++) {
            if (gameJFrame.playerList.get(i).isEmpty()) {
                String s;
                if (i == 1) {
                    s = "恭喜你，胜利了!";
                    backgroundMusic = new MusicPlayer(
                            "D:\\Code\\JAVA\\java_code\\" +
                                    "FarmersFightLandlord\\src\\main" +
                                    "\\java\\org\\cqupt\\music\\win_game.mp3",
                            false);
                } else {
                    s = "很遗憾，本次重邮斗地主，你输了";
                    backgroundMusic = new MusicPlayer(
                            "D:\\Code\\JAVA\\java_code\\" +
                                    "FarmersFightLandlord\\src\\main" +
                                    "\\java\\org\\cqupt\\music\\lose_game.mp3",
                            false);
                }
                backgroundMusic.start();
                for (int j = 0;
                     j < gameJFrame.playerList.get((i + 1) % 3).size(); j++)
                    gameJFrame.playerList.get((i + 1) % 3).get(j).turnFront();
                for (int j = 0;
                     j < gameJFrame.playerList.get((i + 2) % 3).size(); j++)
                    gameJFrame.playerList.get((i + 2) % 3).get(j).turnFront();
                JOptionPane.showMessageDialog(gameJFrame, s);
                return true;
            }
        }
        return false;
    }

    public void computer0() {
        timeWait(1, 0);
        ShowCard(0);

    }

    public void computer2() {
        timeWait(1, 2);
        ShowCard(2);

    }

    //定义一个方法用来暂停N秒
    //参数为等待的时间
    //因为线程中的sleep方法有异常，直接调用影响阅读
    public void sleep(int i) {
        try {
            Thread.sleep(i * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
