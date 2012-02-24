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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class LuaExecutionEnvironmentGroup {

	private ComboViewer installedEEsComboViewer;

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
		final List<LuaExecutionEnvironment> installedExecutionEnvironments = LuaExecutionEnvironmentManager.getInstalledExecutionEnvironments();
		installedEEsComboViewer.setContentProvider(new ArrayContentProvider());
		installedEEsComboViewer.setInput(installedExecutionEnvironments);
		if (installedExecutionEnvironments.size() > 0) {
			installedEEsComboViewer.setSelection(new StructuredSelection(installedExecutionEnvironments.get(0)));
		}
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(installedEEsComboViewer.getControl());

		// new ScopedPreferenceStore(new InstanceScope(), org.eclipse.koneki.ldt.Activator.PLUGIN_ID)
		// .addPropertyChangeListener(new IPropertyChangeListener() {
		// @Override
		// public void propertyChange(PropertyChangeEvent event) {
		// if (event.getProperty().equals(LuaExecutionEnvironmentConstants.PREF_EXECUTION_ENVIRONMENTS_LIST)) {
		// String oldValue = event.getOldValue().toString();
		// String newValue = event.getNewValue().toString();
		// List<String> oldValues = Arrays.asList(oldValue
		// .split(LuaExecutionEnvironmentConstants.PREF_EXECUTION_ENVIRONMENTS_LIST_SEPARATOR));
		// List<String> newValues = Arrays.asList(newValue
		// .split(LuaExecutionEnvironmentConstants.PREF_EXECUTION_ENVIRONMENTS_LIST_SEPARATOR));
		//
		// LuaExecutionEnvironment currentEE = getSelectedLuaExecutionEnvironment();
		// if (currentEE != null && !newValues.contains(currentEE.getEEIdentifier())) {
		// installedEEsComboViewer.setSelection(new StructuredSelection(installedExecutionEnvironments.get(0)));
		// } else {
		// newValues.removeAll(oldValues);
		// if (!newValues.isEmpty()) {
		// installedEEsComboViewer.setSelection(new StructuredSelection(LuaExecutionEnvironmentManager
		// .getExecutionEnvironment(newValues.get(newValues.size() - 1))));
		// }
		// }
		//
		// installedEEsComboViewer.setInput(LuaExecutionEnvironmentManager.getInstalledExecutionEnvironments());
		// }
		// }
		// });

		// Set link to define a new execution environment
		final Link link = new Link(composite, SWT.NONE);
		link.setFont(group.getFont());
		link.setText("<a>" + Messages.LuaExecutionEnvironmentGroupManageExecutionEnvironment + "</a>"); //$NON-NLS-1$  //$NON-NLS-2$
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(link);
		link.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				final String pageId = LuaExecutionEnvironmentConstants.PREFERENCE_PAGE_ID;
				PreferencesUtil.createPreferenceDialogOn(parent.getShell(), pageId, new String[] { pageId }, null).open();
			}
		});
	}

	public LuaExecutionEnvironment getSelectedLuaExecutionEnvironment() {
		ISelection selection = installedEEsComboViewer.getSelection();
		if (!selection.isEmpty()) {
			if (selection instanceof IStructuredSelection) {
				return (LuaExecutionEnvironment) ((IStructuredSelection) selection).getFirstElement();
			}
		}
		return null;
	}
}
