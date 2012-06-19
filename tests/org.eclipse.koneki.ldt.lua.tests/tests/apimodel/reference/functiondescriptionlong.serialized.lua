	do local _ = {
		description = "",
		globalvars = {

		}
		--[[table: 0x98b96e0]],
		shortdescription = "",
		name = "func",
		returns = {
			{
				types = {
					{
						typename = "func",
						tag = "internaltyperef"
					}
					--[[table: 0x98b95a8]]
				}
				--[[table: 0x98b9670]],
				description = "",
				tag = "return"
			}
			--[[table: 0x98b95d0]]
		}
		--[[table: 0x98b9708]],
		types = {
		["__=__3"] = {
				shortdescription = "",
				description = "",
				name = "__=__3",
				parent = nil --[[ref]],
				returns = {

				}
				--[[table: 0x98c6838]],
				params = {

				}
				--[[table: 0x98c6810]],
				tag = "functiontypedef"
			}
			--[[table: 0x98c6750]],
		["__=__1"] = {
				shortdescription = " Short desc.",
				description = "Very long long long description.",
				name = "__=__1",
				parent = nil --[[ref]],
				returns = {

				}
				--[[table: 0x98bf180]],
				params = {

				}
				--[[table: 0x98bf158]],
				tag = "functiontypedef"
			}
			--[[table: 0x98bf098]],
			func = {
				fields = {
					usagef = {
						type = {
							typename = "__=__3",
							tag = "internaltyperef"
						}
						--[[table: 0x98c68b0]],
						description = "",
						parent = nil --[[ref]],
						metadata = {
							usage = {
								{
									tagname = "usage",
									description = "usagef()",
									lineinfo = {
										last = {
											offset = 6,
											line = 1,
											source = "Third party tag lexer",
											column = 6,
											id = 132
										}
										--[[<Third party tag lexer|L1|C6|K6>]],
										first = {
											offset = 1,
											line = 1,
											source = "Third party tag lexer",
											facing = {
												offset = 1,
												line = 1,
												source = "Third party tag lexer",
												facing = nil --[[ref]],
												column = 1,
												id = 128
											}
											--[[<Third party tag lexer|L1|C1|K1>]],
											column = 1,
											id = 129
										}
										--[[<Third party tag lexer|L1|C1|K1>]]
									}
									--[[<Third party tag lexer|L1|C1-6|K1-6>]],
									name = "usage"
								}
								--[[table: 0x98c6410]]
							}
							--[[table: 0x98c6628]]
						}
						--[[table: 0x98c6600]],
						shortdescription = "",
						name = "usagef",
						sourcerange = {
							min = 242,
							max = 302
						}
						--[[table: 0x98c6710]],
						occurrences = {

						}
						--[[table: 0x98c66e8]],
						tag = "item"
					}
					--[[table: 0x98c65d8]],
					longdescriptionlineterminator = {
						type = {
							typename = "__=__2",
							tag = "internaltyperef"
						}
						--[[table: 0x98c27a0]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = " Short desc\010 Very long long long description\010 ",
						name = "longdescriptionlineterminator",
						sourcerange = {
							min = 127,
							max = 240
						}
						--[[table: 0x98c2508]],
						occurrences = {

						}
						--[[table: 0x98c24e0]],
						tag = "item"
					}
					--[[table: 0x98c2248]],
					longdescriptiondot = {
						type = {
							typename = "__=__1",
							tag = "internaltyperef"
						}
						--[[table: 0x98bf1e8]],
						description = "Very long long long description.",
						parent = nil --[[ref]],
						shortdescription = " Short desc.",
						name = "longdescriptiondot",
						sourcerange = {
							min = 21,
							max = 125
						}
						--[[table: 0x98bf070]],
						occurrences = {

						}
						--[[table: 0x98bf048]],
						tag = "item"
					}
					--[[table: 0x98bedb0]]
				}
				--[[table: 0x98bb3a0]],
				name = "func",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 19
				}
				--[[table: 0x98bb3c8]],
				tag = "recordtypedef"
			}
			--[[table: 0x98bb290]],
		["__=__2"] = {
				shortdescription = " Short desc\010 Very long long long description\010 ",
				description = "",
				name = "__=__2",
				parent = nil --[[ref]],
				returns = {

				}
				--[[table: 0x98c2618]],
				params = {

				}
				--[[table: 0x98c25f0]],
				tag = "functiontypedef"
			}
			--[[table: 0x98c2530]]
		}
		--[[table: 0x98b96b8]],
		tag = "file"
	}
	--[[table: 0x98b9580]];
	_.types["__=__3"].parent = _;
	_.types["__=__1"].parent = _;
	_.types.func.fields.usagef.parent = _.types.func;
	_.types.func.fields.usagef.metadata.usage[1].lineinfo.first.facing.facing = _.types.func.fields.usagef.metadata.usage[1].lineinfo.first;
	_.types.func.fields.longdescriptionlineterminator.parent = _.types.func;
	_.types.func.fields.longdescriptiondot.parent = _.types.func;
	_.types.func.parent = _;
	_.types["__=__2"].parent = _;
	return _;
	end