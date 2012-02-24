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
package org.eclipse.koneki.ldt.core.buildpath;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.koneki.ldt.Activator;

public class LuaExecutionEnvironmentPreferenceInitializer extends AbstractPreferenceInitializer {

	public LuaExecutionEnvironmentPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences store = new DefaultScope().getNode(Activator.PLUGIN_ID);
		store.put(LuaExecutionEnvironmentConstants.PREF_EXECUTION_ENVIRONMENTS_LIST, Util.EMPTY_STRING);
	}
}
