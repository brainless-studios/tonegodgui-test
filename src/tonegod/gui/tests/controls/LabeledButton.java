/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.controls;

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.ElementManager;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public class LabeledButton extends ButtonAdapter {
	private AnimatedText label;
	
	public LabeledButton(ElementManager screen, Vector2f position) {
		super(screen, position);
	//	setWidth(150);
		
		Effect in = new Effect(Effect.EffectType.ImageFadeIn, Effect.EffectEvent.Hover, .5f);
		addEffect(in);
		
		Effect out = new Effect(Effect.EffectType.ImageFadeOut, Effect.EffectEvent.LoseFocus, .5f);
		addEffect(out);
		
		label = new AnimatedText(screen, position, getDimensions());
		label.setTextWrap(LineWrapMode.NoWrap);
		label.setFontSize(screen.getStyle("Button").getFloat("fontSize"));
		label.setIgnoreMouse(true);
		label.setAnimType(0);
		label.setAnimTime(.05f);
		label.setSizeToText(false);
		label.setTextAlign(Align.Center);
		label.setTextVAlign(VAlign.Center);
		
		addChild(label);
		
		label.centerToParent();
	}
	
	
	@Override
	public void onButtonFocus(MouseMotionEvent evt) {
		screen.getGUINode().addControl(this);
		label.onGetFocus(evt);
		label.setFontColor(hoverFontColor);
	}
	
	@Override
	public void onButtonLostFocus(MouseMotionEvent evt) {
		label.onLoseFocus(evt);
		label.reset();
		label.update(0);
		screen.getGUINode().removeControl(this);
		label.setFontColor(fontColor);
	}
	
	@Override
	public void update(float tpf) {
		label.update(tpf);
	}
	
	@Override
	public void setText(String text) {
		label.setText(text);
	}
}
