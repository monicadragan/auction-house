
public class StateManager {
	private Statelike currentState;
	private Mediator med;
	
	//user-ul + product pentru care se verifica starea

	public StateManager(Mediator med) {
		this.med = med;
		currentState = new StateInactive(med);
	}

}
