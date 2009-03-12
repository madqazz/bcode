package madqteam;

import battlecode.common.*;
//import static battlecode.common.GameConstants.*;

public class RobotPlayer implements Runnable {

   private final RobotController myRC;

   public RobotPlayer(RobotController rc) {
      myRC = rc;
   }

   public void run() {
	   
	   RobotType robot = myRC.getRobotType();
	   
	   if (robot == RobotType.ARCHON) 
	   {
		  ArchonPlayer archon= new ArchonPlayer(myRC);
		  archon.run();
	   }

	   if (robot == RobotType.WORKER)
	   {
		   WorkerPlayer worker= new WorkerPlayer(myRC);
		   worker.run();
		   
	   }

    

   }
}
