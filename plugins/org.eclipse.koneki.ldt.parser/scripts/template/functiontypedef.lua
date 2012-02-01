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
<a id="$(anchor(_functiontypedef))"/>
# if #_functiontypedef.params > 0 and
#	not (#_functiontypedef.params == 1 and _functiontypedef.params[1].name == "self")  then
	<h4>Parameter$(#_functiontypedef.params > 1 and 's')</h4>
	<ul>
#	for _, param in ipairs(_functiontypedef.params) do
#		if param.name ~= "self" then
			<li><code><em>
#			if param.type then
				$(param.type.module)#$(param.type.typename)
#			end
			$(param.name) $(param.optional) $(param.hidden)
			</em></code>: $( markdown(param.description) )</li>
#		end
#	end
	</ul>
# end
# if #_functiontypedef.returns > 0 then
	<h4>Return$(#_functiontypedef.returns > 1 and 's')</h4>
#	for _, _return in ipairs(_functiontypedef.returns) do
		$(applytemplate(_return))
#	end
# end
#]]
