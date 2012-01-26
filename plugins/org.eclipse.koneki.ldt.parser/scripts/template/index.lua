--------------------------------------------------------------------------------
--  Copyright (c) 2012 Sierra Wireless.
--  All rights reserved. This program and the accompanying materials
--  are made available under the terms of the Eclipse Public License v1.0
--  which accompanies this distribution, and is available at
--  http://www.eclipse.org/legal/epl-v10.html
-- 
--  Contributors:
--       Kevin KIN-FOO <kkinfoo@sierrawireless.com>
--           - initial API and implementation and initial documentation
--------------------------------------------------------------------------------
return
[[<div id="content">
<h2>Modules</h2>
# if #modules then
	<table class="module_list">
#	for _, module in ipairs( modules ) do
		<tr>
		<td class="name" nowrap><a href="$(module.name).html">$(module.name)</a></td>
		<td class="summary">$( markdown(module.description) )</td>
		</tr>
#	end
	</table>
# end
</div>]]
