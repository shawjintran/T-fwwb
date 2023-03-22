package com.t.medicaldocument;


import com.t.medicaldocument.entity.Bo.EsDocumentBo;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.service.impl.SearchServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.core.TimeValue;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
@MapperScan("com.t.medical.medicaldocument.mapper")
@Slf4j
public class EsSave {
    @Autowired
    SearchServiceImpl searchService;
    //数据导入
    @Test
    void testBulkRequest(){


        ArrayList<EsDocumentBo> esDocumentBos=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            EsDocumentBo esDocumentBo = new EsDocumentBo();
            // esDocumentBo.setPdfdescid(1000l+i);
            // esDocumentBo.setPdfid(120l-1);
            // esDocumentBo.setPdfpage(1);
            //esDocumentBo.setAll("[\"（摘要自的探讨三维标测系统和单环状标测导管指示下环肺静脉线性消融电学隔离肺静的可行性和有效性。方法2004年5月至2004年11月间我院对68例症状明显、发作频繁、抗心律失常药物治疗无效的心房颤动（房颤）患者进行了在Carto(n=56)或EnSiteNavX（n=12）和单环状标测早管指示下的环肺静脉线性消融肺静脉电隔离术。收集操作过程中的相关数据。计算初始肺静脉电学隔离率、最终肺静脉电学隔离率及并发症资料。结果68例惠者共计完成136个环形消融线，操作时可平均为（240土65）m×线曝光时间平均为（37土12）mm用于左心房重建和环肺静脉线性消融的放电时间分别为（20土9）min和（62土24）min在完成预定环肺静脉消融线后，初始肺静脉电学隔离率为50.7%（6936）经寻找缝隙补充消融后最终肺静脉电学隔离率为95.6%（130136。70.2%（5984)的缝隙分布于左侧，29.8%（2584）见于右侧。并发症包括1例心脏压塞和2例锁骨下及左胸部皮下血肿，均经保守治疗康复，无肺静脉狭窄。结论三维标测系统加单环状标测导管指示下环肺静脉线生消融电学隔离肺静脉成功率高、并发症率低，操作时间及X线曝光时间可以接受。关键词小心房动：肺静脉：消融\",\"m appng systam and singe circuar mapping catheter in patients with atrial fibrillation DONG Jianzeng MA Chang sheng LU Xing peng IONGDe yong WANG Jing LU Xiao qing Deparim ent ofCardiologyBeijing Anzhen Hopital CapitalUn iversity ofMed ical S cien ces B eijing 100029 Ch ina【Abstract Objective To investigate the feasibility and efficacy ofpuh onary vein(PV) isolation withcircum ferential PV linear ablation gu ided by 3 d m ensionalm app ing sy stem and single circu larmapp ing ca theteiin patien tsw ith atrial fibrillation(AF). Methods From M ay 2004 to November 2004 circum feren tial PV linear ab ation guided by Carto sy stem orEnS ite NavX systm was perfom ed in 68 consecu tive h ighly symp tom aticpatients (pts) with drug refractory AF. Data associated w ith procedure w as collected and initial PV isolationrate cumu lative Pv isolation rate and cm plication rate were statistically analyzed Results Circum ferentiaPV linear ab lation was conducted in 68 pts up to 136 linear circles were produced around each jpsiateralPV sM ean p rocedure time and fluo ro scopy tm e w ere( 240 ±65)m inu tes and ( 37±12)m inutes respectively M eanduration of eft atrium gem etry reconstruction and radiofrequency energy delivery w as( 20 ±9) m inutes and62±24)m inu tes respectiveky A fter ablation at predetem ined circu lar line 50. 7% (69 /136) of ipsilateralPVs w ere isolated sm ultaneously A fter additional ablation applied at gaps 95. 6% ( 130 /136) of cumu lativePV isolation rate was ach ieved Seventy point tw opercenf 59 84) of the gaps were distributed in eft sided ablation lines 30. 0% (25 84) in right sided lines 0ne cardiac tam ponade and 2 subclavian hem atm a associatedw ith the procedurewere observed No Pv stenosis was found Conclusions CircumferentialPv linear ab atiorguided by 3 dm ensionalm apping sy stem and single circu larm apping catheter could be applied for AF pts w itha h igh PV isolation ra te a bwer complication rate and an acceptable procedure and fluoro scopy tmeKey w ordsA trial fibrillation Pu m onary vein Ab lation\",\"马长生刘兴鹏龙德勇王京刘小青\",\"心房颜动\"]");
            esDocumentBo.setPdfpicurl("http//pdf的url.pdf");
            esDocumentBo.setCreatetime("2023-02-15");
            esDocumentBo.setText("这是正文,");
            esDocumentBo.setTitle("[\"讨论\"]");
            esDocumentBo.setFigure("这是图片的内容");
            esDocumentBo.setFigure_caption("这是图片标题");
            esDocumentBo.setTable("这是表格内容");
            esDocumentBo.setTable_caption("这是表格标题");
            esDocumentBo.setHeader("这是页头");
            esDocumentBo.setFooter("这是页尾");
            esDocumentBo.setReference("这是引用");
            esDocumentBo.setEquation("这是公式");
            esDocumentBo.setAll();
            esDocumentBos.add(esDocumentBo);
        }

        System.out.println("内容");
        esDocumentBos.forEach(System.out::println);

        boolean b = searchService.save2ES(esDocumentBos);

        System.out.println(b);


    }




}
