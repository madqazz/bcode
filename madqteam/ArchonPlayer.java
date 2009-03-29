package madqteam;

import battlecode.common.*;
import madqteam.AbstractRobot;
import madqteam.AbstractRobot.RobotState;


//import static battlecode.common.GameConstants.*;

public class ArchonPlayer extends AbstractRobot {

   private final RobotController myRC;
   

   public ArchonPlayer(RobotController rc) {
	  super(rc);
      myRC = rc;
	  state = RobotState.ARCHON_FIND_FLUX;
   }
    
    private void findFLux(){   	
    	int archonNumber=0, soldierNumber=0;
     
         try{
         	 
        	
            if (myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE))
            {
            	state = RobotState.ARCHON_TAKE_FLUX;
                return;
            }
        	 
		  myRC.yield();
		  transferEnergon();
       	
        	
 		   //soldierNumber = numberOfNearbyRobots(RobotType.SOLDIER);
		   //archonNumber = numberOfAirRobots(RobotType.ARCHON)+1;
		   
	       //if(soldierNumber < 3*archonNumber){
		  if(2*myRC.getEnergonLevel() > myRC.getMaxEnergonLevel()){
	    	   spawnSoldier();
		  }
	    		  // for(int i=1;i<10;i++){
	    		//	   myRC.yield();
	    		 //  }
	    	//   }
	    	  // transferEnergon();
	    	   //sendMessage(2);
	       //}

	       transferEnergon();
	       
	       
	     	if (checkEnemy()){
    			//state=RobotState.ARCHON_ATTACK;
    			//return;
	    		   spawnSoldier();
	    		  // transferEnergon();
	    		  // for(int i=1;i<5;i++){
	    		//	   myRC.yield();
	    			   transferEnergon();
	    				sendMessage(2);
	    				  
	    		//   }
		   }
	     	
	    	if (checkEnemy()){
	    		archonRun(5);
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
            		 archonRun(10);
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
    
    
    private void takeFLux(){   	
    	int archonNumber=0, soldierNumber=0;
     
         try{
         	 
        	
            if (myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.NONE))
            {
            	state = RobotState.ARCHON_FIND_FLUX;
                return;
            }
            
            if (myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.OMNI))
            {
            	state = RobotState.ARCHON_ON_FLUX;
                return;
            }
        	 
		  myRC.yield();
     


            while (myRC.isMovementActive())
            {
            	myRC.yield();
            }


            if (myRC.canMove(myRC.getDirection()))
       	 	{
            	 if(myRC.getDirection().equals(myRC.senseDirectionToOwnedFluxDeposit()))
            	 {
            		 waitUntilMovementIdle();
            		 myRC.moveForward();
            		 myRC.yield();
            	 } else
            	 {
            		 waitUntilMovementIdle();
            		 if(!myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.OMNI) && !myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.NONE)){
            			 myRC.setDirection(myRC.senseDirectionToOwnedFluxDeposit());
            			 myRC.yield();
            		 }
            	 }
       	 	}

             if (!myRC.canMove(myRC.getDirection())){
                 if(myRC.getDirection().equals(myRC.senseDirectionToOwnedFluxDeposit()))
            	 {
                	 waitUntilMovementIdle();
            		 if(!myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.OMNI) && !myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.NONE)){
            			 myRC.setDirection(myRC.senseDirectionToOwnedFluxDeposit().opposite());
            			 myRC.yield();
            		 }
            		 randomRun(80);
            	 } else
            	 {
            		 waitUntilMovementIdle();
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
    	int workerNumber = 0, soldierNumber = 0;

   	/*
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
    	
	   soldierNumber = numberOfNearbyRobots(RobotType.SOLDIER);*/
	   workerNumber = numberOfNearbyRobots(RobotType.WORKER);
	   /*
	     if(soldierNumber < 1){
	    	   spawnSoldier();
	    	   transferEnergon();
	    	   sendMessage(4);
	     }
	     */
		 transferEnergon();
	     if(workerNumber < 4){
	       spawn();
	       transferEnergon();
	      }
		  transferEnergon();
       /* 
	   if(generator.nextInt(20)==1){
		   sendMessage(4);
	   }
	   */
    }
    
    
   private void attack() throws GameActionException{
		   int soldierNumber = 0;
		   int archonNumber = 0; 
		   
		   if(!checkEnemy() && generator.nextInt(15)==1){
			   sendMessage(0);
			   state=RobotState.ARCHON_FIND_FLUX;
			   return;
		   }
		   
		   transferEnergon();  

		   soldierNumber = numberOfNearbyRobots(RobotType.SOLDIER);
		   archonNumber = numberOfAirRobots(RobotType.ARCHON)+1;
		   
	       if(soldierNumber < 3*archonNumber){
	    	   spawnSoldier();
	    	   transferEnergon();
	    	   //sendMessage(2);
	       }else{
			   if(generator.nextInt(10)==1){
				   sendMessage(2);
			   }
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

	   soldierNumber = numberOfNearbyRobots(RobotType.SOLDIER);
	   
       if(soldierNumber < 3){
    	   spawnSoldier();
       }
	   transferEnergon();  
   }



	private void spawn() throws GameActionException {

		if(3*myRC.getEnergonLevel() > myRC.getMaxEnergonLevel()){
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
}
	
	
	private boolean spawnSoldier() throws GameActionException {

		
		boolean done=false;
		if(4*myRC.getEnergonLevel() > 3*myRC.getMaxEnergonLevel()){
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
		}
		return done;
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

