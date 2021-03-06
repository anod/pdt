/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Zend Technologies
 *******************************************************************************/
package org.eclipse.php.internal.core.util;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A XML writer that can write Maps into an XML file. The values of the map are
 * saved according to their toString. In case that the value is a type of a
 * List, the values are separated into the list elements (e.g. <value> value
 * </value>). For example, this XML output was generated by this writer:
 * 
 * <pre>
 * 		&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 * 	&lt;map&gt;
 * 		&lt;key name=&quot;first&quot;&gt;
 * 			&lt;value&gt;good &gt; bye&lt;/value&gt;
 * 		&lt;/key&gt;
 * 		&lt;key name=&quot;second&quot;&gt;
 * 			&lt;value&gt;hello&lt;/value&gt;
 * 		&lt;/key&gt;
 * 		&lt;key name=&quot;list of elements&quot;&gt;
 * 			&lt;value&gt;A&lt;/value&gt;
 * 			&lt;value&gt;B&lt;/value&gt;
 * 			&lt;value&gt;C&lt;/value&gt;
 * 		&lt;/key&gt;
 * 	&lt;/map&gt;
 * </pre>
 * 
 * <B>Note</B> that the order of the elements is determined by the order of the
 * keys returned from the given HashMap.
 */
public class MapXMLWriter extends XMLWriter {

	/** The map tag identifier for the saved XML */
	public static final String MAP_TAG = "map";//$NON-NLS-1$
	/** The key tag identifier for the saved XML */
	public static final String KEY_TAG = "key";//$NON-NLS-1$
	/** The value tag identifier for the saved XML */
	public static final String VALUE_TAG = "value";//$NON-NLS-1$
	/** The name tag identifier for the saved XML */
	public static final String NAME_TAG = "name";//$NON-NLS-1$

	/**
	 * Constructs a new HashMapXMLWriter
	 * 
	 * @param output
	 * @throws UnsupportedEncodingException
	 */
	public MapXMLWriter(OutputStream output)
			throws UnsupportedEncodingException {
		super(output);
	}

	/**
	 * Write a Map to the XML.
	 * 
	 * @param map
	 *            A Map
	 */
	public void writeMap(Map map) {
		HashMap tagsMap = new HashMap();
		Iterator keys = map.keySet().iterator();
		startTag(MAP_TAG, null);
		while (keys.hasNext()) {
			Object key = keys.next();
			Object values = map.get(key);
			tagsMap.put(NAME_TAG, key.toString());
			startTag(KEY_TAG, tagsMap);
			if (key != null) {
				if (values instanceof List) {
					// Traverse the list and save each of its elements as a
					// value.
					Iterator valuesIter = ((List) values).iterator();
					while (valuesIter.hasNext()) {
						printSimpleTag(VALUE_TAG, valuesIter.next());
					}
				} else {
					// Save the mapping in as a simple XML tag.
					if (values != null) {
						printSimpleTag(VALUE_TAG, values.toString());
					}
				}
			}
			endTag(KEY_TAG);
		}
		endTag(MAP_TAG);
		flush();
	}
}
