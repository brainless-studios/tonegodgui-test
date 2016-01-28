/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import tonegod.gui.core.Screen;
import tonegod.gui.tests.Main;

/**
 *
 * @author t0neg0d
 */
public abstract class AppStateCommon extends AbstractAppState {
	protected String displayName = "Display Name";
	protected boolean show = true;
	protected Main main;
	protected Screen screen;
	protected boolean init = false;
	
	public AppStateCommon(Main main) {
		this.main = main;
		this.screen = main.getScreen();
	}
	
	public abstract void reshape();
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		initState();
	}
	
	protected abstract void initState();
	
	@Override
	public void update(float tpf) { updateState(tpf); }
	
	public abstract void updateState(float tpf);
	
	@Override
	public void cleanup() {
		super.cleanup();
		cleanupState();
	}
	
	public abstract void cleanupState();
	
	public String getDisplayName() {
		return this.displayName;
	}

	public boolean showInList() {
		return show;
	}
}
