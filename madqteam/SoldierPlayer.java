package madqteam;


import battlecode.common.*;
import madqteam.AbstractRobot;
//import madqteam.AbstractRobot.RobotState;
import madqteam.AbstractRobot.RobotState;


public class SoldierPlayer extends AbstractRobot {

 private final RobotController myRC;

 public SoldierPlayer(RobotController rc) {
	 super(rc);
     myRC = rc;
     state = RobotState.SOLDIER_FOLLOW;
 }
 
 
	protected void attack(RobotInfo robot) throws GameActionException {
        if (myRC.isAttackActive())
            return;
		if (robot.type == RobotType.ARCHON || robot.type == RobotType.SCOUT) {
			if (myRC.canAttackAir() && myRC.canAttackSquare(robot.location)) {
				myRC.attackAir(robot.location);
				return;
			}
		} else {
			if (myRC.canAttackGround() && myRC.canAttackSquare(robot.location)) {
				myRC.attackGround(robot.location);
				return;
			}
		}
		return;
	}
 

 protected MapLocation followArchon(){
	 MapLocation[] archons = myRC.senseAlliedArchons();
	 int minLen = 9999;
	 MapLocation myArchon = null;
	 for(MapLocation archon : archons){
		 if(minLen>myRC.getLocation().distanceSquaredTo(archon)){
			 minLen=myRC.getLocation().distanceSquaredTo(archon);
			 myArchon=archon;
		 }
	 }
	 return myArchon;
	 
 }
 
 protected void follow() throws GameActionException{
	  MapLocation archon = followArchon();
	  Message msg=myRC.getNextMessage();
	  if (!(msg==null)){
		  if (msg.strings[0] == "MQ_ATTACK"){
			  state = RobotState.SOLDIER_ATTACK;
			  return;
		  }
		  if (msg.strings[0] == "MQ_DEFENSE"){
			  goNear(archon);
			  state = RobotState.SOLDIER_DEFENSE;
			  return;
		  }
	/*	  if (msg.strings[0] == "MQ_PATROL"){
			  state = RobotState.SOLDIER_PATROL;
			  return;
		  }*/
	  }
  	  if (checkEnemy()){
		  goNear(archon);
		  state = RobotState.SOLDIER_DEFENSE;
		return;
  	  }
	
	  if (!(archon == null)){
		  randomRun(7);
		  goNear(archon);		  
	  }
	 
 }
 
 
  protected void defense() throws GameActionException{
	  waitUntilAttackIdle();
	  Message msg=myRC.getNextMessage();
	  MapLocation nearestEnemyLoc = null;
	  if (!(msg==null)){
		  if (msg.strings[0] == "MQ_ATTACK"){
			  state = RobotState.SOLDIER_ATTACK;
			  return;
		  }
	//	  if (msg.strings[0] == "WORK"){
	//	  	 myRC.suicide();
	//	  }
		  if (msg.strings[0] == "MQ_DEFENSE"){
  			  nearestEnemyLoc = msg.locations[0];
		  }
		  if (msg.strings[0] == "MQ_PATROL"){
			  state = RobotState.SOLDIER_PATROL;
			  return;
		  }
	  }
	  myRC.yield();	  
	  
	  
	  RobotInfo nearestEnemy = enemyInfo();
	  if (!(nearestEnemy == null)){
		  if (!(myRC.getDirection().equals(myRC.getLocation().directionTo(nearestEnemy.location))) && !(myRC.getLocation().directionTo(nearestEnemy.location).equals(Direction.OMNI))){
			  waitUntilMovementIdle();
			  myRC.setDirection(myRC.getLocation().directionTo(nearestEnemy.location));
			  myRC.yield();
		  }else{
			  attack(nearestEnemy);
		  }
	  }else{
		  if (!(nearestEnemyLoc == null)){
			  if (!(myRC.getDirection().equals(myRC.getLocation().directionTo(nearestEnemyLoc))) && !(myRC.getLocation().directionTo(nearestEnemyLoc).equals(Direction.OMNI))){
				  waitUntilMovementIdle();
				  myRC.setDirection(myRC.getLocation().directionTo(nearestEnemyLoc));
				  myRC.yield();
			  	}
		  }
	  }
	  
	  transferEnergon();  
  }
  
/*  
  protected void patrol() throws GameActionException{
	  waitUntilAttackIdle();
	  Message msg=myRC.getNextMessage();
	  MapLocation nearestEnemyLoc = null;
	  if (!(msg==null)){
		  if (msg.strings[0] == "MQ_ATTACK"){
			  state = RobotState.SOLDIER_ATTACK;
			  return;
		  }
		  if (msg.strings[0] == "MQ_DEFENSE"){
			  state = RobotState.SOLDIER_DEFENSE;
			  return;
		  }
		  if (msg.strings[0] == "MQ_FOLLOW"){
			  state = RobotState.SOLDIER_FOLLOW;
			  return;
		  }
		  if (!(archon == null)){
			  goDirection(myRC.getLocation().directionTo(archon));		  
		  }else{
			  randomRun(3);
		  }

	  
	  transferEnergon();  
  }
  */
  
  
  protected void fight() throws GameActionException{

	  MapLocation nearestEnemyLoc = null;
	  
	  waitUntilAttackIdle();
	  
	  RobotInfo nearestEnemy = enemyInfo();
	  if (!(nearestEnemy == null)){
		  if (myRC.getLocation().isAdjacentTo(nearestEnemy.location)){
			  if (!(myRC.getDirection().equals(myRC.getLocation().directionTo(nearestEnemy.location))) && !(myRC.getLocation().directionTo(nearestEnemy.location).equals(Direction.OMNI))){
				  waitUntilMovementIdle();
				  myRC.setDirection(myRC.getLocation().directionTo(nearestEnemy.location));
				  myRC.yield();
			  }else{
				  attack(nearestEnemy);
				  if (generator.nextInt(50)==1){
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
	  
	  	  if (!(msg==null)){
	  		  if (msg.strings[0] == "MQ_DEFENSE"){
	  			  state = RobotState.SOLDIER_DEFENSE;
	  			  return;
	  		  }
	  		  if (msg.strings[0] == "MQ_PATROL"){
	  			  state = RobotState.SOLDIER_PATROL;
	  			  return;
	  		  }
	  		  if (msg.strings[0] == "MQ_ATTACK"){
	  			  nearestEnemyLoc = msg.locations[0];
	  		  }
	  		  if (msg.strings[0] == "MQ_NEED_HELP"){
	  			  nearestEnemyLoc = msg.locations[0];
	  		  }
	  	  }
	  	  myRC.yield();
	  	  if (!(nearestEnemyLoc == null)){
	  		  if (myRC.getLocation().isAdjacentTo(nearestEnemyLoc)){
	  			  if (!(myRC.getDirection().equals(myRC.getLocation().directionTo(nearestEnemyLoc))) && !(myRC.getLocation().directionTo(nearestEnemyLoc).equals(Direction.OMNI))){
	  				  waitUntilMovementIdle();
	  				  myRC.setDirection(myRC.getLocation().directionTo(nearestEnemyLoc));
	  				  myRC.yield();
	  			  }else{
	  				  nearestEnemy = enemyInfo();
	  				  if (!(nearestEnemy == null)){
	  					  attack(nearestEnemy);
	  					  if (generator.nextInt(150)==1){
	  						  sendMessage(3);
	  					  }
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
 
   public void run() {
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
  					fight();
  				} catch (GameActionException e) {
  					e.printStackTrace();
  				}
  				myRC.yield();
  				break; 
  /*		case SOLDIER_PATROL:
				try {
					patrol();
				} catch (GameActionException e) {
					e.printStackTrace();
				}
				myRC.yield();
				break;*/
  		case SOLDIER_FOLLOW:
				try {
					follow();
				} catch (GameActionException e) {
					e.printStackTrace();
				}
				myRC.yield();
				break;
	   }
  }
}