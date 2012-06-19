	do local _ = {
		unknownglobalvars = {

		}
		--[[table: 0x92208d8]],
		content = {
			localvars = {
				{
					item = {
						shortdescription = "",
						name = "d",
						occurrences = {
							{
								sourcerange = {
									min = 6,
									max = 6
								}
								--[[table: 0x92212c8]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x92212a0]],
							{
								sourcerange = {
									min = 8,
									max = 8
								}
								--[[table: 0x9221690]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x9221628]]
						}
						--[[table: 0x92228b0]],
						sourcerange = {
							min = 6,
							max = 6
						}
						--[[table: 0x92228d8]],
						description = "",
						tag = "item"
					}
					--[[table: 0x92227a0]],
					scope = {
						min = 0,
						max = 0
					}
					--[[table: 0x9222a78]]
				}
				--[[table: 0x9222a50]]
			}
			--[[table: 0x92209c8]],
			sourcerange = {
				min = 0,
				max = 10000
			}
			--[[table: 0x92209f0]],
			content = {
				nil --[[ref]],
				nil --[[ref]]
			}
			--[[table: 0x92209a0]],
			tag = "MBlock"
		}
		--[[table: 0x9220900]],
		tag = "MInternalContent"
	}
	--[[table: 0x9220838]];
	_.content.localvars[1].item.occurrences[1].definition = _.content.localvars[1].item;
	_.content.localvars[1].item.occurrences[2].definition = _.content.localvars[1].item;
	_.content.content[1] = _.content.localvars[1].item.occurrences[1];
	_.content.content[2] = _.content.localvars[1].item.occurrences[2];
	return _;
	end