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
local M  = {}
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
local findIdentifier= function(identifierName)
	return function (lx)
		repeat tk = lx:next()
		until value(tk) == identifierName or tk.tag == 'Eof'
		return tk ~='Eof'
	end
end
local findAt = function (lx)
	local tk
	repeat
		tk = lx:next()
	until lx:is_keyword(tk, '@') or tk.tag == 'Eof'
	return tk.tag~='Eof'
end
local passTaggedResult = function (lx)
	local tagged = {}
	for _, result in ipairs(lx) do
		if type(result) == 'table' and result.tag then
			table.insert(tagged, result)
		end
	end
	return #tagged > 0 and tagged
end
local function findmaintag(nlx)
	local parser = gg.sequence{
		name = 'Main Tag Parser',
		builder = function (x)
			local res = {}
			if #x == 1 then
				-- It is a file
				res.kind = 'file'
			end
			return res
		end,
		'@@',
		gg.multisequence{
			gg.sequence{ 'file' },
			gg.sequence{'recordtype', '[', '#', mlp.id, ']'},
			gg.sequence{'function', '[', gg.optkeyword('#'), mlp.id, ']', mlp.id}
		}
	}
		nlx:add({"@@","file", "function", "recordtype"})
	local result = parser(nlx)
	return {
		kind = result.kind,
		tag  = result.kind:sub(1,1):upper().. result.kind:sub(2):lower()
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
local function findreturntag(nlx)
	local result = returnBlock(nlx)
	local comment = {}
	local sequenceend
	for k = 1, #result do
		local vars = result[k]
		local tmp = {}
		for id = 1, #vars do
			if id == #vars then
				table.insert(tmp, '#')
				sequenceend = vars[id].lineinfo.last.offset
			end
			table.insert(tmp, vars[id][1])
			if id < #vars - 1 then
				table.insert(tmp, '.')
			end
		end
		if not comment.types then comment.types = {tag = 'Types'} end
		table.insert(comment.types, table.concat(tmp))
	end
	return comment, sequenceend
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
	-- The rest of the comment should be special tags starting with '@'
	while at <= #string do
		-- Extract section to analyse
		local newline = string:find('[\n|\r|\r\n]+', at) or #string
		local stringtoparse = string:sub(at, newline)
		if stringtoparse:len() > 0 then
			-- Moving forward to next section
			at = newline + 1
--			print (stringtoparse) 
			-- Cretate lexer used for parsing
			local lx = lexer.lexer:clone()
			lx.extractors = {
				--	"extract_long_comment", "extract_short_comment",
				--	"extract_long_string",
				"extract_short_string", "extract_word", "extract_number",
				"extract_symbol"
			}
			lx:add({"@@","file", "function", "recordtype", "return"})
			-- Detect main tags (file, function, recordtype)
			if stringtoparse:find('@@') then
				-- Parse tag
				local maintag = findmaintag(lx:newstream(stringtoparse, 'Main tags lexer'))
				if maintag then
					-- Append tag information to collection
					if not comment.maintag then comment.maintag = {} end
					table.insert(comment.maintag, maintag)
				end
			else
				local valid, returns, offset = pcall(findreturntag, lx:newstream(stringtoparse, 'Return tags lexer'))
				if valid then
					if not comment.returns then comment.returns = {} end
					local returndesc ={
						description = trim( stringtoparse:sub(offset + 1) ),
						types = returns.types,
						tag = 'Returns'
					}
					table.insert(comment.returns, returndesc)
				end
			end
		end
	end
	return comment
end
return M
