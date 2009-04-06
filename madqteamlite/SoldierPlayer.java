package madqteamlite;


import battlecode.common.*;
import madqteamlite.AbstractRobot;
import madqteamlite.AbstractRobot.RobotState;


public class SoldierPlayer extends AbstractRobot {

 private final RobotController myRC;

 public SoldierPlayer(RobotController rc) {
	 super(rc);
     myRC = rc;
     state = RobotState.SOLDIER_FIND_FLUX;
 }
 
 protected void follow() throws GameActionException{
	 
	  MapLocation archon = followArchon();
	  Message msg=myRC.getNextMessage();
	  
	  if (!(msg==null) && !(msg.strings==null) && msg.strings.length == 2){
		  if (msg.strings[1] == "MQ_ATTACK"){
			  state = RobotState.SOLDIER_ATTACK;
			  return;
		  }
		  if (msg.strings[1] == "MQ_DEFENSE"){
			  goNear(archon);
			  state = RobotState.SOLDIER_DEFENSE;
			  return;
		  }
		  if (msg.strings[1] == "MQ_NEED_HELP"){
			  goNear(msg.locations[0]);
			  state = RobotState.SOLDIER_ATTACK;
			  return;
		  }
		  if (!ourMessage(msg)){
			  sendMessage(3);
			  state = RobotState.SOLDIER_ATTACK;
			  return;
		  }
	  }
	  
	  if (!(archon == null)){
		  goDirection(myRC.getLocation().directionTo(archon));
	  }
	  
	  transferEnergon(); 
}
 
 
	protected void attack(RobotInfo robot) throws GameActionException {
        if (myRC.isAttackActive())
            return;
        if (robot == null)
        	return;
		if (robot.type == RobotType.ARCHON || robot.type == RobotType.SCOUT){
			if (myRC.canAttackAir() && myRC.canAttackSquare(robot.location)){
				myRC.attackAir(robot.location);
				return;
			}
		} else {
			if (myRC.canAttackGround() && myRC.canAttackSquare(robot.location)){
				myRC.attackGround(robot.location);
				return;
			}
		}
		return;
	}
 
  protected void defense() throws GameActionException{
	  Message msg=myRC.getNextMessage();
	  MapLocation nearestEnemyLoc = null;
	  if (!(msg==null) && !(msg.strings==null) && msg.strings.length == 2){
		  if (msg.strings[1] == "MQ_ATTACK"){
			  state = RobotState.SOLDIER_ATTACK;
			  return;
		  }
		  if (msg.strings[1] == "MQ_DEFENSE"){
  			  nearestEnemyLoc = msg.locations[0];
		  }
		  if (msg.strings[1] == "MQ_FOLLOW"){
			  state = RobotState.SOLDIER_FOLLOW;
			  return;
		  }
	  }
	  
	  myRC.yield();	  
	  RobotInfo nearestEnemy = enemyInfo();
	  
	  if (!(nearestEnemy == null)){
		  if (!myRC.canAttackSquare(nearestEnemy.location)
			  && !(myRC.getLocation().directionTo(nearestEnemy.location).equals(Direction.OMNI))){
		  		waitUntilMovementIdle();
		  		myRC.setDirection(myRC.getLocation().directionTo(nearestEnemy.location));
		  		myRC.yield();
		  }else{
			  attack(nearestEnemy);
		  }
	  }else{
		  if (!(nearestEnemyLoc == null)){
			  if (!myRC.canAttackSquare(nearestEnemyLoc)
				  && !(myRC.getLocation().directionTo(nearestEnemyLoc).equals(Direction.OMNI))){
				  	waitUntilMovementIdle();
				  	myRC.setDirection(myRC.getLocation().directionTo(nearestEnemyLoc));
				  	myRC.yield();
			  }else{
				  attack(nearestEnemy);
			  }
		  }
	  }
	  transferEnergon(); 	  
  }
  
  
  protected void combatMode() throws GameActionException{

	  MapLocation nearestEnemyLoc = null;
	  
	  waitUntilAttackIdle();
	  myRC.yield();
	  RobotInfo nearestEnemy = enemyInfo();
	  if (!(nearestEnemy == null)){
		  if (myRC.getLocation().isAdjacentTo(nearestEnemy.location)){
			  if (!(nearestEnemy == null)){
				  if (!myRC.canAttackSquare(nearestEnemy.location)
					  && !(myRC.getLocation().directionTo(nearestEnemy.location).equals(Direction.OMNI))){
				  		waitUntilMovementIdle();
				  		myRC.setDirection(myRC.getLocation().directionTo(nearestEnemy.location));
				  		myRC.yield();
				  }else{
					  attack(nearestEnemy);
					  sendMessage(3);
				  }
			  }
		  }else{
			  if (!(nearestEnemy.location.equals(Direction.OMNI))){
				  goDirection(myRC.getLocation().directionTo(nearestEnemy.location));
			  }
		  }	  	  
	  }else{
	  
	  	  Message msg=myRC.getNextMessage();
	  
	  	  if (!(msg==null) && !(msg.strings==null) && msg.strings.length == 2){
	  		  if (msg.strings[1] == "MQ_DEFENSE"){
	  			  state = RobotState.SOLDIER_DEFENSE;
	  			  return;
	  		  }
	  		  if (msg.strings[1] == "MQ_FOLLOW"){
	  			  state = RobotState.SOLDIER_FOLLOW;
	  			  return;
	  		  }
	  		  if (msg.strings[1] == "MQ_ATTACK"){
	  			  nearestEnemyLoc = msg.locations[0];
	  		  }
	  	  }

	  	  if (!(nearestEnemyLoc == null)){
	  		  if (myRC.getLocation().isAdjacentTo(nearestEnemyLoc)){
	  			  if (!myRC.canAttackSquare(nearestEnemyLoc) 
	  				  && !(myRC.getLocation().directionTo(nearestEnemyLoc).equals(Direction.OMNI))){
	  				  		waitUntilMovementIdle();
	  				  		myRC.setDirection(myRC.getLocation().directionTo(nearestEnemyLoc));
	  				  		myRC.yield();
	  			  }else{
	  				  nearestEnemy = enemyInfo();
	  				  if (!(nearestEnemy == null)){
	  					  attack(nearestEnemy);
	  					  sendMessage(3);
	  				 }
	  			  }
	  			  
	  		  }else{
	  			  goNear(nearestEnemyLoc);
	  		  }
	  	  }else{
	  		  randomRun(2);
	  	  }
	  }
	  transferEnergon();  
  }
  
  
  protected void goToFlux() throws GameActionException{   	
	  
	  	 //myRC.yield();
      	
         if (myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE))
         {
          	state = RobotState.SOLDIER_FOLLOW;
            return;
         }
	       
	     if (checkEnemy()){
	          	state = RobotState.SOLDIER_ATTACK;
	            return;
		 }
	     	
		 Message msg=myRC.getNextMessage();
			  
		 if (!(msg==null) && !(msg.strings==null) && msg.strings.length == 2){
		    if (msg.strings[1] == "MQ_ATTACK"){
		    	state = RobotState.SOLDIER_ATTACK;
				return;
			  }
			if (!ourMessage(msg)){
			    sendMessage(3);
				state = RobotState.SOLDIER_ATTACK;
				return;
			  }

		  }
      	 myRC.yield();
		 transferEnergon();  

  //       if (myRC.isMovementActive()){
  //        	myRC.yield();
  //       }
         
         waitUntilMovementIdle();
         
         if (myRC.canMove(myRC.getDirection())){
          	 if(myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit())){          		 
          		 myRC.moveForward();
          		 myRC.yield();
          	 } else {
          		 if(!myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.OMNI)
          			&& !myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE)){
          			 myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
          			 myRC.yield();
          		 }
          	 }
         } else {
        	 if(myRC.getDirection().equals(myRC.senseDirectionToUnownedFluxDeposit())){
        		 randomRun(10);
        	 } else {
          		 if(!myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.OMNI)
               			&& !myRC.senseDirectionToUnownedFluxDeposit().equals(Direction.NONE)){
               			 myRC.setDirection(myRC.senseDirectionToUnownedFluxDeposit());
               			 myRC.yield();
               	} else {
               		randomRun(10);
               	}
        	 }
         }
    }
  
  
  
  
 
   public void run() {
	   updateStatus();
	   switch (state) {
  		case SOLDIER_DEFENSE:
			   	try {
			   		defense();
			   	} catch (GameActionException e) {
			   		e.printStackTrace();
			   	}
			   	break;
  		case SOLDIER_ATTACK:
  				try {
  					combatMode();
  				} catch (GameActionException e) {
  					e.printStackTrace();
  				}
  				break; 
  		case SOLDIER_FIND_FLUX:
  				try {
  					goToFlux();
  				} catch (GameActionException e) {
  					e.printStackTrace();
  				}
  				break;
  		case SOLDIER_FOLLOW:
				try {
					follow();
				} catch (GameActionException e) {
					e.printStackTrace();
				}
				break;

	   }
  }
}