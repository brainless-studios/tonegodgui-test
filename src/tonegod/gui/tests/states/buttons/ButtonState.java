/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.states.buttons;

import com.jme3.math.Vector2f;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.controls.CollapsePanel;
import tonegod.gui.tests.states.AppStateCommon;

/**
 *
 * @author t0neg0d
 */
public class ButtonState extends AppStateCommon {
	CollapsePanel cPanel;
	
	public ButtonState(Main main) {
		super(main);
		displayName = "Buttons";
		show = true;
	}
	
	@Override
	public void reshape() {
		
	}

	@Override
	protected void initState() {
		if (!init) {
			initCtrlPanel();
			
			init = true;
		}
		main.getTests().addCtrlPanel(cPanel);
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
	}
}
