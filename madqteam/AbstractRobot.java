package madqteam;

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
	        		/*if (checkEnemy() && myRC.getRobotType().equals(RobotType.ARCHON)){
	        			return;
	        		}*/
			   
		             if(myRC.isMovementActive()) {
		                myRC.yield();
		             } else {		            	 
		            	 if(myRC.canMove(myRC.getDirection()) && !(generator.nextInt(20)==1))
		            	 {
		            		 myRC.moveForward();
		            	 } else 
		            	 {
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
				 
	 		   MapLocation enemy = enemyLocation();
			   
	 		   		if (!(enemy==null) && myRC.getLocation().distanceSquaredTo(enemy) < 4){
	 		   				waitUntilMovementIdle();
	 		   				if(!myRC.getDirection().equals(myRC.getLocation().directionTo(enemy).opposite()) 
	 		   						&& !myRC.getLocation().directionTo(enemy).opposite().equals(Direction.OMNI)){
	 		   					myRC.setDirection(myRC.getLocation().directionTo(enemy).opposite());
	 		   					myRC.yield();
	 		   					waitUntilMovementIdle();
	 		   					if(myRC.canMove(myRC.getDirection())){
	 		   						myRC.moveForward();
	 		   					}
	 		   				}
	 		   				
	 		   		}else{

		             if(myRC.isMovementActive()) {
		                myRC.yield();
		             } else {		            	 
		            	 if(myRC.canMove(myRC.getDirection()) && !(generator.nextInt(20)==1))
		            	 {
		            		 myRC.moveForward();
		            	 } else 
		            	 {
		            		 myRC.setDirection(myRC.getDirection().rotateRight());
		            	 }
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
		             if(myRC.isMovementActive()) {
		                myRC.yield();
		             } else {		            	 
		            	 if(myRC.canMove(myRC.getDirection()) && !(generator.nextInt(20)==1))
		            	 {
		            		 myRC.moveForward();
		            	 } else 
		            	 {
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
	 
	 
	 public MapLocation findFlux() throws GameActionException {
		 
			FluxDeposit[] fluxs = myRC.senseNearbyFluxDeposits();
			int minLen = 9999;
			MapLocation myFLux = null;

			for (FluxDeposit deposit : fluxs) {
				MapLocation loc = myRC.senseFluxDepositInfo(deposit).location;
				int dist = myRC.getLocation().distanceSquaredTo(loc);

				if (dist < minLen) {
					minLen = dist;
					myFLux = loc;
				}
			}
		 return myFLux;
	 }
	 
	 
	 protected void goTo(MapLocation dest){

	      boolean onDest = false;

	      while(true){
	         try{

	             myRC.yield();
	             waitUntilMovementIdle();

	            if (myRC.getLocation().directionTo(dest).equals(Direction.OMNI))
	            {
	            	onDest = true;
	                myRC.yield();
	                break;
	            }


	            while (!(onDest)&&(myRC.isMovementActive()))
	            {
	            	myRC.yield();
	            }

	            if (!(onDest)&&(myRC.canMove(myRC.getDirection())))
	       	 	{
	            	 if(myRC.getDirection().equals(myRC.getLocation().directionTo(dest)))
	            	 {
	            		 myRC.moveForward();
	            	 } else
	            	 {
	            		 myRC.setDirection(myRC.getLocation().directionTo(dest));
	                     myRC.yield();
	            	 }
	       	 	}

	             waitUntilMovementIdle();

	             if (!(onDest)&&(!myRC.canMove(myRC.getDirection())))
	             {
	                 if(myRC.getDirection().equals(myRC.getLocation().directionTo(dest)))
	            	 {
	            		 randomRun(6);
	            	 } else
	            	 {
	            		 myRC.setDirection(myRC.getLocation().directionTo(dest));
	                     myRC.yield();
	            	 }
	             }
	            transferEnergon();
	            myRC.yield();

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
				   waitUntilMovementIdle();
				   myRC.yield();
			   }
			myRC.moveForward();
			myRC.yield();	  
	}
	 
	protected void sendMessage(int i) throws GameActionException{
	        Message msgout;
	    	msgout = new Message();
	        msgout.strings = new String[2];
	        msgout.locations = new MapLocation[1];
	        switch(i){
	            case 0:
	            	msgout.strings[0]=myRC.getTeam().toString();
	            	msgout.strings[1]="MQ_WORK";
	                break;
	            case 1:
	            	msgout.strings[0]=myRC.getTeam().toString();
	                msgout.strings[1]="MQ_DEFENSE";
	                msgout.locations[0] = enemyLocation();
	                break;
	            case 2:
	            	msgout.strings[0]=myRC.getTeam().toString();
	            	msgout.strings[1]="MQ_ATTACK";
	                msgout.locations[0] = enemyLocation();
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
	            case 5:
	            	msgout.strings[0]=myRC.getTeam().toString();
	            	msgout.strings[1]="MQ_PATROL";
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
				 && (msg.strings[1] == "MQ_WORK" ||
					 msg.strings[1] == "MQ_DEFENSE" ||
					 msg.strings[1] == "MQ_ATTACK" ||
					 msg.strings[1] == "MQ_NEED_HELP" ||
					 msg.strings[1] == "MQ_FOLLOW" ||
					 msg.strings[1] == "MQ_PATROL")); 
					 
	}
	
	 protected void goNear(MapLocation dest){

	      boolean onDest = false;

	      while(true){
	         try{
	            
	             myRC.yield();

	            if (myRC.getLocation().isAdjacentTo(dest))
	            {
	            	onDest = true;
	                myRC.yield();
	                break;
	            }


	            while (!(onDest)&&(myRC.isMovementActive()))
	            {
	            	myRC.yield();
	            }

	            if (!(onDest)&&(myRC.canMove(myRC.getDirection())))
	       	 	{
	            	 if(myRC.getDirection().equals(myRC.getLocation().directionTo(dest)))
	            	 {
	            		 myRC.moveForward();
	                     myRC.yield();
	            	 } else
	            	 {
	                     if(!myRC.getLocation().directionTo(dest).equals(Direction.OMNI)&&!myRC.getLocation().directionTo(dest).equals(Direction.NONE)){
	                        myRC.setDirection(myRC.getLocation().directionTo(dest));
	                        myRC.yield();
	                     }
	            	 }
	       	 	 }

	              waitUntilMovementIdle();


	             if (!(onDest)&&(!myRC.canMove(myRC.getDirection())))
	             {
	                 if(myRC.getDirection().equals(myRC.getLocation().directionTo(dest)))
	            	 {
	            		 randomRun(6);
	                     myRC.yield();
	            	 } else
	            	 {
	                     if(!myRC.getLocation().directionTo(dest).equals(Direction.OMNI)&&!myRC.getLocation().directionTo(dest).equals(Direction.NONE)){
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
	            	 }else
	            	 {
	                     myRC.setDirection(myRC.getDirection().rotateLeft());
	                     myRC.yield();
	                 }
	            }
	       	 	transferEnergon();
	            myRC.yield();

	         }catch(Exception e) {
	            System.out.println("caught exception:");
	            e.printStackTrace();
	         }
	      }

	   }
	 
   protected int numberOfNearbyRobots (RobotType typeOfRobot) throws GameActionException{
	   myRC.yield();
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
	   myRC.yield();
		for (Robot robot : myRC.senseNearbyGroundRobots()) {
			if (myRC.canSenseObject(robot)){
				RobotInfo robotInfo = myRC.senseRobotInfo(robot);

				if (robotInfo.team == myRC.getTeam()) {
					MapLocation robotLoc = robotInfo.location;

					if ((myRC.getLocation().isAdjacentTo(robotLoc))&&(3*robotInfo.maxEnergon > 4*robotInfo.energonLevel) && !(myRC.senseGroundRobotAtLocation(robotLoc).equals(null))){
						myRC.transferEnergon(Math.max(Math.min(robotInfo.maxEnergon-robotInfo.eventualEnergon, myRC.getEnergonLevel()/2),0), robotLoc, RobotLevel.ON_GROUND);
						myRC.yield();
						break;
					}
				}
			}
		}
  }
   protected void transferToArchon() throws GameActionException {
	   myRC.yield();
		for (Robot robot : myRC.senseNearbyAirRobots()) {
			if (myRC.canSenseObject(robot)){
				RobotInfo robotInfo = myRC.senseRobotInfo(robot);

				if (robotInfo.team == myRC.getTeam()) {
					MapLocation robotLoc = robotInfo.location;

					if ((myRC.getLocation().isAdjacentTo(robotLoc))&&(3*robotInfo.maxEnergon > 4*robotInfo.energonLevel) && !(myRC.senseAirRobotAtLocation(robotLoc).equals(null))){
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
  			if (myRC.canSenseObject(robot) && !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
  				find=true;
  				break;
  			}
  		}
  		if(!find){
  			for(Robot robot : airRobots){
  				if (myRC.canSenseObject(robot) && !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
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

   		for (Robot robot : groundRobots){
   			if (myRC.canSenseObject(robot) && !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
    				RobotInfo robotInfo = myRC.senseRobotInfo(robot);
    				if (minLen > myRC.getLocation().distanceSquaredTo(robotInfo.location)){
   	    					nearestEnemy = robotInfo;
   	    					minLen = myRC.getLocation().distanceSquaredTo(robotInfo.location);
    				}
    				if (myRC.getLocation().add(myRC.getDirection()).equals(robotInfo.location)){
    					nearestEnemy = robotInfo;
    					return nearestEnemy;
    				}
    			}
    		}
		for (Robot robot : airRobots){
			if (myRC.canSenseObject(robot) && !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
    				RobotInfo robotInfo = myRC.senseRobotInfo(robot);
    				if (minLen > myRC.getLocation().distanceSquaredTo(robotInfo.location)){
	    					nearestEnemy = robotInfo;
	    					minLen = myRC.getLocation().distanceSquaredTo(robotInfo.location);
    				}
    				if (myRC.getLocation().add(myRC.getDirection()).equals(robotInfo.location)){
    					nearestEnemy = robotInfo;
    					return nearestEnemy;
    				}
			}
		}	
		return nearestEnemy;    	    			
	  }
   
   protected MapLocation enemyLocation() throws GameActionException{
   	Robot[] groundRobots = myRC.senseNearbyGroundRobots();
   	Robot[] airRobots = myRC.senseNearbyAirRobots();
   	MapLocation nearestEnemy = null;
   	int minLen=9999;

  		for (Robot robot : groundRobots){
  			if (myRC.canSenseObject(robot) && !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
   				RobotInfo robotInfo = myRC.senseRobotInfo(robot);
   				if (minLen > myRC.getLocation().distanceSquaredTo(robotInfo.location)){
  	    					nearestEnemy = robotInfo.location;
  	    					minLen = myRC.getLocation().distanceSquaredTo(robotInfo.location);
   				}   	    				
   			}
   		}
		for (Robot robot : airRobots){
			if (myRC.canSenseObject(robot) && !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
   				RobotInfo robotInfo = myRC.senseRobotInfo(robot);
   				if (minLen > myRC.getLocation().distanceSquaredTo(robotInfo.location)){
	    					nearestEnemy = robotInfo.location;
	    					minLen = myRC.getLocation().distanceSquaredTo(robotInfo.location);
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
	

	
	
