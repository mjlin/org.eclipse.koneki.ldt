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
return 
[[<dl class="function">
<dt>
# if item.type and item.type.tag == 'internaltyperef' then
	<em>$(item.type and '#'..item.type.typename)</em>
# end
$(item.name)</dt>
<dd>$( markdown(item.shortdescription) )</dd>
<dd>$( markdown(item.description) )</dd>
</dl>]]
