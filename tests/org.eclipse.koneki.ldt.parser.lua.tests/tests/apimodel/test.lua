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

	--
	-- Load provided reference
	--
	local luareferenceloadingfunction = loadfile(serializedreferencepath)
	if not luareferenceloadingfunction then
		return nil, string.format('Unable to load reference from %s.', serializedreferencepath)
	end
	local referenceapimodel = luareferenceloadingfunction()

	-- Check that they have references at same place
	local equivalent, message = tablecompare.compare(apimodel, referenceapimodel)
	if not equivalent then

		-- Compute which keys differs
		local differentkeys = tablecompare.diff(apimodel, referenceapimodel)
		local differentkeysstring = table.tostring(differentkeys)
		return nil, string.format('%s\nDifferent keys are:\n%s', message, differentkeysstring)

	end
	return true
end
return M
