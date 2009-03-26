package madqteam;

import battlecode.common.*;
import madqteam.AbstractRobot;
//import madqteam.AbstractRobot.RobotState;



public class WorkerPlayer extends AbstractRobot {

 private final RobotController myRC;
    private MapLocation myFlux=null;
    private MapLocation myLoc=null;
    private MapLocation myLocNext=null;

 public WorkerPlayer(RobotController rc) {
	 super(rc);
    myRC = rc;
 }
 
 
 public void unloadBlock() throws GameActionException{
     MapLocation blockdest = myFlux;

     goTo(myLocNext);
     goTo(myLoc);
     
     
 	if (checkEnemy()){
 		sendMessage(4);
 	}
    
	Message msg=myRC.getNextMessage();
	  
  	if (!(msg==null) && (msg.strings==null)){
  		if (msg.strings[0] == "ENEMY_SPOTTED"){
  			  sendMessage(4);
  		  }
  	}
     
     

	 while(true){
		 try{

             if(myRC.getNumBlocks()==0){
                 break;
             }

              waitUntilMovementIdle();

              if(!myRC.getDirection().equals(myRC.getLocation().directionTo(blockdest))){
                     myRC.setDirection(myRC.getLocation().directionTo(blockdest));
                     myRC.yield();
              }

              waitUntilMovementIdle();

              myRC.yield(); 
              if (myRC.canUnloadBlockToLocation(myRC.getLocation().add(myRC.getLocation().directionTo(myFlux)))){
                     myRC.unloadBlockToLocation(myRC.getLocation().add(myRC.getLocation().directionTo(myFlux)));
                     myRC.yield();
                     break;
              }else{
                     if(myRC.getLocation().equals(myLoc)){
                         goTo(myLocNext);
                         waitUntilMovementIdle();
                         myRC.setDirection(myRC.getLocation().directionTo(myLoc).opposite());
                         myRC.yield();
                         if (myRC.canUnloadBlockToLocation(myLoc)){
                            myRC.unloadBlockToLocation(myLoc);
                            myRC.yield();
                            break;
                        }
                     }
                      if(myRC.getLocation().equals(myLocNext)){
                         goNear(myLocNext);
                         waitUntilMovementIdle();
                         myRC.setDirection(myRC.getLocation().directionTo(myLocNext).opposite());
                         myRC.yield();
                         if (myRC.canUnloadBlockToLocation(myLocNext)){
                            myRC.unloadBlockToLocation(myLocNext);
                            myRC.yield();
                            break;
                        }
                      }

                     myRC.setDirection(myRC.getLocation().directionTo(myFlux).opposite());
                     myRC.yield();

                    waitUntilMovementIdle();

                    while (!(myRC.canMove(myRC.getDirection()))){
                        myRC.setDirection(myRC.getDirection().rotateRight());
                        myRC.yield();
                    }

                    waitUntilMovementIdle();

                    myRC.moveForward();
                    myRC.yield();

                    waitUntilMovementIdle();

               }
               transferEnergon();

       }catch(Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
       }
	 }
 }


 public void blockLoad() throws GameActionException {

	 MapLocation myBlock = null ;
	 MapLocation blocks[] = null;
	 boolean check = false;
	 int maxLen=-9999;
	 
	if (checkEnemy()){
		sendMessage(4);
	}
	    
	Message msg=myRC.getNextMessage();
		  
	if (!(msg==null) && !(msg.strings[0]==null)){
		if (msg.strings[0] == "ENEMY_SPOTTED"){
			  sendMessage(4);
		  }
	}
	 

    while(true){
       try{

           	waitUntilMovementIdle();

    	   if (!check)
    	   {
    	    blocks = myRC.senseNearbyBlocks();
    	    check = true;
    	   }
    	   
           if(myRC.getNumBlocks() > 0){
               break;
           }

    	   for(MapLocation block : blocks){
   				 if (!(block.equals(myFlux))&&(!(block.isAdjacentTo(myFlux)))&&(!(block.equals(myRC.getLocation())))){
   					 if ( maxLen < myFlux.distanceSquaredTo(block))
   					 {
   						 maxLen=myRC.getLocation().distanceSquaredTo(block);
   						 myBlock=block;
   					 }
   				 }
   			}

           if(blocks.length ==0 || myBlock == null){
               randomRun(10);
               check=false;
           }

            if (!(myBlock == null)){


                goNear(myBlock);

                waitUntilMovementIdle();


                if(!myRC.getDirection().equals(myRC.getLocation().directionTo(myBlock))){
                     myRC.setDirection(myRC.getLocation().directionTo(myBlock));
                 }

                waitUntilMovementIdle();

                myRC.yield();
                 if(myRC.canLoadBlockFromLocation(myBlock)){
                     myRC.loadBlockFromLocation(myBlock);
                     break;
                 }else{
                	 randomRun(3);
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
 

 public void run() {


    try {
            myFlux = findFlux();
        }catch(Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
       }

    myLoc = myRC.getLocation();
    myLocNext=null;

    while(myLocNext==null){
        if(myRC.canMove(myLoc.directionTo(myFlux).opposite())){
            myLocNext=myLoc.add(myLoc.directionTo(myFlux).opposite());
            break;
        }
        if(myRC.canMove(myLoc.directionTo(myFlux).opposite().rotateLeft())){
            myLocNext=myLoc.add(myLoc.directionTo(myFlux).opposite().rotateLeft());
            break;
        }
        if(myRC.canMove(myLoc.directionTo(myFlux).opposite().rotateRight())){
            myLocNext=myLoc.add(myLoc.directionTo(myFlux).opposite().rotateRight());
            break;
        }
    }

    while(true){
       try{
            blockLoad();
            transferEnergon();
            unloadBlock();
            transferEnergon();

       }catch(Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
       }
    }
 }
}