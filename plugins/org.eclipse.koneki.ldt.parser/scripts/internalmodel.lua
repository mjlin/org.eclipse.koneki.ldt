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
function M._internalcontent(block, vars)
	return {
		content = block,
		unknownglobalvars = vars,
		tag = "MInternalContent"
	}
end
function M._block(chunk, vars, range)
	return {
		content = chunk,
		localvars = vars,
		sourcerange = range,
		tag = "MBlock"
	}
end
function M._identifier(item, range)
	return {
		definition= item,
		sourcerange = range,
		tag = "MIdentifier"
	}
end
function M._index(key, value, range)
	return {
		left= key,
		right= value,
		sourcerange = range,
		tag = "MIndex"
	}
end
function M._sourcerange(min, max)
	return {
		min=min,
		max=max,
		tag="MSourceRange"
	}
end
function M._call(funct, range)
	return {
		func = funct,
		sourcerange = range,
		tag = "MCall"
	}
end
function M._invoke(name, expr, range)
	return {
		functionname = name,
		record = expr,
		sourcerange = range,
		tag = "MInvoke"
	}
end
return M
