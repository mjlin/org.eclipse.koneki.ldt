/*******************************************************************************
 * Copyright (c) 2011 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.koneki.ldt.wizards.pages;

import java.util.List;
import java.util.Observable;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironment;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironmentConstants;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironmentManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class LuaExecutionEnvironmentGroup extends Observable {

	private ComboViewer installedEEsComboViewer;
	private ISelection selection;

	public LuaExecutionEnvironmentGroup(final Composite parent) {
		// Create group
		Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.LuaExecutionEnvironmentGroupTitle);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(group);
		GridLayoutFactory.swtDefaults().applyTo(group);

		// Create composite for Execution Environment list and link to preference page
		final Composite composite = new Composite(group, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(composite);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(composite);

		// Execution Environment actual list
		installedEEsComboViewer = new ComboViewer(composite, SWT.READ_ONLY | SWT.BORDER);
		installedEEsComboViewer.setContentProvider(new ArrayContentProvider());
		updateExecutionEnvironmentList();
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(installedEEsComboViewer.getControl());

		// Set link to define a new execution environment
		final Link link = new Link(composite, SWT.NONE);
		link.setFont(group.getFont());
		link.setText("<a>" + Messages.LuaExecutionEnvironmentGroupManageExecutionEnvironment + "</a>"); //$NON-NLS-1$  //$NON-NLS-2$
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(link);

		// Refresh list after user went to Execution Environment preferences
		link.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				final String pageId = LuaExecutionEnvironmentConstants.PREFERENCE_PAGE_ID;
				PreferencesUtil.createPreferenceDialogOn(parent.getShell(), pageId, new String[] { pageId }, null).open();
				updateExecutionEnvironmentList();
			}
		});
	}

	public LuaExecutionEnvironment getSelectedLuaExecutionEnvironment() {
		// Secure selection retrieval
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				selection = installedEEsComboViewer.getSelection();
			}
		});
		// Extract Execution Environment from selection
		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			return (LuaExecutionEnvironment) ((IStructuredSelection) selection).getFirstElement();
		}
		return null;
	}

	private void updateExecutionEnvironmentList() {
		if (installedEEsComboViewer != null) {
			final List<LuaExecutionEnvironment> installedExecutionEnvironments = LuaExecutionEnvironmentManager.getInstalledExecutionEnvironments();
			installedEEsComboViewer.setInput(installedExecutionEnvironments);
			if (installedExecutionEnvironments.size() > 0) {
				installedEEsComboViewer.setSelection(new StructuredSelection(installedExecutionEnvironments.get(0)));
			}
			// Ask for page reload
			setChanged();
			notifyObservers();
		}
	}
}
