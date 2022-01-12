package com.bbkj.action;


import com.bbkj.common.BaseAction;
import com.bbkj.common.utils.CommonUtils;
import com.bbkj.common.utils.Page;
import com.bbkj.domain.Member;
import com.bbkj.domain.Skill;
import com.bbkj.service.MemberService;
import com.bbkj.service.SkillService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2021/2/26 9:52
 */
@Setter
@Getter
@Controller
@ParentPackage("post")
@Namespace("/skill")@Slf4j
public class SkillAction extends BaseAction {
    @Autowired
    private SkillService skillService;
    private long paren_id; //父类id
    private String name;
    private long id;
    private int pageNo;
    private int pageSize;
    private long[] ids;
    @Autowired
    private MemberService memberService;
    private long person_id;

    public Skill setSkill() {
        Skill skill = new Skill();
        skill.setName(name);
        skill.setParen_id(paren_id);
        return skill;
    }

    @Action(value = "add", interceptorRefs = @InterceptorRef("json"))
    public String add() throws Exception {
        if (name != null) {
            Skill skill = setSkill();
            DetachedCriteria criteria = DetachedCriteria.forClass(Skill.class)
                    .add(Restrictions.eq("name", name));
            List<Skill> skills = skillService.list(criteria);
            if (skills.size() > 0) {
                responses(204, "有相同分类！");
                return null;
            }
            skillService.save(skill);
            responses(200, "新技能添加成功");
            return null;
        }
        responses(301, "名字为空");
        return null;
    }

    @Action(value = "deleteList", interceptorRefs = @InterceptorRef("json"))
    public String deleteList() throws Exception {
        if (ids.length > 0) {
            skillService.remove(ids);
            //如果是删除一级技能分类，则要删除对应的二级分类
            if (paren_id == 0) {
                for (long id : ids) {
                    try {
                        Skill skill = skillService.findById(id);
                        skillService.remove(skill);
                    } catch (Exception e) {
                        log.info("一级分类下没有二级分类");
                    }
                }
                responses(200, "二级分类删除成功！");
                return null;
            }
            responses(200, "批量删除完成");
            return null;
        }
        responses(201, "ids为空！");
        return null;
    }

    @Action(value = "delete", interceptorRefs = @InterceptorRef("json"))
    public String delete() throws Exception {
        if (id != 0) {
            if (paren_id == 0) {
                //删除一级分类
                Skill skill = skillService.findById(id);
                skillService.remove(skill);
                //删除一级分类下的二级分类
                DetachedCriteria criteria = DetachedCriteria.forClass(Skill.class)
                        .add(Restrictions.eq("paren_id", id));
                List<Skill> skills = skillService.list(criteria);
                for (Skill skill1 : skills) {
                    skillService.remove(skill1);
                }
                responses(200, "删除成功！");
                return null;
            }
            //删除二级分类
            Skill skill = skillService.findById(id);
            skillService.remove(skill);
            responses(200, "删除成功！");
            return null;
        }
        response(301, "id为空！");
        return null;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 通过person_id找到此人的技能标签
     * @Date 2021/12/3 17:17
     **/
    @Action("getPersonTags")
    public String getPersonTags() throws IOException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Member.class);
            criteria.add(Restrictions.eq("id", person_id));
            List<Member> members = memberService.list(criteria);
            if (members.size() > 0) {
                Member member = members.get(0);
                String tags = member.getTag();
                response(200, tags);
                return null;
            }
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: TODO 每一行都需要一级分类和二级分类
     * @author JJ
     * @date 2021/2/27 14:45
     * @version 1.0
     */
    @Action("listFirstAndSen")
    public String listFirstAndSen() throws Exception {
        //找到一级分类
        DetachedCriteria criteria = DetachedCriteria.forClass(Skill.class)
                .addOrder(Order.desc("id"))
                .add(Restrictions.gt("paren_id", 0L));
        Page page = skillService.pageList(criteria, pageNo, pageSize);
        List<Skill> skills = page.getPageList();
        List result = new ArrayList();
        for (Skill skill : skills) {
            //如果是二级分类
            if (skill.getParen_id() != 0) {
                //将集合变为map
                try {
                    Map skillMap = CommonUtils.convertToMap(skill);
                    Skill skill1 = skillService.findById(skill.getParen_id());
                    Map map = new HashMap();
                    map.put("firstSkillName", skill1.getName());
                    skillMap.putAll(map);
                    result.add(skillMap);
                } catch (Exception e) {
                    log.info("找不到对应的一级分类");
                }
            }
        }
        pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), result);
        return null;
    }

    @Action(value = "edit", interceptorRefs = @InterceptorRef("json"))
    public String edit() throws Exception {
        if (id != 0) {
            try {
                Skill skill = setSkill();
                skill.setId(id);
                skill.setName(name);
                skill.setParen_id(0);
                skillService.update(skill);
                response(200, "修改内容成功！");
            } catch (Exception e) {
                log.info("找不到对应id的值");
            }
            return null;
        }
        response(301, "id为空！");
        return null;
    }

    /**
     * @description: TODO 查找一级技能分类
     * @author JJ
     * @date 2021/2/26 14:48
     * @version 1.0
     */
    @Action("listFirstSkill")
    public String listFirstSkill() throws Exception {
        DetachedCriteria criteria = DetachedCriteria.forClass(Skill.class)
                .add(Restrictions.eq("paren_id", 0l));
        Page page = skillService.pageList(criteria, pageNo, pageSize);
        pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), page.getPageList());
        return null;
    }


    /**
     * @description: TODO 单独获取全部一级分类
     * @author JJ
     * @date 2021/2/27 15:25
     * @version 1.0
     */
    @Action("getFirstSkillAll")
    public String getFirstSkillAll() throws Exception {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Skill.class)
                    .add(Restrictions.eq("paren_id", 0l));
            responses(200, "全部一级分类", skillService.list(criteria));
        } catch (Exception e) {
            responses(301, "程序异常");
        }

        return null;
    }

    /**
     * @description: TODO 获取一级分类和二级分类
     * @author JJ
     * @date 2021/2/26 14:41
     * @version 1.0
     */
    @Action("list")
    public String list() throws Exception {
        //找父类
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Skill.class)
                    .add(Restrictions.eq("paren_id", 0l));
            Page page = skillService.pageList(criteria, pageNo, pageSize);
            List<Skill> skills = page.getPageList();
            List result = delResult(skills);
            pageList(page.getTotalPage(), page.getTotalNum(), page.getPageNo(), result);
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    @Action("listAll")
    public String listAll() throws Exception {
        try {
            //找父类
            DetachedCriteria criteria = DetachedCriteria.forClass(Skill.class)
                    .add(Restrictions.eq("paren_id", 0l));
            List<Skill> skills = skillService.list(criteria);
            List result = delResult(skills);
            responses(200, "找到了", result);
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: TODO 处理返回的结果
     * @author JJ
     * @date 2021/2/26 11:13
     * @version 1.0
     */
    public List delResult(List<Skill> skills) {
        try {
            List result = new ArrayList();
            for (Skill skill : skills) {
                Map skillMap = new HashMap();
                skillMap.put("name", skill.getName());
                skillMap.put("paren_id", skill.getParen_id());
                skillMap.put("id", skill.getId());
                DetachedCriteria criteria1 = DetachedCriteria.forClass(Skill.class)
                        .add(Restrictions.eq("paren_id", skill.getId()));
                List<Skill> skills1 = skillService.list(criteria1);
                skillMap.put("skills", skills1);
                result.add(skillMap);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: TODO 给对应的技工增加标签
     * @author JJ
     * @date 2021/3/2 14:16
     * @version 1.0
     */
    @Action(value = "addSkill", interceptorRefs = {@InterceptorRef("json"), @InterceptorRef("checkStack")})
    public String addSkill() throws Exception {
        if (person_id != 0 && name != null) {
            try {
                Member member = memberService.findById(person_id);
                if (member.getOpenid() != null) {
                    //找到了相对应的用户
                    member.setTag(name);
                    memberService.update(member);
                    responses(200, "技能标签添加完成！");
                    return null;
                }
                responses(201, "众里寻他千百度，慕然回首，那人在栏杆处");
            } catch (Exception e) {
                log.info("人生总会有意外，总会有遗憾！");
                responses(203, e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @return java.lang.String
     * @author JJ
     * @Description 获取个人信息
     * @Date 2021/12/4 10:23
     **/
    @Action("getPerson")
    public String getPerson() throws Exception {
        try {
            if (person_id > 0L) {
                // Member member = memberService.findById(person_id);
                DetachedCriteria criteria = DetachedCriteria.forClass(Member.class).add(Restrictions.eq("id", person_id));
                List<Member> members = memberService.list(criteria);
                if (members.size() > 0) {
                    responses(200, "success", members.get(0));
                    return null;
                }
                responses(301, "未找到与person_id相匹配的项");
                return null;
            }
            response(500, "person_id为空");
        } catch (Exception e) {
            response(500, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
