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

--------------------------------------------------------------------------------
-- API MODEL
--------------------------------------------------------------------------------

function M._file()
   local file = {
      -- FIELDS
      tag = "file",
      shortdescription, -- string
      description,      -- string
      types = {},       -- map from typename to type 
      globalvars ={},
      returns={},
   
      -- FUNCTIONS
      addtype =  function (self,type)
                     self.types[type.name] = type
                     type.parent = self
                  end
   }
   return file
end

function M._recordtypedef()
   local recordtype = {
      -- FIELDS
      tag = "recordtypedef",
      name,             -- string
      shortdescription, -- string
      description,      -- string
      fields = {},      -- map from fieldname to field
      sourcerange = {min=0,max=0},
   
      -- FUNCTIONS
      addfield = function (self,field)
                     self.fields[field.name] = field
                     field.parent = self
                  end
   }
   return recordtype
end


function M._functiontypedef()
   return {
   tag = "functiontypedef",
   name,             -- string
   shortdescription, -- string
   description ,     -- string
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
   occurrences={},
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
