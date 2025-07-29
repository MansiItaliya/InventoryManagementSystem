package com.myproject.BillGenerateSys.Service;

import com.myproject.BillGenerateSys.Constant.ErrorCons;
import com.myproject.BillGenerateSys.Constant.MessageCons;
import com.myproject.BillGenerateSys.Constant.otherCons;
import com.myproject.BillGenerateSys.ExceptionHandler.IOrelatedException;
import com.myproject.BillGenerateSys.Model.Bill;
import com.myproject.BillGenerateSys.Model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CsvGenerateService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public String generateProductCsv(List<Product> products){
        String filePath = otherCons.FILEPATH_PRO;
        String[] header = {otherCons.PRODUCT_ID, otherCons.PRODUCT_NAME, otherCons.QUANTITY, otherCons.PRICE, otherCons.GST, otherCons.THRESHOLD};
        logger.info(MessageCons.START_PRODUCT_CSV);
        return writeCsv(products, header, filePath);
    }

    public String generateBillCsv(List<Bill> bills, String filePath){
        String[] header = {otherCons.BILL_ID, otherCons.BILL_DATE, otherCons.TOTAL_AMT, otherCons.BILL_GST, otherCons.FINAL_AMT, otherCons.PAYMENT_CONFIRMED};
        logger.info(MessageCons.START_BILL_CSV);
        return writeCsv(bills, header, filePath);
    }

    public <T>String writeCsv(List<T> items,String[] headers,String filePath){
        try {
            ICsvBeanWriter writer = new CsvBeanWriter(new FileWriter(filePath), CsvPreference.STANDARD_PREFERENCE);

            writer.writeHeader(headers);

            for (T item : items) {
                writer.write(item, headers);
            }
            writer.close();
            logger.info(MessageCons.CSV_SUCCEED);
            return filePath;
        }catch (IOException e){
            logger.error(ErrorCons.WRITE_CSV);
            throw new IOrelatedException(ErrorCons.WRITE_CSV);
        }
    }
}

