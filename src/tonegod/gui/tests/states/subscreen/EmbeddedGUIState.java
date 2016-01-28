package tonegod.gui.tests.states.subscreen;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.core.Element;
import tonegod.gui.core.Element.Docking;
import tonegod.gui.core.Screen;
import tonegod.gui.core.SubScreen;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.effects.Effect;
import tonegod.gui.effects.Effect.EffectEvent;
import tonegod.gui.effects.Effect.EffectType;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.controls.CollapsePanel;
import tonegod.gui.tests.states.AppStateCommon;

/**
 * 
 * @author t0neg0d
 */
public class EmbeddedGUIState extends AppStateCommon {
	private CollapsePanel cPanel;
	private Node door, doorFrame, accessPanel;
	List<SubScreenInfo> scenes = new ArrayList();
	SubScreenInfo currentScreen = null;
	Material mat;
	
	public EmbeddedGUIState(Main main) {
		super(main);
		displayName = "Sub-Screens";
		show = true;
	}
	
	@Override
	public void reshape() {
		
	}
	
	@Override
	protected void initState() {
		if (!init) {
			initMaterials();
			initCtrlPanel();
			
			// Access panel test
			SubScreenInfo info = new SubScreenInfo("Access Panel", 300, 500);
			Node node = (Node)main.getAssetManager().loadModel("Models/EmbeddedGUI/AccessPanel.j3o");
			info.setGeometry((Geometry)node.getChild(0));
			door = (Node)main.getAssetManager().loadModel("Models/EmbeddedGUI/Door.j3o");
			info.addProp(door);
			node = (Node)main.getAssetManager().loadModel("Models/EmbeddedGUI/DoorFrame.j3o");
			node.setMaterial(mat);
			info.addProp(node);
			SubScreen subScreen = new SubScreen(screen, info.getGeometry());
			subScreen.setSubScreenBridge((int)info.getSize().x, (int)info.getSize().y, info.getRoot());
			info.setSubScreen(subScreen);
			createAccessPanel(info);
			scenes.add(info);
			
			// Cube projection test
			info = new SubScreenInfo("Cube Projected UI", 400, 400);
			Geometry geom = new Geometry();
			geom.setMesh(new Box(2,2,2));
			info.setGeometry(geom);
			subScreen = new SubScreen(screen, info.getGeometry());
			subScreen.setSubScreenBridge((int)info.getSize().x, (int)info.getSize().y, info.getRoot());
			info.setSubScreen(subScreen);
			createCubeScene(info);
			scenes.add(info);
			
			// Cylinder projection test
			info = new SubScreenInfo("Cylinder Projected UI", 800, 600);
			geom = new Geometry();
			geom.setMesh(new Cylinder(12,24,2,6));
			geom.setLocalRotation(new Quaternion().fromAngleAxis(-90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
			geom.setLocalRotation(geom.getLocalRotation().mult(new Quaternion().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_Z)));
			info.setGeometry(geom);
			subScreen = new SubScreen(screen, info.getGeometry());
			subScreen.setSubScreenBridge((int)info.getSize().x, (int)info.getSize().y, info.getRoot());
			info.setSubScreen(subScreen);
			createCylinderScene(info);
			scenes.add(info);
			
			init = true;
		}
		main.getTests().addCtrlPanel(cPanel);
		main.addSceneLights();
	}
	
	private void initMaterials() {
		mat = new Material(main.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors", true);
		mat.setColor("Diffuse", ColorRGBA.Gray);
	}
	
	private void initCtrlPanel() {
		cPanel = new CollapsePanel(main, displayName, screen, Vector2f.ZERO, Vector2f.ZERO);
		
		LayoutHelper.reset();
		
		SelectBox screens = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (init) {
					unloadSubScreen();
					currentScreen = scenes.get(selectedIndex);

					for (Node prop : currentScreen.getProps()) {
						main.getRootNode().attachChild(prop);
					}
					
					main.getRootNode().attachChild(currentScreen.getGeometry());
					((Screen)screen).addSubScreen(currentScreen.getSubScreen());
				}
			}
		};
		screens.setDocking(Docking.SW);
		screens.addListItem("Access Panel", 0);
		screens.addListItem("Cube Projected UI", 1);
		screens.addListItem("Cylinder Projected UI", 2);
		cPanel.getContentArea().addChild(screens);
		
		cPanel.pack();
	}

	private void unloadSubScreen() {
		if (currentScreen != null) {
			((Screen)screen).removeSubScreen(currentScreen.getSubScreen());
			for (Node prop : currentScreen.getProps()) {
				main.getRootNode().detachChild(prop);
			}
			main.getRootNode().detachChild(currentScreen.getGeometry());
			
		}
	}
	
	@Override
	public void updateState(float tpf) {
		if (openDoor) {
			if (doorX > doorXMax) {
				doorX -= tpf*2;
				door.setLocalTranslation(doorX,0,0);
			}
		} else if (closeDoor) {
			if (doorX < 0) {
				doorX += tpf*2;
				door.setLocalTranslation(doorX,0,0);
			}
		}
	}

	@Override
	public void cleanupState() {
		main.getTests().removeCtrlPanel(cPanel);
		unloadSubScreen();
	}
	
	public class SubScreenInfo {
		private String name;
		private SubScreen subScreen;
		private Node root;
		private Geometry geometry;
		private List<Node> props = new ArrayList();
		private Vector2f size = new Vector2f();
		
		public SubScreenInfo(String name, int width, int height) {
			this.name = name;
			root = new Node(name);
			size.set(width, height);
		}
		
		public String getName() { return name; }
		public void setSubScreen(SubScreen subScreen) { this.subScreen = subScreen; }
		public SubScreen getSubScreen() { return this.subScreen; }
		public Node getRoot() { return this.root; }
		public void setGeometry(Geometry geometry) { this.geometry = geometry; }
		public Geometry getGeometry() { return this.geometry; }
		public List<Node> getProps() { return this.props; }
		public void addProp(Node node) { this.props.add(node); }
		public void removeProp(Node node) { this.props.remove(node); }
		public void clearProps() { this.props.clear(); }
		public Vector2f getSize() { return this.size; }
	}
	
	float	doorX = 0;
	float	doorXMax = -3.3f;
	String	passCode = "";
	boolean openDoor = false;
	boolean closeDoor = false;
	private void createAccessPanel(final SubScreenInfo info) {
		
		float	padding = 10;
		float	bWidth = 300-padding;
				bWidth /= 3;
				bWidth -= padding;
		
		LayoutHelper.reset();
		LayoutHelper.setPadding(10);
		
		final Element tfBg = new Element(info.getSubScreen(), "tfBg",
			new Vector2f(10,10),
			new Vector2f(info.getSize().x-(LayoutHelper.pad()*2),80),
			new Vector4f(2,2,2,2),
			screen.getStyle("TextField").getString("defaultImg")
		);
		tfBg.setEffectZOrder(false);
		info.getSubScreen().addElement(tfBg);
		
		final Label tf = new Label(info.getSubScreen(),
			new Vector2f(10,10),
			new Vector2f(info.getSize().x-(LayoutHelper.pad()*2),80));
		tf.setFontSize(81);
		tf.setFontColor(screen.getStyle("TextField").getColorRGBA("fontColor"));
		info.getSubScreen().addElement(tf);
		
		LayoutHelper.advanceY(LayoutHelper.pad());
		
		Element lastEl = tfBg;
		int index = 1;
		for (int y = 0; y < 3; y++) {
			LayoutHelper.resetX();
			LayoutHelper.advanceX(LayoutHelper.pad());
			LayoutHelper.advanceY(lastEl, true);
			for (int x = 0; x < 3; x++) {
				final String fIndex = String.valueOf(index);
				ButtonAdapter b = new ButtonAdapter(info.getSubScreen(),
					LayoutHelper.position(),
					LayoutHelper.dimensions(bWidth,bWidth)) {
					@Override
					public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
						passCode = tf.getText() + fIndex;
						tf.setText(passCode);
					}
				};
				b.setFontSize(screen.getStyle("Button").getFloat("fontSize")*3);
				b.setText(String.valueOf(index));
				info.getSubScreen().addElement(b);
				
				index++;
				lastEl = b;
				LayoutHelper.advanceX(lastEl);
				LayoutHelper.advanceX(LayoutHelper.pad());
			}
		}
		
		LayoutHelper.resetX();
		LayoutHelper.advanceX(LayoutHelper.pad());
		LayoutHelper.advanceY(lastEl, true);

		ButtonAdapter bClear = new ButtonAdapter(info.getSubScreen(),
			LayoutHelper.position(),
			LayoutHelper.dimensions(bWidth,bWidth)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				Effect effect = new Effect(EffectType.ColorSwap, EffectEvent.Press, 0.25f);
				effect.setColor(ColorRGBA.White);
				effect.setElement(tfBg);
				info.getSubScreen().getEffectManager().applyEffect(effect);
				
				passCode = "";
				tf.setText("");
				
				if (openDoor) {
					openDoor = false;
					closeDoor = true;
				}
			}
		};
		bClear.setFontSize(screen.getStyle("Button").getFloat("fontSize")*2);
		bClear.setText("Clear");
		info.getSubScreen().addElement(bClear);
	
		LayoutHelper.advanceX(bClear);
		LayoutHelper.advanceX(LayoutHelper.pad());
		
		ButtonAdapter b0 = new ButtonAdapter(info.getSubScreen(),
			LayoutHelper.position(),
			LayoutHelper.dimensions(bWidth,bWidth)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				passCode = tf.getText() + "0";
				tf.setText(passCode);
			}
		};
		b0.setFontSize(screen.getStyle("Button").getFloat("fontSize")*3);
		b0.setText("0");
		info.getSubScreen().addElement(b0);
		
		LayoutHelper.advanceX(b0);
		LayoutHelper.advanceX(LayoutHelper.pad());
		
		ButtonAdapter bEnter = new ButtonAdapter(info.getSubScreen(),
			LayoutHelper.position(),
			LayoutHelper.dimensions(bWidth,bWidth)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (doorX >= 0)
					closeDoor = false;
				if (tf.getText().equals("887129")) {
					Effect effect = new Effect(EffectType.PulseColor, EffectEvent.Press, 0.25f);
					effect.setColor(ColorRGBA.Green);
					effect.setElement(tfBg);
					info.getSubScreen().getEffectManager().applyEffect(effect);
					
					openDoor = true;
				} else {
					Effect effect = new Effect(EffectType.PulseColor, EffectEvent.Press, 0.25f);
					effect.setColor(ColorRGBA.Red);
					effect.setElement(tfBg);
					info.getSubScreen().getEffectManager().applyEffect(effect);
				}
			}
		};
		bEnter.setFontSize(screen.getStyle("Button").getFloat("fontSize")*2);
		bEnter.setText("Enter");
		info.getSubScreen().addElement(bEnter);
		
		LayoutHelper.setPadding(5);
	}
	
	private void createCubeScene(SubScreenInfo info) {
		LayoutHelper.reset();
		
		AlertBox alert = new AlertBox(info.getSubScreen(), Vector2f.ZERO) {
			@Override
			public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {  }
		};
		alert.setMsg("This is a sample Alert Box being projected onto a Box.  I am resizable.  I am movable.  I am everything an Alert Box should be... minus the alert message that makes sense.\n\nThis is a sample Alert Box being projected onto a Box.  I am resizable.  I am movable.  I am everything an Alert Box should be...\n\nminus the alert message that makes sense.");
		alert.setWindowTitle("This is a test");
		info.getSubScreen().addElement(alert);
		alert.centerToParent();
	}
	
	private void createCylinderScene(SubScreenInfo info) {
		LayoutHelper.reset();
		
		AlertBox alert = new AlertBox(info.getSubScreen(), Vector2f.ZERO) {
			@Override
			public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {  }
		};
		alert.setMsg("This is a sample Alert Box being projected onto a Box.  I am resizable.  I am movable.  I am everything an Alert Box should be... minus the alert message that makes sense.\n\nThis is a sample Alert Box being projected onto a Box.  I am resizable.  I am movable.  I am everything an Alert Box should be...\n\nminus the alert message that makes sense.");
		info.getSubScreen().addElement(alert);
		alert.centerToParent();
	}
}
