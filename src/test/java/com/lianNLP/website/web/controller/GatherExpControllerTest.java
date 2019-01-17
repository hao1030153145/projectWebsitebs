package com.lianNLP.website.web.controller;

import com.jeeframework.testframework.AbstractSpringBaseControllerTest;
import com.jeeframework.util.json.JSONUtils;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 采集体验接口单元测试
 *
 * @author haolen
 * @version 1.0 2018-11-20 11:22
 */
public class GatherExpControllerTest extends AbstractSpringBaseControllerTest {


    @Test
    @Rollback(value = false)
    public void getUrlCrawlDataTest() throws Exception {
        String requestURI = "/gatherExp/getUrlCrawlDataList.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .param("crawlUrl", "http://www.sohu.com/a/277965686_100191067")//http://www.sohu.com/c/8/1461
                        //http://www.sohu.com/a/278017509_267106
                        .param("isDepth", "0")
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 0);
    }

    @Test
    @Rollback(value = false)
    public void downloadCrawlDataTest() throws Exception {
        String requestURI = "/gatherExp/downloadCrawlData.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.get(requestURI)
                        .param("testInLogin", "no")
                        .param("bodyData", "{\n" +
                                "\t\"data\": [\n" +
                                "\t\t[\"2018-11-28 08:21\", \"赌输的金立：主要债权人讨论破产重整，小供应商希望直接清算 \", \"原标题：赌输的金立：主要债权人讨论破产重整，小供应商希望直接清算曾经风光一时的国产手机品牌金立已经倒下，现在各方开始收拾残局。 近日，隐匿香港十多个月的金立董事长刘立荣接受媒体采访，称自己已经从金立董事会出局，目前金立的重组思路是破产重整。 与此同时，金立其他股东推进的重整计划正在紧锣密鼓地展开。11月23日，金立金融债权人会议在深圳召开，据悉会上所有银行都支持了破产重整方案。11月28日，金立还将召开经营债权人会议，就金立破产重整方案进行讨论，金立给与会者设置了8000万元债务以上的债权人门槛。 如果两次会议能够达成共识，那么接下来，金立将向法院提交破产重整方案，金立的股东、债权人有关各方可以继续推进金立破产重整。 但并非是所有的债权人都愿意推进金立的破产重整。不少小供应商更愿意推进金立的破产清算，拿到真金白银的现金赔偿，以帮助公司渡过眼下的难关。 11月20日，近20家金立供应商在经过长达几个月的讨债无果后，向深圳中院提交对金立进行破产重整的申请。 刘立荣到底赌输了多少钱去年底，手机圈内风传金立董事长刘立荣在海外赌博输掉几个亿。今年初，欧菲光等金立供应商相继对向法院申请冻结金立的资产，停止向金立供货，金立资金链危机爆发。 有关刘立荣海外赌博到底输掉多少钱，至今是个谜。近期，有报道援引一位接近金立股东人士的说法称，刘立荣在赌博上输了超过100亿，甚至，在塞班岛上，一把牌输掉了7亿美元。 近日，刘立荣在接受证券时报采访时承认自己有参与到塞班赌博，但未透露具体输掉多少钱。他自己称，挪用公司资金在十几个亿。 刘立荣说话慢条斯理，善于长远布局，对人和气友善。但另一面刘立荣又酷爱围棋，喜欢冒险、博弈。 一位金立供应商何国庆（化名）则对澎湃新闻（www.thepaper.cn）记者表示，外界推测刘立荣赌博输掉上百亿的说法是靠谱的，“如果只输掉十几个亿的话，不至于公司垮掉，那么大摊子，周转十几个亿是没有问题的。” 一位不愿意透露姓名的行业分析师对澎湃新闻记者分析，赌博输掉多少，只有刘立荣自己清楚，他自己也承认生活上有些公私不分，挪用资金多少要看第三方出具的报表，金立自己提供的报表都不一定可信，“我听到的传闻是输掉60多亿。” 财务状况到底如何？在刘立荣的口中，金立资金链危机并非是单纯自己赌博输钱造成的，而是公司连续多年亏损。 刘立荣在接受证券时报采访称，从在2013年到2015年，金立平均起来每个月亏损不低于1个亿，到2016年和2017年每个月亏损不低于2个亿。那就意味着，从2013年到2017年底，金立累计亏损就有约80亿。 对于这一说法，金立的另一位供应商李明（化名）对澎湃新闻记者表示，完全不能认同，“说不挣钱是可以的，但说亏损那么多不太可能，金立与乐视不同，乐视是一台机子成本2000元，卖出去1500元，卖得多亏得多，但金立每台机器是有几百元毛利的。” 上述行业分析师也表示，金立一直是很保守的风格，在运营中一直以利润考核为先，不会长期做亏本生意的。“即便2016年、2017年金立投入相对活跃的时期，与OPPO和vivo相比，金立的投入要小得多”，“如果他这样算的话，可能是把自己赌博输掉的钱分摊到之前的年报中去了，去补这个窟窿。” 不过，从2009年起，在金立东莞工厂负责生产副总裁李三保（已经离职）在接受证券时报采访时透露，公司从功能机进入智能手机后，情况并不理想，“感受上，公司每年都很辛苦，我听到盈利的年份不多。”但李三保也表示，自己也没有看过公司报表。 不仅李三保未看过报表，很多金立的小股东也没有看过金立的报表。“我知道很多小股东都没看过报表。”李明称。 刘立荣称，金立的债务大概在170亿元左右。其中包括银行债权人债务约100亿元，上游供应商约50亿元，广告供应商约20亿元。 日前，有报道披露的金立主要资产及抵押情况表中，在2017年12月31日之前，金立的总资产和总负债约人民币201.2亿和281.7亿，净负债已高达80.5亿元。当年应收款项中，有14.3亿元为控股股东刘立荣款项。 刘立荣称，根据德勤出具的报告，金立旗下微众银行股权、南粤银行股权、金立大厦、金立工业园还有一些其他房产和对外投资股权等资产价值100亿元，这些还没有计算一些应收账款以及金立的无形资产继续运作。这个方案思路就是要用3到5年的时间运营资产升值，来100%偿还债权人的债务。 刘立荣称，用三五年时间全额偿债是金立能做到的，也是他现在最大的心愿。 不过，何国庆对刘立荣很多做法提出质疑。“2018年1月6日，他们把金立创投公司改个名字送给别人，这个是在逃避供应商的债务。” 天眼查信息显示：2018年1月09日，深圳市金立创新投资有限公司更名为深圳市新基地创新投资有限公司，原本担任公司董事长的刘立荣退出，吴昊天接替出任更名后新公司董事长。 “这些事情我们比较关心的，你把我们的钱都弄走了，这不是骗供应商吗？”何国庆称，金立共有400家供应商，欠债在70亿-80亿元。 刘立荣在今年1月份接受记者采访时称，金立资金链问题爆发的主要原因是2016年和2017年营销费用和投资费用投入超限，2016年至2017年金立营销费用投入60多亿元。 李明则质疑刘立荣这一说法，“这个都可以去供应商那里去查的，哪里需要60亿-70亿元的，这个都是假的，吹牛的。” 破产重整还是清算？目前，金立的重组顾问为深圳富海银涛资产管理股份有限公司（富海银涛），这家公司在资本重组方面具有足够的经验。 据一财报道，富海银涛董事长武捷思曾任广东省省长助理等职务，在1999年被委派至香港负责对当时负债高达35.85亿美元的粤海集团债务重组的案例时，有一段近似化腐朽为神奇的故事。他也因为在任粤海集团董事长3年时间里，把当时破产边缘的粤海系起死回生而闻名。可谓是明星团队来操盘金立重整。富海银涛提出的供讨论的债务重整思路草稿中写到，原股东将放弃一切权益，金立归全体债权人所有；债权人方面，有抵押物的债权人保留债权、抵押物不变，未付利息转为新贷款本金，无抵押债权人进行债转股，小额债权人保留债权；管理团队负责恢复一定规模的生产和销售。同时不放弃引入战略投资人的机会。 不过，上述两位中小供应商都不认可这份重整草案。“同为债权人，为何部分债转股，而部分依然保留债权？未付利息还可以转成本金？”李明称，并非完全不同意债转股，要转股的话大家一起转股。 “说重整以后继续从事手机业务，对前途不看好，这点我们也不认可。另外，金立创投最少有20亿项目在投，也没有被算进重组里面，还有很多关联公司的债务，不会有那么多债务，我们认为是虚拟债务。”何国庆称，他不能同意富海银涛的重整草案，需要把手机渠道商应收款在内等账务全部拿出来，进一步明确金立的账务。 李明称，很多供应商呼吁对核对渠道商的手机出货量，来查清真实的应收款，以防止当中有猫腻。 上述行业分析师则表示，与银行债权人和上市公司供应商这种有实力的债权人而言，中小供应商更倾向于金立还款形式来偿还债务，“今年下半年手机供应链情况急转直下，非常冷，年底了，供应商需要钱，保持估计这轮10%左右的供应商撑不过去。” 李明称，刘立荣和金立的CFO何大兵挪用公司资金的行为都是违法的，“但需要在重组过程中把他们违法的证据拿出来。那时候供应商才能集体起诉。”返回搜狐，查看更多责任编辑：\", \"http://www.sohu.com/a/278245585_260616\"]\n" +
                                "\t],\n" +
                                "\t\"title\": [\"publishTime\", \"title\", \"content\", \"url\"]\n" +
                                "}")
                        .param("type", "txt")
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(JSONObject.fromObject(response).getInt("code") == 0);
    }

    @Test
    @Rollback(value = false)
    public void textAnalysisTest() throws Exception {
        String requestURI = "/gatherExp/textAnalysis.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .param("textData", "大家好，我是来自外星系的游戏指南，很高兴又和大家见面啦！今天要为大家说的是一名自行发明绝地求生“外挂”，因为其功能是在是太特殊了，被一些嫉妒的玩家举报后，蓝洞经查明却不敢封号的事，究竟是什么样的外挂让蓝洞都不敢惩罚呢？让我们接着一起往下看吧。"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 0);
    }

    @Test
    @Rollback(value = false)
    public void saveRequireTest() throws Exception {
        String requestURI = "/gatherExp/saveRequire.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .param("require", "我想要抓取汽车之家的数据，可以吗?")
                        .param("nickName", "皓月紫光")
                        .param("phone", "13628167338")
                        .param("weixin", "YH1030153145")
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 0);
    }

    @Test
    @Rollback(value = false)
    public void getRequireTest() throws Exception {
        String requestURI = "/gatherExp/getRequire.json";
        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.post(requestURI)
                        .param("testInLogin", "no")
                        .param("page", "1")
                        .param("size", "10")
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(JSONUtils.isJSONValid(response));
        assertTrue(JSONObject.fromObject(response).getInt("code") == 0);
    }

}