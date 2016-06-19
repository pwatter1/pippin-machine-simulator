package pippin;

public enum States {
	AUTO_STEPPING {
		public void enter() {
			states[ASSEMBLE] = false;
			states[CLEAR] = false;
			states[LOAD] = false;
			states[RELOAD] = false;
			states[RUN] = true;
			states[RUNNING] = true;
			states[STEP] = false;
		}		
	},
	NOTHING_LOADED {
		public void enter() {
			states[ASSEMBLE] = true;
			states[CLEAR] = false;
			states[LOAD] = true;
			states[RELOAD] = false;
			states[RUN] = false;
			states[RUNNING] = false;
			states[STEP] = false;
		}
	}, 
	PROGRAM_HALTED {
		public void enter() {
			states[ASSEMBLE] = true;
			states[CLEAR] = true;
			states[LOAD] = true;
			states[RELOAD] = true;
			states[RUN] = false;
			states[RUNNING] = false;
			states[STEP] = false;
		}		
	}, 
	PROGRAM_LOADED_NOT_AUTOSTEPPING {
		public void enter() {
			states[ASSEMBLE] = true;
			states[CLEAR] = true;
			states[LOAD] = true;
			states[RELOAD] = true;
			states[RUN] = true;
			states[RUNNING] = false;
			states[STEP] = true;
		}		
	};
	private static final int ASSEMBLE = 0;
	private static final int CLEAR = 1;
	private static final int LOAD = 2; 
	private static final int RELOAD = 3;
	private static final int RUN = 4;
	private static final int RUNNING = 5;
	private static final int STEP = 6; 
	
	boolean[] states = new boolean[7];
	
	public abstract void enter();
	
	public boolean getAssembleFileActive() {
		return states[ASSEMBLE];
	}
	public boolean getClearActive() {
		return states[CLEAR];
	}
	public boolean getLoadFileActive() {
		return states[LOAD];
	}
	public boolean getReloadActive() {
		return states[RELOAD];
	}
	public boolean getRunningActive() {
		return states[RUNNING];
	}
	public boolean getRunPauseActive() {
		return states[RUN];
	}
	public boolean getStepActive() {
		return states[STEP];
	}
}
