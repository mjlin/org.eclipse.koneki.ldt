/*******************************************************************************
 * Copyright (c) 2012 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.koneki.ldt.core.buildpath.exceptions;

public class LuaExecutionEnvironmentManifestException extends LuaExecutionEnvironmentException {
	private static final long serialVersionUID = 6733554278103059561L;

	public LuaExecutionEnvironmentManifestException(final String message) {
		super(message);
	}
}