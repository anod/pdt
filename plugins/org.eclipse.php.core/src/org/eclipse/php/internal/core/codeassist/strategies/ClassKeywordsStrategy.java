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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.contexts.AbstractCompletionContext;
import org.eclipse.php.internal.core.codeassist.contexts.ICompletionContext;
import org.eclipse.php.internal.core.keywords.PHPKeywords.Context;
import org.eclipse.php.internal.core.keywords.PHPKeywords.KeywordData;
import org.eclipse.php.internal.core.util.text.TextSequence;

/**
 * This strategy completes keywords that can be shown in a class body 
 * @author michael
 */
public class ClassKeywordsStrategy extends KeywordsStrategy {

	private TextSequence statementText;
	
	public void apply(ICompletionContext context, ICompletionReporter reporter) throws BadLocationException {
		statementText = ((AbstractCompletionContext) context).getStatementText();
		super.apply(context, reporter);
	}

	protected boolean filterKeyword(KeywordData keyword) {
		if (keyword.context != Context.CLASS_BODY) {
			return true;
		}
		// check whether this keyword is included in the statement already
		int i = statementText.toString().indexOf(keyword.name);
		if (i != -1) {
			if ((i == 0 || Character.isWhitespace(statementText.charAt(i-1)) && Character.isWhitespace(statementText.charAt(i + keyword.name.length())))) {
				return true;
			}
		}
		return false;
	}

}