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
---
-- This library provide html description of elements from the externalapi
local M = {}

-- Load templates, they are strings to fill
local typedeftemplate =		require 'template.typedef'
local filetemplate =		require 'template.file'
local htmltemplate =		require 'template.html'
local itemtemplate =		require 'template.item'
local functiontemplate =	require 'template.function'
local recordtemplate =		require 'template.record'
local returntemplate =		require 'template.return'
local indextemplate  =		require 'template.index'
local lateraltemplate =		require 'template.lateralindex'

-- Load template engine
local pltemplate = require 'pl.template'

-- Markdown handling
local markdown = function (string)
	local m = require 'markdown.markdown'
	local result = m( string )
	return result:gsub('^%s*<p>(.+)</p>%s*$','%1')
end

function M.index( modules )
	local modtable = {
		ipairs = ipairs,
		modules = modules,
		markdown = markdown
	}
	local html, error = pltemplate.substitute(indextemplate, modtable)
	if error then
		print ( 'Generating index:' )
		table.print(modules, 1)
		print ( error )
		io.flush()
	end
	return html
end
function M.lateralmenu( modules )
	local modtable = {
		ipairs = ipairs,
		modules = modules
	}
	local html, error = pltemplate.substitute(lateraltemplate, modtable)
	if error then
		print ( 'Generating lareral index:' )
		table.print(modules, 1)
		print ( error )
		io.flush()
	end
	return html
end
---
-- Transfor a hash map in list
-- <code>tolist({foo = 'bar'}) is equivalent to <code>{'bar'}</code>
-- @param map Table to transform
-- @result Tasformed table
-- @result #nil, #string Provide an error message in case of failure
local function tolist(map)
	if type(map) ~= 'table' then
		return nil, 'Table is required'
	end
	local tab = {}
	for _, value in pairs(map) do
		table.insert(tab, value)
	end
	return tab
end
---
-- Generates html description for 'return node
-- @param `return node
-- @return #string HTML description of given node
local returnstring = function ( ret )
	local returntable = {
		ipairs		= ipairs,
		markdown	= markdown,
		oreturns	= ret,
	}
	local str, err = pltemplate.substitute(returntemplate, returntable)
	if err then
		print ( 'Generating return template:' )
		table.print(ret, 1)
		print ( err )
		io.flush()
	end
	return str
end
---
-- Generates html description for type definition nodes
-- * recorddef
-- * functiondef
-- This function will use descriptions of given nodes and append the
-- results of M.func or M.record depending of given node type.
-- @param #string name associated to definition
-- @param typed Node to describe
-- @param parentname name of type's parent module, used for link generation
--		Used to match human redeable definition names.
-- @return #string HTML description of given node
function M.typedef( name, typed, types, parentname)
	local templatetable = {
		concat = table.concat,
		description = typed.description,
		fields = typed.fields,
		ipairs = ipairs,
		itemstring = M.item,
		markdown = markdown,
		name = name,
		pairs = pairs,
		params = typed.params,
		parentname = parentname,
		returns = typed.returns,
		returnstring = returnstring,
		shortdescription = typed.shortdescription,
		types = types,
	}
	local html, err = pltemplate.substitute(typedeftemplate, templatetable)
	if err then
		print ( 'Generating typedef template:' )
		table.print(typed,1)
		print ( err )
		io.flush()
	end
	local specific, err
	if typed.tag == 'functiontypedef' then
		specific, err = M.func(templatetable)
	else
		specific, err = M.record(templatetable, types)
	end
	if not specific then
		print('Nothing computed for '.. typed .tag)
	end
	if err then
		print ( 'Generating '.. typed.tag .. ' template:')
		table.print(typed,1)
		print ( err )
		io.flush()
	end
	return html..specific
end
---
-- Will generate HTML documentation for a `file node.
-- This method will rely on other methods from this module:
-- * M.item
-- * M.typedef
-- @param file from external api to describe
-- @return #string HTML code describing given object
function M.file(file)
	local filetable ={
		concat			= table.concat,
		description		= file.description,
		globalvars		= file.globalvars,
		ipairs			= ipairs,
		itemstring		= M.item,
		markdown		= markdown,
		pairs			= pairs,
		returns 		= file.returns,
		returnstring	= returnstring,
		shortdescription= file.shortdescription,
		tolist			= tolist,
		typedefstring	= M.typedef,
		types			= file.types
	}
	local html, error = pltemplate.substitute(filetemplate, filetable)
	if error then
		print ('Generating file template:')
		table.print(file, 1)
		print (error)
		io.flush()
	end
	-- Flush generated HTML in a temporary file, allow to preview file in a browser
	-- For testing purpose
--	if html then
--		local file = io.open('/tmp/docdebug.html', 'w')
--		file:write(html)
--		file:close()
--	end
	return html
end
---
-- Generate HTML documentation for a `functiontypedef node.
-- @param functiontypedef from external api to describe
-- @return #string HTML code describing given object
function M.func(fun)
	local f = {
		func			= fun,
		ipairs			= ipairs,
		markdown		= markdown,
		parentname		= fun.parentname,
		returnstring	= returnstring,
	}
	local html, err = pltemplate.substitute(functiontemplate, f)
	if err then
		print ('Generating function template :')
		table.print(f, 1)
		print(err)
		io.flush()
	end
	return html
end
--- Unused so far
function M.page(context)
	local c = {
		ipairs	= ipairs,
		body	= context.body,
		head	= context.head
	}
	local page, error = pltemplate.substitute(htmltemplate, c)
	if not page then
		print ('Generating page template :')
		print(error)
		table.print(c, 1)
		io.flush()
	end
	return page
end
---
-- Generate HTML documentation for a `item node.
-- @param `item from external api to describe
-- @return #string HTML code describing given object
function M.item(it, name)
	local itemtable = {
		item		= it,
		markdown	= markdown,
	}
	if name then
		itemtable.item.name = name
	end
	local html, err = pltemplate.substitute(itemtemplate, itemtable)
	if err then
		table.print(it, 1)
		print ('Parsing item: '..err)
		io.flush()
	end
	return html
end
---
-- Generate HTML documentation for a `recordtypedef node.
-- @param `recordtypedef from external api to describe
-- @types types defined in current module, use to resolve type names
-- @return #string HTML code describing given object
function M.record(record, types)
	local rectable = {
		ipairs		= ipairs,
		itemstring	= M.item,
		funcstring	= M.func,
		pairs		= pairs,
		parentname	= record.parentname,
		record		= record,
		types		= types
	}
	local html, err = pltemplate.substitute(recordtemplate, rectable)
	if err then
		table.print(rectable, 1)
		print ('Parsing record: '..err)
		io.flush()
	end
	return html
end
return M
