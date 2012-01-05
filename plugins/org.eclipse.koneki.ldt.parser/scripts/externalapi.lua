--------------------------------------------------------------------------------
--  Copyright (c) 2011-2012 Sierra Wireless.
--  All rights reserved. This program and the accompanying materials
--  are made available under the terms of the Eclipse Public License v1.0
--  which accompanies this distribution, and is available at
--  http://www.eclipse.org/legal/epl-v10.html
-- 
--  Contributors:
--       Simon BERNARD <sbernard@sierrawireless.com>
--           - initial API and implementation and initial documentation
--------------------------------------------------------------------------------
local M = {}

----
-- EXTERNAL API MODEL
--
function M._file()
   return {
   tag = "file",
   shortdescription = nil,
   description = nil,
   types = {},
   globalvars ={},
   returns={}
   }
end

function M._recordtypedef(shortdescription,description)
   return {
   tag = "recordtypedef",
   shortdescription = shortdescription or "",
   description =  description or "",
   fields = {}
   }
end


function M._functiontypedef(shortdescription,description)
   return {
   tag = "functiontypedef",
   shortdescription = shortdescription or "",
   description =  description or "",
   params = {},
   returns ={}
   }
end


function M._parameter(description)
   return {
   tag = "parameter",
   description =  description or "",
   type = nil
   }
end


function M._item(name)
   return {
   tag = "item",
   name = name,
   description =  description or "",
   type = nil
   }
end

function M._externaltypref(modulename, typename)
   return {
   tag = "externaltyperef",
   modulename = modulename,
   typename =  typename
   }
end
function M._internaltyperef(typename)
   return {
   tag = "internaltyperef",
   typename =  typename
   }
end

function M._primitivetypref(typename)
   return {
   tag = "primitivetyperef",
   typename =  typename
   }
end


function M._return(description)
   return {
   tag = "return",
   description =  description or "",
   types = {}
   }
end


----
-- UTILITY METHODS
--

local primitivetypes = {string=true, boolean=true, ["nil"]=true, table=true, number=true}

function M.createtyperef(td_typeref)
   local _typref
   if td_typeref.module then
      -- manage external type
      _typeref = M._externaltypref()
      _typeref.modulename = td_typeref.module
      _typeref.typename = td_typeref.type
   else
      if primitivetypes[td_typeref.type] then
         -- manage primitive type
         _typeref = M._primitivetypref()
         _typeref.typename = td_typeref.type
      else
         -- manage internal type
         _typeref = M._internaltyperef()
         _typeref.typename = td_typeref.type
      end
   end
   return _typeref
end

function M.createreturn(td_return)
   local _return = M._return()

   _return.description = td_return.description

   -- manage typeref
   if td_return.types then
      for _, td_typeref in ipairs(td_return.types) do
         local _typref = M.createtyperef(td_typeref)
         if _typeref then
            table.insert(_return.types,_typeref)
         end
      end
   end
   return _return
end


function M.createfield(td_field)
   local _item = M._item()
   _item.description = td_field.description
   _item.name = td_field.name

   -- manage typeref
   local td_typeref = td_field.type
   if td_typeref then
      _item.type =  M.createtyperef(td_typeref)
   end
   return _item
end

function M.createparam(td_param)
   local _parameter = M._parameter()
   _parameter.description = td_param.description
   _parameter.name = td_param.name

   -- manage typeref
   local td_typeref = td_param.type
   if td_typeref then
      _parameter.type =  M.createtyperef(td_typeref)
   end
   return _parameter
end


function M.generatefunctiontypename(_functiontypedef)
   local name = {"__"}
   if _functiontypedef.returns and _functiontypedef.returns[1] then
      local ret =  _functiontypedef.returns[1]
      for _, type in ipairs(ret.types) do
         if type.typename then
            if type.modulename then
               table.insert(name,type.modulename)
            end
            table.insert(name,"#")
            table.insert(name,type.typename)
         end
      end
      
   end
   table.insert(name,"=")
   if _functiontypedef.params then
      for _, param in ipairs(_functiontypedef.params) do
         local type =  param.type
         if type then
            if type.typename then
               if type.modulename then
                  table.insert(name,type.modulename)
               end
               table.insert(name,"#")
               table.insert(name,type.typename)
            else
               table.insert(name,"#unknown")
            end
         end
         table.insert(name,"[")
         table.insert(name,param.name)
         table.insert(name,"]")
      end
   end
    table.insert(name,"__")
--    table.print(name,1)
--    io.flush()
   return table.concat(name)
end


return M
