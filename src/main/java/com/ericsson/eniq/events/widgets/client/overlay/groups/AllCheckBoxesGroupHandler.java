package com.ericsson.eniq.events.widgets.client.overlay.groups;

import com.ericsson.eniq.events.widgets.client.checkable.AllCheckbox;
import com.ericsson.eniq.events.widgets.client.overlay.IWizardOverlayPanel;
import com.ericsson.eniq.events.widgets.client.overlay.translators.GroupTranslator;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author edavboj - Davis Bojars
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public class AllCheckBoxesGroupHandler<T, G> extends SimpleGroupHandler<T, G> {

    protected final Map<String, AllCheckbox> allCheckboxes = new HashMap<String, AllCheckbox>(4);

    public AllCheckBoxesGroupHandler(final IWizardOverlayPanel<T> overlayPanel,
            final GroupTranslator<G> groupResolver, final G[] groups) {
        super(overlayPanel, groupResolver, groups);
    }

    @Override protected void onItemAdd(String groupId, CheckBox checkBox) {
        allCheckboxes.get(groupId).registerChild(checkBox);
    }

    @Override protected void onGroupContentPanelCreate(String groupId, FlowPanel panel) {
        final AllCheckbox groupAllCheckBox = new AllCheckbox();

        overlayPanel.adjustGroupItemStyle(groupAllCheckBox);

        groupAllCheckBox.setWidth(overlayPanel.getItemWidth() + "px");
        groupAllCheckBox.setEnabled(overlayPanel.isEnabled());
        panel.add(groupAllCheckBox);
        allCheckboxes.put(groupId, groupAllCheckBox);
        overlayPanel.registerGroupItem(groupId, groupAllCheckBox);
    }

    @Override public void clear() {
        super.clear();
        allCheckboxes.clear();
    }
}
