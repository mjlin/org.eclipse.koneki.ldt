	do local _ = {
		unknownglobalvars = {
			{
				name = "file",
				shortdescription = "",
				description = "",
				occurrences = {
					{
						sourcerange = {
							min = 10,
							max = 13
						}
						--[[table: 0x888d808]],
						definition = nil --[[ref]],
						tag = "MIdentifier"
					}
					--[[table: 0x888d7e0]]
				}
				--[[table: 0x888f2b8]],
				sourcerange = {
					min = 10,
					max = 13
				}
				--[[table: 0x888f2e0]],
				tag = "item"
			}
			--[[table: 0x888f1a8]]
		}
		--[[table: 0x888ce88]],
		content = {
			localvars = {
				{
					item = {
						type = {
							expression = {
								record = nil --[[ref]],
								sourcerange = {
									min = 10,
									max = 20
								}
								--[[table: 0x888d950]],
								tag = "MInvoke",
								functionname = "read"
							}
							--[[table: 0x888d8b0]],
							returnposition = 1,
							tag = "exprtyperef"
						}
						--[[table: 0x888f000]],
						shortdescription = "",
						name = "f",
						occurrences = {
							{
								sourcerange = {
									min = 6,
									max = 6
								}
								--[[table: 0x888e208]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x888e1a0]]
						}
						--[[table: 0x888ef58]],
						sourcerange = {
							min = 6,
							max = 6
						}
						--[[table: 0x888ef80]],
						description = "",
						tag = "item"
					}
					--[[table: 0x888ee48]],
					scope = {
						min = 0,
						max = 0
					}
					--[[table: 0x888f140]]
				}
				--[[table: 0x888f118]]
			}
			--[[table: 0x888cf78]],
			sourcerange = {
				min = 0,
				max = 10000
			}
			--[[table: 0x888cfa0]],
			content = {
				nil --[[ref]],
				nil --[[ref]]
			}
			--[[table: 0x888cf50]],
			tag = "MBlock"
		}
		--[[table: 0x888ceb0]],
		tag = "MInternalContent"
	}
	--[[table: 0x888cde8]];
	_.unknownglobalvars[1].occurrences[1].definition = _.unknownglobalvars[1];
	_.content.localvars[1].item.type.expression.record = _.unknownglobalvars[1].occurrences[1];
	_.content.localvars[1].item.occurrences[1].definition = _.content.localvars[1].item;
	_.content.content[1] = _.content.localvars[1].item.type.expression;
	_.content.content[2] = _.content.localvars[1].item.occurrences[1];
	return _;
	end