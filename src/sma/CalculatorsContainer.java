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
			AgentController localMinimaFinder1 = agentContainer.createNewAgent("local-minima-finder1", "sma.agents.LocalMinimaFinderAgent", new Object[] {});

			
			
			//AGENT 2 CREATION
			AgentController localMinimaFinder2 = agentContainer.createNewAgent("local-minima-finder2", "sma.agents.LocalMinimaFinderAgent", new Object[] {});

			
			//AGENT 3 CREATION
			AgentController localMinimaFinder3 = agentContainer.createNewAgent("local-minima-finder3", "sma.agents.LocalMinimaFinderAgent", new Object[] {});

			
			//AGENT 4 CREATION
			AgentController localMinimaFinder4 = agentContainer.createNewAgent("local-minima-finder4", "sma.agents.LocalMinimaFinderAgent", new Object[] {});

			localMinimaFinder1.start();
			localMinimaFinder2.start();
			localMinimaFinder3.start();
			localMinimaFinder4.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

}
