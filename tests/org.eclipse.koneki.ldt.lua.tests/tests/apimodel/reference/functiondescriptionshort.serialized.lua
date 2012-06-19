	do local _ = {
		description = "",
		globalvars = {

		}
		--[[table: 0x8ce63c8]],
		shortdescription = "",
		name = "func",
		returns = {
			{
				types = {
					{
						typename = "func",
						tag = "internaltyperef"
					}
					--[[table: 0x8cd8c48]]
				}
				--[[table: 0x8bdbd60]],
				description = "",
				tag = "return"
			}
			--[[table: 0x8ad9938]]
		}
		--[[table: 0x8ce75f8]],
		types = {
		["__=__1"] = {
				shortdescription = " Short desc.",
				description = "",
				name = "__=__1",
				parent = nil --[[ref]],
				returns = {

				}
				--[[table: 0x8ac94b0]],
				params = {

				}
				--[[table: 0x8bea888]],
				tag = "functiontypedef"
			}
			--[[table: 0x8b5e998]],
			func = {
				fields = {
					shortdescriptionlineterminator = {
						type = {
							typename = "__=__2",
							tag = "internaltyperef"
						}
						--[[table: 0x8b74060]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = " Short desc\010 ",
						name = "shortdescriptionlineterminator",
						sourcerange = {
							min = 92,
							max = 171
						}
						--[[table: 0x8b8ff08]],
						occurrences = {

						}
						--[[table: 0x8c3ba90]],
						tag = "item"
					}
					--[[table: 0x8adc230]],
					shortdescriptiondot = {
						type = {
							typename = "__=__1",
							tag = "internaltyperef"
						}
						--[[table: 0x8e35068]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = " Short desc.",
						name = "shortdescriptiondot",
						sourcerange = {
							min = 21,
							max = 90
						}
						--[[table: 0x8b9df60]],
						occurrences = {

						}
						--[[table: 0x8c60cc8]],
						tag = "item"
					}
					--[[table: 0x8b85e70]]
				}
				--[[table: 0x8c56fb0]],
				name = "func",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 19
				}
				--[[table: 0x8db0dc8]],
				tag = "recordtypedef"
			}
			--[[table: 0x8d18e80]],
		["__=__2"] = {
				shortdescription = " Short desc\010 ",
				description = "",
				name = "__=__2",
				parent = nil --[[ref]],
				returns = {

				}
				--[[table: 0x8da8188]],
				params = {

				}
				--[[table: 0x8de5ee8]],
				tag = "functiontypedef"
			}
			--[[table: 0x8d003d8]]
		}
		--[[table: 0x8ce1ee0]],
		tag = "file"
	}
	--[[table: 0x8cd3c30]];
	_.types["__=__1"].parent = _;
	_.types.func.fields.shortdescriptionlineterminator.parent = _.types.func;
	_.types.func.fields.shortdescriptiondot.parent = _.types.func;
	_.types.func.parent = _;
	_.types["__=__2"].parent = _;
	return _;
	end