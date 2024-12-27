package org.cqupt.bean;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.IOException;

public class MusicPlayer implements Runnable {
    private String filePath;
    private boolean loop;
    private boolean playing;

    public MusicPlayer(String filePath, boolean loop) {
        this.filePath = filePath;
        this.loop = loop;
        this.playing = true;
    }

    // 播放音乐的逻辑
    private void playMusic() {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Player player = new Player(fileInputStream);
            player.play();
        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }

    // 实现Runnable的run方法，用于后台线程播放
    @Override
    public void run() {
        while (playing) {
            playMusic();  // 播放一次
            if (!loop) {
                break;  // 如果不需要循环播放，跳出循环
            }
        }
    }

    // 停止播放音乐
    public void stop() {
        playing = false;  // 设置标志位停止播放
    }

    // 启动背景音乐的线程
    public void start() {
        Thread musicThread = new Thread(this);
        musicThread.start();
    }
}
