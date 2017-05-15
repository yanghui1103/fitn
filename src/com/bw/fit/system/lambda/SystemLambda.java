package com.bw.fit.system.lambda;

import java.util.function.Function;
import java.util.function.Supplier;

public class SystemLambda {

	public static Function<Integer,Integer> f = x->x+1;
	public static Supplier<Integer> ff = ()->1;
}
