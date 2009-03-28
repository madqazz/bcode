package madqteam;

import battlecode.common.*;
import madqteam.AbstractRobot;
import madqteam.AbstractRobot.RobotState;



public class WorkerPlayer extends AbstractRobot {

 private final RobotController myRC;
    private MapLocation myFlux=null;
    private MapLocation myLoc=null;
    private MapLocation myLocNext=null;
    private MapLocation myLocNext2=null;

 public WorkerPlayer(RobotController rc) {
	 super(rc);
    myRC = rc;
 }
 
 
 public void unloadBlock() throws GameActionException{

     goTo(myLocNext);
     goTo(myLoc);         

	 while(true){
		 try{
			 
	       	 Message msg=myRC.getNextMessage();
	 		  
	       	 if (!(msg==null) && !ourMessage(msg) || checkEnemy()){
	       			sendMessage(3);
	       	 }

             if(myRC.getNumBlocks()==0){
                 break;
             }

              waitUntilMovementIdle();

              if(!myRC.getDirection().equals(myRC.getLocation().directionTo(myFlux)) && !myRC.getLocation().directionTo(myFlux).equals(Direction.OMNI)){
                     myRC.setDirection(myRC.getLocation().directionTo(myFlux));
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
                         myRC.setDirection(myRC.getLocation().directionTo(myLoc));
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

                     if (!myRC.getLocation().directionTo(myFlux).opposite().equals(Direction.OMNI)){
                    	 myRC.setDirection(myRC.getLocation().directionTo(myFlux).opposite());
                    	 myRC.yield();
                     }

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
	 

    while(true){
       try{
    	   
       	   Message msg=myRC.getNextMessage();
		  
       	   if (!(msg==null) && !ourMessage(msg) || checkEnemy()){
       			sendMessage(3);
       	   }

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
 //   	   updateStatus();
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