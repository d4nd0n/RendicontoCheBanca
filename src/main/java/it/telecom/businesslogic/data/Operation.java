package it.telecom.businesslogic.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    String idOp;
    double idDoc;
    String data;
    double uscite;
    double entrate;
    String operazione;
    String controparte;
    String causale;
    double importo;
    String divisa;
    String commissioni;

    public static String OPERAZIONE = "Operazione";
    public static String CAUSALE = "Causale";
    public static String CONTROPARTE = "Controparte";
    public static String IMPORTO = "Importo";
    public static String DIVISA = "Divisa";
    public static String COMMISSIONI = "Commissioni";

    public void print() {
        System.out.println("OP - " + this.idOp + ": ");
        System.out.println(
                String.format("DOC: %s, DATA: %s, USCITE: %f, ENTRATE: %f, OPERAZIONE: %s, CONTROPARTE: %s, CAUSALE: %s, IMPORTO: %f, DIVISA: %s, COMMISSIONI: %s",
        this.idDoc,this.data,this.uscite,this.entrate,this.operazione,this.controparte,this.causale,this.importo,this.divisa,this.commissioni));
    }

    public List<String> getOpList() {
        List<String> outList = new ArrayList<>();
        outList.add(String.valueOf(this.idDoc));
        outList.add(this.data);
        outList.add(this.uscite==0.0?String.valueOf(this.uscite):"0.0");
        outList.add(this.entrate==0.0?String.valueOf(this.entrate):"0.0");
        outList.add(this.operazione);
        outList.add(this.controparte);
        outList.add(this.causale);
        outList.add(this.importo==0.0?String.valueOf(this.importo):"0.0");
        outList.add(this.divisa);
        outList.add(this.commissioni);
        return outList;
    }
}
