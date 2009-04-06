package madqteamlite;

import battlecode.common.*;
import madqteamlite.AbstractRobot;
import madqteamlite.AbstractRobot.RobotState;


//import static battlecode.common.GameConstants.*;

public class ArchonPlayer extends AbstractRobot {

   private final RobotController myRC;
   

   public ArchonPlayer(RobotController rc) {
	  super(rc);
      myRC = rc;
	  state = RobotState.ARCHON_FIND_FLUX;
   }
    
   
    private void findFLux(){   	     
         try{
         	 
        	
            if (myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE)){
            	state = RobotState.ARCHON_TAKE_FLUX;
                return;
            }   

	        spawnSoldier();
	        myRC.yield();
	        transferEnergon();
            
		   	if(checkEnemy()){	     		
				sendMessage(2);
				myRC.yield();
	    		archonRun(20);
	    		return;
		    }

	       
          //  if (myRC.isMovementActive()){
          //  	myRC.yield();
          //  }
            
   		    waitUntilMovementIdle();

            if (myRC.canMove(myRC.getDirection()))
       	 	{
            	 if(myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit())){
            		 myRC.moveForward();
            		 myRC.yield();
            	 } else {
            		 if(!myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.OMNI) && !myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE)){
            			 myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
            			 myRC.yield();
            		 }
            	 }
       	 	} else {
                 if(myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit())){
            		 archonRun(10);
            	 } else {
            		 if(!myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.OMNI)
            		    && !myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE)){
            			  myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
            			  myRC.yield();
            		 }
            	 }
             }

         }catch(Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
         }
      }
    
    
    private void takeFLux(){   	

         try{

            if (!myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE)){
            	state = RobotState.ARCHON_FIND_FLUX;
                return;
            }
            if (myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.OMNI)){
            	state = RobotState.ARCHON_ON_FLUX;
                return;
            }
        	 

   		   waitUntilMovementIdle();
            
            if (myRC.canMove(myRC.getDirection()))
       	 	{
            	 if(myRC.getDirection().equals(myRC.senseDirectionToOwnedFluxDeposit())){
            		 myRC.moveForward();
            		 myRC.yield();
            	 } else {
            		 if(!myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.OMNI)
            			&& !myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.NONE)){
            			  myRC.setDirection(myRC.senseDirectionToOwnedFluxDeposit());
            			  myRC.yield();
            		 }
            	 }
       	 	} else {
                 if(myRC.getDirection().equals(myRC.senseDirectionToOwnedFluxDeposit())){
            		 if(!myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.OMNI)
            		    && !myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.NONE)){
            			  myRC.setDirection(myRC.senseDirectionToOwnedFluxDeposit().opposite());
            			  myRC.yield();
            		 }
            		 randomRun(80);
            	 } else {
            		 if(!myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.OMNI) && !myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.NONE)){
            			 myRC.setDirection(myRC.senseDirectionToOwnedFluxDeposit());
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

    	Message msg = myRC.getNextMessage();
    	int workerNumber = 0;

    	myRC.yield();
    	
    	if (!(msg==null) && !(msg.strings==null) && msg.strings.length == 2){
    	  	if (msg.strings[1] == "MQ_NEED_HELP"){
    	   		state=RobotState.ARCHON_DEFENSE;
        		return;
    	  	  }
    	  	if (!ourMessage(msg)){
    	   		state=RobotState.ARCHON_DEFENSE;
        		return;
    	  	}
    	}
    	
    	if (checkEnemy()){
			state=RobotState.ARCHON_DEFENSE;
			return;
    	}

	   workerNumber = numberOfNearbyRobots(RobotType.WORKER);

	   if(workerNumber < 4){
	       spawn();
	   }
	   transferEnergon();
    }
   
   private void defense() throws GameActionException{

	   int soldierNumber = 0;	   
	   if(!checkEnemy() && generator.nextInt(5)==1){
		   sendMessage(0);
		   state=RobotState.ARCHON_ON_FLUX;
		   return;
	   }else{
		   if (generator.nextInt(150)==1){
			   sendMessage(1);
		   }
	   }
	   transferEnergon();  

	   myRC.yield();
	   soldierNumber = numberOfNearbyRobots(RobotType.SOLDIER);
	   
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
                		&& myRC.getEnergonLevel() > 2*RobotType.WORKER.spawnCost()
                		&& !myRC.senseTerrainTile(loc).getType().equals(TerrainTile.TerrainType.OFF_MAP)
                		&& terrain.isTraversableAtHeight(RobotLevel.ON_GROUND)) {
                	
                    if(!myRC.getDirection().equals(dir)){
                        waitUntilMovementIdle();
                        myRC.setDirection(dir);
                        myRC.yield();
                    }
                    waitUntilMovementIdle();
                    if (myRC.senseGroundRobotAtLocation(loc) == null 
                    		&& myRC.getEnergonLevel() > 2*RobotType.WORKER.spawnCost() ) {
                    myRC.spawn(RobotType.WORKER);
                    myRC.yield();
                    break;
                    }
			}
		}
	}
}
	
	
	private void spawnSoldier() throws GameActionException {

		for (Direction dir : Direction.values()) {
			if (!dir.equals(Direction.OMNI) && !dir.equals(Direction.NONE)) {
				MapLocation loc = myRC.getLocation().add(dir);
				TerrainTile terrain = myRC.senseTerrainTile(loc);

                if (myRC.senseGroundRobotAtLocation(loc) == null 
                		&& myRC.getEnergonLevel() > 2*RobotType.SOLDIER.spawnCost()
                		&& !myRC.senseTerrainTile(loc).getType().equals(TerrainTile.TerrainType.OFF_MAP) 
                		&& terrain.isTraversableAtHeight(RobotLevel.ON_GROUND)) {
                	
                    if(!myRC.getDirection().equals(dir)){
                        waitUntilMovementIdle();
                        myRC.setDirection(dir);
                        myRC.yield();
                    }
                    waitUntilMovementIdle();
                    if (myRC.senseGroundRobotAtLocation(loc) == null
                    		&& myRC.getEnergonLevel() > 2*RobotType.SOLDIER.spawnCost()) {
                    	myRC.spawn(RobotType.SOLDIER);
                    	myRC.yield();
                    	break;
                    }
                }
			}
		}
}
	


   public void run() {
	   updateStatus();
	   switch (state) {
	   		case ARCHON_FIND_FLUX:
				   	findFLux();
				   	break;
	   		case ARCHON_TAKE_FLUX:
	   				takeFLux();
	   				break;
	   		case ARCHON_ON_FLUX:
	   				try {
	   					onFlux();
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

