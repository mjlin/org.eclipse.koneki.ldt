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

--
-- Defining help message.
--

-- This message is compliant to 'lapp', which will match options and arguments
-- from commande line.
local help = [[luadocumentor, documentation for Lua
	-e, --emptyfiles Will produce documentation only .lua files instead of html files
	-d, --dir (default docs) Output directory
	-h, --help This help.
	-s, --style (default !) File for style sheet (ldd.css)
	[dirs] Directories parsed for .lua files
]]
local generator = require 'docgenerator'
local lapp = require 'pl.lapp'
local args = lapp( help )

if not args or #args < 1 then
	print('No directory provided')
	return
elseif args.help then
	-- Just print help
	print( help )
	return
end

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

-- Some of given directories are absent
if missing then
	-- List missing directories
	print 'Unable to find'
	for _, file in ipairs( missing ) do
		print('\t'.. file)
	end
	return
end

-- Get files from given directories
local filestoparse, error = fs.filelist( args )
if not filestoparse then
	print ( error )
	return
end

--
-- Generate documentation only files
--
if args.emptyfiles then
	local extractor = require 'lddextractor'
	for _, filename in ipairs( filestoparse ) do

		-- Loading file content
		print('Dealing with "'..filename..'".')
		local file = io.open(filename, 'r')
		local code = file:read('*all')
		file:close()

		--
		-- Creating comment file
		--
		local commentfile, error = extractor.generatecommentfile(filename, code)

		-- Getting module name
		-- Optimize me
		local module, moduleerror = extractor.generateapimodule(filename, code)
		if not commentfile then
			print('Unable to create documentation file for "'..filename..'"\n'..error)
		elseif not module or not module.name then
			local error = moduleerror and '\n'..moduleerror or ''
			print('Unable to compute module name for "'..filename..'".'..error)
		else
			--
			-- Flush documentation file on disk
			--
			local path = args.dir..fs.separator..module.name..'.lua'
			local status, err = fs.fill(path, commentfile)
			if not status then
				print(err)
			end
		end
	end
	print('Done')
	return
end

-- Generate html form files
local parsedfiles, unparsed = generator.generatedocforfiles(filestoparse, args.style)

-- Show warnings on unparsed files
if #unparsed > 0 then
	for _, faultyfile in ipairs( unparsed ) do
		print( faultyfile )
	end
end
print (#parsedfiles .. ' file(s) parsed.')

-- Create html files
local generated = 0
for _, apifile in ipairs ( parsedfiles ) do
	local status, err = fs.fill(args.dir..fs.separator..apifile.name..'.html', apifile.body)
	if status then
		generated = generated + 1
	else
		print( 'Unable to create '..apifile.name..'.html on disk.')
	end
end
print (generated .. ' file(s) generated.')

-- Copying css
local css = io.open(args.style, 'r')
if not css then
	print('"'..args.style .. '" does not exist.')
	return
end
local csscontent = css:read("*all")
css:close()
local status, error = fs.fill(args.dir..fs.separator..args.style, csscontent)
if not status then
	print(error)
	return
end
print('Adding css')
print('Done')
