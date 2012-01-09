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

public class ASTBuilderLuaModule extends AbstractMetaLuaModule {

	public static final String META_LIB_PATH = "/scriptMetalua/";//$NON-NLS-1$
	public static final String LIB_PATH = "/scripts/";//$NON-NLS-1$

	public static final String BUILDER = "dltk_ast_builder";//$NON-NLS-1$
	public static final String BUILDER_SCRIPT = BUILDER + ".mlua";//$NON-NLS-1$
	public static final String MARKER = "declaration_marker";//$NON-NLS-1$
	public static final String MARKER_SCRIPT = MARKER + ".mlua";//$NON-NLS-1$

	private LuaState lua = null;

	public synchronized LuaSourceRoot buildAST(final String string) {
		// if (lua == null)
		lua = loadLuaModule();

		pushLuaModule(lua);
		lua.getField(-1, "ast_builder"); //$NON-NLS-1$
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
		sourcepaths.add(BUILDER_SCRIPT);
		sourcepaths.add(MARKER_SCRIPT);
		sourcepaths.add("internalmodel_builder.mlua");
		sourcepaths.add("metalua/treequery.mlua");
		sourcepaths.add("metalua/treequery/walk.mlua");
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
		return BUILDER;
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
