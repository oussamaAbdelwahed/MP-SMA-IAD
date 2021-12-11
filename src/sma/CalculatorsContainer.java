package sma;



import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class CalculatorsContainer {

	public static void main(String[] args) {
		try {
			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			AgentContainer agentContainer = runtime.createAgentContainer(profile);
			double gradStep=0.004;
			
			//AGENT 1 CREATION
			Object [] params1 = new Object[] {
					-2d,
					-2d,
					gradStep,
					0d,
					0d
			};
			AgentController localMinimaFinder1 = agentContainer.createNewAgent("local-minima-finder1", "sma.agents.LocalMinimaFinderAgent", params1);
		
			
			
			//AGENT 2 CREATION
			Object [] params2 = new Object[] {
					-2d,
					0d,
					gradStep,
					0d,
					2d
			};
			AgentController localMinimaFinder2 = agentContainer.createNewAgent("local-minima-finder2", "sma.agents.LocalMinimaFinderAgent", params2);
		
			
			//AGENT 3 CREATION
			Object [] params3 = new Object[] {
					0d,
					-2d,
					gradStep,
					2d,
					0d
			};
			AgentController localMinimaFinder3 = agentContainer.createNewAgent("local-minima-finder3", "sma.agents.LocalMinimaFinderAgent", params3);
			
			
			//AGENT 4 CREATION
			Object [] params4= new Object[] {
					0d,
					0d,
					gradStep,
					2d,
					2d
			};
			AgentController localMinimaFinder4 = agentContainer.createNewAgent("local-minima-finder4", "sma.agents.LocalMinimaFinderAgent", params4);
			localMinimaFinder1.start();
			localMinimaFinder2.start();
			localMinimaFinder3.start();
			localMinimaFinder4.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

}
