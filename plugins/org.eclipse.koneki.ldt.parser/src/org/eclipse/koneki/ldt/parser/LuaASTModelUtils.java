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

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.koneki.ldt.parser.api.external.Item;
import org.eclipse.koneki.ldt.parser.api.external.LuaFileAPI;
import org.eclipse.koneki.ldt.parser.api.external.RecordTypeDef;
import org.eclipse.koneki.ldt.parser.api.external.TypeDef;
import org.eclipse.koneki.ldt.parser.ast.LuaSourceRoot;

public final class LuaASTModelUtils {
	private LuaASTModelUtils() {
	}

	/**
	 * Get LuaSourceRoot from ISourceModule <br/>
	 * 
	 * DLTK Model => AST
	 */
	public static LuaSourceRoot getLuaSourceRoot(ISourceModule module) {
		ModuleDeclaration moduleDeclaration = SourceParserUtil.getModuleDeclaration(module);
		if (moduleDeclaration instanceof LuaSourceRoot)
			return (LuaSourceRoot) moduleDeclaration;
		return null;
	}

	/**
	 * Get LuaSourceRoot from ISourceModule <br/>
	 * 
	 * DLTK Model => AST
	 */
	public static ASTNode getASTNode(IModelElement modelElement) {
		if (modelElement instanceof ISourceModule)
			return getLuaSourceRoot((ISourceModule) modelElement);
		if (modelElement instanceof IType)
			return getTypeDef((IType) modelElement);
		if (modelElement instanceof IField)
			return getItem((IField) modelElement);
		if (modelElement instanceof IMethod)
			return getItem((IMethod) modelElement);
		return null;
	}

	/**
	 * Get Record type def from ISourceModule <br/>
	 * 
	 * DLTK Model => AST
	 */
	public static RecordTypeDef getTypeDef(IType type) {
		LuaSourceRoot luaSourceRoot = getLuaSourceRoot(type.getSourceModule());
		LuaFileAPI fileapi = luaSourceRoot.getFileapi();
		TypeDef typeDef = fileapi.getTypes().get(type.getElementName());
		if (typeDef instanceof RecordTypeDef)
			return (RecordTypeDef) typeDef;
		return null;
	}

	/**
	 * Get Item from Ifield <br/>
	 * 
	 * DLTK Model => AST
	 */
	public static Item getItem(IField field) {
		IModelElement parent = field.getParent();
		if (parent instanceof IType) {
			RecordTypeDef typeDef = getTypeDef((IType) parent);
			return typeDef.getFields().get(field.getElementName());
		} else if (parent instanceof ISourceModule) {
			LuaSourceRoot luaSourceRoot = getLuaSourceRoot((ISourceModule) parent);
			return luaSourceRoot.getFileapi().getGlobalvars().get(field.getElementName());
		}
		return null;
	}

	/**
	 * Get Item from IMethod <br/>
	 * 
	 * DLTK Model => AST
	 */
	public static Item getItem(IMethod method) {
		IModelElement parent = method.getParent();
		if (parent instanceof IType) {
			RecordTypeDef typeDef = getTypeDef((IType) parent);
			return typeDef.getFields().get(method.getElementName());
		} else if (parent instanceof ISourceModule) {
			LuaSourceRoot luaSourceRoot = getLuaSourceRoot((ISourceModule) parent);
			return luaSourceRoot.getFileapi().getGlobalvars().get(method.getElementName());
		}
		return null;
	}

	/**
	 * Get IType from RecordTypeDef <br/>
	 * 
	 * AST => DLTK Model
	 */
	public static IType getIType(ISourceModule module, RecordTypeDef recordtypeDef) {
		IType type = module.getType(recordtypeDef.getName());
		return type;
	}
}
