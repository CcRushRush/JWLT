package sshDao.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import sshDao.IUserDao;
import sshModel.User;
@Repository
public class userDaoImpl implements IUserDao {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public void add(User user) {
        hibernateTemplate.save(user);
    }
}
