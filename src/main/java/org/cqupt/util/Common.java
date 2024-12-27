package org.cqupt.util;

import org.cqupt.bean.Poker;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.cqupt.game.GameJFrame.container;
import static org.cqupt.game.GameJFrame.time;


public class Common {
    public static void move(Poker poker, Point from, Point to) {
        if (to.x != from.x) {
            // 计算直线方程 y = kx + b 的斜率和截距
            double k = (1.0) * (to.y - from.y) / (to.x - from.x);
            double b = to.y - to.x * k;

            // 根据起点和终点的 x 坐标决定移动方向
            int flag;
            if (from.x < to.x) flag = 20; // 如果起点在终点左边，正方向移动
            else flag = -20; // 如果起点在终点右边，反方向移动

            // 沿 x 轴移动扑克牌
            for (int i = from.x; Math.abs(i - to.x) > 20; i += flag) {
                // 根据直线方程计算 y 坐标
                double y = k * i + b;

                // 设置扑克牌的新位置
                poker.setLocation(i, (int) y);

                // 控制移动速度，通过线程暂停模拟动画效果
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // 设置扑克牌的最终位置为目标位置
        poker.setLocation(to);
    }

    public static void rePosition(ArrayList<Poker> list, int flag) {
        Point p = new Point();

        // 根据玩家的位置（flag）设置初始摆放点
        if (flag == 0) {
            p.x = 50;
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2;
        }
        if (flag == 1) {
            p.x = (800 / 2) - (list.size() + 1) * 21 / 2;
            p.y = 450;
        }
        if (flag == 2) {
            p.x = 700;
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2;
        }

        for (Poker poker : list) {
            // 使用 move 方法移动扑克牌到指定位置
            move(poker, poker.getLocation(), p);

            // 将扑克牌设置到最上层（z-index）
            container.setComponentZOrder(poker, 0);

            // 根据 flag 更新下一个扑克牌的摆放位置
            if (flag == 1) p.x += 21; // 中间玩家的牌横向排列，x 坐标递增
            else p.y += 15; // 左右玩家的牌纵向排列，y 坐标递增
        }
    }

    //利用牌的价值，将集合中的牌进行排序
    public static void order(ArrayList<Poker> list) {
        //此时可以改为lambda表达式
        list.sort((o1, o2) -> {
            //获取花色
            //1-黑桃 2-红桃 3-梅花 4-方块 5-大小王
            int a1 = Integer.parseInt(o1.getName().substring(0, 1));
            int a2 = Integer.parseInt(o2.getName().substring(0, 1));

            //获取牌上的数字,同时也是牌的价值
            //1-A ... 11-J 12-Q 13-K
            int b1 = Integer.parseInt(o1.getName().substring(2));
            int b2 = Integer.parseInt(o2.getName().substring(2));

            //计算牌的价值，利用牌的价值进行排序
            //牌上的数字在3~10之间，价值就是3~10
            //3:价值3
            //...
            //10:价值10
            //J:价值11
            //Q:价值12
            //K:价值13
            //A：1 + 20 = 21
            //2：2 + 30 = 32
            //小王：1 + 100 = 101
            //大王：2 + 100 = 102

            //计算大小王牌的价值
            if (a1 == 5) {
                b1 += 100;
            }
            if (a2 == 5) {
                b2 += 100;
            }

            //计算A的价值
            if (b1 == 1) {
                b1 += 20;
            }
            if (b2 == 1) {
                b2 += 20;
            }
            //计算2的价值
            if (b1 == 2) {
                b1 += 30;
            }
            if (b2 == 2) {
                b2 += 30;
            }

            //倒序排列，数值越大的，越应该排在前面
            int flag = b2 - b1;

            //如果牌的价值一样，则按照花色排序
            if (flag == 0) {
                return a2 - a1;
            } else {
                return flag;
            }
        });
    }

    // 用于1号电脑玩家和二号电脑玩家强地主
    public static int getScore(ArrayList<Poker> list) {
        int count = 0;
        for (Poker poker : list) {
            if (poker.getName().substring(0, 1).equals("5")) {
                count += 5;
            }
            if (poker.getName().substring(2).equals("2")) {
                count += 2;
            }
        }
        return count;
    }

    public static void hideCards(ArrayList<Poker> list) {
        for (Poker poker : list) {
            poker.setVisible(false);
        }
    }

    public static Model getModel(ArrayList<Poker> list, int[] orders) {
        ArrayList<Poker> list2 = new ArrayList<>(list);
        Model model = new Model();
        for (int order : orders)
            showOrders(order, list2, model);
        return model;
    }

    public static void showOrders(int i, ArrayList<Poker> list, Model model) {
        switch (i) {
            case 1:
                getSingle(list, model);
                break;
            case 2:
                getTwo(list, model);
                getTwoTwo(list, model);
                break;
            case 3:
                getThree(list, model);
                getPlane(list, model);
                break;
            case 4:
                getBoomb(list, model);
                break;
            case 5:
                get123(list, model);
                break;
        }
    }

    public static void get123(ArrayList<Poker> list, Model model) {
        ArrayList<Poker> del = new ArrayList<>();

        if (!list.isEmpty() && (getValue(list.get(0)) < 7 ||
                getValue(list.get(list.size() - 1)) > 10))
            return;
        // 至少需要5张牌才能构成顺子
        if (list.size() < 5) return;

        ArrayList<Poker> temp = new ArrayList<>();
        ArrayList<Integer> integers = new ArrayList<>();

        // 去重：将不重复点数的牌添加到 temp 列表中
        for (Poker poker : list) {
            if (!integers.contains(getValue(poker))) {
                integers.add(getValue(poker));
                temp.add(poker);
            }
        }

        // 对去重后的牌进行排序
        order(temp);

        // 开始识别顺子牌型
        for (int i = 0, len = temp.size(); i < len; i++) {
            int k = i;

            // 检查是否存在连续的牌
            for (int j = i; j < len; j++) {
                if (getValue(temp.get(j)) - getValue(temp.get(i)) == j - i) {
                    k = j;  // 更新 k 值，表示连续到的最大牌
                } else {
                    break;  // 不连续，跳出循环
                }
            }

            // 如果找到5张或更多的连续牌，则判定为顺子
            if (k - i >= 4) {
                StringBuilder s = new StringBuilder();
                for (int j = i; j < k; j++) {
                    s.append(temp.get(j).getName()).append(",");
                    del.add(temp.get(j));  // 标记这些牌为顺子
                }
                s.append(temp.get(k).getName());
                del.add(temp.get(k));

                // 添加到 model 的顺子牌型
                model.a123.add(s.toString());
                i = k;  // 更新 i，跳过已经处理的部分
            }
        }

        // 从原始列表中移除已经识别为顺子的牌
        list.removeAll(del);
    }


    public static void getTwoTwo(ArrayList<Poker> list, Model model) {
        ArrayList<String> del = new ArrayList<>();
        ArrayList<String> l = model.a2;
        if (l.size() < 3) return;
        int[] s = new int[l.size()];
        for (int i = 0, len = l.size(); i < len; i++) {
            String[] name = l.get(i).split(",");
            s[i] = Integer.parseInt(name[0].substring(2));
        }
        for (int i = 0, len = l.size(); i < len; i++) {
            int k = i;
            for (int j = i; j < len; j++) {
                if (s[i] - s[j] == j - i) k = j;
            }
            if (k - i >= 2) {
                String ss = "";
                for (int j = i; j < k; j++) {
                    ss += l.get(j) + ",";
                    del.add(l.get(j));
                }
                ss += l.get(k);
                model.a112233.add(ss);
                del.add(l.get(k));
                i = k;
            }
        }
        l.removeAll(del);
    }

    public static void getPlane(ArrayList<Poker> list, Model model) {
        ArrayList<String> del = new ArrayList<>();
        ArrayList<String> l = model.a3;
        if (l.size() < 2) return;
        Integer[] s = new Integer[l.size()];
        for (int i = 0, len = l.size(); i < len; i++) {
            String[] name = l.get(i).split(",");
            s[i] = Integer.parseInt(name[0].substring(2));
        }
        for (int i = 0, len = l.size(); i < len; i++) {
            int k = i;
            for (int j = i; j < len; j++) {
                if (s[i] - s[j] == j - i) k = j;
            }
            if (k != i) {
                String ss = "";
                for (int j = i; j < k; j++) {
                    ss += l.get(j) + ",";
                    del.add(l.get(j));
                }
                ss += l.get(k);
                model.a111222.add(ss);
                del.add(l.get(k));
                i = k;
            }
        }
        l.removeAll(del);
    }

    public static void getBoomb(ArrayList<Poker> list, Model model) {
        ArrayList<Poker> del = new ArrayList<>();
        if (list.isEmpty()) return;
        //重新排序过，故而只分析第一和第二便可知道是否为王炸
        if (list.size() >= 2 && getColor(list.get(0)) == 5 &&
                getColor(list.get(1)) == 5) {
            model.a4.add(list.get(0).getName() + "," + list.get(1).getName());
            del.add(list.get(0));
            del.add(list.get(1));
        }
        if (list.size() >= 2 && getColor(list.get(0)) == 5 &&
                getColor(list.get(1)) != 5) {
            del.add(list.get(0));
            model.a1.add(list.get(0).getName());
        }
        list.removeAll(del);
        for (int i = 0, len = list.size(); i < len; i++) {
            if (i + 3 < len &&
                    getValue(list.get(i)) == getValue(list.get(i + 3))) {
                String s = list.get(i).getName() + ",";
                s += list.get(i + 1).getName() + ",";
                s += list.get(i + 2).getName() + ",";
                s += list.get(i + 3).getName();
                model.a4.add(s);
                for (int j = i; j <= i + 3; j++)
                    del.add(list.get(j));
                i = i + 3;
            }
        }
        list.removeAll(del);
    }

    public static void getThree(ArrayList<Poker> list, Model model) {
        ArrayList<Poker> del = new ArrayList<>();
        for (int i = 0, len = list.size(); i < len; i++) {
            if (i + 2 < len &&
                    getValue(list.get(i)) == getValue(list.get(i + 2))) {
                String s = list.get(i).getName() + ",";
                s += list.get(i + 1).getName() + ",";
                s += list.get(i + 2).getName();
                model.a3.add(s);
                for (int j = i; j <= i + 2; j++)
                    del.add(list.get(j));
                i = i + 2;
            }
        }
        list.removeAll(del);
    }

    public static void getTwo(ArrayList<Poker> list, Model model) {
        ArrayList<Poker> del = new ArrayList<>();
        for (int i = 0, len = list.size(); i < len; i++) {
            if (i + 1 < len &&
                    getValue(list.get(i)) == getValue(list.get(i + 1))) {
                String s = list.get(i).getName() + ",";
                s += list.get(i + 1).getName();
                model.a2.add(s);
                for (int j = i; j <= i + 1; j++)
                    del.add(list.get(j));
                i = i + 1;
            }
        }
        list.removeAll(del);
    }

    public static void getSingle(ArrayList<Poker> list, Model model) {
        ArrayList<Poker> del = new ArrayList<>();
        for (int i = 0, len = list.size(); i < len; i++) {
            model.a1.add(list.get(i).getName());
            del.add(list.get(i));
        }
        list.removeAll(del);
    }

    public static int getColor(Poker poker) {
        return Integer.parseInt(poker.getName().substring(0, 1));
    }

    public static int getValue(Poker poker) {
        int i = Integer.parseInt(poker.getName().substring(2));
        if (poker.getName().substring(2).equals("2")) i = i + 13;
        else if (poker.getName().substring(2).equals("1")) i = i + 13;
        else if (getColor(poker) == 5) i = i + 15;
        return i;
    }

    //判断牌型
// 判断牌型的主函数
    public static PokerType judgeType(ArrayList<Poker> list) {
        int len = list.size();

        // 处理长度小于等于4的牌型（单张、对子、三张、炸弹）
        if (len <= 4) {
            if (!list.isEmpty() &&
                    getValue(list.get(0)) == getValue(list.get(len - 1))) {
                switch (len) {
                    case 1:
                        return PokerType.c1;  // 单张
                    case 2:
                        return PokerType.c2;  // 对子
                    case 3:
                        return PokerType.c3;  // 三张
                    case 4:
                        return PokerType.c4;  // 炸弹
                    default:
                        return PokerType.c0;
                }
            }

            // 大小王构成的对子
            if (len == 2 && getColor(list.get(1)) == 5) {
                return PokerType.c2;
            }

            // 三带一
            if (len == 4 &&
                    ((getValue(list.get(0)) == getValue(list.get(len - 2))) ||
                            getValue(list.get(1)) ==
                                    getValue(list.get(len - 1)))) {
                return PokerType.c31;
            }

            return PokerType.c0;
        }

        // 处理长度大于4的牌型（复杂牌型，如顺子、连对、飞机等）
        ArrayList<ArrayList<Integer>> indexList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            indexList.add(new ArrayList<>());
        }

        // 获取各个牌值的出现次数并分类存储
        getMax(list, indexList);

        // 判断牌型
        if (indexList.get(2).size() == 1 && indexList.get(1).size() == 1 &&
                len == 5) {
            return PokerType.c32;  // 三带二
        }
        if (indexList.get(3).size() == 1 && len == 6) {
            return PokerType.c411;  // 四带二
        }
        if (indexList.get(3).size() == 1 && indexList.get(1).size() == 2 &&
                len == 8) {
            return PokerType.c422;  // 四带两对
        }
        if ((getColor(list.get(0)) != 5) && (indexList.get(0).size() == len) &&
                (getValue(list.get(0)) - getValue(list.get(len - 1)) ==
                        len - 1)) {
            return PokerType.c123;  // 顺子
        }
        if (indexList.get(1).size() == len / 2 && len % 2 == 0 &&
                getValue(list.get(0)) - getValue(list.get(len - 1)) ==
                        len / 2 - 1) {
            return PokerType.c112233;  // 连对
        }
        if (indexList.get(2).size() == len / 3 && len % 3 == 0 &&
                (getValue(list.get(0)) - getValue(list.get(len - 1)) ==
                        (len / 3 - 1))) {
            return PokerType.c111222;  // 飞机
        }
        if (indexList.get(2).size() == len / 4 &&
                (indexList.get(2).get(len / 4 - 1) -
                        (indexList.get(2).get(0)) == len / 4 - 1)) {
            return PokerType.c11122234;  // 飞机带单张
        }
        if (indexList.get(2).size() == len / 5 &&
                (indexList.get(2).get(len / 5 - 1) -
                        (indexList.get(2).get(0)) == len / 5 - 1)) {
            return PokerType.c1112223344;  // 飞机带对子
        }

        return PokerType.c0;
    }

    // 获取最大牌值，并分类存入 indexList 中
    public static void getMax(ArrayList<Poker> list,
                              ArrayList<ArrayList<Integer>> indexList) {
        int[] count = new int[16];
        for (int i = 0; i < 16; i++)
            count[i] = 0;

        for (int i = 0, len = list.size(); i < len; i++) {
            if (Common.getColor(list.get(i)) == 5)// 王
                count[15]++;
            else count[Common.getValue(list.get(i)) - 1]++;
        }
        for (int i = 0; i < 14; i++) {
            switch (count[i]) {
                case 1:
                    indexList.get(0).add(i + 1);
                    break;
                case 2:
                    indexList.get(1).add(i + 1);
                    break;
                case 3:
                    indexList.get(2).add(i + 1);
                    break;
                case 4:
                    indexList.get(3).add(i + 1);
                    break;
            }
        }
    }

    public static ArrayList getOrder2(List<Poker> list) {
        ArrayList<Poker> list2 = new ArrayList<>(list);
        ArrayList<Poker> list3 = new ArrayList<>();
        int len = list2.size();
        int[] a = new int[20];
        for (int i = 0; i < 20; i++)
            a[i] = 0;
        for (int i = 0; i < len; i++) {
            a[getValue(list2.get(i))]++;
        }
        int max;
        for (int i = 0; i < 20; i++) {
            max = 0;
            for (int j = 19; j >= 0; j--) {
                if (a[j] > a[max]) max = j;
            }

            for (int k = 0; k < len; k++) {
                if (getValue(list2.get(k)) == max) {
                    list3.add(list2.get(k));
                }
            }
            list2.remove(list3);
            a[max] = 0;
        }
        return list3;
    }


    public static int checkCards(ArrayList<Poker> c,
                                 ArrayList<ArrayList<Poker>> current) {
        ArrayList<Poker> currentlist;
        if (time[0].getText().equals("不要"))
            currentlist = current.get(2);
        else currentlist = current.get(0);
        PokerType cType = judgeType(c);
        PokerType cType2 = judgeType(currentlist);
        //牌的長度要一樣
        if (cType != PokerType.c4 && c.size() != currentlist.size())
            return 0;
        //牌型要一样
        if (cType != PokerType.c4 && judgeType(c) != judgeType(currentlist)) {
            return 0;
        }
        if (cType == PokerType.c4) {
            //王炸的话直接出
            if (c.size() == 2) return 1;
            //如果前一轮不是炸弹，则可直接出
            if (cType2 != PokerType.c4) {
                return 1;
            }
        }

        if (cType == PokerType.c1 || cType == PokerType.c2 ||
                cType == PokerType.c3 || cType == PokerType.c4) {
            if (getValue(c.get(0)) <= getValue(currentlist.get(0))) {
                return 0;
            } else {
                return 1;
            }
        }
        if (cType == PokerType.c123 || cType == PokerType.c112233 ||
                cType == PokerType.c111222) {
            if (getValue(c.get(0)) <= getValue(currentlist.get(0)))
                return 0;
            else return 1;
        }
        if (cType == PokerType.c31 || cType == PokerType.c32 ||
                cType == PokerType.c411 || cType == PokerType.c422 ||
                cType == PokerType.c11122234 ||
                cType == PokerType.c1112223344) {
            List<Poker> a1 = getOrder2(c);
            List<Poker> a2 = getOrder2(currentlist);
            if (getValue(a1.get(0)) < getValue(a2.get(0))) return 0;
        }
        return 1;
    }

}
