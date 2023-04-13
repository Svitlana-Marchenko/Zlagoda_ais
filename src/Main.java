import menu.LoginMenu;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        LoginMenu a = new LoginMenu();
        a.setBounds(800,400,300,200);
        a.setVisible(true);
    }
}
