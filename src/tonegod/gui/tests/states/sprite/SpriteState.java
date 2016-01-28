/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.states.sprite;

import tonegod.gui.tests.controls.AnimatedButton;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.buttons.RadioButtonGroup;
import tonegod.gui.core.Element;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.states.AppStateCommon;
import tonegod.gui.tests.controls.CollapsePanel;

/**
 *
 * @author t0neg0d
 */
public class SpriteState extends AppStateCommon {
	private CollapsePanel cPanel;
	Element content;
	RadioButtonGroup rbg;
	AnimatedButton spriteButton1, spriteButton2, spriteButton3;
	
	public SpriteState(Main main) {
		super(main);
		displayName ="Sprites";
		show = true;
	}
	
	@Override
	public void reshape() {
		if (init) {
			content.centerToParent();
		}
	}

	@Override
	protected void initState() {
		if (!init) {
			initCtrlPanel();
			
			rbg = new RadioButtonGroup(screen) {
				@Override
				public void onSelect(int index, Button value) {
					
				}
			};
			
			content = new Element(screen, UIDUtil.getUID(), Vector2f.ZERO, Vector2f.ZERO, Vector4f.ZERO, null);
			content.setAsContainerOnly();
			
			LayoutHelper.reset();
			
			spriteButton1 = new AnimatedButton(screen, LayoutHelper.position());
			spriteButton1.setText("A Few");
			rbg.addButton(spriteButton1);
			content.addChild(spriteButton1);
			
			LayoutHelper.advanceY(spriteButton1);
			spriteButton2 = new AnimatedButton(screen, LayoutHelper.position());
			spriteButton2.setText("Animated");
			rbg.addButton(spriteButton2);
			content.addChild(spriteButton2);
			
			LayoutHelper.advanceY(spriteButton2);
			spriteButton3 = new AnimatedButton(screen, LayoutHelper.position());
			spriteButton3.setText("Buttons");
			rbg.addButton(spriteButton3);
			content.addChild(spriteButton3);
			
			content.sizeToContent();
			screen.addElement(content, true);
			content.centerToParent();
			
			init = true;
		}
		cPanel.setPosition(Vector2f.ZERO);
		main.getTests().addCtrlPanel(cPanel);
		content.show();
	}
	
	private void initCtrlPanel() {
		cPanel = new CollapsePanel(main, displayName, screen, Vector2f.ZERO, Vector2f.ZERO);
		cPanel.pack();
	}

	@Override
	public void updateState(float tpf) {
		
	}

	@Override
	public void cleanupState() {
		main.getTests().removeCtrlPanel(cPanel);
		content.hide();
	}
}
