package it.telecom.businesslogic.service;

import it.telecom.businesslogic.data.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Value("${env_var1}")
    String env1;

    @Value("${env_var2}")
    String env2;

    @Override
    public String getHello() {
        return "hello";
    }

    @Override
    public String getMessage(String s) {
        return s;
    }

    @Override
    public String getHelloEnv() {
        return "hello " + env1 + " " + env2;
    }

    @Override
    public File createCheBancaDB(String fileString) {
        File out = new File("");
        try {
            OPCPackage pkg = OPCPackage.open(new File("C:\\bx\\Bank\\Rendiconto.xlsx"));

            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            XSSFSheet sheet = wb.getSheetAt(0);
            HashMap<String, Operation> opMap = selectRowsFromSheet(sheet);

            makeNewFileFromOp(opMap);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return out;
    }

    private void makeNewFileFromOp(HashMap<String, Operation> opMap) throws IOException {
        // New file
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Database");

        // First row
        XSSFRow row;
        Set<String> keyid = opMap.keySet();
        int rowid = 0;
        XSSFRow firstRow = spreadsheet.createRow(rowid++);
        Cell c1 = firstRow.createCell(0);
        c1.setCellValue("ID_DOC");
        Cell c2 = firstRow.createCell(1);
        c2.setCellValue("DATA");
        Cell c3 = firstRow.createCell(2);
        c3.setCellValue("USCITE");
        Cell c4 = firstRow.createCell(3);
        c4.setCellValue("ENTRATE");
        Cell c5 = firstRow.createCell(4);
        c5.setCellValue("OPERAZIONE");
        Cell c6 = firstRow.createCell(5);
        c6.setCellValue("CONTROPARTE");
        Cell c7 = firstRow.createCell(6);
        c7.setCellValue("CAUSALE");
        Cell c8 = firstRow.createCell(7);
        c8.setCellValue("IMPORTO");
        Cell c9 = firstRow.createCell(8);
        c9.setCellValue("DIVISA");
        Cell c10 = firstRow.createCell(9);
        c10.setCellValue("COMMISSIONI");
        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Operation op = opMap.get(key);
            int cellid = 0;

            for (String obj : op.getOpList()) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue(obj);
            }
        }
        FileOutputStream fOut = new FileOutputStream(
                new File("C:/bx/Bank/db.xlsx"));
        workbook.write(fOut);
        fOut.close();
    }

    HashMap<String, Operation> selectRowsFromSheet(XSSFSheet sheet) {
        HashMap<String, Operation> outMap = new HashMap<>();
        XSSFRow row;
        XSSFCell cell;
        int rows; // No of rows

        int cols = 0; // No of columns
        int tmp = 0;

        rows = sheet.getPhysicalNumberOfRows();
        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for (int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if (tmp > cols) cols = tmp;
            }
        }
        Operation op = new Operation();
        List<XSSFRow> tmpRows = new ArrayList<>();
        for (int r = 1; r < rows; r++) {
            row = sheet.getRow(r);
            if (row != null) {
                for (int c = 0; c < cols; c++) {
                    cell = row.getCell((short) c);
                    //1-ID,2-DATA,3-USCITE,4-ENTRATE,5-DESCRIZIONE
                    if (c == 1) {
                        if (cell != null) {
                            if (!tmpRows.isEmpty()) {
                                op = makeOperationFromRows(tmpRows, cols, outMap);
                                outMap.put(op.getIdOp(), op);
                                tmpRows.clear();
                                tmpRows.add(row);
                            } else {
                                tmpRows.add(row);
                            }
                        } else {
                            tmpRows.add(row);
                        }
                    }
                }
            }
        }
        return outMap;
    }


    Operation makeOperationFromRows(List<XSSFRow> rows, int cols, HashMap<String, Operation> outMap) {
        Operation out = new Operation();
        out.setIdOp(String.valueOf(outMap.size()));
        XSSFCell cell;
        for (XSSFRow row : rows) {
            if (row != null) {
                for (int c = 0; c < cols; c++) {
                    cell = row.getCell((short) c);
                    //1-ID,2-DATA,3-USCITE,4-ENTRATE,5-DESCRIZIONE
                    switch (c) {
                        case 0:
                            if (out.getIdDoc() == 0 && cell != null) {
                                out.setIdDoc(cell.getNumericCellValue());
                            }
                            break;
                        case 1:
                            if (out.getData() == null && cell != null) {
                                out.setData(cell.getStringCellValue());
                            }
                            break;
                        case 2:
                            if (out.getUscite() == 0 && cell != null) {
                                String tmp = cell.getStringCellValue().replace(",", ".");
                                tmp = tmp.replace("00", "");
                            }
                            break;
                        case 3:
                            if (out.getEntrate() == 0 && cell != null) {
                                String tmp = cell.getStringCellValue().replace(",00", "");
                                tmp = tmp.replace(",", ".");
                                out.setEntrate(Double.parseDouble(tmp));
                            }
                            break;
                        case 4:
                            if (cell != null) {
                                String descr = cell.getStringCellValue();
                                out = getOpFromDescr(out, descr);
                            }
                            break;
                    }
                }
            }
        }
        out.print();
        return out;
    }

    Operation getOpFromDescr(Operation op, String descr) {
        HashMap<String, String> outMap = new HashMap<>();
        HashMap<String, String> map = manageDescription(descr);
        op.setOperazione(op.getOperazione()==null?map.get(Operation.OPERAZIONE):op.getOperazione());
        op.setControparte(op.getControparte()==null?map.get(Operation.CONTROPARTE):op.getControparte());
        op.setCausale(op.getCausale()==null?map.get(Operation.CAUSALE):op.getCausale());
        op.setImporto(op.getImporto()==0.0?
                map.get(Operation.IMPORTO)!=null?Double.parseDouble(map.get(Operation.IMPORTO)):0.0
                :op.getImporto());
        op.setDivisa(op.getDivisa()==null?map.get(Operation.DIVISA):op.getDivisa());
        op.setCommissioni(op.getCommissioni()==null?map.get(Operation.COMMISSIONI):op.getCommissioni());
        return op;
    }

    HashMap<String, String> manageDescription(String descr) {
        HashMap<String, String> outMap = new HashMap<>();
        descr = descr.replace("\n", " ");
        descr = descr.replace(".", "");
        if(!isLongDescr(descr)) {
            if (descr.contains(Operation.OPERAZIONE + ": ")) {
                String[] strings = descr.split(": ");
                outMap.put(Operation.OPERAZIONE, strings[1]);
            }
            if (descr.contains(Operation.CONTROPARTE + ": ")) {
                String[] strings = descr.split(": ");
                outMap.put(Operation.CONTROPARTE, strings[1]);
            }
            if (descr.contains(Operation.CAUSALE + ": ")) {
                String[] strings = descr.split(": ");
                outMap.put(Operation.CAUSALE, strings[1]);
            }
            if (descr.contains(Operation.IMPORTO + ": ")) {
                String[] strings = descr.split(": ");
                String tmp = strings[1].replace(",", ".");
                outMap.put(Operation.IMPORTO, tmp);
            }
            if (descr.contains(Operation.DIVISA + ": ")) {
                String[] strings = descr.split(": ");
                outMap.put(Operation.DIVISA, strings[1]);
            }
            if (descr.contains(Operation.COMMISSIONI + ": ")) {
                String[] strings = descr.split(": ");
                String tmp = strings[1].replace(",", ".");
                outMap.put(Operation.COMMISSIONI, strings[1]);
            }
        } else {
            outMap.put(Operation.OPERAZIONE, "DESCRIZIONE LUNGA");
        }
        return outMap;
    }

    Boolean isLongDescr(String descr) {
        return descr.contains(Operation.OPERAZIONE) && descr.contains(Operation.CONTROPARTE)
                || descr.contains(Operation.CONTROPARTE) && descr.contains(Operation.CAUSALE)
                || descr.contains(Operation.CAUSALE) && descr.contains(Operation.IMPORTO)
                || descr.contains(Operation.IMPORTO) && descr.contains(Operation.DIVISA)
                || descr.contains(Operation.DIVISA) && descr.contains(Operation.COMMISSIONI);
    }
}