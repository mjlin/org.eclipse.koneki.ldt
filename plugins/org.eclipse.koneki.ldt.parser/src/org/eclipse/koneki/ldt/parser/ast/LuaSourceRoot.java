/*******************************************************************************
 * Copyright (c) 2009, 2011 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/

/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-29 17:56:04 +0200 (mer., 29 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: LuaSourceRoot.java 2190 2009-07-29 15:56:04Z kkinfoo $
 */
package org.eclipse.koneki.ldt.parser.ast;

import java.util.Map;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.DefaultProblemIdentifier;
import org.eclipse.dltk.compiler.problem.IProblemIdentifier;
import org.eclipse.dltk.compiler.problem.ProblemSeverity;
import org.eclipse.dltk.utils.CorePrinter;
import org.eclipse.koneki.ldt.parser.api.external.LuaASTNode;
import org.eclipse.koneki.ldt.parser.api.external.LuaFileAPI;
import org.eclipse.koneki.ldt.parser.ast.declarations.DeclarationsContainer;

/**
 * The Root AST Node of a lua source file.
 */
public class LuaSourceRoot extends ModuleDeclaration {

	/**
	 * this is a complete representation of a lua file <br/>
	 * External API + Local AST
	 */
	private static class LuaFile extends LuaASTNode {

		// this is the API of the current Lua file.
		private LuaFileAPI fileAPI;
		private LuaInternalContent internalContent;

		public LuaFile() {
			internalContent = new LuaInternalContent();
		}

		public LuaInternalContent getInternalContent() {
			return internalContent;
		}

		public void setInternalContent(LuaInternalContent internalContent) {
			this.internalContent = internalContent;
		}

		public void setApi(final LuaFileAPI file) {
			fileAPI = file;
		}

		public LuaFileAPI getApi() {
			return fileAPI;
		}

		/**
		 * @see org.eclipse.dltk.ast.ASTNode#traverse(org.eclipse.dltk.ast.ASTVisitor)
		 */
		@Override
		public void traverse(ASTVisitor visitor) throws Exception {
			if (visitor.visit(this)) {
				if (getApi() != null) {
					fileAPI.traverse(visitor);
				}
				visitor.endvisit(this);
			}

		}
	}

	private LuaFile luaFile;

	public LuaSourceRoot(final int sourceLength) {
		super(sourceLength);
		declarationscontainer = new DeclarationsContainer(); // TODO to remove
		luaFile = new LuaFile();
		addStatement(luaFile);
	}

	public LuaFileAPI getFileapi() {
		return luaFile.fileAPI;
	}

	public LuaInternalContent getInternalContent() {
		return luaFile.internalContent;
	}

	/**
	 * Instantiates a new Lua module declaration.
	 * 
	 * @param length
	 *            the length
	 * @param rebuild
	 *            the rebuild
	 */
	public LuaSourceRoot(final int length, final boolean rebuild) {
		super(length, rebuild);
		declarationscontainer = new DeclarationsContainer(); // TODO to remove
		luaFile = new LuaFile();
		addStatement(luaFile);
	}

	// ******************************************************//
	// OLD CODE
	// ******************************************************//
	/** Indicates if any problem occurred during parsing */
	private DefaultProblem problem = null;

	/** documentation information */
	private String documentation;
	private Map<String, String> memberDocumentation;

	/** contains declaration of this source 'file' */
	private DeclarationsContainer declarationscontainer;

	private boolean error;

	public void setProblem(final int line, final int column, final int offset, final String message) {
		final IProblemIdentifier id = DefaultProblemIdentifier.decode(offset);
		problem = new DefaultProblem("", message, id, new String[0], ProblemSeverity.ERROR, offset, -1, line, column); //$NON-NLS-1$
		setError(true);
	}

	public boolean hasError() {
		return error;
	}

	public DefaultProblem getProblem() {
		return problem;
	}

	@Override
	public void printNode(final CorePrinter output) {
		final MethodDeclaration[] functions = this.getFunctions();
		if (functions.length > 0) {
			output.print("functions: "); //$NON-NLS-1$
			for (MethodDeclaration function : functions) {
				output.print(function.getName());
				output.print(' ');
			}
			output.println();
		}
		final FieldDeclaration[] fields = this.getVariables();
		if (fields.length > 0) {
			output.print("fields: ");//$NON-NLS-1$
			for (FieldDeclaration field : fields) {
				output.print(field.getName());
				output.print(' ');
			}
			output.println();
		}
		final TypeDeclaration[] types = this.getTypes();
		if (fields.length > 0) {
			output.print("types: ");//$NON-NLS-1$
			for (TypeDeclaration type : types) {
				output.print(type.getName());
				output.print(' ');
			}
			output.println();
		}
		output.indent();
		for (final Object o : getStatements()) {
			if (o instanceof Statement) {
				((Statement) o).printNode(output);
			}
		}
		output.dedent();
	}

	public void setGlobalDocumentation(final String doc) {
		documentation = doc;
	}

	public String getGlobalDocumentation() {
		return documentation;
	}

	public void setMembersDocumentation(final Map<String, String> doc) {
		memberDocumentation = doc;
	}

	public String getMemberDocumentation(final String memberIdentifier) {
		if (memberDocumentation != null) {
			return memberDocumentation.get(memberIdentifier);
		}
		return null;
	}

	public DeclarationsContainer getDeclarationsContainer() {
		return declarationscontainer;
	}

	/**
	 * @param status
	 */
	public void setError(final boolean status) {
		error = status;
	}

	public void setDocumentationInformation(final LuaFileAPI file) {
		luaFile.setApi(file);
	}

	public void setInternalContent(final LuaInternalContent content) {
		luaFile.setInternalContent(content);
	}
}
