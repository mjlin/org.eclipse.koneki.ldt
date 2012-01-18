/*******************************************************************************
 * Copyright (c) 2009, 2011 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *     Kevin KIN-FOO <kkinfoo@sierrawireless.com>
 *******************************************************************************/
package org.eclipse.koneki.ldt.editor.internal.completion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.codeassist.ScriptCompletionEngine;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.core.Flags;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementVisitor;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.koneki.ldt.editor.Activator;
import org.eclipse.koneki.ldt.parser.LuaASTModelUtils;
import org.eclipse.koneki.ldt.parser.LuaASTUtils;
import org.eclipse.koneki.ldt.parser.LuaASTUtils.TypeResolution;
import org.eclipse.koneki.ldt.parser.api.external.Item;
import org.eclipse.koneki.ldt.parser.api.external.RecordTypeDef;
import org.eclipse.koneki.ldt.parser.ast.LuaSourceRoot;
import org.eclipse.koneki.ldt.parser.ast.declarations.FunctionDeclaration;
import org.eclipse.koneki.ldt.parser.model.FakeField;

/**
 * 
 * @author Kevin KIN-FOO <kkinfoo@sierrawireless.com>
 * 
 */
public class LuaCompletionEngine extends ScriptCompletionEngine {

	@Override
	public void complete(IModuleSource module, int position, int k) {
		try {
			// Unable to parse to high level model element
			final IModelElement modelElement = module.getModelElement();
			ISourceModule sourceModule;
			if (modelElement instanceof ISourceModule) {
				sourceModule = (ISourceModule) modelElement;
			} else {
				Activator.logWarning(Messages.LuaCompletionEngineBadModelElement);
				return;
			}

			// Retrieve start position of word current user is typing
			final String start = getWordStarting(module.getSourceContents(), position).toLowerCase();
			this.actualCompletionPosition = position;
			this.offset = actualCompletionPosition - start.length();
			this.requestor.beginReporting();

			if (start.contains(".") || start.contains(":")) { //$NON-NLS-1$//$NON-NLS-2$
				// Select between module fields if completion is asked after a module reference
				final List<String> ids = new ArrayList<String>();
				Character lastOperator = getExpressionIdentifiers(start, ids);
				addFields(sourceModule, ids, this.offset, lastOperator);
			} else {
				// Search local declaration in AST
				addLocalDeclarations(sourceModule, start, this.offset);

				// Search global declaration in DLTK model
				addGlobalDeclarations(sourceModule, start);

				// Add keywords
				addKeywords(start);
			}
		} catch (ModelException e) {
			Activator.logError(Messages.LuaCompletionEngineIniTialization, e);
		} finally {
			this.requestor.endReporting();
		}

	}

	/**
	 * Browse {@link ISourceModule} in order to find {@link IModelElement}s pertinent for completion stating with given string.
	 * 
	 * @param sourceModule
	 *            Source representation browsed for variable {@link Declaration}s
	 * @param start
	 *            String that user just typed
	 */
	private void addGlobalDeclarations(ISourceModule sourceModule, final String start) throws ModelException {
		IScriptProject project = sourceModule.getScriptProject();
		IProjectFragment[] allProjectFragments = project.getAllProjectFragments();
		for (IProjectFragment iProjectFragment : allProjectFragments) {
			iProjectFragment.accept(new IModelElementVisitor() {
				public boolean visit(IModelElement element) {
					// manage only global member
					if (element instanceof IMember) {
						IMember member = (IMember) element;
						try {
							// manage public member except module
							if (Flags.isPublic(member.getFlags()) && !LuaASTUtils.isModule(member)) {
								final boolean goodStart = element.getElementName().toLowerCase().startsWith(start.toLowerCase());
								final boolean nostart = start.isEmpty();
								if (goodStart || nostart) {
									createProposal(element.getElementName(), element);
								}
							}
						} catch (ModelException e) {
							Activator.logWarning("unable to acces to " + member + " to feed autocompletion.", e); //$NON-NLS-1$//$NON-NLS-2$
						}
						// don't go inside member.
						return false;
					}
					return true;
				}
			});
		}
	}

	/**
	 * Add commons Lua keywords to {@link CompletionProposal} list
	 * 
	 * @param start
	 *            String that user just typed
	 */
	private void addKeywords(String start) {
		String[] keywords = new String[] { "and", "break", "do", "else", "elseif", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"end", "false", "for", "function", "if",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"in", "local", "nil", "not", "or",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"repeat", "return", "then", "true", "until", "while" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		for (int j = 0; j < keywords.length; j++) {
			if (start.isEmpty() || keywords[j].startsWith(start)) {
				createProposal(keywords[j], null);
			}
		}
	}

	private void addLocalDeclarations(ISourceModule sourceModule, String start, int position) {
		LuaSourceRoot luaSourceRoot = LuaASTModelUtils.getLuaSourceRoot(sourceModule);
		if (luaSourceRoot != null) {
			Collection<Item> localVars = LuaASTUtils.getLocalVars(luaSourceRoot, position, start);
			for (Item var : localVars) {
				createProposal(var.getName(), new FakeField(sourceModule, var.getName(), offset, var.getName().length(), Declaration.D_DECLARATOR
						& Declaration.AccPrivate));
			}
		}
	}

	private void addFields(final ISourceModule initialSourceModule, final List<String> ids, int position, Character lastOperator) {
		if (ids.size() < 2)
			return;

		// get the closest definition with the name of the first element of ids
		// we support only Identifier root for now.
		final String rootIdentifierName = ids.get(0);
		final LuaSourceRoot luaSourceRoot = LuaASTModelUtils.getLuaSourceRoot(initialSourceModule);
		final Item rootItem = LuaASTUtils.getClosestItem(luaSourceRoot, rootIdentifierName, position);
		if (rootItem == null)
			return;

		// resolve Item Type
		TypeResolution typeResolution = LuaASTUtils.resolveType(initialSourceModule, rootItem.getType());
		if (typeResolution == null || !(typeResolution.getTypeDef() instanceof RecordTypeDef))
			return;

		// found type of the last bigger complete index
		// (e.g. for identifier.field1.field2.f, get the type of identifier.field1.field2)
		RecordTypeDef currentRecordTypeDef = (RecordTypeDef) typeResolution.getTypeDef();
		ISourceModule currentSourceModule = typeResolution.getModule();
		for (int i = 1; i < ids.size() - 1; i++) {
			// check if the current type
			String fieldname = ids.get(i);
			Item item = currentRecordTypeDef.getFields().get(fieldname);

			// we could resolve the type of this field we stop the research
			if (item == null)
				return;

			// resolve the type
			typeResolution = LuaASTUtils.resolveType(currentSourceModule, item.getType());
			// we are interested only by record type
			if (typeResolution == null || !(typeResolution.getTypeDef() instanceof RecordTypeDef))
				return;

			currentRecordTypeDef = (RecordTypeDef) typeResolution.getTypeDef();
			currentSourceModule = typeResolution.getModule();
		}

		// get all the field of the complete index
		try {
			IType iType = LuaASTModelUtils.getIType(currentSourceModule, currentRecordTypeDef);
			IModelElement[] moduleFields = iType.getChildren();
			// get field name
			final String fieldName = ids.get(ids.size() - 1);
			// Update replacement position as well just insert field name without module reference name
			this.offset = actualCompletionPosition - fieldName.length();
			// search field
			for (final IModelElement field : moduleFields) {
				if (field instanceof IField || field instanceof IMethod) {
					final boolean goodStart = field.getElementName().toLowerCase().startsWith(fieldName.toLowerCase());
					final boolean nostart = fieldName.isEmpty();
					if (goodStart || nostart) {
						createProposal(field.getElementName(), field, lastOperator);
					}
				}
			}
		} catch (ModelException e) {
			Activator.logWarning("Unable to get model element.", e); //$NON-NLS-1$
		}
	}

	/**
	 * @param declaration
	 */
	private void createProposal(Declaration declaration) {
		final CompletionProposal proposal;
		switch (declaration.getKind()) {
		case Declaration.D_METHOD:
			proposal = createProposal(CompletionProposal.METHOD_REF, actualCompletionPosition);
			// All this just to cast Argument List to String Array
			@SuppressWarnings("rawtypes")
			final List parameters = ((FunctionDeclaration) declaration).getArguments();
			ArrayList<String> list = new ArrayList<String>(parameters.size());
			for (Object o : parameters) {
				if (o instanceof Argument) {
					list.add(((Argument) o).getName());
				}
			}
			proposal.setParameterNames(list.toArray(new String[list.size()]));
			break;
		case Declaration.D_CLASS:
			proposal = createProposal(CompletionProposal.TYPE_REF, actualCompletionPosition);
			break;
		default:
			proposal = createProposal(CompletionProposal.LOCAL_VARIABLE_REF, actualCompletionPosition);
			break;
		}
		final String name = declaration.getName();
		proposal.setFlags(declaration.getModifiers() & Flags.AccPrivate);
		proposal.setName(name);
		proposal.setCompletion(name);
		proposal.setReplaceRange(offset, offset + name.length());
		proposal.setRelevance(3);
		this.requestor.accept(proposal);
	}

	private void createProposal(String name, IModelElement element, Character lastOperator) {
		CompletionProposal proposal = null;
		int relevance = 2;
		try {
			if (element == null) {
				// Lower relevance for key words
				relevance = 1;
				proposal = this.createProposal(CompletionProposal.KEYWORD, this.actualCompletionPosition);
			} else {
				// Only collect global proposals
				IMember member = (IMember) element;
				if (lastOperator == ':' && member.getElementType() != IModelElement.METHOD)
					return;

				switch (member.getElementType()) {
				case IModelElement.METHOD:
					proposal = this.createProposal(CompletionProposal.METHOD_REF, this.actualCompletionPosition);
					IMethod method = (IMethod) member;
					if (lastOperator == ':') {
						if (method.getParameterNames().length == 0)
							return;

						String[] parameterNames = method.getParameterNames();
						String[] parameterNamesWithoutFirstOne = Arrays.copyOfRange(parameterNames, 1, parameterNames.length);
						proposal.setParameterNames(parameterNamesWithoutFirstOne);
					} else {
						proposal.setParameterNames(method.getParameterNames());
					}

					break;
				case IModelElement.FIELD:
					proposal = this.createProposal(CompletionProposal.FIELD_REF, this.actualCompletionPosition);
					proposal.setFlags(member.getFlags());
					break;
				case IModelElement.TYPE:
					proposal = this.createProposal(CompletionProposal.TYPE_REF, this.actualCompletionPosition);
					proposal.setFlags(((IType) element).getFlags());
					break;
				default:
					return;
				}
				proposal.setFlags(member.getFlags());
				proposal.setModelElement(member);
			}
			proposal.setName(name);
			proposal.setCompletion(name);
			proposal.setReplaceRange(offset, actualCompletionPosition);
			proposal.setRelevance(relevance);
			this.requestor.accept(proposal);
		} catch (ModelException e) {
			Activator.logWarning(Messages.LuaCompletionEngineProblemProcessingGlobals, e);
		}
	}

	private void createProposal(String name, IModelElement element) {
		createProposal(name, element, '\0');
	}

	// I'm unable to handle spaces in composed identifiers like 'someTable . someField'
	private String getWordStarting(final String content, final int position) {
		// manage inconsistent parameters
		if (position <= 0 || position > content.length())
			return Util.EMPTY_STRING;

		// search the begin on the string sequence to autocomplete
		int currentPosition = position;
		int lastValidPosition = position;
		boolean finish = false;
		do {
			currentPosition--;
			final char currentChar = content.charAt(currentPosition);
			final boolean isInvokeChar = currentChar == ':';
			final boolean isIndexChar = currentChar == '.';
			final boolean isIdentifierPart = Character.isLetterOrDigit(currentChar) || currentChar == '_';

			// we stop if we found a character which is neiter a identifier part or an operator
			if (isIdentifierPart || isIndexChar || isInvokeChar)
				lastValidPosition = currentPosition;
			else
				finish = true;

			// if we are at the end of the file it's finish too
		} while (!finish && currentPosition > 0);

		if (lastValidPosition >= position)
			return Util.EMPTY_STRING;
		return content.substring(lastValidPosition, position);
	}

	private Character getExpressionIdentifiers(final String composedId, List<String> result) {
		StringBuffer stringToParse = new StringBuffer(composedId);
		StringBuffer nextId = new StringBuffer();
		Character lastOperator = '\0'; // we support only if invoke is the last operator

		for (int i = 0; i < stringToParse.length(); i++) {
			Character character = stringToParse.charAt(i);

			if (!(character == '.') && !(character == ':')) {
				// if it's not an operator then append the next char
				nextId.append(character);
			} else {
				// we have an operator

				// don't allow 2 sucesssive operator
				if (nextId.length() == 0)
					return null;

				// we support only if invoke is the last operator
				if (lastOperator == ':' && character == ':')
					return null;

				// store value to next validation
				lastOperator = character;

				// store previous value
				result.add(nextId.toString());
				nextId = new StringBuffer();
			}
		}

		result.add(nextId.toString());

		return lastOperator;
	}
}
