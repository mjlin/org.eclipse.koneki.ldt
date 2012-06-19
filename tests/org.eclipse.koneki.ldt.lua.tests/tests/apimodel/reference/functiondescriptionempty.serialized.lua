	do local _ = {
		description = "",
		globalvars = {

		}
		--[[table: 0x8a49090]],
		shortdescription = "",
		name = "func",
		returns = {
			{
				types = {
					{
						typename = "func",
						tag = "internaltyperef"
					}
					--[[table: 0x8a48f58]]
				}
				--[[table: 0x8a49020]],
				description = "",
				tag = "return"
			}
			--[[table: 0x8a48f80]]
		}
		--[[table: 0x8a490b8]],
		types = {
			func = {
				fields = {
					empty = {
						type = {
							typename = "__=__1",
							tag = "internaltyperef"
						}
						--[[table: 0x8a4ea20]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = "",
						name = "empty",
						sourcerange = {
							min = 21,
							max = 61
						}
						--[[table: 0x8a4e8a8]],
						occurrences = {

						}
						--[[table: 0x8a4e880]],
						tag = "item"
					}
					--[[table: 0x8a4e5e8]]
				}
				--[[table: 0x8a4ad50]],
				name = "func",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 19
				}
				--[[table: 0x8a4ad78]],
				tag = "recordtypedef"
			}
			--[[table: 0x8a4ac40]],
		["__=__1"] = {
				shortdescription = "",
				description = "",
				name = "__=__1",
				parent = nil --[[ref]],
				returns = {

				}
				--[[table: 0x8a4e9b8]],
				params = {

				}
				--[[table: 0x8a4e990]],
				tag = "functiontypedef"
			}
			--[[table: 0x8a4e8d0]]
		}
		--[[table: 0x8a49068]],
		tag = "file"
	}
	--[[table: 0x8a48f30]];
	_.types.func.fields.empty.parent = _.types.func;
	_.types.func.parent = _;
	_.types["__=__1"].parent = _;
	return _;
	end