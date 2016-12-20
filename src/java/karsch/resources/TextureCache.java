package karsch.resources;

import java.awt.Font;
import java.util.HashMap;

import karsch.Values;

import com.jme3.texture.Texture;

// TODO interface texture caching
// TODO font handling
public class TextureCache {
	private static TextureCache instance;
	// private final HashMap<String, ITexture> itextures;
	private final HashMap<String, Texture> textures;

	private Font coolAWTSerifFont, coolAWTSerifFont2, standardFont;

	private TextureCache() {
		// itextures = new HashMap<String, ITexture>();
		textures = new HashMap<String, Texture>();
	}

	public static TextureCache getInstance() {
		if (instance == null)
			instance = new TextureCache();
		return instance;
	}

	public Texture getTexture(final String fileName) {
		if (textures.containsKey(fileName)) {
			return textures.get(fileName);
		}

		Texture tex = Values.getInstance().getAssetManager().loadTexture(fileName);

		if (tex == null) {
			tex = Values.getInstance().getAssetManager()
					.loadTexture(fileName.toUpperCase());
		}

		textures.put(fileName, tex);
		return tex;
	}

	// public ITexture getITexture(final String fileName) {
	// if (itextures.containsKey(fileName)) {
	// return itextures.get(fileName);
	// }
	//
	// ITexture tex = null;
	//
	// try {
	// tex = Binding.getInstance().getTexture(
	// ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE,
	// fileName));
	// if (tex == null) {
	// tex = Binding.getInstance().getTexture(
	// ResourceLocatorTool.locateResource(
	// ResourceLocatorTool.TYPE_TEXTURE, fileName.toUpperCase()));
	// }
	//
	// } catch (final IOException e) {
	// e.printStackTrace();
	// }
	// itextures.put(fileName, tex);
	// return tex;
	// }
	//
	// public ITexture getScaledITexture(final String fileName, final int width,
	// final int height) {
	// final String key = fileName + "_" + width + "_" + height;
	// if (itextures.containsKey(key)) {
	// return itextures.get(key);
	// }
	//
	// ITexture tex = null;
	//
	// try {
	// BufferedImage image = ImageIO.read(ResourceLocatorTool.locateResource(
	// ResourceLocatorTool.TYPE_TEXTURE, fileName));
	// if (image == null) {
	// image = ImageIO.read(ResourceLocatorTool.locateResource(
	// ResourceLocatorTool.TYPE_TEXTURE, fileName.toUpperCase()));
	// }
	//
	// final Image newImg = image.getScaledInstance(width, height,
	// Image.SCALE_AREA_AVERAGING);
	//
	// final BufferedImage bi = new BufferedImage(width, height,
	// BufferedImage.TYPE_INT_ARGB);
	// final Graphics2D biContext = bi.createGraphics();
	// biContext.drawImage(newImg, 0, 0, null);
	//
	// biContext.dispose();
	//
	// tex = Binding.getInstance().getTexture(bi);
	//
	// } catch (final IOException e) {
	// e.printStackTrace();
	// }
	// itextures.put(key, tex);
	// return tex;
	// }
	//
	// public Font getBoldFont() {
	// if (coolAWTSerifFont == null) {
	// final java.awt.Font awtFont = new java.awt.Font("Helvetica",
	// java.awt.Font.BOLD, 24);
	//
	// final FontFactory ff = new FontFactory(Alphabet.GERMAN, awtFont);
	// final AssemblyLine line = ff.getAssemblyLine();
	//
	// final Paint redYellowPaint = new java.awt.GradientPaint(0, 0,
	// java.awt.Color.RED, 15, 15, java.awt.Color.YELLOW, true);
	//
	// line.addStage(new Clear());
	// line.addStage(new DrawCharacter(java.awt.Color.WHITE, false));
	//
	// line.addStage(new BinaryDilation(java.awt.Color.BLACK, 3));
	// line.addStage(new DrawCharacter(java.awt.Color.WHITE, true));
	// line.addStage(new PixelReplacer(redYellowPaint, java.awt.Color.WHITE));
	//
	// coolAWTSerifFont = ff.createFont();
	// }
	// return coolAWTSerifFont;
	// }
	//
	// public Font getFont() {
	// if (coolAWTSerifFont2 == null) {
	// final java.awt.Font awtFont = new java.awt.Font("Helvetica",
	// java.awt.Font.PLAIN, 18);
	//
	// final FontFactory ff = new FontFactory(Alphabet.GERMAN, awtFont);
	// final AssemblyLine line = ff.getAssemblyLine();
	//
	// final Paint redYellowPaint = new java.awt.GradientPaint(-1, -1,
	// java.awt.Color.RED, 10, 10, java.awt.Color.YELLOW, true);
	//
	// line.addStage(new Clear());
	// line.addStage(new DrawCharacter(java.awt.Color.WHITE, true));
	//
	// line.addStage(new BinaryDilation(java.awt.Color.BLACK, 2));
	// line.addStage(new DrawCharacter(java.awt.Color.WHITE, true));
	// line.addStage(new PixelReplacer(redYellowPaint, java.awt.Color.WHITE));
	//
	// coolAWTSerifFont2 = ff.createFont();
	// }
	// return coolAWTSerifFont2;
	// }
	//
	// public Font getStandardFont() {
	// if (standardFont == null) {
	// final java.awt.Font awtFont = new java.awt.Font("Helvetica",
	// java.awt.Font.BOLD, 20);
	//
	// final FontFactory ff = new FontFactory(Alphabet.GERMAN, awtFont);
	// final AssemblyLine line = ff.getAssemblyLine();
	//
	// // Paint redYellowPaint = new java.awt.GradientPaint(-1, -1,
	// // java.awt.Color.RED, 10, 10, java.awt.Color.YELLOW, true);
	//
	// // line.addStage(new Clear());
	// // line.addStage(new DrawCharacter(java.awt.Color.WHITE, true));
	// //
	// // line.addStage(new BinaryDilation(java.awt.Color.BLACK, 2));
	// line.addStage(new DrawCharacter(java.awt.Color.BLACK, true));
	// // line.addStage(new PixelReplacer(redYellowPaint, java.awt.Color.WHITE));
	//
	// standardFont = ff.createFont();
	// }
	// return standardFont;
	// }

}
