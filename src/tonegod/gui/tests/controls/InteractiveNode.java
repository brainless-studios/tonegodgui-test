/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.controls;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;
import tonegod.gui.framework.animation.Interpolation;
import tonegod.gui.framework.core.util.GameTimer;
import tonegod.gui.listeners.MouseButtonListener;
import tonegod.gui.listeners.MouseFocusListener;
import tonegod.gui.style.StyleManager;

/**
 *
 * @author t0neg0d
 */
public abstract class InteractiveNode extends Node implements MouseFocusListener, MouseButtonListener {
	ElementManager screen;
	GameTimer timer;
	Effect fx;
	boolean xDir = true;
	boolean yDir = false;
	float xAmount = 0.5f;
	float yAmount = 0.5f;
	Material defMat, hlMat;
	String toolTipText;
	String icon;
	boolean isInScene = true;
	
	public InteractiveNode(ElementManager screen) {
		this.screen = screen;
		
		initTimer();
	}
	
	private void initTimer() {
		timer = new GameTimer(0.5f) {
			@Override
			public void onComplete(float time) {
				xDir = !xDir;
				yDir = !yDir;
			}
			@Override
			public void timerUpdateHook(float tpf) {
				scaleSpatial(this.getPercentComplete());
			}
		};
		timer.setAutoRestart(true);
		timer.setInterpolation(Interpolation.bounceOut);
	}
	
	public void scaleSpatial(float percent) {
		float percentX = (xDir) ? percent : 1f-percent;
		float percentY = (yDir) ? percent : 1f-percent;
		if (timer.getRunCount() == 0)
			setLocalScale(1+(xAmount*percentX),1,1);
		else
			setLocalScale(1+(xAmount*percentX),1+(yAmount*percentY),1);
	}
	
	public void setDefaultMaterial(Material mat) {
		this.defMat = mat;
		setMaterial(defMat);
	}
	
	public void setHighlightMaterial(Material mat) {
		this.hlMat = mat;
	}
	
	public void setToolTipText(String text) {
		this.toolTipText = text;
	}
	
	public String getToolTipText() { return this.toolTipText; }
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getIcon() { return this.icon; }
	
	public void setIsInScene(boolean isInScene) {
		this.isInScene = isInScene;
	}
	
	public boolean getIsInScene() { return isInScene; }
	
	public void onGetFocus(MouseMotionEvent evt) {
		if (!screen.getAnimManager().hasGameTimer(timer)) {
			timer.resetRunCount();
			timer.reset(false);
			timer.setAutoRestart(true);
			xDir = true;
			yDir = false;
			screen.getAnimManager().addGameTimer(timer);
		}
		setMaterial(hlMat);
		((Screen)screen).setForcedCursor(StyleManager.CursorType.HAND);
		if (screen.getUseToolTips() && toolTipText != null)
			((Screen)screen).setForcedToolTip(toolTipText);
	}
	public void onLoseFocus(MouseMotionEvent evt) {
		timer.setAutoRestart(false);
		timer.endGameTimer();
		setLocalScale(1);
		setMaterial(defMat);
		((Screen)screen).releaseForcedCursor();
		((Screen)screen).releaseForcedToolTip();
		
	}
	public void onMouseLeftPressed(MouseButtonEvent evt) {
		onSpatialMouseDown(evt);
	}
	public void onMouseLeftReleased(MouseButtonEvent evt) {
		onSpatialMouseUp(evt);
	}
	public void onMouseRightPressed(MouseButtonEvent evt) {  }
	public void onMouseRightReleased(MouseButtonEvent evt) {  }
	
	public abstract void onSpatialMouseDown(MouseButtonEvent evt);
	public abstract void onSpatialMouseUp(MouseButtonEvent evt);
}
