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
-- @module docutils
-- Handles link generation, node quick description.
--
-- Provides:
--	* link generation
--	* anchor generation
--	* node quick description
local M = {}

function M.isempty(map)
	local f = pairs(map)
	return f(map) == nil
end
---
-- Provides anchor string for an object of API mode
--
-- @function [parent = #docutils]
-- @param modelobject Object form API model
-- @result #string Anchor for an API model object, this function __may rise an error__
-- @usage # -- In a template
-- # local anchorname = anchor(someobject)
-- <a id="$(anchorname)" />
function M.anchor( modelobject )
	local tag = modelobject.tag
	if tag == 'internaltyperef' then
		return '#'..modelobject.typename
	elseif tag == 'item' then
		-- Handle items referencing globals
		if not modelobject.parent then
			return modelobject.name
		else
			-- Prefix item name with parent anchor
			return M.anchor(modelobject.parent)..'.'..modelobject.name
		end
	elseif tag == 'file' or tag == 'recordtypedef' then
		return modelobject.name or ''
	end
	error('No anchor available for '..tag)
end
---
-- Generates text for HTML links from API model element
--
-- @function [parent = #docutils]
-- @param modelobject Object form API model
-- @result #string Links text for an API model element, this function __may rise an error__.
-- @usage # -- In a template
-- <a href="$( linkto(api) )">Some text</a>
function M.linkto( apiobject )
	local tag = apiobject.tag
	if tag == 'internaltyperef' then
		return '#' .. apiobject.typename
	elseif tag == 'externaltyperef' then
		return apiobject.modulename..'.html#'..apiobject.typename
	elseif tag == 'primitivetyperef' then
		return nil
	elseif tag == 'item' then
		if not apiobject.parent then
			-- This item reference a global definition
			return M.anchor( apiobject )
		else
			-- This item is related to a type
			return M.anchor( apiobject.parent ) .. '.' .. apiobject.name
		end
	elseif tag == 'file' or tag == 'recordtypedef' then
		return M.anchor( apiobject )..'.html'
	elseif tag == 'index' then
		return 'index.html'
	end
	error('No link available for '..tag)
end
---
-- Provide human readable overview from an API model element
--
-- Resolve all element needed to summurize nicely an element form API model.
-- @usage $ print( prettyname(item) )
--	module:somefunction(secondparameter)
-- @function [parent = #docutils]
-- @param apiobject Object form API model
-- @result #string Human readable description of given element, ready to print
--	__may rise error__.
function M.prettyname( apiobject )
	local tag = apiobject.tag
	-- Process references
	if tag == 'internaltyperef' or tag == 'primitivetyperef' then
		return '#'..apiobject.typename
	elseif tag == 'externaltyperef' then
		return apiobject.modulename..'#'..apiobject.typename
	elseif tag == 'file' or tag == 'index' then
		return apiobject.name
	elseif tag == 'item' then
		--
		-- Deal with items
		--
		if not apiobject.type then return apiobject.name end
		-- Retrieve referenced type definition
		local parent = apiobject.parent
		local global = parent and  parent.tag == 'file'
		local typefield = parent and parent.tag == 'recordtypedef'
		local definition
		if global then
			definition = parent.types[ apiobject.type.typename ]
		elseif typefield then
			local file = parent.parent
			definition = file.types[apiobject.type.typename ]
		end

		-- When type is not available, just provide item name
		if not definition then
			return apiobject.name
		elseif definition.tag == 'recordtypedef' then
			-- In case of record return item name prefixed with module name if available
			return global and apiobject.name or apiobject.type.typename..'.'..apiobject.name
		else
			--
			-- Dealing with a function
			--

			-- Build parameter list
			local paramlist = {}
			local hasfirstself = false
			for position, param in ipairs(definition.params) do
				-- When first parameter is 'self', it will not be part of listed parameters
				if position == 1 and param.name == 'self' then
					hasfirstself = true
				else
					paramlist[#paramlist + 1] = param.name
					if position ~= #definition.params then
						paramlist[#paramlist + 1] =  ', '
					end
				end
			end
			-- Compose function prefix,
			-- ':' if 'self' is first parameter, '.' else way
			local fname = ''
			if not global then
				fname = fname .. parent.name..( hasfirstself and ':' or '.' )
			end
			-- Append function parameters
			return fname .. apiobject.name .. '(' .. table.concat( paramlist ) ..')'
		end
	end
	error('No pretty name for '..tag)
end
return M
