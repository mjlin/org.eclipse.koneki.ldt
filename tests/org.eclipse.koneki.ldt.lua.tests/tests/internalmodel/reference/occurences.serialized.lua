	do local _ = {
		unknownglobalvars = {

		}
		--[[table: 0x9d7ed58]],
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
								--[[table: 0x9d7f728]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x9d7f700]],
							{
								sourcerange = {
									min = 8,
									max = 8
								}
								--[[table: 0x9d7faf0]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x9d7fa88]],
							{
								sourcerange = {
									min = 22,
									max = 22
								}
								--[[table: 0x9d80d28]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x9d80d00]]
						}
						--[[table: 0x9d82ae8]],
						sourcerange = {
							min = 6,
							max = 6
						}
						--[[table: 0x9d82b10]],
						description = "",
						tag = "item"
					}
					--[[table: 0x9d829d8]],
					scope = {
						min = 0,
						max = 0
					}
					--[[table: 0x9d82d78]]
				}
				--[[table: 0x9d82d50]]
			}
			--[[table: 0x9d7ee48]],
			sourcerange = {
				min = 0,
				max = 10000
			}
			--[[table: 0x9d7ee70]],
			content = {
				nil --[[ref]],
				nil --[[ref]],
				nil --[[ref]]
			}
			--[[table: 0x9d7ee20]],
			tag = "MBlock"
		}
		--[[table: 0x9d7ed80]],
		tag = "MInternalContent"
	}
	--[[table: 0x9d7ecb8]];
	_.content.localvars[1].item.occurrences[1].definition = _.content.localvars[1].item;
	_.content.localvars[1].item.occurrences[2].definition = _.content.localvars[1].item;
	_.content.localvars[1].item.occurrences[3].definition = _.content.localvars[1].item;
	_.content.content[1] = _.content.localvars[1].item.occurrences[1];
	_.content.content[2] = _.content.localvars[1].item.occurrences[2];
	_.content.content[3] = _.content.localvars[1].item.occurrences[3];
	return _;
	end