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
return [[#
# if oreturns and #oreturns > 0 then
	<ol>
#	for _, currentreturn in ipairs(oreturns) do
		<li>
#		if currentreturn.types and #currentreturn.types > 0 then
#			local typelist = {}
#			for _, type in ipairs(currentreturn.types) do
#				typelist[ #typelist + 1 ] = (type.modulename or '')..'#'..type.typename
#			end
			<em>$(concat(typelist, ', '))</em>:
#	 	end
# 		if currentreturn.description then
			$( markdown(currentreturn.description) )
#		end
		</li>
# 	end
	</ol>
# end
]]