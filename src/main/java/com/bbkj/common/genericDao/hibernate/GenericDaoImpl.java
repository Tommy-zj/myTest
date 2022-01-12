package com.bbkj.common.genericDao.hibernate;


import com.bbkj.common.genericDao.GenericDao;
import com.bbkj.common.utils.Page;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;


public class GenericDaoImpl<T> extends HibernateDaoSupport implements GenericDao<T> {
    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    protected Class<T> entityClass;

    protected Class getEntityClass() {
        if (entityClass == null) {
            entityClass = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }
        return entityClass;
    }


    @Override
    public T load(long id) {
        HibernateTemplate template = getHibernateTemplate();
        return (T) template.load(getEntityClass(), id);
    }


    @Override
    public Serializable save(T object) {
        HibernateTemplate template = getHibernateTemplate();
        return template.save(object);
    }

    @Override
    public void update(T object) {
        HibernateTemplate template = getHibernateTemplate();
        template.update(object);
    }


    @Override
    public void remove(T object) {
        HibernateTemplate template = getHibernateTemplate();
        template.delete(object);
    }

    @Override
    public void remove(long id) {
        HibernateTemplate template = this.getHibernateTemplate();
        template.delete(load(id));
    }

    @Override
    public void remove(long[] ids) {
        for (long id : ids) {
            remove(id);
        }
    }

    @Override
    public List<T> listAll() {
        HibernateTemplate template = getHibernateTemplate();
        return template.loadAll(getEntityClass());
    }

    @Override
    public List<T> list(final DetachedCriteria detachedCriteria) {
        HibernateTemplate template = this.getHibernateTemplate();
        return (List<T>) template.executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                Criteria criteria = detachedCriteria
                        .getExecutableCriteria(session);
                return criteria.list();
            }
        });
    }

    @Override
    public Page pageList(final DetachedCriteria detachedCriteria, final int pageNo, final int pageSize) {
        HibernateTemplate template = this.getHibernateTemplate();
        //hibernate3为getHibernateTemplate().execute()
        return (Page) template.executeWithNativeSession(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                Criteria criteria = detachedCriteria.getExecutableCriteria(session);
                Number totalCount = (Number) criteria.setProjection(Projections.rowCount()).uniqueResult();//兼容考虑:Hibernate2返回Integer类型，Hibernate3返回Long类型
                criteria.setProjection(null);
                criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);//确认把结果以Entity的形式返回，而不是Object[]的形式返回
                List items = criteria
                        .setFirstResult((pageNo - 1) * pageSize)
                        .setMaxResults(pageSize)
                        .list();
                Page page = new Page(pageNo, pageSize, totalCount == null ? 0 : totalCount.intValue());
                page.setPageList(items);
                return page;
            }
        });
    }


    /**
     * @author JJ
     * @Description 按条件找符合条件的数目
     * @Date 2021/3/30 15:47
     * @Param [detachedCriteria]
     * @Return int
     **/
    @Override
    public Number count(final DetachedCriteria detachedCriteria) {
        HibernateCallback temp = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Criteria criteria = detachedCriteria.getExecutableCriteria(session);
                return (Number) criteria.setProjection(Projections.rowCount()).uniqueResult();
            }
        };
        Number result = (Number) getHibernateTemplate().executeWithNativeSession(temp);
        return result;
    }

    @Override
    public Object query(String hql) {
        return null;
    }
}
