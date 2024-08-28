package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.common.client.CommonMain;
import com.ericsson.eniq.events.common.client.json.IJSONArray;
import com.ericsson.eniq.events.common.client.json.IJSONObject;
import com.ericsson.eniq.events.common.client.json.JsonDataParserUtils;
import com.ericsson.eniq.events.common.client.json.JsonObjectWrapper;
import com.ericsson.eniq.events.widgets.client.Widgets;
import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanelResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 01/05/13
 * Time: 16:21
 * Calls the METAdata
 */
public class Holder{

    public HorizontalPanel holdingPanel = new HorizontalPanel();
    private static final WidgetShowcaseResourceBundle resources = GWT.create(WidgetShowcaseResourceBundle.class);
    AbsolutePanel widgetDetailsPanel = new AbsolutePanel();
    Widgets widgetshowcase = new Widgets();
    CallsSnipet callSnip = new CallsSnipet();
    ArrayList<String> widgetNames = new ArrayList<String>();
    ArrayList<String> widgetImages = new ArrayList<String>();
    final CollapsePanelResourceBundle bundle = GWT.create(CollapsePanelResourceBundle.class);
    int widgetCounter;
    FlowPanel linkPanel = new FlowPanel();


    public HorizontalPanel getWidgets(String Name) {
        holdingPanel.setSize("780px","130px");
        holdingPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        holdingPanel.clear();
        /** Get development configuration or production. Only ever even look for the dev configuration if we are in hosted mode **/
        String configData = "";
        if (GWT.isProdMode()) {
            configData = resources.configData().getText();
        } else {
            configData = CommonMain.isDevConfigOn() ? resources.configData().getText() : resources.configData()
                    .getText();
        }
        JsonObjectWrapper metaData = new JsonObjectWrapper(JsonDataParserUtils.parse(configData).isObject());
        createWidget(metaData, Name);
        return holdingPanel;
    }

    public FlowPanel getLinks(String Name) {
        linkPanel.clear();
        /** Get development configuration or production. Only ever even look for the dev configuration if we are in hosted mode **/
        String configData = "";
        if (GWT.isProdMode()) {
            configData = resources.configData().getText();
        } else {
            configData = CommonMain.isDevConfigOn() ? resources.configData().getText() : resources.configData()
                    .getText();
        }
        JsonObjectWrapper metaData = new JsonObjectWrapper(JsonDataParserUtils.parse(configData).isObject());
        createLinks(metaData, Name);
        return linkPanel;
    }

    public void createWidget(JsonObjectWrapper metaData, String groupName) {
        final IJSONArray widgetList = metaData.getArray(groupName);
        widgetCounter=setWidgetCounter(2);
        final Image rightButton = new Image(bundle.arrow_across());
        rightButton.getElement().getStyle().setMarginTop(50, Style.Unit.PX);
        final Image leftButton = new Image("Images/Previousnew.jpg");
        leftButton.getElement().getStyle().setMarginTop(50, Style.Unit.PX);
        if (widgetList.size()<=3){
        for (int i = 0; i < widgetList.size(); i++) {

            final IJSONObject widgetData = widgetList.get(i);
            String imageName = widgetData.getString("image");
            String titleName = widgetData.getString("title");
            final String name = widgetData.getString("name");
            Image image = new Image("Images/"+imageName);
            final Label title = new Label (titleName);
            title.addStyleName("Normal");
            title.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
            title.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
            final FlowPanel widget = new FlowPanel();
            widget.add(image);
            widget.add(title);
            widget.setSize("170px", "90px");
            widget.setStyleName("Align");
            widget.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
            holdingPanel.add(widget);
            widgetNames.add(name);
            widgetImages.add(image.toString());
            image.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent event) {
                    widgetDetailsPanel.clear();
                    widgetDetailsPanel.add(callSnip.callClass(name));
                    colorCode();
                }
            });
            }
        }

        else{
            holdingPanel.add(leftButton);
            for (int i = (0); i < 3; i++) {
                final IJSONObject widgetData = widgetList.get(i);
                String imageName = widgetData.getString("image");
                String titleName = widgetData.getString("title");
                final String name = widgetData.getString("name");
                Image image = new Image("Images/"+imageName);
                final Label title = new Label (titleName);
                title.addStyleName("Normal");
                title.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
                title.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
                final FlowPanel widget = new FlowPanel();
                widget.add(image);
                widget.add(title);
                widget.setSize("170px", "90px");
                widget.setStyleName("Align");
                widget.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
                holdingPanel.add(widget);
                widgetNames.add(name);
                widgetImages.add(image.toString());
                image.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(final ClickEvent event) {
                        widgetDetailsPanel.clear();
                        widgetDetailsPanel.add(callSnip.callClass(name));
                        colorCode();
                    }
                });
            }

            holdingPanel.add(rightButton);

            leftButton.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            if (widgetCounter >2){
                            widgetCounter--;
                            holdingPanel.clear();
                                holdingPanel.add(leftButton);
                                for (int i = widgetCounter-2; i < widgetCounter+1; i++) {
                                    final IJSONObject widgetData = widgetList.get(i);
                                    String imageName = widgetData.getString("image");
                                    String titleName = widgetData.getString("title");
                                    final String name = widgetData.getString("name");
                                    Image image = new Image("Images/"+imageName);
                                    final Label title = new Label (titleName);
                                    title.addStyleName("Normal");
                                    title.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
                                    title.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
                                    final FlowPanel widget = new FlowPanel();
                                    widget.add(image);
                                    widget.add(title);
                                    widget.setSize("170px", "90px");
                                    widget.setStyleName("Align");
                                    widget.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
                                    holdingPanel.add(widget);
                                    widgetNames.add(name);
                                    widgetImages.add(image.toString());
                                    image.addClickHandler(new ClickHandler() {
                                        @Override
                                        public void onClick(final ClickEvent event) {
                                            widgetDetailsPanel.clear();
                                            widgetDetailsPanel.add(callSnip.callClass(name));
                                            colorCode();
                                        }
                                    });
                                }
                                holdingPanel.add(rightButton);
                                setWidgetCounter(widgetCounter);
                            }
                }
            });


            rightButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (widgetCounter < widgetList.size()-1){
                        widgetCounter++;
                        holdingPanel.clear();
                        leftButton.getElement().getStyle().setMarginTop(45, Style.Unit.PX);
                        holdingPanel.add(leftButton);
                        for (int i = widgetCounter-2; i <widgetCounter+1; i++) {
                            final IJSONObject widgetData = widgetList.get(i);
                            String imageName = widgetData.getString("image");
                            String titleName = widgetData.getString("title");
                            final String name = widgetData.getString("name");
                            Image image = new Image("Images/"+imageName);
                            final Label title = new Label (titleName);
                            title.addStyleName("Normal");
                            title.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
                            title.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
                            final FlowPanel widget = new FlowPanel();
                            widget.add(image);
                            widget.add(title);
                            widget.setSize("170px", "90px");
                            widget.setStyleName("Align");
                            widget.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
                            holdingPanel.add(widget);
                            widgetNames.add(name);
                            widgetImages.add(image.toString());
                            image.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(final ClickEvent event) {
                                    widgetDetailsPanel.clear();
                                    widgetDetailsPanel.add(callSnip.callClass(name));
                                    colorCode();
                                }
                            });
                        }
                        holdingPanel.add(rightButton);
                        setWidgetCounter(widgetCounter);
                    }
                }
            });

        }
        }

    public int setWidgetCounter(int setter){
        this.widgetCounter=setter;
        return widgetCounter;
    }

    private native void colorCode() /*-{
        $wnd.prettyPrint();
    }-*/;

    public AbsolutePanel widgetActions(){
        return widgetDetailsPanel;
    }

    public void createLinks(JsonObjectWrapper metaData, String groupName){
        final IJSONArray linkList = metaData.getArray(groupName);
        linkPanel.setWidth("780px");

            for (int i = 0; i < linkList.size(); i++) {
                final IJSONObject linkData = linkList.get(i);
                String imageName = linkData.getString("image");
                String titleName = linkData.getString("title");
                final String name = linkData.getString("name");
                String link = linkData.getString("address");
                Image image = new Image("Images/"+imageName);
                HTML HTMLLink = new HTML("<a href=\""+link+"\">"+titleName+"</a>");
                HTMLLink.addStyleName("Normal");
                HTMLLink.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
                HTMLLink.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
                final FlowPanel widget = new FlowPanel();
                widget.add(image);
                widget.add(HTMLLink);
                widget.setSize("200px", "120px");
                widget.setStyleName("Align");
                widget.getElement().getStyle().setMargin(15, Style.Unit.PX);
                widget.getElement().getStyle().setPaddingLeft(25, Style.Unit.PX);
                widget.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
                linkPanel.add(widget);
            }
        }

}