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

import java.util.Collection;

import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.internal.core.SourceRange;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.contexts.AbstractCompletionContext;
import org.eclipse.php.internal.core.codeassist.contexts.ICompletionContext;
import org.eclipse.php.internal.core.keywords.PHPKeywords;
import org.eclipse.php.internal.core.keywords.PHPKeywords.KeywordData;

/**
 * This strategy completes keywords. Direct implementation must define what kind of keywords
 * should be proposed in code assist. 
 * @author michael
 */
public abstract class KeywordsStrategy extends GlobalElementStrategy {
	
	public void apply(ICompletionContext context, ICompletionReporter reporter) throws BadLocationException {
		
		AbstractCompletionContext concreteContext = (AbstractCompletionContext) context;
		ISourceModule sourceModule = concreteContext.getSourceModule();
		String prefix = concreteContext.getPrefix();
		SourceRange replaceRange = getReplacementRange(concreteContext);
		
		Collection<KeywordData> keywordsList = PHPKeywords.getInstance(sourceModule.getScriptProject().getProject()).findByPrefix(prefix);
		for (KeywordData keyword : keywordsList) {
			if (!filterKeyword(keyword)) {
				reporter.reportKeyword(keyword.name, keyword.suffix, replaceRange);
			}
		}
	}

	/**
	 * Filters keyword from the proposal list
	 * @return
	 */
	abstract protected boolean filterKeyword(KeywordData keyword);
}