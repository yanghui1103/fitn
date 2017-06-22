package com.bw.fit.common.service;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface CommonService<T,R> {
	public List<T> filter(List<T> list,Predicate<T> p);
	public List<T> biFilter(List<T> list,Predicate<T> p,Object j);
	public Set<T> filter(Set<T> sets,Predicate<T> p);
	public void consume(T obj,Consumer<T> c) throws Exception;
	public T supplier(Supplier<T> c) throws Exception;
	public R function(T t,Function<T,R> f) throws Exception; 
}
