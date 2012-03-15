/*******************************************************************************
 * Copyright (c) 2011-2012 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.koneki.ldt.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IExternalSourceModule;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.koneki.ldt.Activator;
import org.eclipse.koneki.ldt.core.buildpath.LuaExecutionEnvironmentBuildpathUtil;

/**
 * Utility class for Lua
 */
public final class LuaUtils {

	private LuaUtils() {
	}

	/**
	 * @return full name of a module with dot syntax <br>
	 * 
	 *         e.g. : socket.core
	 */
	public static String getModuleFullName(IModuleSource module) {
		IModelElement modelElement = module.getModelElement();
		if (modelElement instanceof ISourceModule) {
			return getModuleFullName((ISourceModule) modelElement);
		} else {
			return module.getFileName();
		}
	}

	/**
	 * @return full name of a module with dot syntax <br>
	 * 
	 *         e.g. : socket.core
	 */
	public static String getModuleFullName(ISourceModule module) {
		// get module name
		String moduleName = module.getElementName();
		if (moduleName.endsWith(".lua")) { //$NON-NLS-1$
			moduleName = moduleName.replaceFirst("\\.lua$", ""); //$NON-NLS-1$//$NON-NLS-2$
		}

		// get prefix
		String prefix = null;
		if (module.getParent() instanceof IScriptFolder) {
			prefix = getFolderFullName((IScriptFolder) module.getParent());
		}

		if (prefix != null)
			if ("init".equalsIgnoreCase(moduleName))//$NON-NLS-1$
				return prefix;
			else
				return prefix + "." + moduleName; //$NON-NLS-1$
		else
			return moduleName;
	}

	/*
	 * @return the source folder full name with module dot syntax
	 */
	private static String getFolderFullName(IScriptFolder folder) {
		if (!folder.isRootFolder()) {
			// get folder name
			String folderName = folder.getElementName().replace("/", "."); //$NON-NLS-1$//$NON-NLS-2$

			// get prefix
			IModelElement parent = folder.getParent();
			String prefix = null;
			if (parent instanceof IScriptFolder) {
				prefix = getFolderFullName((IScriptFolder) parent) + "."; //$NON-NLS-1$
			}

			if (prefix != null)
				return prefix + "." + folderName; //$NON-NLS-1$
			else
				return folderName;
		}
		return null;
	}

	/**
	 * @return the {@link IModuleSource} from full name with module dot syntax
	 */
	public static IModuleSource getModuleSource(String name, IScriptProject project) {
		if (project == null && name == null || name.isEmpty())
			return null;

		// search in all source path.
		IProjectFragment[] allProjectFragments;
		try {
			allProjectFragments = project.getAllProjectFragments();
			for (IProjectFragment projectFragment : allProjectFragments) {
				IModuleSource moduleSource = getModuleSource(name, projectFragment);
				if (moduleSource != null)
					return moduleSource;
			}
		} catch (ModelException e) {
			Activator.logError(MessageFormat.format("Unable to find module: {0}.", name), e); //$NON-NLS-1$
			return null;
		}
		return null;
	}

	/*
	 * @return the {@link IModuleSource} from full name with module dot syntax
	 */
	private static IModuleSource getModuleSource(String name, IParent parent) throws ModelException {
		IModelElement[] children = parent.getChildren();
		for (IModelElement child : children) {
			if (child instanceof IModuleSource) {
				if (name.equals(getModuleFullName((IModuleSource) child))) {
					return (IModuleSource) child;
				}
			} else if (child instanceof IParent) {
				IModuleSource moduleSource = getModuleSource(name, (IParent) child);
				if (moduleSource != null)
					return moduleSource;
			}

		}
		return null;
	}

	/**
	 * @return the {@link ISourceModule} from full name with module dot syntax
	 */
	public static ISourceModule getSourceModule(String name, IScriptProject project) {
		IModuleSource moduleSource = getModuleSource(name, project);
		if (moduleSource instanceof ISourceModule) {
			return (ISourceModule) moduleSource;
		}
		return null;
	}

	/**
	 * @return the {@link IModuleSource} from Absolute local file URI
	 */
	public static IModuleSource getModuleSourceFromAbsoluteURI(URI absolutepath, IScriptProject project) {
		if (project == null || absolutepath == null)
			return null;

		ISourceModule sourceModule = getSourceModuleFromAbsoluteURI(absolutepath, project);
		if (sourceModule instanceof IModuleSource) {
			return (IModuleSource) sourceModule;
		}
		return null;
	}

	/**
	 * @return the {@link ISourceModule} from Absolute local file URI
	 */
	public static ISourceModule getSourceModuleFromAbsoluteURI(URI absolutepath, IScriptProject project) {
		if (project == null || absolutepath == null)
			return null;

		// search in all source path.
		IProjectFragment[] allProjectFragments;
		try {
			allProjectFragments = project.getAllProjectFragments();
			for (IProjectFragment projectFragment : allProjectFragments) {
				ISourceModule moduleSource = getSourceModuleFromAbsolutePath(absolutepath, projectFragment);
				if (moduleSource != null)
					return moduleSource;
			}
		} catch (ModelException e) {
			Activator.logError(MessageFormat.format("Unable to find module: {0}.", absolutepath), e); //$NON-NLS-1$
			return null;
		}
		return null;
	}

	/*
	 * @return the {@link ISourceModule} from Absolute local file URI and a parent
	 */
	private static ISourceModule getSourceModuleFromAbsolutePath(URI absolutepath, IParent parent) throws ModelException {
		IModelElement[] children = parent.getChildren();
		for (IModelElement child : children) {
			if (child instanceof ISourceModule) {
				if (URIUtil.sameURI(absolutepath, getModuleAbsolutePath((ISourceModule) child))) {
					return (ISourceModule) child;
				}
			} else if (child instanceof IParent) {
				ISourceModule moduleSource = getSourceModuleFromAbsolutePath(absolutepath, (IParent) child);
				if (moduleSource != null)
					return moduleSource;
			}

		}
		return null;
	}

	/**
	 * @return Absolute local file URI of a module source
	 */
	public static URI getModuleAbsolutePath(ISourceModule module) {
		if (module instanceof IExternalSourceModule) {
			String path = EnvironmentPathUtils.getLocalPath(module.getPath()).toString();
			if (path.length() != 0 && path.charAt(0) != '/') {
				path = '/' + path;
			}
			try {
				return new URI("file", "", path, null); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (URISyntaxException e) {
				final String message = MessageFormat.format("Unable to get file uri for external module : {0}.", module.getPath()); //$NON-NLS-1$
				Activator.logWarning(message, e);
			}
		} else {
			if (module.getResource() != null)
				return module.getResource().getLocationURI();
		}
		return null;
	}

	/**
	 * @return the list of direct project dependencies
	 * @throws ModelException
	 */
	public static List<IScriptProject> getDependencies(IScriptProject project) throws ModelException {
		ArrayList<IScriptProject> result = new ArrayList<IScriptProject>();
		// check in all project fragments
		IProjectFragment[] projectFragments = project.getAllProjectFragments();
		for (int i = 0; i < projectFragments.length; i++) {
			IProjectFragment projectFragment = projectFragments[i];
			if (isProjectDependencyFragment(project, projectFragment)) {
				IScriptProject currentScriptProject = projectFragment.getScriptProject();
				result.add(currentScriptProject);
			}
		}
		return result;
	}

	public static boolean isProjectDependencyFragment(IScriptProject project, IProjectFragment projectFragment) throws ModelException {
		IScriptProject fragmentProject = projectFragment.getScriptProject();
		if (fragmentProject != null && fragmentProject != project) {
			return (!projectFragment.isArchive() && !projectFragment.isBinary() && !projectFragment.isExternal());
		} else {
			return false;
		}
	}

	public static boolean isExecutionEnvironmentFragment(IProjectFragment projectFragment) throws ModelException {
		IBuildpathEntry rawBuildpathEntry = projectFragment.getRawBuildpathEntry();
		return (rawBuildpathEntry != null && LuaExecutionEnvironmentBuildpathUtil.isValidExecutionEnvironmentBuildPath(rawBuildpathEntry.getPath()));
	}

	/** Enable to perform operation in all files and directories in project fragments source directories */
	public static void visitSourceFiles(final IParent parent, final IProjectSourceVisitor visitor, final IProgressMonitor monitor)
			throws CoreException {
		visitSourceFiles(parent, visitor, monitor, Path.EMPTY);
	}

	private static void visitSourceFiles(final IParent parent, final IProjectSourceVisitor visitor, final IProgressMonitor monitor,
			final IPath currentPath) throws CoreException {

		final IModelElement[] children = parent.getChildren();
		for (int i = 0; i < children.length && !monitor.isCanceled(); i++) {
			final IModelElement modelElement = children[i];
			if (modelElement instanceof IExternalSourceModule) {

				/*
				 * Support external module
				 */
				final IExternalSourceModule sourceFile = ((IExternalSourceModule) modelElement);
				final IPath filePath = new Path(sourceFile.getFullPath().toOSString());
				final IPath relativeFilePath = currentPath.append(sourceFile.getElementName());
				final String charset = Charset.defaultCharset().toString();
				visitor.processFile(filePath, relativeFilePath, charset, monitor);
			} else if (modelElement instanceof ISourceModule) {

				/*
				 * Support local module
				 */
				final IResource resource = modelElement.getResource();
				IPath absolutePath;
				String charset;
				if (resource instanceof IFile) {
					final IFile file = (IFile) resource;
					absolutePath = new Path(resource.getLocationURI().getPath());
					charset = file.getCharset();
				} else {
					absolutePath = getAbsolutePathFromModelElement(modelElement);
					charset = Charset.defaultCharset().toString();
				}
				final IPath relativeFilePath = currentPath.append(absolutePath.lastSegment());
				visitor.processFile(absolutePath, relativeFilePath, charset, monitor);
			} else if (modelElement instanceof IScriptFolder) {

				/*
				 * Support source folder
				 */
				final IScriptFolder innerSourceFolder = (IScriptFolder) modelElement;
				// Do not notify interface for Source folders
				if (!innerSourceFolder.isRootFolder()) {
					final IResource resource = innerSourceFolder.getResource();
					IPath absolutePath;
					if (resource != null) {
						absolutePath = new Path(resource.getLocationURI().getPath());
					} else {
						absolutePath = getAbsolutePathFromModelElement(modelElement);
					}

					final IPath newPath = currentPath.append(innerSourceFolder.getElementName());
					visitor.processDirectory(absolutePath, newPath, monitor);
					visitSourceFiles(innerSourceFolder, visitor, monitor, newPath);
				} else {
					// Deal with sub elements
					visitSourceFiles(innerSourceFolder, visitor, monitor);
				}
			}
		}
	}

	private static IPath getAbsolutePathFromModelElement(final IModelElement modelElement) throws CoreException {
		final IPath folderPath = modelElement.getPath();
		if (EnvironmentPathUtils.isFull(folderPath)) {
			return EnvironmentPathUtils.getLocalPath(folderPath);
		} else {
			final String message = MessageFormat.format("Unable to get absolute location for {0}.", modelElement.getElementName()); //$NON-NLS-1$
			final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, message);
			throw new CoreException(status);
		}
	}
}
