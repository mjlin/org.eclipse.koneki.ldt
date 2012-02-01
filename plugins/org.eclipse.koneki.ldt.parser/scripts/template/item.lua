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
[[<a id="$(anchor(_item))" />
<dl class="function">
<dt>
# if _item.type and _item.parent and _item.parent.tag ~= 'functiontypedef' then
	<em><a href="#$( linkto(_item.type) )"></a></em>
# end
$(_item.name)</dt>
# if _item.shortdescription then
	<dd>$( markdown(_item.shortdescription) )</dd>
# end
# if markdown(_item.description) then
	<dd>$( markdown(_item.description) )</dd>
# end
#
# --
# -- Resolve item type definition
# --
# local typedef
# if  _item.parent and _item.type and _item.type.tag == 'internaltyperef' then
#	if _item.parent.tag == 'recordtypedef' then
#		local file = _item.parent.parent
#		typedef = file.types[ _item.type.typename ]
#	elseif _item.parent.tag == 'file' then
#		typedef = _item.parent.types[ _item.type.typename ]
#	end
# end
# --
# -- For function definitions, describe parameters and return values
# -- 
# if typedef and typedef.tag == 'functiontypedef' then
#	-- Describe parameters
#	local fdef = typedef
#	if #fdef.params > 0 then
		<h3>Parameter$(#fdef.params > 1 and 's')</h3>
		<ul>
#		for _, param in ipairs( fdef.params ) do
			<li>
			<code><em>
			$(param.name) $(param.optional and 'optional') $(param.hidden and 'hidden')
			</em></code>:
#			if param.description then
				$( markdown(param.description) )
#			end
			</li>
#		end
		</ul>
#	end
#
#	-- Describe returns types
#	if #fdef.returns > 0 then
		<h3>Return value$(#fdef.returns > 1 and 's')</h3>
#		-- Generate a list if they are several values
#		if #fdef.returns > 1 then
			<ol>
#			for _, ret in ipairs(fdef.returns) do
				<li>pretty types: $(ret.description and markdown(ret.description))</li>
#			end
			</ol>
#		else
			<p>
			pretty type:
#			if fdef.returns[1].description then
				$( markdown(fdef.returns[1].description) )
#			end
			</p>
#		end
#	end
# end
</dl>]]
