	do local _ = {
		description = "",
		globalvars = {

		}
		--[[table: 0x8a32d28]],
		shortdescription = "",
		name = "func",
		returns = {
			{
				types = {
					{
						typename = "func",
						tag = "internaltyperef"
					}
					--[[table: 0x8a32bf0]]
				}
				--[[table: 0x8a32cb8]],
				description = "",
				tag = "return"
			}
			--[[table: 0x8a32c18]]
		}
		--[[table: 0x8a32d50]],
		types = {
		["__#number=__2"] = {
				shortdescription = "",
				description = "",
				name = "__#number=__2",
				parent = nil --[[ref]],
				returns = {
					{
						types = {
							{
								typename = "number",
								tag = "primitivetyperef"
							}
							--[[table: 0x8a3fb98]]
						}
						--[[table: 0x8a3fb70]],
						description = "",
						tag = "return"
					}
					--[[table: 0x8a3fb48]]
				}
				--[[table: 0x8a3fbe8]],
				params = {

				}
				--[[table: 0x8a3fbc0]],
				tag = "functiontypedef"
			}
			--[[table: 0x8a3fb20]],
		["__=__1"] = {
				shortdescription = "",
				description = "",
				name = "__=__1",
				parent = nil --[[ref]],
				returns = {
					{
						types = {

						}
						--[[table: 0x8a3aa80]],
						description = "",
						tag = "return"
					}
					--[[table: 0x8a3aa58]]
				}
				--[[table: 0x8a3aaf8]],
				params = {

				}
				--[[table: 0x8a3aad0]],
				tag = "functiontypedef"
			}
			--[[table: 0x8a3aa30]],
		["__#nil#string=__3"] = {
				shortdescription = "",
				description = "",
				name = "__#nil#string=__3",
				parent = nil --[[ref]],
				returns = {
					{
						types = {
							{
								typename = "nil",
								tag = "primitivetyperef"
							}
							--[[table: 0x8a45f00]],
							{
								typename = "string",
								tag = "primitivetyperef"
							}
							--[[table: 0x8a46128]]
						}
						--[[table: 0x8a45ed8]],
						description = "",
						tag = "return"
					}
					--[[table: 0x8a45eb0]]
				}
				--[[table: 0x8a45f50]],
				params = {

				}
				--[[table: 0x8a45f28]],
				tag = "functiontypedef"
			}
			--[[table: 0x8a45e88]],
			func = {
				fields = {
					returnseveralvalues = {
						type = {
							typename = "__#nil#string=__3",
							tag = "internaltyperef"
						}
						--[[table: 0x8a46260]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = "",
						name = "returnseveralvalues",
						sourcerange = {
							min = 153,
							max = 232
						}
						--[[table: 0x8a45e08]],
						occurrences = {

						}
						--[[table: 0x8a45de0]],
						tag = "item"
					}
					--[[table: 0x8a45cd0]],
					returnempty = {
						type = {
							typename = "__=__1",
							tag = "internaltyperef"
						}
						--[[table: 0x8a3acf8]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = "",
						name = "returnempty",
						sourcerange = {
							min = 21,
							max = 78
						}
						--[[table: 0x8a3a9c8]],
						occurrences = {

						}
						--[[table: 0x8a3a9a0]],
						tag = "item"
					}
					--[[table: 0x8a3a890]],
					dotsasreturn = {
						type = {
							typename = "__#dots=__4",
							tag = "internaltyperef"
						}
						--[[table: 0x8a3fea0]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = "",
						name = "dotsasreturn",
						sourcerange = {
							min = 234,
							max = 297
						}
						--[[table: 0x8a4b240]],
						occurrences = {

						}
						--[[table: 0x8a4b218]],
						tag = "item"
					}
					--[[table: 0x8a4b108]],
					returnsinglevalue = {
						type = {
							typename = "__#number=__2",
							tag = "internaltyperef"
						}
						--[[table: 0x8a3ff18]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = "",
						name = "returnsinglevalue",
						sourcerange = {
							min = 80,
							max = 151
						}
						--[[table: 0x8a3faa0]],
						occurrences = {

						}
						--[[table: 0x8a3fa78]],
						tag = "item"
					}
					--[[table: 0x8a3f968]]
				}
				--[[table: 0x8a349e8]],
				name = "func",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 19
				}
				--[[table: 0x8a34a10]],
				tag = "recordtypedef"
			}
			--[[table: 0x8a348d8]],
		["__#dots=__4"] = {
				shortdescription = "",
				description = "",
				name = "__#dots=__4",
				parent = nil --[[ref]],
				returns = {
					{
						types = {
							{
								typename = "nil",
								tag = "primitivetyperef"
							}
							--[[table: 0x8a4b338]]
						}
						--[[table: 0x8a4b310]],
						description = "",
						tag = "return"
					}
					--[[table: 0x8a4b2e8]]
				}
				--[[table: 0x8a4b388]],
				params = {

				}
				--[[table: 0x8a4b360]],
				tag = "functiontypedef"
			}
			--[[table: 0x8a4b2c0]]
		}
		--[[table: 0x8a32d00]],
		tag = "file"
	}
	--[[table: 0x8a32bc8]];
	_.types["__#number=__2"].parent = _;
	_.types["__=__1"].parent = _;
	_.types["__#nil#string=__3"].parent = _;
	_.types.func.fields.returnseveralvalues.parent = _.types.func;
	_.types.func.fields.returnempty.parent = _.types.func;
	_.types.func.fields.dotsasreturn.parent = _.types.func;
	_.types.func.fields.returnsinglevalue.parent = _.types.func;
	_.types.func.parent = _;
	_.types["__#dots=__4"].parent = _;
	return _;
	end