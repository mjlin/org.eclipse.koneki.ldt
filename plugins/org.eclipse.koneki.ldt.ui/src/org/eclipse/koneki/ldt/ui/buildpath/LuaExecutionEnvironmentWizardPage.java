package org.eclipse.koneki.ldt.ui.buildpath;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.internal.ui.wizards.IBuildpathContainerPage;
import org.eclipse.dltk.internal.ui.wizards.IBuildpathContainerPageExtension2;
import org.eclipse.dltk.ui.wizards.NewElementWizardPage;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironment;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironmentBuildpathUtil;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironmentConstants;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironmentManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.PreferencesUtil;

@SuppressWarnings("restriction")
public class LuaExecutionEnvironmentWizardPage extends NewElementWizardPage implements IBuildpathContainerPage, IBuildpathContainerPageExtension2 {

	private TreeViewer eeTreeViewer;
	private IBuildpathEntry currentSelection;

	public LuaExecutionEnvironmentWizardPage() {
		super("LuaExecutionEnvironmentWizardPage"); //$NON-NLS-1$
	}

	@Override
	public void createControl(final Composite parent) {
		// Define header text
		setTitle(Messages.LuaExecutionEnvironmentWizardPageTitle);
		setDescription(Messages.LuaExecutionEnvironmentWizardPageDescription);

		// Define a composite for list and label
		final Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(composite);

		// Define Execution Environment list
		eeTreeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		eeTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				getContainer().updateButtons();
			}
		});
		updateExecutionEnvironmentList();
		GridDataFactory.fillDefaults().grab(true, true).applyTo(eeTreeViewer.getControl());

		// Allow user to manage Execution Environments
		final Button configureEE = new Button(composite, SWT.PUSH);
		configureEE.setText(Messages.LuaExecutionEnvironmentWizardPageConfigureButtonLabel);
		configureEE.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				final String pageId = LuaExecutionEnvironmentConstants.PREFERENCE_PAGE_ID;
				PreferencesUtil.createPreferenceDialogOn(parent.getShell(), pageId, new String[] { pageId }, null).open();
				updateExecutionEnvironmentList();
				updateSelection();
			}
		});
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BEGINNING).applyTo(configureEE);
		setControl(composite);
	}

	@Override
	public IBuildpathEntry[] getNewContainers() {
		updateSelection();
		final IBuildpathEntry choosenContainer = getSelection();
		if (choosenContainer == null) {
			// No path available or no selection
			return new IBuildpathEntry[0];
		}
		return new IBuildpathEntry[] { getSelection() };
	}

	@Override
	public boolean finish() {
		return true;
	}

	@Override
	public IBuildpathEntry getSelection() {
		return currentSelection;
	}

	@Override
	public void setSelection(IBuildpathEntry containerEntry) {
		currentSelection = containerEntry;
	}

	private void updateExecutionEnvironmentList() {
		if (eeTreeViewer == null) {
			return;
		}
		eeTreeViewer.setContentProvider(new LuaExecutionEnvironmentContentProvider());
		eeTreeViewer.setLabelProvider(new LuaExecutionEnvironmentLabelProvider());
		final List<LuaExecutionEnvironment> installedExecutionEnvironments = LuaExecutionEnvironmentManager.getInstalledExecutionEnvironments();
		eeTreeViewer.setInput(installedExecutionEnvironments);
	}

	private void updateSelection() {
		if (eeTreeViewer == null) {
			return;
		}
		final ISelection selection = eeTreeViewer.getSelection();
		// Extract Execution Environment from selection
		if ((selection != null) && !selection.isEmpty() && (selection instanceof IStructuredSelection)) {
			final LuaExecutionEnvironment ee = (LuaExecutionEnvironment) ((IStructuredSelection) selection).getFirstElement();
			if (ee != null) {
				/*
				 * Generate BuildPathContainer from selected Execution Environment
				 */
				final IPath path = LuaExecutionEnvironmentBuildpathUtil.getLuaExecutionEnvironmentContainerPath(ee);
				final IBuildpathEntry buildpathContainerEntry = DLTKCore.newContainerEntry(path);
				setSelection(buildpathContainerEntry);
			}
		}

	}
}
