import java.awt.*;

/**
 * Created by jameel on 09/01/15.
 */

public class Ship extends Rectangle {

    //static function to load the ship icon
    // or do it in the constructor

    public void paintIcon(Graphics2D g2, int x, int y, int width, int height) {
        //TODO write paint code
        g2.setColor(Color.GREEN);
        g2.fillRect(x, y, width, height);
    }
}
