package org.eclipse.php.internal.core.codeassist.contexts;

import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.internal.core.PHPVersion;
import org.eclipse.php.internal.core.compiler.ast.nodes.NamespaceReference;

public class NamespaceNameContext extends StatementContext {

	public boolean isValid(ISourceModule sourceModule, int offset, CompletionRequestor requestor) {
		if (!super.isValid(sourceModule, offset, requestor)) {
			return false;
		}
		if (getPhpVersion().isLessThan(PHPVersion.PHP5_3)) {
			return false;
		}
		try {
			char[] prefix = getPrefix().toCharArray();
			boolean isNamespace = false;
			for (int i = 0; i < prefix.length; ++i) {
				if (prefix[i] == NamespaceReference.NAMESPACE_SEPARATOR) {
					isNamespace = true;
					continue;
				}
				if (!Character.isJavaIdentifierPart(prefix[i])) {
					isNamespace = false;
					break;
				}
			}
			return isNamespace;

		} catch (BadLocationException e) {
		}
		return false;
	}

	public String getPrefix() throws BadLocationException {
		String prefix = super.getPrefix();
		if (prefix.length() > 0 && prefix.charAt(0) == NamespaceReference.NAMESPACE_SEPARATOR) {
			return prefix.substring(1);
		}
		return prefix;
	}
}