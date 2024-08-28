package com.ericsson.eniq.events.widgets.client.slider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class SlideGroup extends Composite {
    private static final String PERCENTAGE_PATTERN = "#0.##";

    @UiField(provided = true)
    static SlideBarResourceBundle resources = GWT.create(SlideBarResourceBundle.class);

    private static final int TEXTBOX_HEIGHT = 20;

    private static final int THUMB_HEIGHT = 20;

    public static final int COLOR_BAR_HEIGHT = 15;

    @UiField
    HTMLPanel container;

    @UiField
    FlowPanel slideGroup;

    @UiField
    TextBox selectedRangeBox;

    @UiField
    LabelElement labelElem;

    private final List<ColoredBar> bars = new ArrayList<ColoredBar>();

    private final List<SliderThumb> thumbs = new ArrayList<SliderThumb>();

    private final List<TextBox> textBoxes = new ArrayList<TextBox>();

    private final int availableWidth;

    SlideGroupUiBinder binder = GWT.create(SlideGroupUiBinder.class);

    public SlideGroup(final SlideBarDataType dataType) {
        resources.style().ensureInjected();
        initWidget(binder.createAndBindUi(this));
        setSize((dataType.getWidth() + 97) + "px", "71px");
        final List<ColoredBarDataType> coloredBarDataTypes = dataType.getBarDataTypes();
        availableWidth = dataType.getAvailableWidth();
        final int size = coloredBarDataTypes.size();
        final int minDistance = dataType.getMinDistance();
        labelElem.setInnerText("Selected " + dataType.getLabel() + " Percentage");
        for (int i = 0; i < size; i++) {
            final ColoredBarDataType barDataType = coloredBarDataTypes.get(i);
            final boolean lastBar = i == size - 1;
            final ColoredBar bar = addSlideBar(barDataType.getColor(), !lastBar, COLOR_BAR_HEIGHT,
                    barDataType.getWidth(), minDistance, dataType.getThumbWidth());
            if (i > 0) {
                thumbs.get(i - 1).setNextBar(bar);
                bars.get(i - 1).setMaxWidth(
                        coloredBarDataTypes.get(i - 1).getWidth() + barDataType.getWidth() - minDistance);
            }
        }
        slideGroup.setSize(dataType.getWidth() + "px", "57px");
        final FlowPanel textBoxPanel = new FlowPanel();
        for (final TextBox box : textBoxes) {
            textBoxPanel.add(box);
            box.setWidth("90px");
            box.getElement().getStyle().setMarginRight(5, Unit.PX);
            box.getElement().getStyle().setMarginTop(5, Unit.PX);
        }
        textBoxPanel.setHeight(TEXTBOX_HEIGHT + "px");
        textBoxPanel.getElement().getStyle().setProperty("clear", "both");
        container.add(textBoxPanel);
    }

    private ColoredBar addSlideBar(final String color, final boolean addThumb, final int height, final int width,
            final int minWidth, final int thumbWidth) {
        final ColoredBar bar = new ColoredBar();
        bar.getElement().getStyle().setBackgroundColor(color);
        bar.setHeight(height + "px");
        bar.setMinWidth(minWidth);
        bar.setStyleName(resources.style().coloredBar());
        bar.setBarWidth(width);
        bars.add(bar);
        slideGroup.add(bar);
        if (addThumb) {
            addThumb(bar, thumbWidth, color);
        }
        final TextBox textBox = new TextBox();

        textBoxes.add(textBox);
        return bar;
    }

    private SliderThumb addThumb(final IColoredBar bar, final int thumbWidth, final String color) {
        final SliderThumb thumb = new SliderThumb(bar);
        thumb.setHeight(THUMB_HEIGHT + "px");
        thumb.setThumbWidth(thumbWidth);
        thumb.getElement().getStyle().setBackgroundColor(color);
        thumb.setStyleName(resources.style().slidingThumb());
        slideGroup.add(thumb);
        thumbs.add(thumb);
        thumb.setUpdatedHandler(new UpdateHandler());
        return thumb;
    }

    private final class UpdateHandler implements IUpdateHandler {
        @Override
        public void onUpdate() {
            final int size = textBoxes.size();
            for (int i = 0; i < size; i++) {
                final TextBox textBox = textBoxes.get(i);
                final int width = bars.get(i).getWidth();
                textBox.setText(width + "px, " + converToPercentage(width));
            }

        }
    }

    private String converToPercentage(final double width) {
        final double d = (width / availableWidth) * 100.0;
        return NumberFormat.getFormat(PERCENTAGE_PATTERN).format(d) + "%";
    }

    interface SlideGroupUiBinder extends UiBinder<Widget, SlideGroup> {

    }
}
