/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zend and IBM - Initial implementation
 *******************************************************************************/
package org.eclipse.php.internal.core.ast.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.php.internal.core.PHPVersion;
import org.eclipse.php.internal.core.ast.match.ASTMatcher;
import org.eclipse.php.internal.core.ast.visitor.Visitor;

/**
 * Represents namespace declaration:
 * <pre>e.g.<pre>namespace MyNamespace;
 *namespace MyProject\Sub\Level;
 */
public class NamespaceDeclaration extends Statement {
	
	private NamespaceName name;
	private Block body;
	
	/**
	 * The "namespace" structural property of this node type.
	 */
	public static final ChildPropertyDescriptor NAME_PROPERTY = 
		new ChildPropertyDescriptor(NamespaceDeclaration.class, "name", NamespaceName.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$
	
	public static final ChildPropertyDescriptor BODY_PROPERTY = 
		new ChildPropertyDescriptor(NamespaceDeclaration.class, "body", Block.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;
	static {
		List<StructuralPropertyDescriptor> properyList = new ArrayList<StructuralPropertyDescriptor>(2);
		properyList.add(NAME_PROPERTY);
		properyList.add(BODY_PROPERTY);
		PROPERTY_DESCRIPTORS = Collections.unmodifiableList(properyList);
	}

	public NamespaceDeclaration(int start, int end, AST ast, NamespaceName name, Block body) {
		super(start, end, ast);
		
		// Either name or body can be null, but not both of them
		if (name == null && body == null) {
			throw new IllegalArgumentException("Either namespace name or body must not be null");
		}
		
		this.name = name;
		this.body = body;
	}
	
	/**
	 * The body component of this namespace declaration node 
	 * @return body component of this namespace declaration node
	 */
	public Block getBody() {
		return body;
	}

	/**
	 * Sets the name of this parameter
	 * 
	 * @param name of this type declaration.
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setBody(Block block) {
		// an Assignment may occur inside a Expression - must check cycles
		ASTNode oldChild = this.body;
		preReplaceChild(oldChild, block, BODY_PROPERTY);
		this.body = block;
		postReplaceChild(oldChild, block, BODY_PROPERTY);
	}
	
	/**
	 * The name component of this namespace declaration node 
	 * @return name component of this namespace declaration node
	 */
	public NamespaceName getName() {
		return name;
	}

	/**
	 * Sets the name of this parameter
	 * 
	 * @param name of this type declaration.
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setName(NamespaceName name) {
		// an Assignment may occur inside a Expression - must check cycles
		ASTNode oldChild = this.name;
		preReplaceChild(oldChild, name, NAME_PROPERTY);
		this.name = name;
		postReplaceChild(oldChild, name, NAME_PROPERTY);
	}
	
	public void childrenAccept(Visitor visitor) {
		NamespaceName name = getName();
		if (name != null) {
			name.accept(visitor);
		}
		Block body = getBody();
		if (body != null) {
			body.accept(visitor);
		}
	}

	public void traverseTopDown(Visitor visitor) {
		accept(visitor);
		NamespaceName name = getName();
		if (name != null) {
			name.accept(visitor);
		}
		Block body = getBody();
		if (body != null) {
			body.accept(visitor);
		}
	}

	public void traverseBottomUp(Visitor visitor) {
		NamespaceName name = getName();
		if (name != null) {
			name.accept(visitor);
		}
		Block body = getBody();
		if (body != null) {
			body.accept(visitor);
		}
		accept(visitor);
	}

	public void toString(StringBuffer buffer, String tab) {
		buffer.append(tab).append("<NamespaceDeclaration"); //$NON-NLS-1$
		appendInterval(buffer);
		buffer.append(">\n");
		
		NamespaceName name = getName();
		if (name != null) {
			name.toString(buffer, TAB + tab);
			buffer.append("\n"); //$NON-NLS-1$
		}
		
		Block body = getBody();
		if (body != null) {
			body.toString(buffer, TAB + tab);
			buffer.append("\n"); //$NON-NLS-1$
		}
		buffer.append(tab).append("</NamespaceDeclaration>"); //$NON-NLS-1$
	}

	public void accept0(Visitor visitor) {
		final boolean visit = visitor.visit(this);
		if (visit) {
			childrenAccept(visitor);
		}
		visitor.endVisit(this);
	}	
	
	public int getType() {
		return ASTNode.NAMESPACE;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public boolean subtreeMatch(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		final NamespaceName name = ASTNode.copySubtree(target, getName());
		final Block body = ASTNode.copySubtree(target, getBody());
		final NamespaceDeclaration result = new NamespaceDeclaration(this.getStart(), this.getEnd(), target, name, body);
		return result;
	}
	
	@Override
	List<StructuralPropertyDescriptor> internalStructuralPropertiesForType(PHPVersion apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
	
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == BODY_PROPERTY) {
			if (get) {
				return getBody();
			} else {
				setBody((Block) child);
				return null;
			}
		}
		if (property == NAME_PROPERTY) {
			if (get) {
				return getName();
			} else {
				setName((NamespaceName) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}
}