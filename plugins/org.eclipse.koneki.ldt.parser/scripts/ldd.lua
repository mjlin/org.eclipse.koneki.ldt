-------------------------------------------------------------------------------
-- Copyright (c) 2011 Sierra Wireless and others.
-- All rights reserved. This program and the accompanying materials
-- are made available under the terms of the Eclipse Public License v1.0
-- which accompanies this distribution, and is available at
-- http://www.eclipse.org/legal/epl-v10.html
--
-- Contributors:
--     Sierra Wireless - initial API and implementation
-------------------------------------------------------------------------------
require ('metalua.compiler')
local M = {}
---
-- Trim white spaces before and after given string
--
-- @usage local str = '          foo' str:trim()
-- @param string to trim
-- @return String trimmed
local function trim(string)
	local pattern = "^(%s*)(.*)"
	local _, strip =  string:match(pattern)
	if not strip then return string end
	local restrip
	_, restrip = strip:reverse():match(pattern)
	return restrip and restrip:reverse() or strip
end
local value = function(node)
	return node[1]
end
local function findmaintag(nlx)
	local parser = gg.sequence{
		name = 'Main Tag Parser',
		builder = function (x)
			local res = {}
			local parsed = value(x)
			if #parsed == 0 then
				-- It is a file
				res.tag = 'TFile'
			elseif #parsed == 1 then
				-- It's a record
				res.tag = 'TRecordtype'
				res.name = value(value(parsed))
			else
				-- It is a function
				res.tag = 'TFunction'
				-- When '#' is missing, must be a global function
				res.global = not parsed[1]
				res.typescope = value(parsed[2])
				res.name = value(parsed[3])
			end
			return res
		end,
		'@@',
		gg.multisequence{
			gg.sequence{ 'file' },
			gg.sequence{'recordtype', '#', mlp.id },
			gg.sequence{'function', '[', gg.optkeyword('#'), mlp.id, ']', mlp.id}
		}
	}
		nlx:add({"@@","file", "function", "recordtype", "field"})
	local result = parser(nlx)
	return {
		global = result.global,
		name = result.name,
		tag  = result.tag,
		typescope = result.typescope
	}
end
local reference = gg.sequence({
	builder = function(tks)
		-- No identifiers at all
		local ids = {}
		if #tks == 0 then return ids end
		-- List of prefix identifiers
		local scopeIds = tks[1]
		-- Last identifer
		local lastId = tks[2]
		-- Search for scope identifiers
		for _, id in ipairs(scopeIds) do
			table.insert(ids, id)
		end
		-- Append last identifier
		table.insert(ids, lastId)
		return #ids > 0 and ids
	end,
	gg.list({
		separators = '.',
		terminators = '#',
		mlp.id
	}),
	"#",
	mlp.id
})
local referencesList = gg.list({
	name = 'Reference List',
	primary  = reference,
	separators = ','

})
local returnBlock = gg.sequence({
	builder = function (ret)
		return ret[1] or false
	end,
	name = 'Return Documentation Statement Parser',
	'@', 'return', referencesList
})
local composetyperef = function(vars)
	local sequenceend
	local ref = { tag = 'TTyperef'}
	-- Deal with type less type reference
	if #vars == 0 then
		return ref, -1
	elseif #vars > 0 then
		-- Store type name
		local last = vars[#vars]
		ref.type = value( last )
		sequenceend = last.lineinfo.last.offset
	end
	-- Compose module name
	local tmp = {}
	local id = 1
	-- Storing names in an array with gaps
	for index = 1, (#vars-1) * 2 , 2 do
		tmp[index] = value(vars[id])
		id = id + 1
	end
	-- Inserting dots in gaps
	for index = 2, (#vars-2) * 2, 2 do
		tmp[index] = '.'
	end
	-- Retrieve full module name
	if #tmp > 0 then
		ref.module = table.concat(tmp)
	end
	return ref , sequenceend + 1
end
local function findreturntag(nlx)
	local result = returnBlock(nlx)
	local comment = {}
	local name, sequenceend
	if #result > 0 then
		comment.types = {tag = 'Types'}
	end
	for k = 1, #result do
		name, sequenceend = composetyperef(result[k])
		table.insert(comment.types, name)
	end
	return comment, sequenceend
end
local extractvarandref = function (x)
	local fieldtype = composetyperef(value(x))
	local nameend  = x[2].lineinfo.last.offset
	return {
		descriptionstart = nameend +1,
		name = value(x[2]),
		type = fieldtype
	}
end
local extractvar =function (x)
	local token = value(x)
	local name = value(token)
	return {
		descriptionstart = token.lineinfo.last.offset +1,
		name =name,
	}
end
local completefieldblock = gg.sequence({
	builder = extractvarandref,
	'@','field', reference, mlp.id
})
local fieldblock = gg.sequence({
	builder = extractvar,
	'@','field', mlp.id
})
local function findfieldtag(nlx)
	local x = fieldblock(nlx)
	local field = {	name = x.name }
	return field, x.descriptionstart
end
local function findtypedfieldtag(nlx)
	local x = completefieldblock(nlx)
	local field = {
		name = x.name, 
		type = x.type
	}
	return field, x.descriptionstart
end
local completeparamblock = gg.sequence({
	builder = extractvarandref,
	'@','param', reference, mlp.id
})
local paramblock = gg.sequence({
	builder = extractvar,
	'@','param', mlp.id
})
local findtypedparamtag = function(nlx)
	local x = completeparamblock(nlx)
	local p = {
		name = x.name, 
		type = x.type
	}
	return p, x.descriptionstart
end
local findparamtag = function(nlx)
	local x = paramblock(nlx)
	local p = {	name = x.name }
	return p, x.descriptionstart
end
function M.parse(string)
	string = trim(string)
	--
	-- Parse documentation from first tag
	--	
	local newline = string:find('[\n|\r|\r\n]+')
	local at =  string:find('@', newline, true) or #string
	-- Split description before first '@'
	local shortDescription = newline < at and string:sub(1, newline) or ""
	-- There is a long description
	local longDescription
	if at - newline > 2 then
		longDescription = string:sub(newline+1, at-1)
	else
		longDescription = shortDescription
	end
	-- Nothing more to parse
	local comment = {
		description = trim(longDescription),
		shortdescription = trim(shortDescription)
	}
	if not at then
		return comment
	end
	-- Cretate lexer used for parsing
	local lx = lexer.lexer:clone()
	lx.extractors = {
		--	"extract_long_comment", "extract_short_comment",
		--	"extract_long_string",
		"extract_short_string", "extract_word", "extract_number",
		"extract_symbol"
	}
	lx:add({"@@","file", "function", "recordtype", "return", "field", "param"})
	-- The rest of the comment should be special tags starting with '@'
	while at <= #string do
		-- Extract section to analyse
		local newline = string:find('[\n|\r|\r\n]+', at) or #string
		local stringtoparse = string:sub(at, newline)
		if stringtoparse:len() > 0 then
			-- Moving forward to next section
			at = newline + 1
			-- Detect main tags (file, function, recordtype)
			if stringtoparse:find('@@') then
				-- Parse tag
				local maintag = findmaintag(lx:newstream(stringtoparse, 'Main tags lexer'))
				if maintag then
					-- Append tag information to collection
					if not comment.maintags then comment.maintags = {} end
					table.insert(comment.maintags, maintag)
				end
			elseif stringtoparse:find('@return')then
				-- Search for a field
				local returntag, returns, offset = pcall(findreturntag, lx:newstream(stringtoparse, 'Return tags lexer'))
				if not returntag then
					returntag, returns, offset = pcall(findreturntag, lx:newstream(stringtoparse, 'Return tags lexer'))
				end
				if returntag then
					if not comment.returns then comment.returns = {} end
					local returndesc ={
						description = trim( stringtoparse:sub(offset + 1) ),
						types = returns.types
					}
					table.insert(comment.returns, returndesc)
				end
			elseif stringtoparse:find('@field')then
				local field, descriptionstart, x, valid = {}
				if stringtoparse:find('#') then
					valid,x, descriptionstart = pcall(findtypedfieldtag,lx:newstream(stringtoparse, 'Field tags lexer'))
					if valid then
						field.type = x.type
					else
						x = false
					end
				else
					valid, x, descriptionstart = pcall(findfieldtag, lx:newstream(stringtoparse, 'Field tags lexer'))
					if not valid then x = false end
				end
				if x then
					field.name = x.name
					field.description = trim(stringtoparse:sub(descriptionstart))
					field.tag = 'TField'
					if not comment.fields then comment.fields = {} end
					table.insert(comment.fields, field)
				end
			elseif stringtoparse:find('@param')then
				local param, descriptionstart, x, valid = {}
				if stringtoparse:find('#') then
					valid,x, descriptionstart = pcall(findtypedparamtag,lx:newstream(stringtoparse, 'Parameters tags lexer'))
					if valid then
						param.type = x.type
					else
						x = false
					end
				else
					valid, x, descriptionstart = pcall(findparamtag, lx:newstream(stringtoparse, 'Parameters tags lexer'))
					if not valid then x = false end
				end
				if x then
					param.name = x.name
					param.description = trim(stringtoparse:sub(descriptionstart))
					param.tag = 'TParam'
					if not comment.params then comment.params = {} end
					table.insert(comment.params, param)
				end
			end
		end
	end
	return comment
end
return M
