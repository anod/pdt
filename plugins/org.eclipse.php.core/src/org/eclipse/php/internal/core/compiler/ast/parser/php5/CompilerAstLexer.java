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
package org.eclipse.php.internal.core.compiler.ast.parser.php5;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java_cup.runtime.Symbol;

import org.eclipse.dltk.ast.references.TypeReference;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.php.internal.core.ast.nodes.IDocumentorLexer;
import org.eclipse.php.internal.core.ast.scanner.php5.ParserConstants;
import org.eclipse.php.internal.core.compiler.ast.nodes.Comment;
import org.eclipse.php.internal.core.compiler.ast.nodes.PHPDocBlock;
import org.eclipse.php.internal.core.compiler.ast.nodes.VarComment;
import org.eclipse.php.internal.core.compiler.ast.parser.DocumentorLexer;

public class CompilerAstLexer extends org.eclipse.php.internal.core.ast.scanner.php5.PhpAstLexer {

	private static final Pattern VAR_COMMENT_PATTERN = Pattern.compile("(.*)(\\$[^\\s]+)(\\s+)([^\\s]+).*");
	private PHPDocBlock latestDocBlock;
	private VarComment latestVarComment;

	public CompilerAstLexer(InputStream in) {
		super(in);
	}

	public CompilerAstLexer(java.io.Reader in) {
		super(in);
	}

	protected void handleVarComment() {
		String content = yytext();

		Matcher m = VAR_COMMENT_PATTERN.matcher(content);
		if (m.matches()) {
			int start = getTokenStartPosition();
			int end = start + getTokenLength();

			int varStart = start + m.group(1).length();
			String varName = m.group(2);
			int varEnd = varStart + varName.length();
			int typeStart = varEnd + m.group(3).length();
			String typeName = m.group(4);
			int typeEnd = typeStart + typeName.length();

			VariableReference varReference = new VariableReference(varStart, varEnd, varName);
			TypeReference typeReference = new TypeReference(typeStart, typeEnd, typeName);
			VarComment varComment = new VarComment(start, end, varReference, typeReference);
			getCommentList().add(varComment);

			latestVarComment = varComment;
		}
	}

	protected void addComment(int type) {
		int leftPosition = getTokenStartPosition();
		Comment comment = new Comment(commentStartPosition, leftPosition + getTokenLength(), type);
		getCommentList().add(comment);
	}

	protected void addVarComment() {
	}

	protected IDocumentorLexer getDocumentorLexer(java.io.Reader reader) {
		IDocumentorLexer lexer = new DocumentorLexer(reader);
		return lexer;
	}

	protected boolean parsePHPDoc() {
		boolean result = super.parsePHPDoc();
		if (result) {
			latestDocBlock = (PHPDocBlock) getCommentList().getLast();
		}
		return result;
	}

	protected Symbol createSymbol(int symbolNumber) {
		Symbol symbol = super.createSymbol(symbolNumber);

		switch (symbolNumber) {
			case ParserConstants.T_FUNCTION:
			case ParserConstants.T_CONST:
			case ParserConstants.T_VAR:
			case ParserConstants.T_CLASS:
			case ParserConstants.T_INTERFACE:
			case ParserConstants.T_STATIC:
			case ParserConstants.T_ABSTRACT:
			case ParserConstants.T_FINAL:
			case ParserConstants.T_PRIVATE:
			case ParserConstants.T_PROTECTED:
			case ParserConstants.T_PUBLIC:
				symbol.value = latestDocBlock;
				break;
			case ParserConstants.T_EQUAL:
				symbol.value = latestVarComment;
				latestVarComment = null;
				break;
		}

		latestDocBlock = null;
		return symbol;
	}
}