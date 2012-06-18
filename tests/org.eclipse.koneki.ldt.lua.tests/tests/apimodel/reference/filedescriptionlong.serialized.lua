do
	local _ = {
		description = "File long long description.",
		globalvars = {},
		shortdescription = " File short description.",
		name = "filedescription",
		returns = {
			{
				types = {
					{
						typename = "filedescription",
						tag = "internaltyperef"
					}
				} ,
				description = "",
				tag = "return"
			}
		},
		types = {
			filedescription = {
				fields = {} ,
				name = "filedescription",
				parent = nil,
				sourcerange = {
					min = 0,
					max = 91
				},
				tag = "recordtypedef"
			}
		},
		tag = "file"
	};
	_.types.filedescription.parent = _;
	return _;
end
