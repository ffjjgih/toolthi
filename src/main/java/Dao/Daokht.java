package Dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import Model.DsThi;
import Model.KiHoc;
import utils.JpaUtils;

public class Daokht extends Dao<DsThi> {
	private DsThi kht;
	private List<DsThi> lstkht;
	private JpaUtils jpa;
	private EntityManager entity;
	private EntityManagerFactory factory;
	private EntityTransaction trans;

	public Daokht() {
		this.kht = new DsThi();
		this.lstkht = new ArrayList<>();
		this.entity = this.jpa.getEntityManager();
		this.factory = entity.getEntityManagerFactory();
	}

	public List<DsThi> findbykihoc(KiHoc k) {
		this.entity = this.jpa.getEntityManager();
		String hql = "SELECT d FROM DsThi d WHERE kiHoc=:ky_hoc";
		TypedQuery<DsThi> query = this.entity.createQuery(hql, DsThi.class);
		query.setParameter("ky_hoc", k);
		this.lstkht = query.getResultList();
		return this.lstkht;
	}

	public void deleteKHT(int idKHT) throws Exception {
		this.trans = this.entity.getTransaction();
		
		try {
			trans.begin();
			
			DsThi kht = this.entity.find(DsThi.class, idKHT);
			
			if (kht != null) {
				this.entity.remove(kht);
			} else {
				throw new Exception("Not find Kế Hoạch thi, please try again!");
			}
			
			this.trans.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.trans.rollback();
			throw e;
		} finally {
			this.entity.close();
		}
	}
	
	public List<DsThi> findKHTByClass(String idClass){
		this.lstkht = new ArrayList<DsThi>();
		
		String sql = "SELECT * FROM ds_thi p WHERE p.LOP = ?";
		Query query = this.entity.createNativeQuery(sql, DsThi.class);
		query.setParameter(1, idClass);
		this.lstkht = query.getResultList();
		
		return this.lstkht;
	}
	
	public DsThi getKHTById(int idkh) {
		this.kht = new DsThi();
		this.kht = this.entity.find(DsThi.class, idkh);
		return kht;
	}
	
	public List<DsThi> findkht(String mamon,String lop,KiHoc k){
		String hql="SELECT d FROM DsThi d WHERE maMon=:id_mon AND lop=:id_lop AND kiHoc=:id_ky";
		TypedQuery<DsThi> query=this.entity.createQuery(hql,DsThi.class);
		query.setParameter("id_mon", mamon);
		query.setParameter("id_lop", lop);
		query.setParameter("id_ky", k);
		this.lstkht=query.getResultList();
		return this.lstkht;
	}

	@Override
	public String getdatabase() {
		return DsThi.class.getSimpleName();
	}

	@Override
	public Class<DsThi> getclass() {
		return DsThi.class;
	}
}
