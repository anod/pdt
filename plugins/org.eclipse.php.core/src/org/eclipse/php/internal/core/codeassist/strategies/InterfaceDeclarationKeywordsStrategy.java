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

import org.eclipse.dltk.internal.core.SourceRange;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.contexts.AbstractCompletionContext;
import org.eclipse.php.internal.core.codeassist.contexts.ICompletionContext;
import org.eclipse.php.internal.core.codeassist.contexts.InterfaceDeclarationKeywordContext;

/**
 * This strategy completes keywords that can be shown in a class body 
 * @author michael
 */
public class InterfaceDeclarationKeywordsStrategy extends AbstractCompletionStrategy {

	public void apply(ICompletionContext context, ICompletionReporter reporter) throws BadLocationException {
		if (!(context instanceof InterfaceDeclarationKeywordContext)) {
			return;
		}
		
		InterfaceDeclarationKeywordContext concreteContext = (InterfaceDeclarationKeywordContext) context;
		SourceRange replaceRange = getReplacementRange(concreteContext);

		if (!concreteContext.hasExtends()) {
			reporter.reportKeyword("extends", getSuffix(concreteContext), replaceRange);
		}
	}
	
	public String getSuffix(AbstractCompletionContext context) {
		return context.hasWhitespaceBeforeCursor() ? " " : "";
	}
}