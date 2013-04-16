package types;

public enum PacketType {
	TRANSFER {
		@Override
		public String getName() {
			return "Transfer";
		}
	},
	REMOVE_ROW {
		@Override
		public String getName() {
			return "RemoveRow";
		}
	},
	ADD_ROW {
		@Override
		public String getName() {
			return "AddRow";
		}
	},
	SET_VALUE_AT {
		@Override
		public String getName() {
			return "SetValueAt";
		}
	};

    public abstract String getName();	
	
}
