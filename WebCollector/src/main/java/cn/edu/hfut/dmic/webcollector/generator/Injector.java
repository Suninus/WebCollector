/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.hfut.dmic.webcollector.generator;


import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.util.Config;
import cn.edu.hfut.dmic.webcollector.util.Task;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



/**
 *
 * @author hu
 */
public class Injector extends Task{
    
    String crawl_path;
    public Injector(String crawl_path){
        this.crawl_path=crawl_path;
    }
    
    public void inject(String url) throws IOException{
        inject(url,false);
    }
    
    public void inject(ArrayList<String> urls) throws IOException{
        inject(urls,false);
    }
    
    public void inject(String url,boolean append) throws IOException{
        ArrayList<String> urls=new ArrayList<>();
        urls.add(url);
        inject(urls,append);
    }
    
    public boolean hasInjected(){
        String info_path=Config.current_info_path;
        File inject_file=new File(crawl_path,info_path);
        return inject_file.exists();
    }
    
    
    
    public void inject(ArrayList<String> urls,boolean append) throws UnsupportedEncodingException, IOException{
         
        
        String info_path=Config.current_info_path;
        File inject_file=new File(crawl_path,info_path);
        if(!inject_file.getParentFile().exists()){
            inject_file.getParentFile().mkdirs();
        }
        DbWriter<CrawlDatum> writer;
        if(inject_file.exists())
            writer=new DbWriter<CrawlDatum>(CrawlDatum.class,inject_file,append);
        else
            writer=new DbWriter<CrawlDatum>(CrawlDatum.class,inject_file,false);
        for(String url:urls){
            CrawlDatum crawldatum=new CrawlDatum();
            crawldatum.setUrl(url);
            crawldatum.setStatus(CrawlDatum.STATUS_DB_UNFETCHED);
            writer.write(crawldatum);                    
        }
        writer.close();
        
        
        
        
        
    }
    
    
    
    public static void main(String[] args) throws IOException{
        Injector inject=new Injector("/home/hu/data/crawl_avro");
        inject.inject("http://www.xinhuanet.com/");
    }
}
