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
package org.eclipse.php.internal.core.compiler.ast.nodes;

import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.utils.CorePrinter;
import org.eclipse.php.internal.core.compiler.ast.visitor.ASTPrintVisitor;

/**
 * Represents a namespace declaration
 * <pre>
 * <pre>e.g.<pre>
 * namespace A;
 * namespace A {
 * }
 * namespace {
 * }
 */
public class NamespaceDeclaration extends TypeDeclaration implements IPHPDocAwareDeclaration {

	public static final String GLOBAL = "__global__namespace__"; //$NON-NLS-1$
	private PHPDocBlock phpDoc;

	public NamespaceDeclaration(int start, int end, int nameStart, int nameEnd, String className, Block body, PHPDocBlock phpDoc) {
		super(className, nameStart, nameEnd, start, end);
		this.phpDoc = phpDoc;
		
		// If there's no body - create a fake one that will hold the namespace statements
		if (body == null) {
			body = new Block(start, end);
		}
		setBody(body);
	}
	
	public PHPDocBlock getPHPDoc() {
		return phpDoc;
	}
	
	public void addStatement(Statement statement) {
		getBody().addStatement(statement);
	}

	public int getKind() {
		return ASTNodeKinds.NAMESPACE_DECLARATION;
	}

	/**
	 * We don't print anything - we use {@link ASTPrintVisitor} instead
	 */
	public final void printNode(CorePrinter output) {
	}

	public String toString() {
		return ASTPrintVisitor.toXMLString(this);
	}
}