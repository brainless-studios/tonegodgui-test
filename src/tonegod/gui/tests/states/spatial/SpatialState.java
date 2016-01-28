package tonegod.gui.tests.states.spatial;

import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.extras.DragElement;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.core.Element;
import tonegod.gui.core.Element.Docking;
import tonegod.gui.core.Screen;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.controls.CollapsePanel;
import tonegod.gui.tests.controls.InteractiveNode;
import tonegod.gui.tests.states.AppStateCommon;

/**
 * 
 * @author t0neg0d
 */
public class SpatialState extends AppStateCommon implements RawInputListener {
	public static enum InventoryType {
		DragDrop,
		PointClick
	}
	private CollapsePanel cPanel;
	private float iconSize = 40;
	private InventoryType inventoryType = InventoryType.DragDrop;
	private ButtonAdapter cursor;
	private InteractiveNode obj1Node, obj2Node, obj3Node;
	private Texture objIcons;
	private String iconDefault;
	List<ButtonAdapter> slots = new ArrayList();
	
	public SpatialState(Main main) {
		super(main);
		displayName = "Spatial Support";
		show = true;
		
		iconDefault = "x=64|y=64|w=64|h=64";
		
	}
	
	@Override
	public void reshape() {
		
	}
	
	@Override
	protected void initState() {
		main.getInputManager().addRawInputListener(this);
		if (!init) {
			main.getInputManager().deleteMapping("FLYCAM_RotateDrag");
			main.getInputManager().addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
			main.getInputManager().addListener(main.getFlyByCamera(), "FLYCAM_RotateDrag");
			
			objIcons = main.getAssetManager().loadTexture("Textures/Spatials/ObjIcons.png");
			
			initCtrlPanel();
			
			obj1Node = new InteractiveNode(screen) {
				@Override
				public void onSpatialMouseDown(MouseButtonEvent evt) {
					handleObjectClick(this);
					evt.setConsumed();
				}
				@Override
				public void onSpatialMouseUp(MouseButtonEvent evt) {
					evt.setConsumed();
				}
			};
			obj1Node.setName("Box");
			Box obj1 = new Box(1,1,1);
			Geometry obj1Geom = new Geometry();
			obj1Geom.setMesh(obj1);
			obj1Node.attachChild(obj1Geom);
			obj1Node.setIcon("x=0|y=0|w=64|h=64");
			obj1Node.setDefaultMaterial(getMaterial(ColorRGBA.Blue));
			obj1Node.setHighlightMaterial(getMaterial(new ColorRGBA(0.5f,0.5f,1f,1f)));
			obj1Node.setToolTipText("I'm a Box\n\nPick me up and put\nme in your inventory.");
			obj1Node.setLocalTranslation(-3,0,0);
			
			obj2Node = new InteractiveNode(screen) {
				@Override
				public void onSpatialMouseDown(MouseButtonEvent evt) {
					handleObjectClick(this);
					evt.setConsumed();
				}
				@Override
				public void onSpatialMouseUp(MouseButtonEvent evt) {
					evt.setConsumed();
				}
			};
			obj2Node.setName("Sphere");
			Sphere obj2 = new Sphere(8,8,1f);
			Geometry obj2Geom = new Geometry();
			obj2Geom.setMesh(obj2);
			obj2Node.attachChild(obj2Geom);
			obj2Node.setIcon("x=64|y=0|w=64|h=64");
			obj2Node.setDefaultMaterial(getMaterial(ColorRGBA.Red));
			obj2Node.setHighlightMaterial(getMaterial(new ColorRGBA(1f,0.5f,0.5f,1f)));
			obj2Node.setToolTipText("I'm a Sphere\n\nPick me up and put\nme in your inventory.");
			obj2Node.setLocalTranslation(0,0,0);
			
			obj3Node = new InteractiveNode(screen) {
				@Override
				public void onSpatialMouseDown(MouseButtonEvent evt) {
					handleObjectClick(this);
					evt.setConsumed();
				}
				@Override
				public void onSpatialMouseUp(MouseButtonEvent evt) {
					evt.setConsumed();
				}
			};
			obj3Node.setName("Torus");
			Torus obj3 = new Torus(8,8,0.5f,1f);
			Geometry obj3Geom = new Geometry();
			obj3Geom.setMesh(obj3);
			obj3Node.attachChild(obj3Geom);
			obj3Node.setIcon("x=0|y=64|w=64|h=64");
			obj3Node.setDefaultMaterial(getMaterial(ColorRGBA.Green));
			obj3Node.setHighlightMaterial(getMaterial(new ColorRGBA(0.5f,1f,0.5f,1f)));
			obj3Node.setToolTipText("I'm a Torus\n\nPick me up and put\nme in your inventory.");
			obj3Node.setLocalTranslation(3,0,0);
			
			cursor = new ButtonAdapter(screen, UIDUtil.getUID(), Vector2f.ZERO, new Vector2f(iconSize, iconSize), Vector4f.ZERO, null) {
				@Override
				public void update(float tpf) {
					setPosition(screen.getMouseXY().x+25,screen.getMouseXY().y-50);
				}
			};
			cursor.setIsEnabled(false);
			cursor.setInterval(1);
			cursor.setTextureAtlasImage(objIcons, iconDefault);
			cursor.setIsModal(true);
			cursor.setIsGlobalModal(true);
			cursor.setEffectZOrder(false);
			cursor.setIgnoreMouse(true);
			
			init = true;
		}
		screen.setUse3DSceneSupport(true);
		main.getTests().addCtrlPanel(cPanel);
		if (obj1Node.getIsInScene()) main.getRootNode().attachChild(obj1Node);
		if (obj2Node.getIsInScene()) main.getRootNode().attachChild(obj2Node);
		if (obj3Node.getIsInScene()) main.getRootNode().attachChild(obj3Node);
		screen.addElement(cursor);
		cursor.move(0,0,20);
		main.addSceneLights();
	}
	
	private void handleObjectClick(Node parent) {
		switch (inventoryType) {
			case DragDrop:
				DragElement de = createNewDragElement();
				de.setUserData("worldObject", parent);
				de.setTextureAtlasImage(objIcons, ((InteractiveNode)parent).getIcon());
				String ttt = ((InteractiveNode)parent).getToolTipText();
				de.setToolTipText(ttt.substring(0,ttt.indexOf("\n")) + "\n\nDrop me back into\nthe world.");
				parent.removeFromParent();
				((InteractiveNode)parent).setIsInScene(false);
				screen.forceFocusElementRefresh();
				break;
			case PointClick:
				cursor.setTextureAtlasImage(objIcons, ((InteractiveNode)parent).getIcon());
				cursor.setUserData("worldObject", parent);
				parent.removeFromParent();
				((InteractiveNode)parent).setIsInScene(false);
				screen.forceFocusElementRefresh();
				break;
		}
	}
	
	private Material getMaterial(ColorRGBA color) {
		Material mat = new Material(main.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		mat.setColor("Diffuse", color);
		ColorRGBA clone = color.clone();
		clone.multLocal(0.8f);
		clone.a = 1f;
		mat.setColor("Ambient", clone);
		mat.setBoolean("UseMaterialColors", true);
		return mat;
	}
	
	private void initCtrlPanel() {
		cPanel = new CollapsePanel(main, displayName, screen, Vector2f.ZERO, Vector2f.ZERO);
		cPanel.setEffectZOrder(false);
		
		LayoutHelper.reset();
		Element lastEl = null;
		
		SelectBox invType = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				inventoryType = (InventoryType)value;
				changeInventoryType();
			}
		};
		invType.setDocking(Docking.SW);
		invType.setEffectZOrder(false);
		invType.addListItem("Drag & Drop", InventoryType.DragDrop);
		invType.addListItem("Point & Click", InventoryType.PointClick);
		cPanel.getContentArea().addChild(invType);
		
		lastEl = invType;
		
		int index = 0;
		for (int y = 0; y < 4; y++) {
			LayoutHelper.resetX();
			LayoutHelper.advanceY(lastEl, true);
			for (int x = 0; x < 4; x++) {
				ButtonAdapter e = createInventorySlot(index, LayoutHelper.position());
				e.setScaleEW(false);
				e.setScaleNS(false);
				e.setDocking(Element.Docking.SW);
				e.setEffectZOrder(false);
				cPanel.getContentArea().addChild(e);
				slots.add(e);
				lastEl = e;
				LayoutHelper.advanceX(lastEl, true);
				index++;
			}
		}
		
		cPanel.pack();
	}

	private ButtonAdapter createInventorySlot(int index, Vector2f position) {
		ButtonAdapter slot = new ButtonAdapter(
			screen,
			"InvSlot" + index,
			position,
			LayoutHelper.dimensions(iconSize,iconSize),
			screen.getStyle("CheckBox").getVector4f("resizeBorders"),
			screen.getStyle("CheckBox").getString("defaultImg")
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (inventoryType == InventoryType.PointClick) {
					InteractiveNode prevNode = null;
					InteractiveNode nextNode = null;
					if (getUserData("worldObject") != null) {
						prevNode = getUserData("worldObject");
						setToolTipText("");
						setUserData("worldObject", null);
						setButtonIcon(iconSize, iconSize, iconDefault);
					}
					if (cursor.getUserData("worldObject") != null) {
						nextNode = cursor.getUserData("worldObject");
						cursor.setTextureAtlasImage(objIcons, iconDefault);
						cursor.setUserData("worldObject", null);
					}
					if (nextNode != null) {
						String ttt = nextNode.getToolTipText();
						setToolTipText(ttt.substring(0,ttt.indexOf("\n")) + "\n\nDrop me back into\nthe world.");
						setUserData("worldObject", nextNode);
						setButtonIcon(iconSize, iconSize, nextNode.getIcon());
					}
					if (prevNode != null) {
						cursor.setUserData("worldObject", prevNode);
						cursor.setTextureAtlasImage(objIcons, prevNode.getIcon());
					}
				}
			}
		};
		slot.clearAltImages();
		slot.setEffectZOrder(false);
		slot.setButtonIcon(iconSize, iconSize, iconDefault);
		slot.getButtonIcon().setTextureAtlasImage(objIcons, iconDefault);
		slot.setIsDragDropDropElement(true);
		return slot;
	}
	
	private DragElement createNewDragElement() {
		DragElement e = new DragElement(
			screen,
			new Vector2f(
				screen.getMouseXY().x-(iconSize/2),
				screen.getHeight()-(screen.getMouseXY().y+(iconSize/2))
				
			),
			new Vector2f(iconSize,iconSize),
			Vector4f.ZERO,
			screen.getStyle("Window").getString("defaultImg")
		) {
			@Override
			public void onDragStart(MouseButtonEvent evt) {
				if (getElementParent() != null) {
					if (getElementParent() != this.getScreen()) {
						getParentDroppable().bringToFront();
					}
				}
			}
			@Override
			public boolean onDragEnd(MouseButtonEvent evt, Element dropElement) {
				if (dropElement != null) {
					setLockToParentBounds(false);
					if (!dropElement.getDraggableChildren().isEmpty()) {
						if (!dropElement.getDraggableChildren().contains(this)) {
							DragElement de1 = (DragElement)dropElement.getDraggableChildren().get(0);
							Element drop1 = null;
							if (getParentDroppable() != null) {
								drop1 = getParentDroppable();
								getParentDroppable().removeChild(this);
								clearParentDroppable();
							}
							
							if (de1 != null) {
								de1.getParentDroppable().removeChild(de1);
								de1.clearParentDroppable();
							}
							
							if (drop1 != null && de1 != null) {
								de1.bindToDroppable(drop1);
							} else {
								Node n = de1.getUserData("worldObject");
								Node n1 = getUserData("worldObject");
								if (n != n1) {
									main.getRootNode().attachChild(n);
									((InteractiveNode)n).setIsInScene(true);
								}
							}
							evt.setConsumed();
							return true;
						} else {
							return true;
						}
					} else {
						return true;
					}
				} else {
					if (getParentDroppable() != null) {
						getParentDroppable().removeChild(this);
						clearParentDroppable();
					}
					Node n = getUserData("worldObject");
					main.getRootNode().attachChild(n);
					screen.removeElement(this);
					((InteractiveNode)n).onLoseFocus(null);
					((InteractiveNode)n).setIsInScene(true);
					((Screen)screen).forceFocusElementRefresh();
					return false;
				}
			}
		};
		e.setDocking(Docking.SW);
		e.setEffectZOrder(false);
		e.setLockToParentBounds(true);
		e.setUseLockToDropElementCenter(true);
		e.setUseSpringBack(true);
		e.setUseSpringBackEffect(true);
		screen.addElement(e);
		return e;
	}
	
	private void changeInventoryType() {
		switch (inventoryType) {
			case DragDrop:
				if (cursor != null) {
					if (cursor.getUserData("worldObject") != null) {
						InteractiveNode node = cursor.getUserData("worldObject");
						main.getRootNode().attachChild(node);
						cursor.setUserData("worldObject", null);
						cursor.setTextureAtlasImage(objIcons, iconDefault);
					}
					for (ButtonAdapter slot : slots) {
						if (slot.getUserData("worldObject") != null) {
							InteractiveNode node = slot.getUserData("worldObject");
							DragElement de = createNewDragElement();
							de.setUserData("worldObject", node);
							de.setTextureAtlasImage(objIcons, node.getIcon());
							String ttt = node.getToolTipText();
							de.setToolTipText(ttt.substring(0,ttt.indexOf("\n")) + "\n\nDrop me back into\nthe world.");
							de.bindToDroppable(slot);
							de.setEffectZOrder(false);
							slot.setUserData("worldObject", null);
							slot.setButtonIcon(iconSize, iconSize, iconDefault);
						}
					}
				}
				break;
			case PointClick:
				for (ButtonAdapter slot : slots) {
					if (slot.getElementsAsMap().size() > 1) {
						for (Element el : slot.getElementsAsMap().values()) {
							if (el instanceof DragElement) {
								DragElement de = (DragElement)el;
								InteractiveNode n = de.getUserData("worldObject");
								slot.setUserData("worldObject", n);
								slot.setButtonIcon(iconSize, iconSize, n.getIcon());
								slot.removeChild(de);
							}
						}
					}
				}
				break;
		}
	}
	
	@Override
	public void updateState(float tpf) {
		
	}

	@Override
	public void cleanupState() {
		main.getTests().removeCtrlPanel(cPanel);
		if (obj1Node.getIsInScene()) obj1Node.removeFromParent();
		if (obj2Node.getIsInScene()) obj2Node.removeFromParent();
		if (obj3Node.getIsInScene()) obj3Node.removeFromParent();
		screen.removeElement(cursor);
		screen.setUse3DSceneSupport(false);
		main.getInputManager().removeRawInputListener(this);
	}
	
	public void beginInput() {  }
	public void endInput() {  }
	public void onJoyAxisEvent(JoyAxisEvent evt) {  }
	public void onJoyButtonEvent(JoyButtonEvent evt) {  }
	public void onMouseMotionEvent(MouseMotionEvent evt) {  }
	public void onMouseButtonEvent(MouseButtonEvent evt) {
		if (evt.getButtonIndex() == 0 && evt.isPressed()) {
			if (inventoryType == InventoryType.PointClick) {
				if (cursor.getUserData("worldObject") != null) {
					InteractiveNode node = cursor.getUserData("worldObject");
					main.getRootNode().attachChild(node);
					node.setIsInScene(true);
					cursor.setUserData("worldObject", null);
					cursor.setTextureAtlasImage(objIcons, iconDefault);
				}
			}
		}
	}
	public void onKeyEvent(KeyInputEvent evt) {  }
	public void onTouchEvent(TouchEvent evt) {  }
}
