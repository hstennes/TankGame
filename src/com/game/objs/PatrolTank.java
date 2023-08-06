package com.game.objs;

import java.util.Random;

import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Tank;

public class PatrolTank extends Tank{
	
	private final int moveSpeed = 3;
	private final int sightRange = 128;
	
	private float turnTicks;
	private boolean turning;
	
	Random r;
	Handler handler;
	Player player;
	
	public PatrolTank(float x, float y, ObjectID id, Handler handler) {
		super(x, y, id, handler);
		this.handler = handler;
		player = handler.getPlayer();
		forwardSpeed = moveSpeed;
		r = new Random();
	}

	public void tick(){
		if(frozen == 0) {
			tankAI();
		}
		super.tick();
	}
	
	private void tankAI(){	
		if(!turning) {	
			if(a.searchObstacles(sightRange)){
				turning = true;			
				if(r.nextBoolean()) rotationSpeed = 3;
				else rotationSpeed = -3;		
			}	
		}
		else{
			if(turnTicks == 29){
				rotationSpeed = 0;
				turning = false;
				turnTicks = 0;			
			}
			else turnTicks++;
		}	
	}
}
