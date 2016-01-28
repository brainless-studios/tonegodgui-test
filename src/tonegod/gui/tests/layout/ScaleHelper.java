/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.layout;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.text.TextElement;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class ScaleHelper {
	private static Vector2f devScale = new Vector2f();
	private static Vector2f fontScale = new Vector2f();
	private static Vector2f elementScale = new Vector2f();
	private static Vector2f dim1 = new Vector2f(),
							dim2 = new Vector2f();
	private static float refFontSize = 16;
	
	public static void setDevEnvironment(ElementManager screen, float refWidth, float refHeight) {
		devScale.set(refWidth,refHeight);
		refactorScale(screen);
	}
	
	private static void refactorScale(ElementManager screen) {
		factorElementScale(screen);
		factorFontScale(screen);
	}
	
	private static void factorElementScale(ElementManager screen) {
		float baseScale = screen.getWidth()/devScale.x;
		elementScale.set(baseScale, baseScale);
	}
	
	private static void factorFontScale(ElementManager screen) {
		dim1.set(devScale.x,devScale.y);
		TextElement refString = getTestLabel(screen, UIDUtil.getUID(),"Testing", dim1);
		refString.setFontSize(refFontSize);
		float refScale = refString.getAnimText().getLineWidth()/dim1.x;
		
		dim2.set(screen.getWidth(), devScale.y);
		TextElement testString = getTestLabel(screen, UIDUtil.getUID(),"Testing", dim2);
		float checkSize = 5;
		testString.setFontSize(checkSize);
		float testScale = testString.getAnimText().getLineWidth()/dim2.x;
		
		while (testScale < refScale) {
			checkSize++;
			testString.setFontSize(checkSize);
			testScale = testString.getAnimText().getLineWidth()/dim2.x;
		}
		
		fontScale.set(checkSize,checkSize);
	}
	
	private static TextElement getTestLabel(ElementManager screen, String UID, String text, Vector2f dim) {
		TextElement el = new TextElement(screen, UID, Vector2f.ZERO, new Vector2f(dim), null) {
			@Override
			public void onUpdate(float tpf) {  }
			@Override
			public void onEffectStart() {  }
			@Override
			public void onEffectStop() {  }
		};
		el.setIsResizable(false);
		el.setIsMovable(false);
		el.setUseTextClipping(false);
		el.setTextWrap(LineWrapMode.NoWrap);
		el.setTextVAlign(BitmapFont.VAlign.Center);
		el.setTextAlign(BitmapFont.Align.Center);
		el.setFont(((Screen)screen).getDefaultGUIFont());
		el.setFontColor(ColorRGBA.White);
		el.setFontSize(refFontSize);
		el.setText(text);
		el.setIgnoreMouse(true);
		el.getAnimText().setIgnoreMouse(true);
		return el;
	}
	
	public float elementScale() { return elementScale.x; }
	
	public float fontScale() { return fontScale.x; }
	
	public static void scale(Element el) {
		el.resize(el.getAbsoluteWidth()*elementScale.x, el.getAbsoluteHeight()*elementScale.x, Element.Borders.SE);
	}
	
	public static void scale(Vector2f previousResolution, Element el) {
		float scale = el.getScreen().getWidth()/previousResolution.x;
		el.resize(el.getAbsoluteX()+(el.getWidth()*scale), el.getAbsoluteY()+(el.getHeight()*scale), Element.Borders.SE);
	}
}
