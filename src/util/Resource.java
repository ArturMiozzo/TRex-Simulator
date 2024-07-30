package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

// classe com metodos estaticos para administrar os recursos
// Sprites e arquivos de disco
public class Resource {
	
	private static final int NUMBER_WIDTH = 20;
	private static final int NUMBER_HEIGHT = 21;

	public static final String RESOURCES_PATH = "resources";
	
	private static final String[] BIRD_FILE = { "bird-fly-1.png", "bird-fly-2.png" };
	private static final String[] CACTUS_FILE = { "cactus-1.png", "cactus-2.png", "cactus-3.png", "cactus-4.png", "cactus-5.png", "cactus-6.png", "cactus-7.png", "cactus-8.png", "cactus-9.png" };
	private static final String[] DINO_RUN_FILE = { "dino-run-1.png", "dino-run-2.png" };
	private static final String[] DINO_RUN_DOWN_FILE = { "dino-down-run-1.png", "dino-down-run-2.png" };
	private static final String DINO_JUMP_FILE = "dino-jump.png";
	private static final String DINO_DEAD_FILE = "dino-dead.png";
	private static final String LAND_FILE = "land.png";
	private static final String CLOUD_FILE = "cloud.png";

	private static final String HI_FILE = "hi.png";
	private static final String NUMBERS_FILE = "numbers.png";
	private static final String INTRO_FILE = "intro-text.png";
	private static final String GAME_OVER_FILE = "game-over.png";
	private static final String REPLAY_FILE = "replay.png";

	public static final BufferedImage[] BIRD_SPRITE = loadResource(BIRD_FILE);
	public static final BufferedImage[] CACTUS_SPRITE = loadResource(CACTUS_FILE);
	public static final BufferedImage[] DINO_RUN_SPRITE = loadResource(DINO_RUN_FILE);
	public static final BufferedImage[] DINO_RUN_DOWN_SPRITE = loadResource(DINO_RUN_DOWN_FILE);
	public static final BufferedImage DINO_JUMP_SPRITE = loadResource(DINO_JUMP_FILE);
	public static final BufferedImage DINO_DEAD_SPRITE = loadResource(DINO_DEAD_FILE);
	public static final BufferedImage LAND_SPRITE = loadResource(LAND_FILE);
	public static final BufferedImage CLOUD_SPRITE = loadResource(CLOUD_FILE);
	
	public static final BufferedImage HI_SPRITE = loadResource(HI_FILE);
	public static final BufferedImage[] NUMBERS_SPRITE = loadResource(NUMBERS_FILE, NUMBER_WIDTH, NUMBER_HEIGHT, 10);
	public static final BufferedImage INTRO_SPRITE = loadResource(INTRO_FILE);
	public static final BufferedImage GAME_OVER_SPRITE = loadResource(GAME_OVER_FILE);
	public static final BufferedImage REPLAY_SPRITE = loadResource(REPLAY_FILE);
	
	public static BufferedImage getImage(String path) {
		File file = new File(path);
		BufferedImage image = null;
		try {
			if(file.exists())
				image = ImageIO.read(file);
			else {
				path = path.substring(path.indexOf("/") + 1);
				image = ImageIO.read(ClassLoader.getSystemClassLoader().getResource(path));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	private static BufferedImage loadResource(String resourceFile) {
		Path resourcePath = Paths.get(RESOURCES_PATH, resourceFile);
		return getImage(resourcePath.toString());
	}
	
	private static BufferedImage[] loadResource(String[] resourceFiles) {

		List<BufferedImage> imagesBuffer = new ArrayList<BufferedImage>();
		
		for(String resourceFile : resourceFiles) {
			Path resourcePath = Paths.get(RESOURCES_PATH, resourceFile);
			imagesBuffer.add(getImage(resourcePath.toString()));
		}

		return imagesBuffer.toArray(BufferedImage[]::new);
	}
	
	private static BufferedImage[] loadResource(String resourceFile, int cropWidth, int cropHeight, int count) {
		
		BufferedImage imageBuffer = loadResource(resourceFile);
		List<BufferedImage> imagesBuffer = new ArrayList<BufferedImage>();		

		for(int index=0; index<count; index++) {
			imagesBuffer.add(cropImage(imageBuffer, index, cropWidth, cropHeight));
		}

		return imagesBuffer.toArray(BufferedImage[]::new);
	}
	
	private static BufferedImage cropImage(BufferedImage image, int index, int cropWidth, int cropHeight) {
		return image.getSubimage(index * cropWidth, 0, cropWidth, cropHeight);
	}
}
