package com.ericsson.eniq.events.widgets.client.dialog.translators;

/**
 * Translator to get necessary information from a record.
 *
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public interface IRecordTranslator<R> {

   boolean isSystem(R record);

   String getHeader(R record);

   String getValue(R record);

}