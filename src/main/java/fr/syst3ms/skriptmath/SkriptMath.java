package fr.syst3ms.skriptmath;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.function.FunctionEvent;
import ch.njol.skript.lang.function.Functions;
import ch.njol.skript.lang.function.JavaFunction;
import ch.njol.skript.lang.function.Parameter;
import ch.njol.skript.registrations.Classes;
import fr.syst3ms.skriptmath.util.MathUtils;
import fr.syst3ms.skriptmath.util.evaluation.*;
import fr.syst3ms.skriptmath.util.Algorithms;
import fr.syst3ms.skriptmath.util.Associativity;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class SkriptMath extends JavaPlugin {
	private static SkriptMath instance;
	private SkriptAddon addon;
	private FileConfiguration config;
	private String wolframId = null;

	public static SkriptMath getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		startupChecks();
		getLogger().log(Level.INFO, "Starting up skript-math...");
		setupSyntax();
		setupConfig();
		getLogger().log(Level.INFO, "Successfully started up skript-math !");
	}

	@Override
	public void onDisable() {
	}

	private void startupChecks() {
		if (instance == null) {
			instance = this;
		} else {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot create two instances of skript-math !");
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
		getLogger().log(Level.INFO, "Registering skript-math's syntax !");
		try {
			getAddon().loadClasses("fr.syst3ms.skriptmath", "expressions", "effects", "util");
			mathRegister();
			getLogger().log(Level.INFO, "Successfully registered skript-math's syntaxes !");
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Something went wrong while registering syntax, so some syntaxes may not work");
		}
	}

	private void mathRegister() {
		Classes.registerClass(new ClassInfo<>(MathExpression.class, "mathexpression")
				.user("math(ematical)? expressions?")
				.name("Math Expression")
				.description("A compiled math expression.")
				.since("2.2-dev16")
				.parser(new Parser<MathExpression>() {

					@Override
					@Nullable
					public MathExpression parse(String s, ParseContext context) {
						return null;
					}

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(MathExpression o, int flags) {
						MathTerm t = o.getTerm();
						if (t == null)
							return null;
						return t.asString();
					}

					@SuppressWarnings("null")
					@Override
					public String toVariableNameString(MathExpression o) {
						MathTerm t = o.getTerm();
						if (t == null)
							return null;
						return t.asString().replace(" ", "");
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

				}));
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
		Functions.registerFunction(new JavaFunction<Number>("digamma", new Parameter[]{new Parameter<>("x", Classes.getExactClassInfo(Number.class), true, null)}, Classes
			.getExactClassInfo(Number.class), true) {
			@Override
			public Number[] execute(FunctionEvent e, Object[][] objects) {
				Number x = (Number) objects[0][0];
				return new Number[]{MathUtils.digamma(x.doubleValue())};
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
		Functions.registerFunction(new JavaFunction<Number>("acsc", new Parameter[]{new Parameter<>("x", Classes.getExactClassInfo(Number.class), true, null)}, Classes
			.getExactClassInfo(Number.class), true) {
			@Override
			public Number[] execute(FunctionEvent e, Object[][] objects) {
				Number x = (Number) objects[0][0];
				return new Number[]{MathUtils.acsc(x.doubleValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("asec", new Parameter[]{new Parameter<>("x", Classes.getExactClassInfo(Number.class), true, null)}, Classes
			.getExactClassInfo(Number.class), true) {
			@Override
			public Number[] execute(FunctionEvent e, Object[][] objects) {
				Number x = (Number) objects[0][0];
				return new Number[]{MathUtils.asec(x.doubleValue())};
			}
		});
		Functions.registerFunction(new JavaFunction<Number>("acot", new Parameter[]{new Parameter<>("x", Classes.getExactClassInfo(Number.class), true, null)}, Classes
			.getExactClassInfo(Number.class), true) {
			@Override
			public Number[] execute(FunctionEvent e, Object[][] objects) {
				Number x = (Number) objects[0][0];
				return new Number[]{MathUtils.acot(x.doubleValue())};
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
