	do local _ = {
		description = "",
		globalvars = {

		}
		--[[table: 0x82c41a0]],
		shortdescription = "",
		name = "file",
		returns = {
			{
				types = {
					{
						typename = "file",
						tag = "internaltyperef"
					}
					--[[table: 0x82c4130]],
					{
						typename = "innertype",
						tag = "internaltyperef"
					}
					--[[table: 0x82c94e0]]
				}
				--[[table: 0x82c4108]],
				description = "",
				tag = "return"
			}
			--[[table: 0x82c4068]]
		}
		--[[table: 0x82c41c8]],
		types = {
			innertype = {
				description = "",
				fields = {

				}
				--[[table: 0x82c9658]],
				name = "innertype",
				shortdescription = "",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 48
				}
				--[[table: 0x82c9680]],
				tag = "recordtypedef"
			}
			--[[table: 0x82c9548]],
			file = {
				fields = {

				}
				--[[table: 0x82c9450]],
				name = "file",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 48
				}
				--[[table: 0x82c9478]],
				tag = "recordtypedef"
			}
			--[[table: 0x82c9340]]
		}
		--[[table: 0x82c4178]],
		tag = "file"
	}
	--[[table: 0x82c4040]];
	_.types.innertype.parent = _;
	_.types.file.parent = _;
	return _;
	end