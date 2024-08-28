package com.ericsson.eniq.events.widgets.client.dialog;

import com.ericsson.eniq.events.common.client.CommonConstants;
import com.ericsson.eniq.events.common.client.mvp.BasePresenter;
import com.ericsson.eniq.events.widgets.client.dialog.translators.IRecordTranslator;

/**
 * APropertiesDialog is a non modal dialog used to display rows of records <code>R</code>.
 *
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public abstract class APropertiesDialog<R> extends MessageDialog {

   public static final int DEFAULT_WIDTH = 240;
   private final String title;

   private final IRecordTranslator<R> recordTranslator;

   private final BasePresenter presenter;

   private final static String DIALOG_NAME = "Properties";

   /**
    * @param title            dialog title
    * @param recordTranslator translator to get necessary information from a record
    * @param presenter        presenter to unload on close of the dialog
    *
    * @see #init()
    */
   public APropertiesDialog(final String title, final IRecordTranslator<R> recordTranslator,
                            final BasePresenter presenter) {
      this.title = title;
      this.recordTranslator = recordTranslator;
      this.presenter = presenter;
   }

   public void setWidthInPx(final int width) {
      setWidth(width + "px");
   }

   protected abstract StringBuilder iterateRecords(StringBuilder sb);

   protected void init() {
      makeMeDraggable();
      initDialog();
   }

   @Override
   protected void onUnload() {
      if (presenter != null) {
         presenter.unbind();
      }
   }

   protected void initDialog() {
      final StringBuilder buf = new StringBuilder(128).append('\n');
      final StringBuilder sb = iterateRecords(buf);

      setWidthInPx(DEFAULT_WIDTH);
      setModal(false);

      setElementId(CommonConstants.SELENIUM_TAG + "PropertiesWindow");

      show(title + " - " + DIALOG_NAME, sb.toString(), DialogType.INFO);
      center();
   }

   protected void processRecord(final R element, final StringBuilder sb) {
      if (!recordTranslator.isSystem(element)) {
         final String colName = recordTranslator.getHeader(element);

         sb.append(colName).append(':').append(' ');
         final String value = recordTranslator.getValue(element);

         if (value != null) {
            sb.append(value);
         }
         sb.append('\n').append('\n');
      }
   }
}
