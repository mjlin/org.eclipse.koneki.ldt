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
local M = {}
local item        = java.require("org.eclipse.koneki.ldt.parser.api.external.Item")
local retvalues   = java.require("org.eclipse.koneki.ldt.parser.api.external.ReturnValues")
local recorddef   = java.require("org.eclipse.koneki.ldt.parser.api.external.RecordTypeDef")
local functiondef = java.require("org.eclipse.koneki.ldt.parser.api.external.FunctionTypeDef")
local parameter   = java.require("org.eclipse.koneki.ldt.parser.api.external.Parameter")
local etyperef    = java.require("org.eclipse.koneki.ldt.parser.api.external.ExternalTypeRef")
local ityperef    = java.require("org.eclipse.koneki.ldt.parser.api.external.InternalTypeRef")
local ptyperef    = java.require("org.eclipse.koneki.ldt.parser.api.external.PrimitiveTypeRef")
local fileapi     = java.require("org.eclipse.koneki.ldt.parser.api.external.LuaFileAPI")
local mtyperef    = java.require ("org.eclipse.koneki.ldt.parser.api.external.ModuleTypeRef")
local etyperef    = java.require ("org.eclipse.koneki.ldt.parser.api.external.ExprTypeRef")

function M.createtyperef (type)
	if not type then return nil end
	if type.tag == "externaltyperef" then
		return etyperef:new(type.modulename, type.typename)
	elseif type.tag == "internaltyperef" then
      return ityperef:new(type.typename)
   elseif type.tag == "moduletyperef" then
      return mtyperef:new(type.modulename,type.returnposition)
   elseif type.tag == "exprtyperef" then
      return etyperef:new(type.returnposition)
   else
      return ptyperef:new(type.typename) 
	end
end
function M.createitem(def)
	local i = item:new()
	i:setDocumentation(def.description)
	i:setName(def.name)
	-- Define optional type
	if def.type then
		i:setType(M.createtyperef(def.type))
	end
	return i
end
function M.createtypedef(name, definition)
	local def
	-- Dealing with records
	if definition.tag == "recordtypedef" then
		def = recorddef:new()
		def:setName(name)
		-- Appending fields
		for fieldname, item in pairs(definition.fields) do
			def:addField(fieldname, M.createitem(item))
		end
	else
		-- Dealing with function
		def = functiondef:new()
		-- Appending parameters
		for _, param in ipairs(definition.params) do
			def:addParameter( parameter:new(param.name, M.createtyperef(param.type), param.description) )
		end
		-- Appending returned types
		table.print(definition.returns)
		for _, value in ipairs(definition.returns) do
			local ret = retvalues:new()
			for _, type in ipairs( value.types ) do
			   table.print(type)
				ret:addType(M.createtyperef(type))
			end
			def:addReturn(ret)
		end
	end
	def:setDocumentation(definition.description)
	return def
end
---
-- Transpose Lua documentation model to Java
--
-- @param model Instance of Lua model
-- @result org.eclipse.koneki.ldt.parser.api.external#LuaFileAPI Java object
function M.createJAVAModel(model)
	--
	-- Fill file object
	--
	local file = fileapi:new()
	file:setDocumentation(model.description)
	-- Adding gloval variables
	for name, global in pairs(model.globalvars) do
		-- Fill Java item
		file:addGlobalVar(name, M.createitem(global))
	end
	-- Adding returned types
	for _, ret in ipairs(model.returns) do
		local values = retvalues:new()
		for _, type in ipairs(ret.types) do
			values:addType(M.createtyperef(type))
		end
		file:addReturns(values)
	end
	-- Adding types defined in files
	for name, def in pairs(model.types) do
		file:addType(name, M.createtypedef(name ,def))
	end
	return file
end
return M
