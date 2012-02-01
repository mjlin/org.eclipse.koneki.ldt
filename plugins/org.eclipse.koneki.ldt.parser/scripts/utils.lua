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
-- @result #string Anchor for an API model object
-- @result #nil, #string In case of error, error is described in error string.
-- @usage # -- In a template
-- # local anchorname = anchor(someobject)
-- <a id="$(anchorname)" />
function M.anchor( modelobject )
	if not modelobject then return nil, 'No API model object provided.' end
	if not modelobject.tag then return nil, 'No tag given on current object' end
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
		return modelobject.name
	end
	error('No anchor available for '..tag)
end
function M.linkto( apiobject )
	if not apiobject then return nil, 'No API model object provided.' end
	if not apiobject.tag then return nil, 'No tag given on current object' end
	local tag = apiobject.tag
	if tag == 'internaltyperef' then
		return '#'..(apiobject.typename or '')
	elseif tag == 'externaltyperef' then
		return apiobject.modulename..'#'..apiobject.typename
	elseif tag == 'item' then
		if not apiobject.parent then
			-- This item reference a global definition
			return M.anchor( apiobject )
		else
			-- This item is related to a type
			return M.anchor( apiobject.parent ) .. '.' .. apiobject.name
		end
	elseif tag == 'file' or tag == 'recordtypedef' then
		return M.anchor( apiobject )
	end
end
return M
