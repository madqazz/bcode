package madqteam;

import battlecode.common.*;


public class WorkerPlayer implements Runnable {

 private final RobotController myRC;
    private MapLocation myFlux=null;
    private MapLocation myLoc=null;
    private MapLocation myLocNext=null;
    private Message msg=null;

 public WorkerPlayer(RobotController rc) {
    myRC = rc;
 }
 
 public void randomRun(){


     for(int i=0;i<5;i++){
	   try{

		             if(myRC.isMovementActive()) {
		                myRC.yield();
		             } else {
		            	 
		            	 if(myRC.canMove(myRC.getDirection()))
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
 

    public void goNear(MapLocation dest){

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
            		 randomRun();
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
 
 
   public void goTo(MapLocation dest){

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
            		 randomRun();
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
 
 public void unloadBlock() throws GameActionException{

	 MapLocation[] dest= new MapLocation[10];
	 dest[1] = myFlux;
	 dest[2] = myFlux.add(Direction.EAST);
	 dest[3] = myFlux.add(Direction.NORTH_EAST);
	 dest[4] = myFlux.add(Direction.NORTH);
	 dest[5] = myFlux.add(Direction.NORTH_WEST);
	 dest[6] = myFlux.add(Direction.WEST);
	 dest[7] = myFlux.add(Direction.SOUTH_WEST);
	 dest[8] = myFlux.add(Direction.SOUTH);
	 dest[9] = myFlux.add(Direction.SOUTH_EAST);
     MapLocation blockdest = myFlux;

     goTo(myLocNext);
     goTo(myLoc);

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

               
              if (myRC.canUnloadBlockToLocation(myRC.getLocation().add(myRC.getLocation().directionTo(myFlux)))){
                     myRC.yield();
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


 public void sabotageUnloadBlock() throws GameActionException{

	 MapLocation[] dest= new MapLocation[10];

	 while(true){
		 try{

             if(myRC.getNumBlocks()==0){
                 break;
             }
              waitUntilMovementIdle();


              if (myRC.canUnloadBlockToLocation(myRC.getLocation())){
                     myRC.yield();
                     myRC.unloadBlockToLocation(myRC.getLocation());
                     myRC.yield();
                     break;
              }else{
                     
                    
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

 public void blockLoad() {

	 MapLocation myBlock = null ;
	 MapLocation blocks[] = null;
	 boolean check = false, done = false;
	 int maxLen=-9999;

    while(true){
       try{

           	waitUntilMovementIdle();

    	   if (!check)
    	   {
    	    blocks = myRC.senseNearbyBlocks();
    	    check = true;
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
               randomRun();
               check=false;
           }

            if (!(myBlock == null)){


                goNear(myBlock);

                waitUntilMovementIdle();


                if(!myRC.getDirection().equals(myRC.getLocation().directionTo(myBlock))){
                     myRC.setDirection(myRC.getLocation().directionTo(myBlock));
                 }

                waitUntilMovementIdle();

                 if(myRC.canLoadBlockFromLocation(myBlock)){
                     myRC.yield();
                     myRC.loadBlockFromLocation(myBlock);
                     break;
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
 
 
 public void sabotageblockLoad() {
	 
	 MapLocation myBlock = null ;
	 MapLocation blocks[] = null;
	 boolean check = false, done = false;
	 int minLen=9999;
	 
    while(true){
       try{
           
           	waitUntilMovementIdle();

    	   if (!check) 
    	   {
    	    blocks = myRC.senseNearbyBlocks();
    	    check = true;
    	   }
    	    
    	   for(MapLocation block : blocks){
   				 if (!(block.equals(myFlux))&&(!(block.equals(myRC.getLocation())))){
   					 if ( minLen > myFlux.distanceSquaredTo(block))
   					 {
   						 minLen=myRC.getLocation().distanceSquaredTo(block);
   						 myBlock=block;
   					 }
   				 }
   			}

            if (!(myBlock == null)){

                 
                goNear(myBlock);

                waitUntilMovementIdle();


                if(!myRC.getDirection().equals(myRC.getLocation().directionTo(myBlock))){
                     myRC.setDirection(myRC.getLocation().directionTo(myBlock));
                 }
             
                waitUntilMovementIdle();
                
                 if(myRC.canLoadBlockFromLocation(myBlock)){
                     myRC.yield();
                     myRC.loadBlockFromLocation(myBlock);
                     break;
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


  public void bobTheBuilder() {

     MapLocation[] dest= new MapLocation[10];
	 dest[0] = myFlux;
	 dest[1] = myFlux.add(Direction.EAST);
	 dest[2] = myFlux.add(Direction.NORTH_EAST);
	 dest[3] = myFlux.add(Direction.NORTH);
	 dest[4] = myFlux.add(Direction.NORTH_WEST);
	 dest[5] = myFlux.add(Direction.WEST);
	 dest[6] = myFlux.add(Direction.SOUTH_WEST);
	 dest[7] = myFlux.add(Direction.SOUTH);
	 dest[8] = myFlux.add(Direction.SOUTH_EAST);
     int i=1;
     int bobState=0;

    while(true){
       try{

           switch(bobState){
               case 0:

                   if(myRC.getNumBlocks() > 0){
                       bobState=1;
                       break;
                   }

                    i++;
                    if (i>8){
                        i=1;
                    }

                    waitUntilMovementIdle();
                    myRC.setDirection(myRC.getLocation().directionTo(dest[i]));
                    myRC.yield();
                    waitUntilMovementIdle();

                    if(myRC.canLoadBlockFromLocation(dest[i])){
                        waitUntilMovementIdle();
                        myRC.loadBlockFromLocation(dest[i]);
                        bobState=1;
                        myRC.yield();
                        break;
                    }

                    goTo(dest[i]);
                    transferEnergon();
                    break;
                    
               case 1:

                   if(myRC.getNumBlocks() == 0){
                       bobState=0;
                       break;
                   }

                   while(!myRC.getLocation().equals(myLoc)){
                       i--;
                       if (i<1){
                        i=8;
                       }
                       goTo(dest[i]);
                       transferEnergon();

                   }

                   waitUntilMovementIdle();
                   myRC.setDirection(myRC.getLocation().directionTo(dest[0]));
                   myRC.yield();
                   waitUntilMovementIdle();

                  if(myRC.canUnloadBlockToLocation(dest[0])){
                        waitUntilMovementIdle();
                        myRC.unloadBlockToLocation(dest[0]);
                        bobState=0;
                        myRC.yield();
                        break;
                    }



                   while(true){
                        i++;
                        if (i>8){
                            i=1;
                        }
                        goTo(dest[i]);
                        transferEnergon();
                        if(i==1){
                            if(myRC.canUnloadBlockToLocation(dest[8])){
                                waitUntilMovementIdle();
                                myRC.unloadBlockToLocation(dest[8]);
                                bobState=0;
                                myRC.yield();
                                break;
                       }
                        }else{
                            if(myRC.canUnloadBlockToLocation(dest[i-1])){
                                waitUntilMovementIdle();
                                myRC.unloadBlockToLocation(dest[i-1]);
                                bobState=0;
                                myRC.yield();
                                break;
                         }
                        }
                   }
                   break;

           }
       }catch(Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
       }
    }
 }


 private void waitUntilMovementIdle(){
          while(myRC.getRoundsUntilMovementIdle() > 0){
                     myRC.yield();
        }
 }  

    public double min (double a, double b)
   {
	   if (a < b)
	   {
		return a;
	   } else {
		   return b;
	   }

   } 


     private void transferEnergon() throws GameActionException {
		for (Robot robot : myRC.senseNearbyGroundRobots()) {
			RobotInfo robotInfo = myRC.senseRobotInfo(robot);

			if (robotInfo.team == myRC.getTeam()) {
				MapLocation robotLoc = robotInfo.location;

				if ((myRC.getLocation().isAdjacentTo(robotLoc))&&(3*robotInfo.maxEnergon > 4*robotInfo.energonLevel))
					myRC.transferEnergon(min(robotInfo.maxEnergon-robotInfo.energonLevel, myRC.getEnergonLevel()/2), robotLoc, RobotLevel.ON_GROUND);
			}
		}
	}


     private void blockFlux(){
         while(true){
             try {
             goTo(myFlux);
             transferEnergon();
             myRC.yield();

          }catch(Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
       }

    }
  }



     private void sabotageRun(){


         while(true){
            try{
               

            if (myRC.senseDirectionToOwnedFluxDeposit().equals(Direction.OMNI))
            {
              myRC.suicide();
            }

            while (myRC.isMovementActive())
            {
            	myRC.yield();
            }


            if (myRC.canMove(myRC.getDirection()))
       	 	{
            	 if(myRC.getDirection().equals(myRC.senseDirectionToOwnedFluxDeposit()))
            	 {
            		 myRC.moveForward();
            	 } else
            	 {
            		 myRC.setDirection(myRC.senseDirectionToOwnedFluxDeposit());
            	 }
       	 	}

             if (!myRC.canMove(myRC.getDirection()))
             {
                 if(myRC.getDirection().equals(myRC.senseDirectionToOwnedFluxDeposit()))
            	 {

            		 randomRun();
            	 } else
            	 {
            		 myRC.setDirection(myRC.senseDirectionToOwnedFluxDeposit());
            	 }
             }

            myRC.yield();

            /*** end of main loop ***/
          }catch(Exception e) {
           System.out.println("caught exception:");
           e.printStackTrace();
       }
      }
   }
  

     private void sabotage(){
        while(true){
            try{
            sabotageblockLoad();
            sabotageRun();
          //  sabotageUnloadBlock();

              }catch(Exception e) {
           System.out.println("caught exception:");
           e.printStackTrace();
            }
        }
     }
 

 public void run() {

    int state =0;

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

           	 while (msg == null){
                msg = myRC.getNextMessage();
             }

             if (msg.strings[0].equals("1")){
                state=1;
             }
             if (msg.strings[0].equals("2")){
                state=2;
             }
             if (msg.strings[0].equals("3")){
                state=3;
             }


             switch(state){
                 case 0:
                        blockLoad();
                        transferEnergon();
                        unloadBlock();
                        transferEnergon();
                        break;
                 case 1:
                        bobTheBuilder();
                        break;
                 case 2:
                        sabotage();
                        break;
                 case 3:
                        blockFlux();
                        break;

             }

       }catch(Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
       }
    }
 }
}