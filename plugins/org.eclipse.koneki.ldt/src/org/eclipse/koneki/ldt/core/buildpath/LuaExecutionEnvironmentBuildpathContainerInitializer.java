package org.eclipse.koneki.ldt.core.buildpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.BuildpathContainerInitializer;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathContainer;
import org.eclipse.dltk.core.IScriptProject;

public class LuaExecutionEnvironmentBuildpathContainerInitializer extends BuildpathContainerInitializer {

	@Override
	public void initialize(IPath containerPath, IScriptProject project) throws CoreException {
		// TODO see org.eclipse.dltk.internal.core.UserLibraryBuildpathContainerInitializer.initialize(IPath, IScriptProject)

		// check if this is a execution environnement path
		// not sure this test is necessary ...
		if (LuaExecutionEnvironmentBuildpathUtil.isLuaExecutionEnvironmentContainer(containerPath)) {
			// extract EE ID from path
			// use LuaExecutionEnvironmentBuildpathUtil
			String eeID = null;
			// extract EE version from path
			// use LuaExecutionEnvironmentBuildpathUtil
			String eeVersion = null;

			LuaExecutionEnvironment executionEnvironment = LuaExecutionEnvironmentManager.getExecutionEnvironment(eeID, eeVersion);

			LuaExecutionEnvironmentBuildpathContainer container = new LuaExecutionEnvironmentBuildpathContainer(eeID, eeVersion, containerPath);
			DLTKCore.setBuildpathContainer(containerPath, new IScriptProject[] { project }, new IBuildpathContainer[] { container }, null);
		}
	}

	@Override
	public boolean canUpdateBuildpathContainer(IPath containerPath, IScriptProject project) {
		// TODO verify the utility..
		return LuaExecutionEnvironmentBuildpathUtil.isLuaExecutionEnvironmentContainer(containerPath);
	}

	public Object getComparisonID(IPath containerPath, IScriptProject project) {
		return containerPath;
	}
}
