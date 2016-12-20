package karsch.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Arrays;

import karsch.Values;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;

public class TextQuadUtils {

	private String text;

	private float blurIntensity = 0.1f;

	private int kernelSize = 5;

	private ConvolveOp blur;

	private Color foreground = new Color(1f, 0f, 0f);

	private Color background = new Color(0f, 0f, 0f, 0f);

	private float fontResolution = 40f;

	private int shadowOffsetX = 2;

	private int shadowOffsetY = 2;

	private float maxLineWidth = 0;

	private Font font = Font.decode(DEFAULT_FONT);

	private boolean drawShadow = false;

	public static final String DEFAULT_FONT = "Sans PLAIN 48";

	public TextQuadUtils(final String text) {
		this.text = text;
		updateKernel();
	}

	public void setFont(final Font font) {
		this.font = font;
	}

	public void setFont(final String fontString) {
		this.font = Font.decode(fontString);
	}

	public void setShadowOffsetX(final int offsetPixelX) {
		shadowOffsetX = offsetPixelX;
	}

	public void setShadowOffsetY(final int offsetPixelY) {
		shadowOffsetY = offsetPixelY;
	}

	public void setBlurSize(final int kernelSize) {
		this.kernelSize = kernelSize;
		updateKernel();
	}

	public void setBlurStrength(final float strength) {
		this.blurIntensity = strength;
		updateKernel();
	}

	public void setFontResolution(final float fontResolution) {
		this.fontResolution = fontResolution;
	}

	private void updateKernel() {
		final float[] kernel = new float[kernelSize * kernelSize];
		Arrays.fill(kernel, blurIntensity);
		blur = new ConvolveOp(new Kernel(kernelSize, kernelSize, kernel));
	}

	/**
	 * 
	 * @param scaleFactors
	 *          is set to the factors needed to adjust texture coords to the
	 *          next-power-of-two- sized resulting image
	 */
	private BufferedImage getImage(final Vector2f scaleFactors) {
		BufferedImage tmp0 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) tmp0.getGraphics();
		final Font drawFont = font.deriveFont(fontResolution);
		g2d.setFont(drawFont);
		final Rectangle2D b = g2d.getFontMetrics().getStringBounds(text, g2d);

		double lines = b.getWidth() / maxLineWidth;

		lines = Math.ceil(lines);

		float width;

		if (lines == 1f) {
			width = (float) b.getWidth();
		} else {
			width = maxLineWidth;
		}

		final int actualX = (int) width + kernelSize + 1 + Math.abs(shadowOffsetX);

		final int actualY = (int) (g2d.getFontMetrics().getHeight() * lines)
				+ kernelSize + 1 + Math.abs(shadowOffsetY);

		final int desiredX = FastMath.nearestPowerOfTwo(actualX);
		final int desiredY = FastMath.nearestPowerOfTwo(actualY);

		if (scaleFactors != null) {
			scaleFactors.x = (float) actualX / desiredX;
			scaleFactors.y = (float) actualY / desiredY;
		}

		tmp0 = new BufferedImage(desiredX, desiredY, BufferedImage.TYPE_INT_ARGB);

		g2d = (Graphics2D) tmp0.getGraphics();
		g2d.setFont(drawFont);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		final int textX = kernelSize / 2;
		final int textY = g2d.getFontMetrics().getMaxAscent() - kernelSize / 2;

		if (drawShadow) {
			g2d.setColor(background);
			g2d.drawString(text, textX + shadowOffsetX, textY + shadowOffsetY);
		}

		final BufferedImage ret = blur.filter(tmp0, null);

		g2d = (Graphics2D) ret.getGraphics();
		g2d.setFont(drawFont);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setColor(foreground);
		// g2d.drawString(text, textX, textY);

		final Point2D.Float pen = new Point2D.Float(textX, textY);

		final FontRenderContext frc = g2d.getFontRenderContext();

		// let styledText be an AttributedCharacterIterator containing at least
		// one character
		final AttributedString as = new AttributedString(text);
		as.addAttribute(TextAttribute.FONT, drawFont);
		final AttributedCharacterIterator styledText = as.getIterator();

		final LineBreakMeasurer measurer = new LineBreakMeasurer(styledText, frc);
		final float wrappingWidth = maxLineWidth;

		while (measurer.getPosition() < text.length()) {

			final TextLayout layout = measurer.nextLayout(wrappingWidth);

			pen.y += (layout.getAscent());
			final float dx = layout.isLeftToRight() ? 0 : (wrappingWidth - layout
					.getAdvance());

			layout.draw(g2d, pen.x + dx, pen.y);
			pen.y += layout.getDescent() + layout.getLeading();
		}

		return ret;
	}

	public Geometry getQuad(final float height) {
		final Vector2f scales = new Vector2f();
		final BufferedImage img = getImage(scales);
		final float w = img.getWidth() * scales.x;
		final float h = img.getHeight() * scales.y;
		final float factor = height / h;
		final Geometry ret = new Geometry("textLabel2d", new Quad(w * factor, h
				* factor));

		final Image image = new AWTLoader().load(img, true);

		final Material mat_tt = new Material(
				Values.getInstance().getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat_tt.setTexture("ColorMap", new Texture2D(image));
		mat_tt.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); // activate
																																			// transparency
		mat_tt.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		mat_tt.getAdditionalRenderState().setDepthTest(false);

		ret.setMaterial(mat_tt);

		// TODO WTF is this all?
		// final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
		// .createTextureState();
		// final Texture tex = TextureManager.loadTexture(img,
		// MinificationFilter.BilinearNoMipMaps, MagnificationFilter.Bilinear,
		// true);
		//
		// final TexCoords texCo = ret.getTextureCoords(0);
		// // texCo.coords = BufferUtils.createFloatBuffer(texCo.coords.limit());
		// texCo.coords.rewind();
		// for (int i = 0; i < texCo.coords.limit(); i += 2) {
		// final float u = texCo.coords.get(i);
		// final float v = texCo.coords.get(i + 1);
		// texCo.coords.put(i, u * scales.x);
		// texCo.coords.put(i + 1, v * scales.y);
		// }
		// ret.setTextureCoords(texCo);
		// ret.updateGeometricState(0, true);
		//
		// ts.setTexture(tex);
		// ts.setEnabled(true);
		// ret.setRenderState(ts);

		ret.setQueueBucket(Bucket.Transparent);
		ret.updateGeometricState();

		// final BlendState as = DisplaySystem.getDisplaySystem().getRenderer()
		// .createBlendState();
		// as.setBlendEnabled(true);
		// as.setTestEnabled(true);
		// as.setTestFunction(TestFunction.GreaterThan);
		// as.setEnabled(true);
		// ret.setRenderState(as);
		//
		// ret.setLightCombineMode(LightCombineMode.Off);
		// ret.updateRenderState();
		return ret;
	}

	public Node getBillboard(final float height) {
		final Node bb = new Node("bb");
		bb.addControl(new BillboardControl());
		final Geometry q = getQuad(height);
		bb.attachChild(q);
		return bb;
	}

	public void setForeground(final Color foreground) {
		this.foreground = foreground;
	}

	public void setBackground(final Color background) {
		this.background = background;
	}

	public void setText(final String text) {
		this.text = text;
		// updateKernel();
	}

	public void setDrawShadow(final boolean drawShadow) {
		this.drawShadow = drawShadow;
	}

	public float getMaxLineWidth() {
		return maxLineWidth;
	}

	public void setMaxLineWidth(final float maxLineWidth) {
		this.maxLineWidth = maxLineWidth;
	}
}
