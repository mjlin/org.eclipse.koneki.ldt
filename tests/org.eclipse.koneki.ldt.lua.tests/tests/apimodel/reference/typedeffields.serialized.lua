	do local _ = {
		description = "",
		globalvars = {

		}
		--[[table: 0x92994d0]],
		shortdescription = " Module with no fields\010 ",
		name = "mod",
		returns = {
			{
				types = {
					{
						typename = "mod",
						tag = "internaltyperef"
					}
					--[[table: 0x9301b68]]
				}
				--[[table: 0x92a5d00]],
				description = "",
				tag = "return"
			}
			--[[table: 0x92a5c68]]
		}
		--[[table: 0x91dd318]],
		types = {
			fieldsinsameblock = {
				description = "",
				fields = {
					notype = {
						parent = nil --[[ref]],
						shortdescription = "Description",
						name = "notype",
						occurrences = {

						}
						--[[table: 0x92a5248]],
						sourcerange = {
							min = 45,
							max = 131
						}
						--[[table: 0x922fc90]],
						description = "",
						tag = "item"
					}
					--[[table: 0x91dbd78]],
					typed = {
						type = {
							typename = "nil",
							tag = "primitivetyperef"
						}
						--[[table: 0x9250b80]],
						description = "",
						parent = nil --[[ref]],
						shortdescription = "Desc.",
						name = "typed",
						sourcerange = {
							min = 45,
							max = 131
						}
						--[[table: 0x93e0e88]],
						occurrences = {

						}
						--[[table: 0x93def00]],
						tag = "item"
					}
					--[[table: 0x93ddcd8]]
				}
				--[[table: 0x93ebbe8]],
				name = "fieldsinsameblock",
				shortdescription = "",
				parent = nil --[[ref]],
				sourcerange = {
					min = 45,
					max = 131
				}
				--[[table: 0x93d8078]],
				tag = "recordtypedef"
			}
			--[[table: 0x93eb570]],
			mod = {
				fields = {

				}
				--[[table: 0x9349050]],
				name = "mod",
				parent = nil --[[ref]],
				sourcerange = {
					min = 0,
					max = 43
				}
				--[[table: 0x9300d20]],
				tag = "recordtypedef"
			}
			--[[table: 0x9348fb8]]
		}
		--[[table: 0x92bbbc0]],
		tag = "file"
	}
	--[[table: 0x92d2580]];
	_.types.fieldsinsameblock.fields.notype.parent = _.types.fieldsinsameblock;
	_.types.fieldsinsameblock.fields.typed.parent = _.types.fieldsinsameblock;
	_.types.fieldsinsameblock.parent = _;
	_.types.mod.parent = _;
	return _;
	end