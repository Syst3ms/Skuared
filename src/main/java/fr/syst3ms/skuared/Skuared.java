package fr.syst3ms.skuared;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.lang.function.FunctionEvent;
import ch.njol.skript.lang.function.Functions;
import ch.njol.skript.lang.function.JavaFunction;
import ch.njol.skript.lang.function.Parameter;
import ch.njol.skript.registrations.Classes;
import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.Associativity;
import fr.syst3ms.skuared.util.MathUtils;
import fr.syst3ms.skuared.util.evaluation.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class Skuared extends JavaPlugin {
	private static Skuared instance;
	private SkriptAddon addon;
	private FileConfiguration config;
	private String wolframId = null;

	public static Skuared getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		startupChecks();
		getLogger().log(Level.INFO, "Starting up Skuared...");
		setupSyntax();
		setupConfig();
		getLogger().log(Level.INFO, "Successfully started up Skuared !");
	}

	@Override
	public void onDisable() {
	}

	private void startupChecks() {
		if (instance == null) {
			instance = this;
		} else {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot create two instances of Skuared !");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		if (!Skript.isRunningMinecraft(1, 8)) {
			getLogger().log(Level.SEVERE, "Outdated minecraft version ! Please update to 1.8 or newer !");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		config = getConfig();
	}

	private void setupConfig() {
		saveDefaultConfig();
		String s = config.getString("wolfram-id");
		if (s.matches("[A-Z0-9]+-[A-Z0-9]+")) {
			wolframId = s;
		}
	}

	private void setupSyntax() {
		addon = Skript.registerAddon(this);
		getLogger().log(Level.INFO, "Registering Skuared's syntax !");
		try {
			getAddon().loadClasses("fr.syst3ms.skuared", "expressions", "effects", "fr.syst3ms.skuared.util");
			mathRegister();
			getLogger().log(Level.INFO, "Successfully registered Skuared's syntaxes !");
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Something went wrong while registering syntax, so some syntaxes may not work");
		}
	}

	private void mathRegister() {
		Functions.registerFunction(new JavaFunction<Number>("or", new Parameter[]{
			new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null),
			new Parameter<>("b", Classes.getExactClassInfo(Number.class), true, null)
		}, Classes.getExactClassInfo(Number.class), true) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{a.longValue() | b.longValue()};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("and", new Parameter[]{
			new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null),
			new Parameter<>("b", Classes.getExactClassInfo(Number.class), true, null)
		}, Classes.getExactClassInfo(Number.class), true) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{a.longValue() & b.longValue()};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("xor", new Parameter[]{
			new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null),
			new Parameter<>("b", Classes.getExactClassInfo(Number.class), true, null)
		}, Classes.getExactClassInfo(Number.class), true) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{a.longValue() ^ b.longValue()};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("nor", new Parameter[]{
			new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null),
			new Parameter<>("b", Classes.getExactClassInfo(Number.class), true, null)
		}, Classes.getExactClassInfo(Number.class), true) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{~(a.longValue() | b.longValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("nand", new Parameter[]{
			new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null),
			new Parameter<>("b", Classes.getExactClassInfo(Number.class), true, null)
		}, Classes.getExactClassInfo(Number.class), true) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{~(a.longValue() & b.longValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("nxor", new Parameter[]{
			new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null),
			new Parameter<>("b", Classes.getExactClassInfo(Number.class), true, null)
		}, Classes.getExactClassInfo(Number.class), true) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{~(a.longValue() ^ b.longValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("gamma", new Parameter[]{new Parameter<>("x", Classes.getExactClassInfo(Number.class), true, null)}, Classes
			.getExactClassInfo(Number.class), true) {
			@Override
			public Number[] execute(FunctionEvent e, Object[][] objects) {
				Number x = (Number) objects[0][0];
				return new Number[]{MathUtils.gamma(x.doubleValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("factorial", new Parameter[]{new Parameter<>("x", Classes.getExactClassInfo(Number.class), true, null)}, Classes
			.getExactClassInfo(Number.class), true) {
			@Override
			public Number[] execute(FunctionEvent e, Object[][] objects) {
				Number x = (Number) objects[0][0];
				return new Number[]{MathUtils.gamma(x.doubleValue() + 1.0d)};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("csc", new Parameter[]{new Parameter<>("x", Classes.getExactClassInfo(Number.class), true, null)}, Classes
			.getExactClassInfo(Number.class), true) {
			@Override
			public Number[] execute(FunctionEvent e, Object[][] objects) {
				Number x = (Number) objects[0][0];
				return new Number[]{MathUtils.csc(x.doubleValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("sec", new Parameter[]{new Parameter<>("x", Classes.getExactClassInfo(Number.class), true, null)}, Classes
			.getExactClassInfo(Number.class), true) {
			@Override
			public Number[] execute(FunctionEvent e, Object[][] objects) {
				Number x = (Number) objects[0][0];
				return new Number[]{MathUtils.sec(x.doubleValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("cot", new Parameter[]{new Parameter<>("x", Classes.getExactClassInfo(Number.class), true, null)}, Classes
			.getExactClassInfo(Number.class), true) {
			@Override
			public Number[] execute(FunctionEvent e, Object[][] objects) {
				Number x = (Number) objects[0][0];
				return new Number[]{MathUtils.cot(x.doubleValue())};
			}
		});
		Algorithms.registerOperator(">>", RightBitShift.class, Associativity.LEFT, 4);
		Algorithms.registerOperator("<<", LeftBitShift.class, Associativity.LEFT, 4);
		Algorithms.registerOperator(">>>", UnsignedRightBitShift.class, Associativity.LEFT, 4);
		Algorithms.registerOperator("+", Sum.class, Associativity.LEFT, 3);
		Algorithms.registerOperator("-", Difference.class, Associativity.LEFT, 3);
		Algorithms.registerOperator("*", Product.class, Associativity.LEFT, 2);
		Algorithms.registerOperator("/", Division.class, Associativity.LEFT, 2);
		Algorithms.registerOperator("%", Modulo.class, Associativity.LEFT, 2);
		Algorithms.registerOperator("^", Power.class, Associativity.RIGHT, 1);
		Algorithms.registerConstant("pi", Math.PI);
		Algorithms.registerConstant("e", Math.E);
		Algorithms.registerConstant("nan", Double.NaN);
		Algorithms.registerConstant("Infinity", Double.POSITIVE_INFINITY);
		Algorithms.registerConstant("phi", MathUtils.PHI);
	}

	public SkriptAddon getAddon() {
		return addon;
	}

	public String getWolframId() {
		return wolframId;
	}
}
