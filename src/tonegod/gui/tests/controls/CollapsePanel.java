/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.controls;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.LabelElement;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.states.TestState;

/**
 *
 * @author t0neg0d
 */
public class CollapsePanel extends Panel {
	private Main main;
	public TestState tests;
	private boolean isCollapsed = true;
	public ButtonAdapter btnCollapse;
	protected LabelElement header;
	private Element content;
	private String headerText = "Header";
	private float nsWidth = 0, sWidth = 0;
	protected float collapseHeight, fullHeight;
	
	public CollapsePanel(Main main, String headerText, ElementManager screen, Vector2f position, Vector2f dimensions) {
		super(screen, position, dimensions);
		this.main = main;
		this.tests = main.getTests();
		this.headerText = headerText;
		
		collapseHeight = 20+(TestState.contentIndent*2);
		
		setDimensions(
			tests.getInnerWidth(),
			collapseHeight
		);
		
		LayoutHelper.reset();
		LayoutHelper.advanceY(TestState.contentIndent);
		LayoutHelper.advanceX(TestState.contentIndent);
		header = tests.getLabel(headerText, LayoutHelper.position());
		addChild(header);
		
		btnCollapse = new ButtonAdapter(screen,
			Vector2f.ZERO,
			LayoutHelper.absPosition(20, 20)
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggleed) {
				handleCollapse();
			}
		};
		btnCollapse.setButtonIcon(20, 20, screen.getStyle("Common").getString("arrowUp"));
		btnCollapse.setPosition(getWidth()-btnCollapse.getWidth()-TestState.contentIndent, TestState.contentIndent);
		btnCollapse.setDocking(Docking.SW);
		btnCollapse.setScaleEW(false);
		btnCollapse.setScaleNS(false);
		addChild(btnCollapse);
		
		LayoutHelper.advanceY(btnCollapse, true);
		
		content = new Element(screen, UIDUtil.getUID(), LayoutHelper.position(), Vector2f.ZERO, Vector4f.ZERO, null);
		content.setAsContainerOnly();
		content.setDocking(Docking.SW);
		addChild(content);
		
		//setAsContainerOnly();
		setScaleEW(true);
		setScaleNS(false);
		setDocking(Element.Docking.NW);
		setIsResizable(false);
		setIsMovable(false);
	}
	
	public void setHeader(String text) {
		headerText = text;
		header.setText(text);
	}
	
	public boolean getIsCollapsed() { return isCollapsed; }
	
	public Element getContentArea() { return content; }
	
	public void pack() {
		content.sizeToContent();
		fullHeight = collapseHeight+content.getHeight();
		
		handleCollapse();
	}
	
	private void handleCollapse() {
		isCollapsed = !isCollapsed;
		if (isCollapsed) {
			btnCollapse.setButtonIcon(20, 20, screen.getStyle("Common").getString("arrowDown"));
			setHeight(20+(TestState.contentIndent*2));
			setWidth(tests.getInnerWidth());
			content.hide();
		} else {
			btnCollapse.setButtonIcon(20, 20, screen.getStyle("Common").getString("arrowUp"));
			setHeight(fullHeight);
			setWidth(tests.getInnerWidth());
			content.show();
		}
		
		btnCollapse.setY(getHeight()-btnCollapse.getHeight()-TestState.contentIndent);
		header.setY(getHeight()-header.getHeight()-TestState.contentIndent);
		setControlClippingLayer(this);
		header.setClippingLayer(this);
		tests.pack();
	}
}
