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
#
# -- Show item type for internal type
# if typedef and typedef.tag ~= 'functiontypedef'
#	and _item.type and _item.type.tag == 'internaltyperef'
# then
	<em><a href="$( linkto(_item.type) )">$(prettyname(_item.type))</a></em>
# end
$( prettyname(_item) )</dt>
# if _item.shortdescription then
	<dd>$( markdown(_item.shortdescription) )</dd>
# end
# if markdown(_item.description) then
	<dd>$( markdown(_item.description) )</dd>
# end
#
# --
# -- For function definitions, describe parameters and return values
# --
# if typedef and typedef.tag == 'functiontypedef' then
#	--
#	-- Describe parameters
#	--
#	local fdef = typedef
#
#	-- Adjust parameter count if first one is 'self'
#	local paramcount
#	if #fdef.params > 0 and fdef.params[1].name == 'self' then
#		paramcount = #fdef.params - 1
#	else
#		paramcount = #fdef.params
#	end
#
#	-- List parameters
#	if paramcount > 0 then
		<h3>Parameter$( paramcount > 1 and 's' )</h3>
		<ul>
#		for position, param in ipairs( fdef.params ) do
#			if not (position == 1 and param.name == 'self') then
				<li>
				<code><em>
				$(param.name) $(param.optional and 'optional') $(param.hidden and 'hidden')
				</em></code>:
#				if param.description then
					$( markdown(param.description) )
#				end
				</li>
#			end
#		end
		</ul>
#	end
#
#	--
#	-- Describe returns types
#	--
#	if fdef and #fdef.returns > 0 then
		<h3>Return value$(#fdef.returns > 1 and 's')</h3>
#		--
#		-- Generate a list if they are several return clauses
#		--
#		if #fdef.returns > 1 then
			<ol>
#			for position, ret in ipairs(fdef.returns) do
#				--
#				-- Show nice type list
#				--
#				local typelist = {}
#				for position, type in ipairs(ret.types) do
#					typelist[#typelist + 1] = prettyname( type )
#					-- Append end separator or separating comma 
#					typelist[#typelist + 1] = position == #ret.types and ':' or ', '
#				end
				<li>
#				if #typelist > 0 then
					<em>$( table.concat(typelist) )</em>
#				end
				$(ret.description and markdown(ret.description))
				</li>
#			end
			</ol>
#		else
			<p>
#			-- Show return type if provided
#			if fdef.returns[1].type then
				<em>$( prettyname(fdef.returns[1].type) ):</em>
#			end
#			if fdef.returns[1].description then
				$( markdown(fdef.returns[1].description) )
#			end
			</p>
#		end
# 	end
# end
</dl>]]
