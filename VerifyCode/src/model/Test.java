package model;
import java.awt.image.BufferedImage;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedImage img = Client.readFromFile("img4.png");
		BufferedImage model = Client.readFromFile("1.png");
		System.out.println(Client.compare(img, model));
		
	}

}
