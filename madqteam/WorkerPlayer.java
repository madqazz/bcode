package madqteam;

import battlecode.common.*;

public class WorkerPlayer implements Runnable {

 private final RobotController myRC;

 public WorkerPlayer(RobotController rc) {
    myRC = rc;
 }
 
 public void randomRun(){
	   
	   try{
			   for (int i=0; i<10; i++){

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
			   }

	   }catch(Exception e) {
         System.out.println("caught exception:");
         e.printStackTrace();
     }
	   
 }
 
 
 public void blockUnload() {
     
	 boolean done = false; 
	 
	 Direction dir, dir2 = null;
	 MapLocation dest;
//	 FluxDeposit depo, myflux
	 int dist=0, minLen = 9999;

	 
	 FluxDeposit depos[]=myRC.senseNearbyFluxDeposits();
// 	 senseFluxDepositInfo(FluxDeposit o); 
	 
/*	 for(int i=0; i < depos.length; i++)
	 {
		 if (dist==0)
		 {
			 dest=myRC.senseFluxDepositInfo(depos[i]);
			 minLen=myRC.getLocation().distanceSquaredTo(archons[i]);
		 }else{
			 if (minLen > myRC.getLocation().distanceSquaredTo(archons[i]))
			 {
				 minLen = myRC.getLocation().distanceSquaredTo(archons[i]);
				 myArchon=archons[i];
			 }
		 }
	 }
	 
	 if (myRC.getNumBlocks()== 0 )
	 {
		 done=true;
	 }
	 
	 
	 while(!done){
         try{*/
            /*** beginning of main loop ***/
        	  


  /*         	 dir=myRC.getDirection();
           	 dir2=myRC.senseDirectionToOwnedFluxDeposit();
        	 

        	 
            while ((myRC.isMovementActive())&&(myRC.getDirection() == myRC.senseDirectionToOwnedFluxDeposit())) 
            {
            	myRC.yield();
            }
            

                        
       	 
            if ((myRC.isMovementActive() == false)&&(dir == dir2))
       	 	{
            	 if(myRC.canMove(dir)) 
            	 {
            		 myRC.moveForward();
            	 } else 
            	 {
            		if ((myRC.getLocation().distanceSquaredTo(myArchon) == 1)&&(myRC.canUnloadBlockToLocation(myArchon))){
            			myRC.unloadBlockToLocation(myArchon);
            			done=true;
            		}
            		myRC.yield();
            	 }
       	 	}
            

           	if ((myRC.isMovementActive())&&(dir != dir2))
           	{
            	myRC.clearAction();
            }  	
            				
            if ((myRC.isMovementActive() == false)&&(dir != dir2))
            {
                  	myRC.setDirection(myRC.getLocation().directionTo(myArchon));
           	}

            
            myRC.yield();*/

            /*** end of main loop ***/
      /*   }catch(Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
         }
      }*/
	 
 }
 
 
 public void blockLoad() {
	 
	 MapLocation dest = null;
	 MapLocation blocks[] = null;
	 Direction dir, dir2 = null;
	 boolean haveBlock = false, check = false, done = false;
	 int dist=0, minLen = 9999;
	 
    while(!done){
       try{
          /*** beginning of main loop ***/

    	   
    	   if (!check) 
    	   {
    	    blocks = myRC.senseNearbyBlocks();
    	    check = true;
    	   }
    	    
    	   if ( blocks.length > 0 )
    	   {
    			 for(int i=0; i < blocks.length; i++)
    			 {
    				 if (dist==0)
    				 {
    					 dest=blocks[i];
    					 minLen = myRC.getLocation().distanceSquaredTo(blocks[i]);
    				 }else{
    					 if (minLen > myRC.getLocation().distanceSquaredTo(blocks[i]))
    					 {
    						 minLen = myRC.getLocation().distanceSquaredTo(blocks[i]);
    						 dest=blocks[i];
    					 }
    				 }
    			 }
      	 
      	 
    			 while ((myRC.getLocation().distanceSquaredTo(dest) > 1)&&(myRC.isMovementActive())&&(myRC.getDirection() == myRC.getLocation().directionTo(dest))) 
    			 {
    				 	myRC.yield();
    			 }
          
   	   
    			 dir=myRC.getDirection();
    			 dir2=myRC.getLocation().directionTo(dest);
    			 dist=myRC.getLocation().distanceSquaredTo(dest);
     	 
    			 if ((dist > 1)&&(myRC.isMovementActive() == false)&&(dir == dir2))
    			 {
    				 	if(myRC.canMove(dir)) 
    				 	{
    				 		myRC.moveForward();
    				 	}
    			 }
          

    			 if ((dist > 1)&&(myRC.isMovementActive())&&(dir != dir2))
    			 {
    				 myRC.clearAction();
    			 }  	
          				
    			 if ((dist > 1)&&(myRC.isMovementActive() == false)&&(dir != dir2))
    			 {
    				 myRC.setDirection(myRC.getLocation().directionTo(dest));
    			 }

    			 if (dist <= 1)
    			 {
    				 if (myRC.canLoadBlockFromLocation(dest))
    				 {
    					 myRC.loadBlockFromLocation(dest);
    					 haveBlock=true;
    					 done=true;
    				 }else{
    					 check = false;
    				 }
         		
    				 myRC.yield();
    			 }else{ 
    	    		   randomRun();
    			 }
         	          
    	   }else{ 
    		   randomRun();
    	   }
    			 
          myRC.yield();

          /*** end of main loop ***/
       }catch(Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
       }
    }
 }

 

 public void run() {
	 MapLocation myArchon = null;
	 MapLocation archons[] = myRC.senseAlliedArchons();
	 int dist =0, minLen=9999;
	 
	 

	 
    while(true){
       try{
          /*** beginning of main loop ***/

    	   randomRun();
    	  // blockLoad();
    	  // blockUnload();
          
    	   myRC.yield();

          /*** end of main loop ***/
       }catch(Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
       }
    }
 }
}