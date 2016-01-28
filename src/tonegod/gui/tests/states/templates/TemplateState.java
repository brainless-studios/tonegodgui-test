package tonegod.gui.tests.states.templates;

import com.jme3.math.Vector2f;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.controls.CollapsePanel;
import tonegod.gui.tests.states.AppStateCommon;

/**
 * A template to be used when creating a new Test.
 * >> Copy 
 * >> Paste Re-factored Copy and rename to the new test's name
 * 
 * NOTE: Don't forget to change the displayName field in the constructor.
 * 
 * Once your test is set up, add it to the Main class as follows:
 * 
 * 1. In the VARIABLES section add a field specific to your test
 * TemplateState tempState;
 * 
 * 2. In the initStates() methods, initialize your new test and
 *    add it to the states list:
 * tempState = new TemplateState(this);
 * states.add(tempState);
 * 
 * Run the application and your test will be available to load
 * from the available tests SelectBox under the TEST SETTINGS
 * section of the left harness control panel.
 * 
 * @author t0neg0d
 */
public class TemplateState extends AppStateCommon {
	private CollapsePanel cPanel;
	
	public TemplateState(Main main) {
		super(main);
		// Set the display name for the app state
		displayName = "Template";
		// Set the app state to managed (i.e. will display in list of available tests)
		show = true;
	}
	
	@Override
	public void reshape() {
		// Be sure to check if controls are currently null
	}
	
	@Override
	protected void initState() {
		if (!init) {
			// Initialize the control panel
			initCtrlPanel();
			
			// Initialize controls here
			
			init = true;
		}
		main.getTests().addCtrlPanel(cPanel);
		// Show controls here
	}
	
	private void initCtrlPanel() {
		// Create the collapsable control panel
		cPanel = new CollapsePanel(main, displayName, screen, Vector2f.ZERO, Vector2f.ZERO);
		
		// Reset the initial position of the LayoutHelper
		LayoutHelper.reset();
		
		// Add control panel content here
		// cPanel.getContentArea().addChild(el);
		
		// Reshapes the control panel
		cPanel.pack();
	}

	@Override
	public void updateState(float tpf) {
		
	}

	@Override
	public void cleanupState() {
		main.getTests().removeCtrlPanel(cPanel);
		// Hide controls here
	}
}
