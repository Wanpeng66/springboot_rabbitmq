package com.wp.word;

import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author: wp
 * @Title: SpBoRepositoryTest
 * @Description: TODO
 * @date 2019/3/26 21:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpBoRepositoryTest {

    @Autowired
    WordUtil wordUtil;

    @Autowired
    SpBoRepository spBoRepository;

    @Test
    public void test() throws Exception {
        List<SpBO> all = spBoRepository.findAll();
        List<Map<String,String>> docs = new ArrayList<>(  );
        for(SpBO spBO:all){
            Map<String,String> map = new HashMap(  );
            map.put( "PID",spBO.getSPID() );
            map.put( "NAME",spBO.getSPMC() );
            map.put( "B",spBO.getBZXT() );
            map.put( "Z",spBO.getZXGG() );
            map.put( "C",spBO.getCPGG() );
            map.put( "X",spBO.getBZQ() );
            map.put( "D",spBO.getCD() );
            map.put( "J",spBO.getCCTJ() );
            map.put( "Y",spBO.getYSBZ() );
            docs.add( map );

        }

        Map<String,Object> map = new HashMap(  );
        map.put( "sps",docs );
        Template freemarkerTemplate = wordUtil.getConfiguration().getTemplate("22.ftl");
        wordUtil.createDoc( map,freemarkerTemplate,"D:/learning/test.doc" );



    }

}