package io.github.kingschan1204.istock.module.maindata.ctrl;

import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockHisDividend;
import io.github.kingschan1204.istock.module.maindata.repository.StockRepository;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *
 * @author chenguoxiang
 * @create 2018-03-27 15:19
 **/
@Controller
public class StockPageCtrl {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;
        @RequestMapping("/stock/his_dy/{code}")
        public ModelAndView hisdata(@PathVariable String code, Model model){
            ModelAndView mav = new ModelAndView("his_dy");
            mav.addObject("year","''");
            mav.addObject("percent","0");
            Stock stock =stockRepository.findOne(code);
            if(null==stock){
                mav.addObject("msg",String.format("代码:%s",code,"不存在，或者不属于A股代码!"));
                return mav;
            }
            mav.addObject("stock",stock);
            //历年分红
            List<StockHisDividend> list =stockService.getStockDividend(code);
            if(null!=list){
                StringBuffer year = new StringBuffer();
                StringBuffer percent = new StringBuffer();
                list.stream().forEach(item ->{
                    if(item.getPercent()>0){
                        percent.append(item.getPercent()).append(",");
                        year.append("'").append(item.getTitle()).append("',");
                    }

                });
                String data= String.format("%s|%s",year.toString().replaceAll("\\,$",""),
                        percent.toString().replaceAll("\\,$","")
                );
                String item[]=data.split("\\|");
                mav.addObject("year",item[0]);
                mav.addObject("percent",item[1]);
                mav.addObject("rows",list);
            }else{
                mav.addObject("msg","该股票没有分红信息!");
            }

            /*//历年roe
            data=stockService.getStockHisRoe(code);
            String roeItem[]=data.split("\\|");
            mav.addObject("roe_year",roeItem[0]);
            mav.addObject("roe_percent",roeItem[1]);

            //历年pb
            data=stockService.getStockHisPb(code);
            String pbItem[]=data.split("\\|");
            mav.addObject("pb_date",pbItem[0]);
            mav.addObject("pb_value",pbItem[1]);

            //历年pe
            data=stockService.getStockHisPe(code);
            String peItem[]=data.split("\\|");
            mav.addObject("pe_date",peItem[0]);
            mav.addObject("pe_value",peItem[1]);*/
            return  mav;
        }


}
