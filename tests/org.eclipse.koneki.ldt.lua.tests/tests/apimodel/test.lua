-------------------------------------------------------------------------------
-- Copyright (c) 2012 Sierra Wireless and others.
-- All rights reserved. This program and the accompanying materials
-- are made available under the terms of the Eclipse Public License v1.0
-- which accompanies this distribution, and is available at
-- http://www.eclipse.org/legal/epl-v10.html
--
-- Contributors:
--     Sierra Wireless - initial API and implementation
-------------------------------------------------------------------------------
require 'errnode'
local apimodelbuilder = require 'models.apimodelbuilder'
local tablecompare = require 'tablecompare'
--local function f(...) print(...) io.flush() end
--local print = f

local M = {}
function M.test(luasourcepath, serializedreferencepath)

	--
	-- Load provided source
	--
	local luafile, errormessage = io.open(luasourcepath, 'r')
	if not luafile then
		return nil, string.format('Unable to read from %s.\n%s', luasourcepath, errormessage)
	end
	local luasource = luafile:read('*a')
	luafile:close()

	-- Generate AST
	local ast, errormessage = getast( luasource )
	if not ast then
		return nil, string.format('Unable to generate AST for %s.\n%s', luasourcepath, errormessage)
	end

	--
	-- Generate API model
	--
	local apimodel = apimodelbuilder.createmoduleapi(ast)
	apimodel = tablecompare.stripfunctions(apimodel)

	--
	-- Load provided reference
	--
	local luareferenceloadingfunction = loadfile(serializedreferencepath)
	if not luareferenceloadingfunction then
		return nil, string.format('Unable to load reference from %s.', serializedreferencepath)
	end
	local referenceapimodel = luareferenceloadingfunction()

	-- Check that they are equivalent
	local equivalent = tablecompare.compare(apimodel, referenceapimodel)
	if #equivalent > 0 then

		-- Compute which keys differs
		local differentkeys = tablecompare.diff(apimodel, referenceapimodel)
		local differentkeysstring = table.tostring(differentkeys)
		
		-- Formalise first table output
		local _ = '_'
		local line = _:rep(80)
		local firstout   = string.format('%s\nFirst table\n%s\n%s', line, line, table.tostring(apimodel, 1))
		local secondout  = string.format('%s\nSecond table\n%s\n%s', line, line, table.tostring(referenceapimodel, 1))
		return nil, string.format('Keys which differ are:\n%s\n%s\n%s', differentkeysstring, firstout, secondout)

	end
	return true
end
return M
