package com.ericsson.eniq.events.widgets.client.menu.options;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface OptionsMenuResourceBundle extends ClientBundle {

   @Source("OptionsMenu.css")
   OptionsMenuStyle style();

   interface OptionsMenuStyle extends CssResource {

      String menu();

      String left();

      String right();

      String item();

      String separator();
   }
}
