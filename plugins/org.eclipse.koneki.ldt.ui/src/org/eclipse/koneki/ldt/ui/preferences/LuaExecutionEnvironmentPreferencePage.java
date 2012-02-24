/*******************************************************************************
 * Copyright (c) 2012 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.koneki.ldt.ui.preferences;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironment;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironmentConstants;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironmentManager;
import org.eclipse.koneki.ldt.core.buildpath.exceptions.LuaExecutionEnvironmentException;
import org.eclipse.koneki.ldt.ui.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class LuaExecutionEnvironmentPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private class ExecutionEnvironmentListEditor extends ListEditor {
		protected ExecutionEnvironmentListEditor(final Composite parent) {
			super(LuaExecutionEnvironmentConstants.PREF_EXECUTION_ENVIRONMENTS_LIST, Messages.LuaExecutionEnvironmentPreferencePageTitle, parent);
		}

		@Override
		public void doFillIntoGrid(Composite parent, int numColumns) {
			super.doFillIntoGrid(parent, numColumns);
			// Allow list to fill all parent height
			GridDataFactory.fillDefaults().grab(true, true).span(numColumns - 1, SWT.DEFAULT).applyTo(getListControl(parent));
		}

		/**
		 * Convert list separated with {@value LuaExecutionEnvironmentConstants#PREF_EXECUTION_ENVIRONMENTS_LIST_SEPARATOR} to array of installed
		 * execution environment names. <b>Doubles will be ignored</b>.
		 * 
		 */
		@Override
		protected String[] parseString(String stringList) {
			if (stringList.isEmpty()) {
				return new String[0];
			}
			// Avoid doubles
			HashSet<String> hash = new HashSet<String>();
			for (final String lib : stringList.split(LuaExecutionEnvironmentConstants.PREF_EXECUTION_ENVIRONMENTS_LIST_SEPARATOR)) {
				hash.add(lib);
			}
			return hash.toArray(new String[hash.size()]);
		}

		/**
		 * Deploy selected {@link LuaExecutionEnvironment} in installation directory. It will be truly installed when user will apply changes, which
		 * will update preference representing installed {@link LuaExecutionEnvironment}
		 */
		@Override
		protected String getNewInputObject() {
			/*
			 * Ask user for a file
			 */
			FileDialog filedialog = new FileDialog(Display.getDefault().getActiveShell());
			filedialog.setFilterExtensions(new String[] { LuaExecutionEnvironmentConstants.FILE_EXTENSION });
			final String selectedFilePath = filedialog.open();
			if (selectedFilePath == null) {
				return null;
			}

			/*
			 * Deploy
			 */
			try {
				return LuaExecutionEnvironmentManager.installLuaExecutionEnvironment(selectedFilePath).getEEIdentifier();
			} catch (FileNotFoundException e) {
				final Status status = new Status(Status.INFO, Activator.PLUGIN_ID, e.getMessage());
				ErrorDialog.openError(filedialog.getParent(), Messages.LuaExecutionEnvironmentPreferencePageIOProblemTitle,
						Messages.LuaExecutionEnvironmentPreferencePageProblemWithFile, status);
			} catch (LuaExecutionEnvironmentException e) {
				final Status status = new Status(Status.INFO, Activator.PLUGIN_ID, e.getMessage());
				ErrorDialog.openError(filedialog.getParent(), Messages.LuaExecutionEnvironmentPreferencePageUnableToInstallTitle,
						Messages.LuaExecutionEnvironmentPreferencePageInvalidFile, status);
			} catch (IOException e) {
				final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage());
				ErrorDialog.openError(filedialog.getParent(), Messages.LuaExecutionEnvironmentPreferencePageUnableToInstallTitle,
						Messages.LuaExecutionEnvironmentPreferencePageInstallationAborted, status);
			}
			return null;
		}

		/**
		 * Converts valid {@link LuaExecutionEnvironment} name to a {@link String} which can be stored as a preference string
		 * 
		 * @param items
		 *            {@link LuaExecutionEnvironment} full names
		 * @return String to be stored in preferences
		 */
		@Override
		protected String createList(String[] items) {
			final StringBuffer sb = new StringBuffer(items.length * 2);
			for (final String item : items) {
				sb.append(item);
				sb.append(LuaExecutionEnvironmentConstants.PREF_EXECUTION_ENVIRONMENTS_LIST_SEPARATOR);
			}
			return sb.toString();
		}
	};

	public LuaExecutionEnvironmentPreferencePage() {
		super(GRID);
	}

	/**
	 * Sets a PreferenceStrore related to core plug-in, in order to use same preferences as {@link LuaExecutionEnvironmentManager}
	 * 
	 * @see LuaExecutionEnvironmentManager
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(new ScopedPreferenceStore(new InstanceScope(), org.eclipse.koneki.ldt.Activator.PLUGIN_ID));
	}

	@Override
	protected void createFieldEditors() {
		final ListEditor listEditor = new ExecutionEnvironmentListEditor(getFieldEditorParent());
		addField(listEditor);
	}
}
