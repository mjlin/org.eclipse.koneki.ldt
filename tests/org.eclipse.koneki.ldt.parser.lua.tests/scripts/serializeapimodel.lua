#!/usr/bin/lua
--
-- Generate serialized lua API models files next to given file.
--
require 'errnode'
local serializer = require 'serpent'
local apimodelbuilder = require 'models.apimodelbuilder'
if #arg < 1 then
	print 'No file to serialize.'
	return
end
for k = 1, #arg do

	-- Load source to serialize
	local filename = arg[k]
	local luafile = io.open(filename, 'r')
	local luasource = luafile:read('*a')
	luafile:close()

	-- Generate AST
	local ast, errormessage = getast( luasource )
	if not ast then
		print(string.format('Unable to generate AST for %s.\n%s', filename, errormessage))
	else
		--Generate API model
		local apimodel = apimodelbuilder.createmoduleapi(ast)

		-- Serialize model
		local serializedcode = serializer.serialize( apimodel )

		-- Define file name
		
		local serializedfilename = filename:gsub('([%w%-_/\]+)%.lua','%1.serialized')

		-- Save serialized model
		local serializefile = io.open(serializedfilename, 'w')
		serializefile:write( serializedcode )
		serializefile:close()

		-- This a success
		print( string.format('%s serialized to %s.', filename, serializedfilename) )
	end
end
