/*******************************************************************************
 * Copyright (c) 2009, 2011 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 ******************************************************************************/
package org.eclipse.koneki.ldt.parser;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.codeassist.ScriptSelectionEngine;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.koneki.ldt.parser.LuaASTUtils.Definition;
import org.eclipse.koneki.ldt.parser.ast.LuaExpression;
import org.eclipse.koneki.ldt.parser.ast.LuaSourceRoot;

/**
 * Retrieve {@link ASTNode}s under given source code position.
 * 
 * @author Kevin KIN-FOO <kkinfoo@sierrawireless.com>
 */
public class LuaSelectionEngine extends ScriptSelectionEngine {

	/**
	 * Fetch {@link IModelElement} in an {@link IModuleSource} AST from source offset.
	 * 
	 * @param module
	 *            Browsed AST
	 * @param start
	 *            offset
	 * @param end
	 *            offset
	 * @return {@link IModelElement} representing definition of node at given offset
	 * @see org.eclipse.dltk.codeassist.ISelectionEngine#select(IModuleSource, int, int)
	 */
	@Override
	public IModelElement[] select(IModuleSource module, int start, int end) {
		// get the corresponding ISourceModule
		if (!(module instanceof ISourceModule))
			return new IModelElement[0];
		ISourceModule sourceModule = (ISourceModule) module;

		// get luaSourceRoot
		LuaSourceRoot luaSourceRoot = LuaASTModelUtils.getLuaSourceRoot(sourceModule);
		if (luaSourceRoot == null)
			return null;

		// get selected object
		LuaExpression luaExpression = LuaASTUtils.getLuaExpressionAt(luaSourceRoot, start, end);
		if (luaExpression == null)
			return null;

		// get definition
		Definition definition = LuaASTUtils.getDefinition(sourceModule, luaExpression);
		if (definition == null)
			return null;

		// get the corresponding IModelElement
		IModelElement result = LuaASTModelUtils.getIModelElement(definition.getModule(), definition.getItem());
		if (result == null)
			return null;

		return new IModelElement[] { result };
	}
}
