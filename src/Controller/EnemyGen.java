package Controller;

import java.util.concurrent.CopyOnWriteArrayList;

import Constant.Audio;
import Constant.Images;
import Enemy.*;
import Sprite.BlackPanther;
import javafx.scene.canvas.GraphicsContext;

public class EnemyGen {

	public static boolean bowSpawn = true;
	public static double botSpawnRate = 1;
	public static final int ENEMYSTARTNUMBER = 1;
	public static int enemyMaximumNumber = 15;
	private static CopyOnWriteArrayList<HumanSprite> badList = new CopyOnWriteArrayList<HumanSprite>();
	public static BlackPanther instanceTiger;

	public static HumanSprite generateRandom() {
		double px = EnemyGen.randomPosition("X");
		double py = EnemyGen.randomPosition("Y");

		HumanSprite enemy = EnemyGen.randomType();
		enemy.setPosition(px, py);
		enemy.setBotType();
		EnemyGen.setInitialImage(enemy);
		return enemy;
	}

	private static void setInitialImage(HumanSprite enemy) {

		if (enemy instanceof GunMan || enemy instanceof TrapMan) {
			if (enemy.getPositionX() == 1300) {
				enemy.setImage((enemy.getImageL())[0]);
				enemy.setFace("LEFT");
			} else {
				enemy.setImage((enemy.getImageR())[0]);
				enemy.setFace("RIGHT");
			}
		}
	}

	public static void addBot() {
		EnemyGen.getbadList().add(EnemyGen.generateRandom());
	}

	public static void generatelistBot(int num) {
		CopyOnWriteArrayList<HumanSprite> bad = new CopyOnWriteArrayList<HumanSprite>();
		while (num != 0) {
			bad.add(generateRandom());
			num--;
		}
		EnemyGen.setbadList(bad);
	}

	public static void checkAttackHuman(BlackPanther blackPanther) {
		for (int i = 0; i < EnemyGen.badList.size(); i++) {
			HumanSprite enemy = EnemyGen.getbadList().get(i);
			if (enemy.intersect(blackPanther)) {

				if (!(enemy.isWaitToHit())) {
					enemy.setFace(enemy.getFace());
					enemy.nextPosition(enemy.getFace());
					enemy.attack(blackPanther);
					Thread a = new Thread(() -> {
						try {
							enemy.setWaitToHit(true);
							Thread.sleep(1000);
							Thread.sleep(1000);
							enemy.setWaitToHit(false);
						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					});
					a.start();
				}
			}

		}

	}

	public static void removeEnemy() {
		if (EnemyGen.getbadList().size() == 0)
			return;
		for (int i = 0; i < EnemyGen.getbadList().size(); i++) {
			HumanSprite enemy = EnemyGen.getbadList().get(i);
			if (enemy.isDead() && !(enemy.isTomb())) {
				enemy.setTomb(true);
				Audio.ENEMY_DEAD.play();
				enemy.setImage(Images.ENEMYTOMB);
				Controller.ScoreBoard.addScore(enemy);
				Thread t = new Thread(() -> {
					try {
						Thread.sleep(1000);
						EnemyGen.getbadList().remove(enemy);
						ItemGen.genItem(enemy.getPositionX(), enemy.getPositionY());
					} catch (InterruptedException e) {
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

	public static double randomPosition(String axis) {
		if (axis.equals("X")) {
			if (Math.random() < 0.5) {
				return 1300;
			} else {
				return -250;
			}
		}
		return Math.random() * 600 + 215;
	}

	public static HumanSprite randomType() {
		double random = Math.random();
		if (random > HumanSprite.BOT_GREEDY_RATE) {
			return new GunMan();
		} else if (random > HumanSprite.BOT_GREEDY_RATE - 0.07) {
			return new TrapMan();
		} else if (random > 0.40) {
			return new SwordMan();
		} else {
			return new BadHuman();
		}
	}

	public static void modifiyBotLevel() {
		if (Timer.getSec() == 20) {
			EnemyGen.botSpawnRate = 1.10;
			HumanSprite.BOT_GREEDY_RATE = 0.95;
			HumanSprite.BOT_FOLLOWING_RATE = 0.4;
			HumanSprite.BOT_STEADY_RATE = 0.15;
		} else if (Timer.getSec() == 50) {
			EnemyGen.botSpawnRate = 1.2;
			HumanSprite.BOT_GREEDY_RATE = 0.87;
			HumanSprite.BOT_FOLLOWING_RATE = 0.3;
			HumanSprite.BOT_STEADY_RATE = 0.10;
		} else if (Timer.getMin() == 1 && Timer.getSec() == 20) {
			enemyMaximumNumber += 5;
			EnemyGen.botSpawnRate = 1.25;
			HumanSprite.BOT_GREEDY_RATE = 0.80;
			HumanSprite.BOT_FOLLOWING_RATE = 0.2;
			HumanSprite.BOT_STEADY_RATE = 0.05;
		} else if (Timer.getMin() == 1 && Timer.getSec() == 40) {
			enemyMaximumNumber += 5;
			EnemyGen.botSpawnRate = 1.35;
			HumanSprite.BOT_GREEDY_RATE = 0.75;
			HumanSprite.BOT_FOLLOWING_RATE = 0.2;
			HumanSprite.BOT_STEADY_RATE = 0.05;
		} else if (Timer.getMin() == 2) {
			EnemyGen.botSpawnRate = (long) 1.5;
			HumanSprite.BOT_GREEDY_RATE = 0.65;
			HumanSprite.BOT_FOLLOWING_RATE = 0.1;
			HumanSprite.BOT_STEADY_RATE = 0;
		} else if (Timer.getMin() == 2 && Timer.getSec() == 30) {
			enemyMaximumNumber += 5;
			EnemyGen.botSpawnRate = (long) 1.75;
			HumanSprite.BOT_GREEDY_RATE = 0.55;
			HumanSprite.BOT_FOLLOWING_RATE = 0.1;
			HumanSprite.BOT_STEADY_RATE = 0;
		} else if (Timer.getMin() == 3) {
			enemyMaximumNumber += 5;
			EnemyGen.botSpawnRate = 2;
			HumanSprite.BOT_GREEDY_RATE = 0.10;
			HumanSprite.BOT_FOLLOWING_RATE = 0.0;
			HumanSprite.BOT_STEADY_RATE = 0;
		}
	}

	public static void checkQuantityBot() {
		if (EnemyGen.bowSpawn) {
			if (EnemyGen.getbadList().size() < EnemyGen.enemyMaximumNumber) {
				EnemyGen.addBot();
			}
			Thread addBot = new Thread(() -> {
				try {
					EnemyGen.bowSpawn = false;
					Thread.sleep((long) (1000 / EnemyGen.botSpawnRate));
					EnemyGen.bowSpawn = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			addBot.start();
		} else if (EnemyGen.getbadList().size() <= 5) {
			for (int i = 0; i <= 5; i++) {
				EnemyGen.addBot();
				EnemyGen.bowSpawn = true;
			}
		}
	}

	public static void renderBotList(GraphicsContext gc) {
		for (int i = 0; i < EnemyGen.getbadList().size(); i++) {
			((EnemyGen.getbadList()).get(i)).render(gc);
		}
	}
}
