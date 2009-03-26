package madqteam;

import battlecode.common.*;
import madqteam.AbstractRobot;
import madqteam.AbstractRobot.RobotState;


//import static battlecode.common.GameConstants.*;

public class ArchonPlayer extends AbstractRobot {

   private final RobotController myRC;
   private int workers =0;
   

   public ArchonPlayer(RobotController rc) {
	  super(rc);
      myRC = rc;
	  state = RobotState.ARCHON_FIND_FLUX;
   }
    
    private void findFLux(){   	
     
         try{
 
        /*	 
       	   Robot[] NearbyRobots = myRC.senseNearbyGroundRobots();
     	   int soldierNumber = 0;

     	   for (Robot robot : NearbyRobots){
    		   if(myRC.canSenseObject(robot)){
    			   RobotInfo robotInfo = myRC.senseRobotInfo(robot);
    			   if (robotInfo.team.equals(myRC.getTeam()) && robotInfo.type.equals(RobotType.SOLDIER)){
    				   soldierNumber++;
    			   }
    		   }
    	   }
    	   
           if(soldierNumber < 1){
        	   spawnSoldier();
        	   sendMessage(4);
           }
    	   transferEnergon();
        	*/
        	 

            if (myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.OMNI))
            {
            	state = RobotState.ARCHON_ON_FLUX;
        //    	sendMessage(2);
                return;
            }
            
        	if (checkEnemy()){
    			state=RobotState.ARCHON_ATTACK;
    			return;
        	}
            


            while (myRC.isMovementActive())
            {
            	myRC.yield();
            }


            if (myRC.canMove(myRC.getDirection()))
       	 	{
            	 if(myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit()))
            	 {
            		 waitUntilMovementIdle();
            		 myRC.moveForward();
            		 myRC.yield();
            	 } else
            	 {
            		 waitUntilMovementIdle();
            		 if(!myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.OMNI) && !myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE)){
            			 myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
            			 myRC.yield();
            		 }
            	 }
       	 	}

             if (!myRC.canMove(myRC.getDirection())){
                 if(myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit()))
            	 {
            		 randomRun(15);
                	 //myRC.yield();
            	 } else
            	 {
            		 waitUntilMovementIdle();
            		 if(!myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.OMNI) && !myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE)){
            			 myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
            			 myRC.yield();
            		 }
            	 }
            	 if(!myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit())){
            		 waitUntilMovementIdle();
            		 if(!myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.OMNI) && !myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE)){
            			 myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
            			 myRC.yield();
            		 }
            	 }
             }


            myRC.yield();

         }catch(Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
         }
      }
   
    
    protected void onFlux() throws GameActionException{

    	Robot[] NearbyRobots;
    	Message msg = myRC.getNextMessage();
    	int workerNumber = 0, soldierNumber = 0;

    	
    	if (!(msg==null)){
    		if (msg.strings[0]=="ARCHON_TO_DEFENSE"){
    			state=RobotState.ARCHON_DEFENSE;
    			return;
    		}
    	  	if (msg.strings[0] == "ENEMY_SPOTTED"){
    	   		state=RobotState.ARCHON_DEFENSE;
        		return;
    	  	  }
    	}
    	
    	if (checkEnemy()){
			state=RobotState.ARCHON_DEFENSE;
			return;
    	}
    	
        NearbyRobots = myRC.senseNearbyGroundRobots();
  	    for (Robot robot : NearbyRobots){
    	   if(myRC.canSenseObject(robot)){
    		   RobotInfo robotInfo = myRC.senseRobotInfo(robot);
    			   if (robotInfo.team.equals(myRC.getTeam()) && robotInfo.type.equals(RobotType.SOLDIER)){
    				   soldierNumber++;
    			   }
    			   if (robotInfo.team.equals(myRC.getTeam()) && robotInfo.type.equals(RobotType.WORKER)){
    				   workerNumber++;
    			   }
    		   }
    	   }
	     if(soldierNumber < 3){
	    	   spawnSoldier();
	    	   transferEnergon();
	    	   sendMessage(4);
	     }
	     
		 transferEnergon();
	     if(workerNumber < 5){
	       spawn();
	       transferEnergon();
	      }
		  transferEnergon();
        }
 
    
    
   private void attack() throws GameActionException{
 	       Robot[] nearbyRobots = myRC.senseNearbyGroundRobots();
		   int soldierNumber = 0;
		   
		   if(!checkEnemy() && generator.nextInt(15)==1){
			   sendMessage(0);
			   state=RobotState.ARCHON_FIND_FLUX;
			   return;
		   }else{
			   if (generator.nextInt(50)==1){
				   sendMessage(2);
			   }
		   }
		   transferEnergon();  

		   for (Robot robot : nearbyRobots){
			   if(myRC.canSenseObject(robot)){
				   RobotInfo robotInfo = myRC.senseRobotInfo(robot);
				   if (robotInfo.team.equals(myRC.getTeam()) && robotInfo.type.equals(RobotType.SOLDIER)){
					   soldierNumber++;
				   }
			   }
		   }
		   
	       if(soldierNumber < 3){
	    	   spawnSoldier();
	    	   transferEnergon();
	       }
		   transferEnergon();
      }
   

   
   private void defense() throws GameActionException{
	   Robot[] NearbyRobots = myRC.senseNearbyGroundRobots();
	   int soldierNumber = 0;	   
	   if(!checkEnemy() && generator.nextInt(15)==1){
		   sendMessage(0);
		   state=RobotState.ARCHON_ON_FLUX;
		   return;
	   }else{
		   if (generator.nextInt(150)==1){
			   sendMessage(1);
		   }
	   }
	   transferEnergon();  

	   for (Robot robot : NearbyRobots){
		   if(myRC.canSenseObject(robot)){
			   RobotInfo robotInfo = myRC.senseRobotInfo(robot);
			   if (robotInfo.team.equals(myRC.getTeam()) && robotInfo.type.equals(RobotType.SOLDIER)){
				   soldierNumber++;
			   }
		   }
	   }
	   
       if(soldierNumber < 3){
    	   spawnSoldier();
       }
	   transferEnergon();  
   }



	private void spawn() throws GameActionException {

		for (Direction dir : Direction.values()) {
			if (!dir.equals(Direction.OMNI) && !dir.equals(Direction.NONE)) {
				MapLocation loc = myRC.getLocation().add(dir);
				TerrainTile terrain = myRC.senseTerrainTile(loc);

                if (myRC.senseGroundRobotAtLocation(loc) == null 
                		&& myRC.getEnergonLevel() > RobotType.WORKER.spawnCost()
                		&& !myRC.senseTerrainTile(loc).getType().equals(TerrainTile.TerrainType.OFF_MAP)
                		&& terrain.isTraversableAtHeight(RobotLevel.ON_GROUND)) {
                	
                    if(!myRC.getDirection().equals(dir)){
                        waitUntilMovementIdle();
                        myRC.setDirection(dir);
                        myRC.yield();
                    }
                    waitUntilMovementIdle();
                    if (myRC.senseGroundRobotAtLocation(loc) == null && myRC.getEnergonLevel() > RobotType.WORKER.spawnCost() ) {
                    myRC.spawn(RobotType.WORKER);
                    myRC.yield();
                    break;
                    }
			}
		}
	}
}
	
/*	
	private void buildSoldiers(int number) throws GameActionException{
		int i=0;
		while(i<number){
			/*if (spawnSoldier()){
				i++;
			}		
			waitUntilMovementIdle();
			transferEnergon();
		}
	}
	*/
	
	private boolean spawnSoldier() throws GameActionException {

		boolean done=false;
		while(!done){
		for (Direction dir : Direction.values()) {
			if (!dir.equals(Direction.OMNI) && !dir.equals(Direction.NONE)) {
				MapLocation loc = myRC.getLocation().add(dir);
				TerrainTile terrain = myRC.senseTerrainTile(loc);

                if (myRC.senseGroundRobotAtLocation(loc) == null 
                		&& myRC.getEnergonLevel() > RobotType.SOLDIER.spawnCost()
                		&& !myRC.senseTerrainTile(loc).getType().equals(TerrainTile.TerrainType.OFF_MAP) 
                		&& terrain.isTraversableAtHeight(RobotLevel.ON_GROUND)) {
                	
                    if(!myRC.getDirection().equals(dir)){
                        waitUntilMovementIdle();
                        myRC.setDirection(dir);
                        myRC.yield();
                    }
                    waitUntilMovementIdle();
                    if (myRC.senseGroundRobotAtLocation(loc) == null && myRC.getEnergonLevel() > RobotType.SOLDIER.spawnCost()) {
                    	myRC.spawn(RobotType.SOLDIER);
                    	done=true;
                    	myRC.yield();
                    	break;
                    }
                }
			}
		}
		}
		return done;
}
	


   public void run() {
	   switch (state) {
	   		case ARCHON_FIND_FLUX:
				   	findFLux();
				   	break;
	   		case ARCHON_ON_FLUX:
	   				try {
	   					onFlux();
	   				} catch (GameActionException e) {
	   					e.printStackTrace();
	   				}
	   				break;
	   		case ARCHON_ATTACK:
	   				try {
	   					attack();
	   				} catch (GameActionException e) {
	   					e.printStackTrace();
	   				}
	   				break;
	   		case ARCHON_DEFENSE:
	   				try {
	   					defense();
	   				} catch (GameActionException e) {
	   					e.printStackTrace();
	   				}
	   				break;
	   			
	   		
				   
	   }
	
   }
}

