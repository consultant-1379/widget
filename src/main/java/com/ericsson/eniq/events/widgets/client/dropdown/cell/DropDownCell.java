package com.ericsson.eniq.events.widgets.client.dropdown.cell;

import com.ericsson.eniq.events.widgets.client.dropdown.IDropDownItem;
import com.ericsson.eniq.events.widgets.client.dropdown.decorator.DropDownDecorator;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class DropDownCell<V extends IDropDownItem> extends AbstractCell<V> {

    private DropDownDecorator<V> decorator;

    private Delegate<V> delegate;

    private Selection<V> selection;

    private Mark<V> mark;

    public static interface Delegate<T> {
        void execute(T object);

        void mouseOver(T value);
    }

    public static interface Selection<T> {
        boolean isSelected(T value);
    }

    public static interface Mark<T> {
        boolean isMarked(T value);
    }

    public DropDownCell() {
        super((String) null);
    }

    public DropDownCell(final DropDownDecorator<V> decorator, final Delegate<V> delegate, final Selection<V> selection,
            final Mark<V> mark) {
        super("click", "mouseover");
        this.decorator = decorator;
        this.delegate = delegate;
        this.selection = selection;
        this.mark = mark;
    }

    @Override
    public void render(final Context context, final V value, final SafeHtmlBuilder sb) {
        final boolean isSelected = selection.isSelected(value);
        final boolean isMarked = mark.isMarked(value);

        sb.append(decorator.decorate(value, context.getIndex(), isSelected, isMarked));
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, V value, NativeEvent event, ValueUpdater<V> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        final EventTarget eventTarget = event.getEventTarget();
        if (!Element.is(eventTarget)) {
            return;
        }
        if ("click".equals(event.getType())) {
            if (parent.isOrHasChild(Element.as(eventTarget)) && decorator.isClickable(value)) {
                delegate.execute(value);
            }
        } else if ("mouseover".equals(event.getType())) {
            if (parent.isOrHasChild(Element.as(eventTarget))) {
                delegate.mouseOver(value);
            }
        }
    }
}
