package fr.syst3ms.skuared;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.function.FunctionEvent;
import ch.njol.skript.lang.function.Functions;
import ch.njol.skript.lang.function.JavaFunction;
import ch.njol.skript.lang.function.Parameter;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.registrations.Classes;
import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.Associativity;
import fr.syst3ms.skuared.util.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Skuared extends JavaPlugin {
	private static Skuared instance;
	private SkriptAddon addon;

	@Override
	public void onEnable() {
		startupChecks();
		getLogger().log(Level.INFO, "Starting up Skuared...");
		setupSyntax();
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
	}

	private void setupSyntax() {
		addon = Skript.registerAddon(this);
		getLogger().log(Level.INFO, "Registering Skuared's syntax !");
		try {
			addon.loadClasses("fr.syst3ms.skuared", "expressions");
			mathRegister();
			getLogger().log(Level.INFO, "Successfully registered Skuared's syntaxes !");
		} catch (Exception e) {
			getLogger().log(Level.WARNING,
				"Something went wrong while registering syntax, so some syntaxes may not work"
			);
		}
	}

	private void mathRegister() {
		Classes.registerClass(new ClassInfo<>(Associativity.class, "associativity")
			.name("associativity")
			.user("(?: operator)? associativity")
			.usage("left", "right")
			.defaultExpression(new SimpleLiteral<>(Associativity.LEFT, true))
			.parser(new Parser<Associativity>() {
				@Override
				@Nullable
				public Associativity parse(String s, ParseContext context) {
					try {
						return Associativity.valueOf(s);
					} catch (IllegalArgumentException e) {
						return null;
					}
				}

				@Override
				public String toString(Associativity o, int flags) {
					return o.name().replace('_', ' ').toLowerCase();
				}

				@Override
				public String toVariableNameString(Associativity o) {
					return o.name();
				}

				@Override
				@Language("Regexp")
				public String getVariableNamePattern() {
					return "(LEFT|RIGHT)";
				}
			}));
		Functions.registerFunction(new JavaFunction<Number>("or",
			new Parameter[]{new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null), new Parameter<>(
				"b",
				Classes.getExactClassInfo(Number.class),
				true,
				null
			)},
			Classes.getExactClassInfo(Number.class),
			true
		) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{a.longValue() | b.longValue()};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("and",
			new Parameter[]{new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null), new Parameter<>(
				"b",
				Classes.getExactClassInfo(Number.class),
				true,
				null
			)},
			Classes.getExactClassInfo(Number.class),
			true
		) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{a.longValue() & b.longValue()};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("xor",
			new Parameter[]{new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null), new Parameter<>(
				"b",
				Classes.getExactClassInfo(Number.class),
				true,
				null
			)},
			Classes.getExactClassInfo(Number.class),
			true
		) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{a.longValue() ^ b.longValue()};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("nor",
			new Parameter[]{new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null), new Parameter<>(
				"b",
				Classes.getExactClassInfo(Number.class),
				true,
				null
			)},
			Classes.getExactClassInfo(Number.class),
			true
		) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{~(a.longValue() | b.longValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("nand",
			new Parameter[]{new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null), new Parameter<>(
				"b",
				Classes.getExactClassInfo(Number.class),
				true,
				null
			)},
			Classes.getExactClassInfo(Number.class),
			true
		) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{~(a.longValue() & b.longValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("nxor",
			new Parameter[]{new Parameter<>("a", Classes.getExactClassInfo(Number.class), true, null), new Parameter<>(
				"b",
				Classes.getExactClassInfo(Number.class),
				true,
				null
			)},
			Classes.getExactClassInfo(Number.class),
			true
		) {
			@NotNull
			@Override
			public Number[] execute(FunctionEvent functionEvent, Object[][] objects) {
				Number a = (Number) objects[0][0], b = (Number) objects[1][0];
				return new Number[]{~(a.longValue() ^ b.longValue())};
			}
		});
		Algorithms.registerOperator(">>", MathUtils::shr, Associativity.LEFT, 4);
		Algorithms.registerOperator("<<", MathUtils::shl, Associativity.LEFT, 4);
		Algorithms.registerOperator(">>>", MathUtils::ushr, Associativity.LEFT, 4);
		Algorithms.registerOperator("+", MathUtils::plus, Associativity.LEFT, 3);
		Algorithms.registerOperator("-", MathUtils::minus, Associativity.LEFT, 3);
		Algorithms.registerOperator("*", MathUtils::times, Associativity.LEFT, 2);
		Algorithms.registerOperator("/", MathUtils::divide, Associativity.LEFT, 2);
		Algorithms.registerOperator("^", MathUtils::pow, Associativity.RIGHT, 1);
		Algorithms.registerConstant("pi", Math.PI);
		Algorithms.registerConstant("e", Math.E);
		Algorithms.registerConstant("nan", Double.NaN);
		Algorithms.registerConstant("Infinity", Double.POSITIVE_INFINITY);
	}

	public static Skuared getInstance() {
		return instance;
	}

	public static Logger logger() {
		return getInstance().getLogger();
	}
}