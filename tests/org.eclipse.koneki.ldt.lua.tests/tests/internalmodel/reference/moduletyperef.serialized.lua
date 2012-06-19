	do local _ = {
		unknownglobalvars = {
			{
				name = "require",
				shortdescription = "",
				description = "",
				occurrences = {
					{
						sourcerange = {
							min = 10,
							max = 16
						}
						--[[table: 0x842eb10]],
						definition = nil --[[ref]],
						tag = "MIdentifier"
					}
					--[[table: 0x842eae8]]
				}
				--[[table: 0x84305d0]],
				sourcerange = {
					min = 10,
					max = 16
				}
				--[[table: 0x84305f8]],
				tag = "item"
			}
			--[[table: 0x84304c0]]
		}
		--[[table: 0x842e1b0]],
		content = {
			localvars = {
				{
					item = {
						type = {
							modulename = "module",
							returnposition = 1,
							tag = "moduletyperef"
						}
						--[[table: 0x8430318]],
						shortdescription = "",
						name = "l",
						occurrences = {
							{
								sourcerange = {
									min = 6,
									max = 6
								}
								--[[table: 0x842f520]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x842ea90]]
						}
						--[[table: 0x8430270]],
						sourcerange = {
							min = 6,
							max = 6
						}
						--[[table: 0x8430298]],
						description = "",
						tag = "item"
					}
					--[[table: 0x8430160]],
					scope = {
						min = 0,
						max = 0
					}
					--[[table: 0x8430458]]
				}
				--[[table: 0x8430430]]
			}
			--[[table: 0x842e2a0]],
			sourcerange = {
				min = 0,
				max = 10000
			}
			--[[table: 0x842e2c8]],
			content = {
				{
					sourcerange = {
						min = 10,
						max = 25
					}
					--[[table: 0x842ec58]],
					func = nil --[[ref]],
					tag = "MCall"
				}
				--[[table: 0x842ebb8]],
				nil --[[ref]]
			}
			--[[table: 0x842e278]],
			tag = "MBlock"
		}
		--[[table: 0x842e1d8]],
		tag = "MInternalContent"
	}
	--[[table: 0x842e110]];
	_.unknownglobalvars[1].occurrences[1].definition = _.unknownglobalvars[1];
	_.content.localvars[1].item.occurrences[1].definition = _.content.localvars[1].item;
	_.content.content[1].func = _.unknownglobalvars[1].occurrences[1];
	_.content.content[2] = _.content.localvars[1].item.occurrences[1];
	return _;
	end