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
package org.eclipse.php.internal.core.codeassist.contexts;

import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.php.internal.core.util.text.PHPTextSequenceUtilities;
import org.eclipse.php.internal.core.util.text.TextSequence;


/**
 * This context represents the state when staying in a function declaration.
 * <br/>Examples:
 * <pre>
 *  1. function |
 *  2. function foo(|) {}
 *  etc... 
 * </pre>
 * @author michael
 */
public abstract class FunctionDeclarationContext extends DeclarationContext {
	
	private int functionStart;

	public boolean isValid(ISourceModule sourceModule, int offset, CompletionRequestor requestor) {
		if (!super.isValid(sourceModule, offset, requestor)) {
			return false;
		}
		
		TextSequence statementText = getStatementText();
		functionStart = PHPTextSequenceUtilities.isInFunctionDeclaration(statementText);
		if (functionStart == -1) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the start offset of word 'function' in function declaration relative to the
	 * statement text.
	 * @see #getStatementText()
	 * @return
	 */
	public int getFunctionStart() {
		return functionStart;
	}
}