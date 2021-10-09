package Dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import Model.DsThi;
import Model.KiHoc;
import utils.JpaUtils;

abstract public class Dao<T> {
	abstract public String getdatabase();

	abstract public Class<T> getclass();

	private JpaUtils conn;
	private EntityManager entity;
	private EntityTransaction transaction;

	public Dao() {
		this.conn = new JpaUtils();
		this.entity = conn.getEntityManager();
		this.transaction = this.entity.getTransaction();
	}

	public T insert(T t) {
		try {
			this.transaction.begin();
			this.entity.persist(t);
			this.transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			this.transaction.rollback();
		}
		return t;
	};

	public T findbyid(String id) {
		T t = this.entity.find(getclass(), id);
		return t;
	}

	public KiHoc findid(int id) {
		this.entity = conn.getEntityManager();
		KiHoc k = this.entity.find(KiHoc.class, id);
		return k;
	}

	public void delete(String id) {
		T t = this.findbyid(id);
		try {
			this.transaction.begin();
			this.entity.remove(t);
			this.transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(T t) {
		try {
			this.transaction.begin();
			this.entity.merge(t);
			this.transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List<T> getall() {
		String hql = "From" + getdatabase();
		TypedQuery<T> query = entity.createQuery(hql, getclass());
		List<T> t = query.getResultList();
		return t;
	}

	public void updatekihoc() {
		String hql = "UPDATE Kihoc SET trangthai=:status WHERE DATEDIFF(DD,ngayTao,GETDATE())>0";
		TypedQuery<KiHoc> query = this.entity.createQuery(hql, KiHoc.class);
		query.setParameter("status", "Đã kết thúc");
		query.executeUpdate();
	}

	public List<T> getkihoc() {
		List<T> lst = new ArrayList<T>();
		String hql = "FROM KiHoc k ORDER BY idhk DESC";
		TypedQuery<T> query = this.entity.createQuery(hql, getclass());
		lst = query.getResultList();
		return lst;
	}

}
