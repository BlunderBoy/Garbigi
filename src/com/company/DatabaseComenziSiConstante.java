package com.company;


public class DatabaseComenziSiConstante
{
	boolean DEBUG;
	
	private DatabaseComenziSiConstante() {} //singleton
	
	static class SingletonHolder{
		public static final DatabaseComenziSiConstante INSTANTA_SINGLETON = new DatabaseComenziSiConstante();
	}
	public static DatabaseComenziSiConstante getInstace()
	{
		return SingletonHolder.INSTANTA_SINGLETON;
	}
}
