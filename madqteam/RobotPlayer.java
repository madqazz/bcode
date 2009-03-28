package madqteam;

import battlecode.common.*;

public class RobotPlayer implements Runnable {

private AbstractRobot player;
     
public RobotPlayer(RobotController rc) {
     switch (rc.getRobotType()) {
     	  case ARCHON:
     		  player = new ArchonPlayer(rc);
     		  break;
     	  case WORKER:
     		  player = new WorkerPlayer(rc);   		  
     		  break;
     	  case SOLDIER:
     		  player = new SoldierPlayer(rc);
     		  break;
     		  
         }
      }
      
      public void run() {
         while(true){
            try{
               /*** beginning of main loop ***/
           	player.run(); 
               /*** end of main loop ***/
            }catch(Exception e) {
               System.out.println("caught exception:");
               e.printStackTrace();
            }
         }
      }
   }

