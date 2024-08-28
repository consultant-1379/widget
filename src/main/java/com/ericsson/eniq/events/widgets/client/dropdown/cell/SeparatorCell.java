package com.ericsson.eniq.events.widgets.client.dropdown.cell;

import com.ericsson.eniq.events.widgets.client.dropdown.IDropDownItem;
import com.ericsson.eniq.events.widgets.client.dropdown.decorator.DefaultDropDownDecorator;
import com.ericsson.eniq.events.widgets.client.dropdown.decorator.DropDownDecorator;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 * @param <V>
 */
public class SeparatorCell<V extends IDropDownItem> extends DropDownCell<V> {

    private final DropDownDecorator<V> decorator;

    public static interface Delegate<T> {
        void execute(T object);
    }

    public static interface Selection<T> {
        boolean isSelected(T value);
    }

    public static interface Mark<T> {
        boolean isMarked(T value);
    }

    public SeparatorCell(final DropDownDecorator<V> decorator) {
        super();
        this.decorator = decorator;
    }

    @Override
    public void render(final Context context, final V value, final SafeHtmlBuilder sb) {
        sb.append(((DefaultDropDownDecorator<V>) decorator).decorate(value));
    }

    @Override
    public void onBrowserEvent(final Context context, final Element parent, final V value, final NativeEvent event,
            final ValueUpdater<V> valueUpdater) {
        //do nothing
    }

}
