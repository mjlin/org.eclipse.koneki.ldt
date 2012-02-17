package org.eclipse.koneki.ldt.ui.buildpath;

import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.internal.ui.wizards.IBuildpathContainerPage;
import org.eclipse.dltk.internal.ui.wizards.IBuildpathContainerPageExtension2;
import org.eclipse.dltk.ui.wizards.IBuildpathContainerPageExtension;
import org.eclipse.dltk.ui.wizards.NewElementWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class LuaExecutionEnvironmentWizardPage extends NewElementWizardPage implements IBuildpathContainerPage, IBuildpathContainerPageExtension,
		IBuildpathContainerPageExtension2 {

	public LuaExecutionEnvironmentWizardPage() {
		super("LuaExecutionEnvironmentWizardPage"); //$NON-NLS-1$
		// TODO verify if we really need to implements all the extension.
		// see org.eclipse.dltk.internal.ui.wizards.buildpath.UserLibraryWizardPage
	}

	@Override
	public void createControl(Composite parent) {
		setTitle("Execution Environments");
		Label label = new Label(parent, SWT.NONE);
		label.setText("TODO : implement page");

		setControl(label);
	}

	@Override
	public IBuildpathEntry[] getNewContainers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize(IScriptProject project, IBuildpathEntry[] currentEntries) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean finish() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IBuildpathEntry getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelection(IBuildpathEntry containerEntry) {
		// TODO Auto-generated method stub

	}
}