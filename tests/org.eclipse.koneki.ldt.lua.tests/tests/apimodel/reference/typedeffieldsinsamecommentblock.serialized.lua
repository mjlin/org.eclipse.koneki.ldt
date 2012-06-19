	do local _ = {
		description = "",
		globalvars = {

		}
		--[[table: 0x91d2118]],
		shortdescription = "",
		name = "fieldsinanotherblock",
		returns = {
			{
				types = {
					{
						typename = "fieldsinanotherblock",
						tag = "internaltyperef"
					}
					--[[table: 0x91d1fe0]]
				}
				--[[table: 0x91d20a8]],
				description = "",
				tag = "return"
			}
			--[[table: 0x91d2008]]
		}
		--[[table: 0x91d2140]],
		types = {
			fieldsinanotherblock = {
				fields = {
					f = {
						parent = nil --[[ref]],
						shortdescription = "",
						name = "f",
						occurrences = {

						}
						--[[table: 0x91df8b0]],
						sourcerange = {
							min = 34,
							max = 80
						}
						--[[table: 0x91df8d8]],
						description = "",
						tag = "item"
					},
					--[[table: 0x91df888]]
					s = {
						parent = nil --[[ref]],
						shortdescription = "",
						name = "s",
						occurrences = {

						}
						--[[table: 0x91df8b0]],
						sourcerange = {
							min = 84,
							max = 131
						}
						--[[table: 0x91df8d8]],
						description = "",
						tag = "item"
					}
					--[[table: 0x91df888]]
				}
				--[[table: 0x91d3e18]],
				name = "fieldsinanotherblock",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 32
				}
				--[[table: 0x91d3e40]],
				tag = "recordtypedef"
			}
			--[[table: 0x91d3d08]]
		}
		--[[table: 0x91d20f0]],
		tag = "file"
	}
	--[[table: 0x91d1fb8]];
	_.types.fieldsinanotherblock.fields.f.parent = _.types.fieldsinanotherblock;
	_.types.fieldsinanotherblock.fields.s.parent = _.types.fieldsinanotherblock;
	_.types.fieldsinanotherblock.parent = _;
	return _;
	end