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
<h3>Field(s)</h3>
# for name, item in pairs( record.fields )do
#	local def = item.type and types and types[item.type.typename]
	<a id="$(parentname).$(name)"></a>
#	if def and def.tag == 'functiontypedef' then
      <dt>$(item.name)</dt>
      <dd>$( markdown(item.shortdescription) )</dd>
      <dd>$( markdown(item.description) )</dd>
		$(funcstring(def))
#	else
		$( itemstring(item, name) )
#	end
# end
]]
