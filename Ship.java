import java.awt.*;

/**
 * Created by jameel on 09/01/15.
 */

public class Ship extends Rectangle {

	private final Color shipColor;
	
	public Ship(Color shipColor) {
		this.shipColor = shipColor;
		this.width = 50;
		this.height = 10; 
	}
	
    //static function to load the ship icon
    // or do it in the constructor

    public void paintIcon(Graphics2D g2, int x, int y, int width, int height) {
        //TODO write paint code
        g2.setColor(this.shipColor);
        g2.fillRect(x, y, width, height);
    }
}
