package types;

public enum ClientToServerProtocol {
	LAUNCH_OFFER_REQUEST {
		@Override
		public String getName() {
			return "Launch Offer request";
		}
	},
	DROP_OFFER_REQUEST {
		@Override
		public String getName() {
			return "Drop Offer request";
		}
	},
	MAKE_OFFER {
		@Override
		public String getName() {
			return "Make offer";
		}
	},
	DROP_AUCTION {
		@Override
		public String getName() {
			return "Drop auction";
		}
	},
	ACCEPT_OFFER {
		@Override
		public String getName() {
			return "Accept Offer";
		}
	},
	REFUSE_OFFER {
		@Override
		public String getName() {
			return "Refuse Offer";
		}
	},
	VIEW_BEST_OFFER {
		@Override
		public String getName() {
			return "View Best Offer";
		}
	},
	TRANSFER {
		@Override
		public String getName() {
			return "Transfer";
		}
	};

    public abstract String getName();	
	
}
