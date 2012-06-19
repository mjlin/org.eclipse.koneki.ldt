	do local _ = {
		unknownglobalvars = {
			{
				name = "d",
				shortdescription = "",
				description = "",
				occurrences = {
					{
						sourcerange = {
							min = 0,
							max = 0
						}
						--[[table: 0xa05a658]],
						definition = nil --[[ref]],
						tag = "MIdentifier"
					}
					--[[table: 0xa05a630]]
				}
				--[[table: 0xa05b5b8]],
				sourcerange = {
					min = 0,
					max = 0
				}
				--[[table: 0xa05b5e0]],
				tag = "item"
			}
			--[[table: 0xa05b4a8]]
		}
		--[[table: 0xa059cd8]],
		content = {
			localvars = {

			}
			--[[table: 0xa059dc8]],
			sourcerange = {
				min = 0,
				max = 10000
			}
			--[[table: 0xa059df0]],
			content = {
				nil --[[ref]]
			}
			--[[table: 0xa059da0]],
			tag = "MBlock"
		}
		--[[table: 0xa059d00]],
		tag = "MInternalContent"
	}
	--[[table: 0xa059c38]];
	_.unknownglobalvars[1].occurrences[1].definition = _.unknownglobalvars[1];
	_.content.content[1] = _.unknownglobalvars[1].occurrences[1];
	return _;
	end