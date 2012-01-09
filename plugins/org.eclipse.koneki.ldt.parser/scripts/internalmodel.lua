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
local M = {}
function M._internalcontent()
	return {
		content = nil,
		unknownglobalvars = {},
		tag = "MInternalContent"
	}
end
function M._block()
	return {
		content = {},
		localvars = {},
		sourcerange = {min=0,max=0},
		tag = "MBlock"
	}
end
function M._identifier(item, range)
	return {
		definition= item,
		sourcerange = {min=0,max=0},
		tag = "MIdentifier"
	}
end
function M._index(key, value, range)
	return {
		left= key,
		right= value,
		sourcerange = {min=0,max=0},
		tag = "MIndex"
	}
end
--function M._sourcerange(min, max)
--	return {
--		min=min,
--		max=max,
--		tag="MSourceRange"
--	}
--end
function M._call(funct, range)
	return {
		func = funct,
		sourcerange = {min=0,max=0},
		tag = "MCall"
	}
end
function M._invoke(name, expr, range)
	return {
		functionname = name,
		record = expr,
		sourcerange = {min=0,max=0},
		tag = "MInvoke"
	}
end
return M
