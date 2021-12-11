package sma.agents;

import jade.core.Agent;
import jade.wrapper.ControllerException;

public class MyAgent extends Agent{
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
}
