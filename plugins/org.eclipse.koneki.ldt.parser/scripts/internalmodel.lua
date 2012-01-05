--------------------------------------------------------------------------------
--  Copyright (c) 2011-2012 Sierra Wireless.
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
function M.newinternalcontent(block, vars)
	return {
		content = block,
		unknownglobalvars = vars,
		tag = "MInternalContent"
	}
end
function M.newblock(block, vars, range)
	return {
		content = block,
		localvars = vars,
		sourcerange = range,
		tag = "MBlock"
	}
end
function M.newidentifier(item, range)
	return {
		definition= item,
		sourcerange = range,
		tag = "MIdentifier"
	}
end
function M.newindex(key, value, range)
	return {
		left= key,
		right= value,
		sourcerange = range,
		tag = "MIndex"
	}
end
function M.newsourcerange(min, max)
	return {
		min=min,
		max=max,
		tag="MSourceRange"
	}
end
function M.newcall(funct, range)
	return {
		func = funct,
		sourcerange = range,
		tag = "MCall"
	}
end
function M.newinvoke(name, expr, range)
	return {
		functionname = name,
		record = expr,
		sourcerange = range,
		tag = "MInvoke"
	}
end
return M
