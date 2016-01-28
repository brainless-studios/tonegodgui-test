/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.states.text;

import com.jme3.font.BitmapFont.Align;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.core.Element;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.states.AppStateCommon;
import tonegod.gui.tests.controls.AnimatedText;
import tonegod.gui.tests.controls.CollapsePanel;

/**
 *
 * @author t0neg0d
 */
public class AnimatedTextState extends AppStateCommon {
	CollapsePanel cPanel;
	Element content;
	AnimatedText animatedText;
	
	public AnimatedTextState(Main main) {
		super(main);
		displayName = "Animated Text";
		show = true;
	}
	
	@Override
	public void reshape() {
		if (animatedText != null)
			LayoutHelper.reposition(screen, main.getHarness().getPreviousScreenSize(), animatedText);
	}

	@Override
	protected void initState() {
		if (!init) {
			animatedText = new AnimatedText(screen, Vector2f.ZERO);
			animatedText.setText("<p align=Center>I'm animated!</br></br>Mouse over me to start</br>the animation. </p>");
			animatedText.setTextAlign(Align.Center);
			screen.addElement(animatedText, true);
			animatedText.centerToParent();
			
			initCtrlPanel();
			
			init = true;
		}
		main.getTests().addCtrlPanel(cPanel);
		animatedText.show();
	}
	
	private void initCtrlPanel() {
		cPanel = new CollapsePanel(main, displayName, screen, Vector2f.ZERO, Vector2f.ZERO);
		
		LayoutHelper.reset();
		
		SelectBox anims = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				animatedText.setAnimType((Integer)value);
			}
		};
		anims.setDocking(Element.Docking.SW);
		anims.addListItem("Scale By", 0);
		anims.addListItem("Rotate By", 1);
		anims.addListItem("Both", 2);
		anims.addListItem("Both Offset", 3);
		cPanel.getContentArea().addChild(anims);
		cPanel.pack();
	}

	@Override
	public void updateState(float tpf) {
		if (animatedText != null)
			animatedText.update(tpf);
	}

	@Override
	public void cleanupState() {
		main.getTests().removeCtrlPanel(cPanel);
		animatedText.hide();
	}
}
