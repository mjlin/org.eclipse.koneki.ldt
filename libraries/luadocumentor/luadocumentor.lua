#!/usr/bin/lua
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
-- Updating Lua path
--package.path = 'doc/?.lua;doc/?.luac;' .. package.path
--package.path = '/d/penlight/lua/?.lua;' .. package.path
--package.path = '/d/metalua/build/lib/?.luac;/d/metalua/build/lib/?.lua;' .. package.path
-- Should move elsewhere
require 'metalua.compiler'

--
-- Defining help message.
--
-- This message is compliant to 'lapp', which will match options and arguments
-- from commande line.
local help = [[ldd, documentation for Lua
	-c, --convert
	-d, --dir (default docs) output directory
	-h, --help This help.
	-s, --style (default !) directory for style sheet (ldd.css)
	[dirs] Directories parsed for .lua files
]]
local parser = require 'doc.docgenerator'
local lapp = require 'pl.lapp'
local args = lapp( help )

if not args then
	print('No directory provided')
	return
elseif args.help then
-- Just print help
	print( help )
elseif #args > 0 then

	-- Sorting out css
	if args.style == '!' then
		-- Use our default css
		args.style = 'default.css'
	end

	--
	-- Parse files from given folders
	--

	-- Check if all folders exist
	local fs = require 'fs.lfs'
	local allpresent, missing = fs.checkdirectory(args)
	if allpresent then
		-- Get files from given directories
		local filestoparse = fs.filelist( args )
		-- Generate html form files
		local parsedfiles, err = parser.generatedocforfiles(filestoparse, args.style)
		if not parsedfiles then
			return err
		else
			print (#parsedfiles .. ' file(s) parsed.')
		end
		
		-- Create html files
		local status, err = fs.create(args.dir, parsedfiles)
		if status then
			print (#parsedfiles .. ' file(s) generated.')
		else
			print( err )
			return
		end

		-- Copying css
		local css = io.open(args.style, 'r')
		if not css then return args.style .. 'does not exists.' end
		local csscontent = css:read("*all")
		css:close()
		local status, error = fs.fill(args.dir..fs.separator..args.style, csscontent)
		if status then
			print('Adding css')
		else
			print(error)
			return
		end
		print('Done')
	else
		-- List missing directories
		print 'Unable to find'
		for _, file in ipairs( missing ) do
			print('\t'.. file)
		end
	end
end
