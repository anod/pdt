package org.eclipse.php.internal.ui.refactor.processors;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.php.internal.ui.refactor.processors.messages";//$NON-NLS-1$

	public static String ReorgUtils_14;

	public static String DeleteFolderAndSubFolder;

	static {
		reloadMessages();
	}

	public static void reloadMessages() {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
		// Do not instantiate
	}

}
