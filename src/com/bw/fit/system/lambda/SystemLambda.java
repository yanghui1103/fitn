package com.bw.fit.system.lambda;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.*;

import com.bw.fit.common.model.LogUser;

public class SystemLambda<T,R> {

	public static Function<Integer,Integer> f = x->x+1;
	public static Supplier<Integer> ff = ()->1;
	public static BiFunction<LogUser,List<LogUser>,Boolean> getExisteUser = (user,list)->{
		for(LogUser u:list){
			if(u.getUser_cd().equalsIgnoreCase(user.getUser_cd()) &&  u.getIp().equalsIgnoreCase(user.getIp())){
				return true;
			}
		}
	return false;};  
}
