package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.eniq.events.common.client.mvp.BaseView;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog;
import com.ericsson.eniq.events.widgets.client.dialog.MessageDialog.DialogType;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownMenu;
import com.ericsson.eniq.events.widgets.client.dropdown.StringDropDownItem;
import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.utilities.ZIndexHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public abstract class AbstractFilterConfigView<C extends AbstractFilter<T>, T> extends
        BaseView<FilterConfigPresenter<C, T>> {
    interface AbstractFilterConfigViewBinder extends UiBinder<Widget, AbstractFilterConfigView> {

    }

    @UiField
    Label headerLabel;

    @UiField
    DropDownMenu<StringDropDownItem> categoryDropDown;

    @UiField
    Button applyButton;

    @UiField
    Button cancelButton;

    @UiField
    FlowPanel filterContainer;
    

    private final DialogBox box;

    @UiField(provided = true)
    static FilterConfigResourceBundle resourceBundle = GWT.create(FilterConfigResourceBundle.class);

    private final AbstractFilterConfigViewBinder INSTANCE = GWT.create(AbstractFilterConfigViewBinder.class);

    public AbstractFilterConfigView() {
        resourceBundle.style().ensureInjected();
        box = new DialogBox(false, true);
        box.setWidget(INSTANCE.createAndBindUi(this));
        box.setText("Advance Filter Configuration");
        box.getCaption().asWidget().addStyleName(resourceBundle.style().header());
        box.getCaption().asWidget().addStyleName(resourceBundle.style().headerLabel());
        initHeader();
        box.setStyleName(resourceBundle.style().container());
    }

    protected abstract void initHeader();
    
    @UiHandler("cancelButton")
    void onCancelClick(final ClickEvent clickEvent) {
        box.hide();
    }

    @UiHandler("closeButton")
    void onCloseClick(final ClickEvent clickEvent) {
        box.hide();
    }

    @UiHandler("applyButton")
    void onApplyClick(final ClickEvent clickEvent) {
        try{
        getPresenter().handleApplyClick(getFilters(), getSelectedCategory());
        box.hide();
        }catch(NumberFormatException e){
            new MessageDialog("Validate", e.getMessage(), DialogType.INFO).center();
        }
    }

    protected FilterCategory getSelectedCategory() {
        return FilterCategory.MATCH_ALL;
    }

    public void addFilter(final C filter) {
        final AbstractEditFilterView<C, T> editFilterWidget = addFilterWidget(filter);
        
        final FilterConfigPresenter<C, T> presenter = getPresenter();
        editFilterWidget.addPlusClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                presenter.handlePlusClick();
            }
        });
        
        editFilterWidget.addMinusClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                presenter.handleMinusClick(filterContainer.getWidgetIndex(editFilterWidget));
            }
        });
        
        editFilterWidget.enableRemoveFilter(true);
    }

    public abstract AbstractEditFilterView<C, T> addFilterWidget(final C filter);

    public void setHeaderText(final String text) {
        headerLabel.setText(text);
    }

    public List<C> getFilters() throws NumberFormatException{
        final int widgetCount = filterContainer.getWidgetCount();
        final List<C> filters = new ArrayList<C>();
        for (int i = 0; i < widgetCount; i++) {
            final AbstractEditFilterView<C, T> widget = (AbstractEditFilterView<C, T>) filterContainer.getWidget(i);
            if (!widget.isEmpty()) {
                filters.add(widget.getFilter());
            }
        }
        return filters;
    }

    public void show() {
        final Style style = box.getElement().getStyle();
        style.setZIndex(ZIndexHelper.getHighestZIndex());
        Style captionstyle = box.getCaption().asWidget().getElement().getStyle();
        captionstyle.setPaddingLeft(30, Style.Unit.PX);
        box.center();
    }

    public void updateCategories(final List<StringDropDownItem> items) {
        categoryDropDown.update(items);
    }

    public void removeFilter(final int index) {
        filterContainer.remove(index);
    }

    public void setCategory(final FilterCategory category) {

    }

    public void clearFilters() {
        filterContainer.clear();
    }

    public int getFiltersCount(){
        return filterContainer.getWidgetCount();
    }
    
    public Widget getFilter(int index){
        return filterContainer.getWidget(index);
    }
}
