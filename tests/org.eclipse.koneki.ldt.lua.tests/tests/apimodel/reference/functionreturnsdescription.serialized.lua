	do local _ = {
		description = "",
		globalvars = {

		}
		--[[table: 0x9773b20]],
		shortdescription = "",
		name = "func",
		returns = {
			{
				types = {
					{
						typename = "func",
						tag = "internaltyperef"
					}
					--[[table: 0x97739e8]]
				}
				--[[table: 0x9773ab0]],
				description = "",
				tag = "return"
			}
			--[[table: 0x9773a10]]
		}
		--[[table: 0x9773b48]],
		types = {
		["__=__1"] = {
				shortdescription = "",
				description = "",
				name = "__=__1",
				parent = nil --[[ref]],
				returns = {
					{
						types = {

						}
						--[[table: 0x97802c8]],
						description = "Description\010-",
						tag = "return"
					}
					--[[table: 0x97802a0]],
					{
						types = {
							{
								typename = "number",
								tag = "primitivetyperef"
							}
							--[[table: 0x9780568]]
						}
						--[[table: 0x9780540]],
						description = "Description.",
						tag = "return"
					}
					--[[table: 0x97802f0]]
				}
				--[[table: 0x9780340]],
				params = {

				}
				--[[table: 0x9780318]],
				tag = "functiontypedef"
			}
			--[[table: 0x9780258]],
			func = {
				fields = {
					returnseveralvalues = {
						type = {
							typename = "__#nil#string=__2",
							tag = "internaltyperef"
						}
						--[[table: 0x9786aa8]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = "",
						name = "returnseveralvalues",
						sourcerange = {
							min = 177,
							max = 268
						}
						--[[table: 0x97865d8]],
						occurrences = {

						}
						--[[table: 0x97865b0]],
						tag = "item"
					}
					--[[table: 0x97864a0]],
					returnempty = {
						type = {
							typename = "__=__1",
							tag = "internaltyperef"
						}
						--[[table: 0x97806a0]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = "",
						name = "returnempty",
						sourcerange = {
							min = 21,
							max = 175
						}
						--[[table: 0x9780218]],
						occurrences = {

						}
						--[[table: 0x97801f0]],
						tag = "item"
					}
					--[[table: 0x97800e0]]
				}
				--[[table: 0x97757e0]],
				name = "func",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 19
				}
				--[[table: 0x9775808]],
				tag = "recordtypedef"
			}
			--[[table: 0x97756d0]],
		["__#nil#string=__2"] = {
				shortdescription = "",
				description = "",
				name = "__#nil#string=__2",
				parent = nil --[[ref]],
				returns = {
					{
						types = {
							{
								typename = "nil",
								tag = "primitivetyperef"
							}
							--[[table: 0x97866d0]],
							{
								typename = "string",
								tag = "primitivetyperef"
							}
							--[[table: 0x97868f8]]
						}
						--[[table: 0x97866a8]],
						description = "Description",
						tag = "return"
					}
					--[[table: 0x9786680]]
				}
				--[[table: 0x9786720]],
				params = {

				}
				--[[table: 0x97866f8]],
				tag = "functiontypedef"
			}
			--[[table: 0x9786658]]
		}
		--[[table: 0x9773af8]],
		tag = "file"
	}
	--[[table: 0x97739c0]];
	_.types["__=__1"].parent = _;
	_.types.func.fields.returnseveralvalues.parent = _.types.func;
	_.types.func.fields.returnempty.parent = _.types.func;
	_.types.func.parent = _;
	_.types["__#nil#string=__2"].parent = _;
	return _;
	end