package Dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import Model.KiHoc;
import utils.JpaUtils;

public class Daokithi extends Dao<KiHoc> {
	private JpaUtils conjpa;
	private EntityManager manager;
	private EntityTransaction transaction;
	private KiHoc k;
	private List<KiHoc> lst;

	public void daokithi() {
		this.conjpa = new JpaUtils();
//		this.manager = this.conjpa.getEntityManager();
		this.k = new KiHoc();
		this.lst = new ArrayList<KiHoc>();
	}
	
	public KiHoc findkihoc(int id) {
		this.manager=this.conjpa.getEntityManager();
		this.k=this.manager.find(KiHoc.class, id);
		return k;
	}

	@Override
	public String getdatabase() {
		return KiHoc.class.getSimpleName();
	}

	@Override
	public Class<KiHoc> getclass() {
		return KiHoc.class;
	}

}
