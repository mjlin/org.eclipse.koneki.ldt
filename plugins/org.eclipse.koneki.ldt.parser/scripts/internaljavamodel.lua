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

local blockclass =           java.require 'org.eclipse.koneki.ldt.parser.ast.Block'
local callclass  =           java.require 'org.eclipse.koneki.ldt.parser.ast.Call'
local identifierclass =      java.require 'org.eclipse.koneki.ldt.parser.ast.Identifier'
local indexclass =           java.require 'org.eclipse.koneki.ldt.parser.ast.Index'
local invokeclass =          java.require 'org.eclipse.koneki.ldt.parser.ast.Invoke'
local internalcontentclass = java.require 'org.eclipse.koneki.ldt.parser.ast.LuaInternalContent'
local localvarclass =        java.require 'org.eclipse.koneki.ldt.parser.ast.LocalVar'

function J._internalcontent(internalcontent)
   local jinternalcontent = internalcontentclass:new()
   
   -- Setting body
   local handledexpr ={} 
   local jblock = J._block(internalcontent.content,handledexpr)
   jinternalcontent:setContent(jblock)
   
   -- Appending variables
   for _, item in ipairs(internalcontent.unknownglobalvars) do
      local jitem = externalapi.createitem(item)
      jinternalcontent:addUnknownglobalvar(jitem)
   end
   
   return jinternalcontent
end

function J._block(content,handledexpr)
	-- Setting source range
	local jblock = blockclass:new()
	jblock:setStart(content.sourcerange.min)
	jblock:setEnd(content.sourcerange.max)
	
	-- Append nodes to block
	for _, expr in pairs(content.content) do
		local node = J._expression(expr,handledexpr)
		jblock:addContent(node)
	end
	
	for _, vardef in pairs(content.localvars) do
      -- Create Java item
      local jitem = externalapi.createitem(vardef.item)
      if vardef.item.type and vardef.item.type.tag == "exprtyperef" then
         print(vardef.item.type.expression)
         print(handledexpr[vardef.item.type.expression])
         io.flush()
         jitem:getType():setExpression(handledexpr[vardef.item.type.expression]) 
      end
      
      
      for _,occurence in ipairs(vardef.item.occurences) do
         jidentifier = handledexpr[occurence]
         if jidentifier then 
            jitem:addOccurence(jidentifier)
         end
      end
         
            
      -- Append Java local variable definition
      local var  = localvarclass:new(jitem, vardef.scope.min, vardef.scope.max)
      jblock:addLocalVar(var)
   end
   return jblock
end

function J._expression(expr,handledexpr)
	local tag = expr.tag
	if tag == "MIdentifier" then
		return J._identifier(expr,handledexpr)
	elseif tag == "MIndex" then
		return J._index(expr,handledexpr)
	elseif tag == "MCall" then
		return J._call(expr,handledexpr)
	elseif tag == "MInvoke" then
		return J._invoke(expr,handledexpr)
   elseif tag == "MBlock" then
      return J._block(expr,handledexpr)
	end
	return nil
end

function J._identifier(identifier,handledexpr)
	local jidentifier = identifierclass:new()
	jidentifier:setStart(identifier.sourcerange.min)
	jidentifier:setEnd  (identifier.sourcerange.max)
	
	handledexpr[identifier] =jidentifier 
	return jidentifier
end

function J._index(index,handledexpr)
	local jindex = indexclass:new()
	jindex:setStart(index.sourcerange.min)
	jindex:setEnd  (index.sourcerange.max)
	jindex:setLeft (J._expression(index.left,handledexpr))
	jindex:setRight(index.right)
	
	handledexpr[index] =jindex 
	return jindex
end

function J._call(call,handledexpr)
   local jcall = callclass:new()
   jcall:setStart(call.sourcerange.min)
   jcall:setEnd  (call.sourcerange.max)
   jcall:setFunction(J._expression(call.func,handledexpr))
   
   handledexpr[call] =jcall 
   return jcall
end

function J._invoke(invoke,handledexpr)
  local jinvoke = invokeclass:new()
	jinvoke:setStart(invoke.sourcerange.min)
	jinvoke:setEnd  (invoke.sourcerange.max)
	jinvoke:setFunctionName(invoke.functionname)
	jinvoke:setRecord(J._expression(invoke.record,handledexpr))
	
	handledexpr[invoke] =jinvoke
	return jinvoke
end
return J
