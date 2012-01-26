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
-- API MODEL
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
   fields = {},
   sourcerange = {min=0,max=0}
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
   shortdescription = "",
   description =  "",
   type = nil,
   occurences={},
   sourcerange = {min=0,max=0}
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

function M._primitivetyperef(typename)
   return {
   tag = "primitivetyperef",
   typename =  typename
   }
end

function M._moduletyperef(modulename,returnposition)
   return {
   tag = "moduletyperef",
   modulename =  modulename,
   returnposition = returnposition 
   }
end

function M._exprtyperef(expression,returnposition)
   return {
   tag = "exprtyperef",
   expression =  expression,
   returnposition = returnposition 
   }
end


function M._return(description)
   return {
   tag = "return",
   description =  description or "",
   types = {}
   }
end

return M
