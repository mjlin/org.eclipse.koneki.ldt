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
local J = {}
local externalapi = require 'externaljavaapi'

local block =           java.require 'org.eclipse.koneki.ldt.parser.ast.Block'
local call  =           java.require 'org.eclipse.koneki.ldt.parser.ast.Call'
local identifier =      java.require 'org.eclipse.koneki.ldt.parser.ast.Identifier'
local index =           java.require 'org.eclipse.koneki.ldt.parser.ast.Index'
local invoke =          java.require 'org.eclipse.koneki.ldt.parser.ast.Invoke'
local internalcontent = java.require 'org.eclipse.koneki.ldt.parser.ast.LuaInternalContent'
local localvar        = java.require 'org.eclipse.koneki.ldt.parser.ast.LocalVar'
function J._block(content)
	-- Setting source range
	local chunk = block:new()
	chunk:setStart(content.sourcerange.min)
	chunk:setEnd(content.sourcerange.max)
	for _, vardef in pairs(content.localvars) do
		-- Create Java item
		local item = externalapi.createitem(vardef.item)		
		-- Append Java local variable definition
		local var  = localvar:new(item, vardef.scope.min, vardef.scope.max)
		chunk:addLocalVar(var)
	end
	-- Append nodes to block
	for _, expr in pairs(content.content) do
		local node = J._expression(expr)
		chunk:addContent(node)
	end
	return chunk
end
function J._call(expr)
	local jcall = call:new()
	jcall:setStart(expr.sourcerange.min)
	jcall:setEnd  (expr.sourcerange.max)
	jcall:setFunction(J._expression(expr.func))
	return jcall
end
function J._expression(expr)
	local tag = expr.tag
	if tag == "MIdentifier" then
		return J._identifier(expr)
	elseif tag == "MIndex" then
		return J._index(expr)
	elseif tag == "MCall" then
		return J._call(expr)
	elseif tag == "MInvoke" then
		return J._invoke(expr)
   elseif tag == "MBlock" then
      return J._block(expr)
	end
	return nil
end
function J._identifier(item)
	local javaitem = externalapi.createitem(item)
	local id = identifier:new(javaitem)
	id:setStart(item.sourcerange.min)
	id:setEnd  (item.sourcerange.max)
	return id
end
function J._index(x)
	local idx = index:new()
	idx:setStart(x.sourcerange.min)
	idx:setEnd  (x.sourcerange.max)
	idx:setLeft (J._expression(x.left))
	idx:setRight(x.right)
	return idx
end
function J._internalcontent(icontent)
	local content = internalcontent:new()
	-- Setting body
	local chunk = J._block(icontent.content)
	content:setContent(chunk)
	-- Appending variables
	for _, itemdefinition in ipairs(icontent.unknownglobalvars) do
		local item = externalapi.createitem(itemdefinition)
		content:addUnknownglobalvar(item)
	end
	return content
end
function J._invoke(expr)
	local jinvoke = invoke:new()
	jinvoke:setStart(expr.sourcerange.min)
	jinvoke:setEnd  (expr.sourcerange.max)
	jinvoke:setFunctionName(expr.functionname)
	jinvoke:setRecord(J._expression(expr.record))
	return jinvoke
end
return J
