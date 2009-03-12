package madqteam;

import battlecode.common.*;

//import static battlecode.common.GameConstants.*;

public class ArchonPlayer implements Runnable {

   private final RobotController myRC;

   public ArchonPlayer(RobotController rc) {
      myRC = rc;
   }
   
   public void randomRun(){
	   
	   try{
			   for (int i=0; i<GameConstants.ROUNDS_TO_CAPTURE; i++){

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
   
   public double min (double a, double b)
   {
	   if (a < b) 
	   {
		return a;   
	   } else {
		   return b;
	   }
	   
   } 

   public void run() {
	   
	   Direction dir, dir2;
	   boolean onflux = false;
	   Robot NearbyRobots[];
	   RobotInfo rInfo;
	   boolean worker = false;
	   double amount;


      while(true){
         try{
            /*** beginning of main loop ***/
        	  

        	 dir=myRC.getDirection();
        	 dir2=myRC.senseDirectionToUnownedFluxDeposit();
        	 
        	 
            while (!(onflux)&&(myRC.isMovementActive())&&(myRC.getDirection() == myRC.senseDirectionToUnownedFluxDeposit())) 
            {
            	myRC.yield();
            }
            
            if (onflux)
            {
               
               NearbyRobots = myRC.senseNearbyGroundRobots();
               
               if (!worker)
               {
            	   myRC.spawn(RobotType.WORKER);
            	   worker=true;
               }
               
               for (int i=0; i<NearbyRobots.length; i++)
               {            	                 
            	  rInfo = myRC.senseRobotInfo(NearbyRobots[i]);
            	  if (4*rInfo.energonLevel < 3*rInfo.maxEnergon)
            	  {
            		  amount=min(rInfo.maxEnergon-rInfo.energonLevel,myRC.getEnergonLevel()-1); 
                      myRC.transferEnergon(amount,rInfo.location,RobotLevel.ON_GROUND);
            	  }
               }
               


            }
                        
       	 
            if (!(onflux)&&(myRC.isMovementActive() == false)&&(dir == dir2))
       	 	{
            	 if(myRC.canMove(dir)) 
            	 {
            		 myRC.moveForward();
            	 } else 
            	 {
            		 randomRun();
            	 }
       	 	}
            

           	if (!(onflux)&&(myRC.isMovementActive())&&(dir != dir2))
           	{
            	myRC.clearAction();
            }  	
            				
            if (!(onflux)&&(myRC.isMovementActive() == false)&&(dir != dir2))
            {
                  	myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
           	}

            if (!(onflux)&&(dir2 == Direction.OMNI))
            {
            	onflux = true;
            }
            
            myRC.yield();

            /*** end of main loop ***/
         }catch(Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
         }
      }
   }
}