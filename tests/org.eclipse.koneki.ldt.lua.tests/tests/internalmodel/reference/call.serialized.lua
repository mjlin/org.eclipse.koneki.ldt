	do local _ = {
		unknownglobalvars = {
			{
				name = "call",
				shortdescription = "",
				description = "",
				occurrences = {
					{
						sourcerange = {
							min = 10,
							max = 13
						}
						--[[table: 0x83686a0]],
						definition = nil --[[ref]],
						tag = "MIdentifier"
					}
					--[[table: 0x8368678]]
				}
				--[[table: 0x836a120]],
				sourcerange = {
					min = 10,
					max = 13
				}
				--[[table: 0x836a148]],
				tag = "item"
			}
			--[[table: 0x836a010]]
		}
		--[[table: 0x8367d20]],
		content = {
			localvars = {
				{
					item = {
						type = {
							expression = {
								sourcerange = {
									min = 10,
									max = 18
								}
								--[[table: 0x83687e8]],
								func = nil --[[ref]],
								tag = "MCall"
							}
							--[[table: 0x8368748]],
							returnposition = 1,
							tag = "exprtyperef"
						}
						--[[table: 0x8369e68]],
						shortdescription = "",
						name = "c",
						occurrences = {
							{
								sourcerange = {
									min = 6,
									max = 6
								}
								--[[table: 0x83690a0]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x8369038]]
						}
						--[[table: 0x8369dd8]],
						sourcerange = {
							min = 6,
							max = 6
						}
						--[[table: 0x8369e00]],
						description = "",
						tag = "item"
					}
					--[[table: 0x8369cc8]],
					scope = {
						min = 0,
						max = 0
					}
					--[[table: 0x8369fa8]]
				}
				--[[table: 0x8369f80]]
			}
			--[[table: 0x8367e10]],
			sourcerange = {
				min = 0,
				max = 10000
			}
			--[[table: 0x8367e38]],
			content = {
				nil --[[ref]],
				nil --[[ref]]
			}
			--[[table: 0x8367de8]],
			tag = "MBlock"
		}
		--[[table: 0x8367d48]],
		tag = "MInternalContent"
	}
	--[[table: 0x8367c80]];
	_.unknownglobalvars[1].occurrences[1].definition = _.unknownglobalvars[1];
	_.content.localvars[1].item.type.expression.func = _.unknownglobalvars[1].occurrences[1];
	_.content.localvars[1].item.occurrences[1].definition = _.content.localvars[1].item;
	_.content.content[1] = _.content.localvars[1].item.type.expression;
	_.content.content[2] = _.content.localvars[1].item.occurrences[1];
	return _;
	end