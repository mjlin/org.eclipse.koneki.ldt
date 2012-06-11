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
package org.eclipse.koneki.ldt.tests;

import junit.framework.TestSuite;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class SuiteProvider.
 */
public final class SuiteProvider {
	private SuiteProvider() {
	}

	/**
	 * Gets the.
	 * 
	 * @return the test suite
	 */
	public static TestSuite get() {

		// Get plug-in's contributors
		final TestSuite suite = new TestSuite("Lua Development Tools"); //$NON-NLS-1$
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(Activator.EXTENSION_POINT);

		// Append every TestSuite to the current one
		for (final IExtension ext : extensionPoint.getExtensions()) {

			for (final IConfigurationElement configurationElement : ext.getConfigurationElements()) {

				// Get the good extension point from schema
				final String className = configurationElement.getAttribute("class"); //$NON-NLS-1$

				// Retrieve instance of contributor's plug-in
				final String bundleId = ext.getContributor().getName();
				final Bundle bundle = Platform.getBundle(bundleId);

				// Load the TestSuite
				try {
					// Retrieve instance of contributor through it's plug-in
					final Object newInstance = bundle.loadClass(className).newInstance();
					if (newInstance instanceof TestSuite) {
						suite.addTest((TestSuite) newInstance);
					}
				} catch (final InstantiationException e) {
					Activator.logError(e.getMessage(), e);
				} catch (final IllegalAccessException e) {
					Activator.logError(e.getMessage(), e);
				} catch (final ClassNotFoundException e) {
					Activator.logError(e.getMessage(), e);
				}
			}
		}
		return suite;
	}
}
