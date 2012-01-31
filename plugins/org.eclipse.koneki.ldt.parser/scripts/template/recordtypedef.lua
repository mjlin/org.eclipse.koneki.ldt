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
return [[#
<a id ="typedefanchor"></a>
# --
# -- Descriptions
# --
# if _recordtypedef.shortdescription then
	<p>$( markdown( _recordtypedef.shortdescription ) )</p>
# end
# if _recordtypedef.description then
	<p>$( markdown( _recordtypedef.description ) )</p>
# end
# --
# -- Describe type fields
# --
# if not isempty( _recordtypedef.fields ) then
	<h3>Field(s)</h3>
#	for name, item in pairs( _recordtypedef.fields )do
		<a id="resolveitemanchor"></a>
		$( applytemplate(item) )
#	end
# end ]]
