package Dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import Model.User;
import utils.JpaUtils;

public class UserServies {
	private EntityManager em;

	public UserServies() {
		this.em = JpaUtils.getEntityManager();
	}

	public User findByEmail(String Gmail) {
		String hql = "Select u FROM Users u WHERE u.gmail = :gmail";
		TypedQuery<User> query = em.createQuery(hql, User.class);
		query.setParameter("gmail", Gmail);
		List<User> listUser = query.getResultList();
		for (User us : listUser) {
			return us;
		}
		return null;
	}
}
