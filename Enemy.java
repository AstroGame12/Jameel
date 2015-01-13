import java.awt.*;

/**
 * Created by jameel on 09/01/15.
 */

public class Enemy extends Rectangle {

    //instance variables (each object has these)
    Color clr = Color.ORANGE;
    int xSpeed = 10;
    int health = 2;


    public Enemy(int i, int i1, int i2, int i3) {
        super(i, i1, i2, i3);
    }
    public void update() {
        if (health == 1) clr = Color.RED;

    }
}

