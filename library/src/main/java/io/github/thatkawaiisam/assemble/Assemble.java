package io.github.thatkawaiisam.assemble;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Assemble {

	//Instance
	@Getter private static Assemble instance;

	private JavaPlugin plugin;
	private AssembleAdapter adapter;
	private Map<UUID, AssembleBoard> boards;
	private AssembleThread thread;

	//Scoreboard Ticks
	@Setter private long ticks = 2;

	//Default Scoreboard Style
	@Setter private AssembleStyle assembleStyle = AssembleStyle.MODERN;

	public Assemble(JavaPlugin plugin, AssembleAdapter adapter) {
		if (instance != null) {
			throw new RuntimeException("Assemble has already been instantiated!");
		}

		if (plugin == null) {
			throw new RuntimeException("Assemble can not be instantiated without a plugin instance!");
		}

		instance = this;

		this.plugin = plugin;
		this.adapter = adapter;
		this.boards = new ConcurrentHashMap<>();

		this.setup();
	}

	private void setup() {
		//Register Events
		this.plugin.getServer().getPluginManager().registerEvents(new AssembleListener(), this.plugin);

		//Ensure that the thread has stopped running
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		//Start Thread
		this.thread = new AssembleThread(this);
	}

}
