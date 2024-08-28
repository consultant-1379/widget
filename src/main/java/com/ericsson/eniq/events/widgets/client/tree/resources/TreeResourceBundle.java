/*
 * *
 *  * -----------------------------------------------------------------------
 *  *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 *  * -----------------------------------------------------------------------
 *
 */

package com.ericsson.eniq.events.widgets.client.tree.resources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree;

/**
 * TreeResourceBundle  - Resource for report tree
 *
 * @author ekurshi
 * @since November 2011
 */

public interface TreeResourceBundle extends Tree.Resources {

    @Override
    @Source("plus_normal.png")
    ImageResource treeClosed();

    @Override
    @Source("minus_normal.png")
    ImageResource treeOpen();
}
