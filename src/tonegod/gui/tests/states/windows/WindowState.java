/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.states.windows;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.controls.windows.DialogBox;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Element.Docking;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.effects.Effect;
import tonegod.gui.framework.animation.Interpolation;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.states.AppStateCommon;
import tonegod.gui.tests.controls.CollapsePanel;
import tonegod.gui.tests.layout.ScaleHelper;

/**
 *
 * @author t0neg0d
 */
public class WindowState extends AppStateCommon {
	private CollapsePanel cPanel;
	private Window win;
	private AlertBox alert;
	private DialogBox dialog;
	private ButtonAdapter btnClose;
	private Element selectedWindow;
	private Effect fxIn, fxOut;
	
	public WindowState(Main main) {
		super(main);
		displayName = "Windows";
		show = true;
	}
	
	@Override
	public void reshape() {
		if (init) {
			ScaleHelper.setDevEnvironment(screen, 720, 480);
			LayoutHelper.reposition(screen, main.getHarness().getPreviousScreenSize(), win);
			ScaleHelper.scale(main.getHarness().getPreviousScreenSize(),win);
		}
	}

	@Override
	protected void initState() {
		if (!init) {
			LayoutHelper.reset();
			
			initCtrlPanel();
			initTestWindow();
			
			selectedWindow = win;
			
			init = true;
		}
		cPanel.setPosition(Vector2f.ZERO);
		main.getTests().addCtrlPanel(cPanel);
	}
	
	private void initCtrlPanel() {
		cPanel = new CollapsePanel(main, displayName, screen, Vector2f.ZERO, Vector2f.ZERO);
		
		LayoutHelper.reset();
		
		SelectBox windows = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (selectedWindow != null) {
					switch (selectedIndex) {
						case 0:
							if (win != null)
								selectedWindow = win;
							break;
					}
				}
			}
		};
		windows.setDocking(Docking.SW);
		windows.addListItem("Window", 0);
		cPanel.getContentArea().addChild(windows);
		
		LayoutHelper.advanceY(windows,true);
		SelectBox fxInSelect = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (selectedWindow != null) {
					switch (selectedIndex) {
						case 0:
							fxIn = new Effect(Effect.EffectType.FadeIn, Effect.EffectEvent.Show, .5f);
							selectedWindow.addEffect(fxIn);
							break;
						case 1:
							fxIn = new Effect(Effect.EffectType.SlideIn, Effect.EffectEvent.Show, .75f);
							fxIn.setInterpolation(Interpolation.exp5Out);
							selectedWindow.addEffect(fxIn);
							break;
						case 2:
							fxIn = new Effect(Effect.EffectType.SlideIn, Effect.EffectEvent.Show, 1f);
							fxIn.setInterpolation(Interpolation.bounceOut);
							selectedWindow.addEffect(fxIn);
							break;
					}
				}
			}
		};
		fxInSelect.setDocking(Docking.SW);
		fxInSelect.addListItem("Fade In", 0);
		fxInSelect.addListItem("Slide In", 1);
		fxInSelect.addListItem("Bounce In", 2);
		cPanel.getContentArea().addChild(fxInSelect);
		
		LayoutHelper.advanceY(fxInSelect,true);
		SelectBox fxOutSelect = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (selectedWindow != null) {
					switch (selectedIndex) {
						case 0:
							fxOut = new Effect(Effect.EffectType.FadeOut, Effect.EffectEvent.Hide, .5f);
							selectedWindow.addEffect(fxOut);
							break;
						case 1:
							fxOut = new Effect(Effect.EffectType.SlideOut, Effect.EffectEvent.Hide, .75f);
							fxOut.setInterpolation(Interpolation.exp5In);
							selectedWindow.addEffect(fxOut);
							break;
						case 2:
							fxOut = new Effect(Effect.EffectType.SlideOut, Effect.EffectEvent.Hide, 1f);
							fxOut.setInterpolation(Interpolation.bounceOut);
							selectedWindow.addEffect(fxOut);
							break;
					}
				}
			}
		};
		fxOutSelect.setDocking(Docking.SW);
		fxOutSelect.addListItem("Fade Out", "fadeOut");
		fxOutSelect.addListItem("Slide Out", "slideOut");
		fxOutSelect.addListItem("Bounce Out", "slideOut");
		cPanel.getContentArea().addChild(fxOutSelect);
		
		LayoutHelper.advanceY(fxOutSelect,true);
		ButtonAdapter btnShow = new ButtonAdapter(screen, LayoutHelper.position()) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!selectedWindow.getIsVisible())
					selectedWindow.showWithEffect();
			}
		};
		btnShow.setDocking(Docking.SE);
		btnShow.setText("Show");
		cPanel.getContentArea().addChild(btnShow);
		
		cPanel.pack();
		
		btnShow.setX(cPanel.getWidth()-btnShow.getWidth()-10);
		
	}
	
	private void initTestWindow() {
		// Create a test window
		win = new Window(screen, Vector2f.ZERO);
		win.setWindowTitle("Example Window");
		win.setLockToParentBounds(true);
		
		// Customize the window dragbar with controls
		btnClose = new ButtonAdapter(screen,
			Vector2f.ZERO,
			LayoutHelper.dimensions(20, 20)	
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggle) {
				win.hideWithEffect();
			}
		};
		btnClose.setDocking(Docking.SE);
		btnClose.setText("X");
		win.getDragBar().addChild(btnClose);
		btnClose.centerToParent();
		btnClose.setX(win.getDragBar().getWidth()-btnClose.getY()-btnClose.getWidth());
		screen.addElement(win, true);
	}
	
	@Override
	public void updateState(float tpf) {
		
	}

	@Override
	public void cleanupState() {
		main.getTests().removeCtrlPanel(cPanel);
		if (win.getIsVisible()) win.hideWithEffect();
	}
}
