/*******************************************************************************
 * Copyright (c) 2011 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/eplv10.html
 *
 * Contributors:
 *     Sierra Wireless  initial API and implementation
 *******************************************************************************/
package org.eclipse.koneki.ldt.parser;

import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.compiler.IElementRequestor;
import org.eclipse.dltk.compiler.IElementRequestor.FieldInfo;
import org.eclipse.dltk.compiler.IElementRequestor.MethodInfo;
import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.SourceElementRequestVisitor;
import org.eclipse.koneki.ldt.parser.api.external.FunctionTypeDef;
import org.eclipse.koneki.ldt.parser.api.external.InternalTypeRef;
import org.eclipse.koneki.ldt.parser.api.external.Item;
import org.eclipse.koneki.ldt.parser.api.external.LuaFileAPI;
import org.eclipse.koneki.ldt.parser.api.external.Parameter;
import org.eclipse.koneki.ldt.parser.api.external.RecordTypeDef;
import org.eclipse.koneki.ldt.parser.api.external.TypeDef;

/**
 * traverse the Lua AST of a file to extract the DLTK model.
 */
public class LuaSourceElementRequestorVisitor extends SourceElementRequestVisitor {
	private LuaFileAPI luafileapi = null;
	private RecordTypeDef currentRecord = null;

	public LuaSourceElementRequestorVisitor(ISourceElementRequestor requesor) {
		super(requesor);
	}

	/**
	 * @see org.eclipse.dltk.ast.ASTVisitor#visit(org.eclipse.dltk.ast.ASTNode)
	 */
	@Override
	public boolean visit(ASTNode s) throws Exception {
		if (s instanceof Item)
			return visit((Item) s);
		else if (s instanceof RecordTypeDef)
			return visit((RecordTypeDef) s);
		else if (s instanceof LuaFileAPI)
			return visit((LuaFileAPI) s);
		return super.visit(s);
	}

	/**
	 * @see org.eclipse.dltk.ast.ASTVisitor#visit(org.eclipse.dltk.ast.ASTNode)
	 */
	@Override
	public boolean endvisit(ASTNode s) throws Exception {
		if (s instanceof RecordTypeDef)
			return endvisit((RecordTypeDef) s);
		else if (s instanceof LuaFileAPI)
			return endvisit((LuaFileAPI) s);
		return super.endvisit(s);
	}

	public boolean visit(LuaFileAPI luaAPI) throws Exception {
		luafileapi = luaAPI;
		return true;
	}

	public boolean endvisit(LuaFileAPI luaAPI) throws Exception {
		luafileapi = null;
		return true;
	}

	public boolean visit(Item item) throws Exception {

		if (item.getType() instanceof InternalTypeRef) {
			InternalTypeRef internalTypeRef = (InternalTypeRef) item.getType();

			// resolve type to know if item is a method
			if (luafileapi != null) {
				TypeDef typeDef = luafileapi.getTypes().get(internalTypeRef.getTypeName());
				if (typeDef instanceof FunctionTypeDef) {
					FunctionTypeDef funtionTypeDef = (FunctionTypeDef) typeDef;
					List<Parameter> params = funtionTypeDef.getParameters();
					String[] parametersName = new String[params.size()];
					// String[] initializers = new String[params.size()];

					for (int i = 0; i < params.size(); i++) {
						Parameter param = (Parameter) params.get(i);
						parametersName[i] = param.getName();
					}

					MethodInfo methodInfo = new ISourceElementRequestor.MethodInfo();
					methodInfo.name = item.getName();
					methodInfo.parameterNames = parametersName;
					methodInfo.modifiers = Declaration.D_METHOD_DECL & Declaration.AccPublic;
					methodInfo.nameSourceStart = item.sourceStart();
					methodInfo.nameSourceEnd = item.sourceEnd();
					methodInfo.declarationStart = item.sourceStart();
					// methodInfo.parameterInitializers = initializers;

					this.fRequestor.enterMethod(methodInfo);
					int declarationEnd = item.sourceEnd();
					this.fRequestor.exitMethod(declarationEnd);
					return true;
				}
			}
		}

		FieldInfo fieldinfo = new IElementRequestor.FieldInfo();
		fieldinfo.name = item.getName();
		fieldinfo.nameSourceStart = item.sourceStart();
		fieldinfo.nameSourceEnd = item.sourceEnd();
		fieldinfo.declarationStart = item.sourceStart();
		fieldinfo.modifiers = Declaration.AccPublic & Declaration.AccPublic;

		if (currentRecord == null) {
			// global var
		} else {
			// field of type
		}

		this.fRequestor.enterField(fieldinfo);
		int declarationEnd = item.sourceEnd();
		this.fRequestor.exitField(declarationEnd);
		return true;
	}

	public boolean visit(RecordTypeDef type) throws Exception {
		// create type
		RecordTypeDef recordtype = (RecordTypeDef) type;
		TypeInfo typeinfo = new IElementRequestor.TypeInfo();
		typeinfo.name = recordtype.getName();
		typeinfo.declarationStart = type.sourceStart();
		typeinfo.nameSourceStart = type.sourceStart();
		typeinfo.nameSourceEnd = type.sourceEnd();
		typeinfo.modifiers = Declaration.D_TYPE_DECL;

		this.fRequestor.enterType(typeinfo);
		this.currentRecord = recordtype;
		return true;
	}

	public boolean endvisit(RecordTypeDef type) throws Exception {
		int declarationEnd = type.sourceEnd();
		System.out.println(declarationEnd);
		this.fRequestor.exitType(declarationEnd);
		this.currentRecord = null;
		return true;
	}
}
