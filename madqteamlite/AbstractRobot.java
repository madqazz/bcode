package madqteamlite;

import java.util.Random;
import battlecode.common.*;


public abstract class AbstractRobot {
	protected RobotController myRC;
	protected enum RobotState { 
		ARCHON_START,
		ARCHON_FIND_FLUX, 
		ARCHON_TAKE_FLUX,
		ARCHON_ON_FLUX, 
		ARCHON_DEFENSE,
		ARCHON_ATTACK,
		SOLDIER_FOLLOW,
		SOLDIER_DEFENSE,
		SOLDIER_ATTACK,
		SOLDIER_PATROL,
		SOLDIER_BEGIN,
		SOLDIER_FIND_FLUX,
	};
	protected RobotState state;
	protected Random generator;
	
	public AbstractRobot(RobotController rc) {
		myRC = rc;
		generator = new Random();
	}
	
	protected void updateStatus() {
		//myRC.setIndicatorString(1, Integer.toString(69));
		myRC.setIndicatorString(2, state.name());
	}
	
	abstract public void run() throws GameActionException; 
	
	 protected void randomRun(int rounds){
	     for(int i=0;i<rounds;i++){
		   try{
			   
		       if(myRC.isMovementActive()) {
                 myRC.yield();
	           } else {		            	 
            	 if(myRC.canMove(myRC.getDirection()) && !(generator.nextInt(20)==1)){
            		 myRC.moveForward();
            	 } else {
            		 myRC.setDirection(myRC.getDirection().rotateRight());
            	 }
            	 myRC.yield();
	           }	       
		   }catch(Exception e) {
	         System.out.println("caught exception:");
	         e.printStackTrace();
		   }
	     }  
	 }
	 
	 
	 protected void archonRun(int rounds) throws GameActionException{

	     for(int i=0;i<rounds;i++){
		   try{
				 
			   RobotInfo enemyRobot = enemyInfo();			   
			   
 		   		if (!(enemyRobot==null) 
 		   			&& myRC.getLocation().distanceSquaredTo(enemyRobot.location) < 4){
	   				waitUntilMovementIdle();
	   				if(!myRC.getDirection().equals(myRC.getLocation().directionTo(enemyRobot.location).opposite()) 
   						&& !myRC.getLocation().directionTo(enemyRobot.location).opposite().equals(Direction.OMNI)){
 		   					myRC.setDirection(myRC.getLocation().directionTo(enemyRobot.location).opposite());
 		   					myRC.yield();
 		   					waitUntilMovementIdle();
 		   					if(myRC.canMove(myRC.getDirection())){
 		   						myRC.moveForward();
 		   						myRC.yield();
 		   					}
	   				}	 		   				
 		   		} else {
		             if(myRC.isMovementActive()) {
		                myRC.yield();
		             } else {		            	 
		            	 if(myRC.canMove(myRC.getDirection()) && !(generator.nextInt(20)==1)) {
		            		 myRC.moveForward();
		            	 } else {
		            		 myRC.setDirection(myRC.getDirection().rotateRight());
		            	 }
		            	 myRC.yield();
		             }	 
	 		   		}
 		   			myRC.yield();
		            transferEnergon();
		   }catch(Exception e) {
	         System.out.println("caught exception:");
	         e.printStackTrace();
	     }
	     }  
	 }
	 
	 
	 
	 
	 protected void patrol(int rounds) throws GameActionException{
		 MapLocation archon = followArchon();
		 waitUntilMovementIdle();
		 if(!myRC.getDirection().equals(myRC.getLocation().directionTo(archon).opposite()) 
				 && !(myRC.getLocation().directionTo(archon).opposite().equals(Direction.OMNI))
				 && !(myRC.getLocation().directionTo(archon).opposite().equals(Direction.NONE))){
			 myRC.setDirection(myRC.getLocation().directionTo(archon).opposite());
			 myRC.yield();
		 }
         waitUntilMovementIdle();
		 
	     for(int i=0;i<rounds;i++){
		   try{
	             while(myRC.isMovementActive()) {
	                myRC.yield();
	             }		      
	             
            	 if(myRC.canMove(myRC.getDirection()) && !(generator.nextInt(20)==1))
            	 {
            		 myRC.moveForward();
            	 } else {
            		 myRC.setDirection(myRC.getDirection().rotateRight());
            	 }
            	 myRC.yield();
            	 
		   }catch(Exception e) {
	         System.out.println("caught exception:");
	         e.printStackTrace();
	     }
	     }  
	 }
	 
	 
	 protected MapLocation followArchon(){
		 MapLocation[] archons = myRC.senseAlliedArchons();
		 int minLen = 9999;
		 MapLocation myArchon = null;
		 
		 for(MapLocation archon : archons){
			 if(minLen>myRC.getLocation().distanceSquaredTo(archon)){
				 minLen=myRC.getLocation().distanceSquaredTo(archon);
				 myArchon=archon;
			 }
		 }
		 return myArchon;
	 }
	 
	 
	 protected MapLocation findFlux() throws GameActionException {		 
			FluxDeposit[] fluxs = myRC.senseNearbyFluxDeposits();
			int minLen = 9999;
			MapLocation myFLux = null;

			for (FluxDeposit deposit : fluxs) {
				MapLocation loc = myRC.senseFluxDepositInfo(deposit).location;
				if(minLen>myRC.getLocation().distanceSquaredTo(loc)){				
					minLen = myRC.getLocation().distanceSquaredTo(loc);
					myFLux = loc;
				}
			}
		 return myFLux;
	 }
	 
	 
	 protected void goTo(MapLocation dest){

	      while(true){
	         try{
	            waitUntilMovementIdle();

	            if (myRC.getLocation().directionTo(dest).equals(Direction.OMNI)){
	                break;
	            }

	            while (myRC.isMovementActive()){
	            	myRC.yield();
	            }

	            if (myRC.canMove(myRC.getDirection())){
       		 		 waitUntilMovementIdle();
	            	 if(myRC.getDirection().equals(myRC.getLocation().directionTo(dest))){
	            		 myRC.moveForward();
	            	 } else {
	            		 myRC.setDirection(myRC.getLocation().directionTo(dest));
	            	 }
                     myRC.yield();
	       	 	} else {
	                if(myRC.getDirection().equals(myRC.getLocation().directionTo(dest))){
	                	randomRun(6);
	            	} else {
	    	            waitUntilMovementIdle();
	            		myRC.setDirection(myRC.getLocation().directionTo(dest));
	                    myRC.yield();
	            	}
	            }
	            transferEnergon();

	         }catch(Exception e) {
	            System.out.println("caught exception:");
	            e.printStackTrace();
	         }
	      }	   
	   }
	 
	 
	 protected void goNear(MapLocation dest){

	      while(true){
	         try{

	            if (myRC.getLocation().isAdjacentTo(dest)){
	                break;
	            }

	            while (myRC.isMovementActive()){
	            	myRC.yield();
	            }

	            if (myRC.canMove(myRC.getDirection())){
	            	if(myRC.getDirection().equals(myRC.getLocation().directionTo(dest)))
	            	 {
	            		 myRC.moveForward();
	                     myRC.yield();
	            	 } else {
	                     if(!myRC.getLocation().directionTo(dest).equals(Direction.OMNI)&&!myRC.getLocation().directionTo(dest).equals(Direction.NONE)){
	                        myRC.setDirection(myRC.getLocation().directionTo(dest));
	                        myRC.yield();
	                     }
	            	 }
	       	 	 } else {
	                 if(myRC.getDirection().equals(myRC.getLocation().directionTo(dest))){
	            		 randomRun(6);
	            	 } else {
	                     if(!myRC.getLocation().directionTo(dest).equals(Direction.OMNI)
	                    	&& !myRC.getLocation().directionTo(dest).equals(Direction.NONE)){
	                        myRC.setDirection(myRC.getLocation().directionTo(dest));
	                        myRC.yield();
	                     }
	            	 }
	             }

	            while(myRC.getLocation().equals(dest))
	            {
	            	 if (myRC.canMove(myRC.getDirection())){
	       	 	   		 myRC.moveForward();
	                     myRC.yield();
	            	 } else {
	                     myRC.setDirection(myRC.getDirection().rotateLeft());
	                     myRC.yield();
	                 }
	            }
	       	 	transferEnergon();
	         }catch(Exception e) {
	            System.out.println("caught exception:");
	            e.printStackTrace();
	         }
	      }

	   }
	 
	 
	 protected void goDirection(Direction dir) throws GameActionException {
			
		 	if (!myRC.getDirection().equals(dir) && !dir.equals(Direction.OMNI)){
		 		waitUntilMovementIdle();
		 		myRC.setDirection(dir);
		 		myRC.yield();
		 	}
		 	
		 	waitUntilMovementIdle();

			while (!myRC.canMove(myRC.getDirection())) {			 	 
				   myRC.setDirection(myRC.getDirection().rotateRight());
				   myRC.yield();
				   waitUntilMovementIdle();
			}

			myRC.moveForward();
			myRC.yield();	  
	}
	 
	protected void sendMessage(int i) throws GameActionException{
	        Message msgout;
	    	msgout = new Message();
	        msgout.strings = new String[2];
	        msgout.locations = new MapLocation[1];
            RobotInfo enemyRobot = enemyInfo();
	        switch(i){
	            case 1:
	            	msgout.strings[0]=myRC.getTeam().toString();
	                msgout.strings[1]="MQ_DEFENSE";
	                if (!(enemyRobot == null)){	                	              
	                	msgout.locations[0] = enemyRobot.location;
	                } else {
	                	msgout.locations[0] = null;
	                }
	                break;
	            case 2:
	            	msgout.strings[0]=myRC.getTeam().toString();
	            	msgout.strings[1]="MQ_ATTACK";
	                if (!(enemyRobot == null)){	                	              
	                	msgout.locations[0] = enemyRobot.location;
	                } else {
	                	msgout.locations[0] = null;
	                }
	                break;
	            case 3:
	            	msgout.strings[0]=myRC.getTeam().toString();
	            	msgout.strings[1]="MQ_NEED_HELP";
	                msgout.locations[0] = myRC.getLocation();
	                break;
	            case 4:
	            	msgout.strings[0]=myRC.getTeam().toString();
	            	msgout.strings[1]="MQ_FOLLOW";
	            	break;
	        }
		           	 myRC.broadcast(msgout);
		           	 myRC.yield();
	    }
	 
	
	protected boolean ourMessage(Message msg){
		return  (!(msg.strings==null)
				 && (msg.strings.length == 2)
				 && !(msg.strings[0] == null)
				 && !(msg.strings[1] == null)
				 && msg.strings[0].equals(myRC.getTeam().toString())
				 && (msg.strings[1] == "MQ_DEFENSE" ||
					 msg.strings[1] == "MQ_ATTACK" ||
					 msg.strings[1] == "MQ_NEED_HELP" ||
					 msg.strings[1] == "MQ_FOLLOW") ); 
					 
	}
	
	 
   protected int numberOfNearbyRobots (RobotType typeOfRobot) throws GameActionException{
   		Robot[] NearbyRobots;
       	NearbyRobots = myRC.senseNearbyGroundRobots();
       	int count = 0;
	    for (Robot robot : NearbyRobots){
	    	   if(myRC.canSenseObject(robot)){
	    		   RobotInfo robotInfo = myRC.senseRobotInfo(robot);
	    			   if (robotInfo.team.equals(myRC.getTeam()) && robotInfo.type.equals(typeOfRobot)){
	    				   count++;
	    			   }
	    		   }
	    	   }
	    return count;
   }
   
   protected int numberOfAirRobots (RobotType typeOfRobot) throws GameActionException{
  		Robot[] NearbyRobots;
      	NearbyRobots = myRC.senseNearbyAirRobots();
      	int count = 0;
	    for (Robot robot : NearbyRobots){
	    	   if(myRC.canSenseObject(robot)){
	    		   RobotInfo robotInfo = myRC.senseRobotInfo(robot);
	    			   if (robotInfo.team.equals(myRC.getTeam()) && robotInfo.type.equals(typeOfRobot)){
	    				   count++;
	    			   }
	    		   }
	    	   }
	    return count;
  }
   
	   
   protected void transferEnergon() throws GameActionException {
		for (Robot robot : myRC.senseNearbyGroundRobots()) {
			if (myRC.canSenseObject(robot)){
				RobotInfo robotInfo = myRC.senseRobotInfo(robot);

				if (robotInfo.team == myRC.getTeam()) {
					MapLocation robotLoc = robotInfo.location;

					if ((myRC.getLocation().isAdjacentTo(robotLoc))
						&&(3*robotInfo.maxEnergon > 4*robotInfo.energonLevel)
						&& !(robotLoc == null)
						&& !(myRC.senseGroundRobotAtLocation(robotLoc) == null)
						&& (Math.max(Math.min(robotInfo.maxEnergon-robotInfo.eventualEnergon, myRC.getEnergonLevel()/2),0)) > 0 ){
						  myRC.transferEnergon(Math.max(Math.min(robotInfo.maxEnergon-robotInfo.eventualEnergon, myRC.getEnergonLevel()/2),0), robotLoc, RobotLevel.ON_GROUND);
						  myRC.yield();
						  break;
					}
				}
			}
		}
  }
   
   protected void transferToAir() throws GameActionException {
		for (Robot robot : myRC.senseNearbyAirRobots()) {
			if (myRC.canSenseObject(robot)){
				RobotInfo robotInfo = myRC.senseRobotInfo(robot);

				if (robotInfo.team == myRC.getTeam()) {
					MapLocation robotLoc = robotInfo.location;

					if ((myRC.getLocation().isAdjacentTo(robotLoc))
						&&(3*robotInfo.maxEnergon > 4*robotInfo.energonLevel)
						&& !(myRC.senseAirRobotAtLocation(robotLoc).equals(null))
						&& (Math.max(Math.min(robotInfo.maxEnergon-robotInfo.eventualEnergon, myRC.getEnergonLevel()/2),0)) > 0 ){
						  myRC.transferEnergon(Math.max(Math.min(robotInfo.maxEnergon-robotInfo.eventualEnergon, myRC.getEnergonLevel()/2),0), robotLoc, RobotLevel.IN_AIR);
						  myRC.yield();
						  break;
					}
				}
			}
		}
  }
   
   

   protected boolean checkEnemy() throws GameActionException{
	  	Robot[] groundRobots = myRC.senseNearbyGroundRobots();
	  	Robot[] airRobots = myRC.senseNearbyAirRobots();
	  	boolean find=false;
	  		for (Robot robot : groundRobots){
	  			if (myRC.canSenseObject(robot) 
	  				&& !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
	  				  find=true;
	  				  break;
	  			}
	  		}
	  		if(!find){
	  			for(Robot robot : airRobots){
	  				if (myRC.canSenseObject(robot)
	  					&& !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
	      				  find=true;
	      				  break;
	      			}
	  			}
	  		}  		
	  	return find;
	  }
   
   
   protected RobotInfo enemyInfo() throws GameActionException{
    	Robot[] groundRobots = myRC.senseNearbyGroundRobots();
    	Robot[] airRobots = myRC.senseNearbyAirRobots();
    	RobotInfo nearestEnemy = null;
    	int minLen=9999;
    		
		for (Robot robot : airRobots){
			if (myRC.canSenseObject(robot) 
				&& !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
    			  RobotInfo robotInfo = myRC.senseRobotInfo(robot);
    			  if (minLen > myRC.getLocation().distanceSquaredTo(robotInfo.location)){
	    			  nearestEnemy = robotInfo;
	    			  minLen = myRC.getLocation().distanceSquaredTo(robotInfo.location);
    			  }
    			  if (myRC.canAttackSquare(robotInfo.location)){
    					nearestEnemy = robotInfo;
    					return nearestEnemy;
    			  }
			}
		}
    	
   		for (Robot robot : groundRobots){
   			if (myRC.canSenseObject(robot)
   				&& !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
    			  RobotInfo robotInfo = myRC.senseRobotInfo(robot);
    			  if (minLen > myRC.getLocation().distanceSquaredTo(robotInfo.location)){
   	    			  nearestEnemy = robotInfo;
   	    			  minLen = myRC.getLocation().distanceSquaredTo(robotInfo.location);
    			  }
    			  if (myRC.canAttackSquare(robotInfo.location)){
    			      nearestEnemy = robotInfo;
    				  return nearestEnemy;
    			  }
    			}
    		}	
		return nearestEnemy;    	    			
	  }

   protected void waitUntilMovementIdle(){
      while(myRC.getRoundsUntilMovementIdle() > 0){
                 myRC.yield();
    }
}
   
   protected void waitUntilAttackIdle(){
	      while(myRC.getRoundsUntilAttackIdle() > 0){
	                 myRC.yield();
	    }
	}
	
}
	

	
	
