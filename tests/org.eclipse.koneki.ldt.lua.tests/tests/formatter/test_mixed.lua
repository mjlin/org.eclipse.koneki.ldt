require 'errnode'
local formatter = require 'luaformatter'
local string = require 'string'
local assert = java.require("org.junit.Assert")

local M = {}
function M.test(luainputpath, luareferencepath)

	-- Load provided source
	local luafile, errormessage = io.open(luainputpath, 'r')
	if not luafile then
		return nil, string.format('Unable to read from %s.\n%s', luainputpath, errormessage)
	end
	local luasource = luafile:read('*a')
	luafile:close()

	-- format code
	local formattedCode = formatter.indentCode(luasource, '\n', 3, 4, 0, true)
	if not formattedCode then
		return nil, string.format('Unable to format %s.\n', luainputpath)
	end

	-- Load provided reference
	local referenceFile, errormessage = io.open(luareferencepath)
	if not referenceFile then
		return nil, string.format('Unable to read reference from %s.\n%s', luareferencepath, errormessage)
	end
	local referenceCode = referenceFile:read('*a')

	-- Check equality by catching assertException from assertEquals
	local status, errormessage = pcall( function()
		assert:assertEquals("Formatting Error", referenceCode, formattedCode)
	end)
	return status or nil, tostring(errormessage)
end
return M
