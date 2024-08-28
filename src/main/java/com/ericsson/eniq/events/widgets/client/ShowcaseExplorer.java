package com.ericsson.eniq.events.widgets.client;

import com.ericsson.eniq.events.common.client.CommonMain;
import com.ericsson.eniq.events.common.client.gin.CommonInjector;
import com.ericsson.eniq.events.widgets.client.breadcrumb.BreadCrumb;
import com.ericsson.eniq.events.widgets.client.button.CogButton;
import com.ericsson.eniq.events.widgets.client.button.ImageButton;
import com.ericsson.eniq.events.widgets.client.button.TextButton;
import com.ericsson.eniq.events.widgets.client.calendar.CalendarPopUp;
import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanel;
import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanelResourceBundle;
import com.ericsson.eniq.events.widgets.client.component.ComponentMessagePanel;
import com.ericsson.eniq.events.widgets.client.component.ComponentMessageType;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDown;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownMenu;
import com.ericsson.eniq.events.widgets.client.dropdown.StringDropDownItem;
import com.ericsson.eniq.events.widgets.client.mapcontextmenu.MapContextMenu;
import com.ericsson.eniq.events.widgets.client.popmenu.ClusterPopCell;
import com.ericsson.eniq.events.widgets.client.popmenu.PopCell;
import com.ericsson.eniq.events.widgets.client.slider.ColoredBarDataType;
import com.ericsson.eniq.events.widgets.client.slider.SlideBarDataType;
import com.ericsson.eniq.events.widgets.client.slider.SlideGroup;
import com.ericsson.eniq.events.widgets.client.textbox.ExtendedSuggestBox;
import com.ericsson.eniq.events.widgets.client.textbox.ExtendedTextBox;
import com.ericsson.eniq.events.widgets.client.threshold.ThresholdWidget;
import com.ericsson.eniq.events.widgets.client.threshold.dataType.IThreshold;
import com.ericsson.eniq.events.widgets.client.threshold.dataType.IThresholdItem;
import com.ericsson.eniq.events.widgets.client.threshold.events.IThresholdWidgetChange;
import com.ericsson.eniq.events.widgets.client.threshold.events.ThresholdWidgetChangeEvent;
import com.ericsson.eniq.events.widgets.client.togglebuttons.ToggleButtonMiniOnOff;
import com.ericsson.eniq.events.widgets.client.togglebuttons.ToggleButtonOnOff;
import com.ericsson.eniq.events.widgets.client.togglebuttons.ToggleRail;
import com.ericsson.eniq.events.widgets.client.window.FloatingWindow;
import com.ericsson.eniq.events.widgets.client.window.title.TitleWindow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Main entry point for Widget showcase
 *
 * @author evyagrz
 * @since 06 2012
 *
 * NB Image Sizes for Widgets in the Menu should be W 170 H 75.
 */
public class ShowcaseExplorer implements ClickHandler {

    private static CommonInjector commonInjector;

    private EventBus eventbus;

    private static String CONTENT = "se-content";

    private Label selected;

    private AbsolutePanel contentPanel;

    private boolean enabled = true;

    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    ToggleRail TopNav = new ToggleRail("100%");

    final HorizontalPanel Menu = new HorizontalPanel();

    final HorizontalPanel WidgetSelector = new HorizontalPanel();

    final HorizontalPanel widgetList = createWidgetList();

    final VerticalPanel LinksHolder = new VerticalPanel();

    FlowPanel FP = new FlowPanel();
    final Label Spacer = new Label("\n\n");

    final CollapsePanelResourceBundle bundle = GWT.create(CollapsePanelResourceBundle.class);
    final ImageButton RightButton = new ImageButton(bundle.arrow_across());
    public int Widgetcounter;
    public HorizontalPanel Holder = new HorizontalPanel();
    public HorizontalPanel Holder2 = new HorizontalPanel();
    final Image LeftButton = new Image("src/main/Images/Previousnew.jpg");
    AbsolutePanel FPnew = new AbsolutePanel();
    FlowPanel FPAgain = new FlowPanel();
    final Label ButtonsLabel = new Label ("Buttons");                    //Labels for the bottom bar
    final Label MapLabel = new Label ("Map");
    final Label ToggleLabel = new Label ("Toggle");
    final Label PopUpLabel = new Label ("Pop-Up");
    final Label TextLabel = new Label ("Text");
    final Label MiscLabel = new Label ("Misc");
    final Label BetaLabel = new Label ("Beta");

    public Label Title1;
    public Label Title2;
    public Label Title3;
    public Label Title4;
    public Label Title5;

//    @Override
    public void onModuleLoad() {
        Spacer.getElement().getStyle().setProperty("whiteSpace", "pre");
        RightButton.getElement().getStyle().setMarginTop(45, Unit.PX);
        LeftButton.getElement().getStyle().setMarginTop(45, Unit.PX);
        FPnew.setSize("680px", "21px");
        FPAgain.setSize("680px","25px");
        Holder.setSize("680px","100px");
        Menu.setHeight("125px");
        Menu.setWidth("680px");
        LinksHolder.setSize("680px","300px");     //For more Links Increase size
        FPnew.addStyleName("Centre");
        Menu.addStyleName("Border");
        LinksHolder.addStyleName("Border");
        FP.addStyleName("PaddingLeft");
        commonInjector = CommonMain.getCommonInjector();
        eventbus = commonInjector.getEventBus();
        final RootPanel content = RootPanel.get(CONTENT);
        contentPanel = new AbsolutePanel();
        contentPanel.getElement().getStyle().setProperty("minHeight", "780px");
        content.add(contentPanel);
        contentPanel.add(widgetList);
        contentPanel.add(Menu);
        contentPanel.addStyleName("Menu");

        TopNav.addValueChangeHandler (new ValueChangeHandler() {
            @Override
            public void onValueChange(ValueChangeEvent event) {

                contentPanel.removeStyleName("Menu");
                Holder.clear();
                contentPanel.clear();
                contentPanel.add(widgetList);
                contentPanel.add(Menu);
                Menu.clear();
                String Source = TopNav.getValue().toString();
                TopNav.remove("Widgets");             //Doing this removes the blue highlight bar.
                TopNav.remove("Download");
                TopNav.remove("Links");
                TopNav.remove("How To");
                TopNav.add("Widgets");
                TopNav.add("Download");
                TopNav.add("Links");
                TopNav.add("How To");
                if (Source.equals("Widgets")){
                    contentPanel.addStyleName("Menu");
                    contentPanel.remove(WidgetSelector);
                    BuildWidgetSelector();
                    FPnew.clear();
                    WidgetSelector.getElement().getStyle().setZIndex(20);
                    FPnew.add(WidgetSelector,0,0);
                    FPAgain.add(FPnew);
                    contentPanel.add(FPAgain);

                           ButtonsLabel.addClickHandler(new ClickHandler() {
                               @Override
                               public void onClick(final ClickEvent event) {
                                 ButtonsLabel.removeStyleName("Normal");
                                 ButtonsLabel.addStyleName("Decor");
                                 MapLabel.addStyleName("Normal");
                                 ToggleLabel.addStyleName("Normal");
                                 PopUpLabel.addStyleName("Normal");
                                 TextLabel.addStyleName("Normal");
                                 MiscLabel.addStyleName("Normal");
                                 BetaLabel.addStyleName("Normal");
                                 Buttons();                   //Add your Button Widgets in the Buttons() Method. Step by Step Guide to adding Widgets to the Menu.
                               }
                           });

                         MapLabel.addClickHandler(new ClickHandler() {
                         @Override
                             public void onClick(final ClickEvent event) {
                                 MapLabel.removeStyleName("Normal");
                                 ButtonsLabel.addStyleName("Normal");
                                 MapLabel.addStyleName("Decor");
                                 ToggleLabel.addStyleName("Normal");
                                 PopUpLabel.addStyleName("Normal");
                                 TextLabel.addStyleName("Normal");
                                 MiscLabel.addStyleName("Normal");
                                 BetaLabel.addStyleName("Normal");
                                 Map();                   //Add your Map Widgets in the Map() Method
                         }                           });

                         TextLabel.addClickHandler(new ClickHandler() {
                             @Override
                             public void onClick(final ClickEvent event) {
                                 TextLabel.removeStyleName("Normal");
                                 ButtonsLabel.addStyleName("Normal");
                                 MapLabel.addStyleName("Normal");
                                 ToggleLabel.addStyleName("Normal");
                                 PopUpLabel.addStyleName("Normal");
                                 TextLabel.addStyleName("Decor");
                                 MiscLabel.addStyleName("Normal");
                                 BetaLabel.addStyleName("Normal");
                                 Text();                   //Add your Text Widgets in the Map() Method
                             }
                         });


                         PopUpLabel.addClickHandler(new ClickHandler() {
                             @Override
                             public void onClick(final ClickEvent event) {
                                 PopUpLabel.removeStyleName("Normal");
                                 ButtonsLabel.addStyleName("Normal");
                                 MapLabel.addStyleName("Normal");
                                 ToggleLabel.addStyleName("Normal");
                                 PopUpLabel.addStyleName("Decor");
                                 TextLabel.addStyleName("Normal");
                                 MiscLabel.addStyleName("Normal");
                                 BetaLabel.addStyleName("Normal");
                                 PopUp();                   //Add your Pop Up Widgets in the Map() Method
                             }
                         });

                         MiscLabel.addClickHandler(new ClickHandler() {
                              @Override
                              public void onClick(final ClickEvent event) {
                                 MiscLabel.removeStyleName("Normal");
                                 ButtonsLabel.addStyleName("Normal");
                                 MapLabel.addStyleName("Normal");
                                 ToggleLabel.addStyleName("Normal");
                                 PopUpLabel.addStyleName("Normal");
                                 TextLabel.addStyleName("Normal");
                                 MiscLabel.addStyleName("Decor");
                                 BetaLabel.addStyleName("Normal");
                                 Misc();                   //Add your Misc Widgets in the Map() Method
                             }
                         });

                         BetaLabel.addClickHandler(new ClickHandler() {
                               @Override
                               public void onClick(final ClickEvent event) {
                                 BetaLabel.removeStyleName("Normal");
                                 ButtonsLabel.addStyleName("Normal");
                                 MapLabel.addStyleName("Normal");
                                 ToggleLabel.addStyleName("Normal");
                                 PopUpLabel.addStyleName("Normal");
                                 TextLabel.addStyleName("Normal");
                                 MiscLabel.addStyleName("Normal");
                                 BetaLabel.addStyleName("Decor");
                                 Beta();                   //Add your Beta Widgets in the Map() Method
                         }
                           });

                           ToggleLabel.addClickHandler(new ClickHandler() {
                               @Override
                               public void onClick(final ClickEvent event) {
                                 ToggleLabel.removeStyleName("Normal");
                                 ButtonsLabel.addStyleName("Normal");
                                 MapLabel.addStyleName("Normal");
                                 ToggleLabel.addStyleName("Decor");
                                 PopUpLabel.addStyleName("Normal");
                                 TextLabel.addStyleName("Normal");
                                 MiscLabel.addStyleName("Normal");
                                 BetaLabel.addStyleName("Normal");
                                 Toggle();                   //Add your Toggle Widgets in the Map() Method
                             }
                           });
                    }

                if(Source.equals("Download"))            //Add a Link to download the Widget Jar file.
                {
                    contentPanel.addStyleName("Menu");
                }

                if(Source.equals("Links"))     //Add the Links in here
                {
                   Links();
                   contentPanel.addStyleName("Menu");
                }

                if(Source.equals("How To"))
                {   contentPanel.clear();
                    contentPanel.addStyleName("Menu");
                    contentPanel.add(widgetList);
                    Label Header = new Label ("Will Contain Useful Guides");
                    Header.setStyleName("Headers");
                    contentPanel.add(Spacer);
                    contentPanel.add(Header);
                }
            }
        });
        Menu.setWidth("680px");
        RootPanel.get(CONTENT).add(contentPanel); //Adds the Widget List in under the Header
        colorCode();
    }

    @Override
    public void onClick(final ClickEvent event) {
        if (event.getSource() instanceof Label) {
            // First remove prev selection
            if (selected != null) {
                selected.removeStyleName("selected-item");
            }

            selected = (Label) event.getSource();
            selected.addStyleName("selected-item");

            final Widget widget = createWidget(selected.getElement().getId());
            if (widget != null) {
                contentPanel.clear();
                contentPanel.add(widget);
            }
            // Need to call this method to highlight example code
            colorCode();
        }
    }

    private HorizontalPanel createWidgetList() {

        TopNav.add("Widgets");
        TopNav.add("Download");
        TopNav.add("Links");
        TopNav.add("How To");
        HorizontalPanel listContainer = new HorizontalPanel();
        listContainer.add(TopNav);
        return listContainer;
    }

    private Widget createWidget(final String id) {
        if ("MapContextMenu".equals(id)) {
            final MapContextMenu mcm = new MapContextMenu();
            List<PopCell> widgets = new ArrayList<PopCell>();

            for (int j = 0; j < 10; j++) {
                ClusterPopCell popCell = new ClusterPopCell(24, 200, "#e1de5a", "Test text..." + j);
                final LinkedHashMap<String, String> popupData = new LinkedHashMap<String, String>();
                popupData.put("Cell ID ", "1234567");
                popupData.put("KPI ", "This is a useless KPI");
                popupData.put("Value ", "100" + "%");
                popCell.setPopupData(popupData);
                popCell.setDetailsPopupTitle("My Title");
                widgets.add(popCell);
            }
            mcm.populate(widgets);

            Button textContext = new Button("Show Map Context Menu");
            textContext.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {

                    // Reposition the popup relative to the button
                    Widget source = (Widget) event.getSource();
                    int left = source.getAbsoluteLeft() + 10;
                    int top = source.getAbsoluteTop() + 10;
                    mcm.setPopupPosition(left, top);
                    mcm.show();
                }
            });

            final String source = wrapCode(sourceBundle.mapContextMenu().getText());
            return wrapWidgetAndSource(textContext, source);
        }

        if ("Breadcrumb".equals(id)) {
            final BreadCrumb breadCrumb = new BreadCrumb();
            breadCrumb.addBreadCrumb("Main");
            breadCrumb.addBreadCrumb("Navigation");
            breadCrumb.addBreadCrumb("Example");

            final String source = wrapCode(sourceBundle.breadcrumb().getText());
            return wrapWidgetAndSource(breadCrumb, source);
        }

        if ("Button".equals(id)) {
            final FlowPanel container = new FlowPanel();
            container.getElement().getStyle().setWidth(300, Unit.PX);

            final CollapsePanelResourceBundle bundle = GWT.create(CollapsePanelResourceBundle.class);
            final ImageButton imageButton = new ImageButton(bundle.arrow_across());
            imageButton.getElement().getStyle().setMargin(3, Unit.PX);
            container.add(imageButton);

            final CogButton cog1 = new CogButton();
            cog1.getElement().getStyle().setMargin(3, Unit.PX);
            container.add(cog1);

            final Button button = new Button("Standard Button");
            container.add(button);

            final String source = wrapCode(sourceBundle.button().getText());
            return wrapWidgetAndSource(container, source);
        }

        if ("DropDownMenu".equals(id)) {

            final List<StringDropDownItem> menuValues = prepareDropDownList();

            VerticalPanel holderPanel = new VerticalPanel();

            final DropDownMenu dropDownMenu = new DropDownMenu();
            dropDownMenu.setWidth("190px");
            dropDownMenu.update(menuValues);
            dropDownMenu.setValue(new StringDropDownItem("Value 2"));
            dropDownMenu.getElement().getStyle().setMarginBottom(10, Unit.PX);

            holderPanel.add(dropDownMenu);

            final DropDown<StringDropDownItem> testDropdown = new DropDown<StringDropDownItem>();
            testDropdown.update(menuValues);
            testDropdown.getElement().getStyle().setMarginBottom(10, Unit.PX);

            final Button enable = new Button("Enable/Disable");
            enable.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                   testDropdown.setEnabled(enabled);
                    enabled=!enabled;

                }
            });

            holderPanel.add(testDropdown);
            testDropdown.setEnabled(enabled);

            holderPanel.add(enable);

            final String source = wrapCode(sourceBundle.DropDown().getText());
            return wrapWidgetAndSource(holderPanel, source);
        }

        if ("Calendar".equals(id)) {
            final CalendarPopUp calendarPanel = new CalendarPopUp();
            calendarPanel.setMaxNumberDaysRange(7);
           Button calendarButton = new Button("Show Calendar");
           calendarButton.addClickHandler(new ClickHandler() {
               @Override
               public void onClick(ClickEvent event) {
                   calendarPanel.show();
               }
           });

            final String source = wrapCode(sourceBundle.calendar().getText());
            return wrapWidgetAndSource(calendarButton, source);
        }

        if ("CollapsePanel".equals(id)) {
            final CollapsePanel collapsePanel = new CollapsePanel();
            collapsePanel.setWidth("300px");
            collapsePanel.setText("Simple collapse panel");
            collapsePanel.setContent(new Label("Just some content, can be any widget in fact"));

            final String source = wrapCode(sourceBundle.collapsepanel().getText());
            return wrapWidgetAndSource(collapsePanel, source);
        }

        if ("MessagePanel".equals(id)) {
            final ComponentMessagePanel messagePanel = new ComponentMessagePanel();
            messagePanel.setWidth("300px");
            messagePanel.populate(ComponentMessageType.INFO, "Simple message example");

            final String source = wrapCode(sourceBundle.messagedpanel().getText());
            return wrapWidgetAndSource(messagePanel, source);
        }

        if ("MessageDialog".equals(id)) {
            final VerticalPanel container = new VerticalPanel();
            final MessageDialog messageDialog = new MessageDialog();
            //container.clear();
            final Button WarningButton = new Button("Show Warning message");
            final Button ErrorButton = new Button("Show Error message");
            final Button InfoButton = new Button("Show Info message");
            WarningButton.setWidth("150px");
            ErrorButton.setWidth("150px");
            InfoButton.setWidth("150px");

            container.setHeight("100px");
            container.add(ErrorButton);
            container.add(InfoButton);
            container.add(WarningButton);
            WarningButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent event) {
                    messageDialog.show("Title", "This is the content", MessageDialog.DialogType.WARNING);
                }
            });
            InfoButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent event) {
                    messageDialog.show("Title", "This is the content", MessageDialog.DialogType.INFO);
                }
            });
            ErrorButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent event) {
                    messageDialog.show("Title", "This is the content", MessageDialog.DialogType.ERROR);
                }
            });

            final String source = wrapCode(sourceBundle.messagedialog().getText());
            return wrapWidgetAndSource(container, source);
        }

        if ("FloatingWindow".equals(id)) {
            final FloatingWindow floatingWindow = new FloatingWindow(contentPanel, 300, 200, "Floating Window Example");
            floatingWindow.setFooterText("Text!!!");

            final Button ExampleFloat = new Button("Floating Window Example");     //Adds a button to the
            SimplePanel p = new SimplePanel();
            p.setHeight("10px");
            p.setWidth("10px");
            p.getElement().getStyle().setBackgroundColor("red");
            floatingWindow.setFooterWidget(p);                         //Adds the simple Panel to the footer of the floating window
            Label label = new Label("Main Text!!!");
            floatingWindow.add(label);

            ExampleFloat.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent event) {
                    floatingWindow.getElement().getStyle().setPosition(Style.Position.RELATIVE);
                    floatingWindow.show();

                }
            });

            final String source = wrapCode(sourceBundle.FloatingWindow().getText());
            return wrapWidgetAndSource(ExampleFloat, source);
        }

        if ("TitleWindow".equals(id)) {
            final AbsolutePanel titleWindowContainer = new AbsolutePanel();
            titleWindowContainer.getElement().getStyle().setProperty("minHeight", "120px");
            final TitleWindow titleWindow = new TitleWindow("This window is not draggable", 60, false);
            titleWindowContainer.add(titleWindow);

            final TitleWindow titleWindowDraggable = new TitleWindow("This window is draggable (top changes "
                    + "onMouseOver)", 60);
            titleWindowContainer.add(titleWindowDraggable);
            final String source = wrapCode(sourceBundle.titlewindow().getText());
            return wrapWidgetAndSource(titleWindowContainer, source);
        }

        if ("ToggleButtonOnOff".equals(id)) {
            final ToggleButtonOnOff toggleButtonOnOff = new ToggleButtonOnOff();

            final String source = wrapCode(sourceBundle.togglebuttononoff().getText());
            return wrapWidgetAndSource(toggleButtonOnOff, source);
        }

        if ("ToggleButtonMiniOnOff".equals(id)) {
            final FlowPanel buttonsContainer = new FlowPanel();
            // buttonsContainer.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
            buttonsContainer.getElement().getStyle().setWidth(100, Unit.PX);
            final ToggleButtonMiniOnOff toggleButtonMiniOnOff1 = new ToggleButtonMiniOnOff("#549895",
                    "Button 1: On - Enabled");
            final ToggleButtonMiniOnOff toggleButtonMiniOnOff2 = new ToggleButtonMiniOnOff("#549895",
                    "Button 2: Off - Enabled");
            final ToggleButtonMiniOnOff toggleButtonMiniOnOff3 = new ToggleButtonMiniOnOff("#549895",
                    "Button 3: On - Disabled");
            final ToggleButtonMiniOnOff toggleButtonMiniOnOff4 = new ToggleButtonMiniOnOff("#549895",
                    "Button 4: Off - Disabled");
            final ToggleButtonMiniOnOff toggleButtonMiniOnOff5 = new ToggleButtonMiniOnOff("#89ba17",
                    "Button 5: A different color");
            final ToggleButtonMiniOnOff toggleButtonMiniOnOff6 = new ToggleButtonMiniOnOff("#00285f",
                    "Button 6: A different color");

            toggleButtonMiniOnOff4.setSize(6, 12, Unit.PX);
            toggleButtonMiniOnOff5.setSize(12, 12, Unit.PX);

            //set up the margins between the buttons...
            toggleButtonMiniOnOff1.getElement().getStyle().setMargin(3, Unit.PX);
            toggleButtonMiniOnOff2.getElement().getStyle().setMargin(3, Unit.PX);
            toggleButtonMiniOnOff3.getElement().getStyle().setMargin(3, Unit.PX);
            toggleButtonMiniOnOff4.getElement().getStyle().setMargin(3, Unit.PX);
            toggleButtonMiniOnOff5.getElement().getStyle().setMargin(3, Unit.PX);
            toggleButtonMiniOnOff6.getElement().getStyle().setMargin(3, Unit.PX);
            //set the tab index...
            toggleButtonMiniOnOff1.setTabIndex(5);
            toggleButtonMiniOnOff2.setTabIndex(4);
            toggleButtonMiniOnOff3.setTabIndex(3);
            toggleButtonMiniOnOff4.setTabIndex(2);
            toggleButtonMiniOnOff5.setTabIndex(1);
            toggleButtonMiniOnOff6.setTabIndex(6);
            //set the state of the buttons...
            //toggle1
            toggleButtonMiniOnOff1.setToggleOn();
            toggleButtonMiniOnOff1.setEnabled(true);
            //toggle2
            toggleButtonMiniOnOff2.setToggleOff();
            toggleButtonMiniOnOff2.setEnabled(true);
            //toggle3
            toggleButtonMiniOnOff3.setToggleOn();
            toggleButtonMiniOnOff3.setEnabled(false);
            //toggle4
            toggleButtonMiniOnOff4.setToggleOff();
            toggleButtonMiniOnOff4.setEnabled(false);
            //toggle5
            toggleButtonMiniOnOff5.setToggleOn();
            toggleButtonMiniOnOff5.setEnabled(true);
            //toggle 6
            toggleButtonMiniOnOff6.setToggleOn();
            toggleButtonMiniOnOff6.setEnabled(true);
            toggleButtonMiniOnOff6.addStyleName("rounded-corners");
            toggleButtonMiniOnOff6.setSize(12, 12, Unit.PX);

            buttonsContainer.add(toggleButtonMiniOnOff1);
            buttonsContainer.add(toggleButtonMiniOnOff2);
            buttonsContainer.add(toggleButtonMiniOnOff3);
            buttonsContainer.add(toggleButtonMiniOnOff4);
            buttonsContainer.add(toggleButtonMiniOnOff5);
            buttonsContainer.add(toggleButtonMiniOnOff6);
            final String source = wrapCode(sourceBundle.togglebuttonminionoff().getText());
            return wrapWidgetAndSource(buttonsContainer, source);
        }

        if ("ToggleRail".equals(id)) {
            final ToggleRail toggleRail = new ToggleRail("100%");
            toggleRail.add("IMSI");
            toggleRail.add("MSISDN");
            toggleRail.setValue("IMSI", true);
            final String source = wrapCode(sourceBundle.togglerail().getText());
            return wrapWidgetAndSource(toggleRail, source);
        }

        if ("ExtendedTextBox".equals(id)) {
            final FlowPanel container = new FlowPanel();
            container.getElement().getStyle().setWidth(300, Unit.PX);
            //first textbox...
            final ExtendedTextBox extendedTextBox = new ExtendedTextBox();
            extendedTextBox.setWidth("100%");
            extendedTextBox.setDefaultText("This is the default text.");

            //second textbox...
            final ExtendedTextBox extendedTextBoxWithError = new ExtendedTextBox();
            extendedTextBoxWithError.setWidth("100%");
            extendedTextBoxWithError.setDefaultText("This text is invalid.");
            extendedTextBoxWithError.highlightInvalidField(true);

            container.add(extendedTextBox);
            container.add(extendedTextBoxWithError);

            final String source = wrapCode(sourceBundle.extendedtextbox().getText());
            return wrapWidgetAndSource(container, source);
        }

        if ("ExtendedSuggestBox".equals(id)) {
            final FlowPanel container = new FlowPanel();
            container.getElement().getStyle().setWidth(300, Unit.PX);
            final MultiWordSuggestOracle multiWordSuggestOracle = new MultiWordSuggestOracle();
            multiWordSuggestOracle.add("suggestion1");
            multiWordSuggestOracle.add("suggestion2");
            final ExtendedSuggestBox extendedSuggestBox = new ExtendedSuggestBox(multiWordSuggestOracle);
            extendedSuggestBox.setWidth("100%");
            extendedSuggestBox.setDefaultText("This suggestion box is disabled :)");
            extendedSuggestBox.getElement().getStyle().setMargin(3, Unit.PX);

            final ExtendedSuggestBox extendedSuggestBox1 = new ExtendedSuggestBox(multiWordSuggestOracle);
            extendedSuggestBox1.setWidth("100%");
            extendedSuggestBox1.setDefaultText("type the letter s");
            extendedSuggestBox1.getElement().getStyle().setMargin(3, Unit.PX);
            extendedSuggestBox.setEnabled(false);
            container.add(extendedSuggestBox);
            container.add(extendedSuggestBox1);
            final String source = wrapCode(sourceBundle.extendedsuggestbox().getText());
            return wrapWidgetAndSource(container, source);
        }

        if ("TextButton".equals(id)) {
            final TextButton textButton1 = new TextButton("Text Button");
            textButton1.setWidth("100%");
            textButton1.getElement().getStyle().setMarginBottom(5, Unit.PX);

            final TextButton textButton2 = new TextButton("Text Button 2 Disabled");
            textButton2.setEnabled(false);
            textButton2.setWidth("100%");
            textButton2.getElement().getStyle().setMarginBottom(5, Unit.PX);

            final TextButton textButton3 = new TextButton("Text Button 3");
            textButton3.setWidth("100%");

            final FlowPanel buttonExamplePanel = new FlowPanel();
            buttonExamplePanel.add(textButton1);
            buttonExamplePanel.add(textButton2);
            buttonExamplePanel.add(textButton3);

            final String source = wrapCode(sourceBundle.textbutton().getText());
            return wrapWidgetAndSource(buttonExamplePanel, source);
        }

        if ("PopCell".equals(id)) {
            FlowPanel flowPanel = new FlowPanel();

            for (int j = 0; j < 10; j++){
                ClusterPopCell popCell = new ClusterPopCell(24, 200, "#e1de5a", "Test text...");
                final LinkedHashMap<String, String> popupData = new LinkedHashMap<String, String>();
                popupData.put("Cell ID ", "1234567");
                popupData.put("KPI ", "This is a useless KPI");
                popupData.put("Value ", "100"+"%");
                popCell.setPopupData(popupData);
                flowPanel.add(popCell);
            }
            final String source = wrapCode(sourceBundle.textPopCell().getText());
            return wrapWidgetAndSource(flowPanel, source);
        }

        if ("ThresholdWidget".equals(id)) {

            eventbus.addHandler(ThresholdWidgetChangeEvent.TYPE, new IThresholdWidgetChange() {
                @Override
                public void handleThresholdWidgetChange(final IThreshold threshold) {
                    final MessageDialog messageDialog = new MessageDialog();
                    final List<IThresholdItem> items = threshold.getThresholdItems();
                    String message = "\n";
                    for (final IThresholdItem item : items) {
                        message = message + item.getColor() + " is enabled: " + item.isEnabled() + ", max = "
                                + item.getMax() + ", min = " + item.getMin() + "\n";
                    }
                    messageDialog.show("Success", "Got a event from the Threshold Widget:" + message,
                            MessageDialog.DialogType.INFO);
                }
            });
            final ThresholdWidget thresholdWidget = new ThresholdWidget(4, "title");
            thresholdWidget.getElement().getStyle().setPosition(Style.Position.RELATIVE);
            final String source = wrapCode(sourceBundle.Threshold().getText());
            return wrapWidgetAndSource(thresholdWidget, source);
        }
        if ("SlideBar".equals(id)) {

            final List<ColoredBarDataType> coloredBarDataTypes = new ArrayList<ColoredBarDataType>();
            coloredBarDataTypes.add(new ColoredBarDataType(40, "green", "item1"));
            coloredBarDataTypes.add(new ColoredBarDataType(30, "blue", "item2"));
            coloredBarDataTypes.add(new ColoredBarDataType(0, "orange", "item3"));
            coloredBarDataTypes.add(new ColoredBarDataType(20, "gray", "item4"));
            coloredBarDataTypes.add(new ColoredBarDataType(10, "red", "item5"));
            final SlideBarDataType dataType = new SlideBarDataType(coloredBarDataTypes, 100, "Service Request");
            final SlideGroup slideBar = new SlideGroup(dataType);
            slideBar.setHeight("100px");
            final String source = wrapCode(sourceBundle.Slidebar().getText());
            return wrapWidgetAndSource(slideBar, source);
        }
        return null;
    }

    private List<StringDropDownItem> prepareDropDownList() {
        final List<StringDropDownItem> menuValues = new ArrayList<StringDropDownItem>();
        menuValues.add(new StringDropDownItem("Separetor 1") {
            @Override
            public boolean isSeparator() {
                return true;
            }
        });
        menuValues.add(new StringDropDownItem("Value 1"));
        menuValues.add(new StringDropDownItem("Value 2"));
        menuValues.add(new StringDropDownItem("Value 3"));
        menuValues.add(new StringDropDownItem("Separetor 2") {
            @Override
            public boolean isSeparator() {
                return true;
            }
        });

        menuValues.add(new StringDropDownItem("Value 4"));
        menuValues.add(new StringDropDownItem("Value 5 Long Text Example - no wrapping"));
        menuValues.add(new StringDropDownItem("Value 6"));
        menuValues.add(new StringDropDownItem("Value 7"));
        menuValues.add(new StringDropDownItem("Value 8"));
        menuValues.add(new StringDropDownItem("Value 9"));
        menuValues.add(new StringDropDownItem("Value 10"));
        menuValues.add(new StringDropDownItem("Value 11"));
        menuValues.add(new StringDropDownItem("Value 12"));
        menuValues.add(new StringDropDownItem("Value 13"));
        menuValues.add(new StringDropDownItem("Value 14"));
        menuValues.add(new StringDropDownItem("Value 15"));
        menuValues.add(new StringDropDownItem("Value 16"));
        menuValues.add(new StringDropDownItem("Value 17"));
        menuValues.add(new StringDropDownItem("Value 18"));
        menuValues.add(new StringDropDownItem("Value 19"));
        menuValues.add(new StringDropDownItem("Value 20"));

        return menuValues;
    }

    private String wrapCode(final String source) {
        return "<code class=\"prettyprint lang-java\">" + source + "</code>";
    }

    private FlowPanel wrapWidgetAndSource(final Widget widget, final String source) {
        final FlowPanel panel = new FlowPanel();

        final SimplePanel widgetExample = new SimplePanel(widget);
        final SimplePanel sourceExample = new SimplePanel(new HTML(source));

        widgetExample.setStyleName("example-panel");
        sourceExample.setStyleName("source-panel");

        panel.add(widgetExample);
        panel.add(sourceExample);
        return panel;
    }
    /**
     * Need to call this to color code
     */
    private native void colorCode() /*-{
                $wnd.prettyPrint();
    }-*/;

    public Widget Buttons()
    {
        Menu.clear();
        contentPanel.clear();
        Holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        contentPanel.add(widgetList);
        contentPanel.add(Menu);      //Re adds the Menu
        contentPanel.add(FPAgain);

        Image PointerButton = new Image("src/main/Images/Pointer.png"); //Adds the Pointer image
        FPnew.clear();
        PointerButton.getElement().getStyle().setZIndex(25);   //Places the pointer image at the location 25 on the Z axis
        WidgetSelector.getElement().getStyle().setZIndex(20);  //Places the WidgetSelector at the location 20 on the z axis
        FPnew.add(PointerButton,37,0);                         //Places the Pointer 37 pixels to the right of the extreme left of the Panel. Readjust to centre on the selected button
        FPnew.add(WidgetSelector,0,0);                         //Places the WidgetSelector at the edge of the panel
        FPAgain.add(FPnew);



        //To Create a Widget for the Menu
        Image TextButton = new Image("src/main/Images/TextButton.jpg");   //Adds the Image you want to use  (Size W 170 H 75)
        Title1 = new Label ("Text Button");                  //Set the Title of the Widget
        Title1.getElement().getStyle().setMarginBottom(5, Unit.PX); //Sets the Bottom Margin
        Title1.getElement().getStyle().setMarginTop(5, Unit.PX);      //Sets the Top Margin
        final FlowPanel Widget1 = new FlowPanel();                    //Creates our Panel (Widget + Number needed for Loop)
        Widget1.add(TextButton);                                     //Adds the Image to the Panel
        Widget1.add(Title1);                                        //Adds the Title to the Panel
        Widget1.setSize("170px","90px");                           //Sets the size of the panel (Width, Height)
        Widget1.setStyleName("Align");                            //Calls the CSS
        Widget1.getElement().getStyle().setMarginTop(5, Unit.PX);
        //Repeat for each Widget in this menu

        Image ImgButton = new Image("src/main/Images/ImageButton.jpg");
        Title2 = new Label ("Image Button");
        Title2.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title2.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget2 = new FlowPanel();
        Widget2.add(ImgButton);
        Widget2.add(Title2);
        Widget2.setSize("170px","90px");
        Widget2.setStyleName("Align");
        Widget2.getElement().getStyle().setMarginTop(5, Unit.PX);

        Image Cog = new Image("src/main/Images/Cog.jpg");
        Title3 = new Label ("Cog Button");
        Title3.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title3.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget3 = new FlowPanel();
        Widget3.add(Cog);
        Widget3.add(Title3);
        Widget3.setSize("170px","90px");
        Widget3.setStyleName("Align");
        Widget3.getElement().getStyle().setMarginTop(5, Unit.PX);
        Holder.clear();
        Title1.addStyleName("Normal");
        Title2.addStyleName("Normal");
        Title3.addStyleName("Normal");

//                                Holder.add(LeftButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Holder.add(Widget1);
        Holder.add(Widget2);
        Holder.add(Widget3);
//                                Holder.add(RightButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Menu.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        Menu.add(Holder);
        contentPanel.add(FPAgain);

//                                 Uncomment When you have more than Three Widgets in the Menu Panel. Allows for toggle through
//                                 Add and Remove Widgets as Need Be.

//                                LeftButton.addClickHandler(new ClickHandler() {
//
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter > 2) {
//                                            Widgetcounter--;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget" + (Widgetcounter - 1);
//                                            String Widgettwo = "Widget" + (Widgetcounter);
//                                            String Widgetthree = "Widget" + (Widgetcounter + 1);
//                                            if (Widgetone.equals("Widget1") || Widgettwo.equals("Widget1") || Widgetthree.equals("Widget1")) {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2") | Widgettwo.equals("Widget2") || Widgetthree.equals("Widget2")) {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3") | Widgettwo.equals("Widget3") || Widgetthree.equals("Widget3")) {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4") | Widgettwo.equals("Widget4") || Widgetthree.equals("Widget4")) {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5") | Widgettwo.equals("Widget5") || Widgetthree.equals("Widget5")) {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6") | Widgettwo.equals("Widget6") || Widgetthree.equals("Widget6")) {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//
//                                        }
//                                    }
//                                });
//
//                                RightButton.addClickHandler(new ClickHandler() {
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter <5)       //Change to your widget count minus 1
//                                        {Widgetcounter++;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget"+(Widgetcounter-1);
//                                            String Widgettwo = "Widget"+(Widgetcounter);
//                                            String Widgetthree = "Widget"+(Widgetcounter+1);
//                                            if (Widgetone.equals("Widget1")||Widgettwo.equals("Widget1")||Widgetthree.equals("Widget1"))
//                                            {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2")|Widgettwo.equals("Widget2")||Widgetthree.equals("Widget2"))
//                                            {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3")|Widgettwo.equals("Widget3")||Widgetthree.equals("Widget3"))
//                                            {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4")|Widgettwo.equals("Widget4")||Widgetthree.equals("Widget4"))
//                                            {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5")|Widgettwo.equals("Widget5")||Widgetthree.equals("Widget5"))
//                                            {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6")|Widgettwo.equals("Widget6")||Widgetthree.equals("Widget6"))
//                                            {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//                                        }
//                                    }
//                                });

        TextButton.addClickHandler(new ClickHandler() {                                           //Adds the Handler for the Img
            @Override
            public void onClick(final ClickEvent event) {                                       //On CLick
                Title1.removeStyleName("Normal");
                Title1.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title3.addStyleName("Normal");
                final String source = wrapCode(sourceBundle.button().getText());             //Get the Text from the .txt file
                colorCode();                                                                //Colors the Text in the same style as code
                Button TextButton2 = new TextButton("Text Button");                        //Add a Widget to show as an example
                TextButton2.getElement().getStyle().setMarginBottom(5, Unit.PX);
                FP.clear();                                                             //Clear the panel
                FP.add(Spacer);                                                        //Adds two return lines to add padding between the flow panel and Menu
                FP.add(wrapWidgetAndSource(TextButton2, source));                     //Adds the Widgets to the Panel
                contentPanel.remove(FP);                                             //Remove the old Flow panel
                contentPanel.add(FP);                                               //Add the new Flow panel

                colorCode();
            }
        });
        Cog.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title3.removeStyleName("Normal");
                Title1.addStyleName("Normal");
                Title3.addStyleName("Decor");
                Title2.addStyleName("Normal");
                final String source = wrapCode(sourceBundle.CogButton().getText());
                colorCode();
                final CogButton cog1 = new CogButton();
                cog1.getElement().getStyle().setMargin(3, Unit.PX);
                FP.clear();
                FP.add(Spacer);
                FP.add(wrapWidgetAndSource(cog1, source));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        ImgButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title2.removeStyleName("Normal");
                Title1.addStyleName("Normal");
                Title3.addStyleName("Normal");
                Title2.addStyleName("Decor");
                final String source = wrapCode(sourceBundle.Imagebutton().getText());
                colorCode();
                final CollapsePanelResourceBundle bundle = GWT.create(CollapsePanelResourceBundle.class);
                final ImageButton imageButton = new ImageButton(bundle.arrow_across());
                imageButton.getElement().getStyle().setMargin(3, Unit.PX);
                FP.clear();
                FP.add(Spacer);
                FP.add(wrapWidgetAndSource(imageButton, source));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        return contentPanel;
    }

    public Widget Map()
    {

        Menu.clear();
        contentPanel.clear();
        Holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        contentPanel.add(widgetList);
        contentPanel.add(Menu);      //Re adds the Menu
        contentPanel.add(FPAgain);
        Image PointerButton = new Image("src/main/Images/Pointer.png");

        FPnew.clear();
        PointerButton.getElement().getStyle().setZIndex(25);
        WidgetSelector.getElement().getStyle().setZIndex(20);
        FPnew.add(PointerButton,135,0);
        FPnew.add(WidgetSelector,0,0);
        FPAgain.add(FPnew);

        Image ContextMenu = new Image("src/main/Images/ContextMenu.jpg");
        Title1 = new Label ("Context Menu");
        Title1.getElement().getStyle().setMarginTop(5, Unit.PX);
        Title3.getElement().getStyle().setMarginBottom(5, Unit.PX);
        final FlowPanel Widget1 = new FlowPanel();
        Widget1.add(ContextMenu);
        Widget1.add(Title1);
        Widget1.setSize("170px","90px");
        Widget1.setStyleName("Align");
        Widget1.getElement().getStyle().setMarginTop(5, Unit.PX);

        Image PopCell = new Image("src/main/Images/Popcell.jpg");
        Title2 = new Label ("PopCell");
        Title2.getElement().getStyle().setMarginTop(5, Unit.PX);
        Title3.getElement().getStyle().setMarginBottom(5, Unit.PX);
        final FlowPanel Widget2 = new FlowPanel();
        Widget2.add(PopCell);
        Widget2.add(Title2);
        Widget2.setSize("170px","90px");
        Widget2.setStyleName("Align");
        Widget2.getElement().getStyle().setMarginTop(5, Unit.PX);

        Image Threshold = new Image("src/main/Images/Threshold.jpg");
        Title3 = new Label ("Threshold");
        Title3.getElement().getStyle().setMarginTop(5, Unit.PX);
        Title3.getElement().getStyle().setMarginBottom(5, Unit.PX);
        final FlowPanel Widget3 = new FlowPanel();
        Widget3.add(Threshold);
        Widget3.add(Title3);
        Widget3.setSize("170px","90px");
        Widget3.setStyleName("Align");
        Widget3.getElement().getStyle().setMarginTop(5, Unit.PX);
        Holder.clear();

        Title1.addStyleName("Normal");
        Title2.addStyleName("Normal");
        Title3.addStyleName("Normal");

//                                Holder.add(LeftButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Holder.add(Widget1);
        Holder.add(Widget2);
        Holder.add(Widget3);
//                                Holder.add(RightButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Menu.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        Menu.add(Holder);
        contentPanel.add(FPAgain);

//                                 Uncomment When you have more than Three Widgets in the Menu Panel
//                                LeftButton.addClickHandler(new ClickHandler() {
//
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter > 2) {
//                                            Widgetcounter--;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget" + (Widgetcounter - 1);
//                                            String Widgettwo = "Widget" + (Widgetcounter);
//                                            String Widgetthree = "Widget" + (Widgetcounter + 1);
//                                            if (Widgetone.equals("Widget1") || Widgettwo.equals("Widget1") || Widgetthree.equals("Widget1")) {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2") | Widgettwo.equals("Widget2") || Widgetthree.equals("Widget2")) {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3") | Widgettwo.equals("Widget3") || Widgetthree.equals("Widget3")) {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4") | Widgettwo.equals("Widget4") || Widgetthree.equals("Widget4")) {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5") | Widgettwo.equals("Widget5") || Widgetthree.equals("Widget5")) {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6") | Widgettwo.equals("Widget6") || Widgetthree.equals("Widget6")) {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//
//                                        }
//                                    }
//                                });
//
//                                RightButton.addClickHandler(new ClickHandler() {
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter <5)       //Change to your widget count minus 1
//                                        {Widgetcounter++;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget"+(Widgetcounter-1);
//                                            String Widgettwo = "Widget"+(Widgetcounter);
//                                            String Widgetthree = "Widget"+(Widgetcounter+1);
//                                            if (Widgetone.equals("Widget1")||Widgettwo.equals("Widget1")||Widgetthree.equals("Widget1"))
//                                            {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2")|Widgettwo.equals("Widget2")||Widgetthree.equals("Widget2"))
//                                            {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3")|Widgettwo.equals("Widget3")||Widgetthree.equals("Widget3"))
//                                            {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4")|Widgettwo.equals("Widget4")||Widgetthree.equals("Widget4"))
//                                            {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5")|Widgettwo.equals("Widget5")||Widgetthree.equals("Widget5"))
//                                            {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6")|Widgettwo.equals("Widget6")||Widgetthree.equals("Widget6"))
//                                            {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//                                        }
//                                    }
//                                });


        ContextMenu.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title1.removeStyleName("Normal");
                Title1.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title3.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("MapContextMenu"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        Threshold.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title3.removeStyleName("Normal");
                Title3.addStyleName("Decor");
                Title1.addStyleName("Normal");
                Title2.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("ThresholdWidget"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        PopCell.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title2.removeStyleName("Normal");
                Title2.addStyleName("Decor");
                Title3.addStyleName("Normal");
                Title1.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("PopCell"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        return contentPanel;
    }

    public Widget Text()
    {
        Menu.clear();
        contentPanel.clear();
        Holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        contentPanel.add(widgetList);
        contentPanel.add(Menu);      //Re adds the Menu
        contentPanel.add(FPAgain);
        Image PointerButton = new Image("src/main/Images/Pointer.png");

        FPnew.clear();
        PointerButton.getElement().getStyle().setZIndex(25);
        WidgetSelector.getElement().getStyle().setZIndex(20);
        FPnew.add(PointerButton,430,0);
        FPnew.add(WidgetSelector,0,0);
        FPAgain.add(FPnew);

        Image extendedTextBox = new Image("src/main/Images/ExtendedTextBox.jpg");
        Title1 = new Label ("Extended Text Box");
        Title1.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title1.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget1 = new FlowPanel();
        Widget1.add(extendedTextBox);
        Widget1.add(Title1);
        Widget1.setSize("170px","90px");
        Widget1.setStyleName("Align");
        Widget1.getElement().getStyle().setMarginTop(5, Unit.PX);

        Image extendedSuggestBox = new Image("src/main/Images/ExtendedSuggestBox.jpg");
        Title2 = new Label ("Extended Suggest Box");
        Title2.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title2.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget2 = new FlowPanel();
        Widget2.add(extendedSuggestBox);
        Widget2.add(Title2);
        Widget2.setSize("170px","90px");
        Widget2.setStyleName("Align");
        Widget2.getElement().getStyle().setMarginTop(5, Unit.PX);

        Holder.clear();
//                                Holder.add(LeftButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Holder.add(Widget1);
        Holder.add(Widget2);

        Title1.addStyleName("Normal");
        Title2.addStyleName("Normal");

//                                Holder.add(Widget3);
//                                Holder.add(RightButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Menu.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        Menu.add(Holder);
        contentPanel.add(FPAgain);

//                                 Uncomment When you have more than Three Widgets in the Menu Panel
//                                LeftButton.addClickHandler(new ClickHandler() {
//
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter > 2) {
//                                            Widgetcounter--;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget" + (Widgetcounter - 1);
//                                            String Widgettwo = "Widget" + (Widgetcounter);
//                                            String Widgetthree = "Widget" + (Widgetcounter + 1);
//                                            if (Widgetone.equals("Widget1") || Widgettwo.equals("Widget1") || Widgetthree.equals("Widget1")) {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2") | Widgettwo.equals("Widget2") || Widgetthree.equals("Widget2")) {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3") | Widgettwo.equals("Widget3") || Widgetthree.equals("Widget3")) {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4") | Widgettwo.equals("Widget4") || Widgetthree.equals("Widget4")) {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5") | Widgettwo.equals("Widget5") || Widgetthree.equals("Widget5")) {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6") | Widgettwo.equals("Widget6") || Widgetthree.equals("Widget6")) {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//
//                                        }
//                                    }
//                                });
//
//                                RightButton.addClickHandler(new ClickHandler() {
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter <5)       //Change to your widget count minus 1
//                                        {Widgetcounter++;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget"+(Widgetcounter-1);
//                                            String Widgettwo = "Widget"+(Widgetcounter);
//                                            String Widgetthree = "Widget"+(Widgetcounter+1);
//                                            if (Widgetone.equals("Widget1")||Widgettwo.equals("Widget1")||Widgetthree.equals("Widget1"))
//                                            {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2")|Widgettwo.equals("Widget2")||Widgetthree.equals("Widget2"))
//                                            {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3")|Widgettwo.equals("Widget3")||Widgetthree.equals("Widget3"))
//                                            {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4")|Widgettwo.equals("Widget4")||Widgetthree.equals("Widget4"))
//                                            {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5")|Widgettwo.equals("Widget5")||Widgetthree.equals("Widget5"))
//                                            {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6")|Widgettwo.equals("Widget6")||Widgetthree.equals("Widget6"))
//                                            {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//                                        }
//                                    }
//                                });
        extendedTextBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title1.removeStyleName("Normal");
                Title1.addStyleName("Decor");
                Title2.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("ExtendedTextBox"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });

        extendedSuggestBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title2.removeStyleName("Normal");
                Title2.addStyleName("Decor");
                Title1.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("ExtendedSuggestBox"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        return contentPanel;
    }

    public Widget PopUp()
    {
        Menu.clear();
        contentPanel.clear();
        Holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        contentPanel.add(widgetList);
        contentPanel.add(Menu);      //Re adds the Menu
        contentPanel.add(FPAgain);
        Image PointerButton = new Image("src/main/Images/Pointer.png");

        FPnew.clear();
        PointerButton.getElement().getStyle().setZIndex(25);
        WidgetSelector.getElement().getStyle().setZIndex(20);
        FPnew.add(PointerButton,330,0);
        FPnew.add(WidgetSelector,0,0);
        FPAgain.add(FPnew);

        Image FloatingWindow = new Image("src/main/Images/Floating.jpg");
        Title1 = new Label ("Floating Window");
        Title1.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title1.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget1 = new FlowPanel();
        Widget1.add(FloatingWindow);
        Widget1.add(Title1);
        Widget1.setSize("170px","90px");
        Widget1.setStyleName("Align");
        Widget1.getElement().getStyle().setMarginTop(5, Unit.PX);


        Image MessageDialog = new Image("src/main/Images/MessageDialog.jpg");
        Title2 = new Label ("Message Dialog");
        Title2.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title2.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget2 = new FlowPanel();
        Widget2.add(MessageDialog);
        Widget2.add(Title2);
        Widget2.setSize("170px","90px");
        Widget2.setStyleName("Align");
        Widget2.getElement().getStyle().setMarginTop(5, Unit.PX);


        Image Calendar = new Image("src/main/Images/Calendar.jpg");
        Title3 = new Label ("Calendar");
        Title3.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title3.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget3 = new FlowPanel();
        Widget3.add(Calendar);
        Widget3.add(Title3);
        Widget3.setSize("170px","90px");
        Widget3.setStyleName("Align");
        Widget3.getElement().getStyle().setMarginTop(5, Unit.PX);
        Holder.clear();
//                                Holder.add(LeftButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Holder.add(Widget1);
        Holder.add(Widget2);
        Holder.add(Widget3);

        Title1.addStyleName("Normal");
        Title2.addStyleName("Normal");
        Title3.addStyleName("Normal");
//                                Holder.add(RightButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Menu.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        Menu.add(Holder);
        contentPanel.add(FPAgain);

//                                 Uncomment When you have more than Three Widgets in the Menu Panel
//                                LeftButton.addClickHandler(new ClickHandler() {
//
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter > 2) {
//                                            Widgetcounter--;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget" + (Widgetcounter - 1);
//                                            String Widgettwo = "Widget" + (Widgetcounter);
//                                            String Widgetthree = "Widget" + (Widgetcounter + 1);
//                                            if (Widgetone.equals("Widget1") || Widgettwo.equals("Widget1") || Widgetthree.equals("Widget1")) {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2") | Widgettwo.equals("Widget2") || Widgetthree.equals("Widget2")) {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3") | Widgettwo.equals("Widget3") || Widgetthree.equals("Widget3")) {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4") | Widgettwo.equals("Widget4") || Widgetthree.equals("Widget4")) {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5") | Widgettwo.equals("Widget5") || Widgetthree.equals("Widget5")) {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6") | Widgettwo.equals("Widget6") || Widgetthree.equals("Widget6")) {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//
//                                        }
//                                    }
//                                });
//
//                                RightButton.addClickHandler(new ClickHandler() {
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter <5)       //Change to your widget count minus 1
//                                        {Widgetcounter++;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget"+(Widgetcounter-1);
//                                            String Widgettwo = "Widget"+(Widgetcounter);
//                                            String Widgetthree = "Widget"+(Widgetcounter+1);
//                                            if (Widgetone.equals("Widget1")||Widgettwo.equals("Widget1")||Widgetthree.equals("Widget1"))
//                                            {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2")|Widgettwo.equals("Widget2")||Widgetthree.equals("Widget2"))
//                                            {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3")|Widgettwo.equals("Widget3")||Widgetthree.equals("Widget3"))
//                                            {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4")|Widgettwo.equals("Widget4")||Widgetthree.equals("Widget4"))
//                                            {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5")|Widgettwo.equals("Widget5")||Widgetthree.equals("Widget5"))
//                                            {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6")|Widgettwo.equals("Widget6")||Widgetthree.equals("Widget6"))
//                                            {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//                                        }
//                                    }
//                                });
        FloatingWindow.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title1.removeStyleName("Normal");
                Title1.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title3.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("FloatingWindow"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        Calendar.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title3.removeStyleName("Normal");
                Title3.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title1.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("Calendar"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        MessageDialog.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title2.removeStyleName("Normal");
                Title2.addStyleName("Decor");
                Title1.addStyleName("Normal");
                Title3.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("MessageDialog"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        return contentPanel;
    }
    public Widget Misc()
    {
        Menu.clear();
        contentPanel.clear();
        Holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        contentPanel.add(widgetList);
        contentPanel.add(Menu);      //Re adds the Menu
        contentPanel.add(FPAgain);
        Image PointerButton = new Image("src/main/Images/Pointer.png");

        FPnew.clear();
        PointerButton.getElement().getStyle().setZIndex(25);
        WidgetSelector.getElement().getStyle().setZIndex(20);
        FPnew.add(PointerButton,522,0);
        FPnew.add(WidgetSelector,0,0);
        FPAgain.add(FPnew);

        Image Breadcrumb = new Image("src/main/Images/Breadcrumb.jpg");
        Title1 = new Label ("Bread Crumb");
        Title1.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title1.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget1 = new FlowPanel();
        Widget1.add(Breadcrumb);
        Widget1.add(Title1);
        Widget1.setSize("170px","90px");
        Widget1.setStyleName("Align");
        Widget1.getElement().getStyle().setMarginTop(5, Unit.PX);

        Image MessagePanel = new Image("src/main/Images/MessagePanel.jpg");
        Title2 = new Label ("Message Panel");
        Title2.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title2.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget2 = new FlowPanel();
        Widget2.add(MessagePanel);
        Widget2.add(Title2);
        Widget2.setSize("170px","90px");
        Widget2.setStyleName("Align");
        Widget2.getElement().getStyle().setMarginTop(5, Unit.PX);

        Image TitleWindow = new Image("src/main/Images/TitleWindow.jpg");
        Title3 = new Label ("Title Window");
        Title3.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title3.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget3 = new FlowPanel();
        Widget3.add(TitleWindow);
        Widget3.add(Title3);
        Widget3.setSize("170px","90px");
        Widget3.setStyleName("Align");
        Widget3.getElement().getStyle().setMarginTop(5, Unit.PX);
        Holder.clear();
//                                Holder.add(LeftButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Holder.add(Widget1);
        Holder.add(Widget2);
        Holder.add(Widget3);

        Title1.addStyleName("Normal");
        Title2.addStyleName("Normal");
        Title3.addStyleName("Normal");

//                                Holder.add(RightButton);  Uncomment When you have more than Three Widgets in the Menu Panel
        Menu.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        Menu.add(Holder);
        contentPanel.add(FPAgain);

//                                 Uncomment When you have more than Three Widgets in the Menu Panel
//                                LeftButton.addClickHandler(new ClickHandler() {
//
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter > 2) {
//                                            Widgetcounter--;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget" + (Widgetcounter - 1);
//                                            String Widgettwo = "Widget" + (Widgetcounter);
//                                            String Widgetthree = "Widget" + (Widgetcounter + 1);
//                                            if (Widgetone.equals("Widget1") || Widgettwo.equals("Widget1") || Widgetthree.equals("Widget1")) {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2") | Widgettwo.equals("Widget2") || Widgetthree.equals("Widget2")) {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3") | Widgettwo.equals("Widget3") || Widgetthree.equals("Widget3")) {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4") | Widgettwo.equals("Widget4") || Widgetthree.equals("Widget4")) {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5") | Widgettwo.equals("Widget5") || Widgetthree.equals("Widget5")) {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6") | Widgettwo.equals("Widget6") || Widgetthree.equals("Widget6")) {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//
//                                        }
//                                    }
//                                });
//
//                                RightButton.addClickHandler(new ClickHandler() {
//                                    @Override
//                                    public void onClick(ClickEvent event) {
//
//                                        if (Widgetcounter <5)       //Change to your widget count minus 1
//                                        {Widgetcounter++;
//                                            Holder.clear();
//                                            Holder.add(LeftButton);
//                                            String Widgetone = "Widget"+(Widgetcounter-1);
//                                            String Widgettwo = "Widget"+(Widgetcounter);
//                                            String Widgetthree = "Widget"+(Widgetcounter+1);
//                                            if (Widgetone.equals("Widget1")||Widgettwo.equals("Widget1")||Widgetthree.equals("Widget1"))
//                                            {
//                                                Holder.add(Widget1);
//                                            }
//                                            if (Widgetone.equals("Widget2")|Widgettwo.equals("Widget2")||Widgetthree.equals("Widget2"))
//                                            {
//                                                Holder.add(Widget2);
//                                            }
//                                            if (Widgetone.equals("Widget3")|Widgettwo.equals("Widget3")||Widgetthree.equals("Widget3"))
//                                            {
//                                                Holder.add(Widget3);
//                                            }
//                                            if (Widgetone.equals("Widget4")|Widgettwo.equals("Widget4")||Widgetthree.equals("Widget4"))
//                                            {
//                                                Holder.add(Widget4);
//                                            }
//                                            if (Widgetone.equals("Widget5")|Widgettwo.equals("Widget5")||Widgetthree.equals("Widget5"))
//                                            {
//                                                Holder.add(Widget5);
//                                            }
//                                            if (Widgetone.equals("Widget6")|Widgettwo.equals("Widget6")||Widgetthree.equals("Widget6"))
//                                            {
//                                                Holder.add(Widget6);
//                                            }
//                                            Holder.add(RightButton);
//                                        }
//                                    }
//                                });
        Breadcrumb.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title1.removeStyleName("Normal");
                Title1.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title3.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("Breadcrumb"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        TitleWindow.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title3.removeStyleName("Normal");
                Title3.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title1.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("TitleWindow"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        MessagePanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title2.removeStyleName("Normal");
                Title2.addStyleName("Decor");
                Title1.addStyleName("Normal");
                Title3.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("MessagePanel"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        return contentPanel;
    }

    public Widget Beta()
    {
        Menu.clear();
        contentPanel.clear();
        Holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        contentPanel.add(widgetList);
        contentPanel.add(Menu);      //Re adds the Menu
        contentPanel.add(FPAgain);
        Image PointerButton = new Image("src/main/Images/Pointer.png");
        FPnew.clear();
        PointerButton.getElement().getStyle().setZIndex(25);
        WidgetSelector.getElement().getStyle().setZIndex(20);
        FPnew.add(PointerButton,619,0);
        FPnew.add(WidgetSelector,0,0);
        FPAgain.add(FPnew);

        final Image Slide = new Image("src/main/Images/Slide.jpg");    //Building an image for the Menu
        Title2 = new Label ("Slide Bar");
        Title2.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title2.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget1 = new FlowPanel();
        Widget1.add(Slide);
        Widget1.add(Title2);
        Widget1.setSize("170px", "90px");
        Widget1.setStyleName("Align");
        Widget1.getElement().getStyle().setMarginTop(5, Unit.PX);

        Holder.clear();
        Holder.add(Widget1);
        Menu.add(Holder);
        Title2.addStyleName("Normal");


        contentPanel.add(widgetList);
        contentPanel.add(Menu);
        contentPanel.add(FPAgain);




        Slide.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title2.removeStyleName("Normal");
                Title2.addStyleName("Decor");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("SlideBar"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        return contentPanel;


    }

    public Widget Toggle()
    {

        Menu.clear();
        Holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        Widgetcounter = 3;
        contentPanel.clear();
        contentPanel.add(widgetList);
        contentPanel.add(Menu);      //Re adds the Menu
        contentPanel.add(FPAgain);
        Image PointerButton = new Image("src/main/Images/Pointer.png");

        FPnew.clear();
        PointerButton.getElement().getStyle().setZIndex(25);
        WidgetSelector.getElement().getStyle().setZIndex(20);

        FPnew.add(PointerButton,230,0);
        FPnew.add(WidgetSelector,0,0);
        FPAgain.add(FPnew);

        final Image CollapsePanel = new Image("src/main/Images/Collapse.jpg");    //Building an image for the Menu
        Title2 = new Label ("Collapse Panel");
        Title2.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title2.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget2 = new FlowPanel();
        Widget2.add(CollapsePanel);
        Widget2.add(Title2);
        Widget2.setSize("170px","90px");
        Widget2.setStyleName("Align");
        Widget2.getElement().getStyle().setMarginTop(5, Unit.PX);

        final Image ToggleRail = new Image("src/main/Images/ToggleRail.jpg");    //Building an image for the Menu
        Title5 = new Label ("Toggle Rail");
        Title5.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title5.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget5 = new FlowPanel();
        Widget5.add(ToggleRail);
        Widget5.add(Title5);
        Widget5.setSize("170px","90px");
        Widget5.setStyleName("Align");
        Widget5.getElement().getStyle().setMarginTop(5, Unit.PX);

        final Image ToggleButton = new Image("src/main/Images/Togglebutton.jpg");    //Building an image for the Menu
        Title4 = new Label ("Toggle Button");
        Title4.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title4.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget4 = new FlowPanel();
        Widget4.add(ToggleButton);
        Widget4.add(Title4);
        Widget4.setSize("170px","90px");
        Widget4.setStyleName("Align");
        Widget4.getElement().getStyle().setMarginTop(5, Unit.PX);

        final Image Drop = new Image("src/main/Images/Drop.jpg");    //Building an image for the Menu
        Title1 = new Label ("Drop Down");
        Title1.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title1.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget1 = new FlowPanel();
        Widget1.add(Drop);
        Widget1.add(Title1);
        Widget1.setSize("170px","90px");
        Widget1.setStyleName("Align");
        Widget1.getElement().getStyle().setMarginTop(5, Unit.PX);

        final Image ToggleMini = new Image("src/main/Images/Togglemini.jpg");    //Building an image for the Menu
        Title3 = new Label ("Toggle Mini");
        Title3.getElement().getStyle().setMarginBottom(5, Unit.PX);
        Title3.getElement().getStyle().setMarginTop(5, Unit.PX);
        final FlowPanel Widget3 = new FlowPanel();
        Widget3.add(ToggleMini);
        Widget3.add(Title3);
        Widget3.setSize("170px","90px");
        Widget3.setStyleName("Align");
        Widget3.getElement().getStyle().setMarginTop(5, Unit.PX);

        Title1.removeStyleName("Decor");
        Title2.removeStyleName("Decor");
        Title3.removeStyleName("Decor");
        Title1.addStyleName("Normal");
        Title2.addStyleName("Normal");
        Title3.addStyleName("Normal");

        Holder.clear();
        Holder.add(LeftButton);
        Holder.add(Widget1);
        Holder.add(Widget2);
        Holder.add(Widget3);
        Holder.add(RightButton);
        Menu.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        Menu.add(Holder);
        contentPanel.add(FPAgain);

        LeftButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                if (Widgetcounter > 2) {

                    Widgetcounter--;
                    Holder.clear();
                    Holder.add(LeftButton);
                    String Widgetone = "Widget" + (Widgetcounter - 1);
                    String Widgettwo = "Widget" + (Widgetcounter);
                    String Widgetthree = "Widget" + (Widgetcounter + 1);
                    if (Widgetone.equals("Widget1") || Widgettwo.equals("Widget1") || Widgetthree.equals("Widget1")) {
                        Holder.add(Widget1);
                    }
                    if (Widgetone.equals("Widget2") | Widgettwo.equals("Widget2") || Widgetthree.equals("Widget2")) {
                        Holder.add(Widget2);
                    }
                    if (Widgetone.equals("Widget3") | Widgettwo.equals("Widget3") || Widgetthree.equals("Widget3")) {
                        Holder.add(Widget3);
                    }
                    if (Widgetone.equals("Widget4") | Widgettwo.equals("Widget4") || Widgetthree.equals("Widget4")) {

                        Holder.add(Widget4);
                    }
                    if (Widgetone.equals("Widget5") | Widgettwo.equals("Widget5") || Widgetthree.equals("Widget5")) {

                        Holder.add(Widget5);
                    }
//                    if (Widgetone.equals("Widget6") | Widgettwo.equals("Widget6") || Widgetthree.equals("Widget6")) {
//                        Holder.add(Widget6);
//                    }
                    Holder.add(RightButton);

                }
            }
        });

        RightButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if (Widgetcounter <4)
                {
                    Title4.removeStyleName("Decor");
                    Title5.removeStyleName("Decor");
                    Title4.addStyleName("Normal");
                    Title5.addStyleName("Normal");
                    Widgetcounter++;
                    Holder.clear();
                    Holder.add(LeftButton);
                    String Widgetone = "Widget"+(Widgetcounter-1);
                    String Widgettwo = "Widget"+(Widgetcounter);
                    String Widgetthree = "Widget"+(Widgetcounter+1);
                    if (Widgetone.equals("Widget1")||Widgettwo.equals("Widget1")||Widgetthree.equals("Widget1"))
                    {
                        Holder.add(Widget1);
                    }
                    if (Widgetone.equals("Widget2")|Widgettwo.equals("Widget2")||Widgetthree.equals("Widget2"))
                    {
                        Holder.add(Widget2);
                    }
                    if (Widgetone.equals("Widget3")|Widgettwo.equals("Widget3")||Widgetthree.equals("Widget3"))
                    {
                        Holder.add(Widget3);
                    }
                    if (Widgetone.equals("Widget4")|Widgettwo.equals("Widget4")||Widgetthree.equals("Widget4"))
                    {
                        Holder.add(Widget4);
                    }
                    if (Widgetone.equals("Widget5")|Widgettwo.equals("Widget5")||Widgetthree.equals("Widget5"))
                    {
                        Holder.add(Widget5);
                    }
//                    if (Widgetone.equals("Widget6")|Widgettwo.equals("Widget6")||Widgetthree.equals("Widget6"))
//                    {
//                        Holder.add(Widget6);
//                    }
                    Holder.add(RightButton);
                }
            }
        });
        CollapsePanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title2.removeStyleName("Normal");
                Title2.addStyleName("Decor");
                Title1.addStyleName("Normal");
                Title3.addStyleName("Normal");
                Title4.addStyleName("Normal");
                Title5.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("CollapsePanel"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        Drop.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title1.removeStyleName("Normal");
                Title1.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title3.addStyleName("Normal");
                Title4.addStyleName("Normal");
                Title5.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("DropDownMenu"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });

        ToggleMini.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                Title3.removeStyleName("Normal");
                Title3.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title1.addStyleName("Normal");
                Title4.addStyleName("Normal");
                Title5.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("ToggleButtonMiniOnOff"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        ToggleButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
//                Title4.removeStyleName("Normal");
//                Title4.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title1.addStyleName("Normal");
                Title3.addStyleName("Normal");
                Title5.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("ToggleButtonOnOff"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        ToggleRail.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
//                Title5.removeStyleName("Normal");
//                Title5.addStyleName("Decor");
                Title2.addStyleName("Normal");
                Title3.addStyleName("Normal");
                Title4.addStyleName("Normal");
                Title1.addStyleName("Normal");
                FP.clear();
                FP.add(Spacer);
                FP.add(createWidget("ToggleRail"));
                contentPanel.remove(FP);
                contentPanel.add(FP);
                colorCode();
            }
        });
        return contentPanel;
    }

    public Widget Links()
    {   contentPanel.clear();
        LinksHolder.clear();
        contentPanel.add(widgetList);
        contentPanel.add(LinksHolder);
        Holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        Holder2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        Holder2.clear();
        Holder.clear();
        Holder2.setSize("680px","100px");
        Holder.setSize("680px","100px");

        //To Create a Widget for the Menu
        Image GWTShowcase = new Image("src/main/Images/GWTShowcase.jpg");   //Adds the Image you want to use  (Size W 170 H 75)
        Hyperlink link0 = new Hyperlink("GWT Showcase", "http://gwt.googleusercontent.com/samples/Showcase/Showcase.html#!CwCheckBox");
        link0.getElement().getStyle().setMarginBottom(5, Unit.PX); //Sets the Bottom Margin
        link0.getElement().getStyle().setMarginTop(5, Unit.PX);      //Sets the Top Margin
        final FlowPanel Widget1 = new FlowPanel();                    //Creates our Panel (Widget + Number needed for Loop)
        Widget1.add(GWTShowcase);                                     //Adds the Image to the Panel
        Widget1.add(link0);                                        //Adds the Title to the Panel
        Widget1.setSize("170px","90px");                           //Sets the size of the panel (Width, Height)
        Widget1.setStyleName("Align");                            //Calls the CSS
        Widget1.getElement().getStyle().setMargin(5, Unit.PX);   //Adds the margin for the panel on all sides.
        Widget1.getElement().getStyle().setMarginLeft(15, Unit.PX);

        Image W3School = new Image("src/main/Images/W3School.jpg");   //Adds the Image you want to use  (Size W 170 H 75)
        Hyperlink link1 = new Hyperlink("W3 Schools (CSS)","http://www.w3schools.com/css/ " );
        link1.getElement().getStyle().setMarginBottom(5, Unit.PX); //Sets the Bottom Margin
        link1.getElement().getStyle().setMarginTop(5, Unit.PX);      //Sets the Top Margin
        final FlowPanel Widget2 = new FlowPanel();                    //Creates our Panel (Widget + Number needed for Loop)
        Widget2.add(W3School);                                     //Adds the Image to the Panel
        Widget2.add(link1);                                        //Adds the Title to the Panel
        Widget2.setSize("170px","90px");                           //Sets the size of the panel (Width, Height)
        Widget2.setStyleName("Align");                            //Calls the CSS
        Widget2.getElement().getStyle().setMargin(5, Unit.PX);   //Adds the margin for the panel on all sides.

        Image DEFT = new Image("src/main/Images/DEFT.jpg");   //Adds the Image you want to use  (Size W 170 H 75)
        Hyperlink link2 = new Hyperlink("DEFT UI Community Of Practice","http://confluence-oss.lmera.ericsson.se/display/DEFTO/DEFT+UI+Community+Of+Practice");
        link2.getElement().getStyle().setMarginBottom(5, Unit.PX); //Sets the Bottom Margin
        link2.getElement().getStyle().setMarginTop(5, Unit.PX);      //Sets the Top Margin
        final FlowPanel Widget3 = new FlowPanel();                    //Creates our Panel (Widget + Number needed for Loop)
        Widget3.add(DEFT);                                     //Adds the Image to the Panel
        Widget3.add(link2);                                        //Adds the Title to the Panel
        Widget3.setSize("170px","90px");                           //Sets the size of the panel (Width, Height)
        Widget3.setStyleName("Align");                            //Calls the CSS
        Widget3.getElement().getStyle().setMargin(5, Unit.PX);   //Adds the margin for the panel on all sides.

        Image OpenLayers = new Image("src/main/Images/OpenLayers.jpg");   //Adds the Image you want to use  (Size W 170 H 75)
        Hyperlink link3 = new Hyperlink("OpenLayers Showcase","http://demo.gwt-openlayers.org/gwt_ol_showcase/GwtOpenLayersShowcase.html");
        link3.getElement().getStyle().setMarginBottom(5, Unit.PX); //Sets the Bottom Margin
        link3.getElement().getStyle().setMarginTop(5, Unit.PX);      //Sets the Top Margin
        final FlowPanel Widget4 = new FlowPanel();                    //Creates our Panel (Widget + Number needed for Loop)
        Widget4.add(OpenLayers);                                     //Adds the Image to the Panel
        Widget4.add(link3);                                        //Adds the Title to the Panel
        Widget4.setSize("170px","90px");                           //Sets the size of the panel (Width, Height)
        Widget4.setStyleName("Align");                            //Calls the CSS
        Widget4.getElement().getStyle().setMargin(5, Unit.PX);   //Adds the margin for the panel on all sides.

        Image Stack = new Image("src/main/Images/Stack.jpg");   //Adds the Image you want to use  (Size W 170 H 75)
        Hyperlink link4 = new Hyperlink("Stack OverFlow","http://stackoverflow.com/");
        link4.getElement().getStyle().setMarginBottom(5, Unit.PX); //Sets the Bottom Margin
        link4.getElement().getStyle().setMarginTop(5, Unit.PX);      //Sets the Top Margin
        final FlowPanel Widget5 = new FlowPanel();                    //Creates our Panel (Widget + Number needed for Loop)
        Widget5.add(Stack);                                     //Adds the Image to the Panel
        Widget5.add(link4);                                        //Adds the Title to the Panel
        Widget5.setSize("170px","90px");                           //Sets the size of the panel (Width, Height)
        Widget5.setStyleName("Align");                            //Calls the CSS
        Widget5.getElement().getStyle().setMargin(5, Unit.PX);   //Adds the margin for the panel on all sides.

        Image ENIQEVENTS = new Image("src/main/Images/ENIQEVENTSUI.jpg");   //Adds the Image you want to use  (Size W 170 H 75)
        Hyperlink link5 = new Hyperlink("ENIQ Events UI","http://confluence-oss.lmera.ericsson.se/pages/viewpage.action?pageId=21102633");
        link5.getElement().getStyle().setMarginBottom(5, Unit.PX); //Sets the Bottom Margin
        link5.getElement().getStyle().setMarginTop(5, Unit.PX);      //Sets the Top Margin
        final FlowPanel Widget6 = new FlowPanel();                    //Creates our Panel (Widget + Number needed for Loop)
        Widget6.add(ENIQEVENTS);                                     //Adds the Image to the Panel
        Widget6.add(link5);                                        //Adds the Title to the Panel
        Widget6.setSize("170px","90px");                           //Sets the size of the panel (Width, Height)
        Widget6.setStyleName("Align");                            //Calls the CSS
        Widget6.getElement().getStyle().setMargin(5, Unit.PX);   //Adds the margin for the panel on all sides.

        Holder.add(Widget1);
        Holder.add(Widget2);
        Holder.add(Widget3);
        Holder2.add(Widget4);
        Holder2.add(Widget5);
        Holder2.add(Widget6);

        LinksHolder.add(Holder);
        LinksHolder.add(Holder2);


        return contentPanel;
    }

    public Widget BuildWidgetSelector()
    {
        WidgetSelector.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        WidgetSelector.addStyleName("LabelBar");
        WidgetSelector.setWidth("680px");
        WidgetSelector.setHeight("25px");
        ButtonsLabel.addStyleName("Normal");
        MapLabel.addStyleName("Normal");
        ToggleLabel.addStyleName("Normal");
        PopUpLabel.addStyleName("Normal");
        TextLabel.addStyleName("Normal");
        MiscLabel.addStyleName("Normal");
        BetaLabel.addStyleName("Normal");
        WidgetSelector.add(ButtonsLabel);                    //If you need to add more toggle Buttons add them here
        WidgetSelector.add(MapLabel);
        WidgetSelector.add(ToggleLabel);
        WidgetSelector.add(PopUpLabel);
        WidgetSelector.add(TextLabel);
        WidgetSelector.add(MiscLabel);
        WidgetSelector.add(BetaLabel);
        ButtonsLabel.setWidth("50px");
        MapLabel.setWidth("50px");
        ToggleLabel.setWidth("50px");
        PopUpLabel.setWidth("50px");
        TextLabel.setWidth("50px");
        MiscLabel.setWidth("50px");
        BetaLabel.setWidth("50px");
        return WidgetSelector;
    }
}