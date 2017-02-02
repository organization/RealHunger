package zingding;

import cn.nukkit.Player;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerEatFoodEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import me.onebone.economyapi.EconomyAPI;

public class RealHunger extends PluginBase implements Listener {
	public String[] T = new String[] { "§f", "§b", "§c" };
	public Thread thread;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<Player, Double> hunger = new HashMap();

	public void registerCommand(String name, String description, String usage, String permission, String[] aliases) {
		SimpleCommandMap map = this.getServer().getCommandMap();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PluginCommand cmd = new PluginCommand(name, this);
		cmd.setDescription(description);
		cmd.setUsage(usage);
		cmd.setPermission(permission);
		cmd.setAliases(aliases);
		map.register(name, cmd);
	}

	public static boolean isNumber(String str) {
		boolean result = false;

		try {
			Double.parseDouble(str);
			result = true;
		} catch (Exception arg2) {
			;
		}

		return result;
	}

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		@SuppressWarnings("rawtypes")
		Iterator iter = this.getServer().getOnlinePlayers().values().iterator();

		while (iter.hasNext()) {
			Player p = (Player) iter.next();
			this.hunger.put(p, Double.valueOf((double) (p.getFoodData().getLevel() * 100)));
		}

		this.thread = new Thread(new Runnable() {
			public void run() {
				label73: while (true) {
					try {
						if (!Thread.currentThread().isInterrupted()) {
							Thread.sleep(1000L);
							@SuppressWarnings("rawtypes")
							Iterator arg1 = RealHunger.this.hunger.entrySet().iterator();

							while (true) {
								while (true) {
									if (!arg1.hasNext()) {
										continue label73;
									}

									@SuppressWarnings("rawtypes")
									Entry entry = (Entry) arg1.next();
									Player player = (Player) entry.getKey();
									if (!player.isOnline()) {
										RealHunger.this.hunger.remove(player);
									} else {
										Double Kcal = (Double) entry.getValue();
										String type = "";
										if (!player.isAlive()) {
											type = RealHunger.this.T[2] + "사망";
											Kcal = Double.valueOf(2000.0D);
											RealHunger.this.hunger.put(player, Kcal);
										} else if (player.isCreative()) {
											type = "크리에이티브";
											Kcal = Double.valueOf(2000.0D);
											RealHunger.this.hunger.put(player, Kcal);
										} else if (!player.isInsideOfWater() && player.getLevel()
												.getBlock(new Vector3(player.x, player.y - 1.0D, player.z))
												.getId() != 8) {
											if (player.isSprinting()) {
												type = "달리는 중";
												Kcal = Double.valueOf(Kcal.doubleValue() - 7.04D);
												if (Kcal.doubleValue() < 0.0D) {
													Kcal = Double.valueOf(0.0D);
												}

												RealHunger.this.hunger.put(player, Kcal);
											} else if (player.motionChanged) {
												type = "이동 중";
												Kcal = Double.valueOf(Kcal.doubleValue() - 4.56D);
												if (Kcal.doubleValue() < 0.0D) {
													Kcal = Double.valueOf(0.0D);
												}

												RealHunger.this.hunger.put(player, Kcal);
											} else {
												type = "숨쉬는 중";
												Kcal = Double.valueOf(Kcal.doubleValue() - 1.67D);
												if (Kcal.doubleValue() < 0.0D) {
													Kcal = Double.valueOf(0.0D);
												}

												RealHunger.this.hunger.put(player, Kcal);
											}
										} else {
											type = "수영 중";
											Kcal = Double.valueOf(Kcal.doubleValue() - 10.62D);
											if (Kcal.doubleValue() < 0.0D) {
												Kcal = Double.valueOf(0.0D);
											}

											RealHunger.this.hunger.put(player, Kcal);
										}

										if (Kcal.doubleValue() == 0.0D) {
											player.addEffect(Effect.getEffect(2).setAmplifier(0).setDuration(100));
											player.addEffect(Effect.getEffect(4).setAmplifier(0).setDuration(100));
											player.addEffect(Effect.getEffect(18).setAmplifier(0).setDuration(100));
											if (Math.random() < 0.1D) {
												player.addEffect(Effect.getEffect(9).setAmplifier(0).setDuration(300));
											}

											Integer health = (int) player.getHealth();
											player.setHealth((float) (health.intValue() - 1));
										}

										player.sendPopup(RealHunger.this.T[0] + "이름 : " + RealHunger.this.T[1]
												+ player.getName() + RealHunger.this.T[0] + " 돈 : "
												+ RealHunger.this.T[1] + EconomyAPI.getInstance().myMoney(player)
												+ RealHunger.this.T[0] + EconomyAPI.getInstance().getMonetaryUnit()
												+ "\n" + RealHunger.this.T[0] + "X : " + RealHunger.this.T[1]
												+ Double.toString(Math.floor(player.getX() * 100.0) / 100.0)
												+ RealHunger.this.T[0] + " Y : " + RealHunger.this.T[1]
												+ Double.toString(Math.floor(player.getY() * 100.0) / 100.0)
												+ RealHunger.this.T[0] + " Z : " + RealHunger.this.T[1]
												+ Double.toString(Math.floor(player.getZ() * 100.0) / 100.0) + "\n"
												+ RealHunger.this.T[0] + "허기 : " + RealHunger.this.T[1]
												+ Double.toString(Math.floor(Kcal.doubleValue() * 100.0) / 100.0)
												+ RealHunger.this.T[0] + "Kcal" + " 상태 : " + RealHunger.this.T[1]
												+ type);
										player.getFoodData().setLevel((int) Math.floor(Kcal.doubleValue() / 100.0));
									}
								}
							}
						}
					} catch (InterruptedException arg6) {
						;
					}

					return;
				}
			}
		});
		this.thread.start();
	}

	public void onDisable() {
		this.thread.interrupt();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		this.hunger.put(player, Double.valueOf((double) (player.getFoodData().getLevel() * 100)));
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerEatFood(PlayerEatFoodEvent event) {
		Player player = event.getPlayer();
		Double become = Double.valueOf(
				((Double) this.hunger.get(player)).doubleValue() + (double) (event.getFood().getRestoreFood() * 100));
		if (become.doubleValue() > 2000.0D) {
			become = Double.valueOf(2000.0D);
		}

		this.hunger.put(player, become);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Double become = Double.valueOf(((Double) this.hunger.get(player)).doubleValue() - 10.0D);
		if (become.doubleValue() < 0.0D) {
			become = Double.valueOf(0.0D);
			event.setCancelled(true);
		}

		this.hunger.put(player, become);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Double become;
		if (event.getAction() == 1) {
			become = Double.valueOf(((Double) this.hunger.get(player)).doubleValue() - 10.0D);
			if (become.doubleValue() < 0.0D) {
				become = Double.valueOf(0.0D);
			}

			this.hunger.put(player, become);
		} else if (event.getAction() == 3 && player.getInventory().getItemInHand().getId() == 261) {
			become = Double.valueOf(((Double) this.hunger.get(player)).doubleValue() - 15.0D);
			if (become.doubleValue() < 0.0D) {
				become = Double.valueOf(0.0D);
			}

			this.hunger.put(player, become);
		}

	}
}
