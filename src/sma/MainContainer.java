package sma;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;


public class MainContainer {
    public static void main(String [] args) {	
    	try {
			Runtime runtime = Runtime.instance();
			Properties properties = new ExtendedProperties();
			properties.setProperty(Profile.GUI, "true");
			
			Profile profile = new ProfileImpl(properties);
			
			AgentContainer mainContainer = runtime.createMainContainer(profile);

			Object [] params = new Object[] {4};
			AgentController agentController = mainContainer.createNewAgent("global-minima-finder", "sma.agents.GlobalMinimaFinderAgent", params);
			agentController.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
    }
}
