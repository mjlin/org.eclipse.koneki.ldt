	do local _ = {
		unknownglobalvars = {
			{
				name = "string",
				shortdescription = "",
				description = "",
				occurrences = {
					{
						sourcerange = {
							min = 16,
							max = 21
						}
						--[[table: 0x887bf50]],
						definition = nil --[[ref]],
						tag = "MIdentifier"
					}
					--[[table: 0x887bf28]]
				}
				--[[table: 0x887d928]],
				sourcerange = {
					min = 16,
					max = 21
				}
				--[[table: 0x887d950]],
				tag = "item"
			}
			--[[table: 0x887d818]]
		}
		--[[table: 0x887b5d0]],
		content = {
			localvars = {
				{
					item = {
						type = {
							expression = {
								sourcerange = {
									min = 16,
									max = 28
								}
								--[[table: 0x887c098]],
								right = "format",
								left = nil --[[ref]],
								tag = "MIndex"
							}
							--[[table: 0x887bff8]],
							returnposition = 1,
							tag = "exprtyperef"
						}
						--[[table: 0x887d670]],
						shortdescription = "",
						name = "sformat",
						occurrences = {
							{
								sourcerange = {
									min = 6,
									max = 12
								}
								--[[table: 0x887c8f0]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x887c888]]
						}
						--[[table: 0x887d5c8]],
						sourcerange = {
							min = 6,
							max = 12
						}
						--[[table: 0x887d5f0]],
						description = "",
						tag = "item"
					}
					--[[table: 0x887d4b8]],
					scope = {
						min = 0,
						max = 0
					}
					--[[table: 0x887d7b0]]
				}
				--[[table: 0x887d788]]
			}
			--[[table: 0x887b6c0]],
			sourcerange = {
				min = 0,
				max = 10000
			}
			--[[table: 0x887b6e8]],
			content = {
				nil --[[ref]],
				nil --[[ref]]
			}
			--[[table: 0x887b698]],
			tag = "MBlock"
		}
		--[[table: 0x887b5f8]],
		tag = "MInternalContent"
	}
	--[[table: 0x887b530]];
	_.unknownglobalvars[1].occurrences[1].definition = _.unknownglobalvars[1];
	_.content.localvars[1].item.type.expression.left = _.unknownglobalvars[1].occurrences[1];
	_.content.localvars[1].item.occurrences[1].definition = _.content.localvars[1].item;
	_.content.content[1] = _.content.localvars[1].item.type.expression;
	_.content.content[2] = _.content.localvars[1].item.occurrences[1];
	return _;
	end