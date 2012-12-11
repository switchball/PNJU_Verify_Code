package model;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;


public class Adapter {
	Client client;
	boolean state;
	
	public Adapter() {
	}
	
	public void run() {
		new Runnable() {
			@Override
			public void run() {
				try {
					state = true;
					client = new Client();
					client.run();
				} catch (Exception e) {
					state = false;
					e.printStackTrace();
				}
			}
		}.run();
	}
	
	public void setUrlSrc(String urlSrc) {
		Client.urlSrc = urlSrc;
	}
	
	public boolean isSuccess() {
		return state;
	}
	
	public BufferedImage getOriginalImage() {
		return client.imageOriginal;
	}
	
	public BufferedImage getBinaryImage() {
		return client.grayImage;
	}
	
	public BufferedImage getFixedImage() {
		return client.fixedImage;
	}
	
	public BufferedImage getResultImage(int index) {
		switch (index) {
		case 1: return client.img1;
		case 2: return client.img2;
		case 3: return client.img3;
		case 4: return client.img4;
		}
		return null;
	}
	
	public String getResultImageContext(int index) {
		return LifeGame.getImageContext(getResultImage(index));
	}
	
	public String getResult() {
		return "" + client.num1 + client.num2 + client.num3 + client.num4;
	}
	
	public String getSimilarity(int which) {
		double d = client.numSimilarity[which];
		DecimalFormat df = new DecimalFormat("0.#%");
		return df.format(d);
	}
	
	public boolean isSimilarityUnderLevel(int which) {
		double d = client.numSimilarity[which];
		return d < SIMILARITY_PROBABILITY_LEVEL;
	}

	public static final double SIMILARITY_PROBABILITY_LEVEL = 0.9;
}
