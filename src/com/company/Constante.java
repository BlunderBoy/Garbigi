package com.company;

public class Constante
{
	boolean DEBUG;
	
	private Constante() {} //singleton
	
	static class SingletonHolder{
		public static final Constante INSTANTA_SINGLETON = new Constante();
	}
	public static Constante getInstace()
	{
		return SingletonHolder.INSTANTA_SINGLETON;
	}
}
