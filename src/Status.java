
/**
 * Statusul unui produs: inainte de licitatie,
 * 						in timpul licitatiei
 * 						si in timpul transferului
 * @author silvia
 *
 */
public enum Status {
	NO_OFFER {
		@Override
		public String getName() {
			return "No Offer";
		}
	},
	OFFER_MADE {
		@Override
		public String getName() {
			return "Offer Made";
		}
	},
	OFFER_EXCEEDED {
		@Override
		public String getName() {
			return "Offer Exceeded";
		}
	},
	OFFER_ACCEPTED{
		@Override
		public String getName() {
			return "Offer Accepted";
		}
	},
	OFFER_REFUSED{
		@Override
		public String getName() {
			return "Offer Refused";
		}
	},
	TRANSFER_STARTED{
		@Override
		public String getName() {
			return "Transfer started";
		}
	},
	TRANSFER_IN_PROGRESS{
		@Override
		public String getName() {
			return "Transfer in progress";
		}
	},
	TRANSFER_COMPLETED{
		@Override
		public String getName() {
			return "Transfer completed";
		}
	},
	TRANSFER_FAILED{
		@Override
		public String getName() {
			return "Transfer failed";
		}
	};
    public abstract String getName();
};
