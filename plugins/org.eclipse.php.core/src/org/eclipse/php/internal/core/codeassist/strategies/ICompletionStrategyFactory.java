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

import org.eclipse.php.internal.core.codeassist.contexts.ICompletionContext;

/**
 * This factory finds correct strategies according to the given completion context.
 * @author michael
 */
public interface ICompletionStrategyFactory {

	/**
	 * Creates completion strategies for the given context.
	 * @param context Completion context
	 * @return completion strategies or empty list in case no strategy could be found for the given context
	 */
	public ICompletionStrategy[] create(ICompletionContext context);
}