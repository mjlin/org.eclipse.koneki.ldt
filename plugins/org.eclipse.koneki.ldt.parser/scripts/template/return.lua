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
return [[
# if oreturns and #oreturns.types > 0 then
<h4>Return value$(#oreturns.types > 1 and 's')</h4>
#	if oreturns.types then
		<ol>
#		for _, type in ipairs(oreturns.types) do
			<li><em>$(type.modulename)#$(type.typename)</em></li>
#		end
		</ol>
# 	end
# end
# if oreturns.description then
	<p>$(oreturns.description)</p>
# end
]]