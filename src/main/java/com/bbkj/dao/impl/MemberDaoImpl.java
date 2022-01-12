package com.bbkj.dao.impl;

import com.bbkj.common.genericDao.hibernate.GenericDaoImpl;
import com.bbkj.dao.MemberDao;
import com.bbkj.domain.Member;
import org.springframework.stereotype.Repository;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/4/17 16:41
 */
@Repository
public class MemberDaoImpl extends GenericDaoImpl<Member> implements MemberDao {
}
