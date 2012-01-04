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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.core.Flags;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.koneki.ldt.core.LuaUtils;
import org.eclipse.koneki.ldt.parser.api.external.ExternalTypeRef;
import org.eclipse.koneki.ldt.parser.api.external.InternalTypeRef;
import org.eclipse.koneki.ldt.parser.api.external.Item;
import org.eclipse.koneki.ldt.parser.api.external.LuaFileAPI;
import org.eclipse.koneki.ldt.parser.api.external.PrimitiveTypeRef;
import org.eclipse.koneki.ldt.parser.api.external.ReturnValues;
import org.eclipse.koneki.ldt.parser.api.external.TypeDef;
import org.eclipse.koneki.ldt.parser.api.external.TypeRef;
import org.eclipse.koneki.ldt.parser.ast.Block;
import org.eclipse.koneki.ldt.parser.ast.ExprTypeRef;
import org.eclipse.koneki.ldt.parser.ast.LocalVar;
import org.eclipse.koneki.ldt.parser.ast.LuaSourceRoot;
import org.eclipse.koneki.ldt.parser.ast.ModuleTypeRef;
import org.eclipse.koneki.ldt.parser.ast.declarations.ModuleReference;
import org.eclipse.koneki.ldt.parser.ast.visitor.ModuleReferenceVisitor;
import org.eclipse.koneki.ldt.parser.ast.visitor.ScopeVisitor;

public final class LuaASTUtils {
	private LuaASTUtils() {
	}

	private static class ClosestItemVisitor extends ASTVisitor {
		private Item result = null;
		private int position;

		private String identifierName;

		public ClosestItemVisitor(int position, String identifierName) {
			super();
			this.position = position;
			this.identifierName = identifierName;
		}

		public Item getResult() {
			return result;
		}

		@Override
		public boolean visit(ASTNode node) throws Exception {
			// we go down util we found the closer block.
			if (node instanceof Block) {
				if (node.sourceStart() <= position && position <= node.sourceEnd()) {
					return true;
				}
				return false;
			}
			return false;
		}

		@Override
		public boolean endvisit(ASTNode node) throws Exception {
			if (result == null && node instanceof Block) {
				// we go up only on the parent block
				List<LocalVar> localVars = ((Block) node).getLocalVars();
				for (LocalVar localVar : localVars) {
					Item item = localVar.getVar();
					if (item.getName().equals(identifierName)) {
						result = item;
					}
				}
				return true;
			}
			return false;
		}
	};

	public static Item getClosestItem(final LuaSourceRoot luaSourceRoot, final String identifierName, final int position) {
		// traverse the root block on the file with this visitor
		try {
			ClosestItemVisitor closestItemVisitor = new ClosestItemVisitor(position, identifierName);
			luaSourceRoot.getInternalContent().getContent().traverse(closestItemVisitor);
			return closestItemVisitor.getResult();
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// CHECKSTYLE:ON
			Activator.logError("unable to collect local var", e); //$NON-NLS-1$
		}
		return null;
	}

	public static TypeResolution resolveType(ISourceModule sourceModule, TypeRef typeRef) {
		if (typeRef instanceof PrimitiveTypeRef)
			return null;

		if (typeRef instanceof InternalTypeRef) {
			return resolveType(sourceModule, (InternalTypeRef) typeRef);
		}

		if (typeRef instanceof ExternalTypeRef) {
			return resolveType(sourceModule, (ExternalTypeRef) typeRef);
		}

		if (typeRef instanceof ModuleTypeRef) {
			return resolveType(sourceModule, (ModuleTypeRef) typeRef);
		}

		if (typeRef instanceof ExprTypeRef) {
			return resolveType(sourceModule, (ModuleTypeRef) typeRef);
		}

		return null;
	}

	public static TypeResolution resolveType(ISourceModule sourceModule, InternalTypeRef internalTypeRef) {
		LuaSourceRoot luaSourceRoot = LuaASTModelUtils.getLuaSourceRoot(sourceModule);
		TypeDef typeDef = luaSourceRoot.getFileapi().getTypes().get(internalTypeRef.getTypeName());
		return new TypeResolution(sourceModule, typeDef);
	}

	public static TypeResolution resolveType(ISourceModule sourceModule, ExternalTypeRef externalTypeRef) {
		ISourceModule externalSourceModule = LuaUtils.getSourceModule(externalTypeRef.getModuleName(), sourceModule.getScriptProject());
		if (externalSourceModule == null)
			return null;
		LuaSourceRoot luaSourceRoot = LuaASTModelUtils.getLuaSourceRoot(externalSourceModule);
		TypeDef typeDef = luaSourceRoot.getFileapi().getTypes().get(externalTypeRef.getTypeName());
		return new TypeResolution(externalSourceModule, typeDef);
	}

	public static TypeResolution resolveType(ISourceModule sourceModule, ModuleTypeRef moduleTypeRef) {
		ISourceModule referencedSourceModule = LuaUtils.getSourceModule(moduleTypeRef.getModuleName(), sourceModule.getScriptProject());
		if (referencedSourceModule == null)
			return null;

		LuaSourceRoot luaSourceRoot = LuaASTModelUtils.getLuaSourceRoot(referencedSourceModule);
		LuaFileAPI fileapi = luaSourceRoot.getFileapi();
		if (fileapi != null) {
			ArrayList<ReturnValues> returns = fileapi.getReturns();
			if (returns.size() > 0) {
				ReturnValues returnValues = returns.get(0);
				if (returnValues.getTypes().size() > moduleTypeRef.getReturnPosition() - 1) {
					TypeRef typeRef = returnValues.getTypes().get(moduleTypeRef.getReturnPosition() - 1);
					return resolveType(referencedSourceModule, typeRef);
				}
			}
		}
		return null;
	}

	public static class TypeResolution {
		private ISourceModule module;
		private TypeDef typeDef;

		public TypeResolution(ISourceModule module, TypeDef typeDef) {
			super();
			this.module = module;
			this.typeDef = typeDef;
		}

		public ISourceModule getModule() {
			return module;
		}

		public TypeDef getTypeDef() {
			return typeDef;
		}

	}

	public static Collection<Item> getLocalVars(LuaSourceRoot luaSourceRoot, final int offset, final String start) {
		// the localVars collected, indexed by var name;
		final Map<String, Item> collectedLocalVars = new HashMap<String, Item>();

		// the visitor which will collect local vars and store it in the map.
		ASTVisitor localvarCollector = new ASTVisitor() {
			@Override
			public boolean visit(ASTNode node) throws Exception {
				// we go down util we found the closer block.
				if (node instanceof Block) {
					if (node.sourceStart() <= offset && offset <= node.sourceEnd()) {
						return true;
					}
					return false;
				}
				return false;
			}

			@Override
			public boolean endvisit(ASTNode node) throws Exception {
				if (node instanceof Block) {
					// we go up only on all the parent block which
					List<LocalVar> localVars = ((Block) node).getLocalVars();
					for (LocalVar localVar : localVars) {
						Item item = localVar.getVar();
						if (!collectedLocalVars.containsKey(item.getName()) && (start == null || item.getName().startsWith(start))) {
							collectedLocalVars.put(item.getName(), item);
						}
					}
					return true;
				}
				return false;
			}
		};

		// traverse the root block on the file with this visitor
		try {
			luaSourceRoot.getInternalContent().getContent().traverse(localvarCollector);
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// CHECKSTYLE:ON
			Activator.logError("unable to collect local var", e); //$NON-NLS-1$
		}

		return collectedLocalVars.values();
	}

	/*
	 * DECPRECATED METHODE
	 */

	private static boolean isModule(int flags) {
		return (flags & Flags.AccModule) != 0;
	}

	public static boolean isModule(IMember member) throws ModelException {
		return member instanceof IType && isModule(member.getFlags());
	}

	public static boolean isModuleFunction(IMember member) throws ModelException {
		return member instanceof IMethod && isModule(member.getFlags());
	}

	public static boolean isGlobalTable(IMember member) throws ModelException {
		return member instanceof IType && Flags.isPublic(member.getFlags());
	}

	public static boolean isLocalTable(IMember member) throws ModelException {
		return member instanceof IType && Flags.isPrivate(member.getFlags());
	}

	public static ASTNode getClosestScope(ModuleDeclaration ast, int sourcePosition) throws Exception {
		ScopeVisitor visitor = new ScopeVisitor(sourcePosition);
		ast.traverse(visitor);
		return visitor.getScope();
	}

	public static ModuleReference getModuleReferenceFromName(final ModuleDeclaration ast, final String lhsName) {
		final ModuleReferenceVisitor visitor = new ModuleReferenceVisitor(lhsName);
		try {
			ast.traverse(visitor);
			return visitor.getModuleReference();
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// CHECKSTYLE:ON
			return null;
		}
	}

	public static IModelElement[] getModuleFields(ISourceModule module) throws ModelException {
		return new IModelElement[0];
	}

	public static IModelElement[] getModuleFields(String name, IScriptProject project) throws ModelException {
		IModuleSource moduleSource = LuaUtils.getModuleSource(name, project);
		if (moduleSource instanceof ISourceModule) {
			return getModuleFields((ISourceModule) moduleSource);
		}
		return new IModelElement[0];
	}

	public static boolean isAncestor(IModelElement element, IModelElement ancestor) {
		return ancestor != null && element != null && (ancestor.equals(element.getParent()) || isAncestor(element.getParent(), ancestor));
	}

}
