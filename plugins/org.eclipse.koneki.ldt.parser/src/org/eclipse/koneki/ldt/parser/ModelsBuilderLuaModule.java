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
package org.eclipse.koneki.ldt.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.koneki.ldt.internal.parser.DLTKObjectFactory;
import org.eclipse.koneki.ldt.parser.ast.LuaSourceRoot;

import com.naef.jnlua.LuaState;

public class ModelsBuilderLuaModule extends AbstractMetaLuaModule {

	public static final String META_LIB_PATH = "/scriptMetalua/";//$NON-NLS-1$
	public static final String LIB_PATH = "/scripts/";//$NON-NLS-1$

	public static final String MODELS_BUILDER = "javamodelsbuilder";//$NON-NLS-1$
	public static final String MODELS_BUILDER_SCRIPT = MODELS_BUILDER + ".mlua";//$NON-NLS-1$

	public static final String INTERNAL_MODEL_BUILDER = "internalmodelbuilder";//$NON-NLS-1$
	public static final String INTERNAL_MODEL_BUILDER_SCRIPT = INTERNAL_MODEL_BUILDER + ".mlua";//$NON-NLS-1$

	private LuaState lua = null;

	public synchronized LuaSourceRoot buildAST(final String string) {
		// if (lua == null)
		lua = loadLuaModule();

		pushLuaModule(lua);
		lua.getField(-1, "build"); //$NON-NLS-1$
		lua.pushString(string);
		lua.call(1, 1);
		LuaSourceRoot luaSourceRoot = lua.checkJavaObject(-1, LuaSourceRoot.class);
		lua.pop(2);

		lua.close();

		return luaSourceRoot;
	}

	/**
	 * @see org.eclipse.koneki.ldt.parser.AbstractMetaLuaModule#loadLuaModule()
	 */
	@Override
	protected LuaState loadLuaModule() {
		LuaState luaState = super.loadLuaModule();
		DLTKObjectFactory.register(luaState);
		return luaState;
	}

	/**
	 * @see org.eclipse.koneki.ldt.parser.AbstractMetaLuaModule#getMetaLuaSourcePath()
	 */
	@Override
	protected String getMetaLuaSourcePath() {
		return META_LIB_PATH;
	}

	/**
	 * @see org.eclipse.koneki.ldt.parser.AbstractMetaLuaModule#getMetaLuaFileToCompile()
	 */
	@Override
	protected List<String> getMetaLuaFileToCompile() {
		ArrayList<String> sourcepaths = new ArrayList<String>();
		sourcepaths.add(MODELS_BUILDER_SCRIPT);
		sourcepaths.add(INTERNAL_MODEL_BUILDER_SCRIPT);
		// sourcepaths.add("metalua/treequery.mlua");
		// sourcepaths.add("metalua/treequery/walk.mlua");
		return sourcepaths;
	}

	/**
	 * @see org.eclipse.koneki.ldt.parser.AbstractMetaLuaModule#getPluginID()
	 */
	@Override
	protected String getPluginID() {
		return Activator.PLUGIN_ID;
	}

	/**
	 * @see org.eclipse.koneki.ldt.parser.AbstractMetaLuaModule#getModuleName()
	 */
	@Override
	protected String getModuleName() {
		return MODELS_BUILDER;
	}

	/**
	 * @see org.eclipse.koneki.ldt.parser.AbstractLuaModule#getLuaSourcePaths()
	 */
	@Override
	protected List<String> getLuaSourcePaths() {
		ArrayList<String> sourcepaths = new ArrayList<String>();
		sourcepaths.add(LIB_PATH);
		return sourcepaths;
	}
}
