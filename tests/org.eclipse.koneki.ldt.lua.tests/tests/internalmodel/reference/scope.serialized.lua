	do local _ = {
		unknownglobalvars = {
			{
				name = "d",
				shortdescription = "",
				description = "",
				occurrences = {
					{
						sourcerange = {
							min = 43,
							max = 43
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
		--[[table: 0x9feb1e8]],
		content = {
			localvars = {
				{
					item = {
						shortdescription = "",
						name = "M",
						occurrences = {
							{
								sourcerange = {
									min = 6,
									max = 6
								}
								--[[table: 0x9febdf0]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x9febdc8]],
							{
								sourcerange = {
									min = 22,
									max = 22
								}
								--[[table: 0x9fec1b8]],
								definition = nil --[[ref]],
								tag = "MIdentifier"
							}
							--[[table: 0x9fec150]]
						}
						--[[table: 0x9fef7e0]],
						sourcerange = {
							min = 6,
							max = 6
						}
						--[[table: 0x9fef808]],
						description = "",
						tag = "item"
					}
					--[[table: 0x9fef6d0]],
					scope = {
						min = 0,
						max = 0
					}
					--[[table: 0x9fef9b8]]
				}
				--[[table: 0x9fef990]]
			}
			--[[table: 0x9feb2d8]],
			sourcerange = {
				min = 0,
				max = 50
			}
			--[[table: 0x9feb300]],
			content = {
				nil --[[ref]],
				{
					sourcerange = {
						min = 22,
						max = 24
					}
					--[[table: 0x9fec340]],
					right = "f",
					left = nil --[[ref]],
					tag = "MIndex"
				}
				--[[table: 0x9fec2a0]],
				{
					localvars = {
						{
							item = {
								shortdescription = "",
								name = "d",
								occurrences = {
									{
										sourcerange = {
											min = 35,
											max = 35
										}
										--[[table: 0x9fed3d8]],
										definition = nil --[[ref]],
										tag = "MIdentifier"
									}
									--[[table: 0x9fed3b0]],
									{
										sourcerange = {
											min = 42,
											max = 42
										}
										--[[table: 0x9fed738]],
										definition = nil --[[ref]],
										tag = "MIdentifier"
									}
									--[[table: 0x9fed710]]
								}
								--[[table: 0x9fefaf0]],
								sourcerange = {
									min = 35,
									max = 35
								}
								--[[table: 0x9fefb18]],
								description = "",
								tag = "item"
							}
							--[[table: 0x9fef9e0]],
							scope = {
								min = 0,
								max = 0
							}
							--[[table: 0x9fefcb8]]
						}
						--[[table: 0x9fefc90]]
					}
					--[[table: 0x9fec8a8]],
					sourcerange = {
						min = 25,
						max = 40
					}
					--[[table: 0x9fecbe8]],
					content = {
						nil --[[ref]]
					}
					--[[table: 0x9fec880]],
					tag = "MBlock"
				}
				--[[table: 0x9fec858]],
				nil --[[ref]]
			}
			--[[table: 0x9feb2b0]],
			tag = "MBlock"
		}
		--[[table: 0x9feb210]],
		tag = "MInternalContent"
	}
	--[[table: 0x9feb148]];
	_.content.localvars[1].item.occurrences[1].definition = _.content.localvars[1].item;
	_.content.localvars[1].item.occurrences[2].definition = _.content.localvars[1].item;
	_.content.content[1] = _.content.localvars[1].item.occurrences[1];
	_.content.content[2].left = _.content.localvars[1].item.occurrences[2];
	_.content.content[3].localvars[1].item.occurrences[1].definition = _.content.content[3].localvars[1].item;
	_.content.content[3].localvars[1].item.occurrences[2].definition = _.content.content[3].localvars[1].item;
	_.content.content[3].content[1] = _.content.content[3].localvars[1].item.occurrences[1];
	_.content.content[4] = _.content.content[3].localvars[1].item.occurrences[2];
	return _;
	end