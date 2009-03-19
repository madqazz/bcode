package madqteam;

import battlecode.common.*;
import java.util.Random;

//import static battlecode.common.GameConstants.*;

public class ArchonPlayer implements Runnable {

   private final RobotController myRC;
   private MapLocation enemyFlux;
   private Random generator;

   public ArchonPlayer(RobotController rc) {
      myRC = rc;
   }


    public void randomRun() throws GameActionException{

        MapLocation myFlux=null;
        Message msg=null;

       for(int i=0;i<35;i++){
	   try{

                msg = myRC.getNextMessage();
                if(!(msg==null)&&(msg.strings[0].equals("blablabla"))){
                     myRC.yield();
                     waitUntilMovementIdle();
                     myRC.setDirection(myRC.getDirection().opposite());
                     myRC.yield();
                     waitUntilMovementIdle();
                }

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
                     myRC.yield();

            }catch(Exception e) {
           System.out.println("caught exception:");
           e.printStackTrace();
       }
     /*  if (!(myFlux==null)){
        goTo(myFlux);
      }*/
    }
   }

  
   public MapLocation senseFlux() throws GameActionException{

       MapLocation myFlux = null ,deposit = null;
       int minLen=9999;
       FluxDeposit[] depos=myRC.senseNearbyFluxDeposits();

		   for (FluxDeposit depo: depos){
   	   			deposit=myRC.senseFluxDepositInfo(depo).location;
                    if (minLen < myRC.getLocation().distanceSquaredTo(deposit)){
                      if (myRC.senseAirRobotAtLocation(deposit) == null) {
                        minLen = myRC.getLocation().distanceSquaredTo(deposit);
                        myFlux=deposit;
                    }
               }
           }
      return myFlux;
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
  
   public void goTo(MapLocation dest){

      boolean onDest = false;

      while(true){
         try{

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
            	 }
       	 	}

             if (!(onDest)&&(!myRC.canMove(myRC.getDirection())))
             {
                 if(myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit()))
            	 {
            		 randomRun();
            	 } else
            	 {
            		 myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
            	 }
             }


            myRC.yield();


         }catch(Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
         }
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


    private void findFLux(){

	   boolean onflux = false;
	   Robot NearbyRobots[];
	   int workers =0;


      while(true){
         try{

            if (myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.OMNI))
            {
            	onflux = true;
                myRC.yield();
            }

            while (!(onflux)&&(myRC.isMovementActive()))
            {
            	myRC.yield();
            }

            if (onflux)
            {
                if (workers < 5){
                    if (workers ==4){
                        myRC.yield();
                        myRC.setDirection(myRC.getDirection().opposite());
                        myRC.yield();
                        spawnBob();
                        workers++;
                        sendMessage(0);
                    }else{
                        spawn();
                        workers++;
                        sendMessage(0);
                    }
                }else{
                    NearbyRobots = myRC.senseNearbyGroundRobots();
                    if(NearbyRobots.length <4){
                        spawn();
                        sendMessage(0);
                    }
                }
                transferEnergon();
                myRC.yield();
            }

            if (!(onflux)&&(myRC.canMove(myRC.getDirection())))
       	 	{
            	 if(myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit()))
            	 {
            		 myRC.moveForward();
            	 } else
            	 {
            		 myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
            	 }
       	 	}

             if (!(onflux)&&(!myRC.canMove(myRC.getDirection())))
             {
                 if(myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit()))
            	 {

                     	FluxDeposit[] fluxs = myRC.senseNearbyFluxDeposits();
                        enemyFlux=null;

                    	//for (FluxDeposit deposit : fluxs) {

                          //  MapLocation loc = myRC.senseFluxDepositInfo(deposit).location;

                          //  if(myRC.getLocation().isAdjacentTo(loc)){

                                for (Robot robot : myRC.senseNearbyGroundRobots()) {
                                     RobotInfo robotInfo = myRC.senseRobotInfo(robot);
                                     if (!robotInfo.team.equals(myRC.getTeam())&&(robotInfo.location.isAdjacentTo(myRC.getLocation())))  {

                                		sabotage();

                                     }

                            //   }
                           // }
                        }
            		 randomRun();
            	 } else
            	 {
            		 myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
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

     private void waitUntilMovementIdle(){
          while(myRC.getRoundsUntilMovementIdle() > 0){
                     myRC.yield();
        }
 }

    private void sendMessage(int i) throws GameActionException{
        Message msgout;
    	msgout = new Message();
        String[] msg = new String[5];
        switch(i){
            case 0:
                msg[0]="0";
                break;
            case 1:
                msg[0]="1";
                break;
            case 2:
                msg[0]="2";
                break;
            case 3:
                msg[0]="3";
                break;
            case 10:
                msg[0]="blablabla";
                break;

        }
	       		 msgout.strings = msg;
	           	 myRC.broadcast(msgout);
	           	 myRC.yield();

    }


	private void spawn() throws GameActionException {

		for (Direction dir : Direction.values()) {
			if (!dir.equals(Direction.OMNI) && !dir.equals(Direction.NONE)) {
				MapLocation loc = myRC.getLocation().add(dir);

                if (myRC.senseGroundRobotAtLocation(loc) == null && myRC.getEnergonLevel() > RobotType.WORKER.spawnCost()) {
                    if(!myRC.getDirection().equals(dir)){
                        waitUntilMovementIdle();
                        myRC.setDirection(dir);
                        myRC.yield();
                    }
                    waitUntilMovementIdle();
                    myRC.spawn(RobotType.WORKER);
                    myRC.yield();
                    break;
			}
		}
	}
}

    	private void spawnBob() throws GameActionException {

		while(true) {
                if (myRC.senseGroundRobotAtLocation(myRC.getLocation().add(myRC.getDirection())) == null && myRC.getEnergonLevel() > RobotType.WORKER.spawnCost()) {
                    waitUntilMovementIdle();
                    myRC.spawn(RobotType.WORKER);
                    myRC.yield();
                    break;
                }else{
                    myRC.setDirection(myRC.getDirection().rotateLeft());
                    myRC.yield();

                }
       }
}


    private void sabotage(){
     int workers =0;
     while(true){
         try{

             
                if (workers == 0){
                        spawn();
                        workers++;
                        myRC.yield();
                        sendMessage(3);
                }

                if (workers <3 ){
                        spawn();
                        workers++;
                        sendMessage(2);

                }else{
                    Robot[] NearbyRobots = myRC.senseNearbyGroundRobots();
                    if(NearbyRobots.length <5){
                        spawn();
                        sendMessage(2);
                    }
                }

                transferEnergon();
                if(generator.nextInt(100)==0){
                    sendMessage(10);
                }

         }catch(Exception e) {
           System.out.println("caught exception:");
           e.printStackTrace();
       }
     }
 }


   public void run() {

      generator = new Random();
	  findFLux();
      while(true){
        myRC.yield();
      }
      }

}
