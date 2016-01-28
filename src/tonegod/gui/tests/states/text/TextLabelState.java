/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.states.text;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.text.LabelElement;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Element;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.states.AppStateCommon;
import tonegod.gui.tests.controls.CollapsePanel;

/**
 *
 * @author t0neg0d
 */
public class TextLabelState extends AppStateCommon {
	private CollapsePanel cPanel;
	private LabelElement label;
	private BitmapFont fnt;
	private float fntSize = 22;
	private Align txtAlign = Align.Center;
	private VAlign txtVAlign = VAlign.Center;
	
	public TextLabelState(Main main) {
		super(main);
		displayName = "Text Labels";
		show = true;
	}
	
	@Override
	public void reshape() {
		if (label != null)
			LayoutHelper.reposition(screen, main.getHarness().getPreviousScreenSize(), label);
	}

	@Override
	protected void initState() {
		if (!init) {
			label = new LabelElement(screen, Vector2f.ZERO, new Vector2f(300, 50));
			label.setSizeToText(true);
			label.setText("This is a Sample Label.");
			label.setIgnoreMouse(false);
			screen.addElement(label, true);
			label.centerToParent();
			
			initCtrlPanel();
			
			init = true;
		}
		main.getTests().addCtrlPanel(cPanel);
		label.show();
	}
	
	private void initCtrlPanel() {
		cPanel = new CollapsePanel(main, displayName, screen, Vector2f.ZERO, Vector2f.ZERO);
		
		LayoutHelper.reset();
		
		SelectBox fonts = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				fnt = (BitmapFont)value;
				label.setFont(fnt);
				label.setFontSize(fntSize);
			}
		};
		fonts.setDocking(Element.Docking.SW);
		fonts.addListItem("Font 1", main.getAssetManager().loadFont("Interface/Fonts/Font1.fnt"));
		fonts.addListItem("Font 2", main.getAssetManager().loadFont("Interface/Fonts/Font2.fnt"));
	//	fonts.addListItem("Font 3", main.getAssetManager().loadFont("Interface/Fonts/Font3.fnt"));
		cPanel.getContentArea().addChild(fonts);
		
		LayoutHelper.advanceY(fonts, true);
		final TextField size = new TextField(screen, LayoutHelper.position());
		size.setType(TextField.Type.NUMERIC);
		size.setText(String.valueOf(fntSize));
		size.setDocking(Element.Docking.SW);
		size.setWidth(fonts.getWidth()/4*3);
		label.setFontSize(fntSize);
		cPanel.getContentArea().addChild(size);
		
		LayoutHelper.advanceX(size, true);
		ButtonAdapter setSize = new ButtonAdapter(screen, LayoutHelper.position(), new Vector2f(size.getHeight(),size.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				fntSize = Float.parseFloat(size.getText());
				label.setFontSize(fntSize);
			}
		};
		setSize.setButtonIcon(20, 20, screen.getStyle("Common").getString("arrowRight"));
		setSize.setDocking(Element.Docking.SW);
		cPanel.getContentArea().addChild(setSize);
		
		LayoutHelper.resetX();
		LayoutHelper.advanceY(size, true);
		SelectBox align = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				txtAlign = Align.valueOf((String)value);
				label.setTextAlign(txtAlign);
			}
		};
		align.setDocking(Element.Docking.SW);
		align.addListItem("Left", "Left");
		align.addListItem("Center", "Center");
		align.addListItem("Right", "Right");
		align.setSelectedByCaption("Center", true);
		cPanel.getContentArea().addChild(align);
		
		LayoutHelper.advanceY(size, true);
		SelectBox valign = new SelectBox(screen, LayoutHelper.position()) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				txtVAlign = VAlign.valueOf((String)value);
				label.setTextVAlign(txtVAlign);
			}
		};
		valign.setDocking(Element.Docking.SW);
		valign.addListItem("Top", "Top");
		valign.addListItem("Center", "Center");
		valign.addListItem("Bottom", "Bottom");
		valign.setSelectedByCaption("Center", true);
		cPanel.getContentArea().addChild(valign);
		
		cPanel.pack();
	}
	
	@Override
	public void updateState(float tpf) {
		
	}

	@Override
	public void cleanupState() {
		main.getTests().removeCtrlPanel(cPanel);
		label.hide();
	}
}
