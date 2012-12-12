package model;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;

import javax.imageio.ImageIO;

/**
 * 复制图片识别的核心类
 * @author Xuanxiao
 *
 */
public class Client {

	BufferedImage imageOriginal;
	BufferedImage grayImage;
	BufferedImage fixedImage;
	BufferedImage img1;
	BufferedImage img2;
	BufferedImage img3;
	BufferedImage img4;
	int num1;
	int num2;
	int num3;
	int num4;
	double[] numSimilarity = new double[4];
	
	public static BufferedImage readImageFromUrl(String url) {
		try {
			BufferedImage img = ImageIO.read(new URL(url));
			return img;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage readFromFile(String fileName) {
		File file = new File(System.getProperty("user.dir"), fileName);
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeToFile(BufferedImage img, String fileName) {
		File file = new File(System.getProperty("user.dir"), fileName);
		try {
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
//		imageOriginal = readImageFromUrl("http://p.nju.edu.cn/portal/img.html?id=123");
		imageOriginal = readImageFromUrl("http://p.nju.edu.cn/img.html?"+Math.random());
//		imageOriginal = readFromFile("code.png");
		writeToFile(imageOriginal, "code.png");

		int width = imageOriginal.getWidth();
		int height = imageOriginal.getHeight();
		
		// 黑白化，保存到grayImage
		grayImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = imageOriginal.getRGB(i, j);
				grayImage.setRGB(i, j, rgb);
//				System.out.println(((rgb >> 16) & 255) + ", " + ((rgb >> 8) & 255) + ", " + (rgb & 255));
				rgb = grayImage.getRGB(i, j);
//				System.out.println(((rgb >> 16) & 255) + ", " + ((rgb >> 8) & 255) + ", " + (rgb & 255));
//				System.out.println("====");
				if ((rgb & 0xff) < 192) rgb &= 0xff000000;
				else rgb = 0xffffff;
				grayImage.setRGB(i, j, rgb);
			}
		}
		
		// 修正边缘，保存到fixedImage
		fixedImage = LifeGame.life(grayImage);
		
		// 批量在Console输出模板
		LifeGame.printImage(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		// 横线竖线扫描
		LinkedList<Integer> l2rList = scanFromLeftToRight(fixedImage);
		LinkedList<Integer> u2dList = scanFromUpToDown(fixedImage);
		
		int u = u2dList.getFirst();
		int d = u2dList.getLast();
		int l1, r1, l2, r2, l3, r3, l4, r4;
		l1 = l2rList.get(0);
		r1 = l2rList.get(1);
		l2 = l2rList.get(2);
		r2 = l2rList.get(3);
		l3 = l2rList.get(4);
		r3 = l2rList.get(5);
		l4 = l2rList.get(6);
		r4 = l2rList.get(7);
		
		// 切割图片
		img1 = fixedImage.getSubimage(l1, u, r1 - l1, d - u);
		img2 = fixedImage.getSubimage(l2, u, r2 - l2, d - u);
		img3 = fixedImage.getSubimage(l3, u, r3 - l3, d - u);
		img4 = fixedImage.getSubimage(l4, u, r4 - l4, d - u);
		
		writeToFile(img1, "img1.png");
		writeToFile(img2, "img2.png");
		writeToFile(img3, "img3.png");
		writeToFile(img4, "img4.png");
		
		// 计算相似度数值（若数值过低，可以选择丢弃）
		num1 = getSimilarNumber(img1, 0);
		num2 = getSimilarNumber(img2, 1);
		num3 = getSimilarNumber(img3, 2);
		num4 = getSimilarNumber(img4, 3);
		
		System.out.println(num1 + "," + num2 + "," + num3 + "," + num4);
		
		LifeGame.printImage(num1, num2, num3, num4);
		
		writeToFile(grayImage, "gray.png");
	}
	
	public LinkedList<Integer> scanFromLeftToRight(BufferedImage img) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		int width = img.getWidth();
		int height = img.getHeight();
		boolean isWhite = true;
		int x,y;
		for (x = 0; x < width; x++) {
			for (y = 0; y < height; y++) {
				if(img.getRGB(x, y) != 0xffffffff) break;
			}
			boolean isResultWhite;
			if (y < height) {
				// Black
				isResultWhite = false;
			} else {
				// White
				isResultWhite = true;
			}
			if (isWhite ^ isResultWhite) {
//				System.out.println("State changed At x = " + x);
				list.add(x);
				isWhite = !isWhite;
			}
		}
		return list;
	}
	
	public LinkedList<Integer> scanFromUpToDown(BufferedImage img) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		int width = img.getWidth();
		int height = img.getHeight();
		boolean isWhite = true;
		int x,y;
		for (y = 0; y < height; y++) {
			for (x = 0; x < width; x++) {
				if(img.getRGB(x, y) != 0xffffffff) break;
			}
			boolean isResultWhite;
			if (x < width) {
				// Black
				isResultWhite = false;
			} else {
				// White
				isResultWhite = true;
			}
			if (isWhite ^ isResultWhite) {
//				System.out.println("State changed At y = " + y);
				list.add(y);
				isWhite = !isWhite;
			}
		}
		return list;
	}
	
	int getSimilarNumber(BufferedImage img, int which) {
		int[] diff = getCompareDiffArray(img);
		numSimilarity[which] = calcSimilarity(diff);
		System.out.println(Arrays.toString(diff) + ", "+numSimilarity[which]);
		int min = 1000, index = -1;
		for (int n = 0; n <=9; n++) {
			if (diff[n] < min) {
				index = n;
				min = diff[n];
			}
		}
		return index;
	}
	
	/**
	 * 获取偏差值数组
	 * @param img 待比较的图像
	 * @return 分别与0,1,...,9模板相比，像素的偏差数目（越小表示越接近）
	 */
	int[] getCompareDiffArray(BufferedImage img) {
		int[] diff = new int[10];
		for (int n = 0; n <= 9; n++) {
			BufferedImage model = readFromFile(n + ".png");
			diff[n] = compare(img, model);
		}
		return diff;
	}
	
	/**
	 * 根据相似程度计算可信概率
	 * @param diff 分别与0,1,...,9模板相比，像素的偏差数目
	 * @return 图片为某个数的概率，通常在95%以上即任务可信
	 */
	double calcSimilarity(int[] diff) {
		int min = 1000, index = -1;
		for (int n = 0; n <=9; n++) {
			if (diff[n] < min) {
				index = n;
				min = diff[n];
			}
		}
		if (min == 0) return 1;
		double sum = 0; double[] d = new double[10];
		for (int n = 0; n <=9; n++) {
			d[n] = diff[n] * 1.0 - min;
		}
		for (int n = 0; n <=9; n++) {
			sum += Math.exp(-d[n]);
		}
		return Math.exp(0) / sum;
	}
	
	/**
	 * 将图片做逐差比较（包括位移），返回最小像素误差
	 * @param img 待比较图片
	 * @param model 模板图片
	 * @return 最小可能的像素误差
	 */
	static int compare(BufferedImage img, BufferedImage model) {
		int count = 0, min = 1000, x, y, xoffset, yoffset;
		int width = img.getWidth();
		int height = img.getHeight();
		int widthModel = model.getWidth();
		int heightModel = model.getHeight();
		int xOffsetRelative = width - widthModel,   xStep = xOffsetRelative >= 0 ? 1 : -1;
		int yOffsetRelative = height - heightModel, yStep = yOffsetRelative >= 0 ? 1 : -1;
		
		// 两个for循环遍历位移
		for (xoffset = 0; Math.abs(xoffset) <= Math.abs(xOffsetRelative); xoffset += xStep) {
			for (yoffset = 0; Math.abs(yoffset) <= Math.abs(yOffsetRelative); yoffset += yStep) {
				// Compare Process
				count = 0;
				for (x = 0; x < widthModel; x++) {
					for (y = 0; y < heightModel; y++) {
						int xx = x + xoffset, yy = y +yoffset;
						if (0<=xx && xx<width && 0<=yy && yy<height)
							if(img.getRGB(xx, yy) != model.getRGB(x, y)) 
								count++;
					}
				}
				if (count < min) min = count;
//				System.out.println(String.format("[DEBUG]xoffset=%d,yoffset=%d,diff=%d",xoffset,yoffset,count));
			}
		}
//		System.out.println("[DEBUG]  ======");
		return min;
	}
	
	static boolean isWhite(int rgb) {
		return (rgb & 0xffffff) == 0xffffff;
	}
	
	static boolean isBlack(int rgb) {
		return (rgb & 0xffffff) == 0;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}

	public static String urlSrc = "http://p.nju.edu.cn/img.html";
}
