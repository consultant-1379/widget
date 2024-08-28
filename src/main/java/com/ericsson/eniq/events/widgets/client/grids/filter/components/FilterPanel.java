package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterConfigs;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.CustomButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 * @param <C>
 * @param <T>
 */
public abstract class FilterPanel<C extends AbstractFilter<T>, T> extends Composite {

    public static final int CONFIG_BUTTON_WIDTH = 14;

    public static final int BORDER_WIDTH = 2;

    interface ImageTemplate extends SafeHtmlTemplates {
        @Template("<div class='{0}'></div><span class='{1}'>{2}</span>")
        SafeHtml label(String infoIconStyle, String labelStyle, String label);
    }

    interface Template extends SafeHtmlTemplates {
        @Template("<span>{0}</span>")
        SafeHtml label(String header);
    }

    private final FilterResourceBundle resourceBundle = GWT.create(FilterResourceBundle.class);

    private static final ImageTemplate IMAGE_TEMPLATE = GWT.create(ImageTemplate.class);

    private static final Template TEMPLATE = GWT.create(Template.class);

    protected final FlowPanel filterContainer = new FlowPanel();

    private Image configButton;

    private boolean cogEnabled;

    private TextBox filterBox;

    private final IFilterUpdate<C, T> filterUpdateHandler;

    protected final CustomButton filterAppliedButton = new CustomButton(true);

    private String lastFilter = "";

    private final EventBus eventBus;

    private final FilterDetailPopup hoverPopup;

    private FilterConfigs<C, T> configs;

    public FilterPanel(final IFilterUpdate<C, T> filterUpdateHandler, final EventBus eventBus) {
        this.eventBus = eventBus;
        this.filterUpdateHandler = filterUpdateHandler;
        resourceBundle.style().ensureInjected();
        filterAppliedButton.setHeight(20);
        filterAppliedButton.setVisible(false);
        initWidget(filterContainer);
        filterContainer.addStyleName(resourceBundle.style().container());
        filterContainer.setHeight(20 + "px");
        filterAppliedButton.addStyleName(resourceBundle.style().filterAppliedButton());
        hoverPopup = FilterDetailPopup.getSharedInstance();
        addHandlers();
        filterContainer.add(filterAppliedButton);
        init();
    }

    private void addHandlers() {
        filterAppliedButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                onFilterAppliedClick();
            }

        });
        filterAppliedButton.addCloseClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                setFilterAppliedButtonVisible(false);
                clearFilter();
            }
        });

        filterAppliedButton.addDomHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(final MouseOverEvent event) {
                showHoverWidget(event.getClientX(), event.getClientY() + 10);
            }
        }, MouseOverEvent.getType());

        filterAppliedButton.addDomHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(final MouseOutEvent event) {
                hoverPopup.hide();
            }
        }, MouseOutEvent.getType());
    }

    public FilterResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public FilterDetailPopup getHoverPopup() {
        return hoverPopup;
    }

    protected void init() {
        filterBox = new TextBox();
        filterBox.setStyleName(resourceBundle.style().filterBox());
        filterBox.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(final BlurEvent event) {
                onFilterApply();
            }
        });
        filterBox.addKeyDownHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(final KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onFilterApply();
                }
            }
        });
        filterContainer.add(filterBox);
        addConfigButton();
    }

    public TextBox getFilterBox() {
        return filterBox;
    }

    protected void addConfigButton() {
        configButton = new Image();
        configButton.setResource(resourceBundle.configureNormal());
        configButton.addStyleName(resourceBundle.style().configureImage());
        filterContainer.add(configButton);
        
        configButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                if (cogEnabled) {
                    onConfigurationClick();
                }
            }

        });
        
        enableCog();

    }

    protected void onConfigurationClick() {
        //handle configuration click
    }

    public void applyFilter(final List<FilterType> filterTypes, final List<T> filters, final FilterCategory category) {
        this.configs = createFilterConfigs(filterTypes, filters, category);
        filterUpdateHandler.onFilterUpdated(configs);
    }

    protected abstract FilterConfigs<C, T> createFilterConfigs(List<FilterType> filterTypes, List<T> filters,
            FilterCategory category);

    protected void onFilterAppliedClick() {
        // launch Presenter
    }

    public void addWidget(final Widget w) {
        filterContainer.add(w);
    }

    protected void clearFilterBox() {
        lastFilter = "";
        filterBox.setText("");
    }

    public void clearFilter() {
        clearFilterBox();
        setFilterAppliedButtonVisible(false);
        final List<T> filterList = new ArrayList<T>();
        final List<FilterType> typeList = new ArrayList<FilterType>();
        final FilterCategory filterType = getCategory();
        applyFilter(typeList, filterList, filterType);
    }

    public FilterCategory getCategory() {
        return FilterCategory.MATCH_ALL;
    }

    public abstract Comparator<T> getComparator();

    public EventBus getEventBus() {
        return eventBus;
    }

    public SafeHtml getImageTemplate(final FilterType filterType, final String filter) {
        return IMAGE_TEMPLATE.label(filterType.getStyle(), resourceBundle.style().appliedFilterLabel(), filter);
    }

    private void onFilterApply() {
        final String text = filterBox.getText().trim();
        if (text.equals(lastFilter)) {
            return;
        }
        lastFilter = text;
        //        configButton.addStyleName(resourceBundle.style().configureDisabled());
        final List<FilterType> typeList = new ArrayList<FilterType>();
        final List<T> filterList = new ArrayList<T>();
        if (!text.isEmpty()) {//clearing filter
            parseFilterText(text, filterList, typeList);
        }
        onFilterApply2(typeList, filterList, getCategory());
    }

    protected void onFilterApply2(final List<FilterType> typeList, final List<T> filterList,
            final FilterCategory category) {
        setFilterAppliedButtonVisible(!typeList.isEmpty());
        applyFilter(typeList, filterList, category);
        final int filterCount = filterList.size();
        final List<String> filters = new ArrayList<String>();
        final List<C> filtersList = getFilterConfigs().getFilters();
        String header = "";
        for (final C filter : filtersList) {
            filters.add(filter.toString());
        }
        if (filterCount == 1) {
            header = filterCount + " Filter Applied";
            filterAppliedButton.setHTML(getFilterHTML(String.valueOf(filterList.get(0)), typeList.get(0)));
        } else if (filterCount > 1) {
            header = filterCount + " Filters Applied";
            filterAppliedButton.setHTML(TEMPLATE.label(header));
        }
        hoverPopup.configure(header, filters);
    }

    public abstract void parseFilterText(final String text, final List<T> filters, final List<FilterType> types);

    protected void setFilterAppliedButtonVisible(final boolean visible) {
        filterAppliedButton.setVisible(visible);
        filterBox.setVisible(!visible);
    }

    public void setWidth(final int width) {
        final int boxWidth = Math.max(0, width - CONFIG_BUTTON_WIDTH - BORDER_WIDTH);
        filterBox.setWidth(boxWidth + "px");
        filterAppliedButton.setWidth(boxWidth);
    }

    public FilterConfigs<C, T> getFilterConfigs() {
        return configs;
    }

    private void showHoverWidget(int mouseXPosition, int mouseYPosition) {
        if (!hoverPopup.isShowing() && getFilterConfigs().getFilters().size() > 0) {         
            hoverPopup.bringToFront();
            hoverPopup.show();
            
            if( (mouseXPosition + hoverPopup.getClientWidth()) >= Window.getClientWidth()) {
                mouseXPosition -= hoverPopup.getClientWidth();
            }
            
            if( (mouseYPosition + hoverPopup.getClientHeight()) >= Window.getClientHeight()) {
                mouseYPosition -= hoverPopup.getClientHeight() + 20;
            }
                        
            hoverPopup.setPopupPosition(mouseXPosition + Window.getScrollLeft(), mouseYPosition + Window.getScrollTop());
        }
    }

    public SafeHtml getFilterHTML(final String filter, final FilterType filterType) {
        return TEMPLATE.label(filter);
    }

    protected void disableCog() {
        configButton.addStyleName(resourceBundle.style().configureDisabled());
        cogEnabled = false;
    }

    protected void enableCog() {
        configButton.removeStyleName(resourceBundle.style().configureDisabled());
        cogEnabled = true;
    }
}
