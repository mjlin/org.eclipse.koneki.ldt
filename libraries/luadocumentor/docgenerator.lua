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
require 'metalua.compiler'
local builder = require 'apimodelbuilder'
--
-- Load documentation generator and update its path
--
local docgen  = require 'templateengine'
for name, def in pairs( require 'utils' ) do
	docgen.env [ name ] = def
end
local M = {}
M.defaultsitemainpagename = 'index'
local function generateapielementforfile(filepath)
	-- Get ast from file
	local status, ast = pcall(mlc.luafile_to_ast, filepath)
	--
	-- Detect errors
	--
	if not status then return nil, ast end
	local status, error = pcall(mlc.check_ast, ast)
	if not status then return nil, error end

	-- Create API module
	local apimodule = builder.createmoduleapi(ast)
	if apimodule and apimodule.name then
		return apimodule, apimodule.name
	end
	return nil, 'No module name provided for '..filepath
end
local function generateapielementforcfile( filepath )
	
end
function M.generatedocforfiles(filenames, cssname)
	if not filenames then return nil, 'No files provided' end
	--
	-- Generate API model elements for all files
	--
	local generatedfiles = {}
	for _, filename in pairs( filenames ) do
		local file, name = generateapielementforfile( filename )
		if file then
			table.insert(generatedfiles, file)
		else
			-- Return error string
			return nil, name
		end
	end
	--
	-- Defining index, which will summarize all modules
	--
	local index = {
		modules = generatedfiles,
		name = M.defaultsitemainpagename,
		tag='index'
	}
	table.insert(generatedfiles, index)

	--
	-- Define page cursor
	--
	local pagecursor = {
		currentmodule = nil,
		headers = { [[<link rel="stylesheet" href="]].. cssname ..[[" type="text/css"/>]] },
		modules = generatedfiles,
		tag = 'page'
	}

	--
	-- Iterate over modules, generating complete doc pages
	--
	for _, module in ipairs( generatedfiles ) do
		-- Update current cursor page
		pagecursor.currentmodule = module
		-- Generate page
		local content, error = docgen.applytemplate(pagecursor)
		if not content then return nil, error end
		module.body = content
	end
	return generatedfiles
end
return M
