/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.php.examples.xss;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.core.builder.IBuildParticipant;

/**
 * This build participant that just after the PHP builder, and validates
 * the PHP code for places where XSS can be applied (http://en.wikipedia.org/wiki/Cross-site_scripting)
 */
public class XSSProtectionParticipant implements IBuildParticipant {

	public void build(IBuildContext context) throws CoreException {
		// Current file is being built:
		ISourceModule sourceModule = context.getSourceModule();
		
		// Get file AST:
		ModuleDeclaration moduleDeclaration = SourceParserUtil.getModuleDeclaration(sourceModule);
		
		// Run the validation visitor:
		try {
			moduleDeclaration.traverse(new XSSValidationVisitor(context));
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, XSSPlugin.PLUGIN_ID, "An error has occurred while invoking XSS validator", e);
			throw new CoreException(status);
		}
	}

}