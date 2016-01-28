/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.controls;

import com.jme3.font.BitmapFont.Align;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.texture.Texture;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.extras.SpriteElement;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class AnimatedButton extends ButtonAdapter {
	SpriteElement sprite;
	Element light;
	String lightOff = "x=0|y=0|w=32|h=32";
	String lightOn = "x=32|y=0|w=32|h=32";
	Texture texLight;
	
	public AnimatedButton(ElementManager screen, Vector2f position) {
		super(screen, position, new Vector2f(150,30));
		
		sprite = new SpriteElement(screen, UIDUtil.getUID(), Vector2f.ZERO, new Vector2f(50,50), Vector4f.ZERO, null) {
			@Override
			public void updateSpriteHook() {
				setAlphaMap("x=64|y=0|w=64|h=64");
			}
		};
		Texture animTex = screen.createNewTexture("Textures/Sprites/btnGears.png");
		sprite.setSprite(animTex, 2, 2, 20);
		sprite.setFrames(new int[] { 0, 1, 2 });
		sprite.setIgnoreMouse(true);
		sprite.setScaleEW(false);
		sprite.setScaleNS(false);
		sprite.setIsEnabled(false);
		sprite.setClippingLayer(this);
		sprite.setAlphaMap("x=64|y=0|w=64|h=64");
		addChild(sprite);
		sprite.centerToParent();
		sprite.setX(0);
		
		texLight = screen.createNewTexture("Textures/Sprites/btnLight.png");
		light = new Element(screen, UIDUtil.getUID(), Vector2f.ZERO, new Vector2f(getHeight()/4*3, getHeight()/4*3), Vector4f.ZERO, null);
		light.setTextureAtlasImage(texLight, lightOff);
		light.setScaleEW(false);
		light.setScaleNS(false);
		light.setIgnoreMouse(true);
		addChild(light);
		light.centerToParent();
		light.setX(getWidth()-(getHeight()/4*3));
		
		setIsToggleButton(true);
		
		super.setText("Test");
		getTextElement().setAlignment(Align.Left);
		setTextPosition(52, getTextPosition().y);
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		getTextElement().setAlignment(Align.Left);
	//	setTextPosition(52, getTextPosition().y);
	}
	
	@Override
	public void onButtonFocus(MouseMotionEvent evt) {
		sprite.setIsEnabled(true);
	}
	
	@Override
	public void onButtonLostFocus(MouseMotionEvent evt) {
		sprite.setIsEnabled(false);
	}
	
	@Override
	public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean toggled) {
		light.setTextureAtlasImage(texLight, toggled ? lightOn : lightOff);
	}
}
