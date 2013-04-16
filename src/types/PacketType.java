package types;

public enum PacketType {
	VIEW_BEST_OFFER {
		@Override
		public String getName() {
			return "View Best Offer";
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
