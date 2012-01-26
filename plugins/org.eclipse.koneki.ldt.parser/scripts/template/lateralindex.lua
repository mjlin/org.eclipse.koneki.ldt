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
[[<div id="navigation">
# if #modules > 0 then
<h2>Modules</h2>
	<ul>
#	for _, module in ipairs( modules ) do
 		<li><a href="$(module.name).html">$(module.name)</a></li>
#	end
	</ul>
# end
</div>]]
