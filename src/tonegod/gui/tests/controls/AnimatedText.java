/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.controls;

import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.text.LabelElement;
import tonegod.gui.core.ElementManager;
import tonegod.gui.framework.animation.RotateByAction;
import tonegod.gui.framework.animation.ScaleByAction;
import tonegod.gui.framework.core.QuadData;
import tonegod.gui.listeners.MouseFocusListener;

/**
 *
 * @author t0neg0d
 */
public class AnimatedText extends LabelElement implements MouseFocusListener {
	private boolean animate = false;
	private boolean reset = false;
	private float rollTime = .025f;
	private float altRollTime = .05f;
	private int animType = 1;
	
//	private float rotTime = .125f;
	
	public AnimatedText(ElementManager screen, Vector2f position) {
		this(screen, position, new Vector2f(200,200));
	}
	
	public AnimatedText(ElementManager screen, Vector2f position, Vector2f dimensions) {
		super(screen, position, dimensions);
		setFontSize(30);
		setTextWrap(LineWrapMode.Word);
		setIgnoreMouse(false);
		setIsMovable(true);
		setSizeToText(true);
		reset();
	}
	
	public void onGetFocus(MouseMotionEvent evt) {
		animate = true;
	}
	
	public void onLoseFocus(MouseMotionEvent evt) {
		animate = false;
		reset = true;
	}
	
	public void setAnimType(int index) {
		animType = index;
	}
	
	public void setAnimTime(float rollTime) {
		this.rollTime = rollTime;
	}
	
	@Override
	public void onUpdate(float tpf) {
		if (animate) {
			animateText(tpf);
			animateText2(tpf);
		} else if (reset) {
			reset();
			reset = false;
		}
	}
	
	public void reset() {
		getAnimText().setUserData("qdIndex", (Integer)0);
		getAnimText().setUserData("rollCounter", (Float)0.0f);
		getAnimText().setUserData("qdIndex2", (Integer)0);
		getAnimText().setUserData("rollCounter2", (Float)0.0f);
		for (QuadData dq : getAnimText().getQuads().values()) {
			dq.actions.clear();
			dq.setScale(1, 1);
			dq.setRotation(0);
		}
	}
	
	private void animateText(float tpf) {
		int qdIndex = 0;
		
		Object test = getAnimText().getUserData("qdIndex");
		if (test != null)
			qdIndex = (Integer)test;
		float rollCounter = 0;
		test = getAnimText().getUserData("rollCounter");
		if (test != null)
			rollCounter = (Float)test;
		rollCounter += tpf;
		if (rollCounter >= rollTime) {
			if (getAnimText().getQuadDataAt(qdIndex).getScale().x == 1 &&
				getAnimText().getQuadDataAt(qdIndex).getScale().y == 1) {
				if (animType == 0 || animType == 2 || animType == 3) {
					ScaleByAction sScale = new ScaleByAction();
					sScale.setAmount(.75f, 1.05f);
					sScale.setDuration(.25f);
					sScale.setAutoReverse(true);
					getAnimText().getQuadDataAt(qdIndex).addAction(sScale);
				}
				qdIndex++;
				if (qdIndex == getAnimText().getQuads().size())
					qdIndex = 0;
				getAnimText().setUserData("qdIndex", (Integer)qdIndex);
				rollCounter = 0;
			}
		}
		getAnimText().setUserData("rollCounter", (Float)rollCounter);
	}
	
	private void animateText2(float tpf) {
		int qdIndex = 0;
		
		Object test = getAnimText().getUserData("qdIndex2");
		if (test != null)
			qdIndex = (Integer)test;
		float rollCounter = 0;
		test = getAnimText().getUserData("rollCounter2");
		if (test != null)
			rollCounter = (Float)test;
		rollCounter += tpf;
		if (rollCounter >= ((animType == 3) ? altRollTime : rollTime)) {
			if (getAnimText().getQuadDataAt(qdIndex).getRotation() == 0 ||
				getAnimText().getQuadDataAt(qdIndex).getRotation() < -359) {
				if (animType == 1 || animType == 2 || animType == 3) {
					getAnimText().getQuadDataAt(qdIndex).setRotation(0);
					RotateByAction sRoll = new RotateByAction();
					sRoll.setAmount(-360);
					sRoll.setDuration(.25f);
					getAnimText().getQuadDataAt(qdIndex).addAction(sRoll);
				}
				qdIndex++;
				if (qdIndex == getAnimText().getQuads().size())
					qdIndex = 0;
				getAnimText().setUserData("qdIndex2", (Integer)qdIndex);
				rollCounter = 0;
			}
		}
		getAnimText().setUserData("rollCounter2", (Float)rollCounter);
	}
}
