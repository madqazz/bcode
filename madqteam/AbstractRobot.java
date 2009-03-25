package madqteam;

import java.util.Random;
import battlecode.common.*;


public abstract class AbstractRobot {
	protected RobotController myRC;
	protected enum RobotState { 
		ARCHON_START,
		ARCHON_FIND_FLUX, 
		ARCHON_ON_FLUX, 
		ARCHON_DEFENSE,
		ARCHON_ATTACK,
		SOLDIER_FOLLOW,
		SOLDIER_DEFENSE,
		SOLDIER_ATTACK,
	};
	protected RobotState state;
	protected Random generator;
	
	public AbstractRobot(RobotController rc) {
		myRC = rc;
		generator = new Random();
	}
	
	abstract public void run() throws GameActionException; 
	
	 protected void randomRun(int rounds){
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
	        msgout.strings = new String[1];
	        msgout.locations = new MapLocation[1];
	        switch(i){
	            case 0:
	            	msgout.strings[0]="WORK";
	                break;
	            case 1:
	                msgout.strings[0]="DEFENSE";
	                msgout.locations[0] = enemyLocation();
	                break;
	            case 2:
	            	msgout.strings[0]="ATTACK";
	                msgout.locations[0] = enemyLocation();
	                break;
	            case 3:
	            	msgout.strings[0]="NEED_HELP";
	                msgout.locations[0] = myRC.getLocation();
	                break;
	            case 4:
	            	msgout.strings[0]="FOLLOW";
	            	break;
	        }
		           	 myRC.broadcast(msgout);
		           	 myRC.yield();

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
	 
   
   protected double min (double a, double b){
	   if (a < b) 
		   {
			return a;   
		   } else {
			   return b;
		   } 
	   } 
	   
	   
   protected void transferEnergon() throws GameActionException {
	   myRC.yield();
		for (Robot robot : myRC.senseNearbyGroundRobots()) {
			if (myRC.canSenseObject(robot)){
				RobotInfo robotInfo = myRC.senseRobotInfo(robot);

				if (robotInfo.team == myRC.getTeam()) {
					MapLocation robotLoc = robotInfo.location;

					if ((myRC.getLocation().isAdjacentTo(robotLoc))&&(3*robotInfo.maxEnergon > 4*robotInfo.energonLevel) && !(myRC.senseGroundRobotAtLocation(robotLoc).equals(null))){
						myRC.transferEnergon(min(robotInfo.maxEnergon-robotInfo.eventualEnergon, myRC.getEnergonLevel()/2), robotLoc, RobotLevel.ON_GROUND);
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
    			}
    		}
		for (Robot robot : airRobots){
			if (myRC.canSenseObject(robot) && !myRC.senseRobotInfo(robot).team.equals(myRC.getTeam())){
    				RobotInfo robotInfo = myRC.senseRobotInfo(robot);
    				if (minLen > myRC.getLocation().distanceSquaredTo(robotInfo.location)){
	    					nearestEnemy = robotInfo;
	    					minLen = myRC.getLocation().distanceSquaredTo(robotInfo.location);
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
	

	
	
