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
---
-- Template used to generate function documentation HTML.
return [[#
# if #func.params > 0 then
	<h4>Parameter$(#func.params > 1 and 's')</h4>
	<ul>
#	for _, param in ipairs(func.params) do
#     if param.name ~= "self" then 
	   <li><code><em>
#		  if param.type then
			  $(param.type.module)#$(param.type.typename)
#		  end
		  $(param.name)
#		  if param.optional then
			  $(param.optional)
#		  end
#		  if param.hidden then
			  $(param.hidden)
#		  end
		</em></code>: $( markdown(param.description) )</li>
#		end
#	end
	</ul>
# end
$(returnstring(func.returns))
]]
