package sma.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;

public class GlobalMinimaFinderAgent extends Agent{
	private double localMinimasCoords[][];
	private int nbrOfLocalMinimas;
	int nbrOfReceivedMsgs = 0;
	int coordsParamsCounter=0;
	
	private  double evalMathFn(double x,double y) {
	    double res = 0.2*(2*(1-x)*(1-x)*Math.exp(-(x*x)-(y+1)*(y+1))-8*(x/5-x*x*x-y*y*y*y*y)*Math.exp(-x*x-y*y)-(1/3)*Math.exp(-(x+1)*(x+1)-y*y));
	    return res;
	}
	    
	
	private double [] getGlobalMinimaCoords() {
		double globalMinimaCoords[] = new double[2];
		globalMinimaCoords[0] = localMinimasCoords[0][0];
		globalMinimaCoords[1] = localMinimasCoords[0][1];
		
		
		for(int i=1;i<nbrOfLocalMinimas;i++) {
			if(evalMathFn(localMinimasCoords[i][0],localMinimasCoords[i][1]) < evalMathFn(globalMinimaCoords[0],globalMinimaCoords[1])) {
				globalMinimaCoords[0] = localMinimasCoords[i][0];
				globalMinimaCoords[1] = localMinimasCoords[i][1];
			}
		}
		return globalMinimaCoords;		
	}
	
	@Override
    protected void setup() {
		System.out.println("Création et initialisation de l'agent : "+this.getAID().getName());
		double gradStep=0.004;
		

		String [] subDomainsCoords = new String[] {
				"-2;-2;"+gradStep+";0;0",
				"-2;0;"+gradStep+";0;2",
				"0;-2;"+gradStep+";2;0",
				"0;0;"+gradStep+";2;2"
		};
 	  
 	   Object[] args = getArguments();
 	   
 	   nbrOfLocalMinimas = (int)args[0];
 	   localMinimasCoords = new double[nbrOfLocalMinimas][2];
 	   
 	   addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
			
				ACLMessage response = receive();
				if(response!=null) {
				  if(response.getOntology().equals("UP_AND_READY")) {
					  System.out.println("UP_AND_READY ONTOLOGY RECEIVED FROM AGENT "+response.getSender().getName());
					  ACLMessage m = new ACLMessage(ACLMessage.INFORM);
					  m.setOntology("SUB_FUNCTION_DOMAIN_SET");
					  
					  //m.addReceiver(new AID(response.getSender().getName(),AID.ISLOCALNAME));
					  m.addReceiver(response.getSender());
					  m.setContent(subDomainsCoords[coordsParamsCounter++]);
					  send(m);
				  }else if(response.getOntology().equals("LOCAL_MINIMA_COORDS")) {
					nbrOfReceivedMsgs+=1;
					System.out.println("=====> Message received from agent "+response.getSender().getName()+" and contains localMinimaCoordinates = ("+response.getContent()+")");  
					String [] coords = response.getContent().split(",");
					localMinimasCoords[nbrOfReceivedMsgs-1][0] =  Double.parseDouble(coords[0]);
					localMinimasCoords[nbrOfReceivedMsgs-1][1] =  Double.parseDouble(coords[1]);
					if(nbrOfReceivedMsgs == nbrOfLocalMinimas) {
					    doAction();
					}
			      }else if(response.getOntology().equals("MATH_FUNCTION_EVAL")) {
			    	  //System.out.println("math function parameters = "+response.getContent());
			    	  String [] coords = response.getContent().split(";");
			    	  String res="";
			    	  for(int i=0;i<3;i++) {
			    		  String []item = coords[i].split(",");
			    		  double x = Double.parseDouble(item[0]);
				    	  double y = Double.parseDouble(item[1]);
				    	  res+=""+evalMathFn(x, y)+"";
				    	  if(i!=3)
				    		  res+=";";
			    	  }
			    
			    	  ACLMessage reply = response.createReply();
			    	  reply.setOntology("MATH_FUNCTION_EVAL");
			    	  reply.setContent(res);
			    	  send(reply);
				  }else {}
				}else {
					block();
				}
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
    
    private void doAction() {
    	System.out.println("************ DONE: tous les agents ont déja réussi a envoyer les minimums locals trouvés ***************");
		 //here find global minima like this
		 double [] globalMinimaCoords = getGlobalMinimaCoords();
		 System.out.println("*********** GLOBAL MINIMA IS  f(x,y)= "+evalMathFn(globalMinimaCoords[0],globalMinimaCoords[1])+" / (x,y) = ("+globalMinimaCoords[0]+","+globalMinimaCoords[1]+") ***********" );
    }
}
