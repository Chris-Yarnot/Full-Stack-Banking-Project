package com.revature;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.database.DAOs;
import com.revature.presentation.Presentation;
import com.revature.presentation.PresentationImpl;
import com.revature.service.DaoService;
import com.revature.service.DaoServiceImpl;


public class MainDriver {
	public static final Logger logger= Logger.getLogger(MainDriver.class);
	public static final Scanner input= new Scanner(System.in);
	public static final DAOs daos= new DAOs();
	public static final DaoService daoService= new DaoServiceImpl(daos, logger);
	public static final Presentation presentation= new PresentationImpl(daoService);
	public static void main(String[] args) {
			presentation.run();
		//appLayerTools.run();
	}

}
