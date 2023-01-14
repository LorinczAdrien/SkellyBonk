package game_logic;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager implements Runnable{
    // Sound effects
    private Clip theme, gunShot, bonk;

    // Control variables
    private boolean shootGun = false, playerDamaged = false, playTheme = true;
    private Thread audioThread;

    public AudioManager() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("src" + File.separator + "audio" + File.separator + "theme" + File.separator + "theme.wav"));
            this.theme = AudioSystem.getClip();
            this.theme.open(inputStream);
            this.theme.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException exception) {
            exception.printStackTrace();
            System.out.println("Couldn't load theme music");
        }

        try {
            AudioInputStream inputStream2 = AudioSystem.getAudioInputStream(new File("src" + File.separator + "audio" + File.separator + "gunshot" + File.separator + "gun_shot.wav"));
            this.gunShot = AudioSystem.getClip();
            this.gunShot.open(inputStream2);

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException exception) {
            exception.printStackTrace();
            System.out.println("Couldn't load gun shot effect");
        }

        try {
            AudioInputStream inputStream3 = AudioSystem.getAudioInputStream(new File("src" + File.separator + "audio" + File.separator + "damage" + File.separator + "bonk_sound.wav"));
            this.bonk = AudioSystem.getClip();
            this.bonk.open(inputStream3);

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException exception) {
            exception.printStackTrace();
            System.out.println("Couldn't load bonk sound effect");
        }

        this.audioThread = new Thread(this);
        this.audioThread.start();
    }

    public void playerDamaged() {
        this.playerDamaged = true;
    }
    public void shootGun() {
        this.shootGun = true;
    }

    public void stopTheme() {
        this.theme.stop();
    }

    public void startTheme() {
        this.theme.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void run() {
        while(this.audioThread != null) {
            if(this.shootGun) {
                this.gunShot.loop(1);
                this.shootGun = false;
            }

            if(this.playerDamaged) {
                this.bonk.loop(1);
                this.playerDamaged = false;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException exception) {
                System.out.println("Audio manager - Couldn't sleep");
            }
        }
    }
}
