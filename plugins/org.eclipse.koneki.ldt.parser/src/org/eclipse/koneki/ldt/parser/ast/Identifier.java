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
package org.eclipse.koneki.ldt.parser.ast;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.koneki.ldt.parser.api.external.Item;

public class Identifier extends LuaExpression {

	private Item definition;

	public Identifier(Item definition) {
		super();
		this.definition = definition;
	}

	public Item getDefinition() {
		return definition;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			visitor.endvisit(this);
		}
	}
}
