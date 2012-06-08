require 'errnode'
local apimodelbuilder = require 'models.apimodelbuilder'
local tablecompare = require 'tablecompare'
local M = {}
local function f(...) print(...) io.flush() end
local print = f

function M.recut(luasourcepath, serializedreferencepath)

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

	-- Compare  generated API model to reference one
--	local diff = tablecompare.diff(apimodel, referenceapimodel)
--	if #diff > 0 then
--		return nil, table.tostring(diff)
--	end
	
	-- Check that they have references at same place
	local equivalent, message = tablecompare.compare(apimodel, referenceapimodel)
	if not equivalent then
		return nil, message
	end
	return true
end
return M
