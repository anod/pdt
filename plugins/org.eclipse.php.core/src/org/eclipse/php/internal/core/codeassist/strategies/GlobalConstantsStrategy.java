/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.php.internal.core.codeassist.strategies;

import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.core.*;
import org.eclipse.dltk.internal.core.SourceRange;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.internal.core.PHPCoreConstants;
import org.eclipse.php.internal.core.PHPCorePlugin;
import org.eclipse.php.internal.core.codeassist.CodeAssistUtils;
import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.contexts.AbstractCompletionContext;
import org.eclipse.php.internal.core.codeassist.contexts.ICompletionContext;

/**
 * This strategy completes global constants 
 * @author michael
 */
public class GlobalConstantsStrategy extends GlobalElementStrategy {
	
	public void apply(ICompletionContext context, ICompletionReporter reporter) throws BadLocationException {
		if (!showConstantAssist()) {
			return;
		}
		
		AbstractCompletionContext abstractContext = (AbstractCompletionContext) context;
		CompletionRequestor requestor = abstractContext.getCompletionRequestor();

		int mask = 0;
		if (requestor.isContextInformationMode()) {
			mask |= CodeAssistUtils.EXACT_NAME;
		}
		if (constantsCaseSensitive()) {
			mask |= CodeAssistUtils.CASE_SENSITIVE;
		}
		
		String prefix = abstractContext.getPrefix();
		SourceRange replaceRange = getReplacementRange(abstractContext);
		
		IModelElement[] constants = CodeAssistUtils.getGlobalFields(abstractContext.getSourceModule(), prefix, mask);
		for (IModelElement constant : constants) {
			IField field = (IField) constant;
			try {
				if ((field.getFlags() & Modifiers.AccConstant) != 0) {
					reporter.reportField(field, "", replaceRange);
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG_COMPLETION) {
					e.printStackTrace();
				}
			}
		}
	}

	protected boolean constantsCaseSensitive() {
		return Platform.getPreferencesService().getBoolean(PHPCorePlugin.ID, PHPCoreConstants.CODEASSIST_CONSTANTS_CASE_SENSITIVE, false, null);
	}
	
	protected boolean showConstantAssist() {
		return Platform.getPreferencesService().getBoolean(PHPCorePlugin.ID, PHPCoreConstants.CODEASSIST_SHOW_CONSTANTS_ASSIST, true, null);
	}
}