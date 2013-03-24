
public abstract class Statelike {

	Mediator med;
	
	Statelike(Mediator med){
		this.med = med;
	}
	
	abstract void gotoNextState();

}

class StateInactive extends Statelike {

	public StateInactive(Mediator med) {
		super(med);
		// TODO Auto-generated constructor stub
	}

	@Override
	void gotoNextState() {
		// TODO Auto-generated method stub
		
	}
	
}

class StateNoOffer extends Statelike {

	StateNoOffer(Mediator med) {
		super(med);
		// TODO Auto-generated constructor stub
	}

	@Override
	void gotoNextState() {
		// TODO Auto-generated method stub
		
	}
	
}

class StateOfferMade extends Statelike {

	StateOfferMade(Mediator med) {
		super(med);
		// TODO Auto-generated constructor stub
	}

	@Override
	void gotoNextState() {
		// TODO Auto-generated method stub
		
	}
	
}

//class StateOfferExceeded extends Statelike {
//	
//}
//
//class StateOfferAccepted extends Statelike {
//	
//}
//
//class StateOfferRefused extends Statelike {
//	
//}
//
//class StateTransferStarted extends Statelike {
//	
//}
//
//class StateTransferInProgress extends Statelike {
//	
//}
//
//class StateTransferCompleted extends Statelike {
//	
//}
//
//class StateTransferFailed extends Statelike {
//	
//}
