package sma.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

public class LocalMinimaFinderAgent extends Agent{
	@Override
    protected void setup() {
 	   System.out.println("Création et initialisation de l'agent : "+this.getAID().getName());
 	   Object[] args = getArguments();
	   
	   double x = (Double)args[0];
	   double y = (Double)args[1];
	   double gradStep = (Double)args[2];
	   double xMax = (Double)args[3];
	   double yMax = (Double)args[4];
	   System.out.println("initial x = "+x);
	   System.out.println("initial y = "+y);
	   System.out.println("gradient step  = "+gradStep);
	   System.out.println("MAX X = "+xMax);
	   System.out.println("MAX Y = "+yMax); 	
	   
	   addBehaviour(new OneShotBehaviour() {
		 @Override
		 public void action() {
    		double [] localMinimaCoords = gradientDescentAlgorithm(x, y, gradStep, xMax, yMax);
			doAction(localMinimaCoords);
		 }
	   });
    }
    
    @Override
    protected void beforeMove() {
 	    try {
 		    System.out.println("Avant migration de l'agent : "+this.getAID().getName());
 			System.out.println("de "+this.getContainerController().getContainerName());
 		} catch (ControllerException e) {
 			e.printStackTrace();
 		}
    }
    
    
    @Override
    protected void afterMove() {
 	    try {
 			System.out.println("Aprés migration de l'agent : "+this.getAID().getName());
 			System.out.println("vers "+this.getContainerController().getContainerName());
 		} catch (ControllerException e) {
 			e.printStackTrace();
 		} 
    }
    
    @Override
    protected void takeDown() {
 	   System.out.println("L'agent "+this.getAID().getName()+" va mourir");
    }
    
	private  double[] gradientDescentAlgorithm (double x,double y,double gradStep,double xMax,double yMax){
		double delta = 0.01;
		double epsilon = 0.0001;
		
		double gradient[] = new double[2];
		double minDirection[] = new double[2];
		
		minDirection[0] = x;
		minDirection[1] = y;
		
		while(minDirection[0] >= x && minDirection[0]<= xMax && minDirection[1]>= y && minDirection[1] <=yMax) {
			StringBuilder data =new StringBuilder();
			
			data.append(minDirection[0]);
			data.append(",");
			data.append(minDirection[1]);
			data.append(";");
			
			
			data.append(minDirection[0]+delta);
			data.append(",");
			data.append(minDirection[1]);
			data.append(";");
			

			data.append(minDirection[0]);
			data.append(",");
			data.append(minDirection[1] + delta);
			
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setOntology("MATH_FUNCTION_EVAL");
			msg.addReceiver(new AID("global-minima-finder",AID.ISLOCALNAME));
			msg.setContent(data.toString());
			send(msg);
			
			ACLMessage response = blockingReceive();
			String  mathFnEvalResults = response.getContent();
			String finalFnResults [] = mathFnEvalResults.split(";");
			
			gradient[0] = (Double.parseDouble(finalFnResults[1]) - Double.parseDouble(finalFnResults[0])) / delta;
			gradient[1] = (Double.parseDouble(finalFnResults[2]) - Double.parseDouble(finalFnResults[0])) / delta;
			
			if(norm(gradient[0],gradient[1]) <= epsilon) 
			   break;
			
			minDirection[0] = minDirection[0] - gradStep*gradient[0];
			minDirection[1] = minDirection[1]  - gradStep*gradient[1];
		}
		return  minDirection;
	}
	
	private  static double norm(double x,double y) {
		return Math.sqrt(x*x + y*y);
	}
	
	private void doAction(double [] localMinimaCoords) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setOntology("LOCAL_MINIMA_COORDS");
		message.addReceiver(new AID("global-minima-finder",AID.ISLOCALNAME));
		message.setContent(localMinimaCoords[0]+","+localMinimaCoords[1]);
		
		send(message);
	}
}
