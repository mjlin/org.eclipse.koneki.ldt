	do local _ = {
		description = "",
		globalvars = {

		}
		--[[table: 0x9d58d00]],
		shortdescription = "",
		name = "file",
		returns = {
			{
				types = {
					{
						typename = "file",
						tag = "internaltyperef"
					}
					--[[table: 0x9d58bc8]]
				}
				--[[table: 0x9d58c90]],
				description = "",
				tag = "return"
			}
			--[[table: 0x9d58bf0]]
		}
		--[[table: 0x9d58d28]],
		types = {
			innertype = {
				description = "Long long description.",
				fields = {

				}
				--[[table: 0x9d5c530]],
				name = "innertype",
				shortdescription = " Short description.",
				parent = nil --[[ref]],
				sourcerange = {
					min = 21,
					max = 91
				}
				--[[table: 0x9d5c558]],
				tag = "recordtypedef"
			}
			--[[table: 0x9d5c400]],
			file = {
				fields = {

				}
				--[[table: 0x9d5a9c0]],
				name = "file",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 19
				}
				--[[table: 0x9d5a9e8]],
				tag = "recordtypedef"
			}
			--[[table: 0x9d5a8b0]]
		}
		--[[table: 0x9d58cd8]],
		tag = "file"
	}
	--[[table: 0x9d58ba0]];
	_.types.innertype.parent = _;
	_.types.file.parent = _;
	return _;
	end