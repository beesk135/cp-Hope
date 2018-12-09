package Controller;

import java.util.concurrent.CopyOnWriteArrayList;

import Constant.Audio;
import Constant.Images;
import Enemy.*;
import Sprite.BlackPanther;

public class EnemyGen {
    private static CopyOnWriteArrayList<HumanSprite> badList = new CopyOnWriteArrayList<HumanSprite>();
    public static BlackPanther instanceTiger;
    // addBOt
    

	   public static HumanSprite generateRandom() {
	    	double px,py;
	    	if(Math.random() < 0.5) {
		        px = 1300;
	    	} else {
	    		px = -250;
	    	}
	        py = Math.random()*600+215;
	        
	        
		    double random = Math.random();
	    	HumanSprite enemy;
		    if(random > 0.8) {
		    	enemy = new GunMan();
		    } else if(random > 0.6) {
		    	enemy = new TrapMan();
		    } else if (random > 0.4) {
		    	enemy = new SwordMan();
		    } else {
		    	enemy = new BadHuman();
		    }
	        enemy.setPosition(px, py);
		    EnemyGen.setInitialImage(enemy);
	        return enemy;
	    }
	    
	    private static void setInitialImage(HumanSprite enemy) {
		// TODO Auto-generated method stub
	    	if(enemy instanceof GunMan) {
	    		if(enemy.getPositionX() == 1300) {
	    			enemy.setImage((Images.GUNMANL)[0]);
	    			enemy.setFace("LEFT");
	    		} else {
	    			enemy.setImage((Images.GUNMANR)[0]);
	    			enemy.setFace("RIGHT");
	    		}
	    	}
	    }

		public static void addBot() {
	    	EnemyGen.getbadList().add(EnemyGen.generateRandom());
	    }
	    
	    public static void generatelistBot(int num) {
	    	CopyOnWriteArrayList<HumanSprite> bad = new CopyOnWriteArrayList<HumanSprite>();
	    	while(num !=0) {
	    		bad.add(generateRandom());
	    		num--;
	    	}
	    	EnemyGen.setbadList(bad);
	    }
	    public static void checkAttackHuman(BlackPanther tiger) {
	    	for(int i=0; i< EnemyGen.badList.size();i++) {
	    		HumanSprite enemy = EnemyGen.getbadList().get(i);
	    		if(enemy.intersect(tiger)) {
	    			if(!(enemy.isWaitToHit())) {
	    				enemy.attack(tiger);
	    				Thread a = new Thread (()->{
	    					try {
	    						enemy.setWaitToHit(true);
	    						Thread.sleep(2000);
	    						enemy.setWaitToHit(false);
	    					} catch (InterruptedException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	    				});
	    				a.start();
	    			}
	    		}
	    		
	    	}
	    	
	    }
	    public static void removeEnemy() {
			if(EnemyGen.getbadList().size()==0) return ;
			for(int i =0;i<EnemyGen.getbadList().size();i++) {
				HumanSprite enemy = EnemyGen.getbadList().get(i);
				if(enemy.isDead() && !(enemy.isTomb())) {
					Audio.ENEMY_DEAD.play();
					Controller.ScoreBoard.addScore(1000);
					enemy.setTomb(true);
					Thread t = new Thread(()-> {
								try {
									enemy.setImage(Images.enemyTomb);  
									ItemGen.genItem(enemy.getPositionX(), enemy.getPositionY());
									Thread.sleep(1000);
									EnemyGen.getbadList().remove(enemy);
								}
								catch(InterruptedException e){
									e.printStackTrace();
								}
					});
					t.start();
				}
			}
		}
	
	    public static CopyOnWriteArrayList<HumanSprite> getbadList() {
			return EnemyGen.badList;
		}

		public static void setbadList(CopyOnWriteArrayList<HumanSprite> list) {
			EnemyGen.badList = list;
		}
}