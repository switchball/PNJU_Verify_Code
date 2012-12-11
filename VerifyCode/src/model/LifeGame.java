package model;
import java.awt.image.BufferedImage;


public class LifeGame {
	public static BufferedImage life(BufferedImage imgSrc) {
		int x, y;
		int width = imgSrc.getWidth();
		int height = imgSrc.getHeight();
		int[][] bin = new int[height][width];
		boolean[][] trans = new boolean[height][width];
		BufferedImage image = new BufferedImage(width, height, imgSrc.getType());
		for (x = 0; x < width; x++) {
			for (y = 0; y < height; y++) {
				// Init
				if (imgSrc.getRGB(x, y) == 0xff000000)
					bin[y][x] = 1;
			}
		}
		System.out.println("Before:");
		printImage(bin, width, height);
		
		boolean isIterFinished = false;
		
		do {
		
		
		for (x = 0; x < width; x++) {
			for (y = 0; y < height; y++) {
				boolean isAlive = (bin[y][x] == 1);
				// Get neighbor count
				int count = 0;
				if (y > 0 && x > 0) count += bin[y-1][x-1];
				if (y > 0 && x < width - 1) count += bin[y-1][x+1];
				if (y > 0) count += bin[y-1][x];
				if (y < height - 1 && x > 0) count += bin[y+1][x-1];
				if (y < height - 1 && x < width - 1) count += bin[y+1][x+1];
				if (y < height - 1) count += bin[y+1][x];
				if (x > 0) count += bin[y][x-1];
				if (x < width - 1) count += bin[y][x+1];
				if (isAlive) {
					switch (count) {
					case 0: case 1: case 2:
						trans[y][x] = true;	// Alive and too low neighbour
						break;
					case 3: case 4: case 5: case 6: case 7: case 8:
						trans[y][x] = false;
					}
				} else {
					switch (count) {
					case 0: case 1: case 2: case 3: case 4: case 5:
						trans[y][x] = false;
						break;
					case 6: case 7: case 8:
						trans[y][x] = true;	// many neighbour make it looks like a hole
					}
				}
			}
		}
		isIterFinished = true;
		for (x = 0; x < width; x++) {
			for (y = 0; y < height; y++) {
				// Apply Trans
				if (trans[y][x]) {
					bin[y][x] = 1 - bin[y][x];
					isIterFinished = false;
				}
			}
		}
		System.out.println("After:");
		printImage(bin, width, height);
		
			
		}while(!isIterFinished);
		
		for (x = 0; x < width; x++) {
			for (y = 0; y < height; y++) {
				// Fill Image
				if (bin[y][x] == 1)
					image.setRGB(x, y, 0xff000000);
				else
					image.setRGB(x, y, 0xffffffff);
			}
		}
		
		return image;
	}
	
	public static void printImage(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		System.out.println("==========================");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (img.getRGB(x, y) == 0xff000000)
					System.out.print("*");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println("==========================");
	}
	
	public static void printImage(int num) {
		printImage(Client.readFromFile(num + ".png"));
	}
	
	public static void printImage(int[][] img, int width, int height) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (img[y][x] != 0)
					System.out.print("*");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	public static void printImage(int ... ints ) {
		int height = 0, width = 0, scanIndex = 0, scanX = 0;
		int[] widths  = new int[ints.length];
		int[] heights = new int[ints.length];
		BufferedImage[] imgs = new BufferedImage[ints.length];
		for (int i = 0; i < ints.length; i++) {
			imgs[i] = Client.readFromFile(ints[i] + ".png");
			widths[i]  = imgs[i].getWidth();
			heights[i] = imgs[i].getHeight();
			width += widths[i];
			if (heights[i] > height) height = heights[i];
		}
		for (int y = 0; y < height; y++) {
			scanIndex = 0;
			scanX = 0;
			for (int x = 0; x < width; x++, scanX++) {
				if (scanX >= widths[scanIndex]) {
					scanX = 0; scanIndex++;
					System.out.print("|");
				}
				if (y < heights[scanIndex] && imgs[scanIndex].getRGB(scanX, y) == 0xff000000) {
					System.out.print("*");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
	public static String getImageContext(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		StringBuffer sb = new StringBuffer();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (img.getRGB(x, y) == 0xff000000)
					sb.append('*');
				else
					sb.append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}
