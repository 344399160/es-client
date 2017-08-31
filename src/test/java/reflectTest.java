import bo.Enterprise;
import bo.Instrument;
import cn.com.deepdata.elasticsearch.core.ElasticsearchTemplate;
import cn.com.deepdata.elasticsearch.core.query.Criterion;
import cn.com.deepdata.elasticsearch.core.query.QueryBuilders;
import cn.com.deepdata.elasticsearch.core.query.QueryConstructor;
import cn.com.deepdata.elasticsearch.model.Page;
import cn.com.deepdata.elasticsearch.model.Result;
import com.alibaba.fastjson.JSON;
import model.Identity;
import model.OneTest;
import model.Phone;
import model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author: qiaobin
 * @Description:
 * @Date: Created in 13:52 2017/7/28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test.xml")
public class reflectTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void createIndex() {
        boolean f = elasticsearchTemplate.createIndex("qiaobin");
        Assert.assertTrue(f);
    }
    @Test
    public void deleteIndex() {
        elasticsearchTemplate.deleteIndex(User.class);
    }

    @Test
    public void putMapping() {
        elasticsearchTemplate.putMapping(User.class);
    }

    @Test
    public void save() {
        List<User> userList = new ArrayList<>();
        //-------
        User user = new User();
        user.setAge(5);
        user.setName("ffff");
        user.setContent("adfsdaf");
        Phone phone = new Phone();
        phone.setNum("12344123");
        phone.setType("lt");
        Phone phone33 = new Phone();
        phone33.setNum("555123");
        phone33.setType("yd");
        List<Phone> list = new ArrayList<>();
        list.add(phone);
        list.add(phone33);
        user.setPhones(list);
        //---------
        User user1 = new User();
        user1.setAge(3);
        user1.setName("qiaobin");
        user1.setContent("content");
        Phone phone1 = new Phone();
        phone1.setNum("123123");
        phone1.setType("lt");
        Phone phone11 = new Phone();
        phone11.setNum("115");
        phone11.setType("665");
        List<Phone> list1 = new ArrayList<>();
        list1.add(phone1);
        list1.add(phone11);
        user1.setPhones(list1);
        userList.add(user1);
        userList.add(user);
        User save = elasticsearchTemplate.save(user);
        System.out.println(save);
    }

    @Test
    public void update() {
        User user = new User();
        user.setIdS("AV2mIC7psi26-_grs9TT");
        user.setAge(1111111);
        user.setName("111111");
        user.setContent("111111111");
        User update = elasticsearchTemplate.update(user);
        System.out.println(update);

    }

    @Test
    public void delete() {
        elasticsearchTemplate.delete("AV2mIC7psi26-_grs9TT", User.class);
    }

    @Test
    public void findById() {
//        User byId = elasticsearchTemplate.findById("AV2npJJdsi26-_grs9Td", User.class);
//        System.out.println(byId);
        medinvise.Identity byId = elasticsearchTemplate.findById("AV304secHQNqCQlKSBi7", medinvise.Identity.class);
        System.out.println(byId);
    }

    @Test
    public void count() {
        System.out.println(elasticsearchTemplate.count(User.class));
    }

    @Test
    public void bulkSave() {
        User user = new User();
        user.setAge(2);
        user.setName("b");
        user.setContent("b");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        user.setList(list);
        elasticsearchTemplate.save(user);
    }

    @Test
    public void search() {
        List<Map<String, Object>> result = elasticsearchTemplate.search(new QueryConstructor(), new String[]{"name", "age"}, null, User.class);
        for (Map<String, Object> user : result) {
            System.out.println(user);
        }
    }

    @Test
    public void stat() {
        Map<String, Object> name = elasticsearchTemplate.statSearch(null, "phones.num", User.class);
        System.out.println(name);

    }

    @Test
    public void testtt() {
//        elasticsearchTemplate.putMapping(Identity.class);
        elasticsearchTemplate.putMapping(medinvise.User.class);
//        Identity identity = new Identity();
//        identity.setUserId("AV2rI-6Ksi26-_grs9Tp");
//        identity.setAdmin(0);
//        identity.setType("0");
//        identity.setFullOrgName("冬冬医疗");
//        identity.setDuty(Arrays.asList("董事长","CTO"));
//        Identity save = elasticsearchTemplate.save(identity);
//        System.out.println(save);
//        Identity identity = elasticsearchTemplate.findById("AV2r-34Vsi26-_grs9T1", Identity.class);
//        System.out.println(identity);

//        medinvise.User byId = elasticsearchTemplate.findById("AV2r6p7Psi26-_grs9Tx", medinvise.User.class);
//        System.out.println(byId);

//        QueryConstructor constructor = new QueryConstructor();
//        constructor.must(new QueryBuilders().term("userId", "AV2sPyGwsi26-_grs-em"));
//        Iterator<Identity> iterator = elasticsearchTemplate.search(constructor, Identity.class).iterator();
//        System.out.println(iterator.next());
    }

    @Test
    public void countTest() {
        QueryConstructor constructor = new QueryConstructor();
        constructor.must(new QueryBuilders().term("userId", "AV2scbbxsi26-_grs-e2"));
        long l =  elasticsearchTemplate.count(constructor, "userId", Identity.class);
        System.out.println(l);
    }

    @Test
    public void one() {
        elasticsearchTemplate.putMapping(OneTest.class);
        OneTest one = new OneTest();
        one.setName("haha");
        one.setIdS("123");
        Phone phone = new Phone();
        phone.setType("lt");
        phone.setNum("18600181040");
        elasticsearchTemplate.save(one);
        OneTest byId = elasticsearchTemplate.findById("123", OneTest.class);
        System.out.println(byId);

    }

    @Test
    public void fuzzy() {
        String name = "东北";
//        List<String> list = new ArrayList<>();
        Set<String> list = new HashSet<>();
        list.add("LN");
        QueryConstructor constructor = new QueryConstructor();
        constructor.should(new QueryBuilders().fuzzy("enterpriseName", name).fuzzy("operName", name).fuzzy("partners.stockName", name));  //企业名称、投资机构、创始人 匹配任意
        if (!CollectionUtils.isEmpty(list)) {
            constructor.must(new QueryBuilders().term("province", "LN").range("startDate", Long.parseLong("829670400000"), Long.parseLong("1211040000000")));  //省份
        }
        constructor.setMinimumNumberShouldMatch(1);
        Iterable<Enterprise> enterprises = elasticsearchTemplate.search(constructor, Enterprise.class);
        Iterator<Enterprise> iterator = enterprises.iterator();
        System.out.println(iterator);
    }

    @Test
    public void get() {
        String sql = "select * from company_project limit 1";
        Page<Enterprise> enterprisePage = elasticsearchTemplate.sqlPageQuery(sql, Enterprise.class);
        System.out.println(enterprisePage);
    }

    @Test
    public void parse() {
        String parser = "{\"took\":2,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"failed\":0},\"hits\":{\"total\":2,\"max_score\":0.0,\"hits\":[{\"_index\":\"enterprise\",\"_type\":\"detail\",\"_id\":\"AV3F5BROSHN9pYddBcoa\",\"_score\":0.0,\"_source\":{\"end_date\":\"1900-01-01 00:00:00\",\"regNo\":\"\",\"addresses\":[{\"address\":\"大连市金州区登沙河临港工业区河滨南路18号\",\"name\":\"\",\"postcode\":\"\"}],\"supDtaSource\":\"http://i.yjapi.com\",\"entid\":\"6d0d8ea6d74fda77d98d25bb5982ffb7\",\"sortTime\":\"2017-06-19 13:48:38\",\"econKind\":\"有限责任公司(国有控股)\",\"type\":\"flumetype\",\"clientUuid\":\"ff80808157f6294c0157fefc89987508\",\"keyNo\":\"6d0d8ea6d74fda77d98d25bb5982ffb7\",\"province\":\"LN\",\"registCapi\":\"364417万人民币\",\"scope\":\"钢铁冶炼；钢压延加工；汽车保养；汽车（轿车除外）销售；特殊钢产品、深加工产品及附加产品生产、销售；机械加工制造；机电设备设计、制造、安装、维修；来料加工；房屋、设备租赁；商标租赁；特殊钢冶金和压延加工技术咨询、技术培训、技术服务；货物及技术进出口；有线电视服务。（依法须经批准的项目，经相关部门批准后方可开展经营活动。）\",\"operName\":\"董事\",\"tel\":\"0411-62693333\",\"id\":\"43abe099802ce24c8fbcd5331d486aa4d8fca0f1\",\"saveTime\":\"2017-06-19 13:48:38\",\"enterpriseName\":\"东北特殊钢集团有限责任公司\",\"email\":\"tzgl@dtsteel.com\",\"changeRecords\":[{\"changeDate\":\"2017-02-23 00:00:00\",\"beforeContent\":\"1黑龙江省人民政府国有资产监督管理委员会;2辽宁省人民政府国有资产监督管理委员会;3辽宁省国有资产经营有限公司;4中国东方资产管理公司;\",\"seqNo\":0,\"afterContent\":\"1黑龙江省人民政府国有资产监督管理委员会;2辽宁省人民政府国有资产监督管理委员会;3辽宁物产集团有限责任公司;4中国东方资产管理股份有限公司;\",\"changeItem\":\"投资人变更（包括出资额、出资方式、出资日期、投资人名称等）\"}],\"index\":\"flume-company-info\",\"termEnd\":\"1900-01-01 00:00:00\",\"checkDate\":\"2017-02-23 00:00:00\",\"branches\":[{\"seqNo\":0,\"name\":\"东北特殊钢集团有限责任公司大连炼铁分公司\",\"regNo\":\"210213000081207\",\"belongOrg\":\"大连金普新区市场监督管理局\"}],\"unscid\":\"912100007497716597\",\"tags\":[{\"tag\":\"bad_record\"},{\"tag\":\"demo\"}],\"termStart\":\"1996-05-17 00:00:00\",\"websiteName\":\"\",\"risks\":[{\"riskId\":null,\"riskName\":null,\"flag\":\"average_company_risk_for_user\",\"value\":0,\"desc\":false,\"riskFlagName\":\"用户风险均值\",\"dnaScoreV2\":0,\"riskLevel\":1},{\"riskId\":null,\"riskName\":null,\"flag\":\"average_company_risk_for_tenant\",\"value\":0,\"desc\":false,\"riskFlagName\":\"租户风险均值\",\"dnaScoreV2\":0,\"riskLevel\":1}],\"partners\":[{\"invertType\":\"其他\",\"exId\":\"0\",\"inpSeqNo\":0,\"identifyNo\":\"70284265-9\",\"stockPercent\":\"\",\"identifyType\":\"机关法人登记证\",\"stockName\":\"黑龙江省人民政府国有资产监督管理委员会\",\"realCapi\":\"\",\"realCapiItems\":{\"investType\":\"机关法人登记证\",\"realCapiDate\":\"\",\"realCapi\":\"\"},\"shouldCapi\":\"52910万\",\"invertName\":\"2008年06月20日\",\"stockType\":\"机关法人\",\"shouldCapiItems\":null}],\"belongOrg\":\"辽宁省工商行政管理局\",\"domain\":\"http://www.dtgroup.cn/\",\"name\":\"东北特殊钢集团有限责任公司\",\"employees\":[{\"jobTitle\":\"董事\",\"name\":\"张洪坤\",\"seqNo\":0}],\"startDate\":1171468800000,\"hash\":\"东北特殊钢集团有限责任公司\",\"status\":\"存续（在营、开业、在册）\",\"lastUpdateTime\":\"2017-06-19 13:48:38\"}},{\"_index\":\"enterprise\",\"_type\":\"detail\",\"_id\":\"AV3F_a9dSHN9pYddBcoh\",\"_score\":0.0,\"_source\":{\"end_date\":\"1900-01-01 00:00:00\",\"regNo\":\"\",\"addresses\":[{\"address\":\"大连市金州区登沙河临港工业区河滨南路18号\",\"name\":\"\",\"postcode\":\"\"}],\"supDtaSource\":\"http://i.yjapi.com\",\"entid\":\"6d0d8ea6d74fda77d98d25bb5982ffb7\",\"sortTime\":\"2017-06-19 13:48:38\",\"econKind\":\"有限责任公司(国有控股)\",\"type\":\"flumetype\",\"clientUuid\":\"ff80808157f6294c0157fefc89987508\",\"keyNo\":\"6d0d8ea6d74fda77d98d25bb5982ffb7\",\"province\":\"LN\",\"registCapi\":\"364417万人民币\",\"scope\":\"钢铁冶炼；钢压延加工；汽车保养；汽车（轿车除外）销售；特殊钢产品、深加工产品及附加产品生产、销售；机械加工制造；机电设备设计、制造、安装、维修；来料加工；房屋、设备租赁；商标租赁；特殊钢冶金和压延加工技术咨询、技术培训、技术服务；货物及技术进出口；有线电视服务。（依法须经批准的项目，经相关部门批准后方可开展经营活动。）\",\"operName\":\"董事\",\"tel\":\"0411-62693333\",\"id\":\"43abe099802ce24c8fbcd5331d486aa4d8fca0f1\",\"saveTime\":\"2017-06-19 13:48:38\",\"enterpriseName\":\"东北特殊钢集团有限责任公司\",\"email\":\"tzgl@dtsteel.com\",\"changeRecords\":[{\"changeDate\":\"2017-02-23 00:00:00\",\"beforeContent\":\"1黑龙江省人民政府国有资产监督管理委员会;2辽宁省人民政府国有资产监督管理委员会;3辽宁省国有资产经营有限公司;4中国东方资产管理公司;\",\"seqNo\":0,\"afterContent\":\"1黑龙江省人民政府国有资产监督管理委员会;2辽宁省人民政府国有资产监督管理委员会;3辽宁物产集团有限责任公司;4中国东方资产管理股份有限公司;\",\"changeItem\":\"投资人变更（包括出资额、出资方式、出资日期、投资人名称等）\"}],\"index\":\"flume-company-info\",\"termEnd\":\"1900-01-01 00:00:00\",\"checkDate\":\"2017-02-23 00:00:00\",\"branches\":[{\"seqNo\":0,\"name\":\"东北特殊钢集团有限责任公司大连炼铁分公司\",\"regNo\":\"210213000081207\",\"belongOrg\":\"大连金普新区市场监督管理局\"}],\"unscid\":\"912100007497716597\",\"tags\":[{\"tag\":\"bad_record\"},{\"tag\":\"demo\"}],\"termStart\":\"1996-05-17 00:00:00\",\"websiteName\":\"\",\"risks\":[{\"riskId\":null,\"riskName\":null,\"flag\":\"average_company_risk_for_user\",\"value\":0,\"desc\":false,\"riskFlagName\":\"用户风险均值\",\"dnaScoreV2\":0,\"riskLevel\":1},{\"riskId\":null,\"riskName\":null,\"flag\":\"average_company_risk_for_tenant\",\"value\":0,\"desc\":false,\"riskFlagName\":\"租户风险均值\",\"dnaScoreV2\":0,\"riskLevel\":1}],\"partners\":[{\"invertType\":\"其他\",\"exId\":\"0\",\"inpSeqNo\":0,\"identifyNo\":\"70284265-9\",\"stockPercent\":\"\",\"identifyType\":\"机关法人登记证\",\"stockName\":\"黑龙江省人民政府国有资产监督管理委员会\",\"realCapi\":\"\",\"realCapiItems\":{\"investType\":\"机关法人登记证\",\"realCapiDate\":\"\",\"realCapi\":\"\"},\"shouldCapi\":\"52910万\",\"invertName\":\"2008年06月20日\",\"stockType\":\"机关法人\",\"shouldCapiItems\":null}],\"belongOrg\":\"辽宁省工商行政管理局\",\"domain\":\"http://www.dtgroup.cn/\",\"name\":\"东北特殊钢集团有限责任公司\",\"employees\":[{\"jobTitle\":\"董事\",\"name\":\"张洪坤\",\"seqNo\":0}],\"startDate\":1171468800000,\"hash\":\"东北特殊钢集团有限责任公司\",\"status\":\"存续（在营、开业、在册）\",\"lastUpdateTime\":\"2017-06-19 13:48:38\"}}]}}".replaceFirst("hits", "result");
        Result result = JSON.parseObject(parser, Result.class);
        System.out.println(result);

    }


    @Test
    public void boost() {
        QueryConstructor constructor = new QueryConstructor();
        constructor.should(new QueryBuilders().fuzzy("product_name", "口罩"));
        constructor.should(new QueryBuilders().fuzzy("register", "口罩"));
        Iterable<Instrument> search = elasticsearchTemplate.search(constructor, Instrument.class);
        System.out.println(JSON.toJSONString(search));
    }

    @Test
    public void testMapping() {
        elasticsearchTemplate.putMapping(OneTest.class);
        elasticsearchTemplate.putMapping(Phone.class);
        OneTest one = new OneTest();
        one.setIdS("1");
        one.setName("testName");
        Phone phone = new Phone();
        phone.setNum("18600180044");
        phone.setType("yd");
        phone.setName("testName");
        phone.setOneId("1");
        elasticsearchTemplate.save(phone);
        elasticsearchTemplate.save(one);
    }

}
