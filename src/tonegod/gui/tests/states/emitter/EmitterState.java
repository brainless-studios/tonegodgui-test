package tonegod.gui.tests.states.emitter;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.extras.android.Joystick;
import tonegod.gui.controls.extras.emitter.AlphaInfluencer;
import tonegod.gui.controls.extras.emitter.ColorInfluencer;
import tonegod.gui.controls.extras.emitter.DirectionInfluencer;
import tonegod.gui.controls.extras.emitter.ElementEmitter;
import tonegod.gui.controls.extras.emitter.GravityInfluencer;
import tonegod.gui.controls.extras.emitter.ImpulseInfluencer;
import tonegod.gui.controls.extras.emitter.RotationInfluencer;
import tonegod.gui.controls.extras.emitter.SizeInfluencer;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.core.Element.Docking;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.framework.animation.Interpolation;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.controls.CollapsePanel;
import tonegod.gui.tests.states.AppStateCommon;

/**
 * 
 * @author t0neg0d
 */
public class EmitterState extends AppStateCommon {
	private CollapsePanel cPanel;
	private ElementEmitter emitter;
	private Joystick joystick;
	private String	p1 = "Textures/Particles/particles01.png",
					p2 = "Textures/Particles/particles02.png",
					shape = "Textures/Emitter/shape.png";
	private int currentConfig = 0;
	
	public EmitterState(Main main) {
		super(main);
		displayName = "2D Emitter";
		show = true;
	}
	
	@Override
	public void reshape() {
		if (emitter != null)
			emitter.setPosition(screen.getWidth()/2-(emitter.getEmitterWidth()/2),screen.getHeight()/2-(emitter.getEmitterHeight()/2));
	}
	
	@Override
	protected void initState() {
		if (!init) {
			initCtrlPanel();
			init = true;
		}
		main.getTests().addCtrlPanel(cPanel);
		configEmitter(0);
	}
	
	private void initCtrlPanel() {
		cPanel = new CollapsePanel(main, displayName, screen, Vector2f.ZERO, Vector2f.ZERO);
		
		LayoutHelper.reset();
		
		SelectBox configSelect = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (emitter != null) {
					configEmitter((Integer)value);
				}
			}
		};
		configSelect.setDocking(Docking.SW);
		configSelect.addListItem("Config 1", 0);
		configSelect.addListItem("Config 2", 1);
		configSelect.addListItem("Config 3", 2);
		cPanel.getContentArea().addChild(configSelect);
		
		LayoutHelper.advanceY(configSelect, true);
		joystick = new Joystick(screen, LayoutHelper.position(), 100) {
			@Override
			public void onUpdate(float tpf, float deltaX, float deltaY) {
				switch (currentConfig) {
					case 0:
						emitter.getInfluencer(GravityInfluencer.class).setGravity(-deltaX,-deltaY);
						break;
					case 1:
						emitter.getInfluencer(ImpulseInfluencer.class).setVariationStrength(deltaY*10);
						emitter.getInfluencer(SizeInfluencer.class).setStartSize(1f+(deltaX*2));
						break;
					case 2:
						emitter.getInfluencer(DirectionInfluencer.class).setDirection(deltaX,deltaY);
						break;
				}
			}
		};
		cPanel.getContentArea().addChild(joystick);
		
		cPanel.pack();
	}
	
	private void configEmitter(int index) {
		if (emitter != null) {
			if (emitter.getIsEnabled()) {
				emitter.removeAllParticles();
				emitter.stopEmitter();
			}
		}
		currentConfig = index;
		switch(index) {
			case 0:
				emitter = new ElementEmitter(screen, Vector2f.ZERO,10,10);
				emitter.setSprite(p1, 4, 4, 4);
				emitter.setMaxParticles(60);
				emitter.setParticlesPerSecond(30);
				emitter.setForce(.045f);
				emitter.setLowHighLife(1f,4f);
				emitter.getInfluencer(GravityInfluencer.class).setGravity(new Vector2f(.16f,.70f));
				emitter.getInfluencer(RotationInfluencer.class).setMaxRotationSpeed(3);
				emitter.getInfluencer(ColorInfluencer.class).setStartColor(ColorRGBA.Red);
				emitter.getInfluencer(ColorInfluencer.class).setEndColor(ColorRGBA.Yellow);
				emitter.getInfluencer(ColorInfluencer.class).setInterpolation(Interpolation.linear);
				emitter.getInfluencer(ImpulseInfluencer.class).setVariationStrength(.86f);
				emitter.getInfluencer(SizeInfluencer.class).setStartSize(1f);
				emitter.getInfluencer(SizeInfluencer.class).setStartSize(.5f);
				emitter.getInfluencer(AlphaInfluencer.class).setStartAlpha(.85f);
				emitter.getInfluencer(AlphaInfluencer.class).setEndAlpha(0f);
				break;
			case 1:
				emitter = new ElementEmitter(screen, Vector2f.ZERO,2,2);
				emitter.setSprite(p2, 3, 3, 8);
				emitter.setMaxParticles(60);
				emitter.setParticlesPerSecond(30);
				emitter.setForce(.25f);
				emitter.setLowHighLife(1.2f,4f);
				emitter.getInfluencer(GravityInfluencer.class).setGravity(new Vector2f(0f,0f));
				emitter.getInfluencer(RotationInfluencer.class).setMaxRotationSpeed(.25f);
				emitter.getInfluencer(ColorInfluencer.class).setStartColor(ColorRGBA.Green);
				emitter.getInfluencer(ColorInfluencer.class).setEndColor(ColorRGBA.White);
				emitter.getInfluencer(ColorInfluencer.class).setInterpolation(Interpolation.linear);
				emitter.getInfluencer(SizeInfluencer.class).setStartSize(1f);
				emitter.getInfluencer(SizeInfluencer.class).setStartSize(.5f);
				emitter.getInfluencer(ImpulseInfluencer.class).setVariationStrength(1.5f);
				emitter.getInfluencer(AlphaInfluencer.class).setStartAlpha(1f);
				emitter.getInfluencer(AlphaInfluencer.class).setEndAlpha(.4f);
				break;
			case 2:
				emitter = new ElementEmitter(screen, Vector2f.ZERO,128,64);
				emitter.setEmitterShape(shape);
				emitter.setSprite(p2, 3, 3, 1);
				emitter.setMaxParticles(400);
				emitter.setLowHighLife(.3f,.5f);
				emitter.setMinMaxForce(2.5f,10.2f);
				emitter.setParticlesPerSecond(333);
				emitter.getInfluencer(AlphaInfluencer.class).setStartAlpha(1f);
				emitter.getInfluencer(AlphaInfluencer.class).setEndAlpha(1f);
				emitter.getInfluencer(GravityInfluencer.class).setGravity(new Vector2f(0,6f));
				emitter.getInfluencer(DirectionInfluencer.class).setDirection(new Vector2f(0f,1f));
				emitter.getInfluencer(DirectionInfluencer.class).setStrength(.45f);
				emitter.getInfluencer(SizeInfluencer.class).setStartSize(0.2f);
				emitter.getInfluencer(SizeInfluencer.class).setEndSize(0.1f);
				emitter.getInfluencer(RotationInfluencer.class).setMaxRotationSpeed(60);
				emitter.getInfluencer(ColorInfluencer.class).setStartColor(ColorRGBA.Red);
				emitter.getInfluencer(ColorInfluencer.class).setEndColor(ColorRGBA.Yellow);
				emitter.getInfluencer(ColorInfluencer.class).setInterpolation(Interpolation.exp5Out);
				break;
		}
		emitter.setPosition(screen.getWidth()/2-(emitter.getEmitterWidth()/2),screen.getHeight()/2-(emitter.getEmitterHeight()/2));
		emitter.startEmitter();
	}
	
	@Override
	public void updateState(float tpf) {
		
	}

	@Override
	public void cleanupState() {
		main.getTests().removeCtrlPanel(cPanel);
		emitter.stopEmitter();
	}
}
