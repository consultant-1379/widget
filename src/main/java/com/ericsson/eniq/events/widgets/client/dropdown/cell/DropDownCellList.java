package com.ericsson.eniq.events.widgets.client.dropdown.cell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ericsson.eniq.events.widgets.client.dropdown.IDropDownItem;
import com.ericsson.eniq.events.widgets.client.dropdown.decorator.DropDownDecorator;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class DropDownCellList<V extends IDropDownItem> extends Widget {

    private DropDownCell<V> cell;

    private DropDownCell<V> separetorCell;

    private List<V> items = new ArrayList<V>();

    private V selectedItem;

    private V markedItem;

    private DropDownCell.Delegate<V> delegate;

    private boolean isRefreshing = false;

    private static com.google.gwt.user.client.Element tmpElem;

    public DropDownCellList(final DropDownDecorator<V> decorator, final DropDownCell.Delegate<V> delegate) {
        this.delegate = delegate;
        setElement(Document.get().createDivElement());

        cell = new DropDownCell<V>(decorator, delegate, new DropDownCell.Selection<V>() {
            @Override
            public boolean isSelected(final V value) {
                return value.equals(selectedItem);
            }
        }, new DropDownCell.Mark<V>() {
            @Override
            public boolean isMarked(final V value) {
                return value.equals(markedItem);
            }
        });
        separetorCell = new SeparatorCell<V>(decorator);

        sinkEvents(cell.getConsumedEvents());
    }

    private void sinkEvents(final Set<String> typeNames) {
        // Add also keydown & keyup event handlers, if not added already
        final Set<String> eventTypes = new HashSet<String>(typeNames);

        eventTypes.add("keydown");
        eventTypes.add("keyup");
        int eventsToSink = 0;
        for (final String type : eventTypes) {
            final int typeInt = Event.getTypeInt(type);
            if (typeInt > 0) {
                eventsToSink |= typeInt;
            }
        }

        if (eventsToSink > 0) {
            sinkEvents(eventsToSink);
        }
    }

    public void update(final List<V> items) {
        final SafeHtmlBuilder sb = new SafeHtmlBuilder();
        renderRowValues(sb, items);

        // If the widget is not attached, attach an event listener so we can catch
        // synchronous load events from cached images.
        if (!isAttached()) {
            DOM.setEventListener(getElement(), this);
        }

        isRefreshing = true;

        // Render the HTML.
        getElement().setInnerHTML(sb.toSafeHtml().asString());

        isRefreshing = false;

        // Detach the event listener.
        if (!isAttached()) {
            DOM.setEventListener(getElement(), null);
        }

        // Nullify marked item, as new values has been added
        this.markedItem = null;

        this.items = items;
    }

    public V getSelected() {
        if (selectedItem == null && items.size() > 1)
            selectedItem = items.get(0);
        return selectedItem;
    }

    public int getSelectedIndex() {
        return items.indexOf(getSelected());
    }

    public void setSelected(final int index) {
        final int previousSelectedIndex = selectedItem == null ? -1 : items.indexOf(selectedItem);
        if (previousSelectedIndex >= 0) {
            refreshChildren(previousSelectedIndex);
        }
        if (index >= 0) {
            refreshChildren(index);
        }
    }

    public void setSelected(final V item) {

        final int previousSelectedIndex = selectedItem == null ? -1 : items.indexOf(selectedItem);

        this.selectedItem = item;

        if (previousSelectedIndex >= 0) {
            // Unselect previous element
            refreshChildren(previousSelectedIndex);
        }

        if (item != null) {
            // Select current element
            final int index = items.indexOf(item);
            if (index >= 0) {
                refreshChildren(index);
            }
        }
    }

    @Override
    public final void onBrowserEvent(final Event event) {
        // Ignore spurious events (such as onblur) while we refresh the table.
        if (isRefreshing) {
            return;
        }

        // Verify that the target is still a child of this widget. IE fires focus
        // events even after the element has been removed from the DOM.
        final EventTarget eventTarget = event.getEventTarget();
        if (!Element.is(eventTarget) || !getElement().isOrHasChild(Element.as(eventTarget))) {
            return;
        }

        super.onBrowserEvent(event);

        final Element target = event.getEventTarget().cast();

        // Forward the event to the cell.
        String idxString = "";
        Element cellTarget = target;
        while ((cellTarget != null) && ((idxString = cellTarget.getAttribute("__idx")).length() == 0)) {
            cellTarget = cellTarget.getParentElement();
        }

        if (idxString.length() > 0) {
            final int idx = Integer.parseInt(idxString);
            if (items.size() <= idx) {
                // If the event causes us to page, then the index will be out of bounds.
                return;
            }

            final V value = items.get(idx);
            if (value.isSeparator()) {
                return;
            }
            final Cell.Context context = new Cell.Context(idx, 0, items.indexOf(value));

            fireEventToCell(context, event, cellTarget, value);
        }
    }

    public boolean shouldUpdate(final List<V> items) {
        return !this.items.equals(items);
    }

    public void markNext() {
        int currentIndex;
        if (markedItem != null) {
            currentIndex = items.indexOf(markedItem);
        } else if (selectedItem != null) {
            currentIndex = items.indexOf(selectedItem);
        } else { // If no item is selected/marked, make it to default to -1
            currentIndex = -1;
        }

        if (currentIndex >= -1 && (currentIndex + 1) < items.size()) {
            mark(currentIndex + 1);
        }
    }

    public void markPrevious() {
        int currentIndex;
        if (markedItem != null) {
            currentIndex = items.indexOf(markedItem);
        } else if (selectedItem != null) {
            currentIndex = items.indexOf(selectedItem);
        } else { // If no item is selected/marked, mark 1st element
            mark(0);
            return;
        }

        if (currentIndex > 0 && currentIndex < items.size()) {
            mark(currentIndex - 1);
        } else if (currentIndex == 0) {
            mark(0);
        }
    }

    public void selectMarked() {
        if (this.markedItem == null || this.markedItem.isSeparator()) {
            return;
        }

        delegate.execute(this.markedItem);

        this.markedItem = null;
    }

    private void mark(final int index) {
        if (items.size() == 0 || items.indexOf(markedItem) == index || index < 0) { // No need to mark anything if there are no items
            return;
        }

        final int previousMarkedIndex = markedItem == null ? -1 : items.indexOf(markedItem);

        markedItem = items.get(index);

        // Remove marking from previous element
        if (previousMarkedIndex >= 0) {
            refreshChildren(previousMarkedIndex);
        }

        if (items.get(index) != null) {
            refreshChildren(index);
        }
    }

    public void clearMarked() {
        if (items.contains(markedItem)) {
            final int index = items.indexOf(markedItem);

            markedItem = null;

            refreshChildren(index);
        }
    }

    private void renderRowValues(final SafeHtmlBuilder sb, final List<V> values) {
        final int end = values.size();
        for (int i = 0; i < end; i++) {
            final V value = values.get(i);

            final SafeHtmlBuilder cellBuilder = new SafeHtmlBuilder();
            if (!value.isSeparator()) {
                final Cell.Context context = new Cell.Context(i, 0, items.indexOf(value));
                cell.render(context, value, cellBuilder);
            } else {
                separetorCell.render(null, value, cellBuilder);
            }
            sb.append(cellBuilder.toSafeHtml());
        }
    }

    private void fireEventToCell(final Cell.Context context, final Event event, final Element element, final V value) {
        final Set<String> consumedEvents = cell.getConsumedEvents();
        if (consumedEvents != null && consumedEvents.contains(event.getType())) {
            cell.onBrowserEvent(context, element, value, event, null);
        }
    }

    private void refreshChildren(final int index) {
        if (items.size() <= index || index < 0) {
            return;
        }

        final V value = items.get(index);

        final SafeHtmlBuilder sb = new SafeHtmlBuilder();
        if (value.isSeparator()) {
            separetorCell.render(null, value, sb);
        } else {
            cell.render(new Cell.Context(index, 0, items.indexOf(value)), value, sb);
        }

        final Element newChildren = createElementFromHtml(sb.toSafeHtml());
        getElement().replaceChild(newChildren.getChild(0), getElement().getChild(index));
    }

    private com.google.gwt.user.client.Element createElementFromHtml(final SafeHtml html) {
        final com.google.gwt.user.client.Element elemParent = getTmpElem();

        // Attach an event listener so we can catch synchronous load events from
        // cached images.
        DOM.setEventListener(elemParent, this);

        tmpElem.setInnerHTML(html.asString());

        // Detach the event listener.
        DOM.setEventListener(elemParent, null);

        return elemParent;
    }

    private static com.google.gwt.user.client.Element getTmpElem() {
        if (tmpElem == null) {
            tmpElem = Document.get().createDivElement().cast();
        }

        return tmpElem;
    }

    public boolean isEmpty() {
        return items.size() == 0;
    }

    public int getItemCount() {
        return items.size();
    }

    public V getItem(final int index) {
        if (index > -1)
            return items.get(index);
        return null;
    }
}
