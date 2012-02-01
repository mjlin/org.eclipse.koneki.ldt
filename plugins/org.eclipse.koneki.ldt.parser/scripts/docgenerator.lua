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

-- Load template engine
local pltemplate = require 'pl.template'

-- Markdown handling
local markdown = function (string)
   local m = require 'markdown.markdown'
   local result = m( string )
   return result:gsub('^%s*<p>(.+)</p>%s*$','%1')
end



-- apply template to the given element
function M.applytemplate(elem,templatetype)
	-- define environment
	local env = M.getenv(elem,templatetype)

	-- load template
	local template = M.gettemplate(elem,templatetype)
	if not template then
		templatetype = templatetype and ' "'.. templatetype.. '"' or ''
		local elementname = ' for '.. (elem.tag or 'untagged element')
		error('Unable to load'..templatetype..' template'..elementname)
	end

	-- apply template
	local str, err = pltemplate.substitute(template, env)

	--manage errors
	if not str then
		local templateerror = templatetype and ' parsing "'.. templatetype ..'" template ' or ''
		error('An error occured' ..templateerror ..' for "'..elem.tag..'"\n'..err)
	end
	
	return str
end

-- get the a new environment for this element
function M.getenv(elem)
   local currentenv ={}
   for k,v in pairs(M.env) do currentenv[k] = v end
   if elem and elem.tag then
      currentenv['_'..elem.tag]= elem
   end
   return currentenv
end

-- get the template for this element
function M.gettemplate(elem,templatetype)
   local tag = elem and elem.tag
   if tag then
      if templatetype then
         return require ("template." .. templatetype.. "." .. tag)
      else
         return require ("template." .. tag)
      end
   end
end
local magiclist =  function (tab)
	if not tab then return '' end
	-- Determine type of given elements
	local type = tab.tag
	if not type then return 'Elements given to magiclist() have no tags' end
	-- Initialise list parameters
	local inputelements, separator, first, last
	if type == 'functiontypedef' then
		separator =', '
		first = '('
		last = ')'
		-- Output list will be a parameter list
		inputelements = tab.params
	elseif type == 'return' then
		separator = ', '
		first = ''
		last = ''
		inputelements = tab.types
	else
		return 'No magic for '..type
	end
	-- Append list beginning
	local list = {first}

	-- Build value list with gaps for separators
	for i=1, #inputelements do
		-- Format value depending on type
		local currentelement = inputelements[i]
		local value
		if currentelement.tag == 'param' then
			value = currentelement.name
		elseif currentelement.tag == 'externaltyperef' then
			value = currentelement.modulename ..'#'..currentelement.typename
		elseif currentelement.tag == 'internaltyperef' or currentelement.tag == 'internaltyperef' then
			value = '#'..currentelement.typename
		end
		list[ 2*i ] = value
	end

	-- Fill gaps with separators
	for i=1, #inputelements-1 do
		list[ 2*i + 1 ] = separator
	end
	-- Append list ending
	table.insert(list, last)
	return table.concat(list)
end
	
-- define default template environnement
local defaultenv = {
	table		= table,
	ipairs		= ipairs,
	pairs		= pairs,
	markdown	= markdown,
	applytemplate	= M.applytemplate,
	isempty	= function(map)
		local f = pairs(map)
		return f(map) == nil
	end,
	magiclist = magiclist
}

-- this is the global env accessible in the templates
-- env should be redefine by docgenerator user to add functions or redefine it.
M.env = defaultenv
return M
