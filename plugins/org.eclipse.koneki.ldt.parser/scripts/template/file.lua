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
return[[#
<div id="content">
# --
# -- Module name
# --
# local moduletyperef = returns and returns[1] and returns[1].types[1]
# if moduletyperef then
	<h1>Module <code>$(file.name)</code></h1>
# end
# --
# -- Descriptions
# --
# local moduletype = moduletyperef and types[ moduletyperef.typename ]
# if moduletype and moduletype.shortdescription then
	$(markdown( file.shortdescription) )
# end
# if moduletype and description then
	<br/>$(markdown( file.description ) )
# end
# --
# -- Current module index
# --
# local fieldlist = moduletyperef and tolist(types[moduletyperef.typename] and types[moduletyperef.typename].fields)
# if fieldlist and #fieldlist > 0 then
	<h2>Field(s)</h2>
	<table class="function_list">
#	for _, item in ipairs(fieldlist) do
#		-- Append parameters for functions
#		local parameters
#		if item.type and item.type.tag == 'internaltyperef' then
#			if types[item.type.typename] and types[item.type.typename].tag == 'functiontypedef' then
#				local functiondef = types[item.type.typename]
#				local tab = {'('}
#				for i=1, #functiondef.params do
#						tab[ #tab +1 ] = functiondef.params[i].name
#						tab[ #tab +1 ] = ','
#				end
#				-- Overwrite last coma with closing parenthesis 
#				tab[ #tab > 1 and #tab or 2 ] = ')'
#				parameters = concat(tab)
#			end				
#		end
		<tr>
		<td class="name" nowrap="nowrap">
			<a href="#$(moduletyperef.typename).$(item.name)">$(item.name)$(parameters)</a>
		</td>
		<td class="summary">$( markdown(item.shortdescription) )</td>
		</tr>
#	end
	</table>
# end
# if returns and #returns > 0 then
	<h3>Returns</h3>
	$(returnstring(returns))
# end
# --
# -- Listing other types than module
# --
# local listtypes = tolist(types)
# if listtypes and #listtypes > 0 then 
# 	for defname, def in pairs(types) do
#		if def ~= moduletype then
#			if def.tag ~= 'functiontypedef' then
				<h2>Type $(defname)</h2>
				<table class="function_list">
#				for name, item in pairs(def.fields) do
					<tr>
					<td class="name" nowrap="nowrap"><a href="#$(defname).$(name)">$(name)</a></td>
					<td class="summary">$( markdown(item.shortdescription) )</td>
					</tr>
#				end
				</table>
#			end
#		end
# 	end
# end
# if moduletype then
<hr/>
<h2>Detailed documentation</h2>
# 	for name, item in pairs(moduletype.fields) do
#		local definition = item.type and types[item.type.typename]
#		if definition then
#			if definition.tag == 'functiontypedef' then
				$( typedefstring(name, definition, types, moduletyperef.typename) )
#			else
				<h3>$(item.name)<a id="$(moduletyperef.typename).$(item.name)"></a></h3>
				<p>
#				if item.type.tag == 'internaltyperef' and item.type.typename then
					<a href="#$(moduletyperef.typename).$(item.type.typename)">#$(item.type.typename)</a>
#				else
					#$(item.name)
#				end
				$( markdown(item.description) )
				</p>
#			end
#		end
# 	end
# end
# if types then
<h2>Internal type(s)</h2>
# 	for name, def in pairs(types) do
#		if def ~= moduletype and def.tag ~= 'functiontypedef' then
			$( typedefstring(name, def, types, name) )
#		end
# 	end
# end
#-- Globals to be handled later
#-- local globals = {}
#-- for name, item in pairs(globalvars) do
#--	globals[#globals] ='<h3>'..name..'</h3>'..itemstring(def)
#-- end
#-- if #globals > 0 then
#--	<h2>Global$(#globals>1 and 's')</h2>
#-- $(concat(globals))
#-- end
</div>
]]
