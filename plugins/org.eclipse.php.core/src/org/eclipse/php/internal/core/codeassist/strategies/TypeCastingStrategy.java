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
package org.eclipse.php.internal.core.codeassist.strategies;

import org.eclipse.dltk.internal.core.SourceRange;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.contexts.TypeCastingContext;

public class TypeCastingStrategy extends AbstractCompletionStrategy {

	private static final String[] TYPE_CASTS = { "int", "integer", "bool",
			"boolean", "float", "double", "real", "string", "array", "object",
			"unset", "binary" };

	public TypeCastingStrategy(ICompletionContext context) {
		super(context);
	}

	public void apply(ICompletionReporter reporter) throws BadLocationException {
		ICompletionContext context = getContext();
		SourceRange range = getReplacementRange(context);
		TypeCastingContext typeCastingContext = (TypeCastingContext) context;
		String prefix = typeCastingContext.getPrefix().toLowerCase();
		for (String cast : TYPE_CASTS) {
			if (cast.startsWith(prefix)) {
				reporter.reportKeyword(cast, "", range);
			}
		}
	}

}
