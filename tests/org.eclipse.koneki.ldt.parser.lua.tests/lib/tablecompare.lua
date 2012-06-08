---
-- @module tablecompare
--
local M = {}

local function checks() end

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

--------------------------------------------------------------------------------
-- Cleans a path.
--
-- Removes trailing/preceding/doubling '.'.
--
-- @function [parent=#tablecompare] clean
-- @param path string containing the path to clean.
-- @return cleaned path as a string.
--
function M.clean(path)
	checks('string')
	local p = M.segments(path)
	return pathconcat(p)
end

--
-- @function [parent=#tablecompare] recursivePairs
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
-- Splits a path into segments.
--
-- Each segment is delimited by '.' pattern.
--
-- @function [parent=#tablecompare] segments
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

---
-- @function [parent=#tablecompare] recursivepairs
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
---
-- @function [parent=#tablecompare] compare
-- @param #table t1
-- @param #table t2
--
local ignoredtypes = {
	['function'] = true,
	['thread']   = true,
	['userdata'] = true,
}
function M.compare(t1, t2)

	-- Build t1 copy
	local t3 = {}
	for k,v in M.recursivepairs(t1) do
		t3[k] = v
	end

	-- Browse recursively for differences
	local differences = {}
	for k, v in M.recursivepairs( t2 ) do
		print( string.format('t1 k %s.', k))
		local t3valuetype = type( t3[k] )
		local t2valuetype = type( v )

		-- Values are different when their type differ
		if t3valuetype ~= t2valuetype then
			table.insert(differences, k)
		elseif not ignoredtypes[t3valuetype] and v ~= t3[k] then
			-- Same type but different values
			table.insert(differences, k)
		end
		t3[k] = nil
	end

	-- Loacate t2 keys which are not in t1
	for k, v in M.recursivepairs( t3 ) do
		table.insert(differences, k)
	end
	return differences
end

---
-- @function [parent=#tablecompare] hassamestructure
-- @param #table t1
-- @param #table t2
-- @param #table t1references
-- @param #table t2references
--
function M.hassamestructure(t1, t2, t1references, t2references)

	-- Secure parameters
	t1references = t1references or {}
	t2references = t2references or {}

	--
	-- Seek for references
	--
	local samereferences = true
	local errormessage
	local key, t1v = next(t1)
	while samereferences and key do

		-- Check only tables
		local typev = type(t1v)
		local t2v = t2[key]
		local t2vtype = type(t2v)
		if t2vtype ~= typev then
			
			samereferences = false
			errormessage = string.format("Values type differ on key %s first table is a %s and second table is a %s.", 
				tostring(key), typev, t2vtype)

		elseif typev == 'table' then

			-- Register those tables, if we encounter them later, we know they
			-- are self references.
			if not ( t1references[t1v] and t2references[t2v] ) then
				t1references[t1v] = true
				t2references[t2v] = true
				if not M.hassamestructure(t1v, t2v, t1references, t2references) then
					samereferences = false
					errormessage = string.format('Tables do not share reference on key %s.', key)
				end

			-- Reference only in t1
			elseif t1references[t1v] and not t2references[t2v] then
				samereferences = false
				errormessage = string.format('First table is a reference and second one is not.')

			-- Reference only in t2
			elseif not t1references[t1v] and t2references[t2v] then
				samereferences = false
				errormessage = string.format('First table is not a reference and second one is.')
			end
		end

		-- Move to next key
		key, t1v = next(t1, key)
	end

	-- Stop here if keys did not match
	if not samereferences then
		return false, errormessage
	end

	-- Check that second table does not contains keys fist table does not.
	for k, v in pairs(t2) do
		if t1[k] == nil then
			return false, string.format('Second table contains key %s, first one does not.', tostring(k))
		end
	end
	return true
end
return M
