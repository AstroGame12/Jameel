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

    public void paintIcon(Graphics2D g2) {
        g2.setColor(this.shipColor);
        g2.fillRect(this.x, this.y, this.width, this.height);
    }
}
