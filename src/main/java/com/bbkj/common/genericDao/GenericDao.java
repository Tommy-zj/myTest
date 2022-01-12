package com.bbkj.common.genericDao;

import com.bbkj.common.utils.Page;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

/**
 * 数据访问对象基础接口.
 * User: 包永雄
 * Date: 11-4-8
 * Time: 下午6:16
 *
 * @author JJ
 */

public interface GenericDao<T> {
    public T load(long id);

    public Serializable save(T object);

    public void update(T object);

    public void remove(T object);

    public void remove(long id);

    public void remove(long[] ids);

    public List<T> listAll();

    public List<T> list(DetachedCriteria detachedCriteria);

    public Page pageList(final DetachedCriteria detachedCriteria, final int pageNo, final int pageSize);


    public Number count(DetachedCriteria criteria);

    public Object query(String hql);
}
