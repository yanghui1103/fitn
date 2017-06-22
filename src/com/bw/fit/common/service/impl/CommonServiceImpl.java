package com.bw.fit.common.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.bw.fit.common.service.CommonService;

public class CommonServiceImpl<T,R> implements CommonService<T,R>{
	@Override
	public List<T> filter(List<T> list,Predicate<T> p){
		List<T> result = new ArrayList<>();
		for(T t:list){
			if(p.test(t))
				result.add(t);
		}
		return result ;
	}
	
	@Override
	public Set<T> filter(Set<T> sets,Predicate<T> p){
		Set<T> result = new HashSet<>();
		for(T t:sets){
			if(p.test(t))
				result.add(t);
		}
		return result ;
	}

	@Override
	public void consume(T obj, Consumer<T> c) throws Exception {
		// TODO Auto-generated method stub
		c.accept(obj);
	}

	@Override
	public T supplier(Supplier<T> s) throws Exception {
		// TODO Auto-generated method stub
		return s.get();
	}

	@Override
	public R function(T t, Function<T, R> f) throws Exception {
		// TODO Auto-generated method stub
		return f.apply(t);
	}

	@Override
	public List<T> biFilter(List<T> list, Predicate<T> p,Object j) {
		List<T> result = new ArrayList<>();
		for(T t:list){
			if(p.test(t))
				result.add(t);
		}
		return result ;
	}

	
}
