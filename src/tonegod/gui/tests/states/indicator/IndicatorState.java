/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.tests.states.indicator;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.core.Element;
import tonegod.gui.core.Element.Orientation;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.tests.Main;
import tonegod.gui.tests.controls.CollapsePanel;
import tonegod.gui.tests.states.AppStateCommon;

/**
 *
 * @author roah
 */
public class IndicatorState extends AppStateCommon {

    private Indicator[] indicatorList = new Indicator[2];
    private CollapsePanel cPanel;
    private int currentIndex = 0;
    private float timer = 0;
    private SelectBox orientationSelector;

    public IndicatorState(Main main) {
        super(main);
        displayName = "Indicator";
    }

    @Override
    public void reshape() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void initState() {
        if (!init) {
            Orientation[] orientation = new Orientation[] {Orientation.HORIZONTAL, Orientation.VERTICAL};
            Vector2f[] size = new Vector2f[] {new Vector2f(500, 30), new Vector2f(30, 500)};
            for(int i = indicatorList.length-1; i >= 0; i--) {
                indicatorList[i] = new Indicator(screen, Vector2f.ZERO, size[i], orientation[i], true) {
                    @Override
                    public void onChange(float currentValue, float currentPercentage) {
                    }
                };
                indicatorList[i].setDisplayPercentage();
                indicatorList[i].setBaseImage(screen.getStyle("Window").getString("defaultImg"));
                indicatorList[i].setIndicatorColor(ColorRGBA.Red);
                indicatorList[i].setIndicatorPadding(new Vector4f(7,7,7,7));
                indicatorList[i].setAlphaMap(screen.getStyle("Indicator").getString("alphaImg"));
                indicatorList[i].setMaxValue(100);
            }

            initCtrlPanel();

            init = true;
        }
        screen.addElement(indicatorList[currentIndex]);
        indicatorList[currentIndex].centerToParent();
        
        cPanel.setPosition(Vector2f.ZERO);
        main.getTests().addCtrlPanel(cPanel);
    }

    private void initCtrlPanel() {
        cPanel = new CollapsePanel(main, displayName, screen, Vector2f.ZERO, Vector2f.ZERO);

        LayoutHelper.reset();

        orientationSelector = new SelectBox(screen, LayoutHelper.position()) {
            @Override
            public void onChange(int selectedIndex, Object value) {
                if (indicatorList != null && selectedIndex != currentIndex && currentIndex <= indicatorList.length) {
                    screen.removeElement(indicatorList[currentIndex]);
                    indicatorList[currentIndex].setCurrentValue(0);
                    screen.addElement(indicatorList[selectedIndex]);
                    indicatorList[selectedIndex].centerToParent();
                    timer = 0;
                    currentIndex = selectedIndex;
                }
            }
        };
        orientationSelector.setDocking(Element.Docking.SW);
        orientationSelector.addListItem("Horizontal", 0);
        orientationSelector.addListItem("Vertical", 1);
        cPanel.getContentArea().addChild(orientationSelector);
        cPanel.pack();
    }

    @Override
    public void updateState(float tpf) {
        timer += tpf * 5;
        if (timer <= 100) {
            indicatorList[currentIndex].setCurrentValue((int) timer);
        } else if (timer > 110) {
            timer = 0;
        }
    }

    @Override
    public void cleanupState() {
        main.getTests().removeCtrlPanel(cPanel);
        screen.removeElement(indicatorList[currentIndex]);
        orientationSelector.setSelectedIndex(0);
        currentIndex = 0;
        timer = 0;
    }

}
