	do local _ = {
		unknownglobalvars = {
			{
				name = "tostring",
				shortdescription = "",
				description = "",
				occurrences = {
					{
						sourcerange = {
							min = 10,
							max = 17
						}
						--[[table: 0x8561688]],
						definition = nil --[[ref]],
						tag = "MIdentifier"
					}
					--[[table: 0x8561660]]
				}
				--[[table: 0x8563108]],
				sourcerange = {
					min = 10,
					max = 17
				}
				--[[table: 0x8563130]],
				tag = "item"
			}
			--[[table: 0x8562ff8]]
		}
		--[[table: 0x8560d08]],
		content = {
			localvars = {
				{
					item = {
						type = {
							expression = {
								sourcerange = {
									min = 10,
									max = 22
								}
								--[[table: 0x85617d0]],
								func = nil --[[ref]],
								tag = "MCall"
							}
							--[[table: 0x8561730]],
							returnposition = 1,
							tag = "exprtyperef"
						}
						--[[table: 0x8562e50]],
						shortdescription = "",
						name = "e",
						occurrences = {
							{
								sourcerange = {
									min = 6,
									max = 6
								}
								--[[table: 0x8562088]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x8562020]]
						}
						--[[table: 0x8562dc0]],
						sourcerange = {
							min = 6,
							max = 6
						}
						--[[table: 0x8562de8]],
						description = "",
						tag = "item"
					}
					--[[table: 0x8562cb0]],
					scope = {
						min = 0,
						max = 0
					}
					--[[table: 0x8562f90]]
				}
				--[[table: 0x8562f68]]
			}
			--[[table: 0x8560df8]],
			sourcerange = {
				min = 0,
				max = 10000
			}
			--[[table: 0x8560e20]],
			content = {
				nil --[[ref]],
				nil --[[ref]]
			}
			--[[table: 0x8560dd0]],
			tag = "MBlock"
		}
		--[[table: 0x8560d30]],
		tag = "MInternalContent"
	}
	--[[table: 0x8560c68]];
	_.unknownglobalvars[1].occurrences[1].definition = _.unknownglobalvars[1];
	_.content.localvars[1].item.type.expression.func = _.unknownglobalvars[1].occurrences[1];
	_.content.localvars[1].item.occurrences[1].definition = _.content.localvars[1].item;
	_.content.content[1] = _.content.localvars[1].item.type.expression;
	_.content.content[2] = _.content.localvars[1].item.occurrences[1];
	return _;
	end