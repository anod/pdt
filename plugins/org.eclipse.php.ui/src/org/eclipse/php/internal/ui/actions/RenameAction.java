/*******************************************************************************
 * Copyright (c) 2005, 2009 Zend Technologies.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.php.internal.ui.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.internal.ui.refactoring.actions.RenameResourceAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.php.internal.ui.PHPUIMessages;
import org.eclipse.php.ui.actions.IRenamePHPElementActionFactory;
import org.eclipse.ui.*;

public class RenameAction implements IWorkbenchWindowActionDelegate, IEditorActionDelegate {

	private IActionDelegate fRenamePHPElement;
	private RenameResourceAction resourceAction;
	private ISelection selection;
	private static final String RENAME_ELEMENT_ACTION_ID = "org.eclipse.php.ui.actions.RenameElement"; //$NON-NLS-1$

	public void dispose() {

	}

	public void init(IWorkbenchWindow window) {
		if (window != null) {
			// gets the right factory to the element rename refactoring
			final IRenamePHPElementActionFactory actionDelegatorFactory = PHPActionDelegatorRegistry.getActionDelegatorFactory(RENAME_ELEMENT_ACTION_ID);
			if (actionDelegatorFactory == null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					if (page.getActivePart() != null)
						resourceAction = new RenameResourceAction(page.getActivePart().getSite());
				}
			} else {
				fRenamePHPElement = actionDelegatorFactory.createRenameAction();
				if (fRenamePHPElement instanceof IWorkbenchWindowActionDelegate) {
					((IWorkbenchWindowActionDelegate) fRenamePHPElement).init(window);
				}
			}
		}

	}

	public void run(IAction action) {
		if (resourceAction != null) {

			if (!selection.isEmpty()) {
				Object object = ((IStructuredSelection) selection).getFirstElement();
				IResource resource = null;
				if (object instanceof ISourceModule) {
					resource = ((ISourceModule) object).getResource();
				}

				if (object instanceof IResource) {
					resource = (IResource) object;
				}

				if (resource != null) {
					IStructuredSelection resourceSel = new StructuredSelection(resource);
					resourceAction.run(resourceSel);
				} else {
					MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), PHPUIMessages.getString("RenamePHPElementAction_name"), PHPUIMessages.getString("RenamePHPElementAction_not_available"));
				}
			}
		} else {
			fRenamePHPElement.run(action);
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;

		if (fRenamePHPElement != null) {
			fRenamePHPElement.selectionChanged(action, selection);
		}

	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor != null && resourceAction == null) {
			resourceAction = new RenameResourceAction(targetEditor.getSite());
		}
		if (fRenamePHPElement instanceof IEditorActionDelegate) {
			((IEditorActionDelegate) fRenamePHPElement).setActiveEditor(action, targetEditor);
		}
	}

}