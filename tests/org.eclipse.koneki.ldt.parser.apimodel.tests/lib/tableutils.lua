require 'errnode'
local apimodelbuilder = require 'models.apimodelbuilder'

local M = {}
local function f(...) print(...) io.flush() end
local print = f
local function pathconcat(pt, starti, endi)
	local t = {}
	local prev
	local empties = 0
	starti = starti or 1
	endi = endi or #pt

	for i = starti, endi do
		local v = pt[i]
		if not v then break
		elseif v == '' then
			empties = empties+1
		else
			table.insert(t, prev)
			prev = v
		end
	end
	table.insert(t, prev)
	--log('PATH', 'INFO', "pathconcat(%s, %d, %d) generates table %s, wants indexes %d->%d",
	--    sprint(pt), starti, endi, sprint(t), 1, endi-starti+1-empties)
	return table.concat(t, '.', 1, endi-starti+1-empties)
end
local function checks() end
--------------------------------------------------------------------------------
-- Cleans a path.
--
-- Removes trailing/preceding/doubling '.'.
--
-- @function [parent=#tableutils] clean
-- @param path string containing the path to clean.
-- @return cleaned path as a string.
--
function M.clean(path)
	checks('string')
	local p = M.segments(path)
	return pathconcat(p)
end

--
-- @function [parent=#utils.table] recursivePairs
-- @param t table to iterate.
-- @param prefix path prefix to prepend to the key path returned.
-- @return iterator function.
-- @usage {toto={titi=1, tutu=2}, tata = 3, tonton={4, 5}} will iterate through
-- ("toto.titi",1), ("toto.tutu",2), ("tata",3) ("tonton.1", 4), ("tonton.2"=5)
--

function M.recursivepairs(t, prefix)
	checks('table', '?string')
	local function it(t, prefix, cp)
		cp[t] = true
		local pp = prefix == "" and prefix or "."
		for k, v in pairs(t) do
			k = pp..tostring(k)
			if type(v) == 'table' then
				if not cp[v] then it(v, prefix..k, cp) end
			else
				coroutine.yield(prefix..k, v)
			end
		end
		cp[t] = nil
	end

	prefix = prefix or ""
	return coroutine.wrap(function() it(t, M.clean(prefix), {}) end)
end
--------------------------------------------------------------------------------
-- Produces a diff of two tables.
--
-- Recursive diff by default.
--
-- @function [parent=#tableutils] diff
-- @param t1 first table to compare.
-- @param t2 second table to compare.
-- @param norecurse boolean value to disable recursive diff.
-- @return the diff result as a table.
-- @usage
--> t1 = { a = 1, b=3, c = "foo"}
--> t2 = { a = 1, b=3, c = "foo"}
--> :diff(t1, t2) --= { }
--> t1 = { a = 1, b=3, c = "foo"}
--> t2 = { a = 1, b=4, c = "foo"}
--> :diff(t1, t2) --= { "b" }
--> t1 = { a = 1, b=3, c = "foo"}
--> t2 = { a = 1, b=3} --> :diff(t1, t2) --= { "c" }
--> t1 = { b=3, c = "foo", d="bar"}
--> t2 = { a = 1, b=3, c = "foo"}
--> :diff(t1, t2)
--= {
-- "a",
-- "d" }
--
function M.diff(t1, t2, norecurse)
	local d = {}
	local t3 = {}
	local rpairs = norecurse and pairs or M.recursivepairs
	for k, v in rpairs(t1) do t3[k] = v end
	for k, v in rpairs(t2) do
		if v ~= t3[k] then
--			 print(string.format("Difference for key %s, v1=%s v2=%s", tostring(k), tostring(t3[k]), tostring(v)))
			table.insert(d, k)
		end
		t3[k] = nil
	end
	for k, v in pairs(t3) do
		table.insert(d, k)
	end
	return d
end

--------------------------------------------------------------------------------
-- Splits a path into segments.
--
-- Each segment is delimited by '.' pattern.
--
-- @function [parent=#tableutils] segments
-- @param path string containing the path to split.
-- @return list of split path elements.
--
function M.segments(path)
	checks('string')
	local t = {}
	local index, newindex, elt = 1
	repeat
		newindex = path:find(".", index, true) or #path+1 --last round
		elt = path:sub(index, newindex-1)
		elt = tonumber(elt) or elt
		if elt and elt ~= "" then table.insert(t, elt) end
		index = newindex+1
	until newindex==#path+1
	return t
end

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
	local diff = M.diff(apimodel, referenceapimodel)
	--	print '\n\n *** apimodel: ***'
	--	table.print(apimodel, 60)
	--	print '\n\n *** referenceapimodel: ***'
	--	table.print(referenceapimodel, 60)
	--	print '\n\n *** diff: ***'
	--	table.print(diff, 60)
	if #diff > 0 then
		return nil, table.tostring(diff)
	end
	return true
end
return M
